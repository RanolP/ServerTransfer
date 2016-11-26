package me.ranol.servertransfer;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import me.ranol.servertransfer.packet.LogoutPacket;

public class ServerTransfer {
	protected static Shell shell;
	private Text text;
	private Text visible;
	private Text nPwd;

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
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
		shell.dispose();
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
				try {
					ClientManagement.staticClient.sendPacket(new LogoutPacket());
				} catch (IOException e) {
					e.printStackTrace();
				}
				shell.dispose();
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
					ClientManagement.staticClient.sendPacket(new LogoutPacket());
				} catch (Exception e) {
				}
				shell.dispose();
				LoginFrame frame = new LoginFrame();
				frame.open();
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

	}
}
