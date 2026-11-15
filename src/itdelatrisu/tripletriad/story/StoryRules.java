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
import java.util.Random;

import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/**
 * FF8-style regional rules for the story campaign.
 * Each city has a native set that can gain a carried rule after a match.
 * {@link Rule#SUDDEN_DEATH} stays at the final and never spreads.
 * Random is not used in the campaign and never spreads.
 */
public final class StoryRules {
	/** Rules that may be carried or added by the Queen. Combo follows Same/Plus. */
	public static final Rule[] SPREADABLE = {
		Rule.OPEN, Rule.SAME, Rule.SAME_WALL, Rule.PLUS, Rule.ELEMENTAL
	};

	/** Base chance, in percent, that a carried rule lands after the first match in a city. */
	public static final int BASE_CHANCE = 15;

	/** Extra percent per previous match already played in that city. */
	public static final int CHANCE_PER_PLAY = 8;

	/** Upper cap for the spread roll. */
	public static final int MAX_CHANCE = 50;

	private StoryRules() {}

	/** Native rules of a city at the start of a campaign (and after a format-4 load). */
	public static Rule[] defaults(String regionId) {
		if (regionId == null)
			return new Rule[] { Rule.OPEN, Rule.SAME, Rule.PLUS, Rule.COMBO };
		switch (regionId) {
			case "garden":
				return new Rule[] { Rule.OPEN, Rule.SAME, Rule.PLUS, Rule.COMBO };
			case "town":
				return new Rule[] { Rule.SAME, Rule.PLUS, Rule.COMBO };
			case "timber":
			case "deling":
			case "dollet":
				return new Rule[] { Rule.SAME, Rule.PLUS, Rule.COMBO, Rule.ELEMENTAL };
			case "fh":
			case "esthar":
				return new Rule[] { Rule.SAME, Rule.PLUS, Rule.COMBO, Rule.ELEMENTAL, Rule.SAME_WALL };
			case "final":
				return new Rule[] { Rule.SAME, Rule.SAME_WALL, Rule.PLUS, Rule.COMBO,
					Rule.ELEMENTAL, Rule.SUDDEN_DEATH };
			default:
				return new Rule[] { Rule.SAME, Rule.PLUS, Rule.COMBO };
		}
	}

	/** @return the city's current rules, or the native set when none were saved */
	public static Rule[] effective(StorySave save, String regionId) {
		if (save != null) {
			Rule[] stored = save.cityRules(regionId);
			if (stored != null && stored.length > 0)
				return withCombo(stored);
		}
		return defaults(regionId);
	}

	public static Rule[] effective(StorySave save, Region region) {
		return effective(save, region == null ? null : region.id);
	}

	/** @return true if the city currently has this rule */
	public static boolean has(StorySave save, String regionId, Rule rule) {
		if (rule == null)
			return false;
		Rule[] on = effective(save, regionId);
		for (int i = 0; i < on.length; i++) {
			if (on[i] == rule)
				return true;
		}
		return false;
	}

	/**
	 * Chance that a carried rule spreads, after {@code plays} matches in the city
	 * (including the one that just ended).
	 */
	public static int chance(int plays) {
		if (plays <= 0)
			return 0;
		int value = BASE_CHANCE + CHANCE_PER_PLAY * (plays - 1);
		return Math.min(MAX_CHANCE, value);
	}

	/**
	 * Updates play count, maybe spreads the carried rule into this city, then
	 * picks a new rule to carry. Deterministic when {@code rng} is seeded.
	 * @return a spread event, or {@code null} if nothing landed
	 */
	public static Event afterMatch(StorySave save, String regionId, Random rng) {
		if (save == null || regionId == null || regionId.isEmpty())
			return null;
		save.addPlay(regionId);
		int plays = save.plays(regionId);
		Rule carry = save.carry();
		Event event = null;
		if (carry != null && canSpread(carry) && !has(save, regionId, carry)) {
			int roll = (rng == null) ? 100 : rng.nextInt(100);
			if (roll < chance(plays)) {
				addRule(save, regionId, carry);
				event = new Event(carry, regionId);
			}
		}
		save.setCarry(pickCarry(save, regionId, rng));
		return event;
	}

	/** Same as {@link #afterMatch(StorySave, String, Random)} with a save-seeded roll. */
	public static Event afterMatch(StorySave save, String regionId) {
		if (save == null)
			return null;
		long mix = save.seed() ^ ((long) save.plays(regionId) * 0x9E3779B9L)
			^ (regionId == null ? 0L : regionId.hashCode());
		return afterMatch(save, regionId, new Random(mix));
	}

