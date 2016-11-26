package me.ranol.servertransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import me.ranol.servertransfer.packet.LogPacket;

public class Pinger {
	public static void main(String[] args) {
		try (Socket soc = new Socket()) {
			soc.setSoTimeout(3000);
			SocketAddress address = new InetSocketAddress(InetAddress.getByName("localhost"), 2929);
			soc.connect(address);
			DataInputStream in = new DataInputStream(soc.getInputStream());
			DataOutputStream out = new DataOutputStream(soc.getOutputStream());
			LogPacket packet = new LogPacket();
			System.out.println("Pinging...");
			packet.ping(out);
			System.out.println("Ping!");
			System.out.println(packet.pong(in));
			System.out.println("Pong!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
