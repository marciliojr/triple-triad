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
import itdelatrisu.tripletriad.ProfileStore;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Switch, create or delete player profiles.
 */
public class ProfilesScreen extends Screen {
	/** Game instance. */
	private final TripleTriad game;

	/** Selected row (0 = new profile). */
	private int selected;

	/** First visible row. */
	private int scroll;

	/** Cached listing. */
	private ArrayList<ProfileStore.Entry> entries = new ArrayList<ProfileStore.Entry>();

	/**
	 * Constructor.
	 * @param game the game
	 */
	public ProfilesScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		refresh();
		selected = 0;
		scroll = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();

		Ui.drawCentered(font, I18n.profilesTitle(), height * 0.08f, Ui.TITLE);

		float listTop = height * 0.22f;
		float line = small.getLineHeight() * 2.2f;
		int visible = Math.max(3, (int) ((height * 0.82f - listTop) / line));
		clampScroll(visible);
		int total = rowCount();
		float x = Options.getWidth() * 0.18f;
		for (int row = scroll; row < total && row < scroll + visible; row++) {
			float y = listTop + (row - scroll) * line;
			boolean on = (row == selected);
			String label = rowLabel(row);
			Color color = on ? Ui.SELECTED : Ui.HINT;
			small.drawString(x, y, label, color);
			if (on) {
				Image cursor = GameImage.CURSOR.getImage();
				cursor.draw(
					x - cursor.getWidth() * 1.15f,
					y + (small.getLineHeight() - cursor.getHeight()) / 2f
				);
			}
		}

		Ui.drawCentered(small, I18n.hintProfiles(), height * 0.90f, Ui.HINT);
	}

	@Override
	public void keyPressed(int key, char c) {
		int total = rowCount();
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showSettings();
			break;
		case Input.KEY_DOWN:
			if (total > 0)
				selected = (selected + 1) % total;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			if (total > 0)
				selected = (selected + total - 1) % total;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate();
			break;
		case Input.KEY_DELETE:
			deleteSelected();
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

	@Override
	public void mouseWheelMoved(int change) {
		int total = rowCount();
		if (total <= 0)
			return;
		if (change < 0)
			selected = Math.min(total - 1, selected + 1);
		else if (change > 0)
			selected = Math.max(0, selected - 1);
	}

	private void activate() {
		if (selected == 0) {
			AudioController.Effect.SELECT.play();
			game.showCreateProfile();
			return;
		}
		ProfileStore.Entry entry = entryAt(selected);
		if (entry == null) {
			AudioController.Effect.INVALID.play();
			return;
		}
		if (entry.getId() == game.getProfileId()) {
			AudioController.Effect.SELECT.play();
			game.showMenu();
			return;
		}
		if (!game.switchProfile(entry.getId())) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
	}

	private void deleteSelected() {
		ProfileStore.Entry entry = entryAt(selected);
		if (entry == null || !game.canDeleteProfile(entry.getId())) {
			AudioController.Effect.INVALID.play();
			return;
		}
		if (!game.deleteProfile(entry.getId())) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.BACK.play();
		refresh();
		if (selected >= rowCount())
			selected = rowCount() - 1;
	}

	private void refresh() {
		entries = ProfileStore.list();
	}

	private int rowCount() {
		return 1 + entries.size();
	}

	private String rowLabel(int row) {
		if (row == 0)
			return I18n.newProfile();
		ProfileStore.Entry entry = entryAt(row);
		if (entry == null)
			return "";
		if (entry.getId() == game.getProfileId())
			return entry.getName() + "  (" + I18n.profileActive() + ")";
		return entry.getName();
	}

	private ProfileStore.Entry entryAt(int row) {
		int index = row - 1;
		if (index < 0 || index >= entries.size())
			return null;
		return entries.get(index);
	}

	private float listTop() {
		return Options.getHeight() * 0.22f;
	}

	private float lineHeight() {
		return Options.getSmallFont().getLineHeight() * 2.2f;
	}

	private int visibleRows() {
		float startY = listTop();
		return Math.max(3, (int) ((Options.getHeight() * 0.82f - startY) / lineHeight()));
	}

	private int hitRow(int x, int y) {
		float startY = listTop();
		float line = lineHeight();
		int visible = visibleRows();
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
}
