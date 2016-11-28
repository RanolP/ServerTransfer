package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import me.ranol.servertransfer.ClientManagement;
import me.ranol.servertransfer.PercentageWriter;

public class FilePacket implements Packet<Boolean> {
	byte[] bytes;
	int len;
	String path;
	PercentageWriter pw = new PercentageWriter(null);
	String filename;

	public FilePacket(File file, String path) {
		try {
			filename = file.getName();
			this.path = path;
			bytes = Files.readAllBytes(file.toPath());
			len = bytes.length;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FilePacket(String file, String directory) {
		this(new File(file), directory);
	}

	@Override
	public int id() {
		return 7;
	}

	@Override
	public void ping(DataOutputStream out) throws IOException {
		pw.setOut(out);
		Packet.super.ping(pw);
		pw.writeUTF(ClientManagement.staticClient.getUUID());
		pw.writeInt(len);
		pw.writeUTF(path + "\\" + filename);
		pw.start();
		pw.write(bytes);
	}

	@Override
	public Boolean pong(DataInputStream in) throws IOException {
		return in.readBoolean();
	}

	public PercentageWriter writer() {
		return pw;
	}

	public int all() {
		return len;
	}

}
