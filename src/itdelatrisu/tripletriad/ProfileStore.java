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
 * along with Triple Triad.  If not, see <https://www.gnu.org/licenses/>.
 */

package itdelatrisu.tripletriad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import itdelatrisu.tripletriad.gfx.Log;

/**
 * Loads and saves local player profiles (one file per player).
 */
public class ProfileStore {
	/** Directory for per-player profile files. */
	private static final File PROFILE_DIR = new File(".triple-triad-profiles");

	/** Legacy single-profile file (migrated on first load). */
	private static final File LEGACY_FILE = new File(".triple-triad-profile");

	/**
	 * A profile listing entry.
	 */
	public static final class Entry {
		/** Profile id. */
		private final int id;

		/** Display name. */
		private final String name;

		/**
		 * Constructor.
		 * @param id the profile id
		 * @param name the display name
		 */
		public Entry(int id, String name) {
			this.id = id;
			this.name = (name != null) ? name : "";
		}

		/** @return the profile id */
		public int getId() { return id; }

		/** @return the display name */
		public String getName() { return name; }
	}

	// This class should not be instantiated.
	private ProfileStore() {}

	/**
	 * Copies the legacy single-file profile into the profiles directory if needed.
	 */
	public static void migrateIfNeeded() {
		ensureDir();
		if (countProfileFiles() > 0)
			return;
		if (!LEGACY_FILE.isFile())
			return;
		copyFile(LEGACY_FILE, fileFor(1));
		if (Options.getActiveProfileId() <= 0) {
			Options.setActiveProfileId(1);
			Options.saveOptions();
		}
	}

	/**
	 * Lists saved profiles, sorted by id.
	 * @return the entries
	 */
	public static ArrayList<Entry> list() {
		migrateIfNeeded();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		File[] files = PROFILE_DIR.listFiles();
		if (files == null)
			return entries;
		for (int i = 0; i < files.length; i++) {
			int id = parseProfileId(files[i].getName());
			if (id <= 0)
				continue;
			String name = readName(files[i]);
			if (name == null || name.isEmpty())
				continue;
			entries.add(new Entry(id, name));
		}
		Collections.sort(entries, new Comparator<Entry>() {
			@Override
			public int compare(Entry a, Entry b) {
				return Integer.compare(a.getId(), b.getId());
			}
		});
		return entries;
	}

	/**
	 * Loads a profile by id.
	 * @param id the profile id
	 * @return the profile, or null
	 */
	public static Profile load(int id) {
		if (id <= 0)
			return null;
		return loadFromFile(fileFor(id));
	}

	/**
	 * Writes a profile by id.
	 * @param id the profile id
	 * @param profile the profile
	 */
	public static void save(int id, Profile profile) {
		if (id <= 0 || profile == null || !profile.isValid())
			return;
		ensureDir();
		saveToFile(fileFor(id), profile);
	}

	/**
	 * Deletes a profile file.
	 * @param id the profile id
	 * @return true if the file was removed
	 */
	public static boolean delete(int id) {
		if (id <= 0)
			return false;
		File file = fileFor(id);
		return file.isFile() && file.delete();
	}

