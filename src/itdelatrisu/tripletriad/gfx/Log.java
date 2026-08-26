/*
 * Triple Triad - a card game from FFVIII
 * Copyright (C) 2014 Jeffrey Han
 *
 * Triple Triad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Triple Triad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Triple Triad.  If not, see <http://www.gnu.org/licenses/>.
 */

package itdelatrisu.tripletriad.gfx;

import java.io.PrintStream;

/**
 * File-backed logger (replacement for Slick {@code Log}).
 */
public final class Log {
	/** Destination stream. */
	private static PrintStream out = System.err;

	/** Whether verbose messages are printed. */
	private static boolean verbose;

	// This class should not be instantiated.
	private Log() {}

	/**
	 * Enables or disables verbose logging.
	 * @param v true for verbose
	 */
	public static void setVerbose(boolean v) { verbose = v; }

	/**
	 * Sets the log destination.
	 * @param stream the stream
	 */
	public static void setOut(PrintStream stream) {
		out = (stream != null) ? stream : System.err;
	}

	/**
	 * Logs an error.
	 * @param message the message
	 */
	public static void error(String message) {
		out.println("[ERROR] " + message);
	}

	/**
	 * Logs an error with a cause.
	 * @param message the message
	 * @param t the cause
	 */
	public static void error(String message, Throwable t) {
		out.println("[ERROR] " + message);
		if (t != null)
			t.printStackTrace(out);
	}

	/**
	 * Logs a throwable.
	 * @param t the cause
	 */
	public static void error(Throwable t) {
		if (t != null)
			t.printStackTrace(out);
	}

	/**
	 * Logs a warning.
	 * @param message the message
	 */
	public static void warn(String message) {
		out.println("[WARN] " + message);
	}

	/**
	 * Logs a warning with a cause.
	 * @param message the message
	 * @param t the cause
	 */
	public static void warn(String message, Throwable t) {
		out.println("[WARN] " + message);
		if (t != null)
			t.printStackTrace(out);
	}

	/**
	 * Logs a verbose message.
	 * @param message the message
	 */
	public static void info(String message) {
		if (verbose)
			out.println("[INFO] " + message);
	}
}
