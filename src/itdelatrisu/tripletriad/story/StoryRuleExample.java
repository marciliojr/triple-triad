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

import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.CardResult;
import itdelatrisu.tripletriad.Element;
import itdelatrisu.tripletriad.Rule;

/**
 * A compact board or hand shown in Squall's rule lessons.
 * Layouts use real catalog ranks (T L R B) and must match {@link CardResult}.
 */
public final class StoryRuleExample {
	/** Mini 3x3 board, or a 5-card trade hand. */
	public enum Kind { BOARD, HAND }

	/** Geezard (right 4) captures Red Bat (left 2) from the left. */
	public static final StoryRuleExample OPEN = board(
			ids(0, 0, 0, 1, 4, 0, 0, 0, 0),
			owners(false, false, false, true, false, false, false, false, false),
			null, 3, new int[] { 4 });

	/** Thrustaevis in the center ties 5 with Grand Mantis and Fastitocalon-F. */
	public static final StoryRuleExample SAME = board(
			ids(0, 22, 0, 8, 17, 0, 0, 0, 0),
			owners(false, false, false, false, true, false, false, false, false),
			null, 4, new int[] { 1, 3 },
			Rule.SAME);

	/** Same as {@link #SAME}, plus Grat to the right of Grand Mantis for Combo. */
	public static final StoryRuleExample COMBO = board(
			ids(0, 22, 12, 8, 17, 0, 0, 0, 0),
			owners(false, false, false, false, true, false, false, false, false),
			null, 4, new int[] { 1, 3, 2 },
			Rule.SAME, Rule.COMBO);

	/** Vysage in the center: 6+2 and 5+3 both sum to 8. */
	public static final StoryRuleExample PLUS = board(
			ids(0, 8, 0, 3, 35, 0, 0, 0, 0),
			owners(false, false, false, false, true, false, false, false, false),
			null, 4, new int[] { 1, 3 },
			Rule.PLUS);

	/** PuPu in the top-right has A on the wall and a 1 that matches Funguar. */
	public static final StoryRuleExample SAME_WALL = board(
			ids(0, 2, 48, 0, 0, 0, 0, 0, 0),
			owners(false, false, true, false, false, false, false, false, false),
			null, 2, new int[] { 1 },
			Rule.SAME, Rule.SAME_WALL);

	/** Fire SAM08G on a Fire square: raw 6 vs 6, bonus makes 7 and captures Armadodo. */
	public static final StoryRuleExample ELEMENTAL = board(
			ids(0, 0, 0, 29, 24, 0, 0, 0, 0),
			owners(false, false, false, true, false, false, false, false, false),
			elements(3, Element.FIRE), 3, new int[] { 4 });

	/** Five opponent cards; the marked one is the card you would pick. */
	public static final StoryRuleExample TRADE = hand(
			new int[] { 1, 2, 3, 4, 12 }, 4);

	public final Kind kind;
	public final int[] ids;
	public final boolean[] playerOwned;
	public final Element[] elements;
	/** Board index of the card just played, or the highlighted hand index. */
	public final int placedPos;
	/** Positions flipped by the play (board), ignored for a hand. */
	public final int[] flipPos;
	public final Rule[] rules;

	private StoryRuleExample(Kind kind, int[] ids, boolean[] playerOwned,
			Element[] elements, int placedPos, int[] flipPos, Rule[] rules) {
		this.kind = kind;
		this.ids = ids;
		this.playerOwned = playerOwned;
		this.elements = elements;
		this.placedPos = placedPos;
		this.flipPos = (flipPos == null) ? new int[0] : flipPos;
		this.rules = (rules == null) ? new Rule[0] : rules;
	}

	private static StoryRuleExample board(int[] ids, boolean[] playerOwned,
			Element[] elements, int placedPos, int[] flipPos, Rule... rules) {
		return new StoryRuleExample(Kind.BOARD, ids, playerOwned, elements, placedPos, flipPos, rules);
	}

	private static StoryRuleExample hand(int[] ids, int picked) {
		boolean[] owners = new boolean[ids.length];
		return new StoryRuleExample(Kind.HAND, ids, owners, null, picked, new int[0], new Rule[0]);
	}

	private static int[] ids(int... v) { return v; }

	private static boolean[] owners(boolean... v) { return v; }

