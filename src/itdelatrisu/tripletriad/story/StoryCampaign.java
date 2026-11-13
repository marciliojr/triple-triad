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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Rule;

/**
 * World Triple Triad Tournament, an original fan story in the world of FFVIII.
 * The player is a Balamb Garden student. Squall mentors. Nilo Vann calls the matches.
 * <p>
 * Regulation (also shown in the campaign):
 * <ul>
 * <li>Garden qualifier is a ladder. A win faces the next seed. A loss is a rematch
 * of the same tie and does not eliminate the student.</li>
 * <li>Points phases score 3 for a win. A loss scores 0 and stays off the table
 * until the rematch replaces it. Other tables in the group are already filled in.</li>
 * <li>Cutoffs: Balamb Town, Deling and Dollet top 2; Timber top 3; FH quarter and
 * Esthar semi top 1. The FH match against the Queen is a seed match: no points,
 * nobody is eliminated.</li>
 * <li>The final is one match. A loss is a rematch. The title passes only on a win.</li>
 * <li>Every win: choose one card from the hand that was played. A designated
 * special card is added on the first win only and is never in that hand.</li>
 * </ul>
 * Specials use real catalog ids. They are excluded from every story hand.
 */
public final class StoryCampaign {
	public enum After { HUB, STAGE, LIST, PICK, CARD, CEREMONY }

	public enum Kind { LADDER, GROUP, FINAL }

	public enum Ruleset {
		OPEN_BASIC(Rule.OPEN, Rule.SAME, Rule.COMBO),
		BASIC(Rule.SAME, Rule.COMBO),
		PLUS(Rule.SAME, Rule.PLUS, Rule.COMBO),
		ELEMENT(Rule.SAME, Rule.PLUS, Rule.COMBO, Rule.ELEMENTAL),
		FINAL(Rule.SAME, Rule.SAME_WALL, Rule.PLUS, Rule.COMBO, Rule.ELEMENTAL, Rule.SUDDEN_DEATH);

		public final Rule[] on;

		Ruleset(Rule... on) { this.on = on; }
	}

	/** One spoken box. Narrator lines draw the original badge, not a face. */
	public static final class Line {
		public final boolean narrator;
		public final String portrait;
		public final boolean defeat;
		public final String namePt, nameEn, nameEs;
		public final String pt, en, es;

		public Line(boolean narrator, String portrait, boolean defeat,
				String namePt, String nameEn, String nameEs, String pt, String en, String es) {
			this.narrator = narrator;
			this.portrait = portrait;
			this.defeat = defeat;
			this.namePt = namePt;
			this.nameEn = nameEn;
			this.nameEs = nameEs;
			this.pt = pt;
			this.en = en;
			this.es = es;
		}

		public String name() { return I18n.text(namePt, nameEn, nameEs); }

		public String text(StorySave save) { return fill(I18n.text(pt, en, es), save); }
	}

	/** One opponent. {@code rewardId} 0 means no fixed special. */
	public static final class Duel {
		public final String id;
		public final String portrait;
		public final String namePt, nameEn, nameEs;
		public final int minLevel, maxLevel;
		public final Options.AIType ai;
		public final Ruleset rules;
		public final int rewardId;
		public final boolean inTable;
		public final String rolePt, roleEn, roleEs;
		public final String stylePt, styleEn, styleEs;
		public final Line[] bio;
		public final Line[] open;
		public final Line before, win, lose;
		public final Line[] afterWin, afterLose;

		public Duel(String id, String portrait, String namePt, String nameEn, String nameEs,
				int minLevel, int maxLevel, Options.AIType ai, Ruleset rules, int rewardId, boolean inTable,
				String rolePt, String roleEn, String roleEs, String stylePt, String styleEn, String styleEs,
				Line[] bio, Line[] open, Line before, Line win, Line lose, Line[] afterWin, Line[] afterLose) {
			this.id = id;
			this.portrait = portrait;
			this.namePt = namePt;
			this.nameEn = nameEn;
			this.nameEs = nameEs;
			this.minLevel = minLevel;
			this.maxLevel = maxLevel;
			this.ai = ai;
			this.rules = rules;
			this.rewardId = rewardId;
			this.inTable = inTable;
			this.rolePt = rolePt;
			this.roleEn = roleEn;
			this.roleEs = roleEs;
			this.stylePt = stylePt;
			this.styleEn = styleEn;
			this.styleEs = styleEs;
			this.bio = bio;
			this.open = open;
			this.before = before;
			this.win = win;
			this.lose = lose;
			this.afterWin = afterWin;
			this.afterLose = afterLose;
		}

