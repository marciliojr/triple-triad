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

/**
 * Layout of Squall's rule-example board or trade hand above the dialogue box.
 * Cards are about 1.85 times the original mini size, fitted so the grid stays
 * on screen and clear of the box and portraits (which sit inside the box).
 */
public final class StoryRuleExampleLayout {
	/** Previous board card size, as a fraction of height. */
	public static final float OLD_BOARD_H = 0.115f;
	/** Previous board card size, as a fraction of width. */
	public static final float OLD_BOARD_W = 0.12f;
	/** Previous trade-hand card size, as a fraction of height. */
	public static final float OLD_HAND_H = 0.12f;
	/** Previous trade-hand card size, as a fraction of width. */
	public static final float OLD_HAND_W = 0.10f;
	/** Target multiplier over the original mini-card size. */
	public static final float SIZE_SCALE = 1.85f;

	public static final float NORMAL_BOX_Y = 0.62f;
	public static final float NORMAL_BOX_H = 0.28f;
	/** Lower, slightly shorter box used while a lesson example is on screen. */
	public static final float EXAMPLE_BOX_Y = 0.735f;
	public static final float EXAMPLE_BOX_H = 0.22f;
	public static final float HINT_Y = 0.96f;
	public static final float BOX_WIDTH = 0.92f;

	public final float boxX, boxY, boxW, boxH;
	public final float size, gap, ox, oy, gridW, gridH, pad, frame;
	public final boolean hand;

	private StoryRuleExampleLayout(float boxX, float boxY, float boxW, float boxH,
			float size, float gap, float ox, float oy, float gridW, float gridH,
			float pad, float frame, boolean hand) {
		this.boxX = boxX;
		this.boxY = boxY;
		this.boxW = boxW;
		this.boxH = boxH;
		this.size = size;
		this.gap = gap;
		this.ox = ox;
		this.oy = oy;
		this.gridW = gridW;
		this.gridH = gridH;
		this.pad = pad;
		this.frame = frame;
		this.hand = hand;
	}

	/**
	 * Dialogue box used when no example is showing.
	 */
	public static StoryRuleExampleLayout dialogue(int width, int height) {
		return boxOnly(width, height, false);
	}

	/**
	 * @param width screen width
	 * @param height screen height
	 * @param hand true for the 5-card trade row, false for the 3x3 board
	 * @param count cards in a hand row (ignored for a board)
	 * @return the layout
	 */
	public static StoryRuleExampleLayout of(int width, int height, boolean hand, int count) {
		float boxW = width * BOX_WIDTH;
		float boxX = (width - boxW) / 2f;
		float boxH = height * EXAMPLE_BOX_H;
		float boxY = height * EXAMPLE_BOX_Y;
		float hintY = height * HINT_Y;
		float minGap = height * 0.012f;
		if (boxY + boxH > hintY - minGap)
			boxY = hintY - minGap - boxH;

		float top = height * 0.02f;
		float gapBox = height * 0.016f;
		float pad = Math.max(8f, Math.min(width, height) * 0.012f);
		float availH = boxY - top - gapBox - 2f * pad;
		float availW = width * 0.94f - 2f * pad;
		if (availH < 8f)
			availH = 8f;
		if (availW < 8f)
			availW = 8f;

		float size;
		float gap;
		float gridW;
		float gridH;
		if (hand) {
			int n = Math.max(1, count);
			float old = Math.min(height * OLD_HAND_H, width * OLD_HAND_W);
			float target = old * 2f;
			float gapRatio = 0.08f;
			float maxByW = availW / (n + Math.max(0, n - 1) * gapRatio);
			size = Math.min(target, Math.min(availH, maxByW));
			gap = size * gapRatio;
			gridW = n * size + Math.max(0, n - 1) * gap;
			gridH = size;
		} else {
			float old = Math.min(height * OLD_BOARD_H, width * OLD_BOARD_W);
			float target = old * SIZE_SCALE;
			float gapRatio = 0.07f;
			float maxByH = availH / (3f + 2f * gapRatio);
			float maxByW = availW / (3f + 2f * gapRatio);
			size = Math.min(target, Math.min(maxByH, maxByW));
			gap = size * gapRatio;
			gridW = 3f * size + 2f * gap;
			gridH = gridW;
		}
		float ox = (width - gridW) / 2f;
		float oy = boxY - gapBox - pad - gridH;
		if (oy < top)
			oy = top;
		float frame = Math.max(3f, size * 0.036f);
		return new StoryRuleExampleLayout(boxX, boxY, boxW, boxH, size, gap, ox, oy,
			gridW, gridH, pad, frame, hand);
	}

	private static StoryRuleExampleLayout boxOnly(int width, int height, boolean example) {
		float boxW = width * BOX_WIDTH;
		float boxX = (width - boxW) / 2f;
		float boxH = height * (example ? EXAMPLE_BOX_H : NORMAL_BOX_H);
		float boxY = height * (example ? EXAMPLE_BOX_Y : NORMAL_BOX_Y);
		return new StoryRuleExampleLayout(boxX, boxY, boxW, boxH, 0, 0, 0, 0, 0, 0, 0, 0, false);
	}

	/** Previous mini-card size for a 3x3 board at this resolution. */
	public static float oldBoardSize(int width, int height) {
		return Math.min(height * OLD_BOARD_H, width * OLD_BOARD_W);
	}

	/** Previous mini-card size for the trade hand at this resolution. */
	public static float oldHandSize(int width, int height) {
		return Math.min(height * OLD_HAND_H, width * OLD_HAND_W);
	}

	/** @return true if the example panel stays above the dialogue box */
	public boolean fitsAboveBox() {
		return oy >= 0f && oy + gridH + pad <= boxY + 0.5f;
	}
}
