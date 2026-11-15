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

import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/**
 * Optional rematch against a city boss: a stronger deck, new lines and
 * one extra rare, granted only on the first rematch win.
 */
public final class StoryRematch {
	public static final String SUFFIX = "_rematch";

	private StoryRematch() {}

	public static boolean isRematchId(String id) {
		return id != null && id.endsWith(SUFFIX);
	}

	public static String rematchId(String bossId) {
		if (bossId == null || bossId.isEmpty())
			return "";
		if (isRematchId(bossId))
			return bossId;
		return bossId + SUFFIX;
	}

	public static String baseId(String id) {
		if (id == null)
			return "";
		if (isRematchId(id))
			return id.substring(0, id.length() - SUFFIX.length());
		return id;
	}

	/** @return the second rare for this boss, or 0 if the duel has no rematch */
	public static int reward(String bossId) {
		String id = baseId(bossId);
		if (id.isEmpty())
			return 0;
		switch (id) {
			case "king": return 84;          // Shiva (after Ifrit)
			case "fujin": return 81;         // MiniMog (after Quezacotl)
			case "rinoa": return 80;         // Gilgamesh (after Siren)
			case "queen_deling": return 91;  // Leviathan (after Alexander)
			case "queen_dollet": return 92;  // Odin (after Diablos)
			case "flo": return 94;           // Cerberus (after Phoenix)
			case "seifer": return 108;       // Edea (after Seifer)
			case "queen_esthar": return 99;  // Eden (after Bahamut)
			default: return 0;
		}
	}

	public static boolean hasRematch(Duel duel) {
		return duel != null && duel.isBoss() && reward(duel.id) > 0;
	}

	/** @return true once the city's boss has been beaten, so the rematch seat opens */
	public static boolean unlocked(StorySave save, Duel boss) {
		return hasRematch(boss) && save != null && save.isCleared(boss.id);
	}

	/**
	 * Stronger copy of the boss used as the rematch opponent.
	 * The rare is still attached so {@link Duel#isBoss()} stays true; the save
	 * decides whether it is granted ({@link StorySave#specialGranted}).
	 */
	public static Duel asDuel(Duel boss) {
		if (boss == null || !hasRematch(boss))
			return boss;
		int prize = reward(boss.id);
		int min = Math.min(10, boss.minLevel + 1);
		int max = Math.min(10, boss.maxLevel + 1);
		if (min > max) {
			int t = min;
			min = max;
			max = t;
		}
		Options.AIType ai = boss.ai;
		if (ai == Options.AIType.DEFENSIVE)
			ai = Options.AIType.BALANCED;
		else if (ai == Options.AIType.BALANCED)
			ai = Options.AIType.OFFENSIVE;
		Line[] lines = StoryScript.rematchLines(boss.id);
		Line bio0 = lines[0];
		Line open0 = lines[1];
		Line before = lines[2];
		Line win = lines[3];
		Line lose = lines[4];
		Line afterWin = lines[5];
		Line afterLose = lines[6];
		return new Duel(
			rematchId(boss.id), boss.portrait,
			boss.namePt, boss.nameEn, boss.nameEs,
			min, max, ai, boss.rules, prize, boss.inTable,
			boss.rolePt, boss.roleEn, boss.roleEs,
			boss.stylePt, boss.styleEn, boss.styleEs,
			new Line[] { bio0 },
			new Line[] { open0 },
			before, win, lose,
			new Line[] { afterWin },
			new Line[] { afterLose });
	}

	/** Rare prize still available on this rematch seat, or 0 after it was taken. */
	public static int pendingReward(StorySave save, Duel rematch) {
		if (rematch == null)
			return 0;
		int prize = reward(baseId(rematch.id));
		if (prize <= 0)
			return 0;
		if (save != null && save.specialGranted(rematchId(baseId(rematch.id))))
			return 0;
		return prize;
	}

	public static Duel bossOf(String rematchOrBossId) {
		return StoryCampaign.findDuel(baseId(rematchOrBossId));
	}

	public static Region regionOf(String rematchOrBossId) {
		return StoryCampaign.regionOf(baseId(rematchOrBossId));
	}
}
