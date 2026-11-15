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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.gfx.Assets;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/**
 * Campaign deck, starter list, opponent hands, special rolls and save migration.
 * The profile album is never read or written here.
 */
public final class StoryBalance {
	/** Level-1 and level-2 cards that open a new campaign. No rares. */
	public static final int[] STARTER = { 1, 4, 9, 11, 12, 15, 17, 22 };

	/** Catalog names of {@link #STARTER}, documented for the memory-bank. */
	public static final String[] STARTER_NAMES = {
		"Geezard", "Red Bat", "Blood Soul", "Cockatrice",
		"Grat", "Glacial Eye", "Thrustaevis", "Grand Mantis"
	};

	/** Chance that a common opponent carries one above-curve card. */
	public static final int SPECIAL_CHANCE = 18;

	/** At most one such card among the commons of a single city. */
	public static final int MAX_SPECIALS_PER_REGION = 1;

	/** Catalog level at which GF and character cards start. */
	public static final int RARE_LEVEL = 8;

	/**
	 * Specials granted by the pre-4 reward table, used only when migrating
	 * a campaign that already cleared those duels.
	 */
	private static final Map<String, Integer> LEGACY_REWARDS = legacyRewards();

	private static Info[] catalog;
	private static Map<Integer, Info> byId;

	private StoryBalance() {}

	/** One catalog row. {@code power} is the sum of the four ranks (A = 10). */
	public static final class Info {
		public final int id;
		public final String name;
		public final int level;
		public final int power;

		public Info(int id, String name, int level, int power) {
			this.id = id;
			this.name = name;
			this.level = level;
			this.power = power;
		}
	}

	public static Info[] catalog() {
		if (catalog == null)
			loadCatalog();
		return catalog;
	}

	public static Info info(int id) {
		if (byId == null)
			loadCatalog();
		return byId.get(Integer.valueOf(id));
	}

	public static int levelOf(int id) {
		Info card = info(id);
		return card == null ? 0 : card.level;
	}

	public static int powerOf(int id) {
		Info card = info(id);
		return card == null ? 0 : card.power;
	}

	public static boolean isRare(int id) {
		return levelOf(id) >= RARE_LEVEL;
	}

	/** Fills a brand-new campaign with the starter list and a stable seed. */
	public static void grantStarter(StorySave save) {
		if (save == null)
			return;
		if (save.seed() == 0L)
			save.setSeed(seedFrom(save));
		save.clearCollection();
		for (int i = 0; i < STARTER.length; i++)
			save.addCard(STARTER[i]);
		save.setCollectionReady(true);
	}

	/**
	 * Gives existing campaigns a playable collection: the starter plus one
	 * stand-in per cleared duel and any special already earned under the old table.
	 * Pending rewards are left untouched. Already-ready collections are skipped.
	 */
	public static void migrateCollection(StorySave save) {
		if (save == null)
			return;
		save.remapDuelIds();
		if (save.isCollectionReady())
			return;
		if (save.seed() == 0L)
			save.setSeed(seedFrom(save));
		boolean empty = save.collectionSize() == 0;
		if (empty)
			grantStarter(save);
		String[] cleared = save.clearedCsv().isEmpty() ? new String[0] : save.clearedCsv().split(",");
		for (int i = 0; i < cleared.length; i++) {
			String id = StoryCampaign.canonicalId(cleared[i].trim());
			if (id.isEmpty())
				continue;
			Integer legacy = LEGACY_REWARDS.get(id);
			if (legacy != null)
				save.addCard(legacy.intValue());
			Duel duel = StoryCampaign.findDuel(id);
			if (duel != null) {
				int standIn = standInCard(duel, save);
				if (standIn > 0)
					save.addCard(standIn);
			}
			markRulesOf(save, duel);
		}
		save.setCollectionReady(true);
		save.touch();
	}

	/** @return five card IDs the opponent will play and offer as the prize */
	public static int[] opponentHand(StorySave save, Duel duel) {
		if (duel == null)
			return new int[0];
		Random rng = new Random(handSeed(save, duel.id));
		int min = levelMin(duel, save);
		int max = levelMax(duel, save);
		ArrayList<Info> pool = pool(min, max, max >= RARE_LEVEL);
		widen(pool, min, max, max >= RARE_LEVEL);
		shuffle(pool, rng);
		int n = Math.min(5, pool.size());
		int[] hand = new int[n];
		for (int i = 0; i < n; i++)
			hand[i] = pool.get(i).id;
		boolean bossLike = duel.isBoss() || StoryRematch.isRematchId(duel.id);
		if (!bossLike && carriesSpecial(save, duel)) {
			int special = pickSpecial(duel, hand, rng);
			if (special > 0)
				hand[weakest(hand)] = special;
		}
		return hand;
	}

