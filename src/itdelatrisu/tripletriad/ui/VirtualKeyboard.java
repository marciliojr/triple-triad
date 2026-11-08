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

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.Controls;
import itdelatrisu.tripletriad.Controls.Action;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * PS1-style on-screen keyboard for text fields, driven by the D-pad.
 * Letters, numbers, space, delete, shift, OK and cancel. It opens when input
 * comes from a joystick and closes as soon as the physical keyboard is used.
 */
public class VirtualKeyboard {
	/** Results of {@link #handle}. */
	public static final int PASS = -1, CONSUMED = 0, CHAR = 1, DELETE = 2, OK = 3, CANCEL = 4;

	private static final String[] ROWS = { "ABCDEFGHIJ", "KLMNOPQRST", "UVWXYZ-_\u00c7\u00c3", "0123456789" };
	private static final int COLS = 10;

	/** Special keys on the last row (each two columns wide). */
	private static final int SP_SPACE = 0, SP_DELETE = 1, SP_SHIFT = 2, SP_OK = 3, SP_CANCEL = 4;

	private boolean active;
	private boolean shift = true;
	private int row, col;
	private char typed;

	/** @return true if visible */
	public boolean isActive() { return active; }

	public void open() {
		if (!active) {
			active = true;
			row = 0;
			col = 0;
		}
	}

	public void close() { active = false; }

	/** Opens the keyboard if the last input came from a joystick (call on screen enter). */
	public void autoOpen() {
		if (Controls.fromPad())
			open();
		else
			close();
	}

	/** @return the character of the last {@link #CHAR} result */
	public char typed() { return typed; }

	/**
	 * Handles a key press.
	 * @return {@link #PASS} if the screen should handle it as usual, else a result
	 */
	public int handle(int key, char c) {
		if (!Controls.fromPad()) {
			close(); // physical keyboard in use
			return PASS;
		}
		if (!active) {
			if (Controls.is(key, Action.CANCEL))
				return PASS;
			open();
			AudioController.playCursor();
			return CONSUMED;
		}
		int rows = ROWS.length + 1;
		if (Controls.is(key, Action.UP)) {
			row = (row + rows - 1) % rows;
			AudioController.playCursor();
		} else if (Controls.is(key, Action.DOWN)) {
			row = (row + 1) % rows;
			AudioController.playCursor();
		} else if (Controls.is(key, Action.LEFT)) {
			col = (row == ROWS.length) ? ((col / 2 + 4) % 5) * 2 : (col + COLS - 1) % COLS;
			AudioController.playCursor();
		} else if (Controls.is(key, Action.RIGHT)) {
			col = (row == ROWS.length) ? ((col / 2 + 1) % 5) * 2 : (col + 1) % COLS;
			AudioController.playCursor();
		} else if (Controls.is(key, Action.DELETE)) {
			return DELETE;
		} else if (Controls.is(key, Action.MENU)) {
			return OK;
		} else if (Controls.is(key, Action.CANCEL)) {
			return CANCEL;
		} else if (Controls.is(key, Action.CONFIRM)) {
			if (row < ROWS.length) {
				char ch = ROWS[row].charAt(col);
				typed = shift ? ch : Character.toLowerCase(ch);
				return CHAR;
			}
			switch (col / 2) {
				case SP_SPACE: typed = ' '; return CHAR;
				case SP_DELETE: return DELETE;
				case SP_SHIFT: shift = !shift; AudioController.playCursor(); return CONSUMED;
				case SP_OK: return OK;
				default: return CANCEL;
			}
		}
		return CONSUMED;
	}

	private static String special(int i) {
		switch (i) {
			case SP_SPACE: return I18n.vkSpace();
			case SP_DELETE: return I18n.vkDelete();
			case SP_SHIFT: return I18n.vkShift();
			case SP_OK: return I18n.vkOk();
			default: return I18n.vkCancel();
		}
	}

	/**
	 * Draws the keyboard between two y coordinates (centered horizontally).
	 */
	public void render(Graphics g, float top, float bottom) {
		if (!active)
			return;
		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight();
		int rows = ROWS.length + 1;
		float hint = line * 1.4f;
		float rh = Math.min(line * 1.6f, (bottom - top - hint) / rows);
		float cw = Math.min(Options.getWidth() * 0.06f, rh * 1.6f);
		float w = cw * COLS, x0 = (Options.getWidth() - w) / 2f;
		Ps1.panel(g, x0 - cw * 0.3f, top - rh * 0.2f, w + cw * 0.6f, rh * rows + rh * 0.4f + hint);
		for (int r = 0; r < rows; r++) {
			float y = top + r * rh;
			if (r < ROWS.length) {
				for (int k = 0; k < COLS; k++) {
					boolean on = (r == row && k == col);
					float x = x0 + k * cw;
					if (on)
						Ps1.innerPanel(g, x + 1, y + 1, cw - 2, rh - 2);
					char ch = ROWS[r].charAt(k);
					String s = String.valueOf(shift ? ch : Character.toLowerCase(ch));
					small.drawString(x + (cw - small.getWidth(s)) / 2f, y + (rh - line) / 2f, s, on ? Ui.TITLE : Color.white);
				}
			} else {
				for (int k = 0; k < 5; k++) {
					boolean on = (r == row && k == col / 2);
					float x = x0 + k * cw * 2;
					if (on)
						Ps1.innerPanel(g, x + 1, y + 1, cw * 2 - 2, rh - 2);
					String s = Ui.fit(small, special(k), cw * 2 - line * 0.3f);
					Color c = (k == SP_SHIFT && shift) ? Ui.TITLE : (on ? Ui.TITLE : Color.white);
					small.drawString(x + (cw * 2 - small.getWidth(s)) / 2f, y + (rh - line) / 2f, s, c);
				}
			}
		}
		String h = Ui.fit(small, I18n.hintVirtualKeyboard(), w + cw * 0.4f);
		small.drawString((Options.getWidth() - small.getWidth(h)) / 2f, top + rows * rh + (hint - line) / 2f, h, Ui.HINT);
	}
}
