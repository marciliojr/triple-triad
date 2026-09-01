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

package itdelatrisu.tripletriad;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import itdelatrisu.tripletriad.gfx.Log;

/**
 * Export and import album / championship saves via the system file dialog.
 */
public final class SaveTransfer {
	/** File extension for portable saves. */
	public static final String EXTENSION = ".ttsave";

	/** Kind of portable save. */
	public enum Kind {
		DECK,
		CHAMPIONSHIP
	}

	/**
	 * Parsed contents of a {@code .ttsave} file.
	 */
	public static final class Snapshot {
		/** Save kind. */
		private Kind kind;

		/** Album card IDs. */
		private final ArrayList<Integer> collection = new ArrayList<Integer>();

		/** Championship round. */
		private int runRound;

		/** Championship wins. */
		private int runWins;

		/** Player hand. */
		private int[] runHand;

		/** Opponent hand. */
		private int[] runOpponent;

		/** Run bag. */
		private final ArrayList<Integer> runBag = new ArrayList<Integer>();

		/** @return the kind, or null if missing */
		public Kind getKind() { return kind; }

		/** @return true if this is a usable deck snapshot */
		public boolean isDeckValid() {
			return kind == Kind.DECK;
		}

		/** @return true if this is a usable championship snapshot */
		public boolean isChampionshipValid() {
			return kind == Kind.CHAMPIONSHIP && runRound >= 2;
		}
	}

	// This class should not be instantiated.
	private SaveTransfer() {}

	/**
	 * Opens a save dialog. Returns null if the player cancels.
	 * @param title the window title
	 * @param defaultName the suggested file name
	 * @return the file, or null
	 */
	public static File chooseExportFile(String title, String defaultName) {
		File chosen = showDialog(title, defaultName, FileDialog.SAVE);
		if (chosen == null)
			return null;
		return ensureExtension(chosen);
	}

	/**
	 * Opens a load dialog. Returns null if the player cancels.
	 * @param title the window title
	 * @return the file, or null
	 */
	public static File chooseImportFile(String title) {
		return showDialog(title, "*" + EXTENSION, FileDialog.LOAD);
	}

	/**
	 * Writes the album to a file.
	 * @param file the destination
	 * @param profile the profile
	 * @return true if written
	 */
	public static boolean writeDeck(File file, Profile profile) {
		if (file == null || profile == null)
			return false;
		try (BufferedWriter writer = openWriter(file)) {
			writer.write("# Triple Triad save");
			writer.newLine();
			writer.write("KIND = DECK");
			writer.newLine();
			writer.write("COLLECTION = " + ProfileStore.formatIdList(profile.getCollection()));
			writer.newLine();
			return true;
		} catch (IOException e) {
			Log.error(String.format("Failed to export deck '%s'.", file.getAbsolutePath()), e);
			return false;
		}
	}

	/**
	 * Writes the championship checkpoint to a file.
	 * @param file the destination
	 * @param profile the profile
	 * @return true if written
	 */
	public static boolean writeChampionship(File file, Profile profile) {
		if (file == null || profile == null || !profile.hasChampionshipSave())
			return false;
		try (BufferedWriter writer = openWriter(file)) {
			writer.write("# Triple Triad save");
			writer.newLine();
			writer.write("KIND = CHAMPIONSHIP");
			writer.newLine();
			writer.write(String.format("RUN_ROUND = %d", profile.getRunRound()));
			writer.newLine();
			writer.write(String.format("RUN_WINS = %d", profile.getRunWins()));
			writer.newLine();
			writer.write("RUN_HAND = " + ProfileStore.formatCardIds(profile.getRunHand()));
			writer.newLine();
			writer.write("RUN_OPPONENT = " + ProfileStore.formatCardIds(profile.getRunOpponent()));
			writer.newLine();
			writer.write("RUN_BAG = " + ProfileStore.formatIdList(profile.getRunBag()));
			writer.newLine();
			return true;
		} catch (IOException e) {
			Log.error(String.format("Failed to export championship '%s'.", file.getAbsolutePath()), e);
			return false;
		}
	}

	/**
	 * Reads a portable save. Returns null if the file is missing or unreadable.
	 * @param file the source
	 * @return the snapshot, or null
	 */
	public static Snapshot read(File file) {
		if (file == null || !file.isFile())
			return null;
		Snapshot snap = new Snapshot();
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "utf-8"))) {
			String line;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || line.startsWith("#"))
					continue;
				int index = line.indexOf('=');
				if (index == -1)
					continue;
				String key = line.substring(0, index).trim();
				String value = line.substring(index + 1).trim();
				if (key.equals("KIND")) {
					if (value.equalsIgnoreCase("DECK"))
						snap.kind = Kind.DECK;
					else if (value.equalsIgnoreCase("CHAMPIONSHIP"))
						snap.kind = Kind.CHAMPIONSHIP;
				} else if (key.equals("COLLECTION")) {
					addIds(snap.collection, ProfileStore.parseCardIds(value));
				} else if (key.equals("RUN_ROUND")) {
					snap.runRound = parseInt(value, 0);
				} else if (key.equals("RUN_WINS")) {
					snap.runWins = parseInt(value, 0);
				} else if (key.equals("RUN_HAND")) {
					snap.runHand = ProfileStore.parseCardIds(value);
				} else if (key.equals("RUN_OPPONENT")) {
					snap.runOpponent = ProfileStore.parseCardIds(value);
				} else if (key.equals("RUN_BAG")) {
					addIds(snap.runBag, ProfileStore.parseCardIds(value));
				}
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to import save '%s'.", file.getAbsolutePath()), e);
			return null;
		}
		return snap;
	}

	/**
	 * Applies a snapshot to the profile (replaces album or championship run).
	 * @param profile the profile
	 * @param snap the snapshot
	 * @return true if applied
	 */
	public static boolean apply(Profile profile, Snapshot snap) {
		if (profile == null || snap == null)
			return false;
		if (snap.isDeckValid()) {
			profile.replaceCollection(snap.collection);
			return true;
		}
		if (snap.isChampionshipValid()) {
			profile.setRunRound(snap.runRound);
			profile.setRunWins(snap.runWins);
			profile.setRunHand(snap.runHand);
			profile.setRunOpponent(snap.runOpponent);
			profile.setRunBag(snap.runBag);
			return true;
		}
		return false;
	}

	private static File showDialog(String title, String defaultName, int mode) {
		FileDialog dialog = new FileDialog((Frame) null, title != null ? title : "", mode);
		dialog.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name != null && name.toLowerCase().endsWith(EXTENSION);
			}
		});
		if (defaultName != null)
			dialog.setFile(defaultName);
		dialog.setVisible(true);
		String dir = dialog.getDirectory();
		String name = dialog.getFile();
		if (dir == null || name == null || name.isEmpty())
			return null;
		return new File(dir, name);
	}

	private static File ensureExtension(File file) {
		String name = file.getName();
		if (name.toLowerCase().endsWith(EXTENSION))
			return file;
		return new File(file.getParentFile(), name + EXTENSION);
	}

	private static BufferedWriter openWriter(File file) throws IOException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
	}

	private static void addIds(ArrayList<Integer> dest, int[] ids) {
		if (ids == null)
			return;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] > 0)
				dest.add(Integer.valueOf(ids[i]));
		}
	}

	private static int parseInt(String value, int fallback) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return fallback;
		}
	}
}
