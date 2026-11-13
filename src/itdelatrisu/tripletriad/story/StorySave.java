/*
 * Triple Triad - a card game from FFVIII
 * Copyright (C) 2014 Jeffrey Han
 *
 * Triple Triad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package itdelatrisu.tripletriad.story;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

/**
 * One story campaign. A profile can hold several of these.
 * The album, decks, championship blocks and trophies stay on the profile.
 * Cleared duels, standings inputs, rewards and the pending card choice stay here.
 */
public final class StorySave {
	/**
	 * On-disk format. Version 1 was the single STORY_CLEARED list.
	 * Version 3 adds {@code CAMP.n.GENDER} (the chosen protagonist).
	 */
	public static final int FORMAT = 3;

	/** Protagonist: aluno (male student). */
	public static final String MALE = "M";

	/** Protagonist: aluna (female student). */
	public static final String FEMALE = "F";

	/**
	 * Protagonist of a campaign saved before the choice existed (no GENDER key).
	 * Every line up to format 2 addressed the player as "o aluno" and no
	 * protagonist portrait was shown, so those campaigns stay with the aluno.
	 */
	public static final String LEGACY_GENDER = MALE;

	/** Portrait ids under {@code res/story/portraits/} ({@code <id>_normal.png}, {@code <id>_derrota.png}). */
	public static final String PORTRAIT_MALE = "aluno_m";
	public static final String PORTRAIT_FEMALE = "aluna";

	/** Same capacity as the championship memory card. Never auto-deleted. */
	public static final int MAX = 15;

	public static final int NAME_MAX = 16;

	/** Win dialogue still has to play. */
	public static final String TALK = "TALK";

	/** The player still has to choose the card. */
	public static final String PICK = "PICK";

	private static final DateTimeFormatter WHEN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	private String id = "";
	private String student = "";
	private String gender = LEGACY_GENDER;
	private long createdAt;
	private long savedAt;
	private final ArrayList<String> cleared = new ArrayList<String>();
	private final ArrayList<String> arrivals = new ArrayList<String>();
	private final ArrayList<String> specials = new ArrayList<String>();
	private String pendingDuel = "";
	private String pendingPhase = "";
	private int[] pendingHand = new int[0];
	private int pendingSpecial;
	private int[] lastHand = new int[0];
	private boolean complete;
	private boolean fromLegacy;
	private boolean starterScene;

	private StorySave() {}

	/**
	 * @param student the chosen name
	 * @return a new campaign, or null if the name is empty
	 */
	public static StorySave create(String student) {
		return create(student, LEGACY_GENDER);
	}

	/**
	 * @param student the chosen name
	 * @param gender {@link #MALE} or {@link #FEMALE}; anything else keeps {@link #LEGACY_GENDER}
	 * @return a new campaign, or null if the name is empty
	 */
	public static StorySave create(String student, String gender) {
		String clean = cleanName(student);
		if (clean.isEmpty())
			return null;
		StorySave save = new StorySave();
		save.student = clean;
		save.setGender(gender);
		save.createdAt = System.currentTimeMillis();
		save.savedAt = save.createdAt;
		save.id = "c" + Long.toString(save.createdAt, 36) + Integer.toString(new Random().nextInt(46656), 36);
		return save;
	}

	/** Blank save filled by the profile loader. */
	public static StorySave blank() { return new StorySave(); }

