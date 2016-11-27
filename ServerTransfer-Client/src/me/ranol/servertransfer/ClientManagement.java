package me.ranol.servertransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

import me.ranol.servertransfer.packet.ConnectPacket;
import me.ranol.servertransfer.packet.Packet;
import me.ranol.servertransfer.packet.RPackets;
import me.ranol.servertransfer.packet.RecieveablePacket;
import me.ranol.servertransfer.swtutils.MessageView;
import me.ranol.servertransfer.swtutils.SWTRun;

public class ClientManagement {
	Socket socket;
	Socket recieveOnly;
	InetSocketAddress address;
	String uuid;
	public static ClientManagement staticClient;
	boolean onSend = false;
	public String salt;

	public ClientManagement(String host) {
		staticClient = this;
		this.socket = new Socket();
		this.recieveOnly = new Socket();
		try {
			socket.setSoTimeout(3000);
			address = new InetSocketAddress(InetAddress.getByName(host), 2929);
		} catch (Exception e) {
			e.printStackTrace();
		}
		connect();
	}

	public <T> T sendPacket(Packet<T> packet) throws IOException {
		if (onSend)
			return null;
		onSend = true;
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		DataInputStream in = new DataInputStream(socket.getInputStream());
		packet.ping(out);
		T obj = packet.pong(in);
		onSend = false;
		return obj;
	}

	public void connectReciever() {
		try {
			recieveOnly.connect(address);
			ConnectPacket connect = new ConnectPacket();
			onSend = true;
			DataOutputStream out = new DataOutputStream(recieveOnly.getOutputStream());
			DataInputStream in = new DataInputStream(recieveOnly.getInputStream());
			connect.ping(out);
			boolean con = connect.pong(in);
			if (con) {
				new Thread(() -> {
					loop: while (true) {
						try {
							byte[] bytes = new byte[4];
							in.readFully(bytes);
							if (!Arrays.equals(RecieveablePacket.RECIEVEMAGIC, bytes)) {
								System.out.println("반환 소켓에서 잘못된 Magic을 받았습니다.");
								System.out.println("Magic: " + Arrays.toString(bytes));
							}
							System.out.println("패킷 Magic이 정상적입니다. 분석을 시도합니다.");
							int id = in.readInt();
							System.out.println("ID: " + id);
							RPackets packet = RPackets.valueOf(id);
							switch (packet) {
							case LOG:
								String log = packet.pong(in);
								ServerTransfer.setLog(log);
								break;
							case KICK:
								SWTRun.runAsync(() -> {
									MessageView.info(ServerTransfer.shell).message("서버가 당신을 퇴출했습니다.").title("강제 퇴출")
											.open();
									ServerTransfer.close();
									LoginFrame l = new LoginFrame();
									l.open();
								});
								break loop;
							default:
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public void connect() {
		try {
			socket.connect(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUUID() {
		return uuid;
	}
}
