package me.ranol.servertransfer.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.ranol.servertransfer.ClientManagement;
import me.ranol.servertransfer.PasswordSaver;
import me.ranol.servertransfer.SaltMaker;

public class LoginPacket implements Packet<String> {
	String id;
	String pwd;

	static class LoginResponse {
		public static final byte PWD_NOT_EQUAL = 0;
		public static final byte ACCOUNT_NOT_EXISTS = 1;
		public static final byte SUCESS_LOGIN = 2;
	}

	public LoginPacket(String id, String pwd) {
		this.id = id;
		this.pwd = pwd;
	}

	@Override
	public int id() {
		return 0;
	}

	@Override
	public void ping(DataOutputStream out) throws IOException {
		Packet.super.ping(out);
		out.writeUTF(id);
		out.writeUTF(PasswordSaver.hashing(pwd));
		ClientManagement.staticClient.salt = SaltMaker.makeSalt(16);
		out.writeUTF(ClientManagement.staticClient.salt);
	}

	@Override
	public String pong(DataInputStream in) throws IOException {
		int response = in.readInt();
		switch (response) {
		case LoginResponse.PWD_NOT_EQUAL:
			return "PWD";
		case LoginResponse.ACCOUNT_NOT_EXISTS:
			return "ACC";
		case LoginResponse.SUCESS_LOGIN:
			return in.readUTF();
		default:
			return "WTF";
		}
	}

}
