/*
 * Triple Triad - a card game from FFVIII
 * Copyright (C) 2014 Jeffrey Han
 *
 * Triple Triad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package itdelatrisu.tripletriad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/** Screen-to-bed mapping for the menu and battle tracks. */
public class AudioControllerTest {
	@Test
	public void introHasNoBed() {
		assertNull(AudioController.bedFor(null));
		assertNull(AudioController.bedFor(GameScreen.INTRO));
	}

	@Test
	public void matchesUseTheBattleBed() {
		assertEquals(AudioController.Bed.BATTLE, AudioController.bedFor(GameScreen.MATCH));
	}

	@Test
	public void menusAndHubsUseTheMenuBed() {
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.MENU));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.PROFILE_SELECT));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.PROFILE));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.SETTINGS));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.HOW_TO_PLAY));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.ABOUT));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.MY_DECK));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.STORY));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.CHAMPIONSHIP));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.VERSUS));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.MEMORY_CARD));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.TROPHIES));
		assertEquals(AudioController.Bed.MENU, AudioController.bedFor(GameScreen.CONTROLS));
	}
}
