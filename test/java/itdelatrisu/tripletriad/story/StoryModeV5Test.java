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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.ProfileStore;
import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;
import itdelatrisu.tripletriad.story.StorySave.Difficulty;

/** Save format 5: regional rules, rematch, progress and difficulty. */
public class StoryModeV5Test {
	@Test
	public void format4SaveLoadsAsNormalWithNativeRules() throws Exception {
		Profile profile = new Profile();
		profile.setName("Marcilio");
		profile.addCampaign("Ana", StorySave.FEMALE, Difficulty.HARD);
		File file = Files.createTempFile("story-v4", ".profile").toFile();
		file.deleteOnExit();
		ProfileStore.writeFile(file, profile);
		StringBuilder old = new StringBuilder();
		for (String line : Files.readAllLines(file.toPath())) {
			if (line.startsWith("CAMP.1.DIFFICULTY") || line.startsWith("CAMP.1.CITYRULES")
					|| line.startsWith("CAMP.1.CARRY") || line.startsWith("CAMP.1.PLAYS")
					|| line.startsWith("CAMP.1.QUEEN") || line.startsWith("CAMP.1.WINS")
					|| line.startsWith("CAMP.1.LOSSES") || line.startsWith("CAMP.1.CARDWON"))
				continue;
			old.append(line.startsWith("STORY_FORMAT") ? "STORY_FORMAT = 4" : line).append('\n');
		}
		Files.write(file.toPath(), old.toString().getBytes("UTF-8"));

		Profile loaded = ProfileStore.readFile(file);
		StorySave save = loaded.activeStory();
		assertEquals(Difficulty.NORMAL, save.difficulty());
		assertFalse(save.isHard());
		assertArrayEquals(StoryRules.defaults("garden"), StoryRules.effective(save, "garden"));
		assertEquals(0, save.wins());
		assertNull(save.carry());
	}

	@Test
	public void newSaveWritesFormat5AndRoundTripsDifficultyAndRules() throws Exception {
		Profile profile = new Profile();
		profile.setName("Marcilio");
		StorySave save = profile.addCampaign("Caio", StorySave.MALE, Difficulty.EASY);
		save.recordWin();
		save.addCardsWon(2);
		StoryRules.addRule(save, "town", Rule.OPEN);
		save.setCarry(Rule.OPEN);
		save.addPlay("town");
		File file = Files.createTempFile("story-v5", ".profile").toFile();
		file.deleteOnExit();
		ProfileStore.writeFile(file, profile);
		String text = new String(Files.readAllBytes(file.toPath()), "UTF-8");
		assertTrue(text.contains("STORY_FORMAT = 5"));
		assertTrue(text.contains("CAMP.1.DIFFICULTY = EASY"));

		StorySave again = ProfileStore.readFile(file).activeStory();
		assertEquals(Difficulty.EASY, again.difficulty());
		assertTrue(StoryRules.has(again, "town", Rule.OPEN));
		assertEquals(Rule.OPEN, again.carry());
		assertEquals(1, again.plays("town"));
		assertEquals(1, again.wins());
		assertEquals(2, again.cardsWon());
	}

	@Test
	public void carriedRuleSpreadsIntoTheCurrentCity() {
		StorySave save = StorySave.create("Ana");
		save.setCarry(Rule.OPEN);
		assertFalse(StoryRules.has(save, "town", Rule.OPEN));
		Random always = new Random() {
			@Override
			public int nextInt(int bound) { return 0; }
		};
		StoryRules.Event event = StoryRules.afterMatch(save, "town", always);
		assertNotNull(event);
		assertEquals(Rule.OPEN, event.rule);
		assertEquals("town", event.regionId);
		assertTrue(StoryRules.has(save, "town", Rule.OPEN));
		assertEquals(1, save.plays("town"));
	}

	@Test
	public void suddenDeathAndRandomNeverSpread() {
		StorySave save = StorySave.create("Ana");
		save.setCarry(Rule.SUDDEN_DEATH);
		Random always = new Random() {
			@Override
			public int nextInt(int bound) { return 0; }
		};
		assertNull(StoryRules.afterMatch(save, "town", always));
		assertFalse(StoryRules.has(save, "town", Rule.SUDDEN_DEATH));
		assertFalse(StoryRules.canSpread(Rule.SUDDEN_DEATH));
	}

	@Test
	public void missedRollDoesNotAddTheRuleButStillCountsThePlay() {
		StorySave save = StorySave.create("Ana");
		save.setCarry(Rule.OPEN);
		Random never = new Random() {
			@Override
			public int nextInt(int bound) { return 99; }
		};
		assertNull(StoryRules.afterMatch(save, "town", never));
		assertFalse(StoryRules.has(save, "town", Rule.OPEN));
		assertEquals(1, save.plays("town"));
		assertTrue(StoryRules.chance(1) < 99);
	}

	@Test
	public void squallLessonFiresForANewlySpreadRule() {
		StorySave save = StorySave.create("Ana");
		Region town = StoryCampaign.regionOf("raijin");
		StoryRules.addRule(save, town.id, Rule.OPEN);
		boolean sawOpen = false;
		for (Line line : StoryScript.lessons(save, StoryCampaign.findDuel("raijin"))) {
			if (line.pt.startsWith("Open:"))
				sawOpen = true;
		}
		assertTrue(sawOpen);
		assertTrue(save.hasSeenRule("OPEN"));
		assertEquals(0, StoryScript.lessons(save, StoryCampaign.findDuel("raijin")).length);
	}

