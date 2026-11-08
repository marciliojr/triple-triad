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
import itdelatrisu.tripletriad.Controls;
import itdelatrisu.tripletriad.Controls.Action;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Remaps every action for keyboard (left column) and joystick (right column):
 * Confirm on a row, then "press the key / button...". Delete clears the
 * binding of the selected column. The last row restores the defaults.
 * The INFO box shows the detected controller and the footer a live button test.
 */
public class ControlsScreen extends Screen {
	private static final Action[] ACTIONS = Action.values();

	private final TripleTriad game;
	private int selected;
	private int scroll;

	/** 0 = keyboard column, 1 = joystick column. */
	private int column;

	/** Waiting for input (true between "press..." and the capture). */
	private boolean waiting;

	public ControlsScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		selected = 0;
		scroll = 0;
		column = 0;
		waiting = false;
	}

	private int count() { return ACTIONS.length + 1; }

	/** @return number of rows that fit above the live test line */
	private int visible() {
		float bottom = Options.getHeight() * 0.84f;
		int n = (int) ((bottom - Ps1.contentTop()) / (Ps1.rowHeight() + Ps1.gap()));
		return Math.max(3, Math.min(count(), n));
	}

	private void clampScroll() {
		int v = visible();
		if (selected < scroll)
			scroll = selected;
		if (selected >= scroll + v)
			scroll = selected - v + 1;
		scroll = Math.max(0, Math.min(scroll, count() - v));
	}

	private String info() {
		String name = Controls.controllerName();
		if (name != null)
			return I18n.controllerDetected(name, Controls.isGeneric());
		if (Controls.incompatibleMode())
			return I18n.controllerIncompatible();
		return I18n.controllerNone();
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		clampScroll();
		Ps1.frame(g, info(), I18n.settingsControls(), waiting ? I18n.controlsPress(column == 1) : I18n.controlsHelp());
		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight();
		float keyCol = Ps1.rowX() + Ps1.rowW() * 0.55f;
		float padCol = Ps1.rowX() + Ps1.rowW() * 0.78f;
		int v = visible();
		for (int r = 0; r < v; r++) {
			int i = scroll + r;
			if (i >= count())
				break;
			boolean on = (i == selected);
			if (i == ACTIONS.length) {
				Ps1.row(g, r, I18n.controlsReset(), null, on, true);
				continue;
			}
			Action a = ACTIONS[i];
			Ps1.row(g, r, I18n.controlAction(a), null, on, true);
			float ty = Ps1.rowY(r) + (Ps1.rowHeight() - line) / 2f;
			String k = (waiting && on && column == 0) ? "..." : Controls.keyName(Controls.getKey(a));
			String p = (waiting && on && column == 1) ? "..." : Controls.padName(a);
			small.drawString(keyCol, ty, k, (on && column == 0) ? Ui.TITLE : Color.white);
			small.drawString(padCol, ty, p, (on && column == 1) ? Ui.TITLE : Color.white);
		}
		// live button test
		String test = I18n.controllerTest(Controls.lastPadInput());
		Ui.drawCentered(small, Ui.fit(small, test, Options.getWidth() * 0.9f), Options.getHeight() * 0.865f,
			Controls.lastPadInput() != null ? Ui.TITLE : Ui.HINT);
		Ps1.hint(I18n.hintControls());
	}

	@Override
	public void keyPressed(int key, char c) {
		if (waiting || key == 0)
			return;
		if (Controls.is(key, Action.UP)) {
			selected = (selected + count() - 1) % count();
			AudioController.playCursor();
		} else if (Controls.is(key, Action.DOWN)) {
			selected = (selected + 1) % count();
			AudioController.playCursor();
		} else if (Controls.is(key, Action.LEFT) || Controls.is(key, Action.RIGHT)) {
			column = 1 - column;
			AudioController.playCursor();
		} else if (Controls.is(key, Action.CONFIRM) || Controls.is(key, Action.MENU)) {
			activate();
		} else if (Controls.is(key, Action.DELETE)) {
			clear();
		} else if (Controls.is(key, Action.CANCEL)) {
			AudioController.Effect.BACK.play();
			Options.saveOptions();
			game.showSettings();
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (waiting || button != Input.MOUSE_LEFT_BUTTON)
			return;
		int hit = Ps1.hitRow(x, y, visible());
		if (hit < 0 || scroll + hit >= count())
			return;
		hit += scroll;
		column = (x > Ps1.rowX() + Ps1.rowW() * 0.76f) ? 1 : 0;
		if (hit != selected) {
			selected = hit;
			AudioController.playCursor();
		} else
			activate();
	}

	@Override
	public void mouseWheelMoved(int change) {
		selected = Math.max(0, Math.min(count() - 1, selected + (change < 0 ? 1 : -1)));
	}

	/** Removes the binding of the selected column. */
	private void clear() {
		if (selected >= ACTIONS.length) {
			AudioController.Effect.INVALID.play();
			return;
		}
		Action a = ACTIONS[selected];
		if (column == 0)
			Controls.setKey(a, 0);
		else
			Controls.setPad(a, Controls.PAD_NONE);
		Options.saveOptions();
		AudioController.Effect.BACK.play();
	}

	private void activate() {
		if (selected == ACTIONS.length) {
			Controls.resetDefaults();
			Options.saveOptions();
			AudioController.Effect.SELECT.play();
			return;
		}
		final Action a = ACTIONS[selected];
		waiting = true;
		AudioController.Effect.SELECT.play();
		Controls.setCapture(new Controls.Capture() {
			@Override
			public void key(int keycode) {
				waiting = false;
				if (column == 1 || (keycode == com.badlogic.gdx.Input.Keys.ESCAPE && a != Action.CANCEL)) {
					// a key while waiting for a joystick button (or Esc) cancels
					AudioController.Effect.BACK.play();
					return;
				}
				Controls.setKey(a, keycode);
				Options.saveOptions();
				AudioController.Effect.SELECT.play();
			}

			@Override
			public void button(int b) {
				waiting = false;
				if (column != 1) {
					AudioController.Effect.BACK.play();
					return;
				}
				Controls.setPad(a, b);
				Options.saveOptions();
				AudioController.Effect.SELECT.play();
			}
		});
	}
}
