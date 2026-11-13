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

package itdelatrisu.tripletriad.ui;

import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * "Quit the game?" overlay, drawn as an FFVIII window over the current screen.
 * Opens from Esc (where Esc used to quit) and from the menu's Quit entry.
 * The default answer is No. The key logic has no audio or GL calls so it can be tested.
 */
public final class ExitConfirm {
	/** What a key or click did. */
	public enum Result {
		/** Nothing happened. */
		NONE,
		/** The cursor moved between No and Yes. */
		MOVE,
		/** The dialog closed and the game keeps running. */
		STAY,
		/** The player confirmed: quit the game. */
		EXIT
	}

	/** Index of the No answer (default). */
	public static final int NO = 0;

	/** Index of the Yes answer. */
	public static final int YES = 1;

	/** True while the overlay is showing. */
	private boolean open;

	/** Highlighted answer. */
	private int choice = NO;

	/** Opens the dialog with No highlighted. */
	public void open() {
		open = true;
		choice = NO;
	}

	/** Closes the dialog. */
	public void close() {
		open = false;
		choice = NO;
	}

	/** @return true while the dialog is showing */
	public boolean isOpen() { return open; }

	/** @return {@link #NO} or {@link #YES} */
	public int choice() { return choice; }

	/**
	 * Handles a canonical key (see {@code Controls}).
	 * Left/right/up/down move, Z/Enter confirms the highlighted answer, Esc answers No.
	 * @param key the key code
	 * @return what happened
	 */
	public Result key(int key) {
		if (!open)
			return Result.NONE;
		switch (key) {
		case Input.KEY_LEFT:
		case Input.KEY_RIGHT:
		case Input.KEY_UP:
		case Input.KEY_DOWN:
			choice = 1 - choice;
			return Result.MOVE;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (choice == YES) {
				open = false;
				return Result.EXIT;
			}
			close();
			return Result.STAY;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
			close();
			return Result.STAY;
		default:
			return Result.NONE;
		}
	}

	/**
	 * Picks an answer directly (mouse click on it).
	 * @param answer {@link #NO} or {@link #YES}
	 * @return {@link Result#EXIT} for Yes, {@link Result#STAY} for No
	 */
	public Result answer(int answer) {
		if (!open)
			return Result.NONE;
		if (answer == YES) {
			open = false;
			return Result.EXIT;
		}
		close();
		return Result.STAY;
	}

	/**
	 * Handles a left click.
	 * @return what happened; a click outside both answers does nothing
	 */
	public Result click(int x, int y) {
		if (!open)
			return Result.NONE;
		Box box = measure();
		for (int i = 0; i < 2; i++) {
			if (x >= box.optX[i] - box.pad && x <= box.optX[i] + box.optW[i] + box.pad
					&& y >= box.optY - box.pad * 0.5f && y <= box.optY + box.line + box.pad * 0.5f)
				return answer(i);
		}
		return Result.NONE;
	}

	/** Draws the overlay over whatever screen is underneath. */
	public void render(Graphics g) {
		if (!open)
			return;
		int width = Options.getWidth();
		int height = Options.getHeight();
		g.setColor(new Color(0f, 0f, 0f, 0.62f));
		g.fillRect(0, 0, width, height);

		Box box = measure();
		MenuScreen.drawWindow(g, box.x, box.y, box.w, box.h, true, 1f);
		UnicodeFont title = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		String question = Ui.fit(title, I18n.confirmExitGame(), box.w - box.pad * 2f);
		float qx = box.x + (box.w - title.getWidth(question)) / 2f;
		float qy = box.y + box.pad;
		title.drawString(qx + 1f, qy + 1f, question, new Color(0.02f, 0.04f, 0.10f, 0.9f));
		title.drawString(qx, qy, question, Ui.TITLE);

		g.setColor(new Color(0.75f, 0.86f, 1f, 0.55f));
		g.fillRect(box.x + 8f, box.optY - box.pad * 0.6f, box.w - 16f, 2f);

		String[] labels = { I18n.confirmLeaveNo(), I18n.confirmLeaveYes() };
		for (int i = 0; i < 2; i++) {
			boolean on = i == choice;
			if (on) {
				g.setColor(new Color(0.55f, 0.75f, 1f, 0.28f));
				g.fillRect(box.optX[i] - box.pad * 0.4f, box.optY - box.pad * 0.15f,
					box.optW[i] + box.pad * 0.8f, box.line + box.pad * 0.3f);
				Image hand = GameImage.CURSOR.getImage();
				if (hand != null) {
					float ch = box.line;
					float cw = hand.getHeight() > 0 ? ch * hand.getWidth() / (float) hand.getHeight() : ch;
					hand.draw(box.optX[i] - cw - box.pad * 0.5f, box.optY, cw, ch);
				}
			}
			small.drawString(box.optX[i], box.optY, labels[i],
				on ? Color.white : new Color(0.74f, 0.82f, 0.94f));
		}
		HintBar.draw(I18n.hintExitConfirm(), height * 0.90f);
	}

	/** Window geometry for this frame. */
	private static Box measure() {
		int width = Options.getWidth();
		int height = Options.getHeight();
		UnicodeFont title = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		Box b = new Box();
		b.line = small.getLineHeight();
		b.pad = Math.max(10f, b.line * 0.7f);
		float want = Math.max(title.getWidth(I18n.confirmExitGame()) + b.pad * 2f, width * 0.40f);
		b.w = Math.min(width * 0.9f, want);
		b.h = b.pad * 3.2f + title.getLineHeight() + b.line;
		b.x = (width - b.w) / 2f;
		b.y = (height - b.h) / 2f - height * 0.04f;
		b.optY = b.y + b.pad * 1.9f + title.getLineHeight();
		String[] labels = { I18n.confirmLeaveNo(), I18n.confirmLeaveYes() };
		for (int i = 0; i < 2; i++) {
			b.optW[i] = small.getWidth(labels[i]);
			float center = b.x + b.w * (i == 0 ? 0.33f : 0.67f);
			b.optX[i] = center - b.optW[i] / 2f;
		}
		return b;
	}

	/** Geometry holder. */
	private static final class Box {
		float x, y, w, h, pad, line, optY;
		final float[] optX = new float[2];
		final float[] optW = new float[2];
	}
}
