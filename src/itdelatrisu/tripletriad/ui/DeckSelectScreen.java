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
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Triple Triad.  If not, see <http://www.gnu.org/licenses/>.
 */

package itdelatrisu.tripletriad.ui;

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.SavedDeck;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;

import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Choose a saved deck or create a new one for Quick Game.
 */
public class DeckSelectScreen extends Screen {
	/** Game instance. */
	private final TripleTriad game;

	/** Selected row (0 = new deck). */
	private int selected;

	/** First visible row when the list is long. */
	private int scroll;

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
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();
		int width = container.getWidth();

		Ui.drawCentered(font, "Jogo R\u00e1pido", height * 0.08f, Ui.TITLE);
		Ui.drawCentered(small, "Escolha um deck ou monte um novo", height * 0.16f, Ui.HINT);

		float startY = height * 0.24f;
		float line = small.getLineHeight() * 1.6f;
		int visible = Math.max(4, (int) ((height * 0.62f - startY) / line));
		clampScroll(visible);

		int total = rowCount();
		for (int row = scroll; row < total && row < scroll + visible; row++) {
			float y = startY + (row - scroll) * line;
			boolean on = (row == selected);
			String label = (row == 0) ? "+  Novo deck" : deckLabel(row - 1);
			Ui.drawCentered(small, label, y, on ? Ui.SELECTED : Ui.HINT);

			if (row > 0) {
				SavedDeck deck = game.getProfile().getDecks().get(row - 1);
				drawMiniCards(deck, y + small.getLineHeight() * 0.05f, width);
			}
		}

		Ui.drawCentered(small, "Enter jogar    E editar    Del apagar    Esc voltar",
				height * 0.90f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		int total = rowCount();
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_DOWN:
			selected = (selected + 1) % total;
			AudioController.Effect.SELECT.play();
			break;
		case Input.KEY_UP:
			selected = (selected + total - 1) % total;
			AudioController.Effect.SELECT.play();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (selected == 0) {
				AudioController.Effect.SELECT.play();
				game.showDeckBuilder(null);
			} else {
				playDeck(selected - 1);
			}
			break;
		case Input.KEY_E:
			if (selected == 0) {
				AudioController.Effect.INVALID.play();
			} else {
				AudioController.Effect.SELECT.play();
				game.showDeckBuilder(game.getProfile().getDecks().get(selected - 1));
			}
			break;
		case Input.KEY_DELETE:
			if (selected == 0) {
				AudioController.Effect.INVALID.play();
			} else {
				deleteDeck(selected - 1);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		int row = hitRow(y);
		if (row < 0)
			return;
		if (row != selected) {
			selected = row;
			AudioController.Effect.SELECT.play();
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
		if (change < 0)
			selected = Math.min(rowCount() - 1, selected + 1);
		else if (change > 0)
			selected = Math.max(0, selected - 1);
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

	private int hitRow(int y) {
		UnicodeFont small = Options.getSmallFont();
		float startY = Options.getHeight() * 0.24f;
		float line = small.getLineHeight() * 1.6f;
		int visible = Math.max(4, (int) ((Options.getHeight() * 0.62f - startY) / line));
		int total = rowCount();
		for (int row = scroll; row < total && row < scroll + visible; row++) {
			float top = startY + (row - scroll) * line;
			if (y >= top && y < top + line)
				return row;
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

	private void drawMiniCards(SavedDeck deck, float y, int width) {
		Deck catalog = game.getDeck();
		int[] ids = deck.getCardIds();
		float size = Options.getCardLength() * 0.18f;
		float gap = size * 0.12f;
		float totalW = ids.length * size + (ids.length - 1) * gap;
		float x = (width - totalW) / 2f + Options.getSmallFont().getWidth(deck.getName()) * 0.55f;
		if (x + totalW > width - 20)
			x = width - 20 - totalW;
		for (int i = 0; i < ids.length; i++) {
			Card card = catalog.getCardById(ids[i]);
			if (card != null)
				card.drawSized(x + i * (size + gap), y, size, true, false);
		}
	}
}