	public static String cleanName(String raw) {
		if (raw == null)
			return "";
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < raw.length() && out.length() < NAME_MAX; i++) {
			char c = raw.charAt(i);
			if (c == '=' || c == '\n' || c == '\r' || c == ',')
				continue;
			out.append(c);
		}
		return out.toString().trim();
	}

	public String id() { return id; }

	public void setId(String value) {
		if (value != null && !value.isEmpty())
			id = value;
	}

	public String student() { return student; }

	public void setStudent(String value) {
		String clean = cleanName(value);
		if (!clean.isEmpty())
			student = clean;
	}

	/** @return {@link #MALE} or {@link #FEMALE} */
	public String gender() { return gender; }

	public boolean isFemale() { return FEMALE.equals(gender); }

	/** Accepts M/F (any case) and aluno/aluna. Unknown values leave the current choice. */
	public void setGender(String value) {
		String parsed = parseGender(value);
		if (parsed != null)
			gender = parsed;
	}

	/** @return {@link #MALE}, {@link #FEMALE}, or null when the value is not a protagonist */
	public static String parseGender(String value) {
		if (value == null)
			return null;
		String v = value.trim().toLowerCase();
		if (v.equals("m") || v.equals("aluno") || v.equals("male"))
			return MALE;
		if (v.equals("f") || v.equals("aluna") || v.equals("female"))
			return FEMALE;
		return null;
	}

	/** @return the portrait id of this campaign's protagonist */
	public String portraitId() { return portraitFor(gender); }

	public static String portraitFor(String gender) {
		return FEMALE.equals(gender) ? PORTRAIT_FEMALE : PORTRAIT_MALE;
	}

	public long createdAt() { return createdAt; }

	public long savedAt() { return savedAt; }

	public void setCreatedAt(long value) { createdAt = Math.max(0L, value); }

	public void setSavedAt(long value) { savedAt = Math.max(0L, value); }

	public void touch() { savedAt = System.currentTimeMillis(); }

	public String when() {
		long stamp = savedAt > 0 ? savedAt : createdAt;
		if (stamp <= 0)
			return "";
		return WHEN.format(Instant.ofEpochMilli(stamp).atZone(ZoneId.systemDefault()));
	}

	public boolean isCleared(String duelId) { return duelId != null && cleared.contains(duelId); }

	public boolean hasArrival(String regionId) { return regionId != null && arrivals.contains(regionId); }

	public void markArrival(String regionId) {
		if (regionId != null && !regionId.isEmpty() && !arrivals.contains(regionId))
			arrivals.add(regionId);
	}

	public boolean specialGranted(String duelId) { return duelId != null && specials.contains(duelId); }

	public boolean isComplete() { return complete; }

	public void setComplete(boolean value) { complete = value; }

	public boolean isFromLegacy() { return fromLegacy; }

	public void setFromLegacy(boolean value) { fromLegacy = value; }

	public boolean wantsStarter() { return starterScene; }

	public void setStarterScene(boolean value) { starterScene = value; }

	public void clearStarterScene() { starterScene = false; }

	public int[] lastHand() { return lastHand.clone(); }

	public void setLastHand(int[] ids) { lastHand = copyHand(ids); }

	public boolean hasPending() { return pendingDuel != null && !pendingDuel.isEmpty(); }

	public String pendingDuel() { return pendingDuel; }

	public String pendingPhase() { return pendingPhase; }

	public int[] pendingHand() { return pendingHand.clone(); }

	public int pendingSpecial() { return pendingSpecial; }

	/**
	 * Records a win that still needs its dialogue and card choice.
	 * Cards are not granted here. A second call does not replace an open reward.
	 */
	public void armWin(String duelId, int[] hand, int specialId) {
		if (duelId == null || duelId.isEmpty() || hasPending())
			return;
		pendingDuel = duelId;
		pendingPhase = TALK;
		pendingHand = copyHand(hand);
		pendingSpecial = (!specialGranted(duelId) && specialId > 0 && !StoryCampaign.isSpecialInHand(pendingHand, specialId))
			? specialId : 0;
		if (StoryCampaign.isSpecial(pendingSpecial) && contains(pendingHand, pendingSpecial))
			pendingSpecial = 0;
		touch();
	}

	/** The win dialogue finished. The card choice is still open. */
	public void advanceToPick() {
		if (hasPending())
			pendingPhase = PICK;
	}

	/**
	 * Grants the chosen card and, on the first win, the fixed special.
	 * @return the ids added, an empty array if there is nothing pending,
	 *         or null if the choice is not one of the eligible cards
	 */
	public int[] confirm(int chosen) {
		if (!hasPending())
			return new int[0];
		if (!eligible(chosen))
			return null;
		int[] granted = (pendingSpecial > 0) ? new int[] { chosen, pendingSpecial } : new int[] { chosen };
		if (pendingSpecial > 0 && !specials.contains(pendingDuel))
			specials.add(pendingDuel);
		if (!cleared.contains(pendingDuel))
			cleared.add(pendingDuel);
		pendingDuel = "";
		pendingPhase = "";
		pendingHand = new int[0];
		pendingSpecial = 0;
		touch();
		return granted;
	}

	/** @return true if this hand card can be taken (not a fixed special) */
	public boolean eligible(int cardId) {
		return cardId > 0 && contains(pendingHand, cardId) && !StoryCampaign.isSpecial(cardId);
	}

	public String clearedCsv() { return join(cleared); }

	public String arrivalsCsv() { return join(arrivals); }

	public String specialsCsv() { return join(specials); }

	public void setClearedCsv(String csv) { fill(cleared, csv); }

	public void setArrivalsCsv(String csv) { fill(arrivals, csv); }

	public void setSpecialsCsv(String csv) { fill(specials, csv); }

	public void setPending(String duelId, String phase, int[] hand, int special) {
		pendingDuel = (duelId == null) ? "" : duelId;
		pendingPhase = PICK.equals(phase) ? PICK : (pendingDuel.isEmpty() ? "" : TALK);
		pendingHand = copyHand(hand);
		pendingSpecial = special > 0 ? special : 0;
		if (pendingDuel.isEmpty()) {
			pendingPhase = "";
			pendingHand = new int[0];
			pendingSpecial = 0;
		}
	}

	/** Copies a pre-campaign clear list without dropping unknown ids. */
	public void absorbLegacy(String clearedCsv, String arrivalCsv) {
		setClearedCsv(clearedCsv);
		setArrivalsCsv(arrivalCsv);
		fromLegacy = true;
		if (cleared.contains("final"))
			complete = true;
		for (int i = 0; i < cleared.size(); i++) {
			StoryCampaign.Duel duel = StoryCampaign.findDuel(cleared.get(i));
			if (duel != null && duel.rewardId > 0 && !specials.contains(duel.id))
				specials.add(duel.id);
		}
		touch();
	}

	private static boolean contains(int[] ids, int cardId) {
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == cardId)
				return true;
		}
		return false;
	}

	private static int[] copyHand(int[] ids) {
		if (ids == null || ids.length == 0)
			return new int[0];
		int n = 0;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] > 0)
				n++;
		}
		int[] out = new int[n];
		int w = 0;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] > 0)
				out[w++] = ids[i];
		}
		return out;
	}

	static String join(ArrayList<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0)
				builder.append(',');
			builder.append(list.get(i));
		}
		return builder.toString();
	}

	static void fill(ArrayList<String> list, String csv) {
		list.clear();
		if (csv == null || csv.isEmpty())
			return;
		String[] parts = csv.split(",");
		for (int i = 0; i < parts.length; i++) {
			String id = parts[i].trim();
			if (!id.isEmpty() && !list.contains(id))
				list.add(id);
		}
	}

	public static String formatIds(int[] ids) {
		if (ids == null || ids.length == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] <= 0)
				continue;
			if (builder.length() > 0)
				builder.append(',');
			builder.append(ids[i]);
		}
		return builder.toString();
	}

	public static int[] parseIds(String csv) {
		if (csv == null || csv.isEmpty())
			return new int[0];
		String[] parts = csv.split(",");
		int[] tmp = new int[parts.length];
		int n = 0;
		for (int i = 0; i < parts.length; i++) {
			try {
				int id = Integer.parseInt(parts[i].trim());
				if (id > 0)
					tmp[n++] = id;
			} catch (NumberFormatException e) {
				// skip
			}
		}
		int[] out = new int[n];
		System.arraycopy(tmp, 0, out, 0, n);
		return out;
	}
}
