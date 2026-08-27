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
import itdelatrisu.tripletriad.Lang;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.ProfileStore;
import itdelatrisu.tripletriad.TripleTriad;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Toggle music, cursor sound, language, AI difficulty, and player profiles.
 */
public class SettingsScreen extends Screen {
	/** Music toggle row. */
	private static final int ROW_MUSIC = 0;

	/** Cursor sound toggle row. */
	private static final int ROW_CURSOR = 1;

	/** Language row. */
	private static final int ROW_LANG = 2;

	/** Opponent AI difficulty row. */
	private static final int ROW_AI = 3;

	/** Player name row. */
	private static final int ROW_NAME = 4;

	/** Profiles list row. */
	private static final int ROW_PROFILES = 5;

	/** Number of rows. */
	private static final int ROW_COUNT = 6;

	/** Easy difficulty index. */
	private static final int DIFF_EASY = 0;

	/** Normal difficulty index. */
	private static final int DIFF_NORMAL = 1;

	/** Hard difficulty index. */
	private static final int DIFF_HARD = 2;

	/** Game instance. */
	private final TripleTriad game;

	/** Selected row. */
	private int selected;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public SettingsScreen(TripleTriad game) {
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
		int width = container.getWidth();
		int height = container.getHeight();

		Ui.drawCentered(font, I18n.settingsTitle(), height * 0.08f, Ui.TITLE);

		float panelW = width * 0.62f;
		float panelH = height * 0.70f;
		float panelX = (width - panelW) / 2f;
		float panelY = height * 0.18f;
		g.setColor(Ui.TITLE);
		g.setLineWidth(2f);
		g.drawRect(panelX, panelY, panelW, panelH);
		g.setLineWidth(1f);

		float ruleLine = small.getLineHeight() * 1.85f;
		float ruleStart = panelY + small.getLineHeight() * 1.6f;
		for (int i = 0; i < ROW_COUNT; i++) {
			float y = ruleStart + i * ruleLine;
			boolean on = (i == selected);
			String name = rowName(i);
			String value = rowValue(i);
			Color nameColor = on ? Ui.SELECTED : Ui.HINT;
			Color valueColor = on ? Ui.SELECTED : rowValueColor(i);
			small.drawString(panelX + 48, y, name, nameColor);
			float valueX = panelX + panelW - 24 - small.getWidth(value);
			small.drawString(valueX, y, value, valueColor);
			if (on) {
				Image cursor = GameImage.CURSOR.getImage();
				cursor.draw(
					panelX + 48 - cursor.getWidth() * 1.15f,
					y + (small.getLineHeight() - cursor.getHeight()) / 2f
				);
			}
		}

		Ui.drawCentered(small, I18n.hintEscBack(), height * 0.92f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_DOWN:
			selected = (selected + 1) % ROW_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			selected = (selected + ROW_COUNT - 1) % ROW_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_LEFT:
			if (selected == ROW_LANG)
				cycleLanguage(false);
			else if (selected == ROW_AI)
				cycleDifficulty(false);
			break;
		case Input.KEY_RIGHT:
			if (selected == ROW_LANG)
				cycleLanguage(true);
			else if (selected == ROW_AI)
				cycleDifficulty(true);
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate();
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		int row = hitRow(x, y);
		if (row < 0)
			return;
		if (row != selected) {
			selected = row;
			AudioController.playCursor();
			return;
		}
		activate();
	}

	private void activate() {
		if (selected == ROW_MUSIC) {
			Options.toggleMusicEnabled();
			AudioController.applyMusic();
			Options.saveOptions();
			AudioController.Effect.SELECT.play();
		} else if (selected == ROW_CURSOR) {
			Options.toggleCursorSound();
			Options.saveOptions();
			AudioController.playCursor();
		} else if (selected == ROW_LANG) {
			cycleLanguage(true);
		} else if (selected == ROW_AI) {
			cycleDifficulty(true);
		} else if (selected == ROW_NAME) {
			AudioController.Effect.SELECT.play();
			game.showRenameProfile();
		} else if (selected == ROW_PROFILES) {
			AudioController.Effect.SELECT.play();
			game.showProfiles();
		}
	}

	private void cycleLanguage(boolean forward) {
		Lang next = forward ? Options.getLang().next() : Options.getLang().prev();
		Options.setLang(next);
		Options.saveOptions();
		AudioController.Effect.SELECT.play();
	}

	private void cycleDifficulty(boolean forward) {
		int i = difficultyIndex();
		i = forward ? (i + 1) % 3 : (i + 2) % 3;
		Options.AIType type;
		if (i == DIFF_EASY)
			type = Options.AIType.RANDOM;
		else if (i == DIFF_NORMAL)
			type = Options.AIType.BALANCED;
		else
			type = Options.AIType.OFFENSIVE;
		Options.setOpponentAI(type);
		Options.saveOptions();
		AudioController.Effect.SELECT.play();
	}

	private int difficultyIndex() {
		switch (Options.getOpponentAI()) {
		case RANDOM:
			return DIFF_EASY;
		case OFFENSIVE:
			return DIFF_HARD;
		default:
			return DIFF_NORMAL;
		}
	}

	private String difficultyLabel() {
		switch (difficultyIndex()) {
		case DIFF_EASY:
			return I18n.difficultyEasy();
		case DIFF_HARD:
			return I18n.difficultyHard();
		default:
			return I18n.difficultyNormal();
		}
	}

	private String rowName(int row) {
		if (row == ROW_MUSIC)
			return I18n.settingsMusic();
		if (row == ROW_CURSOR)
			return I18n.settingsCursor();
		if (row == ROW_LANG)
			return I18n.settingsLanguage();
		if (row == ROW_AI)
			return I18n.settingsDifficulty();
		if (row == ROW_NAME)
			return I18n.settingsPlayerName();
		return I18n.settingsProfiles();
	}

	private String rowValue(int row) {
		if (row == ROW_MUSIC)
			return Options.isMusicEnabled() ? I18n.on() : I18n.off();
		if (row == ROW_CURSOR)
			return Options.isCursorSoundEnabled() ? I18n.on() : I18n.off();
		if (row == ROW_LANG)
			return I18n.languageName(Options.getLang());
		if (row == ROW_AI)
			return difficultyLabel();
		if (row == ROW_NAME) {
			Profile profile = game.getProfile();
			return (profile != null && profile.isValid()) ? profile.getName() : "";
		}
		return I18n.profilesCount(ProfileStore.list().size());
	}

	private Color rowValueColor(int row) {
		if (row == ROW_MUSIC)
			return Options.isMusicEnabled() ? Ui.TITLE : Ui.DISABLED;
		if (row == ROW_CURSOR)
			return Options.isCursorSoundEnabled() ? Ui.TITLE : Ui.DISABLED;
		return Ui.TITLE;
	}

	private int hitRow(int x, int y) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		float panelW = width * 0.62f;
		float panelX = (width - panelW) / 2f;
		float panelY = height * 0.18f;
		if (x < panelX || x > panelX + panelW)
			return -1;
		UnicodeFont small = Options.getSmallFont();
		float ruleLine = small.getLineHeight() * 1.85f;
		float ruleStart = panelY + small.getLineHeight() * 1.6f;
		for (int i = 0; i < ROW_COUNT; i++) {
			float top = ruleStart + i * ruleLine;
			if (y >= top && y < top + ruleLine)
				return i;
		}
		return -1;
	}
}
