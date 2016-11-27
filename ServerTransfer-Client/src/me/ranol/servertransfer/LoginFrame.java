package me.ranol.servertransfer;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import me.ranol.servertransfer.packet.LoginPacket;
import me.ranol.servertransfer.swtutils.MessageView;

public class LoginFrame {
	protected Shell shell;
	public static Text host;
	public static Text id;
	public static Text pwd;

	public static void main(String[] args) {
		try {
			LoginFrame window = new LoginFrame();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void open() {
		try {
			Display display = Display.getDefault();
			createContents();
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		shell = new Shell(SWT.TITLE | SWT.CLOSE | SWT.MIN);
		shell.setImage(SWTResourceManager.getImage(LoginFrame.class, "/image/icon.png"));
		shell.setSize(600, 450);
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		shell.setText("Login");
		shell.setSize(300, 300);

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(0, 0, 294, 271);

		Label label = new Label(composite, SWT.NONE);
		label.setText("Login");
		label.setFont(SWTResourceManager.getFont("맑은 고딕", 22, SWT.BOLD));
		label.setBounds(10, 10, 77, 40);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText("Host : ");
		label_1.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		label_1.setBounds(10, 77, 52, 28);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText("ID : ");
		label_2.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		label_2.setBounds(10, 111, 52, 28);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText("Password : ");
		label_3.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		label_3.setBounds(10, 145, 77, 28);

		host = new Text(composite, SWT.BORDER);
		host.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		host.setBounds(92, 78, 150, 28);

		id = new Text(composite, SWT.BORDER);
		id.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		id.setBounds(92, 112, 150, 28);

		pwd = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		pwd.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		pwd.setBounds(93, 146, 150, 28);

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent eeeeeee) {
				button.setEnabled(false);
				button.setText("Login..");
				new ClientManagement(LoginFrame.getHost());
				LoginPacket packet = new LoginPacket(LoginFrame.getId(), LoginFrame.getPassword());
				try {
					String result = ClientManagement.staticClient.sendPacket(packet);
					if (result != null) {
						MessageView cant = MessageView.info(shell).title("로그인 불가");
						switch (result) {
						case "ACC":
							cant.message("서버에 등록된 Id가 아닙니다.").open();
							break;
						case "PWD":
							cant.message("비밀번호가 일치하지 않습니다.").open();
							break;
						case "ALA":
							cant.message("이미 로그인된 계정입니다.").open();
							break;
						case "WTF":
							cant.message("알 수 없는 이유로 거절되었습니다.").open();
							break;
						default:
							ClientManagement.staticClient.setUUID(result);
							ClientManagement.staticClient.connectReciever();
							MessageView.info(shell).message("등록된 계정입니다.\n다시 오신 것을 축하합니다.").title("로그인").open();
							shell.dispose();
							ServerTransfer st = new ServerTransfer();
							st.open();
						}
					}
				} catch (SocketTimeoutException e) {
					MessageView.info(shell).message("서버 연결에 실패했습니다.").title("로그인 불가").open();
				} catch (IOException e) {
					e.printStackTrace();
				}
				button.setEnabled(true);
				button.setText("Login");
			}
		});
		button.setText("Login");
		button.setFont(SWTResourceManager.getFont("맑은 고딕", 11, SWT.NORMAL));
		button.setBounds(11, 179, 231, 40);
	}

	public static String getHost() {
		return host.getText();
	}

	public static String getId() {
		return id.getText();
	}

	public static String getPassword() {
		return pwd.getText();
	}

}
