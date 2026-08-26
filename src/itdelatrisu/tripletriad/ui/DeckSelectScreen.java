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
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.SavedDeck;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Choose a saved deck or create a new one for Quick Game, and toggle match rules.
 */
public class DeckSelectScreen extends Screen {
	/** Focus on the deck list. */
	private static final int FOCUS_DECKS = 0;

	/** Focus on the rule panel. */
	private static final int FOCUS_RULES = 1;

	/** Game instance. */
	private final TripleTriad game;

	/** Selected deck row (0 = new deck). */
	private int selected;

	/** First visible row when the list is long. */
	private int scroll;

	/** Active column. */
	private int focus = FOCUS_DECKS;

	/** Selected rule index. */
	private int ruleIndex;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public DeckSelectScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		selected = 0;
		scroll = 0;
		focus = FOCUS_DECKS;
		ruleIndex = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();
		Rule[] rules = Rule.values();

		Ui.drawCentered(font, I18n.deckSelectTitle(), height * 0.06f, Ui.TITLE);
		Ui.drawCentered(small, I18n.deckSelectHint(), height * 0.13f, Ui.HINT);

		float listTop = listTop();
		float line = deckLineHeight();
		int visible = visibleDeckRows();
		clampScroll(visible);

		float deckX = deckLeft();
		int total = rowCount();
		for (int row = scroll; row < total && row < scroll + visible; row++) {
			float y = listTop + (row - scroll) * line;
			boolean on = (focus == FOCUS_DECKS && row == selected);
			String label = (row == 0) ? I18n.newDeck() : deckLabel(row - 1);
			small.drawString(deckX, y, label, on ? Ui.SELECTED : Ui.HINT);
			if (on)
				drawCursor(deckX, y, small);
			if (row > 0) {
				SavedDeck deck = game.getProfile().getDecks().get(row - 1);
				drawMiniCards(deck, y + small.getLineHeight() * 0.95f, deckX);
			}
		}

		float panelX = rulesLeft();
		float panelY = listTop - 10;
		float panelW = rulesRight() - panelX;
		float panelH = height * 0.70f - panelY;
		g.setColor(Ui.TITLE);
		g.setLineWidth(focus == FOCUS_RULES ? 2f : 1f);
		g.drawRect(panelX, panelY, panelW, panelH);
		g.setLineWidth(1f);

		small.drawString(panelX + 16, panelY + 12, I18n.rulesTitle(), Ui.TITLE);
		float ruleLine = small.getLineHeight() * 1.45f;
		float ruleStart = panelY + small.getLineHeight() * 2.1f;
		for (int i = 0; i < rules.length; i++) {
			float y = ruleStart + i * ruleLine;
			boolean on = (focus == FOCUS_RULES && i == ruleIndex);
			boolean active = rules[i].isActive();
			Color nameColor = on ? Ui.SELECTED : (active ? Ui.TITLE : Ui.HINT);
			Color stateColor = active ? Ui.TITLE : Ui.DISABLED;
			small.drawString(panelX + 36, y, rules[i].getDisplayName(), nameColor);
			String state = active ? I18n.on() : I18n.off();
			float stateX = panelX + panelW - 18 - small.getWidth(state);
			small.drawString(stateX, y, state, on ? Ui.SELECTED : stateColor);
			if (on)
				drawCursor(panelX + 36, y, small);
		}

