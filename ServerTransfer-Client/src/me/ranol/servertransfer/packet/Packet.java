package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.ClientManagement;

public interface Packet<T> {
	static final byte[] MAGIC = { 127, -128, 127, -128 };

	public int id();

	public default void ping(DataOutputStream out) throws IOException {
		out.write(MAGIC);
		out.writeInt(id());
		if (!(this instanceof LoginPacket))
			out.writeUTF(ClientManagement.staticClient.salt);
	}

	public T pong(DataInputStream in) throws IOException;
}
