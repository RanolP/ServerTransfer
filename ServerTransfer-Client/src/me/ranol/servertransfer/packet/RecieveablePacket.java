package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.IOException;

public interface RecieveablePacket<T> {
	static final byte[] RECIEVEMAGIC = { -100, 100, -100, 100 };

	public int id();

	public T pong(DataInputStream in) throws IOException;
}
