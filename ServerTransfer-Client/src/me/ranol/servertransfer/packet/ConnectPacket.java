package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.ClientManagement;

public class ConnectPacket implements Packet<Boolean> {

	@Override
	public int id() {
		return 6;
	}

	@Override
	public void ping(DataOutputStream out) throws IOException {
		Packet.super.ping(out);
		System.out.println(ClientManagement.staticClient.salt);
		out.writeUTF(ClientManagement.staticClient.getUUID());
	}

	@Override
	public Boolean pong(DataInputStream in) throws IOException {
		return in.readBoolean();
	}

}
