package me.ranol.servertransfer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PercentageWriter extends DataOutputStream {
	List<WrittenChecker> checker = new ArrayList<>();
	int minus = 0;

	@FunctionalInterface
	static interface WrittenChecker {
		public void check(int newWritten);
	}

	public PercentageWriter(OutputStream out) {
		super(out);
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}

	@Override
	public synchronized void write(int b) throws IOException {
		super.write(b);
		checkAll();
	}

	public void checkAll() {
		checker.forEach(c -> c.check(minus - size()));
	}

	public void addChecker(WrittenChecker wc) {
		checker.add(wc);
	}

	public void start() {
		minus = written;
	}

	public void removeChecker(WrittenChecker wc) {
		checker.remove(wc);
	}

}
