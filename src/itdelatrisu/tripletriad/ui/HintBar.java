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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.Gfx;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * PS1-style button legend. A hint such as
 * {@code "[Z]/[A] seleciona    [Esc]/[B] volta"} becomes a dark panel with one
 * entry per action: key badges (keyboard keys as gray keycaps, joystick
 * face buttons colored like a pad) followed by the action name. Entries
 * flow into as many centered rows as needed; when more than two rows would
 * be used the smaller legend font is tried. Text is never cut.
 */
public final class HintBar {
	private HintBar() {}

	/** Top of the last drawn legend panel. */
	private static float lastTop;

	/** @return the y of the top edge of the last legend (to place notices above it) */
	public static float lastTop() { return lastTop; }

	private static final Pattern BADGE = Pattern.compile("\\[([^\\]]+)\\]");

	private static final Color BAR_BG = new Color(0f, 0f, 0f, 0.72f);
	private static final Color BAR_LINE = new Color(0.55f, 0.55f, 0.62f, 0.9f);
	private static final Color KEY_BG = new Color(0.82f, 0.82f, 0.86f);
	private static final Color KEY_SHADE = new Color(0.45f, 0.45f, 0.52f);
	private static final Color KEY_TEXT = new Color(0.08f, 0.08f, 0.12f);
	private static final Color PAD_A = new Color(0.30f, 0.72f, 0.35f);
	private static final Color PAD_B = new Color(0.85f, 0.28f, 0.28f);
	private static final Color PAD_X = new Color(0.28f, 0.48f, 0.90f);
	private static final Color PAD_Y = new Color(0.92f, 0.76f, 0.22f);
	private static final Color PAD_OTHER = new Color(0.30f, 0.30f, 0.38f);

	/** One legend entry. */
	private static final class Item {
		final List<String> badges = new ArrayList<String>();
		final List<Boolean> slash = new ArrayList<Boolean>(); // "/" before badge i
		String label = "";
	}

	private static List<Item> parse(String text) {
		List<Item> items = new ArrayList<Item>();
		for (String part : text.trim().split("\\s{2,}")) {
			if (part.isEmpty())
				continue;
			Item it = new Item();
			Matcher m = BADGE.matcher(part);
			int pos = 0;
			StringBuilder label = new StringBuilder();
			while (m.find()) {
				String between = part.substring(pos, m.start());
				if (!it.badges.isEmpty() && between.trim().equals("/"))
					it.slash.add(Boolean.TRUE);
				else {
					label.append(between);
					it.slash.add(Boolean.FALSE);
				}
				it.badges.add(m.group(1));
				pos = m.end();
			}
			label.append(part.substring(pos));
			it.label = label.toString().replaceAll("\\s+", " ").trim();
			items.add(it);
		}
		return items;
	}

	private static boolean isPad(String b) {
		return b.equals("A") || b.equals("B") || b.equals("X") || b.equals("Y") || b.startsWith("L") && b.length() == 2
			|| b.startsWith("R") && b.length() == 2 && Character.isDigit(b.charAt(1)) || b.equals("START") || b.equals("SELECT")
			|| b.startsWith("D-pad") || b.startsWith("Bot") || b.startsWith("Button");
	}

	private static float badgeW(UnicodeFont f, String b) {
		float line = f.getLineHeight();
		return Math.max(line * 1.15f, f.getWidth(b) + line * 0.6f);
	}

	private static float itemW(UnicodeFont f, Item it) {
		float line = f.getLineHeight();
		float w = 0;
		for (int i = 0; i < it.badges.size(); i++) {
			if (i > 0)
				w += it.slash.get(i) ? f.getWidth("/") + line * 0.2f : line * 0.15f;
			w += badgeW(f, it.badges.get(i));
		}
		if (!it.label.isEmpty())
			w += (it.badges.isEmpty() ? 0 : line * 0.35f) + f.getWidth(it.label);
		return w;
	}

	/** Splits items into rows that fit the width. */
	private static List<List<Item>> rows(UnicodeFont f, List<Item> items, float maxW, float gap) {
		List<List<Item>> rows = new ArrayList<List<Item>>();
		List<Item> cur = new ArrayList<Item>();
		float w = 0;
		for (Item it : items) {
			float iw = itemW(f, it);
			if (!cur.isEmpty() && w + gap + iw > maxW) {
				rows.add(cur);
				cur = new ArrayList<Item>();
				w = 0;
			}
			w += (cur.isEmpty() ? 0 : gap) + iw;
			cur.add(it);
		}
		if (!cur.isEmpty())
			rows.add(cur);
		return rows;
	}