		public String name() { return I18n.text(namePt, nameEn, nameEs); }

		public String role() { return I18n.text(rolePt, roleEn, roleEs); }

		public String style() { return I18n.text(stylePt, styleEn, styleEs); }
	}

	/** A tournament stop. */
	public static final class Region {
		public final String id;
		public final String video;
		public final String background;
		public final Kind kind;
		public final int cutoff;
		public final String[][] npcWins;
		public final String namePt, nameEn, nameEs;
		public final Line[] arrival;
		public final Duel[] duels;

		public Region(String id, String video, String background, Kind kind, int cutoff, String[][] npcWins,
				String namePt, String nameEn, String nameEs, Line[] arrival, Duel[] duels) {
			this.id = id;
			this.video = video;
			this.background = background;
			this.kind = kind;
			this.cutoff = cutoff;
			this.npcWins = (npcWins == null) ? new String[0][0] : npcWins;
			this.namePt = namePt;
			this.nameEn = nameEn;
			this.nameEs = nameEs;
			this.arrival = arrival;
			this.duels = duels;
		}

		public String name() { return I18n.text(namePt, nameEn, nameEs); }
	}

	/** One row of a real group table. */
	public static final class Row {
		public final String id;
		public final String name;
		public final int points;
		public final int rank;
		public final boolean player;

		public Row(String id, String name, int points, int rank, boolean player) {
			this.id = id;
			this.name = name;
			this.points = points;
			this.rank = rank;
			this.player = player;
		}
	}

	/**
	 * Fixed prizes. These ids never enter a story hand, so the common choice
	 * cannot take them early. Mapping: duel id, character, card, catalog id.
	 * king/Quistis/Ifrit/85, queen_town/Queen/Shiva/84, raijin/Raijin/Angelo/79,
	 * fujin/Fujin/Quezacotl/83, zone/Zone/Chicobo/82, jack/CC Jack/Gilgamesh/80,
	 * rinoa/Rinoa/Siren/86, diamond/CC Diamond/Sacred/87,
	 * soldier_deling/Mara Quill/Minotaur/88, queen_deling/Queen/Alexander/95,
	 * pub/Tomas Reed/Chubby Chocobo/78, soldier_dollet/Enne Doss/MiniMog/81,
	 * queen_dollet/Queen/Diablos/90, pescador/Nash Pell/Leviathan/91,
	 * shumi/Elder Muum/Carbuncle/89, flo/Dobe and Flo/Phoenix/96,
	 * queen_fh/Queen/Doomtrain/98, cientista/Ivo Lenn/Odin/92,
	 * odine/Odine/Eden/99, seifer/Seifer/Seifer/109, queen_esthar/Queen/Bahamut/97.
	 */
	private static final int[] SPECIALS = {
		78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 95, 96, 97, 98, 99, 109
	};

	/** Clips that are not in the tree yet. The scenes still play on the still background. */
	public static final String[] PENDING_VIDEOS = {
		"chegada_dollet", "chegada_fh", "chegada_esthar", "final_historia"
	};

	public static final String PLAYER = "you";

	public static final Region[] REGIONS = StoryScript.build();

	public static final Line[] STARTER = StoryScript.starter();

	public static Line[] ceremony() { return StoryScript.ceremony(); }

	public static final String FINAL_DUEL = "queen_esthar";

	private StoryCampaign() {}

	public static boolean isSpecial(int cardId) {
		for (int i = 0; i < SPECIALS.length; i++) {
			if (SPECIALS[i] == cardId)
				return true;
		}
		return false;
	}

	static boolean isSpecialInHand(int[] hand, int specialId) {
		if (hand == null)
			return false;
		for (int i = 0; i < hand.length; i++) {
			if (hand[i] == specialId)
				return true;
		}
		return false;
	}

