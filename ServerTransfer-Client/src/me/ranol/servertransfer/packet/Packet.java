package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet<T> {
	static final byte[] MAGIC = { 127, -128, 127, -128 };

	public int id();

	public default void ping(DataOutputStream out) throws IOException {
		out.write(MAGIC);
		out.writeInt(id());
	}

	public T pong(DataInputStream in) throws IOException;
}
