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
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;
import java.util.HashSet;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Short how-to-play manual in the current UI language.
 */
public class HowToPlayScreen extends Screen {
	/** Game instance. */
	private final TripleTriad game;

	/** Wrapped lines for the current width. */
	private final ArrayList<String> lines = new ArrayList<String>();

	/** Heading lines after wrap. */
	private final HashSet<String> headings = new HashSet<String>();

	/** First visible line. */
	private int scroll;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public HowToPlayScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		rebuildLines();
		scroll = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();
		int width = container.getWidth();

		Ui.drawCentered(font, I18n.menuHowTo(), height * 0.06f, Ui.TITLE);

		float top = height * 0.16f;
		float bottom = height * 0.86f;
		float lineH = small.getLineHeight() * 1.22f;
		int visible = Math.max(1, (int) ((bottom - top) / lineH));
		clampScroll(visible);

		float x = width * 0.10f;
		for (int i = 0; i < visible && scroll + i < lines.size(); i++) {
			String line = lines.get(scroll + i);
			float y = top + i * lineH;
			Color color = headings.contains(line) ? Ui.TITLE : Ui.HINT;
			small.drawString(x, y, line, color);
		}

		Ui.drawCentered(small, I18n.hintHowTo(), height * 0.92f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		int visible = visibleLines();
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_DOWN:
			scroll = Math.min(maxScroll(visible), scroll + 1);
			break;
		case Input.KEY_UP:
			scroll = Math.max(0, scroll - 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		int visible = visibleLines();
		if (change < 0)
			scroll = Math.min(maxScroll(visible), scroll + 1);
		else if (change > 0)
			scroll = Math.max(0, scroll - 1);
	}

	private void rebuildLines() {
		lines.clear();
		headings.clear();
		UnicodeFont small = Options.getSmallFont();
		int maxWidth = (int) (Options.getWidth() * 0.80f);
		String[] paras = I18n.howToParagraphs();
		for (int p = 0; p < paras.length; p++) {
			String para = paras[p];
			if (para.length() == 0) {
				lines.add("");
				continue;
			}
			boolean heading = para.charAt(0) == '#';
			if (heading)
				para = para.substring(1);
			int before = lines.size();
			wrapInto(para, small, maxWidth);
			if (heading) {
				for (int i = before; i < lines.size(); i++)
					headings.add(lines.get(i));
			}
		}
	}

	private void wrapInto(String para, UnicodeFont font, int maxWidth) {
		String[] words = para.split(" ");
		StringBuilder current = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			String trial = current.length() == 0 ? word : current + " " + word;
			if (font.getWidth(trial) <= maxWidth) {
				if (current.length() > 0)
					current.append(' ');
				current.append(word);
			} else {
				if (current.length() > 0)
					lines.add(current.toString());
				current = new StringBuilder(word);
			}
		}
		if (current.length() > 0)
			lines.add(current.toString());
	}

	private int visibleLines() {
		float top = Options.getHeight() * 0.16f;
		float bottom = Options.getHeight() * 0.86f;
		float lineH = Options.getSmallFont().getLineHeight() * 1.22f;
		return Math.max(1, (int) ((bottom - top) / lineH));
	}

	private int maxScroll(int visible) {
		return Math.max(0, lines.size() - visible);
	}

	private void clampScroll(int visible) {
		int max = maxScroll(visible);
		if (scroll > max)
			scroll = max;
		if (scroll < 0)
			scroll = 0;
	}
}
