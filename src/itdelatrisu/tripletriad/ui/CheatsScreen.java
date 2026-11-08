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
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Cheats: one text line where the player types a code that unlocks cards
 * into Meu Deck (PS1 window style, like the memory card screen).
 * <p>
 * Card categories follow the FF8 card levels in {@code cards/deck.txt}:
 * 1-7 monsters (6-7 are the boss cards, counted as monsters), 8-9 Guardian
 * Forces, 10 characters.
 */
public class CheatsScreen extends Screen {
	/** Max code length. */
	private static final int MAX_LENGTH = 16;

	/** Game. */
	private final TripleTriad game;

	/** Typed code. */
	private final StringBuilder code = new StringBuilder();

	/** On-screen keyboard (joystick). */
	private final VirtualKeyboard vk = new VirtualKeyboard();

	/** Feedback line and whether it is a success. */
	private String feedback;
	private boolean feedbackOk;

	/**
	 * Creates the screen.
	 * @param game the game
	 */
	public CheatsScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		code.setLength(0);
		feedback = null;
		vk.autoOpen();
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		int width = container.getWidth();
		int height = container.getHeight();
		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight();
		g.setColor(new Color(0f, 0f, 0f));
		g.fillRect(0, 0, width, height);

		float m = width * 0.035f;
		float bh = line * 1.9f;
		float gap = height * 0.012f;
		float y0 = height * 0.03f;
		float titleW = width * 0.22f;
		float infoW = width - 2f * m - titleW - gap;
		MemoryCardScreen.drawPanel(g, m, y0, infoW, bh, MemoryCardScreen.PANEL_TOP, MemoryCardScreen.PANEL_BOTTOM);
		drawTagged(small, "INFO", I18n.cheatsInfo(), m, y0, bh);
		float titleX = m + infoW + gap;
		MemoryCardScreen.drawPanel(g, titleX, y0, titleW, bh, MemoryCardScreen.PANEL_TOP, MemoryCardScreen.PANEL_BOTTOM);
		String title = I18n.menuCheats();
		small.drawString(titleX + (titleW - small.getWidth(title)) / 2f, y0 + (bh - line) / 2f, title, Color.white);

		float y1 = y0 + bh + gap;
		MemoryCardScreen.drawPanel(g, m, y1, width - 2f * m, bh, MemoryCardScreen.PANEL_TOP, MemoryCardScreen.PANEL_BOTTOM);
		drawTagged(small, I18n.cheatsHelpTag(), I18n.cheatsHelp(), m, y1, bh);

		// the input line
		float inY = height * 0.40f;
		float inH = line * 2.2f;
		float inX = width * 0.20f;
		float inW = width * 0.60f;
		MemoryCardScreen.drawPanel(g, inX, inY, inW, inH, MemoryCardScreen.INNER_TOP, MemoryCardScreen.INNER_BOTTOM);
		boolean blink = (System.currentTimeMillis() / 500L) % 2L == 0L;
		String text = "> " + code + (blink ? "_" : "");
		small.drawString(inX + line * 0.6f, inY + (inH - line) / 2f, text, Color.white);

		if (feedback != null)
			Ui.drawCentered(small, feedback, inY + inH + line * 1.2f, feedbackOk ? Ui.TITLE : Ui.DISABLED);

		if (vk.isActive())
			vk.render(g, height * 0.56f, height * 0.97f);
		else
			Ui.drawCentered(small, I18n.hintCheats(), height * 0.90f, Ui.HINT);
	}

	private static void drawTagged(UnicodeFont small, String tag, String text, float x, float y, float h) {
		float line = small.getLineHeight();
		float ty = y + (h - line) / 2f;
		small.drawString(x + line * 0.4f, ty, tag, MemoryCardScreen.TAG);
		small.drawString(x + line * 0.4f + small.getWidth(tag + "   "), ty, text, Color.white);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (vk.handle(key, c)) {
		case VirtualKeyboard.PASS: break;
		case VirtualKeyboard.CHAR: key = 0; c = Character.toLowerCase(vk.typed()); break;
		case VirtualKeyboard.DELETE: key = Input.KEY_BACK; break;
		case VirtualKeyboard.OK: key = Input.KEY_ENTER; break;
		case VirtualKeyboard.CANCEL: key = Input.KEY_ESCAPE; break;
		default: return;
		}
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			return;
		case Input.KEY_ENTER:
			submit();
			return;
		case Input.KEY_BACK:
			if (code.length() > 0)
				code.setLength(code.length() - 1);
			return;
		default:
			break;
		}
		if (code.length() < MAX_LENGTH && c > ' ' && c < 127 && Character.isLetterOrDigit(c)) {
			code.append(c);
			AudioController.playCursor();
		}
	}

	/**
	 * Applies the typed code.
	 */
	private void submit() {
		String typed = code.toString().trim().toLowerCase();
		code.setLength(0);
		int minLevel, maxLevel;
		switch (typed) {
			case "cid": minLevel = 1; maxLevel = 10; break;
			case "edea": minLevel = 8; maxLevel = 9; break;
			case "balamb": minLevel = 10; maxLevel = 10; break;
			case "monster": minLevel = 1; maxLevel = 7; break;
			default:
				feedback = I18n.cheatsInvalid();
				feedbackOk = false;
				AudioController.Effect.INVALID.play();
				return;
		}
		int added = unlock(minLevel, maxLevel);
		feedback = I18n.cheatsUnlocked(added);
		feedbackOk = true;
		AudioController.Effect.SELECT.play();
	}

	/**
	 * Adds one copy of every card in a level range that Meu Deck does not have yet,
	 * then writes the profile.
	 * @return how many cards were added
	 */
	private int unlock(int minLevel, int maxLevel) {
		Profile profile = game.getProfile();
		if (profile == null)
			return 0;
		int added = 0;
		for (Card card : game.getDeck().getCards()) {
			int level = card.getLevel();
			if (level < minLevel || level > maxLevel)
				continue;
			Integer id = Integer.valueOf(card.getID());
			if (profile.getCollection().contains(id))
				continue;
			profile.addCard(card.getID());
			added++;
		}
		if (added > 0)
			game.saveProfile();
		return added;
	}
}