	/**
	 * Draws the legend.
	 * @param text the hint text (dynamic bindings already applied)
	 * @param y the y where the old one-line hint was drawn
	 */
	public static void draw(String text, float y) {
		if (text == null || text.trim().isEmpty())
			return;
		Graphics g = Gfx.getGraphics();
		int width = Options.getWidth(), height = Options.getHeight();
		List<Item> items = parse(text);
		float maxW = width * 0.92f;
		UnicodeFont f = Options.getSmallFont();
		List<List<Item>> rows = rows(f, items, maxW, f.getLineHeight() * 1.3f);
		if (rows.size() > 2) {
			UnicodeFont t = Options.getTinyFont();
			List<List<Item>> r2 = rows(t, items, maxW, t.getLineHeight() * 1.3f);
			if (r2.size() < rows.size()) {
				f = t;
				rows = r2;
			}
		}
		float line = f.getLineHeight();
		float gap = line * 1.3f;
		float rowH = line * 1.45f;
		float padY = line * 0.3f;
		float barH = rows.size() * rowH + padY * 2;
		float barW = 0;
		for (List<Item> row : rows) {
			float w = 0;
			for (Item it : row)
				w += itemW(f, it);
			w += gap * (row.size() - 1);
			barW = Math.max(barW, w);
		}
		barW = Math.min(width - 4f, barW + line * 1.6f);
		// footer hints sit on the bottom edge and grow upwards; others center on the old line
		float oldLine = Options.getSmallFont().getLineHeight();
		float top;
		if (y > height * 0.75f)
			top = Math.min(height - 4f, y + oldLine + padY * 2) - barH;
		else
			top = y + oldLine / 2f - barH / 2f;
		if (top + barH > height - 4f)
			top = height - 4f - barH;
		if (top < 2f)
			top = 2f;
		lastTop = top;
		float left = (width - barW) / 2f;
		g.setColor(BAR_BG);
		g.fillRect(left, top, barW, barH);
		g.setColor(BAR_LINE);
		g.drawRect(left, top, barW, barH);
		g.drawRect(left + 2, top + 2, barW - 4, barH - 4);

		for (int r = 0; r < rows.size(); r++) {
			List<Item> row = rows.get(r);
			float w = 0;
			for (Item it : row)
				w += itemW(f, it);
			w += gap * (row.size() - 1);
			float x = (width - w) / 2f;
			float ry = top + padY + r * rowH;
			for (Item it : row) {
				x = drawItem(g, f, it, x, ry, rowH);
				x += gap;
			}
		}
	}

	private static float drawItem(Graphics g, UnicodeFont f, Item it, float x, float ry, float rowH) {
		float line = f.getLineHeight();
		float bh = line * 1.12f;
		float by = ry + (rowH - bh) / 2f;
		float ty = ry + (rowH - line) / 2f + line * 0.06f;
		for (int i = 0; i < it.badges.size(); i++) {
			if (i > 0) {
				if (it.slash.get(i)) {
					x += line * 0.1f;
					f.drawString(x, ty, "/", Ui.HINT);
					x += f.getWidth("/") + line * 0.1f;
				} else
					x += line * 0.15f;
			}
			String b = it.badges.get(i);
			float bw = badgeW(f, b);
			Color bg, fg;
			if (isPad(b)) {
				bg = b.equals("A") ? PAD_A : b.equals("B") ? PAD_B : b.equals("X") ? PAD_X : b.equals("Y") ? PAD_Y : PAD_OTHER;
				fg = b.equals("Y") ? KEY_TEXT : Color.white;
				// rounded pad button: body + clipped corners
				g.setColor(bg);
				float c = bh * 0.22f;
				g.fillRect(x + c, by, bw - 2 * c, bh);
				g.fillRect(x, by + c, bw, bh - 2 * c);
				g.fillRect(x + c * 0.4f, by + c * 0.4f, bw - c * 0.8f, bh - c * 0.8f);
			} else {
				// keyboard keycap with a darker bottom edge
				bg = KEY_BG;
				fg = KEY_TEXT;
				g.setColor(KEY_SHADE);
				g.fillRect(x, by, bw, bh);
				g.setColor(bg);
				g.fillRect(x + 1, by + 1, bw - 2, bh - bh * 0.14f - 1);
			}
			f.drawString(x + (bw - f.getWidth(b)) / 2f, ty, b, fg);
			x += bw;
		}
		if (!it.label.isEmpty()) {
			if (!it.badges.isEmpty())
				x += line * 0.35f;
			f.drawString(x, ty, it.label, Color.white);
			x += f.getWidth(it.label);
		}
		return x;
	}
}
