package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.IOException;

public class KickPacket implements RecieveablePacket<Boolean> {

	@Override
	public int id() {
		return 5;
	}

	@Override
	public Boolean pong(DataInputStream in) throws IOException {
		return true;
	}

}
