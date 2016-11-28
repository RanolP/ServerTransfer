package me.ranol.servertransfer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import me.ranol.servertransfer.packet.FilePacket;
import me.ranol.servertransfer.swtutils.SWTRun;

public class FileSendFrame {

	enum RunStatus {
		NOTSTART(SWTResourceManager.getImage(RunStatus.class, "/image/unknown.png")), RUNNING(
				SWTResourceManager.getImage(RunStatus.class, "/image/upload.png")), END(
						SWTResourceManager.getImage(RunStatus.class, "/image/check.png"));
		Image img = null;

		RunStatus(Image img) {
			this.img = img;
		}
	}

	protected static Shell shell;
	private Table table;
	ProgressBar all;
	ProgressBar current;
	Label lblCurrent;
	Label lblAll;
	HashMap<String, RunStatus> resources = new HashMap<>();
	int index = 0;
	long allBytes = 0;
	long currentBytes = 0;

	public FileSendFrame(String... resources) {
		for (String s : resources) {
			File f = new File(s);
			allBytes += f.getTotalSpace();
			this.resources.put(s, RunStatus.NOTSTART);
		}
	}

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		if (shell != null) {
			shell = null;
			createContents();
			return;
		}
		Display display = Display.getDefault();
		createContents();
		shell.setVisible(true);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public static void close() {
		SWTRun.runAsync(() -> shell.setVisible(false));
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.TITLE | SWT.CLOSE);
		shell.setImage(SWTResourceManager.getImage(FileSendFrame.class, "/image/icon.png"));
		shell.setSize(600, 350);
		shell.setText("파일 전송");
		shell.addListener(SWT.Close, e -> e.doit = false);

		all = new ProgressBar(shell, SWT.NONE);
		all.setBounds(10, 266, 564, 35);
		all.setMaximum(100);
		all.setMinimum(0);

		current = new ProgressBar(shell, SWT.NONE);
		current.setBounds(10, 202, 564, 35);
		current.setMaximum(100);
		current.setMinimum(0);

		lblAll = new Label(shell, SWT.NONE);
		lblAll.setBounds(10, 245, 133, 15);
		lblAll.setText("전체 진행률 [0%]");

		lblCurrent = new Label(shell, SWT.NONE);
		lblCurrent.setText("현재 진행률 [0%]");
		lblCurrent.setBounds(10, 181, 133, 15);

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(10, 10, 564, 162);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);

		table.setItemCount(resources.size());

		updateTable();
		next();
	}

	long before = 0;

	void sendFile(String file) {
		FilePacket packet = new FilePacket(file, ServerTransfer.directory);

		packet.writer().addChecker(i -> {
			System.out.println(i);
			int perc = i * 100 / packet.all();
			SWTRun.runAsync(() -> {
				current.setSelection(perc);
				lblCurrent.setText("현재 진행률 [" + perc + "%]");
			});
			if (i > before) {
				currentBytes += i - before;
				before = i;
			}
			calcAll();
		});
		new Thread(() -> {
			ClientManagement.staticClient.sendPacket(packet);
			next();
		}).start();
	}

	void calcAll() {
		SWTRun.runAsync(() -> {
			int perc = (int) (currentBytes / allBytes);
			this.all.setSelection(perc);
			lblAll.setText("전체 진행률 [" + perc + "%]");
		});
	}

	void next() {
		System.out.println("전송종료: " + index);
		if (index == resources.size()) {
			close();
			ServerTransfer.reopen();
			return;
		}
		before = 0;
		if (index != 0)
			resources.put(keys().get(index), RunStatus.END);
		resources.put(keys().get(index), RunStatus.RUNNING);
		sendFile(keys().get(index++));
		SWTRun.runAsync(() -> {
			current.setSelection(0);
			lblCurrent.setText("현재 진행률 [0%]");
		});
		updateTable();
		System.out.println("전송시작: " + index);
	}

	List<String> keys() {
		List<String> keys = new ArrayList<>();
		keys.addAll(resources.keySet());
		return keys;
	}

	void updateTable() {
		SWTRun.runAsync(() -> {
			List<String> keys = keys();
			for (int i = 0; i < resources.size(); i++) {
				TableItem item = table.getItem(i);
				String key = keys.get(i);
				item.setText(key.replaceAll(".+\\\\", ""));
				Image img = resources.get(key).img;
				if (img != null)
					item.setImage(img);
			}
		});
	}

	public static void reopen(String... resources) {
		SWTRun.runAsync(() -> {
			FileSendFrame frame = new FileSendFrame(resources);
			frame.open();
			shell.setVisible(true);
		});
	}
}