	public static boolean canSpread(Rule rule) {
		if (rule == null || rule == Rule.SUDDEN_DEATH)
			return false;
		for (int i = 0; i < SPREADABLE.length; i++) {
			if (SPREADABLE[i] == rule)
				return true;
		}
		return false;
	}

	/** Adds a spreadable rule (and Combo when Same or Plus arrives). */
	public static void addRule(StorySave save, String regionId, Rule rule) {
		if (save == null || regionId == null || !canSpread(rule))
			return;
		Rule[] now = effective(save, regionId);
		if (contains(now, rule)) {
			save.setCityRules(regionId, withCombo(now));
			return;
		}
		Rule[] next = new Rule[now.length + 1];
		System.arraycopy(now, 0, next, 0, now.length);
		next[now.length] = rule;
		save.setCityRules(regionId, withCombo(next));
	}

	/** Removes a rule if the table would still have a capture rule. Never drops Sudden Death. */
	public static boolean removeRule(StorySave save, String regionId, Rule rule) {
		if (save == null || regionId == null || rule == null || rule == Rule.SUDDEN_DEATH)
			return false;
		if (!has(save, regionId, rule))
			return false;
		Rule[] now = effective(save, regionId);
		ArrayList<Rule> keep = new ArrayList<Rule>();
		for (int i = 0; i < now.length; i++) {
			if (now[i] != rule)
				keep.add(now[i]);
		}
		Rule[] next = keep.toArray(new Rule[0]);
		if (rule == Rule.SAME || rule == Rule.PLUS)
			next = withCombo(next);
		if (!hasCapture(next))
			return false;
		save.setCityRules(regionId, next);
		return true;
	}

	/** What the Queen can offer in this city, once. */
	public static QueenOffer offer(StorySave save, String regionId) {
		Rule add = null;
		Rule[] addOrder = addPriority(regionId);
		for (int i = 0; i < addOrder.length; i++) {
			if (!has(save, regionId, addOrder[i]) && canAdd(regionId, addOrder[i])) {
				add = addOrder[i];
				break;
			}
		}
		Rule remove = null;
		Rule[] remOrder = { Rule.ELEMENTAL, Rule.SAME_WALL, Rule.OPEN, Rule.PLUS, Rule.COMBO };
		for (int i = 0; i < remOrder.length; i++) {
			if (wouldRemainPlayable(save, regionId, remOrder[i])) {
				remove = remOrder[i];
				break;
			}
		}
		if (add == null && remove == null)
			return null;
		return new QueenOffer(add, remove);
	}

	public static boolean applyQueen(StorySave save, String regionId, Rule add, Rule remove) {
		if (save == null || regionId == null || save.queenUsed(regionId))
			return false;
		boolean changed = false;
		if (add != null && canAdd(regionId, add) && !has(save, regionId, add)) {
			addRule(save, regionId, add);
			changed = true;
		}
		if (remove != null)
			changed = removeRule(save, regionId, remove) || changed;
		save.markQueenUsed(regionId);
		return changed;
	}

	/** Official table name, used in every language (no typographic punctuation). */
	public static String label(Rule rule) {
		if (rule == null)
			return "";
		switch (rule) {
			case OPEN: return "Open";
			case SAME: return "Same";
			case SAME_WALL: return "Same Wall";
			case PLUS: return "Plus";
			case COMBO: return "Combo";
			case ELEMENTAL: return "Elemental";
			case SUDDEN_DEATH: return "Sudden Death";
			default: return rule.name();
		}
	}

