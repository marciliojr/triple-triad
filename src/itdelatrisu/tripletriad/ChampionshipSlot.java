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

import java.util.ArrayList;

/**
 * One championship save block on the "memory card" (exact checkpoint of a run
 * between matches: round, wins, hands, card bag, rules, play time).
 */
public class ChampionshipSlot {
	/** Round [2, 8]. */
	private int round;

	/** Wins in the run. */
	private int wins;

	/** Player hand (may be null). */
	private int[] hand;

	/** Opponent hand (may be null). */
	private int[] opponent;

	/** Run card bag. */
	private final ArrayList<Integer> bag = new ArrayList<Integer>();

	/** Active rules when saved (null = unknown, keep current rules). */
	private Rule[] rules;

	/** Play time of the run in milliseconds. */
	private long playMillis;

	/** Wall-clock time the block was written (epoch ms). */
	private long savedAt;

	/**
	 * Captures a run (with the currently active rules).
	 * @param run the run
	 * @return the block
	 */
	public static ChampionshipSlot fromRun(ChampionshipRun run) {
		ChampionshipSlot slot = new ChampionshipSlot();
		slot.round = run.getRound();
		slot.wins = run.getWins();
		slot.setHand(run.getPlayerHandIds());
		slot.setOpponent(run.getOpponentHandIds());
		slot.setBag(run.getBag());
		slot.rules = activeRules();
		slot.playMillis = run.getPlayMillis();
		slot.savedAt = System.currentTimeMillis();
		return slot;
	}

	/**
	 * Rebuilds the run and re-applies the saved rules.
	 * @param profile the profile (fallback bag)
	 * @return the run
	 */
	public ChampionshipRun toRun(Profile profile) {
		ChampionshipRun run = new ChampionshipRun();
		run.setRound(round);
		run.setWins(wins);
		run.setPlayerHandIds(hand);
		run.setOpponentHandIds(opponent);
		if (!bag.isEmpty())
			run.setBag(bag);
		else if (profile != null)
			run.setBag(profile.getCollection());
		run.setPlayMillis(playMillis);
		if (rules != null) {
			for (Rule r : Rule.values())
				r.setState(false);
			for (Rule r : rules)
				r.setState(true);
		}
		return run;
	}

	/** @return the currently active rules */
	private static Rule[] activeRules() {
		ArrayList<Rule> on = new ArrayList<Rule>();
		for (Rule r : Rule.values()) {
			if (r.isActive())
				on.add(r);
		}
		return on.toArray(new Rule[0]);
	}

	/** @return true if the block holds a resumable run */
	public boolean isValid() { return round >= 2; }

	public int getRound() { return round; }
	public void setRound(int round) { this.round = Math.max(0, round); }

	public int getWins() { return wins; }
	public void setWins(int wins) { this.wins = Math.max(0, wins); }

	public int[] getHand() { return (hand != null) ? hand.clone() : null; }
	public void setHand(int[] ids) { this.hand = (ids != null && ids.length > 0) ? ids.clone() : null; }

	public int[] getOpponent() { return (opponent != null) ? opponent.clone() : null; }
	public void setOpponent(int[] ids) { this.opponent = (ids != null && ids.length > 0) ? ids.clone() : null; }

	public ArrayList<Integer> getBag() { return bag; }

	public void setBag(ArrayList<Integer> cards) {
		bag.clear();
		if (cards == null)
			return;
		for (Integer id : cards) {
			if (id != null && id.intValue() > 0)
				bag.add(id);
		}
	}

	public long getPlayMillis() { return playMillis; }
	public void setPlayMillis(long ms) { this.playMillis = Math.max(0L, ms); }

	public long getSavedAt() { return savedAt; }
	public void setSavedAt(long t) { this.savedAt = t; }

	/** @return the saved rules, or null if unknown */
	public Rule[] getRules() { return (rules != null) ? rules.clone() : null; }

	/**
	 * Parses rules from a comma-separated list of names (unknown names ignored).
	 * @param value the list, or null
	 */
	public void setRulesFromString(String value) {
		if (value == null) {
			rules = null;
			return;
		}
		ArrayList<Rule> on = new ArrayList<Rule>();
		for (String token : value.split(",")) {
			String t = token.trim();
			if (t.isEmpty())
				continue;
			try {
				on.add(Rule.valueOf(t));
			} catch (IllegalArgumentException e) {
				// ignore unknown rule
			}
		}
		rules = on.toArray(new Rule[0]);
	}

	/** @return the rules as a comma-separated list ("" if none / unknown) */
	public String rulesToString() {
		if (rules == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rules.length; i++) {
			if (i > 0)
				sb.append(',');
			sb.append(rules[i].name());
		}
		return sb.toString();
	}

	/** @return true if the saved rules are known */
	public boolean hasRules() { return rules != null; }
}
