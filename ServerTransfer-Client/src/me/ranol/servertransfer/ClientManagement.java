package me.ranol.servertransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import me.ranol.servertransfer.packet.Packet;

public class ClientManagement {
	Socket socket;
	InetSocketAddress address;
	String uuid;
	public static ClientManagement staticClient;
	boolean onSend = false;
	public String salt;

	public ClientManagement(String host) {
		staticClient = this;
		this.socket = new Socket();
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
