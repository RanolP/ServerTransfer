package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import me.ranol.servertransfer.ClientManagement;

public class FilePacket implements Packet<Boolean> {
	byte[] bytes;
	String path;

	public FilePacket(File file, String path) {
		try {
			this.path = path;
			bytes = Files.readAllBytes(file.toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int id() {
		return 7;
	}

	@Override
	public void ping(DataOutputStream out) throws IOException {
		Packet.super.ping(out);
		out.writeUTF(path);
		out.writeUTF(ClientManagement.staticClient.getUUID());
		out.writeInt(bytes.length);
		out.write(bytes);
	}

	@Override
	public Boolean pong(DataInputStream in) throws IOException {
		return in.readBoolean();
	}

}
