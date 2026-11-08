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

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.ProfileStore;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Profile selection shown after the intro, as memory card blocks:
 * avatar, name, cards and trophies, plus a "new profile" block.
 */
public class ProfileSelectScreen extends Screen {
	private static final int PER_PAGE = 3;

	private final TripleTriad game;
	private final ArrayList<Integer> ids = new ArrayList<Integer>();
	private final ArrayList<Profile> profiles = new ArrayList<Profile>();
	private int selected;

	public ProfileSelectScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		ids.clear();
		profiles.clear();
		for (ProfileStore.Entry e : ProfileStore.list()) {
			Profile p = ProfileStore.load(e.getId());
			if (p == null || !p.isValid())
				continue;
			ids.add(Integer.valueOf(e.getId()));
			profiles.add(p);
		}
		selected = 0;
		int active = game.getProfileId();
		for (int i = 0; i < ids.size(); i++)
			if (ids.get(i).intValue() == active)
				selected = i;
	}

	private int count() { return profiles.size() + 1; }

	private float blockH() {
		return (Options.getHeight() * 0.88f - Ps1.contentTop() - Ps1.gap() * (PER_PAGE - 1)) / PER_PAGE;
	}

	private float blockY(int row) { return Ps1.contentTop() + row * (blockH() + Ps1.gap()); }

	private float blockX() { return Options.getWidth() * 0.08f; }

	private float blockW() { return Options.getWidth() * 0.84f; }

	@Override
	public void render(GameContainer container, Graphics g) {
		Ps1.frame(g, I18n.memoryCardInUse(), I18n.profileSelectTitle(), I18n.profileSelectHelp());
		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight();
		int first = (selected / PER_PAGE) * PER_PAGE;
		for (int row = 0; row < PER_PAGE; row++) {
			int i = first + row;
			if (i >= count())
				break;
			float x = blockX(), y = blockY(row), w = blockW(), h = blockH();
			Ps1.panel(g, x, y, w, h);
			small.drawString(x + line * 0.25f, y + line * 0.1f, String.format("%02d", i + 1), Color.white);
			if (i == profiles.size()) {
				small.drawString(x + line * 1.6f, y + (h - line) / 2f, I18n.profileSelectNew(), Color.white);
			} else {
				Profile p = profiles.get(i);
				float pad = h * 0.08f;
				float ah = h - pad * 2f, aw = ah * 0.8f;
				float ax = x + small.getWidth("00") + line * 0.6f;
				Avatars.draw(p.getAvatar(), ax, y + pad, aw, ah);
				float tx = ax + aw + line * 0.8f;
				float right = x + w - line * 0.5f;
				small.drawString(tx, y + pad, p.getName(), Color.white);
				String cards = I18n.championshipCards(p.getCollection().size());
				small.drawString(right - small.getWidth(cards), y + pad, cards, Color.white);
				String cups = I18n.championshipCups(p.getChampionshipWins());
				small.drawString(tx, y + pad + line * 1.1f, cups, Color.white);
				String quick = I18n.trophyQuickShort(p.getQuickWins());
				small.drawString(right - small.getWidth(quick), y + pad + line * 1.1f, quick, Color.white);
				if (ids.get(i).intValue() == game.getProfileId()) {
					String act = I18n.profileSelectActive();
					float iy = y + h - pad - line * 1.35f;
					if (iy > y + pad + line * 2.1f) {
						Ps1.innerPanel(g, tx - line * 0.3f, iy, right - tx + line * 0.3f, line * 1.35f);
						small.drawString(tx + line * 0.2f, iy + line * 0.17f, act, Color.white);
					}
				}
			}
			if (i == selected)
				Ps1.hand(x, y + line * 0.1f, line);
		}
		int pages = (count() + PER_PAGE - 1) / PER_PAGE;
		String pageText = (first / PER_PAGE + 1) + "/" + pages;
		small.drawString(Options.getWidth() - Ps1.margin() - small.getWidth(pageText), Options.getHeight() * 0.92f, pageText, Ui.DISABLED);
		Ps1.hint(I18n.hintProfileSelect());
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_UP:
			selected = (selected + count() - 1) % count();
			AudioController.playCursor();
			break;
		case Input.KEY_DOWN:
			selected = (selected + 1) % count();
			AudioController.playCursor();
			break;
		case Input.KEY_LEFT:
			if (selected >= PER_PAGE) {
				selected -= PER_PAGE;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_RIGHT:
			if (selected + PER_PAGE < count()) {
				selected += PER_PAGE;
				AudioController.playCursor();
			} else if ((selected / PER_PAGE) < (count() - 1) / PER_PAGE) {
				selected = count() - 1;
				AudioController.playCursor();
			}
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
		if (button != Input.MOUSE_LEFT_BUTTON || x < blockX() || x > blockX() + blockW())
			return;
		int first = (selected / PER_PAGE) * PER_PAGE;
		for (int row = 0; row < PER_PAGE; row++) {
			int i = first + row;
			if (i < count() && y >= blockY(row) && y < blockY(row) + blockH()) {
				if (i != selected) {
					selected = i;
					AudioController.playCursor();
				} else
					activate();
				return;
			}
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (change < 0 && selected < count() - 1)
			selected++;
		else if (change > 0 && selected > 0)
			selected--;
	}

	private void activate() {
		AudioController.Effect.SELECT.play();
		if (selected == profiles.size()) {
			if (profiles.isEmpty())
				game.showProfile();
			else
				game.showCreateProfileFromSelect();
			return;
		}
		int id = ids.get(selected).intValue();
		if (id == game.getProfileId())
			game.showMenu();
		else if (!game.switchProfile(id))
			AudioController.Effect.INVALID.play();
	}
}
