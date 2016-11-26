package me.ranol.servertransfer.swtutils;

import org.eclipse.swt.widgets.Display;

public class SWTRun {
	public static void runAsync(Runnable run) {
		Display.getDefault().asyncExec(run);
	}

	public static void runSync(Runnable run) {
		Display.getDefault().syncExec(run);
	}

	public static void runThread(Runnable run) {
		new Thread(() -> Display.getDefault().asyncExec(run)).start();
	}
}
