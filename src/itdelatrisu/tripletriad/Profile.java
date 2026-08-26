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

	/**
	 * Creates an empty profile.
	 */
	public Profile() {
		this.name = "";
		this.decks = new ArrayList<SavedDeck>();
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
}
