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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** The lesson board must grow and still fit above the dialogue box. */
public class StoryRuleExampleLayoutTest {
	@Test
	public void boardAtDefaultResolutionIsAboutTwiceAsBigAndFits() {
		int w = 1280, h = 720;
		StoryRuleExampleLayout layout = StoryRuleExampleLayout.of(w, h, false, 0);
		float old = StoryRuleExampleLayout.oldBoardSize(w, h);
		assertTrue(layout.size >= old * 1.5f, "size " + layout.size + " old " + old);
		assertTrue(layout.size <= old * 2.0f + 0.5f, "size " + layout.size + " old " + old);
		assertFits(layout, w, h);
	}

	@Test
	public void handAtDefaultResolutionIsAboutTwiceAsBigAndFits() {
		int w = 1280, h = 720;
		StoryRuleExampleLayout layout = StoryRuleExampleLayout.of(w, h, true, 5);
		float old = StoryRuleExampleLayout.oldHandSize(w, h);
		assertTrue(layout.size >= old * 1.5f, "size " + layout.size + " old " + old);
		assertTrue(layout.size <= old * 2.0f + 0.5f, "size " + layout.size + " old " + old);
		assertFits(layout, w, h);
	}

	@Test
	public void boardAtClassicResolutionStaysOnScreen() {
		int w = 640, h = 360;
		StoryRuleExampleLayout layout = StoryRuleExampleLayout.of(w, h, false, 0);
		float old = StoryRuleExampleLayout.oldBoardSize(w, h);
		assertTrue(layout.size >= old * 1.5f, "size " + layout.size + " old " + old);
		assertFits(layout, w, h);
	}

	@Test
	public void handAtClassicResolutionStaysOnScreen() {
		int w = 640, h = 360;
		StoryRuleExampleLayout layout = StoryRuleExampleLayout.of(w, h, true, 5);
		float old = StoryRuleExampleLayout.oldHandSize(w, h);
		assertTrue(layout.size >= old * 1.5f, "size " + layout.size + " old " + old);
		assertFits(layout, w, h);
	}

	private static void assertFits(StoryRuleExampleLayout layout, int w, int h) {
		assertTrue(layout.oy >= 0f, "oy " + layout.oy);
		assertTrue(layout.ox >= 0f, "ox " + layout.ox);
		assertTrue(layout.ox + layout.gridW <= w, "grid right " + (layout.ox + layout.gridW));
		assertTrue(layout.fitsAboveBox(), "grid bottom " + (layout.oy + layout.gridH) + " box " + layout.boxY);
		assertTrue(layout.boxY + layout.boxH <= h * StoryRuleExampleLayout.HINT_Y,
			"box hits hint: " + (layout.boxY + layout.boxH));
		assertTrue(layout.size > 0f);
	}
}
