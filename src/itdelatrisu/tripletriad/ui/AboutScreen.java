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

import itdelatrisu.tripletriad.About;
import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * About screen in the PS1 memory-card layout: fork credits, what the fork added,
 * license and the Square Enix notice. Up/down (or the wheel) scrolls; Esc returns to the menu.
 */
public class AboutScreen extends Screen {
	/** Game instance. */
	private final TripleTriad game;

	/** Wrapped lines for the current width and language. */
	private final ArrayList<String> lines = new ArrayList<String>();

	/** Parallel flags: true for heading lines. */
	private final ArrayList<Boolean> heads = new ArrayList<Boolean>();

	/** First visible line. */
	private int scroll;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public AboutScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		rebuild();
		scroll = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		int width = container.getWidth();
		int height = container.getHeight();
		Ps1.frame(g, I18n.aboutInfo(), I18n.menuAbout(), I18n.aboutHelp());

		float m = Ps1.margin();
		float top = Ps1.contentTop();
		float bottom = height * 0.86f;
		float panelW = width - 2f * m;
		Ps1.panel(g, m, top, panelW, bottom - top);

		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight();
		float lineH = line * 1.18f;
		float pad = line * 0.6f;
		int visible = visible();
		clamp(visible);

		// card back as a small emblem in the corner
		Image emblem = GameImage.CARD_BACK.getImage();
		float emblemSize = Math.min(line * 3.2f, (bottom - top) * 0.25f);
		if (emblem != null) {
			emblem.setAlpha(0.9f);
			emblem.draw(m + panelW - pad - emblemSize, top + pad, emblemSize, emblemSize);
			emblem.setAlpha(1f);
		}

		float x = m + pad;
		float y = top + pad;
		for (int i = 0; i < visible && scroll + i < lines.size(); i++) {
			String text = lines.get(scroll + i);
			boolean head = heads.get(scroll + i);
			Color color = head ? Ui.TITLE : Color.white;
			small.drawString(x, y + i * lineH, text, color);
		}

		// scroll arrows, like the memory card pages
		if (scroll > 0)
			small.drawString(m + panelW - pad - small.getWidth("^"), bottom - pad - line * 2.2f, "^", Ui.TITLE);
		if (scroll + visible < lines.size())
			small.drawString(m + panelW - pad - small.getWidth("v"), bottom - pad - line, "v", Ui.TITLE);

		Ps1.hint(I18n.hintAbout());
	}

	@Override
	public void keyPressed(int key, char c) {
		int visible = visible();
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_DOWN:
			if (scroll < max(visible)) {
				scroll++;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_UP:
			if (scroll > 0) {
				scroll--;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_LEFT:
			scroll = Math.max(0, scroll - visible);
			break;
		case Input.KEY_RIGHT:
			scroll = Math.min(max(visible), scroll + visible);
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		int visible = visible();
		if (change < 0)
			scroll = Math.min(max(visible), scroll + 1);
		else if (change > 0)
			scroll = Math.max(0, scroll - 1);
	}

	/** Re-wraps the text for the current window width and language. */
	private void rebuild() {
		lines.clear();
		heads.clear();
		UnicodeFont small = Options.getSmallFont();
		float m = Ps1.margin();
		float pad = small.getLineHeight() * 0.6f;
		float emblem = small.getLineHeight() * 3.2f;
		float maxWidth = Options.getWidth() - 2f * m - pad * 3f - emblem;
		for (String para : About.paragraphs(Options.getLang())) {
			if (para.isEmpty()) {
				lines.add("");
				heads.add(Boolean.FALSE);
				continue;
			}
			boolean head = para.charAt(0) == '#';
			String text = head ? para.substring(1) : para;
			for (String w : wrap(small, text, maxWidth)) {
				lines.add(w);
				heads.add(head);
			}
		}
	}

	private static ArrayList<String> wrap(UnicodeFont font, String text, float maxWidth) {
		ArrayList<String> out = new ArrayList<String>();
		StringBuilder current = new StringBuilder();
		for (String word : text.split(" ")) {
			String trial = current.length() == 0 ? word : current + " " + word;
			if (font.getWidth(trial) <= maxWidth || current.length() == 0) {
				current.setLength(0);
				current.append(trial);
			} else {
				out.add(current.toString());
				current.setLength(0);
				current.append(word);
			}
		}
		if (current.length() > 0)
			out.add(current.toString());
		return out;
	}

	private int visible() {
		float top = Ps1.contentTop();
		float bottom = Options.getHeight() * 0.86f;
		UnicodeFont small = Options.getSmallFont();
		float pad = small.getLineHeight() * 0.6f;
		float lineH = small.getLineHeight() * 1.18f;
		return Math.max(1, (int) ((bottom - top - pad * 2f) / lineH));
	}

	private int max(int visible) {
		return Math.max(0, lines.size() - visible);
	}

	private void clamp(int visible) {
		scroll = Math.max(0, Math.min(scroll, max(visible)));
	}
}
