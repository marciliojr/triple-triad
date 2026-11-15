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
import java.util.HashSet;

import itdelatrisu.tripletriad.story.StoryBalance.Info;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/**
 * Campaign collection progress: unique cards versus the obtainable pool,
 * and the rare prizes still missing.
 */
public final class StoryProgress {
	private StoryProgress() {}

	/** One rare that can be won from a city boss or a first rematch. */
	public static final class Rare {
		public final int cardId;
		public final String regionId;
		public final String duelId;
		public final boolean rematch;

		public Rare(int cardId, String regionId, String duelId, boolean rematch) {
			this.cardId = cardId;
			this.regionId = regionId;
			this.duelId = duelId;
			this.rematch = rematch;
		}

		public Region region() { return StoryCampaign.regionOf(duelId); }

		public Duel duel() { return StoryCampaign.findDuel(duelId); }
	}

	/** Every unique catalog id a Normal campaign can reasonably obtain. */
	public static HashSet<Integer> pool() {
		HashSet<Integer> ids = new HashSet<Integer>();
		for (int i = 0; i < StoryBalance.STARTER.length; i++)
			ids.add(Integer.valueOf(StoryBalance.STARTER[i]));
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			for (int i = 0; i < region.duels.length; i++) {
				Duel duel = region.duels[i];
				addRange(ids, duel.minLevel, duel.maxLevel);
				if (duel.rewardId > 0)
					ids.add(Integer.valueOf(duel.rewardId));
				if (StoryRematch.hasRematch(duel)) {
					ids.add(Integer.valueOf(StoryRematch.reward(duel.id)));
					Duel rematch = StoryRematch.asDuel(duel);
					addRange(ids, rematch.minLevel, rematch.maxLevel);
				}
			}
		}
		return ids;
	}

	public static int poolSize() { return pool().size(); }

	public static int uniqueOwned(StorySave save) {
		if (save == null)
			return 0;
		HashSet<Integer> seen = new HashSet<Integer>();
		ArrayList<Integer> cards = save.cards();
		for (int i = 0; i < cards.size(); i++) {
			int id = cards.get(i).intValue();
			if (id > 0)
				seen.add(Integer.valueOf(id));
		}
		return seen.size();
	}

	/** Unique owned cards that also sit in the obtainable pool, 0-100. */
	public static int percent(StorySave save) {
		HashSet<Integer> obtainable = pool();
		int total = obtainable.size();
		if (total == 0)
			return 0;
		if (save == null)
			return 0;
		int have = 0;
		HashSet<Integer> seen = new HashSet<Integer>();
		ArrayList<Integer> cards = save.cards();
		for (int i = 0; i < cards.size(); i++) {
			Integer id = cards.get(i);
			if (id == null || id.intValue() <= 0 || !seen.add(id))
				continue;
			if (obtainable.contains(id))
				have++;
		}
		return (int) Math.round(100.0 * have / total);
	}

	/** Boss and first-rematch rares, in tour order. */
	public static Rare[] rares() {
		ArrayList<Rare> out = new ArrayList<Rare>();
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			for (int i = 0; i < region.duels.length; i++) {
				Duel duel = region.duels[i];
				if (duel.rewardId > 0)
					out.add(new Rare(duel.rewardId, region.id, duel.id, false));
				int extra = StoryRematch.reward(duel.id);
				if (extra > 0)
					out.add(new Rare(extra, region.id, duel.id, true));
			}
		}
		return out.toArray(new Rare[0]);
	}

	public static Rare[] missingRares(StorySave save) {
		Rare[] all = rares();
		ArrayList<Rare> out = new ArrayList<Rare>();
		for (int i = 0; i < all.length; i++) {
			if (save == null || !save.containsCard(all[i].cardId))
				out.add(all[i]);
		}
		return out.toArray(new Rare[0]);
	}

	/** @return true when this opponent (or its rematch) has already been beaten */
	public static boolean opponentSeen(StorySave save, Rare rare) {
		if (save == null || rare == null)
			return false;
		if (save.isCleared(rare.duelId))
			return true;
		return rare.rematch && save.isCleared(StoryRematch.rematchId(rare.duelId));
	}

	private static void addRange(HashSet<Integer> ids, int min, int max) {
		int lo = Math.min(min, max);
		int hi = Math.max(min, max);
		Info[] catalog = StoryBalance.catalog();
		if (catalog == null)
			return;
		for (int i = 0; i < catalog.length; i++) {
			if (catalog[i].level >= lo && catalog[i].level <= hi)
				ids.add(Integer.valueOf(catalog[i].id));
		}
	}
}