	/** Lower level bound, after rematch bump and Easy/Hard. */
	public static int levelMin(Duel duel, StorySave save) {
		if (duel == null)
			return 1;
		int min = Math.min(duel.minLevel, duel.maxLevel);
		int max = levelMax(duel, save);
		return Math.min(min, max);
	}

	/** Upper level bound, after rematch bump and Easy/Hard (capped at 10). */
	public static int levelMax(Duel duel, StorySave save) {
		if (duel == null)
			return 1;
		int max = Math.max(duel.minLevel, duel.maxLevel);
		if (save != null && save.isEasy())
			max = Math.max(1, max - 1);
		else if (save != null && save.isHard())
			max = Math.min(10, max + 1);
		return max;
	}

	/** AI used in a story match, after difficulty (and rematch seats already bumped). */
	public static Options.AIType aiFor(Duel duel, StorySave save) {
		Options.AIType ai = (duel == null || duel.ai == null) ? Options.AIType.BALANCED : duel.ai;
		if (save == null || save.difficulty() == StorySave.Difficulty.NORMAL)
			return ai;
		if (save.isEasy()) {
			if (ai == Options.AIType.OFFENSIVE)
				return Options.AIType.BALANCED;
			if (ai == Options.AIType.BALANCED)
				return Options.AIType.DEFENSIVE;
			return ai;
		}
		if (ai == Options.AIType.DEFENSIVE)
			return Options.AIType.BALANCED;
		if (ai == Options.AIType.BALANCED)
			return Options.AIType.OFFENSIVE;
		return ai;
	}

	/** @return true when this common opponent is the one (at most one per city) that carries the extra card */
	public static boolean carriesSpecial(StorySave save, Duel duel) {
		if (save == null || duel == null || duel.isBoss())
			return false;
		Region region = StoryCampaign.regionOf(duel.id);
		if (region == null)
			return false;
		int cap = specialCap(duel);
		if (cap <= 0)
			return false;
		Random rng = new Random(specialSeed(save, region.id));
		int taken = 0;
		for (int i = 0; i < region.duels.length; i++) {
			Duel other = region.duels[i];
			if (other.isBoss())
				continue;
			if (taken >= MAX_SPECIALS_PER_REGION)
				return false;
			boolean hit = rng.nextInt(100) < SPECIAL_CHANCE;
			if (!hit)
				continue;
			taken++;
			if (other.id.equals(duel.id))
				return true;
		}
		return false;
	}

	/**
	 * Highest level a common may smuggle. Never above the next boss's max level,
	 * and never a rare before that boss also sits at rare level.
	 */
	public static int specialCap(Duel duel) {
		Duel next = nextBoss(duel);
		if (next == null)
			return 0;
		int cap = next.maxLevel;
		if (cap < RARE_LEVEL)
			return cap;
		return cap;
	}