	/**
	 * Returns the next unused profile id.
	 * @return the id
	 */
	public static int nextId() {
		migrateIfNeeded();
		int max = 0;
		File[] files = PROFILE_DIR.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				int id = parseProfileId(files[i].getName());
				if (id > max)
					max = id;
			}
		}
		return max + 1;
	}

	/**
	 * True if another profile already uses this name.
	 * @param name the name to check
	 * @param exceptId profile id to ignore (rename of self)
	 * @return true if taken
	 */
	public static boolean nameTaken(String name, int exceptId) {
		if (name == null)
			return false;
		String trimmed = name.trim();
		if (trimmed.isEmpty())
			return false;
		ArrayList<Entry> entries = list();
		for (int i = 0; i < entries.size(); i++) {
			Entry entry = entries.get(i);
			if (entry.getId() != exceptId && entry.getName().equalsIgnoreCase(trimmed))
				return true;
		}
		return false;
	}

	private static File fileFor(int id) {
		return new File(PROFILE_DIR, id + ".profile");
	}

	private static void ensureDir() {
		if (!PROFILE_DIR.isDirectory())
			PROFILE_DIR.mkdirs();
	}

	private static int countProfileFiles() {
		File[] files = PROFILE_DIR.listFiles();
		if (files == null)
			return 0;
		int count = 0;
		for (int i = 0; i < files.length; i++) {
			if (parseProfileId(files[i].getName()) > 0)
				count++;
		}
		return count;
	}

	private static int parseProfileId(String filename) {
		if (filename == null || !filename.endsWith(".profile"))
			return 0;
		String stem = filename.substring(0, filename.length() - ".profile".length());
		try {
			int id = Integer.parseInt(stem);
			return (id > 0) ? id : 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private static String readName(File file) {
		if (file == null || !file.isFile())
			return null;
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
				if (key.equals("NAME"))
					return line.substring(index + 1).trim();
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to read profile name '%s'.", file.getAbsolutePath()), e);
		}
		return null;
	}

	private static void copyFile(File from, File to) {
		try (FileInputStream in = new FileInputStream(from);
				FileOutputStream out = new FileOutputStream(to)) {
			byte[] buf = new byte[4096];
			int n;
			while ((n = in.read(buf)) != -1)
				out.write(buf, 0, n);
		} catch (IOException e) {
			Log.error(String.format("Failed to migrate profile '%s'.", from.getAbsolutePath()), e);
		}
	}

	private static Profile loadFromFile(File file) {
		if (file == null || !file.isFile())
			return null;

		Profile profile = new Profile();
		Map<Integer, String> names = new HashMap<Integer, String>();
		Map<Integer, int[]> cards = new HashMap<Integer, int[]>();

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

				if (key.equals("NAME")) {
					profile.setName(value);
					continue;
				}
				if (key.equals("COLLECTION")) {
					int[] ids = parseCardIds(value);
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] > 0)
							profile.addCard(ids[i]);
					}
					continue;
				}
				if (key.equals("CHAMPIONSHIP_WINS")) {
					try {
						profile.setChampionshipWins(Integer.parseInt(value));
					} catch (NumberFormatException e) {
						profile.setChampionshipWins(0);
					}
					continue;
				}
				if (key.equals("RUN_ROUND")) {
					try {
						profile.setRunRound(Integer.parseInt(value));
					} catch (NumberFormatException e) {
						profile.setRunRound(0);
					}
					continue;
				}
				if (key.equals("RUN_WINS")) {
					try {
						profile.setRunWins(Integer.parseInt(value));
					} catch (NumberFormatException e) {
						profile.setRunWins(0);
					}
					continue;
				}
				if (key.equals("RUN_HAND")) {
					profile.setRunHand(parseCardIds(value));
					continue;
				}
				if (key.equals("RUN_OPPONENT")) {
					profile.setRunOpponent(parseCardIds(value));
					continue;
				}
				if (key.equals("RUN_BAG")) {
					int[] ids = parseCardIds(value);
					ArrayList<Integer> bag = new ArrayList<Integer>();
					for (int i = 0; i < ids.length; i++) {
						if (ids[i] > 0)
							bag.add(Integer.valueOf(ids[i]));
					}
					profile.setRunBag(bag);
					continue;
				}

				if (key.startsWith("DECK.") && key.endsWith(".NAME")) {
					Integer deckId = parseDeckIndex(key);
					if (deckId != null)
						names.put(deckId, value);
				} else if (key.startsWith("DECK.") && key.endsWith(".CARDS")) {
					Integer deckId = parseDeckIndex(key);
					if (deckId != null)
						cards.put(deckId, parseCardIds(value));
				}
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to read profile '%s'.", file.getAbsolutePath()), e);
			return null;
		}

		int max = 0;
		for (Integer deckId : names.keySet()) {
			if (deckId > max)
				max = deckId;
		}
		for (Integer deckId : cards.keySet()) {
			if (deckId > max)
				max = deckId;
		}
		for (int i = 1; i <= max; i++) {
			String name = names.get(Integer.valueOf(i));
			int[] ids = cards.get(Integer.valueOf(i));
			if (name == null || name.isEmpty() || ids == null)
				continue;
			SavedDeck deck = new SavedDeck(name, ids);
			if (deck.isComplete())
				profile.getDecks().add(deck);
		}

		return profile.isValid() ? profile : null;
	}

	private static void saveToFile(File file, Profile profile) {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "utf-8"))) {
			writer.write("# Triple Triad profile");
			writer.newLine();
			writer.write(String.format("NAME = %s", profile.getName()));
			writer.newLine();
			writer.write(String.format("COLLECTION = %s", formatCollection(profile)));
			writer.newLine();
			writer.write(String.format("CHAMPIONSHIP_WINS = %d", profile.getChampionshipWins()));
			writer.newLine();
			if (profile.hasChampionshipSave()) {
				writer.write(String.format("RUN_ROUND = %d", profile.getRunRound()));
				writer.newLine();
				writer.write(String.format("RUN_WINS = %d", profile.getRunWins()));
				writer.newLine();
				writer.write(String.format("RUN_HAND = %s", formatCardIds(profile.getRunHand())));
				writer.newLine();
				writer.write(String.format("RUN_OPPONENT = %s", formatCardIds(profile.getRunOpponent())));
				writer.newLine();
				writer.write(String.format("RUN_BAG = %s", formatIdList(profile.getRunBag())));
				writer.newLine();
			}
			writer.newLine();

			for (int i = 0; i < profile.getDecks().size(); i++) {
				SavedDeck deck = profile.getDecks().get(i);
				writer.write(String.format("DECK.%d.NAME = %s", i + 1, deck.getName()));
				writer.newLine();
				writer.write(String.format("DECK.%d.CARDS = %s", i + 1, formatCardIds(deck.getCardIds())));
				writer.newLine();
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to write profile '%s'.", file.getAbsolutePath()), e);
		}
	}

	/**
	 * Parses the numeric index from a DECK.N.NAME / DECK.N.CARDS key.
	 */
	private static Integer parseDeckIndex(String key) {
		try {
			String[] parts = key.split("\\.");
			if (parts.length != 3)
				return null;
			return Integer.valueOf(Integer.parseInt(parts[1]));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Parses a comma-separated list of card IDs.
	 */
	static int[] parseCardIds(String value) {
		if (value == null || value.isEmpty())
			return new int[0];
		String[] tokens = value.split(",");
		int[] ids = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			try {
				ids[i] = Integer.parseInt(tokens[i].trim());
			} catch (NumberFormatException e) {
				return new int[0];
			}
		}
		return ids;
	}

	/**
	 * Formats the championship collection as a comma-separated list.
	 */
	private static String formatCollection(Profile profile) {
		return formatIdList(profile.getCollection());
	}

	/**
	 * Formats a list of card IDs as a comma-separated list.
	 */
	static String formatIdList(ArrayList<Integer> ids) {
		if (ids == null || ids.isEmpty())
			return "";
		int[] array = new int[ids.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = ids.get(i).intValue();
		return formatCardIds(array);
	}

	/**
	 * Formats card IDs as a comma-separated list.
	 */
	static String formatCardIds(int[] ids) {
		if (ids == null || ids.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			if (i > 0)
				sb.append(',');
			sb.append(ids[i]);
		}
		return sb.toString();
	}
}
