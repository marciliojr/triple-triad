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
 * Main menu: Quick Game, Championship, My Deck, Save, how to play, settings.
 */
public class MenuScreen extends Screen {
	/** Number of menu entries. */
	private static final int ITEM_COUNT = 6;

	/** Game instance. */
	private final TripleTriad game;

	/** Selected index. */
	private int selected;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public MenuScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		selected = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();
		int width = container.getWidth();

		Ui.drawCentered(font, "TRIPLE TRIAD", height * 0.10f, Ui.TITLE);
		if (game.getProfile() != null && game.getProfile().isValid())
			Ui.drawCentered(small, game.getProfile().getName(), height * 0.19f, Ui.HINT);

		float startY = height * 0.26f;
		float line = font.getLineHeight() * 1.12f;
		for (int i = 0; i < ITEM_COUNT; i++) {
			float y = startY + i * line;
			boolean on = (i == selected);
			Color color = on ? Ui.SELECTED : Ui.HINT;
			String label = label(i);
			Ui.drawCentered(font, label, y, color);
			if (on) {
				Image cursor = GameImage.CURSOR.getImage();
				float textWidth = font.getWidth(label);
				cursor.draw(
					(width - textWidth) / 2f - cursor.getWidth() * 1.2f,
					y + (font.getLineHeight() - cursor.getHeight()) / 2f
				);
			}
		}

		Ui.drawCentered(small, I18n.hintMenu(), height * 0.90f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_DOWN:
			selected = (selected + 1) % ITEM_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			selected = (selected + ITEM_COUNT - 1) % ITEM_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate(selected);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		int index = hitIndex(y);
		if (index < 0)
			return;
		if (index != selected) {
			selected = index;
			AudioController.playCursor();
			return;
		}
		activate(index);
	}

	/**
	 * Activates a menu entry.
	 */
	private void activate(int index) {
		AudioController.Effect.SELECT.play();
		if (index == 0)
			game.showDeckSelect();
		else if (index == 1)
			game.showChampionship();
		else if (index == 2)
			game.showMyDeck();
		else if (index == 3)
			game.showSave();
		else if (index == 4)
			game.showHowToPlay();
		else if (index == 5)
			game.showSettings();
	}

	private String label(int index) {
		switch (index) {
			case 0: return I18n.menuQuick();
			case 1: return I18n.menuChampionship();
			case 2: return I18n.menuMyDeck();
			case 3: return I18n.menuSave();
			case 4: return I18n.menuHowTo();
			case 5: return I18n.menuSettings();
			default: return "";
		}
	}

	/**
	 * Returns the menu index at a y coordinate, or -1.
	 */
	private int hitIndex(int y) {
		UnicodeFont font = Options.getFont();
		float startY = Options.getHeight() * 0.26f;
		float line = font.getLineHeight() * 1.12f;
		for (int i = 0; i < ITEM_COUNT; i++) {
			float top = startY + i * line;
			if (y >= top && y < top + font.getLineHeight())
				return i;
		}
		return -1;
	}
}