	@Test
	public void queenCanAddAndRemoveACityRuleOnce() {
		StorySave save = StorySave.create("Ana");
		assertFalse(save.queenUsed("deling"));
		StoryRules.QueenOffer offer = StoryRules.offer(save, "deling");
		assertNotNull(offer);
		assertEquals(Rule.SAME_WALL, offer.add);
		assertTrue(StoryRules.applyQueen(save, "deling", offer.add, null));
		assertTrue(StoryRules.has(save, "deling", Rule.SAME_WALL));
		assertTrue(save.queenUsed("deling"));
		assertFalse(StoryRules.applyQueen(save, "deling", Rule.OPEN, null));
	}

	@Test
	public void rematchRareIsGrantedOnlyOnceAndIsNotRequiredToClearTheStage() {
		StorySave save = StorySave.create("Ana");
		Duel boss = StoryCampaign.findDuel("king");
		assertTrue(StoryRematch.hasRematch(boss));
		assertEquals(84, StoryRematch.reward("king"));
		save.setClearedCsv("cadete,spade,joker,club,heart,king");
		assertTrue(StoryCampaign.stageCleared(StoryCampaign.REGIONS[0], save));
		assertTrue(StoryRematch.unlocked(save, boss));
		Duel rematch = StoryRematch.asDuel(boss);
		assertEquals("king_rematch", rematch.id);
		assertTrue(rematch.minLevel > boss.minLevel || rematch.maxLevel > boss.maxLevel);
		assertEquals(84, StoryRematch.pendingReward(save, rematch));
		save.armWin(rematch.id, new int[] { 20, 21, 22, 23, 24 }, 84);
		save.advanceToPick();
		assertEquals(84, save.takeBossReward());
		assertEquals(0, StoryRematch.pendingReward(save, rematch));
		int[] granted = save.confirm(20);
		assertArrayEquals(new int[] { 20 }, granted);
		assertTrue(save.isCleared("king_rematch"));
		assertEquals(0, StoryRematch.pendingReward(save, rematch));
		assertTrue(StoryCampaign.stageCleared(StoryCampaign.REGIONS[0], save));
	}

	@Test
	public void progressPercentCountsUniquePoolCardsAndListsMissingRares() {
		StorySave save = StorySave.create("Ana");
		int start = StoryProgress.percent(save);
		assertTrue(start > 0 && start < 100);
		int have = StoryProgress.uniqueOwned(save);
		assertEquals(StoryBalance.STARTER.length, have);
		int missingBefore = StoryProgress.missingRares(save).length;
		assertTrue(missingBefore >= 16);
		save.addCard(85);
		save.addCard(85);
		assertEquals(have + 1, StoryProgress.uniqueOwned(save));
		assertEquals(missingBefore - 1, StoryProgress.missingRares(save).length);
		assertTrue(StoryProgress.percent(save) >= start);
		boolean sawIfrit = false;
		for (StoryProgress.Rare rare : StoryProgress.missingRares(save)) {
			if (rare.cardId == 85)
				sawIfrit = true;
		}
		assertFalse(sawIfrit);
		StoryProgress.Rare shiva = null;
		for (StoryProgress.Rare rare : StoryProgress.missingRares(save)) {
			if (rare.cardId == 84 && rare.rematch)
				shiva = rare;
		}
		assertNotNull(shiva);
		assertEquals("garden", shiva.regionId);
		assertFalse(StoryProgress.opponentSeen(save, shiva));
	}

	@Test
	public void difficultyChangesAiAndLevelsAndHardLossTakesACard() {
		Duel raijin = StoryCampaign.findDuel("raijin");
		StorySave easy = StorySave.create("Ana", StorySave.FEMALE, Difficulty.EASY);
		StorySave hard = StorySave.create("Caio", StorySave.MALE, Difficulty.HARD);
		StorySave normal = StorySave.create("Nilo");
		assertEquals(Options.AIType.BALANCED, raijin.ai);
		assertEquals(Options.AIType.DEFENSIVE, StoryBalance.aiFor(raijin, easy));
		assertEquals(Options.AIType.BALANCED, StoryBalance.aiFor(raijin, normal));
		assertEquals(Options.AIType.OFFENSIVE, StoryBalance.aiFor(raijin, hard));
		assertEquals(raijin.maxLevel - 1, StoryBalance.levelMax(raijin, easy));
		assertEquals(Math.min(10, raijin.maxLevel + 1), StoryBalance.levelMax(raijin, hard));
		assertEquals(raijin.maxLevel, StoryBalance.levelMax(raijin, normal));

		assertFalse(normal.canForfeitCard(new int[] { 1, 4, 9, 11, 12 }));
		hard.addCard(30);
		assertTrue(hard.collectionSize() > 5);
		assertTrue(hard.canForfeitCard(new int[] { 1, 4, 9, 11, 12 }));
		int before = hard.collectionSize();
		hard.armGive(new int[] { 1, 4, 9, 11, 12 });
		assertEquals(1, hard.confirmGive(1));
		assertEquals(before - 1, hard.collectionSize());
		assertFalse(hard.hasPending());
		assertEquals(0, easy.losses());
	}

	@Test
	public void rematchDeckDiffersFromTheFirstBossFight() {
		StorySave save = StorySave.create("Ana");
		Duel boss = StoryCampaign.findDuel("king");
		Duel rematch = StoryRematch.asDuel(boss);
		int[] first = StoryBalance.opponentHand(save, boss);
		int[] second = StoryBalance.opponentHand(save, rematch);
		assertEquals(5, first.length);
		assertEquals(5, second.length);
		boolean same = true;
		for (int i = 0; i < 5; i++) {
			if (first[i] != second[i])
				same = false;
		}
		assertFalse(same);
	}
}
