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
 * In-memory championship gauntlet (8 rounds). Not persisted mid-match.
 */
public class ChampionshipRun {
	/** Number of rounds to win the cup. */
	public static final int ROUNDS = 8;

	/** Current round [1, 8]. */
	private int round = 1;

	/** Wins in this run. */
	private int wins;

	/** Player hand IDs for the current match. */
	private int[] playerHandIds;

	/** Opponent hand IDs for the current round (kept on retry). */
	private int[] opponentHandIds;

	/** Cards in this run (temporary pack or a copy of Meu Deck). */
	private final ArrayList<Integer> bag = new ArrayList<Integer>();

	/**
	 * Sets the current round (1-based).
	 * @param round the round
	 */
	public void setRound(int round) {
		this.round = (round < 1) ? 1 : round;
	}

	/**
	 * Sets wins in this run.
	 * @param wins the wins
	 */
	public void setWins(int wins) {
		this.wins = (wins < 0) ? 0 : wins;
	}

	/**
	 * Sets the opponent hand IDs (used when restoring a save).
	 * @param ids the IDs
	 */
	public void setOpponentHandIds(int[] ids) {
		this.opponentHandIds = (ids != null) ? ids.clone() : null;
	}

	/**
	 * Restores a run from a profile save.
	 * @param profile the profile
	 * @return the run
	 */
	public static ChampionshipRun fromProfile(Profile profile) {
		ChampionshipRun run = new ChampionshipRun();
		if (profile == null)
			return run;
		run.setRound(profile.getRunRound());
		run.setWins(profile.getRunWins());
		run.setPlayerHandIds(profile.getRunHand());
		run.setOpponentHandIds(profile.getRunOpponent());
		ArrayList<Integer> saved = profile.getRunBag();
		if (saved != null && !saved.isEmpty())
			run.setBag(saved);
		else
			run.setBag(profile.getCollection());
		return run;
	}

	/**
	 * Returns this run's card bag.
	 * @return the list
	 */
	public ArrayList<Integer> getBag() { return bag; }

	/**
	 * Replaces this run's card bag.
	 * @param cards the IDs
	 */
	public void setBag(ArrayList<Integer> cards) {
		bag.clear();
		if (cards == null)
			return;
		for (int i = 0; i < cards.size(); i++) {
			int id = cards.get(i).intValue();
			if (id > 0)
				bag.add(Integer.valueOf(id));
		}
	}

	/**
	 * Adds a card to this run's bag.
	 * @param cardId the catalog ID
	 */
	public void addToBag(int cardId) {
		if (cardId > 0)
			bag.add(Integer.valueOf(cardId));
	}

	/**
	 * Removes the first instance of a card ID from this run's bag.
	 * @param cardId the catalog ID
	 * @return true if a card was removed
	 */
	public boolean removeFromBag(int cardId) {
		for (int i = 0; i < bag.size(); i++) {
			if (bag.get(i).intValue() == cardId) {
				bag.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the current round (1-based).
	 * @return the round
	 */
	public int getRound() { return round; }

	/**
	 * Returns wins in this run.
	 * @return the wins
	 */
	public int getWins() { return wins; }

	/**
	 * Returns the player's current hand IDs.
	 * @return the IDs, or null
	 */
	public int[] getPlayerHandIds() { return playerHandIds; }

	/**
	 * Sets the player's current hand IDs.
	 * @param ids five card IDs
	 */
	public void setPlayerHandIds(int[] ids) {
		this.playerHandIds = (ids != null) ? ids.clone() : null;
	}

	/**
	 * Returns the opponent's current hand IDs.
	 * @return the IDs, or null
	 */
	public int[] getOpponentHandIds() { return opponentHandIds; }

	/**
	 * Rolls a new opponent hand for the current round.
	 * @param catalog the card catalog
	 */
	public void rollOpponent(Deck catalog) {
		this.opponentHandIds = catalog.createRandomHandIds();
	}

	/**
	 * Returns the AI type for the current round (not written to the cfg).
	 * @return the AI type
	 */
	public Options.AIType aiForRound() {
		if (round <= 2)
			return Options.AIType.RANDOM;
		if (round <= 5)
			return Options.AIType.BALANCED;
		if (round <= 7)
			return Options.AIType.OFFENSIVE;
		return Options.AIType.DEFENSIVE;
	}

	/**
	 * Records a round win and advances. Returns true if the cup is won.
	 * @return true if wins reached {@link #ROUNDS}
	 */
	public boolean recordWin() {
		wins++;
		if (wins >= ROUNDS)
			return true;
		round++;
		return false;
	}

	/**
	 * Picks the opponent steal: highest rank-sum among the player's match hand.
	 * Ties keep the first card.
	 * @param catalog the card catalog
	 * @return the stolen card ID, or 0
	 */
	public int stealBestPlayerCard(Deck catalog) {
		if (playerHandIds == null || playerHandIds.length == 0)
			return 0;
		int bestId = playerHandIds[0];
		int bestSum = -1;
		for (int i = 0; i < playerHandIds.length; i++) {
			Card card = catalog.getCardById(playerHandIds[i]);
			int sum = (card != null) ? card.rankSum() : 0;
			if (sum > bestSum) {
				bestSum = sum;
				bestId = playerHandIds[i];
			}
		}
		return bestId;
	}
}