	public static String fill(String text, StorySave save) {
		if (text == null)
			return "";
		String name = (save == null || save.student().isEmpty()) ? "..." : save.student();
		return text.replace("{name}", name);
	}

	public static Duel findDuel(String id) {
		if (id == null)
			return null;
		for (int r = 0; r < REGIONS.length; r++) {
			Duel[] duels = REGIONS[r].duels;
			for (int i = 0; i < duels.length; i++) {
				if (duels[i].id.equals(id))
					return duels[i];
			}
		}
		return null;
	}

	public static Region regionOf(String duelId) {
		for (int r = 0; r < REGIONS.length; r++) {
			Duel[] duels = REGIONS[r].duels;
			for (int i = 0; i < duels.length; i++) {
				if (duels[i].id.equals(duelId))
					return REGIONS[r];
			}
		}
		return null;
	}

	public static int regionIndex(Region region) {
		for (int i = 0; i < REGIONS.length; i++) {
			if (REGIONS[i] == region)
				return i;
		}
		return 0;
	}

	public static int currentRegion(StorySave save) {
		if (save == null)
			return 0;
		for (int i = 0; i < REGIONS.length; i++) {
			if (!stageCleared(REGIONS[i], save))
				return i;
		}
		return REGIONS.length - 1;
	}

	public static boolean regionUnlocked(StorySave save, int index) {
		if (index <= 0)
			return true;
		if (index >= REGIONS.length || save == null)
			return false;
		return stageCleared(REGIONS[index - 1], save);
	}

	public static boolean stageCleared(Region region, StorySave save) {
		if (region == null || save == null)
			return false;
		for (int i = 0; i < region.duels.length; i++) {
			if (!save.isCleared(region.duels[i].id))
				return false;
		}
		if (region.kind == Kind.GROUP && playerRank(region, save) > region.cutoff)
			return false;
		return true;
	}

	public static boolean duelUnlocked(StorySave save, Region region, int duelIndex) {
		if (region == null || duelIndex < 0 || duelIndex >= region.duels.length)
			return false;
		if (!regionUnlocked(save, regionIndex(region)))
			return false;
		if (duelIndex == 0)
			return true;
		return save != null && save.isCleared(region.duels[duelIndex - 1].id);
	}

	public static int clearedCount(StorySave save) {
		int n = 0;
		for (int r = 0; r < REGIONS.length; r++) {
			for (int i = 0; i < REGIONS[r].duels.length; i++) {
				if (save != null && save.isCleared(REGIONS[r].duels[i].id))
					n++;
			}
		}
		return n;
	}

	public static int duelCount() {
		int n = 0;
		for (int r = 0; r < REGIONS.length; r++)
			n += REGIONS[r].duels.length;
		return n;
	}

	public static Duel nextDuel(StorySave save) {
		if (save == null)
			return REGIONS[0].duels[0];
		for (int r = 0; r < REGIONS.length; r++) {
			for (int i = 0; i < REGIONS[r].duels.length; i++) {
				if (!save.isCleared(REGIONS[r].duels[i].id))
					return REGIONS[r].duels[i];
			}
		}
		return null;
	}

	/** Group table, or an empty array when the stage is a ladder or the final. */
	public static Row[] table(Region region, StorySave save) {
		if (region == null || region.kind != Kind.GROUP)
			return new Row[0];
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(PLAYER);
		for (int i = 0; i < region.duels.length; i++) {
			if (region.duels[i].inTable)
				ids.add(region.duels[i].id);
		}
		HashMap<String, Integer> points = new HashMap<String, Integer>();
		HashMap<String, HashSet<String>> beats = new HashMap<String, HashSet<String>>();
		for (int i = 0; i < ids.size(); i++) {
			points.put(ids.get(i), Integer.valueOf(0));
			beats.put(ids.get(i), new HashSet<String>());
		}
		if (save != null) {
			for (int i = 0; i < region.duels.length; i++) {
				Duel duel = region.duels[i];
				if (!duel.inTable || !save.isCleared(duel.id))
					continue;
				addWin(points, beats, PLAYER, duel.id);
			}
		}
		for (int i = 0; i < region.npcWins.length; i++) {
			if (region.npcWins[i].length < 2)
				continue;
			addWin(points, beats, region.npcWins[i][0], region.npcWins[i][1]);
		}
		String student = (save == null || save.student().isEmpty()) ? "..." : save.student();
		Row[] rows = new Row[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(i);
			String name = PLAYER.equals(id) ? student : nameOf(id);
			rows[i] = new Row(id, name, points.get(id).intValue(), 0, PLAYER.equals(id));
		}
		Arrays.sort(rows, new Comparator<Row>() {
			@Override
			public int compare(Row a, Row b) {
				if (a.points != b.points)
					return Integer.compare(b.points, a.points);
				boolean ab = beats.get(a.id).contains(b.id);
				boolean ba = beats.get(b.id).contains(a.id);
				if (ab && !ba)
					return -1;
				if (ba && !ab)
					return 1;
				return a.name.compareTo(b.name);
			}
		});
		Row[] ranked = new Row[rows.length];
		for (int i = 0; i < rows.length; i++)
			ranked[i] = new Row(rows[i].id, rows[i].name, rows[i].points, i + 1, rows[i].player);
		return ranked;
	}

