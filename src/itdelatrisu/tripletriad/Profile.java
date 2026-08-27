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
 * Local player profile: display name and saved decks.
 */
public class Profile {
	/** Player display name. */
	private String name;

	/** Saved decks. */
	private final ArrayList<SavedDeck> decks;

	/** Championship collection (card IDs, duplicates allowed). */
	private final ArrayList<Integer> collection;

	/** Completed championship runs. */
	private int championshipWins;

	/** Saved run round (0 = none; save starts at 2). */
	private int runRound;

	/** Saved run wins. */
	private int runWins;

	/** Saved 5-card hand for the current run match. */
	private int[] runHand;

	/** Saved opponent hand for the current run match. */
	private int[] runOpponent;

	/** Saved card bag for the current championship run. */
	private final ArrayList<Integer> runBag;

	/**
	 * Creates an empty profile.
	 */
	public Profile() {
		this.name = "";
		this.decks = new ArrayList<SavedDeck>();
		this.collection = new ArrayList<Integer>();
		this.runBag = new ArrayList<Integer>();
		this.championshipWins = 0;
	}

	/**
	 * Returns the player name.
	 * @return the name
	 */
	public String getName() { return name; }

	/**
	 * Sets the player name.
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = (name != null) ? name.trim() : "";
	}

	/**
	 * Returns whether this profile can unlock the main menu.
	 * @return true if a name is set
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty();
	}

	/**
	 * Returns the saved decks.
	 * @return the list of decks
	 */
	public ArrayList<SavedDeck> getDecks() { return decks; }

	/**
	 * Inserts or replaces a deck by name (case-insensitive).
	 * @param deck the deck to store
	 */
	public void upsertDeck(SavedDeck deck) {
		if (deck == null || deck.getName().isEmpty())
			return;
		for (int i = 0; i < decks.size(); i++) {
			if (decks.get(i).getName().equalsIgnoreCase(deck.getName())) {
				decks.set(i, deck);
				return;
			}
		}
		decks.add(deck);
	}

	/**
	 * Removes a saved deck.
	 * @param deck the deck to remove
	 */
	public void removeDeck(SavedDeck deck) {
		decks.remove(deck);
	}

	/**
	 * Returns the championship collection (card IDs).
	 * @return the list
	 */
	public ArrayList<Integer> getCollection() { return collection; }

	/**
	 * Adds a card to the championship collection.
	 * @param cardId the catalog ID
	 */
	public void addCard(int cardId) {
		if (cardId > 0)
			collection.add(Integer.valueOf(cardId));
	}

	/**
	 * Empties the championship collection.
	 */
	public void clearCollection() {
		collection.clear();
	}

	/**
	 * Removes the first instance of a card ID from the collection.
	 * @param cardId the catalog ID
	 * @return true if a card was removed
	 */
	public boolean removeCard(int cardId) {
		for (int i = 0; i < collection.size(); i++) {
			if (collection.get(i).intValue() == cardId) {
				collection.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns how many championships this profile has won.
	 * @return the count
	 */
	public int getChampionshipWins() { return championshipWins; }

	/**
	 * Sets championship wins (used when loading).
	 * @param wins the count
	 */
	public void setChampionshipWins(int wins) {
		this.championshipWins = (wins < 0) ? 0 : wins;
	}

	/**
	 * Records one completed championship.
	 */
	public void addChampionshipWin() {
		championshipWins++;
	}

	/**
	 * Returns whether a championship run can be continued (from match 2).
	 * @return true if a save exists
	 */
	public boolean hasChampionshipSave() {
		return runRound >= 2;
	}

	/**
	 * Returns the saved run round.
	 * @return the round, or 0
	 */
	public int getRunRound() { return runRound; }

	/**
	 * Sets the saved run round.
	 * @param round the round
	 */
	public void setRunRound(int round) {
		this.runRound = (round < 0) ? 0 : round;
	}

	/**
	 * Returns the saved run wins.
	 * @return the wins
	 */
	public int getRunWins() { return runWins; }

	/**
	 * Sets the saved run wins.
	 * @param wins the wins
	 */
	public void setRunWins(int wins) {
		this.runWins = (wins < 0) ? 0 : wins;
	}

	/**
	 * Returns the saved run hand.
	 * @return the IDs, or null
	 */
	public int[] getRunHand() {
		return (runHand != null) ? runHand.clone() : null;
	}

	/**
	 * Sets the saved run hand.
	 * @param ids the IDs
	 */
	public void setRunHand(int[] ids) {
		this.runHand = (ids != null) ? ids.clone() : null;
	}

	/**
	 * Returns the saved opponent hand.
	 * @return the IDs, or null
	 */
	public int[] getRunOpponent() {
		return (runOpponent != null) ? runOpponent.clone() : null;
	}

	/**
	 * Sets the saved opponent hand.
	 * @param ids the IDs
	 */
	public void setRunOpponent(int[] ids) {
		this.runOpponent = (ids != null) ? ids.clone() : null;
	}

	/**
	 * Returns the saved championship run bag.
	 * @return the list
	 */
	public ArrayList<Integer> getRunBag() { return runBag; }

	/**
	 * Replaces the saved championship run bag.
	 * @param cards the IDs
	 */
	public void setRunBag(ArrayList<Integer> cards) {
		runBag.clear();
		if (cards == null)
			return;
		for (int i = 0; i < cards.size(); i++) {
			int id = cards.get(i).intValue();
			if (id > 0)
				runBag.add(Integer.valueOf(id));
		}
	}

	/**
	 * Stores a championship run if it is at match 2 or later.
	 * @param run the run
	 */
	public void storeChampionshipRun(ChampionshipRun run) {
		if (run == null || run.getRound() < 2) {
			clearChampionshipSave();
			return;
		}
		this.runRound = run.getRound();
		this.runWins = run.getWins();
		this.runHand = run.getPlayerHandIds();
		this.runOpponent = run.getOpponentHandIds();
		setRunBag(run.getBag());
	}

	/**
	 * Clears an in-progress championship save.
	 */
	public void clearChampionshipSave() {
		this.runRound = 0;
		this.runWins = 0;
		this.runHand = null;
		this.runOpponent = null;
		runBag.clear();
	}
}