	private static Element[] elements(int pos, Element element) {
		Element[] board = new Element[9];
		for (int i = 0; i < 9; i++)
			board[i] = Element.NEUTRAL;
		board[pos] = element;
		return board;
	}

	/**
	 * Runs this board through {@link CardResult} with only {@link #rules} on.
	 * Restores the previous global rule flags afterwards.
	 * @return the engine result, or {@code null} for a hand example
	 */
	public CardResult evaluate() {
		if (kind != Kind.BOARD)
			return null;
		boolean[] saved = snapshotRules();
		try {
			for (Rule rule : Rule.values())
				rule.setState(false);
			for (Rule rule : rules)
				rule.setState(true);
			Card[] board = new Card[9];
			Card placed = null;
			for (int i = 0; i < 9; i++) {
				if (ids[i] <= 0)
					continue;
				Card card = catalogCard(ids[i]);
				card.setOwner(playerOwned[i]);
				card.setPosition(i);
				board[i] = card;
				if (i == placedPos)
					placed = card;
			}
			if (placed == null)
				throw new IllegalStateException("example is missing the placed card");
			return new CardResult(placed, placedPos, board, elements);
		} finally {
			restoreRules(saved);
		}
	}

	/** Same as {@link #evaluate()} but with no elemental squares. */
	public CardResult evaluateWithoutElements() {
		return new StoryRuleExample(kind, ids, playerOwned, null, placedPos, flipPos, rules).evaluate();
	}

	/** Same as {@link #evaluate()} with an extra rule set off (or on). */
	public CardResult evaluateWithout(Rule off) {
		boolean[] saved = snapshotRules();
		try {
			for (Rule rule : Rule.values())
				rule.setState(false);
			for (Rule rule : rules) {
				if (rule != off)
					rule.setState(true);
			}
			Card[] board = new Card[9];
			Card placed = null;
			for (int i = 0; i < 9; i++) {
				if (ids[i] <= 0)
					continue;
				Card card = catalogCard(ids[i]);
				card.setOwner(playerOwned[i]);
				card.setPosition(i);
				board[i] = card;
				if (i == placedPos)
					placed = card;
			}
			return new CardResult(placed, placedPos, board, elements);
		} finally {
			restoreRules(saved);
		}
	}

	public boolean isFlip(int pos) {
		for (int flip : flipPos) {
			if (flip == pos)
				return true;
		}
		return false;
	}

	public boolean isPlaced(int pos) {
		return kind == Kind.BOARD && pos == placedPos;
	}

	/**
	 * Catalog ranks used by the lesson boards, matching {@code cards/deck.txt}.
	 * Images are not loaded (the {@link Card} constructor is lazy).
	 */
	public static Card catalogCard(int id) {
		switch (id) {
			case 1: return new Card(1, "Geezard", "1541", Element.NEUTRAL, 1);
			case 2: return new Card(2, "Funguar", "5311", Element.NEUTRAL, 1);
			case 3: return new Card(3, "Bite Bug", "1533", Element.NEUTRAL, 1);
			case 4: return new Card(4, "Red Bat", "6211", Element.NEUTRAL, 1);
			case 8: return new Card(8, "Fastitocalon-F", "3152", Element.EARTH, 1);
			case 12: return new Card(12, "Grat", "7113", Element.NEUTRAL, 2);
			case 17: return new Card(17, "Thrustaevis", "5532", Element.WIND, 2);
			case 22: return new Card(22, "Grand Mantis", "5325", Element.NEUTRAL, 2);
			case 24: return new Card(24, "Armadodo", "6631", Element.EARTH, 3);
			case 29: return new Card(29, "SAM08G", "5462", Element.FIRE, 3);
			case 35: return new Card(35, "Vysage", "6554", Element.NEUTRAL, 4);
			case 48: return new Card(48, "PuPu", "31A2", Element.NEUTRAL, 5);
			default: throw new IllegalArgumentException("no lesson card for id " + id);
		}
	}

	private static boolean[] snapshotRules() {
		Rule[] all = Rule.values();
		boolean[] saved = new boolean[all.length];
		for (int i = 0; i < all.length; i++)
			saved[i] = all[i].isActive();
		return saved;
	}

	private static void restoreRules(boolean[] saved) {
		Rule[] all = Rule.values();
		for (int i = 0; i < all.length; i++)
			all[i].setState(saved[i]);
	}
}
