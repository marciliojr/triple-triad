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
import java.util.HashMap;
import java.util.Map;

import itdelatrisu.tripletriad.gfx.Log;

/**
 * Loads and saves the local player profile.
 */
public class ProfileStore {
	/** Profile data file. */
	private static final File PROFILE_FILE = new File(".triple-triad-profile");

	// This class should not be instantiated.
	private ProfileStore() {}

	/**
	 * Loads the profile from disk.
	 * @return the profile, or null if none is stored
	 */
	public static Profile load() {
		if (!PROFILE_FILE.isFile())
			return null;

		Profile profile = new Profile();
		Map<Integer, String> names = new HashMap<Integer, String>();
		Map<Integer, int[]> cards = new HashMap<Integer, int[]>();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(PROFILE_FILE), "utf-8"))) {
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

				if (key.startsWith("DECK.") && key.endsWith(".NAME")) {
					Integer id = parseDeckIndex(key);
					if (id != null)
						names.put(id, value);
				} else if (key.startsWith("DECK.") && key.endsWith(".CARDS")) {
					Integer id = parseDeckIndex(key);
					if (id != null)
						cards.put(id, parseCardIds(value));
				}
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to read profile '%s'.", PROFILE_FILE.getAbsolutePath()), e);
			return null;
		}

		int max = 0;
		for (Integer id : names.keySet()) {
			if (id > max)
				max = id;
		}
		for (Integer id : cards.keySet()) {
			if (id > max)
				max = id;
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

	/**
	 * Writes the profile to disk.
	 * @param profile the profile
	 */
	public static void save(Profile profile) {
		if (profile == null || !profile.isValid())
			return;

		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(PROFILE_FILE), "utf-8"))) {
			writer.write("# Triple Triad profile");
			writer.newLine();
			writer.write(String.format("NAME = %s", profile.getName()));
			writer.newLine();
			writer.newLine();

			for (int i = 0; i < profile.getDecks().size(); i++) {
				SavedDeck deck = profile.getDecks().get(i);
				writer.write(String.format("DECK.%d.NAME = %s", i + 1, deck.getName()));
				writer.newLine();
				writer.write(String.format("DECK.%d.CARDS = %s", i + 1, formatCardIds(deck.getCardIds())));
				writer.newLine();
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to write profile '%s'.", PROFILE_FILE.getAbsolutePath()), e);
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
	private static int[] parseCardIds(String value) {
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
	 * Formats card IDs as a comma-separated list.
	 */
	private static String formatCardIds(int[] ids) {
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
