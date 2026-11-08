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
import java.util.List;

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

	/** Number of save blocks on the championship "memory card". */
	public static final int SLOTS = 15;

	/** Championship save blocks (null = free). */
	private final ChampionshipSlot[] slots = new ChampionshipSlot[SLOTS];

	/** Legacy (pre-1.3.0) single save: round (0 = none). Migrated to block 01. */
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
	 * Replaces the album with the given card IDs (IDs of 0 or less are skipped).
	 * @param cards the IDs
	 */
	public void replaceCollection(List<Integer> cards) {
		collection.clear();
		if (cards == null)
			return;
		for (int i = 0; i < cards.size(); i++) {
			int id = cards.get(i).intValue();
			if (id > 0)
				collection.add(Integer.valueOf(id));
		}
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
	 * Returns whether any championship save block is used.
	 * @return true if at least one block holds a run
	 */
	public boolean hasChampionshipSave() {
		for (ChampionshipSlot slot : slots) {
			if (slot != null)
				return true;
		}
		return false;
	}

	/**
	 * Returns a save block.
	 * @param index the block index [0, SLOTS)
	 * @return the block, or null if free / out of range
	 */
	public ChampionshipSlot getSlot(int index) {
		return (index >= 0 && index < SLOTS) ? slots[index] : null;
	}

	/**
	 * Writes (or clears with null) a save block.
	 * @param index the block index
	 * @param slot the block, or null
	 */
	public void setSlot(int index, ChampionshipSlot slot) {
		if (index < 0 || index >= SLOTS)
			return;
		slots[index] = (slot != null && slot.isValid()) ? slot : null;
	}

	/**
	 * Returns the first free block.
	 * @return the index, or -1 if the card is full
	 */
	public int firstFreeSlot() {
		for (int i = 0; i < SLOTS; i++) {
			if (slots[i] == null)
				return i;
		}
		return -1;
	}

	/**
	 * Returns the most recently written block.
	 * @return the index, or -1 if none
	 */
	public int latestSlot() {
		int best = -1;
		for (int i = 0; i < SLOTS; i++) {
			if (slots[i] != null && (best < 0 || slots[i].getSavedAt() > slots[best].getSavedAt()))
				best = i;
		}
		return best;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunRound(int round) {
		this.runRound = (round < 0) ? 0 : round;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunWins(int wins) {
		this.runWins = (wins < 0) ? 0 : wins;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunHand(int[] ids) {
		this.runHand = (ids != null) ? ids.clone() : null;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunOpponent(int[] ids) {
		this.runOpponent = (ids != null) ? ids.clone() : null;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
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
	 * Moves a legacy single championship save (RUN_* keys) into block 01
	 * (or the first free block) and clears the legacy fields.
	 * @return true if something was migrated
	 */
	public boolean migrateLegacyRun() {
		if (runRound < 2)
			return false;
		ChampionshipSlot slot = new ChampionshipSlot();
		slot.setRound(runRound);
		slot.setWins(runWins);
		slot.setHand(runHand);
		slot.setOpponent(runOpponent);
		slot.setBag(runBag.isEmpty() ? collection : runBag);
		slot.setSavedAt(System.currentTimeMillis());
		int index = (slots[0] == null) ? 0 : firstFreeSlot();
		runRound = 0;
		runWins = 0;
		runHand = null;
		runOpponent = null;
		runBag.clear();
		if (index < 0)
			return false;
		slots[index] = slot;
		return true;
	}
}
