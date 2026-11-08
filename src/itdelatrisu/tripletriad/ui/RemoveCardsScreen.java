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
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;

/**
 * "Remove all cards" from Meu Deck: everything or one category, with Yes/No.
 * Categories by card level: 1-5 monsters, 6-7 bosses, 8-9 GFs, 10 characters.
 */
public class RemoveCardsScreen extends Screen {
	/** Level ranges per row: all, monsters, bosses, GFs, characters. */
	private static final int[][] RANGES = { {1, 10}, {1, 5}, {6, 7}, {8, 9}, {10, 10} };

	private final TripleTriad game;
	private int selected;
	private boolean confirm;
	private int confirmChoice;
	private String notice;
	private long noticeUntil;

	public RemoveCardsScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		selected = 0;
		confirm = false;
		notice = null;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ps1.frame(g, I18n.removeAllInfo(), I18n.removeAllTitle(), I18n.removeAllHelp());
		for (int i = 0; i < RANGES.length; i++) {
			int n = count(i);
			Ps1.row(g, i, I18n.removeCategory(i), I18n.championshipCards(n), i == selected, n > 0);
		}
		if (notice != null && System.currentTimeMillis() < noticeUntil)
			Ui.drawCentered(itdelatrisu.tripletriad.Options.getSmallFont(), notice,
				container.getHeight() * 0.86f, Ui.TITLE);
		if (confirm)
			Ui.drawYesNoConfirm(g, I18n.removeAllConfirm(I18n.removeCategory(selected), count(selected)), confirmChoice);
		else
			Ps1.hint(I18n.hintRemoveAll());
	}

	private boolean inRange(int cardId, int row) {
		Card card = game.getDeck().getCardById(cardId);
		if (card == null)
			return row == 0;
		return card.getLevel() >= RANGES[row][0] && card.getLevel() <= RANGES[row][1];
	}

	private int count(int row) {
		Profile p = game.getProfile();
		if (p == null)
			return 0;
		int n = 0;
		for (Integer id : p.getCollection())
			if (id != null && inRange(id.intValue(), row))
				n++;
		return n;
	}

	@Override
	public void keyPressed(int key, char c) {
		if (confirm) {
			switch (key) {
			case Input.KEY_UP:
			case Input.KEY_DOWN:
				confirmChoice = 1 - confirmChoice;
				AudioController.playCursor();
				break;
			case Input.KEY_Z:
			case Input.KEY_ENTER:
				resolve(confirmChoice == 1);
				break;
			case Input.KEY_ESCAPE:
			case Input.KEY_X:
			case Input.KEY_BACK:
				resolve(false);
				break;
			default:
				break;
			}
			return;
		}
		switch (key) {
		case Input.KEY_UP:
			selected = (selected + RANGES.length - 1) % RANGES.length;
			AudioController.playCursor();
			break;
		case Input.KEY_DOWN:
			selected = (selected + 1) % RANGES.length;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			ask();
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			AudioController.Effect.BACK.play();
			game.showMyDeck();
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		if (confirm) {
			int hit = Ui.hitYesNoConfirm(y);
			if (hit >= 0)
				resolve(hit == 1);
			return;
		}
		int hit = Ps1.hitRow(x, y, RANGES.length);
		if (hit < 0)
			return;
		if (hit != selected) {
			selected = hit;
			AudioController.playCursor();
		} else
			ask();
	}

	private void ask() {
		if (count(selected) == 0) {
			AudioController.Effect.INVALID.play();
			return;
		}
		confirm = true;
		confirmChoice = 0;
		AudioController.Effect.SELECT.play();
	}

	private void resolve(boolean yes) {
		confirm = false;
		if (!yes) {
			AudioController.Effect.BACK.play();
			return;
		}
		Profile p = game.getProfile();
		ArrayList<Integer> keep = new ArrayList<Integer>();
		int removed = 0;
		for (Integer id : p.getCollection()) {
			if (id != null && inRange(id.intValue(), selected))
				removed++;
			else
				keep.add(id);
		}
		p.replaceCollection(keep);
		game.saveProfile();
		AudioController.Effect.SELECT.play();
		notice = I18n.removeAllDone(removed);
		noticeUntil = System.currentTimeMillis() + 2500L;
	}
}
