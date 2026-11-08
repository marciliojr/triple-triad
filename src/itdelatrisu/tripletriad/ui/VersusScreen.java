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
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.Controls;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Versus hand pick: Player A chooses 5 cards, then Player B, from the active
 * profile's album (each distinct card can be picked any number of times).
 */
public class VersusScreen extends Screen {
	private static final int HAND = 5;

	private final TripleTriad game;

	/** Distinct card IDs of the album. */
	private final ArrayList<Integer> cards = new ArrayList<Integer>();

	/** Current hand being built. */
	private final ArrayList<Integer> hand = new ArrayList<Integer>();

	/** Player A's confirmed hand (null while A picks). */
	private int[] handA;

	private int cursor;
	private int scroll;

	public VersusScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		cards.clear();
		hand.clear();
		handA = null;
		cursor = 0;
		scroll = 0;
		Profile p = game.getProfile();
		if (p != null) {
			for (Integer id : p.getCollection())
				if (id != null && !cards.contains(id))
					cards.add(id);
		}
		final Deck deck = game.getDeck();
		Collections.sort(cards, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				Card ca = deck.getCardById(a.intValue()), cb = deck.getCardById(b.intValue());
				int la = (ca != null) ? ca.getLevel() : 0, lb = (cb != null) ? cb.getLevel() : 0;
				return (la != lb) ? Integer.compare(la, lb) : Integer.compare(a.intValue(), b.intValue());
			}
		});
	}

	private boolean pickingA() { return handA == null; }

	private float cell() { return Math.max(48f, Options.getCardLength() / 3f); }

	private float gap() { return Math.max(4f, cell() / 12f); }

	private int cols() {
		return Math.max(1, (int) ((Options.getWidth() * 0.84f + gap()) / (cell() + gap())));
	}

	private float gridX() {
		int c = cols();
		return (Options.getWidth() - (c * cell() + (c - 1) * gap())) / 2f;
	}

	private float gridTop() { return Ps1.contentTop(); }

	private int visibleRows() {
		return Math.max(1, (int) ((Options.getHeight() * 0.66f - gridTop()) / (cell() + gap())));
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		String who = pickingA() ? I18n.versusPlayerA() : I18n.versusPlayerB();
		Ps1.frame(g, who + " - " + I18n.versusChoose(hand.size()), I18n.menuVersus(), I18n.versusHelp());
		UnicodeFont small = Options.getSmallFont();
		Deck deck = game.getDeck();
		if (cards.isEmpty()) {
			Ui.drawCentered(small, I18n.versusNoCards(), Options.getHeight() * 0.45f, Ui.HINT);
			Ps1.hint(I18n.hintEscBack());
			return;
		}
		int c = cols();
		float size = cell(), gp = gap();
		int rows = visibleRows();
		for (int r = 0; r < rows; r++) {
			for (int k = 0; k < c; k++) {
				int i = (scroll + r) * c + k;
				if (i >= cards.size())
					break;
				float x = gridX() + k * (size + gp), y = gridTop() + r * (size + gp);
				Card card = deck.getCardById(cards.get(i).intValue());
				if (card != null)
					card.drawSized(x, y, size, pickingA(), false);
				if (i == cursor) {
					g.setColor(Ui.TITLE);
					g.setLineWidth(2f);
					g.drawRect(x - 2, y - 2, size + 4, size + 4);
					g.setLineWidth(1f);
				}
			}
		}
		// hand row
		float hs = Options.getHeight() * 0.13f;
		float hgap = hs * 0.12f;
		float hx = (Options.getWidth() - (HAND * hs + (HAND - 1) * hgap)) / 2f;
		float hy = Options.getHeight() * 0.69f;
		for (int i = 0; i < HAND; i++) {
			float x = hx + i * (hs + hgap);
			if (i < hand.size()) {
				Card card = deck.getCardById(hand.get(i).intValue());
				if (card != null)
					card.drawSized(x, hy, hs, pickingA(), false);
			} else
				Ps1.innerPanel(g, x, hy, hs, hs);
		}
		Card cur = deck.getCardById(cards.get(cursor).intValue());
		if (cur != null)
			Ui.drawCentered(small, cur.getName(), hy + hs + small.getLineHeight() * 0.4f, Color.white);
		Ps1.hint(I18n.hintVersusPick());
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE && (Controls.lastSource() == Controls.SOURCE_KEYBOARD || hand.isEmpty())) {
			AudioController.Effect.BACK.play();
			game.showMenu();
			return;
		}
		if (cards.isEmpty())
			return;
		if (!Controls.allows(pickingA()))
			return;
		int cl = cols();
		switch (key) {
		case Input.KEY_LEFT:
			if (cursor > 0) { cursor--; AudioController.playCursor(); }
			break;
		case Input.KEY_RIGHT:
			if (cursor < cards.size() - 1) { cursor++; AudioController.playCursor(); }
			break;
		case Input.KEY_UP:
			if (cursor - cl >= 0) { cursor -= cl; AudioController.playCursor(); }
			break;
		case Input.KEY_DOWN:
			if (cursor + cl < cards.size()) { cursor += cl; AudioController.playCursor(); }
			break;
		case Input.KEY_Z:
			add(cards.get(cursor).intValue());
			break;
		case Input.KEY_X:
		case Input.KEY_BACK:
		case Input.KEY_DELETE:
		case Input.KEY_ESCAPE:
			if (!hand.isEmpty()) {
				hand.remove(hand.size() - 1);
				AudioController.Effect.BACK.play();
			}
			break;
		case Input.KEY_R:
			Random rnd = new Random();
			while (hand.size() < HAND)
				hand.add(cards.get(rnd.nextInt(cards.size())));
			AudioController.Effect.SELECT.play();
			break;
		case Input.KEY_ENTER:
			confirm();
			break;
		default:
			break;
		}
		clampScroll();
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON || cards.isEmpty() || !Controls.allows(pickingA()))
			return;
		int c = cols();
		float size = cell(), gp = gap();
		for (int r = 0; r < visibleRows(); r++) {
			for (int k = 0; k < c; k++) {
				int i = (scroll + r) * c + k;
				if (i >= cards.size())
					return;
				float cx = gridX() + k * (size + gp), cy = gridTop() + r * (size + gp);
				if (x >= cx && x < cx + size && y >= cy && y < cy + size) {
					if (i != cursor) {
						cursor = i;
						AudioController.playCursor();
					} else
						add(cards.get(i).intValue());
					return;
				}
			}
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		int c = cols();
		if (change < 0 && cursor + c < cards.size())
			cursor += c;
		else if (change > 0 && cursor - c >= 0)
			cursor -= c;
		clampScroll();
	}

	private void clampScroll() {
		int row = cursor / cols();
		int vis = visibleRows();
		if (row < scroll)
			scroll = row;
		else if (row >= scroll + vis)
			scroll = row - vis + 1;
	}

	private void add(int id) {
		if (hand.size() >= HAND) {
			confirm();
			return;
		}
		hand.add(Integer.valueOf(id));
		AudioController.Effect.SELECT.play();
	}

	private void confirm() {
		if (hand.size() < HAND) {
			AudioController.Effect.INVALID.play();
			return;
		}
		int[] ids = new int[HAND];
		for (int i = 0; i < HAND; i++)
			ids[i] = hand.get(i).intValue();
		AudioController.Effect.SELECT.play();
		hand.clear();
		if (pickingA()) {
			handA = ids;
			cursor = 0;
			scroll = 0;
			return;
		}
		game.startVersus(handA, ids);
	}
}
