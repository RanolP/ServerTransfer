package me.ranol.servertransfer;

import java.security.MessageDigest;

public class PasswordSaver {
	public static String hashing(String real) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update((real + "_STPWD").getBytes());
			byte[] bytes = md.digest();
			StringBuffer buff = new StringBuffer();
			for (byte b : bytes)
				buff.append(Integer.toString(b & 0xFF + 0x100, 16).substring(0));
			return buff.toString();
		} catch (Exception e) {
		}
		return real;
	}
}
