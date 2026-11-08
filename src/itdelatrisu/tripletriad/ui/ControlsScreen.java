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
 * Remaps actions for keyboard (Z/Enter on the row) and joystick (right on the
 * row, then Z/Enter): "press the key / button...". Last row restores defaults.
 */
public class ControlsScreen extends Screen {
	private static final Action[] ACTIONS = Action.values();

	private final TripleTriad game;
	private int selected;

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
		column = 0;
		waiting = false;
	}

	private int count() { return ACTIONS.length + 1; }

	@Override
	public void render(GameContainer container, Graphics g) {
		String info = I18n.controllersCount(Controls.controllerCount());
		Ps1.frame(g, info, I18n.settingsControls(), waiting ? I18n.controlsPress(column == 1) : I18n.controlsHelp());
		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight();
		float keyCol = Ps1.rowX() + Ps1.rowW() * 0.55f;
		float padCol = Ps1.rowX() + Ps1.rowW() * 0.80f;
		for (int i = 0; i < ACTIONS.length; i++) {
			Action a = ACTIONS[i];
			boolean on = (i == selected);
			Ps1.row(g, i, I18n.controlAction(a), null, on, true);
			float ty = Ps1.rowY(i) + (Ps1.rowHeight() - line) / 2f;
			String k = (waiting && on && column == 0) ? "..." : Controls.keyName(Controls.getKey(a));
			String p = (waiting && on && column == 1) ? "..." : Controls.padName(a);
			small.drawString(keyCol, ty, k, (on && column == 0) ? Ui.TITLE : Color.white);
			small.drawString(padCol, ty, p, (on && column == 1) ? Ui.TITLE : Color.white);
		}
		Ps1.row(g, ACTIONS.length, I18n.controlsReset(), null, selected == ACTIONS.length, true);
		Ps1.hint(I18n.hintControls());
	}

	@Override
	public void keyPressed(int key, char c) {
		if (waiting)
			return;
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
		case Input.KEY_RIGHT:
			column = 1 - column;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate();
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			AudioController.Effect.BACK.play();
			Options.saveOptions();
			game.showSettings();
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (waiting || button != Input.MOUSE_LEFT_BUTTON)
			return;
		int hit = Ps1.hitRow(x, y, count());
		if (hit < 0)
			return;
		column = (x > Ps1.rowX() + Ps1.rowW() * 0.78f) ? 1 : 0;
		if (hit != selected) {
			selected = hit;
			AudioController.playCursor();
		} else
			activate();
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
				if (keycode == Input.KEY_ESCAPE && a != Action.CANCEL) {
					AudioController.Effect.BACK.play();
					return;
				}
				if (column == 0)
					Controls.setKey(a, keycode);
				Options.saveOptions();
				AudioController.Effect.SELECT.play();
			}

			@Override
			public void button(int b) {
				waiting = false;
				if (column == 1)
					Controls.setPad(a, b);
				Options.saveOptions();
				AudioController.Effect.SELECT.play();
			}
		});
	}
}
