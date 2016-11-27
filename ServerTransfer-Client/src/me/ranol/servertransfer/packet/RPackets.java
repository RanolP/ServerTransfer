package me.ranol.servertransfer.packet;

import java.io.DataInputStream;

public enum RPackets {
	LOG(new LogPacket()),

	KICK(new KickPacket());
	RecieveablePacket<? extends Object> packet;

	RPackets(RecieveablePacket<? extends Object> recieve) {
		packet = recieve;
	}

	public <T> T pong(DataInputStream in) {
		try {
			return (T) packet.pong(in);
		} catch (Exception e) {
		}
		return null;
	}

	public static RPackets valueOf(int id) {
		for (RPackets rp : values()) {
			if (rp.packet.id() == id)
				return rp;
		}
		return null;
	}
}