	public static int playerRank(Region region, StorySave save) {
		Row[] rows = table(region, save);
		for (int i = 0; i < rows.length; i++) {
			if (rows[i].player)
				return rows[i].rank;
		}
		return 99;
	}

	private static void addWin(Map<String, Integer> points, Map<String, HashSet<String>> beats,
			String winner, String loser) {
		if (!points.containsKey(winner) || !points.containsKey(loser) || winner.equals(loser))
			return;
		points.put(winner, Integer.valueOf(points.get(winner).intValue() + 3));
		beats.get(winner).add(loser);
	}

	private static String nameOf(String duelId) {
		Duel duel = findDuel(duelId);
		return (duel == null) ? duelId : duel.name();
	}

	/**
	 * Five non-special cards. The level band is preferred; the pool widens
	 * until five eligible cards exist.
	 */
	public static int[] rollHand(Deck deck, int minLevel, int maxLevel, Random rng) {
		if (deck == null)
			return new int[0];
		ArrayList<itdelatrisu.tripletriad.Card> cards = deck.getCards();
		int[] ids = new int[cards.size()];
		int[] levels = new int[cards.size()];
		for (int i = 0; i < cards.size(); i++) {
			ids[i] = cards.get(i).getID();
			levels[i] = cards.get(i).getLevel();
		}
		return rollHand(ids, levels, minLevel, maxLevel, rng);
	}

	public static int[] rollHand(int[] ids, int[] levels, int minLevel, int maxLevel, Random rng) {
		Random random = (rng == null) ? new Random() : rng;
		int min = Math.min(minLevel, maxLevel);
		int max = Math.max(minLevel, maxLevel);
		ArrayList<Integer> pool = new ArrayList<Integer>();
		collect(pool, ids, levels, min, max);
		int guard = 0;
		while (pool.size() < 5 && guard < 12) {
			min = Math.max(1, min - 1);
			max = max + 1;
			pool.clear();
			collect(pool, ids, levels, min, max);
			guard++;
		}
		if (pool.size() < 5) {
			pool.clear();
			collect(pool, ids, levels, 1, 99);
		}
		for (int i = pool.size() - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			Integer swap = pool.get(i);
			pool.set(i, pool.get(j));
			pool.set(j, swap);
		}
		int n = Math.min(5, pool.size());
		int[] hand = new int[n];
		for (int i = 0; i < n; i++)
			hand[i] = pool.get(i).intValue();
		return hand;
	}

	private static void collect(ArrayList<Integer> pool, int[] ids, int[] levels, int min, int max) {
		if (ids == null)
			return;
		for (int i = 0; i < ids.length; i++) {
			if (isSpecial(ids[i]))
				continue;
			int level = (levels != null && i < levels.length) ? levels[i] : 1;
			if (level < min || level > max)
				continue;
			if (!pool.contains(Integer.valueOf(ids[i])))
				pool.add(Integer.valueOf(ids[i]));
		}
	}

	public static Line tie(Duel duel) {
		return new Line(false, duel.portrait, false, duel.namePt, duel.nameEn, duel.nameEs,
			"Empatou. A mesa continua aberta.",
			"It's a draw. The table stays open.",
			"Empate. La mesa sigue abierta.");
	}
}
