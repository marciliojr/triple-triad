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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.gfx.ResourceLoader;

/**
 * Deck data type.
 */
public class Deck {
	/** List of cards. */
	private ArrayList<Card> deck;

	/**
	 * Creates a deck by parsing all cards.
	 */
	public Deck() {
		deck = new ArrayList<Card>();

		/*
		 * Data file format (all elements separated by tabs):
		 * - ID
		 * - Name
		 * - Ranks ({top}{left}{right}{bottom})
		 * - Element (all caps)
		 * - Level
		 */
		try (BufferedReader in = new BufferedReader(new InputStreamReader(
				ResourceLoader.getResourceAsStream(Options.DATA_FILE)))) {
			String line;
			while ((line = in.readLine()) != null) {
				// create an object only for valid input
				String[] tokens = line.split("\\t");
				if (tokens.length != 5) {
					Log.warn(String.format("Failed to parse line: %s", line));
					continue;
				}

				// ID
				int id = 0;
				try {
					id = Integer.parseInt(tokens[0]);
				} catch (NumberFormatException e) {
					Log.warn(String.format("Failed to parse ID in line: %s", line), e);
					continue;
				}
				if (id < 0) {
					Log.warn(String.format("Failed to parse ID in line: %s", line));
					continue;
				}

				// name (no checks)
				String name = tokens[1];

				// ranks
				String ranks = tokens[2];
				if (!ranks.matches("[1-9A]{4}")) {
					Log.warn(String.format("Failed to parse ranks for card %d: %s", id, tokens[2]));
					continue;
				}

				// element
				Element element;
				try {
					element = Element.valueOf(tokens[3]);
				} catch (IllegalArgumentException e) {
					Log.warn(String.format("Failed to parse element for card %d: %s", id, tokens[3]));
					continue;
				}

				// level
				int level = 0;
				try {
					level = Integer.parseInt(tokens[4]);
				} catch (NumberFormatException e) {
					Log.warn(String.format("Failed to parse level in line: %s", line), e);
					continue;
				}
				if (level < 0) {
					Log.warn(String.format("Failed to parse level in line: %s", line));
					continue;
				}

				deck.add(new Card(id, name, ranks, element, level));
			}
		} catch (IOException e) {
			Log.error("Failed to read card data.", e);
		}
	}

	/**
	 * Builds two hands of cards, without repeats.
	 * @param playerCards the player hand
	 * @param opponentCards the opponent hand
	 */
	public void buildHands(Card[] playerCards, Card[] opponentCards) {
		// deck size too small
		if (deck.size() < playerCards.length + opponentCards.length) {
			Log.error("Not enough cards loaded (10 minimum).");
			return;
		}

		Collections.shuffle(deck);
		for (int i = 0; i < playerCards.length; i++) {
			playerCards[i] = new Card(deck.get(i));
			playerCards[i].setOwner(TripleTriad.PLAYER);
		}
		for (int i = 0; i < opponentCards.length; i++) {
			opponentCards[i] = new Card(deck.get(i + playerCards.length));
			opponentCards[i].setOwner(TripleTriad.OPPONENT);
		}
	}

	/**
	 * Returns the full catalog of cards.
	 * @return the cards
	 */
	public ArrayList<Card> getCards() { return deck; }

	/**
	 * Returns a catalog card by ID.
	 * @param id the card ID
	 * @return the card, or null
	 */
	public Card getCardById(int id) {
		for (int i = 0; i < deck.size(); i++) {
			if (deck.get(i).getID() == id)
				return deck.get(i);
		}
		return null;
	}

	/**
	 * Creates a playable copy of a catalog card.
	 * @param id the card ID
	 * @param owner PLAYER or OPPONENT
	 * @return the copy, or null if the ID is unknown
	 */
	public Card createCard(int id, boolean owner) {
		Card prototype = getCardById(id);
		if (prototype == null)
			return null;
		Card copy = new Card(prototype);
		copy.setOwner(owner);
		return copy;
	}

	/**
	 * Fills a hand from explicit card IDs.
	 * @param ids the card IDs
	 * @param cards the hand to fill
	 * @param owner PLAYER or OPPONENT
	 */
	public void buildHand(int[] ids, Card[] cards, boolean owner) {
		for (int i = 0; i < cards.length; i++) {
			int id = (ids != null && i < ids.length) ? ids[i] : 0;
			Card copy = createCard(id, owner);
			if (copy == null && !deck.isEmpty()) {
				copy = new Card(deck.get(i % deck.size()));
				copy.setOwner(owner);
			}
			cards[i] = copy;
		}
	}

	/**
	 * Fills a hand with random catalog cards (no repeats in the hand).
	 * @param cards the hand to fill
	 * @param owner PLAYER or OPPONENT
	 */
	public void buildRandomHand(Card[] cards, boolean owner) {
		if (deck.size() < cards.length) {
			Log.error("Not enough cards loaded.");
			return;
		}
		ArrayList<Card> shuffled = new ArrayList<Card>(deck);
		Collections.shuffle(shuffled, new Random());
		for (int i = 0; i < cards.length; i++) {
			cards[i] = new Card(shuffled.get(i));
			cards[i].setOwner(owner);
		}
	}

	/**
	 * Builds a 5-card starter pack with unique IDs and at least one rank A.
	 * @return five card IDs, or an empty array if the catalog is too small
	 */
	public int[] createStarterPack() {
		if (deck.size() < SavedDeck.SIZE)
			return new int[0];
		ArrayList<Card> shuffled = new ArrayList<Card>(deck);
		Collections.shuffle(shuffled, new Random());
		int[] ids = new int[SavedDeck.SIZE];
		boolean hasA = false;
		for (int i = 0; i < SavedDeck.SIZE; i++) {
			ids[i] = shuffled.get(i).getID();
			if (shuffled.get(i).hasRankA())
				hasA = true;
		}
		if (!hasA) {
			Card withA = null;
			for (int i = 0; i < deck.size(); i++) {
				if (deck.get(i).hasRankA()) {
					withA = deck.get(i);
					break;
				}
			}
			if (withA != null)
				ids[new Random().nextInt(SavedDeck.SIZE)] = withA.getID();
		}
		return ids;
	}

	/**
	 * Picks five card IDs from a collection bag without repeating the same index.
	 * Duplicate catalog IDs in the bag may still appear together.
	 * @param bag collection card IDs
	 * @return five IDs, or an empty array if the bag is too small
	 */
	public int[] pickRandomFromBag(ArrayList<Integer> bag) {
		if (bag == null || bag.size() < SavedDeck.SIZE)
			return new int[0];
		ArrayList<Integer> shuffled = new ArrayList<Integer>(bag);
		Collections.shuffle(shuffled, new Random());
		int[] ids = new int[SavedDeck.SIZE];
		for (int i = 0; i < SavedDeck.SIZE; i++)
			ids[i] = shuffled.get(i).intValue();
		return ids;
	}

	/**
	 * Returns five unique random catalog IDs for an opponent hand.
	 * @return the IDs
	 */
	public int[] createRandomHandIds() {
		Card[] cards = new Card[SavedDeck.SIZE];
		buildRandomHand(cards, TripleTriad.OPPONENT);
		int[] ids = new int[SavedDeck.SIZE];
		for (int i = 0; i < ids.length; i++)
			ids[i] = (cards[i] != null) ? cards[i].getID() : 0;
		return ids;
	}
}