package me.ranol.servertransfer;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import me.ranol.servertransfer.packet.FileListPacket;
import me.ranol.servertransfer.swtutils.SWTRun;

public class ServerTransfer {
	HashMap<String, Image> cache = new HashMap<>();
	protected static Shell shell;
	private Text text;
	private Text visible;
	private Text nPwd;
	private String directory = ".";
	static Image unknown = SWTResourceManager.getImage(ServerTransfer.class, "/image/file.png");
	static Image folder = SWTResourceManager.getImage(ServerTransfer.class, "/image/coloredFolder.png");

	static Label logs;
	private Table table;
	private Text dir;
	static Image X = SWTResourceManager.getImage(ServerTransfer.class, "/image/X.png");

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		if (shell != null) {
			shell.setVisible(true);
			return;
		}
		Display display = Display.getDefault();
		createContents();
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
		shell = new Shell(SWT.TITLE | SWT.MIN);
		shell.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/icon.png"));
		shell.setSize(600, 450);
		shell.setText("Server Transfer - Client");
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		shell.addListener(SWT.Close, new Listener() {

			@Override
			public void handleEvent(Event e) {
				ClientManagement.staticClient.logout();
				System.exit(-1);
			}
		});

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, 595, 425);

		TabItem tbtmViewLog = new TabItem(tabFolder, SWT.NONE);
		tbtmViewLog.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/log.png"));
		tbtmViewLog.setText("Logs");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmViewLog.setControl(composite_1);

		text = new Text(composite_1, SWT.BORDER);
		text.setBounds(10, 347, 450, 25);

		Button cmd = new Button(composite_1, SWT.NONE);
		cmd.setBounds(466, 345, 111, 25);
		cmd.setText("Issue Command");

		Button stop = new Button(composite_1, SWT.NONE);
		stop.setBounds(466, 314, 111, 25);
		stop.setText("Stop server");

		Button start = new Button(composite_1, SWT.NONE);
		start.setText("Start server");
		start.setBounds(466, 282, 111, 25);

		logs = new Label(composite_1, SWT.WRAP);
		logs.setBounds(10, 10, 450, 331);

		TabItem tbtmSetOption = new TabItem(tabFolder, SWT.NONE);
		tbtmSetOption.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/option.png"));
		tbtmSetOption.setText("Options");

		TabItem tbtmUserOption = new TabItem(tabFolder, SWT.NONE);
		tbtmUserOption.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/user.png"));
		tbtmUserOption.setText("User");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmUserOption.setControl(composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("맑은 고딕", 22, SWT.BOLD));
		lblNewLabel.setBounds(10, 10, 121, 46);
		lblNewLabel.setText("Settings");

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		lblNewLabel_1.setBounds(10, 62, 140, 30);
		lblNewLabel_1.setText("Visible Name : ");

		visible = new Text(composite, SWT.BORDER);
		visible.setBounds(156, 62, 140, 30);

		Button logout = new Button(composite, SWT.NONE);
		logout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ee) {
				ClientManagement.staticClient.logout();
				close();
				System.exit(-1);
			}
		});
		logout.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/logout.png"));
		logout.setFont(SWTResourceManager.getFont("맑은 고딕", 12, SWT.NORMAL));
		logout.setBounds(10, 326, 130, 46);
		logout.setText("Logout");

		Button logoutNlogin = new Button(composite, SWT.NONE);
		logoutNlogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent ee) {
				try {
					ClientManagement.staticClient.logout();
				} catch (Exception e) {
				}
				close();
				LoginFrame.reopen();
			}
		});
		logoutNlogin.setText("Logout && Login");
		logoutNlogin.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/login.png"));
		logoutNlogin.setFont(SWTResourceManager.getFont("맑은 고딕", 12, SWT.NORMAL));
		logoutNlogin.setBounds(156, 326, 180, 46);

		Label lblNewPassword = new Label(composite, SWT.NONE);
		lblNewPassword.setText("New Password : ");
		lblNewPassword.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		lblNewPassword.setBounds(10, 98, 140, 30);

		nPwd = new Text(composite, SWT.BORDER);
		nPwd.setBounds(156, 98, 140, 30);

		Button applyNick = new Button(composite, SWT.NONE);
		applyNick.setBounds(302, 62, 100, 30);
		applyNick.setText("Apply");

		Button applyPwd = new Button(composite, SWT.NONE);
		applyPwd.setText("Apply");
		applyPwd.setBounds(302, 98, 100, 30);

		TabItem tbtmFiles = new TabItem(tabFolder, SWT.NONE);
		tbtmFiles.setImage(SWTResourceManager.getImage(ServerTransfer.class, "/image/folder.png"));
		tbtmFiles.setText("Files");

		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmFiles.setControl(composite_3);

		table = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLocation(0, 35);
		table.setSize(587, 347);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);

		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		lblNewLabel_2.setBounds(0, 0, 102, 29);
		lblNewLabel_2.setText("Directory : ");

		dir = new Text(composite_3, SWT.BORDER);
		dir.setFont(SWTResourceManager.getFont("맑은 고딕", 14, SWT.NORMAL));
		dir.setBounds(108, 0, 400, 29);

		Button btnNewButton = new Button(composite_3, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				directory = dir.getText();
				updateFiles();
			}
		});
		btnNewButton.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		btnNewButton.setBounds(511, 0, 76, 29);
		btnNewButton.setText("Search");
		dir.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				openFile(table.getItem(table.getSelectionIndex()));
			}
		});
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!(e.item instanceof TabItem) || !"Files".equals(((TabItem) e.item).getText())
						|| table.getItemCount() > 0)
					return;
				updateFiles();
			}
		});

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);

	}

	void updateFiles() {
		ArrayList<String> files = (ArrayList<String>) ClientManagement.staticClient
				.sendPacket(new FileListPacket(directory));
		table.clearAll();
		if (files == null)
			return;
		if (files.get(0).equals("해당 경로가 존재하지 않습니다 :(")) {
			table.setItemCount(1);
			TableItem nope = table.getItem(0);
			nope.setImage(X);
			dir.setText(".");
			nope.setText("해당 경로가 존재하지 않습니다. [텍스트 창에서 엔터를 눌러주세요]");
			return;
		}
		table.setItemCount(files.size() + 1);
		TableItem up = table.getItem(0);
		up.setImage(folder);
		up.setText("상위 폴더로: ");
		SWTRun.runAsync(() -> {
			int idx = 1;
			for (String s : files) {
				render(s, table.getItem(idx++));
			}
		});
	}

	private void openFile(TableItem item) {
		if (item.getImage() != null && item.getImage().equals(folder)) {
			if ("상위 폴더로: ".equals(item.getText())) {
				int last = directory.lastIndexOf('\\');
				if (last > 0) {
					directory = directory.substring(0, last);
					dir.setText(directory);
					updateFiles();
				}
				return;
			}
			directory += "\\" + item.getText();
			dir.setText(directory);
			updateFiles();
		}
	}

	public void render(String s, TableItem i) {
		boolean isFolder = s.charAt(0) == 'O';
		String fn = s.substring(1);
		int last = fn.lastIndexOf('.');
		String extend = last > 0 ? fn.substring(last) : fn;
		if (!cache.containsKey(extend) && !isFolder) {
			Program p = Program.findProgram(extend);
			if (p == null || p.getImageData() == null)
				cache.put(extend, unknown);
			else
				cache.put(extend, new Image(i.getDisplay(), p.getImageData()));
		}
		i.setText(fn);
		i.setImage(isFolder ? folder : cache.get(extend));
	}

	public static void setLog(String log) {
		SWTRun.runAsync(() -> logs.setText(log));
	}
}
