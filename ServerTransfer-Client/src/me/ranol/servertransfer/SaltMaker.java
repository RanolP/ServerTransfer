package me.ranol.servertransfer;

public class SaltMaker {
	public static String makeSalt(int length) {
		StringBuilder b = new StringBuilder();
		while (length-- > 0)
			b.append((char) (Math.random() * 1000));
		return b.toString();
	}
}
