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
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;

/**
 * Trophies: persistent counters of the active profile (Versus does not count).
 */
public class TrophiesScreen extends Screen {
	private final TripleTriad game;

	public TrophiesScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Profile p = game.getProfile();
		String name = (p != null) ? p.getName() : "";
		Ps1.frame(g, name, I18n.menuTrophies(), I18n.trophiesHelp());
		if (p == null)
			return;
		int quickPlayed = p.getQuickWins() + p.getQuickLosses() + p.getQuickDraws();
		String[][] rows = {
			{ I18n.trophyCups(), String.valueOf(p.getChampionshipWins()) },
			{ I18n.trophyChampMatches(), p.getChampMatchWins() + " / " + p.getChampMatchLosses() },
			{ I18n.trophyQuickWins(), String.valueOf(p.getQuickWins()) },
			{ I18n.trophyQuickLosses(), String.valueOf(p.getQuickLosses()) },
			{ I18n.trophyQuickDraws(), String.valueOf(p.getQuickDraws()) },
			{ I18n.trophyQuickPlayed(), String.valueOf(quickPlayed) },
			{ I18n.trophyCards(), String.valueOf(p.getCollection().size()) },
		};
		float x = Ps1.rowX();
		float avatarW = Options.getWidth() * 0.075f;
		Avatars.draw(p.getAvatar(), x + Ps1.rowW() + Ps1.gap() * 2f,
			Ps1.rowY(0), avatarW, avatarW * 1.25f);
		for (int i = 0; i < rows.length; i++)
			Ps1.row(g, i, rows[i][0], rows[i][1], false, true);
		Ps1.hint(I18n.hintEscBack());
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE || key == Input.KEY_X || key == Input.KEY_BACK
				|| key == Input.KEY_Z || key == Input.KEY_ENTER) {
			AudioController.Effect.BACK.play();
			game.showMenu();
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		game.showMenu();
	}
}