	/** @return the next city boss after this duel, or the boss of this city if still ahead */
	public static Duel nextBoss(Duel duel) {
		if (duel == null)
			return null;
		boolean passed = false;
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Duel[] duels = StoryCampaign.REGIONS[r].duels;
			for (int i = 0; i < duels.length; i++) {
				if (!passed) {
					if (duels[i].id.equals(duel.id))
						passed = true;
					continue;
				}
				if (duels[i].isBoss())
					return duels[i];
			}
		}
		return null;
	}

	/** Best-five power of the cards a player is expected to hold at the start of a region. */
	public static int expectedPlayerPower(int regionIndex) {
		ArrayList<Integer> bag = expectedCollection(regionIndex);
		return bestFive(bag);
	}

	/** Typical opponent power for a duel (average of several seeded rolls when specials apply). */
	public static int typicalOpponentPower(Duel duel) {
		if (duel == null)
			return 0;
		StorySave probe = StorySave.create("Probe");
		if (probe == null)
			return 0;
		// use a few seeds so a lucky special does not dominate the average
		int total = 0;
		int n = 8;
		for (int i = 0; i < n; i++) {
			probe.setSeed(1L + (long) i * 17L);
			int[] hand = opponentHand(probe, duel);
			total += powerSum(hand);
		}
		return total / n;
	}

	/** Starter plus one stand-in (and the boss card) for every duel cleared before this region. */
	public static ArrayList<Integer> expectedCollection(int regionIndex) {
		ArrayList<Integer> bag = new ArrayList<Integer>();
		for (int i = 0; i < STARTER.length; i++)
			bag.add(Integer.valueOf(STARTER[i]));
		int last = Math.max(0, Math.min(regionIndex, StoryCampaign.REGIONS.length));
		for (int r = 0; r < last; r++) {
			Duel[] duels = StoryCampaign.REGIONS[r].duels;
			for (int i = 0; i < duels.length; i++) {
				Duel duel = duels[i];
				if (duel.rewardId > 0)
					bag.add(Integer.valueOf(duel.rewardId));
				int standIn = typicalPick(duel, bag);
				if (standIn > 0)
					bag.add(Integer.valueOf(standIn));
			}
		}
		return bag;
	}

	public static int bestFive(ArrayList<Integer> bag) {
		if (bag == null || bag.isEmpty())
			return 0;
		int[] powers = new int[bag.size()];
		for (int i = 0; i < bag.size(); i++)
			powers[i] = powerOf(bag.get(i).intValue());
		java.util.Arrays.sort(powers);
		int sum = 0;
		int n = Math.min(5, powers.length);
		for (int i = 0; i < n; i++)
			sum += powers[powers.length - 1 - i];
		return sum;
	}

	public static int powerSum(int[] ids) {
		if (ids == null)
			return 0;
		int sum = 0;
		for (int i = 0; i < ids.length; i++)
			sum += powerOf(ids[i]);
		return sum;
	}

	public static String ruleKey(Rule rule) {
		return rule == null ? "" : rule.name();
	}

	private static long seedFrom(StorySave save) {
		long mix = (save.id() == null ? 0 : save.id().hashCode());
		mix = (mix << 32) ^ save.createdAt();
		if (mix == 0L)
			mix = 1L;
		return mix;
	}

	private static long handSeed(StorySave save, String duelId) {
		long seed = (save == null ? 1L : save.seed());
		long duel = (duelId == null ? 0 : duelId.hashCode());
		return seed ^ (duel * 0x9E3779B97F4A7C15L) ^ 0xD1B54A32D192ED03L;
	}

	private static long specialSeed(StorySave save, String regionId) {
		long seed = (save == null ? 1L : save.seed());
		long region = (regionId == null ? 0 : regionId.hashCode());
		return seed ^ (region * 0xBF58476D1CE4E5B9L) ^ 0x51A11E5L;
	}

	private static ArrayList<Info> pool(int min, int max, boolean rares) {
		Info[] all = catalog();
		ArrayList<Info> pool = new ArrayList<Info>();
		for (int i = 0; i < all.length; i++) {
			if (all[i].level < min || all[i].level > max)
				continue;
			if (!rares && all[i].level >= RARE_LEVEL)
				continue;
			pool.add(all[i]);
		}
		return pool;
	}

	private static void widen(ArrayList<Info> pool, int min, int max, boolean rares) {
		int guard = 0;
		int lo = min;
		int hi = max;
		while (pool.size() < 5 && guard < 12) {
			lo = Math.max(1, lo - 1);
			hi = Math.min(10, hi + 1);
			pool.clear();
			pool.addAll(pool(lo, hi, rares || hi >= RARE_LEVEL));
			guard++;
		}
		if (pool.size() < 5) {
			pool.clear();
			pool.addAll(pool(1, 10, true));
		}
	}

	private static int pickSpecial(Duel duel, int[] hand, Random rng) {
		int cap = specialCap(duel);
		if (cap <= 0)
			return 0;
		boolean rares = cap >= RARE_LEVEL;
		int lo = Math.max(duel.maxLevel + 1, 1);
		if (lo > cap)
			lo = cap;
		ArrayList<Info> pool = pool(lo, cap, rares);
		if (pool.isEmpty())
			pool = pool(Math.max(1, cap - 1), cap, rares);
		for (int i = pool.size() - 1; i >= 0; i--) {
			if (contains(hand, pool.get(i).id))
				pool.remove(i);
		}
		if (pool.isEmpty())
			return 0;
		return pool.get(rng.nextInt(pool.size())).id;
	}

	private static int weakest(int[] hand) {
		int w = 0;
		for (int i = 1; i < hand.length; i++) {
			if (powerOf(hand[i]) < powerOf(hand[w]))
				w = i;
		}
		return w;
	}

	private static int standInCard(Duel duel, StorySave save) {
		return typicalPick(duel, save == null ? new ArrayList<Integer>() : save.cards());
	}

	private static int typicalPick(Duel duel, ArrayList<Integer> held) {
		if (duel == null)
			return 0;
		ArrayList<Info> pool = pool(duel.minLevel, duel.maxLevel, duel.maxLevel >= RARE_LEVEL);
		if (pool.isEmpty())
			return 0;
		Info best = null;
		for (int i = 0; i < pool.size(); i++) {
			Info card = pool.get(i);
			if (held != null && held.contains(Integer.valueOf(card.id)))
				continue;
			if (card.level >= RARE_LEVEL && !duel.isBoss())
				continue;
			if (best == null || card.power > best.power)
				best = card;
		}
		return best == null ? 0 : best.id;
	}

	private static void markRulesOf(StorySave save, Duel duel) {
		if (save == null || duel == null || duel.rules == null)
			return;
		for (int i = 0; i < duel.rules.on.length; i++)
			save.markRuleSeen(ruleKey(duel.rules.on[i]));
		save.markRuleSeen("TRADE");
	}

	private static boolean contains(int[] ids, int id) {
		if (ids == null)
			return false;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == id)
				return true;
		}
		return false;
	}

	private static void shuffle(ArrayList<Info> list, Random rng) {
		for (int i = list.size() - 1; i > 0; i--) {
			int j = rng.nextInt(i + 1);
			Info swap = list.get(i);
			list.set(i, list.get(j));
			list.set(j, swap);
		}
	}

	private static Map<String, Integer> legacyRewards() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("king", Integer.valueOf(85));
		map.put("queen_town", Integer.valueOf(84));
		map.put("raijin", Integer.valueOf(79));
		map.put("fujin", Integer.valueOf(83));
		map.put("zone", Integer.valueOf(82));
		map.put("jack", Integer.valueOf(80));
		map.put("rinoa", Integer.valueOf(86));
		map.put("diamond", Integer.valueOf(87));
		map.put("soldier_deling", Integer.valueOf(88));
		map.put("queen_deling", Integer.valueOf(95));
		map.put("pub", Integer.valueOf(78));
		map.put("soldier_dollet", Integer.valueOf(81));
		map.put("queen_dollet", Integer.valueOf(90));
		map.put("pescador", Integer.valueOf(91));
		map.put("shumi", Integer.valueOf(89));
		map.put("flo", Integer.valueOf(96));
		map.put("queen_fh", Integer.valueOf(98));
		map.put("cientista", Integer.valueOf(92));
		map.put("odine", Integer.valueOf(99));
		map.put("seifer", Integer.valueOf(109));
		map.put("queen_esthar", Integer.valueOf(97));
		map.put("final", Integer.valueOf(97));
		return map;
	}

	private static synchronized void loadCatalog() {
		if (catalog != null)
			return;
		ArrayList<Info> list = new ArrayList<Info>();
		File file = Assets.file("deck.txt");
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\t");
				if (tokens.length != 5)
					continue;
				try {
					int id = Integer.parseInt(tokens[0]);
					String ranks = tokens[2];
					int level = Integer.parseInt(tokens[4]);
					int power = 0;
					for (int i = 0; i < ranks.length(); i++) {
						char c = ranks.charAt(i);
						power += (c == 'A' || c == 'a') ? 10 : Character.digit(c, 10);
					}
					list.add(new Info(id, tokens[1], level, power));
				} catch (NumberFormatException e) {
					// skip
				}
			}
		} catch (Exception e) {
			catalog = new Info[0];
			byId = new HashMap<Integer, Info>();
			return;
		}
		catalog = list.toArray(new Info[0]);
		byId = new HashMap<Integer, Info>();
		for (int i = 0; i < catalog.length; i++)
			byId.put(Integer.valueOf(catalog[i].id), catalog[i]);
	}
}
