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

/**
 * A named 5-card deck saved with the player profile.
 */
public class SavedDeck {
	/** Required number of cards in a playable deck. */
	public static final int SIZE = 5;

	/** Display name. */
	private String name;

	/** Card IDs (unique, length 5 when complete). */
	private int[] cardIds;

	/**
	 * Constructor.
	 * @param name the deck name
	 * @param cardIds the card IDs
	 */
	public SavedDeck(String name, int[] cardIds) {
		this.name = (name != null) ? name.trim() : "";
		this.cardIds = (cardIds != null) ? cardIds.clone() : new int[0];
	}

	/**
	 * Returns the deck name.
	 * @return the name
	 */
	public String getName() { return name; }

	/**
	 * Sets the deck name.
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = (name != null) ? name.trim() : "";
	}

	/**
	 * Returns a copy of the card IDs.
	 * @return the IDs
	 */
	public int[] getCardIds() { return cardIds.clone(); }

	/**
	 * Sets the card IDs.
	 * @param cardIds the IDs
	 */
	public void setCardIds(int[] cardIds) {
		this.cardIds = (cardIds != null) ? cardIds.clone() : new int[0];
	}

	/**
	 * Returns whether this deck can be used in a match.
	 * @return true if it has five unique IDs
	 */
	public boolean isComplete() {
		if (cardIds == null || cardIds.length != SIZE)
			return false;
		for (int i = 0; i < cardIds.length; i++) {
			if (cardIds[i] <= 0)
				return false;
			for (int j = i + 1; j < cardIds.length; j++) {
				if (cardIds[i] == cardIds[j])
					return false;
			}
		}
		return true;
	}
}
