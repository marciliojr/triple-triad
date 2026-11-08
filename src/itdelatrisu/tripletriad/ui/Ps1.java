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
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Shared layout for the PS1 (FF8 memory card) style screens: black background,
 * INFO + title header, HELP box and a vertical list of gray panels.
 */
public final class Ps1 {
	private Ps1() {}

	/** @return outer margin */
	public static float margin() { return Options.getWidth() * 0.035f; }

	/** @return header/help box height */
	public static float boxHeight() { return Options.getSmallFont().getLineHeight() * 1.9f; }

	/** @return gap between panels */
	public static float gap() { return Options.getHeight() * 0.012f; }

	/** @return y where content below the HELP box starts */
	public static float contentTop() {
		return Options.getHeight() * 0.03f + boxHeight() * 2f + gap() * 3f;
	}

	/** @return the help tag in the current language */
	public static String helpTag() {
		switch (Options.getLang()) {
			case EN: return "HELP";
			case ES: return "AYUDA";
			default: return "AJUDA";
		}
	}

	/**
	 * Draws background, INFO header, title box and HELP box.
	 */
	public static void frame(Graphics g, String info, String title, String help) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		UnicodeFont small = Options.getSmallFont();
		g.setColor(new Color(0f, 0f, 0f));
		g.fillRect(0, 0, width, height);
		float m = margin();
		float y0 = height * 0.03f;
		float bh = boxHeight();
		float titleW = width * 0.22f;
		float infoW = width - 2f * m - titleW - gap();
		panel(g, m, y0, infoW, bh);
		tagged(small, "INFO", info, m, y0, bh);
		float titleX = m + infoW + gap();
		panel(g, titleX, y0, titleW, bh);
		String t = Ui.fit(small, title, titleW - small.getLineHeight() * 0.5f);
		small.drawString(titleX + (titleW - small.getWidth(t)) / 2f, y0 + (bh - small.getLineHeight()) / 2f, t, Color.white);
		float y1 = y0 + bh + gap();
		panel(g, m, y1, width - 2f * m, bh);
		tagged(small, helpTag(), help, m, y1, bh);
	}

	/** Gray gradient panel with border. */
	public static void panel(Graphics g, float x, float y, float w, float h) {
		MemoryCardScreen.drawPanel(g, x, y, w, h, MemoryCardScreen.PANEL_TOP, MemoryCardScreen.PANEL_BOTTOM);
	}

	/** Darker inner panel. */
	public static void innerPanel(Graphics g, float x, float y, float w, float h) {
		MemoryCardScreen.drawPanel(g, x, y, w, h, MemoryCardScreen.INNER_TOP, MemoryCardScreen.INNER_BOTTOM);
	}

	/** "TAG   text" inside a box. */
	public static void tagged(UnicodeFont small, String tag, String text, float x, float y, float h) {
		float line = small.getLineHeight();
		float ty = y + (h - line) / 2f;
		small.drawString(x + line * 0.4f, ty, tag, MemoryCardScreen.TAG);
		float tx = x + line * 0.4f + small.getWidth(tag + "   ");
		small.drawString(tx, ty, Ui.fit(small, text, Options.getWidth() - tx - margin() - line), Color.white);
	}

	/** @return list row height for simple text rows */
	public static float rowHeight() { return Options.getSmallFont().getLineHeight() * 1.8f; }

	/** @return x of list rows */
	public static float rowX() { return Options.getWidth() * 0.12f; }

	/** @return width of list rows */
	public static float rowW() { return Options.getWidth() * 0.76f; }

	/** @return y of a list row */
	public static float rowY(int i) { return contentTop() + i * (rowHeight() + gap()); }

	/**
	 * Draws a simple text row panel with optional right-aligned value and hand cursor.
	 */
	public static void row(Graphics g, int i, String label, String value, boolean selected, boolean enabled) {
		UnicodeFont small = Options.getSmallFont();
		float x = rowX(), y = rowY(i), w = rowW(), h = rowHeight();
		float line = small.getLineHeight();
		if (selected)
			panel(g, x, y, w, h);
		else
			innerPanel(g, x, y, w, h);
		Color c = enabled ? Color.white : Ui.DISABLED;
		small.drawString(x + line * 0.6f, y + (h - line) / 2f, label, c);
		if (value != null && !value.isEmpty())
			small.drawString(x + w - line * 0.6f - small.getWidth(value), y + (h - line) / 2f, value, c);
		if (selected)
			hand(x, y + (h - line) / 2f, line);
	}

	/** Hand cursor to the left of x. */
	public static void hand(float x, float textY, float line) {
		Image hand = GameImage.CURSOR.getImage();
		hand.draw(x - hand.getWidth() * 1.05f, textY + (line - hand.getHeight()) / 2f);
	}

	/**
	 * Row index under the mouse.
	 * @return the index, or -1
	 */
	public static int hitRow(int x, int y, int count) {
		if (x < rowX() || x > rowX() + rowW())
			return -1;
		for (int i = 0; i < count; i++) {
			float top = rowY(i);
			if (y >= top && y < top + rowHeight())
				return i;
		}
		return -1;
	}

	/** Footer hint. */
	public static void hint(String text) {
		Ui.drawCentered(Options.getSmallFont(), text, Options.getHeight() * 0.92f, Ui.HINT);
	}
}