	public static String summarize(Rule[] rules) {
		if (rules == null || rules.length == 0)
			return "-";
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < rules.length; i++) {
			if (i > 0)
				out.append(", ");
			out.append(label(rules[i]));
		}
		return out.toString();
	}

	public static Rule parseRule(String name) {
		if (name == null || name.isEmpty())
			return null;
		String key = name.trim().toUpperCase().replace(' ', '_');
		if (key.equals("SAMEWALL"))
			key = "SAME_WALL";
		if (key.equals("SUDDENDEATH") || key.equals("SUDDEN_DEATH"))
			return Rule.SUDDEN_DEATH;
		try {
			return Rule.valueOf(key);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static String formatRules(Rule[] rules) {
		if (rules == null || rules.length == 0)
			return "";
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < rules.length; i++) {
			if (i > 0)
				out.append('+');
			out.append(rules[i].name());
		}
		return out.toString();
	}

	public static Rule[] parseRules(String csv) {
		if (csv == null || csv.isEmpty())
			return new Rule[0];
		String[] parts = csv.split("[+,]");
		ArrayList<Rule> out = new ArrayList<Rule>();
		for (int i = 0; i < parts.length; i++) {
			Rule rule = parseRule(parts[i]);
			if (rule != null && !contains(out.toArray(new Rule[0]), rule))
				out.add(rule);
		}
		return out.toArray(new Rule[0]);
	}

	private static Rule pickCarry(StorySave save, String regionId, Random rng) {
		Rule[] on = effective(save, regionId);
		ArrayList<Rule> pool = new ArrayList<Rule>();
		for (int i = 0; i < on.length; i++) {
			if (canSpread(on[i]))
				pool.add(on[i]);
		}
		if (pool.isEmpty())
			return null;
		Rule signature = signature(regionId);
		if (signature != null && pool.contains(signature))
			return signature;
		if (rng == null)
			return pool.get(0);
		return pool.get(rng.nextInt(pool.size()));
	}

	private static Rule signature(String regionId) {
		if (regionId == null)
			return Rule.PLUS;
		switch (regionId) {
			case "garden": return Rule.OPEN;
			case "town": return Rule.PLUS;
			case "timber":
			case "deling":
			case "dollet":
				return Rule.ELEMENTAL;
			case "fh":
			case "esthar":
			case "final":
				return Rule.SAME_WALL;
			default: return Rule.PLUS;
		}
	}

	private static Rule[] addPriority(String regionId) {
		if (canAdd(regionId, Rule.SAME_WALL))
			return new Rule[] { Rule.SAME_WALL, Rule.OPEN, Rule.ELEMENTAL, Rule.PLUS, Rule.SAME };
		return new Rule[] { Rule.OPEN, Rule.ELEMENTAL, Rule.PLUS, Rule.SAME };
	}

	/** Same Wall is only offered from Timber onward, so the qualifier stays readable. */
	private static boolean canAdd(String regionId, Rule rule) {
		if (!canSpread(rule))
			return false;
		if (rule == Rule.SAME_WALL)
			return indexOf(regionId) >= 2;
		return true;
	}

	private static int indexOf(String regionId) {
		if (regionId == null)
			return 0;
		for (int i = 0; i < StoryCampaign.REGIONS.length; i++) {
			if (regionId.equals(StoryCampaign.REGIONS[i].id))
				return i;
		}
		return 0;
	}

	private static boolean wouldRemainPlayable(StorySave save, String regionId, Rule remove) {
		if (!has(save, regionId, remove) || remove == Rule.SUDDEN_DEATH)
			return false;
		Rule[] now = effective(save, regionId);
		ArrayList<Rule> keep = new ArrayList<Rule>();
		for (int i = 0; i < now.length; i++) {
			if (now[i] != remove)
				keep.add(now[i]);
		}
		Rule[] next = keep.toArray(new Rule[0]);
		if (remove == Rule.SAME || remove == Rule.PLUS)
			next = withCombo(next);
		return hasCapture(next);
	}

	private static Rule[] withCombo(Rule[] rules) {
		boolean sameOrPlus = contains(rules, Rule.SAME) || contains(rules, Rule.PLUS);
		boolean hasCombo = contains(rules, Rule.COMBO);
		if (sameOrPlus && !hasCombo) {
			Rule[] next = new Rule[rules.length + 1];
			System.arraycopy(rules, 0, next, 0, rules.length);
			next[rules.length] = Rule.COMBO;
			return next;
		}
		if (!sameOrPlus && hasCombo) {
			ArrayList<Rule> keep = new ArrayList<Rule>();
			for (int i = 0; i < rules.length; i++) {
				if (rules[i] != Rule.COMBO)
					keep.add(rules[i]);
			}
			return keep.toArray(new Rule[0]);
		}
		return rules;
	}

	private static boolean hasCapture(Rule[] rules) {
		return contains(rules, Rule.SAME) || contains(rules, Rule.PLUS) || contains(rules, Rule.ELEMENTAL)
			|| contains(rules, Rule.OPEN);
	}

	private static boolean contains(Rule[] rules, Rule rule) {
		if (rules == null)
			return false;
		for (int i = 0; i < rules.length; i++) {
			if (rules[i] == rule)
				return true;
		}
		return false;
	}

	/** A rule that just landed in a city. */
	public static final class Event {
		public final Rule rule;
		public final String regionId;

		public Event(Rule rule, String regionId) {
			this.rule = rule;
			this.regionId = regionId;
		}
	}

	/** Optional add and/or remove the Queen can apply in one visit. */
	public static final class QueenOffer {
		public final Rule add;
		public final Rule remove;

		public QueenOffer(Rule add, Rule remove) {
			this.add = add;
			this.remove = remove;
		}
	}
}
