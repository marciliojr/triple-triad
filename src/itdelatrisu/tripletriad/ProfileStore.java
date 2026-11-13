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
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.story.StorySave;

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

	/** Reads a profile file. Used by the story tests and by {@link #load(int)}. */
	public static Profile readFile(File file) {
		return loadFromFile(file);
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

	/** Writes a profile file. Same bytes as {@link #save(int, Profile)}. */
	public static void writeFile(File file, Profile profile) {
		if (file == null || profile == null || !profile.isValid())
			return;
		saveToFile(file, profile);
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
		ChampionshipSlot[] parsedSlots = new ChampionshipSlot[Profile.SLOTS];
		Map<Integer, StorySave> camps = new HashMap<Integer, StorySave>();

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
				if (key.equals("AVATAR") || key.startsWith("STAT_")) {
					int n;
					try {
						n = Integer.parseInt(value);
					} catch (NumberFormatException e) {
						continue;
					}
					switch (key) {
						case "AVATAR": profile.setAvatar(n); break;
						case "STAT_QUICK_WINS": profile.setQuickWins(n); break;
						case "STAT_QUICK_LOSSES": profile.setQuickLosses(n); break;
						case "STAT_QUICK_DRAWS": profile.setQuickDraws(n); break;
						case "STAT_CHAMP_MATCH_WINS": profile.setChampMatchWins(n); break;
						case "STAT_CHAMP_MATCH_LOSSES": profile.setChampMatchLosses(n); break;
						default: break;
					}
					continue;
				}
				if (key.equals("STORY_CLEARED") || key.equals("STORY_LEGACY_CLEARED")) {
					if (key.equals("STORY_LEGACY_CLEARED") || profile.getStoryClearedCsv().isEmpty())
						profile.setStoryClearedCsv(value);
					continue;
				}
				if (key.equals("STORY_ARRIVALS") || key.equals("STORY_LEGACY_ARRIVALS")) {
					if (key.equals("STORY_LEGACY_ARRIVALS") || profile.getStoryArrivalsCsv().isEmpty())
						profile.setStoryArrivalsCsv(value);
					continue;
				}
				if (key.equals("STORY_MIGRATED")) {
					profile.setStoryMigrated("1".equals(value));
					continue;
				}
				if (key.equals("STORY_ACTIVE")) {
					profile.setActiveStoryId(value);
					continue;
				}
				if (key.startsWith("CAMP.")) {
					readCampKey(camps, key, value);
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

				if (key.startsWith("SLOT.")) {
					readSlotKey(parsedSlots, key, value);
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

		for (int i = 0; i < Profile.SLOTS; i++)
			profile.setSlot(i, parsedSlots[i]);
		int campMax = 0;
		for (Integer campId : camps.keySet()) {
			if (campId.intValue() > campMax)
				campMax = campId.intValue();
		}
		for (int i = 1; i <= campMax && profile.campaignCount() < StorySave.MAX; i++) {
			StorySave camp = camps.get(Integer.valueOf(i));
			if (camp != null && !camp.id().isEmpty() && !camp.student().isEmpty())
				profile.getCampaigns().add(camp);
		}
		profile.migrateLegacyRun();
		profile.migrateStoryIfNeeded();
		return profile.isValid() ? profile : null;
	}

	private static void readCampKey(Map<Integer, StorySave> camps, String key, String value) {
		String[] parts = key.split("\\.");
		if (parts.length != 3)
			return;
		int index;
		try {
			index = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			return;
		}
		if (index <= 0)
			return;
		StorySave save = camps.get(Integer.valueOf(index));
		if (save == null) {
			save = StorySave.blank();
			camps.put(Integer.valueOf(index), save);
		}
		String field = parts[2];
		try {
			if (field.equals("ID"))
				save.setId(value);
			else if (field.equals("STUDENT"))
				save.setStudent(value);
			else if (field.equals("GENDER"))
				save.setGender(value);
			else if (field.equals("CREATED"))
				save.setCreatedAt(Long.parseLong(value));
			else if (field.equals("SAVED"))
				save.setSavedAt(Long.parseLong(value));
			else if (field.equals("CLEARED"))
				save.setClearedCsv(value);
			else if (field.equals("ARRIVALS"))
				save.setArrivalsCsv(value);
			else if (field.equals("SPECIALS"))
				save.setSpecialsCsv(value);
			else if (field.equals("PENDING"))
				save.setPending(value, save.pendingPhase(), save.pendingHand(), save.pendingSpecial());
			else if (field.equals("PHASE"))
				save.setPending(save.pendingDuel(), value, save.pendingHand(), save.pendingSpecial());
			else if (field.equals("HAND"))
				save.setPending(save.pendingDuel(), save.pendingPhase(), StorySave.parseIds(value), save.pendingSpecial());
			else if (field.equals("SPECIAL"))
				save.setPending(save.pendingDuel(), save.pendingPhase(), save.pendingHand(), Integer.parseInt(value));
			else if (field.equals("LAST"))
				save.setLastHand(StorySave.parseIds(value));
			else if (field.equals("COMPLETE"))
				save.setComplete("1".equals(value));
			else if (field.equals("LEGACY"))
				save.setFromLegacy("1".equals(value));
			else if (field.equals("STARTER"))
				save.setStarterScene("1".equals(value));
		} catch (NumberFormatException e) {
			// keep defaults
		}
	}

	/**
	 * Parses one SLOT.N.FIELD key (N is 1-based) into the profile's save block.
	 */
	private static void readSlotKey(ChampionshipSlot[] slots, String key, String value) {
		String[] parts = key.split("\\.");
		if (parts.length != 3)
			return;
		int index;
		try {
			index = Integer.parseInt(parts[1]) - 1;
		} catch (NumberFormatException e) {
			return;
		}
		if (index < 0 || index >= Profile.SLOTS)
			return;
		ChampionshipSlot slot = slots[index];
		if (slot == null) {
			slot = new ChampionshipSlot();
			slots[index] = slot;
		}
		String field = parts[2];
		try {
			if (field.equals("ROUND"))
				slot.setRound(Integer.parseInt(value));
			else if (field.equals("WINS"))
				slot.setWins(Integer.parseInt(value));
			else if (field.equals("HAND"))
				slot.setHand(parseCardIds(value));
			else if (field.equals("OPPONENT"))
				slot.setOpponent(parseCardIds(value));
			else if (field.equals("BAG")) {
				int[] ids = parseCardIds(value);
				ArrayList<Integer> bag = new ArrayList<Integer>();
				for (int i = 0; i < ids.length; i++)
					bag.add(Integer.valueOf(ids[i]));
				slot.setBag(bag);
			} else if (field.equals("RULES"))
				slot.setRulesFromString(value);
			else if (field.equals("PLAY_MS"))
				slot.setPlayMillis(Long.parseLong(value));
			else if (field.equals("SAVED_AT"))
				slot.setSavedAt(Long.parseLong(value));
		} catch (NumberFormatException e) {
			// keep defaults
		}
	}

	private static void saveToFile(File file, Profile profile) {
		File dir = file.getParentFile();
		File tmp = new File(dir != null ? dir : new File("."), file.getName() + ".tmp");
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(tmp), "utf-8"))) {
			writer.write("# Triple Triad profile");
			writer.newLine();
			writer.write(String.format("NAME = %s", profile.getName()));
			writer.newLine();
			writer.write(String.format("COLLECTION = %s", formatCollection(profile)));
			writer.newLine();
			writer.write(String.format("CHAMPIONSHIP_WINS = %d", profile.getChampionshipWins()));
			writer.newLine();
			writer.write(String.format("AVATAR = %d", profile.getAvatar()));
			writer.newLine();
			writer.write(String.format("STAT_QUICK_WINS = %d", profile.getQuickWins()));
			writer.newLine();
			writer.write(String.format("STAT_QUICK_LOSSES = %d", profile.getQuickLosses()));
			writer.newLine();
			writer.write(String.format("STAT_QUICK_DRAWS = %d", profile.getQuickDraws()));
			writer.newLine();
			writer.write(String.format("STAT_CHAMP_MATCH_WINS = %d", profile.getChampMatchWins()));
			writer.newLine();
			writer.write(String.format("STAT_CHAMP_MATCH_LOSSES = %d", profile.getChampMatchLosses()));
			writer.newLine();
			writer.write("STORY_FORMAT = " + StorySave.FORMAT);
			writer.newLine();
			writer.write("STORY_MIGRATED = " + (profile.isStoryMigrated() ? "1" : "0"));
			writer.newLine();
			writer.write("STORY_ACTIVE = " + profile.getActiveStoryId());
			writer.newLine();
			writer.write("STORY_CLEARED = " + profile.getStoryClearedCsv());
			writer.newLine();
			writer.write("STORY_ARRIVALS = " + profile.getStoryArrivalsCsv());
			writer.newLine();
			writer.write("STORY_LEGACY_CLEARED = " + profile.getStoryClearedCsv());
			writer.newLine();
			writer.write("STORY_LEGACY_ARRIVALS = " + profile.getStoryArrivalsCsv());
			writer.newLine();
			for (int i = 0; i < profile.campaignCount(); i++) {
				StorySave camp = profile.campaignAt(i);
				String p = "CAMP." + (i + 1) + ".";
				writer.write(p + "ID = " + camp.id());
				writer.newLine();
				writer.write(p + "STUDENT = " + camp.student());
				writer.newLine();
				writer.write(p + "GENDER = " + camp.gender());
				writer.newLine();
				writer.write(p + "CREATED = " + camp.createdAt());
				writer.newLine();
				writer.write(p + "SAVED = " + camp.savedAt());
				writer.newLine();
				writer.write(p + "CLEARED = " + camp.clearedCsv());
				writer.newLine();
				writer.write(p + "ARRIVALS = " + camp.arrivalsCsv());
				writer.newLine();
				writer.write(p + "SPECIALS = " + camp.specialsCsv());
				writer.newLine();
				writer.write(p + "PENDING = " + camp.pendingDuel());
				writer.newLine();
				writer.write(p + "PHASE = " + camp.pendingPhase());
				writer.newLine();
				writer.write(p + "HAND = " + StorySave.formatIds(camp.pendingHand()));
				writer.newLine();
				writer.write(p + "SPECIAL = " + camp.pendingSpecial());
				writer.newLine();
				writer.write(p + "LAST = " + StorySave.formatIds(camp.lastHand()));
				writer.newLine();
				writer.write(p + "COMPLETE = " + (camp.isComplete() ? "1" : "0"));
				writer.newLine();
				writer.write(p + "LEGACY = " + (camp.isFromLegacy() ? "1" : "0"));
				writer.newLine();
				writer.write(p + "STARTER = " + (camp.wantsStarter() ? "1" : "0"));
				writer.newLine();
			}
			for (int i = 0; i < Profile.SLOTS; i++) {
				ChampionshipSlot slot = profile.getSlot(i);
				if (slot == null)
					continue;
				String p = String.format("SLOT.%d.", i + 1);
				writer.write(p + "ROUND = " + slot.getRound());
				writer.newLine();
				writer.write(p + "WINS = " + slot.getWins());
				writer.newLine();
				writer.write(p + "HAND = " + formatCardIds(slot.getHand()));
				writer.newLine();
				writer.write(p + "OPPONENT = " + formatCardIds(slot.getOpponent()));
				writer.newLine();
				writer.write(p + "BAG = " + formatIdList(slot.getBag()));
				writer.newLine();
				if (slot.hasRules()) {
					writer.write(p + "RULES = " + slot.rulesToString());
					writer.newLine();
				}
				writer.write(p + "PLAY_MS = " + slot.getPlayMillis());
				writer.newLine();
				writer.write(p + "SAVED_AT = " + slot.getSavedAt());
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
			writer.flush();
		} catch (IOException e) {
			Log.error(String.format("Failed to write profile '%s'.", file.getAbsolutePath()), e);
			tmp.delete();
			return;
		}
		try {
			try {
				Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (AtomicMoveNotSupportedException e) {
				Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to replace profile '%s'.", file.getAbsolutePath()), e);
			tmp.delete();
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
