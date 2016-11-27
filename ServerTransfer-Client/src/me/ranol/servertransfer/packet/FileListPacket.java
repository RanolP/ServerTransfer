package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.ranol.servertransfer.ClientManagement;

public class FileListPacket implements Packet<Collection<String>> {

	String dir;

	public FileListPacket(String path) {
		dir = path;
	}

	@Override
	public int id() {
		return 8;
	}

	@Override
	public void ping(DataOutputStream out) throws IOException {
		Packet.super.ping(out);
		out.writeUTF(ClientManagement.staticClient.getUUID());
		out.writeUTF(dir);
	}

	@Override
	public Collection<String> pong(DataInputStream in) throws IOException {
		int size = in.readInt();
		List<String> result = new ArrayList<>();
		while (size-- > 0)
			result.add(in.readUTF());
		return result;
	}

}
