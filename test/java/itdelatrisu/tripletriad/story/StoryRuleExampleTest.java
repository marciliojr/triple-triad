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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.CardResult;
import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/** Validates Squall's lesson boards against {@link CardResult}. */
public class StoryRuleExampleTest {
	@Test
	public void openCapturesRedBatWithAHigherRightSide() {
		CardResult result = StoryRuleExample.OPEN.evaluate();
		assertTrue(result.hasCapture());
		assertFalse(result.isSame());
		assertFalse(result.isPlus());
		assertEquals(Set.of(4), positions(result.getCapturedList()));
		assertEquals(1, result.getCapturedCount());
	}

	@Test
	public void sameTiesFiveOnTwoSides() {
		CardResult result = StoryRuleExample.SAME.evaluate();
		assertTrue(result.isSame());
		assertFalse(result.isPlus());
		assertFalse(result.hasCombo());
		assertEquals(Set.of(1, 3), positions(result.getSameList()));
		assertEquals(2, result.getCapturedCount());
	}

	@Test
	public void comboChainsFromGrandMantisOntoGrat() {
		CardResult result = StoryRuleExample.COMBO.evaluate();
		assertTrue(result.isSame());
		assertTrue(result.hasCombo());
		assertEquals(Set.of(1, 3), positions(result.getSameList()));
		assertEquals(Set.of(2), positions(result.nextCombo()));
		assertFalse(result.hasCombo());
		assertEquals(3, result.getCapturedCount());
	}

	@Test
	public void plusMatchesSixPlusTwoAndFivePlusThree() {
		CardResult result = StoryRuleExample.PLUS.evaluate();
		assertTrue(result.isPlus());
		assertFalse(result.isSame());
		assertEquals(Set.of(1, 3), positions(result.getPlusList()));
		assertEquals(2, result.getCapturedCount());
	}

	@Test
	public void sameWallClosesWithAOnTheEdgeAndOneMatchingNeighbor() {
		CardResult result = StoryRuleExample.SAME_WALL.evaluate();
		assertTrue(result.isSame());
		assertEquals(Set.of(1), positions(result.getSameList()));
		assertEquals(1, result.getCapturedCount());

		CardResult withoutWall = StoryRuleExample.SAME_WALL.evaluateWithout(Rule.SAME_WALL);
		assertFalse(withoutWall.isSame());
		assertEquals(0, withoutWall.getCapturedCount());
	}

	@Test
	public void elementalBonusTurnsATieIntoACapture() {
		CardResult result = StoryRuleExample.ELEMENTAL.evaluate();
		assertTrue(result.hasCapture());
		assertFalse(result.isSame());
		assertEquals(Set.of(4), positions(result.getCapturedList()));
		assertEquals(1, result.getCapturedCount());

		CardResult raw = StoryRuleExample.ELEMENTAL.evaluateWithoutElements();
		assertFalse(raw.hasCapture());
		assertEquals(0, raw.getCapturedCount());
	}

	@Test
	public void tradeIsAHandNotABoard() {
		assertEquals(StoryRuleExample.Kind.HAND, StoryRuleExample.TRADE.kind);
		assertNull(StoryRuleExample.TRADE.evaluate());
		assertEquals(12, StoryRuleExample.TRADE.ids[StoryRuleExample.TRADE.placedPos]);
	}

	@Test
	public void catalogRanksMatchTheExampleCards() {
		assertEquals(4, StoryRuleExample.catalogCard(1).getRank(Card.Rank.RIGHT));
		assertEquals(2, StoryRuleExample.catalogCard(4).getRank(Card.Rank.LEFT));
		assertEquals(5, StoryRuleExample.catalogCard(17).getRank(Card.Rank.TOP));
		assertEquals(5, StoryRuleExample.catalogCard(17).getRank(Card.Rank.LEFT));
		assertEquals(5, StoryRuleExample.catalogCard(22).getRank(Card.Rank.BOTTOM));
		assertEquals(5, StoryRuleExample.catalogCard(8).getRank(Card.Rank.RIGHT));
		assertEquals(2, StoryRuleExample.catalogCard(8).getRank(Card.Rank.BOTTOM));
		assertEquals(3, StoryRuleExample.catalogCard(3).getRank(Card.Rank.RIGHT));
		assertEquals(6, StoryRuleExample.catalogCard(35).getRank(Card.Rank.TOP));
		assertEquals(5, StoryRuleExample.catalogCard(35).getRank(Card.Rank.LEFT));
		assertEquals(10, StoryRuleExample.catalogCard(48).getRank(Card.Rank.RIGHT));
		assertEquals(1, StoryRuleExample.catalogCard(48).getRank(Card.Rank.LEFT));
		assertEquals(1, StoryRuleExample.catalogCard(2).getRank(Card.Rank.RIGHT));
		assertEquals(6, StoryRuleExample.catalogCard(29).getRank(Card.Rank.RIGHT));
		assertEquals(6, StoryRuleExample.catalogCard(24).getRank(Card.Rank.LEFT));
		assertEquals(2, StoryRuleExample.catalogCard(22).getRank(Card.Rank.RIGHT));
		assertEquals(1, StoryRuleExample.catalogCard(12).getRank(Card.Rank.LEFT));
	}

	@Test
	public void lessonsAttachTheBoardsOnceAndKeepSeen() {
		StorySave save = StorySave.create("Ana");
		int examples = 0;
		boolean sawTradeClassic = false;
		boolean sawRandom = false;
		boolean sawSuddenDeath = false;
		for (Region region : StoryCampaign.REGIONS) {
			for (Duel duel : region.duels) {
				for (Line line : StoryScript.lessons(save, duel)) {
					if (line.example != null)
						examples++;
					if (line.pt.contains("jogo clássico"))
						sawTradeClassic = true;
					if (line.pt.startsWith("Random"))
						sawRandom = true;
					if (line.pt.startsWith("Sudden Death")) {
						sawSuddenDeath = true;
						assertFalse(line.hasExample());
					}
				}
			}
		}
		assertTrue(sawTradeClassic);
		assertTrue(sawRandom);
		assertTrue(sawSuddenDeath);
		assertEquals(7, examples); // Open, Same, Combo, Plus, Same Wall, Elemental, Trade
		for (Region region : StoryCampaign.REGIONS) {
			for (Duel duel : region.duels)
				assertEquals(0, StoryScript.lessons(save, duel).length);
		}
	}

	private static Set<Integer> positions(ArrayList<Card> cards) {
		Set<Integer> out = new HashSet<Integer>();
		if (cards == null)
			return out;
		for (Card card : cards)
			out.add(card.getPosition());
		return out;
	}
}
