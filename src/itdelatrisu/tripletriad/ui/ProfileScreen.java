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

import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Create or rename a player profile.
 */
public class ProfileScreen extends Screen {
	/** First-run mandatory create (Esc quits the game). */
	public static final int MODE_FIRST = 0;

	/** Rename the active profile. */
	public static final int MODE_RENAME = 1;

	/** Create an additional profile. */
	public static final int MODE_CREATE = 2;

	/** Maximum name length. */
	private static final int MAX_NAME = 16;

	/** Game instance. */
	private final TripleTriad game;

	/** Name being typed. */
	private final StringBuilder name = new StringBuilder();

	/** Current mode. */
	private int mode = MODE_FIRST;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public ProfileScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		enterFirst();
	}

	/**
	 * First-run create. Esc quits the game.
	 */
	public void enterFirst() {
		mode = MODE_FIRST;
		name.setLength(0);
	}

	/**
	 * Rename the active profile. Esc returns to settings.
	 * @param current the current name
	 */
	public void enterRename(String current) {
		mode = MODE_RENAME;
		name.setLength(0);
		if (current != null)
			name.append(current);
	}

	/**
	 * Create an extra profile. Esc returns to the profile list.
	 */
	public void enterCreate() {
		mode = MODE_CREATE;
		name.setLength(0);
	}

	/**
	 * True if Esc should exit the application.
	 * @return true on first-run create
	 */
	public boolean quitsOnEscape() {
		return mode == MODE_FIRST;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();

		Ui.drawCentered(font, "TRIPLE TRIAD", height * 0.18f, Ui.TITLE);
		Ui.drawCentered(small, prompt(), height * 0.32f, Ui.HINT);
		Ui.drawCentered(small, I18n.profileName(), height * 0.46f, Ui.HINT);

		String shown = name.length() == 0 ? "_" : name.toString() + "_";
		Ui.drawCentered(font, shown, height * 0.52f, Ui.SELECTED);

		Ui.drawCentered(small, mode == MODE_FIRST ? I18n.hintProfile() : I18n.hintProfileEdit(),
			height * 0.82f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			if (mode == MODE_FIRST)
				return;
			AudioController.Effect.BACK.play();
			if (mode == MODE_RENAME)
				game.showSettings();
			else
				game.showProfiles();
			return;
		}
		if (key == Input.KEY_ENTER || key == Input.KEY_Z) {
			String trimmed = name.toString().trim();
			if (trimmed.isEmpty()) {
				AudioController.Effect.INVALID.play();
				return;
			}
			boolean ok;
			if (mode == MODE_RENAME)
				ok = game.renameProfile(trimmed);
			else if (mode == MODE_CREATE)
				ok = game.createAdditionalProfile(trimmed);
			else
				ok = game.createProfile(trimmed);
			if (!ok) {
				AudioController.Effect.INVALID.play();
				return;
			}
			AudioController.Effect.SELECT.play();
			return;
		}
		if (key == Input.KEY_BACK && name.length() > 0) {
			name.deleteCharAt(name.length() - 1);
			AudioController.Effect.BACK.play();
			return;
		}
		if (Ui.isNameChar(c) && name.length() < MAX_NAME) {
			name.append(c);
		}
	}

	private String prompt() {
		if (mode == MODE_RENAME)
			return I18n.profileRenamePrompt();
		if (mode == MODE_CREATE)
			return I18n.profileCreatePrompt();
		return I18n.profilePrompt();
	}
}
