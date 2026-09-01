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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Session-wide card-grid sort. Does not mutate the catalog, album or run bag.
 */
public enum CardSort {
	/** Catalog ID, same as {@code deck.txt}. */
	ORDER,
	/** Card level 1–10, then ID. */
	LEVEL,
	/** Rank sum, strongest first, then ID. */
	VALUE;

	/** Sort used by all card grids this session. */
	private static CardSort current = ORDER;

	/**
	 * Returns the active sort.
	 * @return the mode
	 */
	public static CardSort current() { return current; }

	/**
	 * Advances to the next mode and returns it.
	 * @return the new mode
	 */
	public static CardSort cycle() {
		current = current.next();
		return current;
	}

	/**
	 * Returns the next mode in the cycle (order → level → value).
	 * @return the next mode
	 */
	public CardSort next() {
		switch (this) {
			case ORDER: return LEVEL;
			case LEVEL: return VALUE;
			default: return ORDER;
		}
	}

	/**
	 * Sorted copy of a catalog list. The source list is left unchanged.
	 * @param cards the catalog
	 * @return a new ordered list
	 */
	public static ArrayList<Card> sortedCatalog(List<Card> cards) {
		ArrayList<Card> copy = new ArrayList<Card>();
		if (cards != null)
			copy.addAll(cards);
		Collections.sort(copy, new Comparator<Card>() {
			@Override
			public int compare(Card a, Card b) {
				return compareCards(a, b);
			}
		});
		return copy;
	}

	/**
	 * Permutation of bag indices for the current sort. Duplicates stay distinct.
	 * @param bag card IDs (album or championship bag)
	 * @param catalog the card catalog
	 * @return bag indices in display order
	 */
	public static ArrayList<Integer> sortedBagIndices(List<Integer> bag, Deck catalog) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		if (bag == null)
			return indices;
		for (int i = 0; i < bag.size(); i++)
			indices.add(Integer.valueOf(i));
		Collections.sort(indices, new Comparator<Integer>() {
			@Override
			public int compare(Integer ia, Integer ib) {
				int aIndex = ia.intValue();
				int bIndex = ib.intValue();
				Card a = cardAt(bag, catalog, aIndex);
				Card b = cardAt(bag, catalog, bIndex);
				int cmp = compareCards(a, b);
				if (cmp != 0)
					return cmp;
				return Integer.compare(aIndex, bIndex);
			}
		});
		return indices;
	}

	/**
	 * Finds a catalog card in a sorted view.
	 * @param view the sorted catalog
	 * @param id the card ID
	 * @return the view index, or -1
	 */
	public static int indexOfCardId(List<Card> view, int id) {
		if (view == null)
			return -1;
		for (int i = 0; i < view.size(); i++) {
			Card card = view.get(i);
			if (card != null && card.getID() == id)
				return i;
		}
		return -1;
	}

	/**
	 * Finds a bag index in a sorted view.
	 * @param view sorted bag indices
	 * @param bagIndex the original bag index
	 * @return the view index, or -1
	 */
	public static int indexOfBag(List<Integer> view, int bagIndex) {
		if (view == null)
			return -1;
		for (int i = 0; i < view.size(); i++) {
			if (view.get(i).intValue() == bagIndex)
				return i;
		}
		return -1;
	}

	/**
	 * Returns the bag index at a view slot.
	 * @param view sorted bag indices
	 * @param viewIndex the display index
	 * @return the bag index, or -1
	 */
	public static int bagAt(List<Integer> view, int viewIndex) {
		if (view == null || viewIndex < 0 || viewIndex >= view.size())
			return -1;
		return view.get(viewIndex).intValue();
	}

	private static int compareCards(Card a, Card b) {
		if (a == null && b == null)
			return 0;
		if (a == null)
			return 1;
		if (b == null)
			return -1;
		int primary;
		switch (current) {
			case LEVEL:
				primary = Integer.compare(a.getLevel(), b.getLevel());
				break;
			case VALUE:
				primary = Integer.compare(b.rankSum(), a.rankSum());
				break;
			case ORDER:
			default:
				primary = Integer.compare(a.getID(), b.getID());
				break;
		}
		if (primary != 0)
			return primary;
		return Integer.compare(a.getID(), b.getID());
	}

	private static Card cardAt(List<Integer> bag, Deck catalog, int bagIndex) {
		if (bag == null || bagIndex < 0 || bagIndex >= bag.size())
			return null;
		if (catalog == null)
			return null;
		return catalog.getCardById(bag.get(bagIndex).intValue());
	}
}