		String hint = (focus == FOCUS_RULES)
			? I18n.hintDeckRules()
			: I18n.hintDeckList();
		Ui.drawCentered(small, hint, height * 0.92f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		int total = rowCount();
		int ruleCount = Rule.values().length;
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_LEFT:
			if (focus == FOCUS_RULES) {
				focus = FOCUS_DECKS;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_RIGHT:
			if (focus == FOCUS_DECKS) {
				focus = FOCUS_RULES;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_DOWN:
			if (focus == FOCUS_RULES)
				ruleIndex = (ruleIndex + 1) % ruleCount;
			else if (total > 0)
				selected = (selected + 1) % total;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			if (focus == FOCUS_RULES)
				ruleIndex = (ruleIndex + ruleCount - 1) % ruleCount;
			else if (total > 0)
				selected = (selected + total - 1) % total;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (focus == FOCUS_RULES)
				toggleSelectedRule();
			else if (selected == 0) {
				AudioController.Effect.SELECT.play();
				game.showDeckBuilder(null);
			} else {
				playDeck(selected - 1);
			}
			break;
		case Input.KEY_E:
			if (focus != FOCUS_DECKS || selected == 0) {
				AudioController.Effect.INVALID.play();
			} else {
				AudioController.Effect.SELECT.play();
				game.showDeckBuilder(game.getProfile().getDecks().get(selected - 1));
			}
			break;
		case Input.KEY_DELETE:
			if (focus != FOCUS_DECKS || selected == 0)
				AudioController.Effect.INVALID.play();
			else
				deleteDeck(selected - 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;

		int ruleHit = hitRule(x, y);
		if (ruleHit >= 0) {
			focus = FOCUS_RULES;
			ruleIndex = ruleHit;
			toggleSelectedRule();
			return;
		}

		int row = hitRow(x, y);
		if (row < 0)
			return;
		focus = FOCUS_DECKS;
		if (row != selected) {
			selected = row;
			AudioController.playCursor();
			return;
		}
		if (selected == 0) {
			AudioController.Effect.SELECT.play();
			game.showDeckBuilder(null);
		} else {
			playDeck(selected - 1);
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (focus == FOCUS_RULES) {
			int n = Rule.values().length;
			if (change < 0)
				ruleIndex = Math.min(n - 1, ruleIndex + 1);
			else if (change > 0)
				ruleIndex = Math.max(0, ruleIndex - 1);
			return;
		}
		if (change < 0)
			selected = Math.min(rowCount() - 1, selected + 1);
		else if (change > 0)
			selected = Math.max(0, selected - 1);
	}

	private void toggleSelectedRule() {
		Rule[] rules = Rule.values();
		if (ruleIndex < 0 || ruleIndex >= rules.length)
			return;
		Rule rule = rules[ruleIndex];
		boolean turningOn = !rule.isActive();
		rule.toggle();
		if (rule == Rule.SAME_WALL && turningOn)
			Rule.SAME.setState(true);
		Options.saveOptions();
		AudioController.Effect.SELECT.play();
	}

	private void drawCursor(float textX, float textY, UnicodeFont small) {
		Image cursor = GameImage.CURSOR.getImage();
		cursor.draw(
			textX - cursor.getWidth() * 1.15f,
			textY + (small.getLineHeight() - cursor.getHeight()) / 2f
		);
	}

	private int rowCount() {
		Profile profile = game.getProfile();
		int decks = (profile != null) ? profile.getDecks().size() : 0;
		return 1 + decks;
	}

	private String deckLabel(int index) {
		SavedDeck deck = game.getProfile().getDecks().get(index);
		return deck.getName();
	}

	private void playDeck(int index) {
		SavedDeck deck = game.getProfile().getDecks().get(index);
		if (!deck.isComplete()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.startQuickMatch(deck.getCardIds());
	}

	private void deleteDeck(int index) {
		Profile profile = game.getProfile();
		ArrayList<SavedDeck> decks = profile.getDecks();
		if (index < 0 || index >= decks.size())
			return;
		decks.remove(index);
		game.saveProfile();
		AudioController.Effect.BACK.play();
		if (selected >= rowCount())
			selected = rowCount() - 1;
	}

	private float deckLeft() { return Options.getWidth() * 0.08f; }

	private float rulesLeft() { return Options.getWidth() * 0.54f; }

	private float rulesRight() { return Options.getWidth() * 0.94f; }

	private float listTop() { return Options.getHeight() * 0.22f; }

	private float deckLineHeight() {
		return Options.getSmallFont().getLineHeight() * 2.35f;
	}

	private int visibleDeckRows() {
		float startY = listTop();
		return Math.max(3, (int) ((Options.getHeight() * 0.82f - startY) / deckLineHeight()));
	}

	private int hitRow(int x, int y) {
		if (x >= rulesLeft())
			return -1;
		float startY = listTop();
		float line = deckLineHeight();
		int visible = visibleDeckRows();
		int total = rowCount();
		for (int row = scroll; row < total && row < scroll + visible; row++) {
			float top = startY + (row - scroll) * line;
			if (y >= top && y < top + line)
				return row;
		}
		return -1;
	}

	private int hitRule(int x, int y) {
		if (x < rulesLeft() || x > rulesRight())
			return -1;
		UnicodeFont small = Options.getSmallFont();
		float panelY = listTop() - 10;
		float ruleLine = small.getLineHeight() * 1.45f;
		float ruleStart = panelY + small.getLineHeight() * 2.1f;
		Rule[] rules = Rule.values();
		for (int i = 0; i < rules.length; i++) {
			float top = ruleStart + i * ruleLine;
			if (y >= top && y < top + ruleLine)
				return i;
		}
		return -1;
	}

	private void clampScroll(int visible) {
		if (selected < scroll)
			scroll = selected;
		if (selected >= scroll + visible)
			scroll = selected - visible + 1;
		if (scroll < 0)
			scroll = 0;
	}

	private void drawMiniCards(SavedDeck deck, float y, float originX) {
		Deck catalog = game.getDeck();
		int[] ids = deck.getCardIds();
		float size = Options.getCardLength() * 0.16f;
		float gap = size * 0.10f;
		for (int i = 0; i < ids.length; i++) {
			Card card = catalog.getCardById(ids[i]);
			if (card != null)
				card.drawSized(originX + i * (size + gap), y, size, true, false);
		}
	}
}
