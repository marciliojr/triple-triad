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

	/** Chosen avatar [1, 8]. */
	private int avatar = 1;

	/** True if a MODE_CREATE was opened from the profile selection screen. */
	private boolean fromSelect;

	/** On-screen keyboard (joystick). */
	private final VirtualKeyboard vk = new VirtualKeyboard();

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
		avatar = 1;
		vk.autoOpen();
	}

	/**
	 * Rename the active profile. Esc returns to settings.
	 * @param current the current name
	 */
	public void enterRename(String current) {
		enterRename(current, 1);
	}

	/**
	 * Edit name and avatar of the active profile.
	 * @param current the current name
	 * @param currentAvatar the current avatar
	 */
	public void enterRename(String current, int currentAvatar) {
		mode = MODE_RENAME;
		name.setLength(0);
		if (current != null)
			name.append(current);
		avatar = currentAvatar;
		vk.autoOpen();
	}

	/**
	 * Create an extra profile. Esc returns to the profile list.
	 */
	public void enterCreate() {
		enterCreate(false);
	}

	/**
	 * Create an extra profile.
	 * @param fromSelect true to return to the profile selection on Esc
	 */
	public void enterCreate(boolean fromSelect) {
		mode = MODE_CREATE;
		name.setLength(0);
		avatar = 1;
		this.fromSelect = fromSelect;
		vk.autoOpen();
	}

	/** @return the chosen avatar */
	public int getAvatar() { return avatar; }

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

		Ui.drawCentered(font, "TRIPLE TRIAD", height * 0.08f, Ui.TITLE);
		Ui.drawCentered(small, prompt(), height * 0.18f, Ui.HINT);
		Ui.drawCentered(small, I18n.profileName(), height * 0.26f, Ui.HINT);

		String shown = name.length() == 0 ? "_" : name.toString() + "_";
		Ui.drawCentered(font, shown, height * 0.31f, Ui.SELECTED);

		Ui.drawCentered(small, I18n.profileAvatar(), height * 0.44f, Ui.HINT);
		float[] box = avatarBox(0);
		for (int i = 0; i < itdelatrisu.tripletriad.Profile.AVATARS; i++) {
			box = avatarBox(i);
			boolean on = (i + 1 == avatar);
			if (on)
				Ps1.panel(g, box[0] - 4, box[1] - 4, box[2] + 8, box[3] + 8);
			Avatars.draw(i + 1, box[0], box[1], box[2], box[3]);
			if (!on) {
				g.setColor(new itdelatrisu.tripletriad.gfx.Color(0f, 0f, 0f, 0.45f));
				g.fillRect(box[0], box[1], box[2], box[3]);
			}
		}

		if (vk.isActive()) {
			vk.render(g, height * 0.68f, height * 0.99f);
			return;
		}
		HintBar.draw(mode == MODE_FIRST ? I18n.hintProfile() : I18n.hintProfileEdit(), height * 0.82f);
	}

	@Override
	public void keyPressed(int key, char c) {
		boolean onScreen = vk.isActive();
		switch (vk.handle(key, c)) {
		case VirtualKeyboard.PASS: break;
		case VirtualKeyboard.CHAR: key = 0; c = vk.typed(); break;
		case VirtualKeyboard.DELETE: key = Input.KEY_BACK; break;
		case VirtualKeyboard.OK: key = Input.KEY_ENTER; break;
		case VirtualKeyboard.CANCEL: key = Input.KEY_ESCAPE; break;
		default:
			// Select cycles the avatar while the keyboard uses the D-pad
			if (onScreen && itdelatrisu.tripletriad.Controls.is(key, itdelatrisu.tripletriad.Controls.Action.SELECT)) {
				avatar = (avatar % itdelatrisu.tripletriad.Profile.AVATARS) + 1;
				AudioController.playCursor();
			}
			return;
		}
		// typed text first, so a letter bound to an action still types
		if (Ui.isNameChar(c) && c != ' ' || (c == ' ' && key == 0)) {
			if (name.length() < MAX_NAME)
				name.append(c);
			return;
		}
		if (key == Input.KEY_ESCAPE) {
			if (mode == MODE_FIRST)
				return;
			AudioController.Effect.BACK.play();
			if (mode == MODE_RENAME)
				game.showSettings();
			else if (fromSelect)
				game.showProfileSelect();
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
		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
			int n = itdelatrisu.tripletriad.Profile.AVATARS;
			avatar = (key == Input.KEY_LEFT) ? ((avatar + n - 2) % n) + 1 : (avatar % n) + 1;
			AudioController.playCursor();
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

	/** @return x, y, w, h of avatar i (0-based) */
	private static float[] avatarBox(int i) {
		int n = itdelatrisu.tripletriad.Profile.AVATARS;
		float w = Options.getWidth() * 0.085f;
		float h = w * 1.25f;
		float gap = w * 0.18f;
		float total = n * w + (n - 1) * gap;
		float x0 = (Options.getWidth() - total) / 2f;
		return new float[] { x0 + i * (w + gap), Options.getHeight() * 0.50f, w, h };
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		for (int i = 0; i < itdelatrisu.tripletriad.Profile.AVATARS; i++) {
			float[] b = avatarBox(i);
			if (x >= b[0] && x < b[0] + b[2] && y >= b[1] && y < b[1] + b[3]) {
				if (avatar != i + 1) {
					avatar = i + 1;
					AudioController.playCursor();
				}
				return;
			}
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
