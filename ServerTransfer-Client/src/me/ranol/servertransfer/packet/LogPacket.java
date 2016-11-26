package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.IOException;

public class LogPacket implements Packet<String> {

	@Override
	public int id() {
		return 1;
	}

	@Override
	public String pong(DataInputStream in) throws IOException {
		return in.readUTF();
	}

}
