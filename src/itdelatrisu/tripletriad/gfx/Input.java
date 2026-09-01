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

package itdelatrisu.tripletriad.gfx;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * Keyboard and mouse constants plus key-repeat state (Slick-compatible).
 */
public class Input {
	public static final int KEY_ESCAPE = Keys.ESCAPE;
	public static final int KEY_ENTER = Keys.ENTER;
	public static final int KEY_BACK = Keys.BACKSPACE;
	public static final int KEY_DELETE = Keys.FORWARD_DEL;
	public static final int KEY_UP = Keys.UP;
	public static final int KEY_DOWN = Keys.DOWN;
	public static final int KEY_LEFT = Keys.LEFT;
	public static final int KEY_RIGHT = Keys.RIGHT;
	public static final int KEY_Z = Keys.Z;
	public static final int KEY_X = Keys.X;
	public static final int KEY_C = Keys.C;
	public static final int KEY_E = Keys.E;
	public static final int KEY_S = Keys.S;
	public static final int KEY_R = Keys.R;
	public static final int KEY_T = Keys.T;
	public static final int KEY_F1 = Keys.F1;
	public static final int KEY_F5 = Keys.F5;
	public static final int MOUSE_LEFT_BUTTON = Buttons.LEFT;

	/** Delay before key repeat starts, in milliseconds. */
	private static final int REPEAT_START_MS = 400;

	/** Interval between repeats, in milliseconds. */
	private static final int REPEAT_INTERVAL_MS = 50;

	/** Whether key repeat is enabled. */
	private boolean keyRepeat;

	/** Currently held key, or -1. */
	private int heldKey = -1;

	/** Character associated with the held key. */
	private char heldChar;

	/** Time the key has been held. */
	private int heldTime;

	/** Accumulator after the initial delay. */
	private int repeatAccum;

	/** Game that receives repeated presses. */
	private BasicGame game;

	/**
	 * Enables Slick-style key repeat.
	 */
	public void enableKeyRepeat() { keyRepeat = true; }

	/**
	 * Binds the game that receives repeat events.
	 * @param game the game
	 */
	void setGame(BasicGame game) { this.game = game; }

	/**
	 * Records a key press for repeat tracking.
	 * @param key the key
	 * @param c the character
	 */
	void onKeyDown(int key, char c) {
		heldKey = key;
		heldChar = c;
		heldTime = 0;
		repeatAccum = 0;
	}

	/**
	 * Records a key release.
	 * @param key the key
	 */
	void onKeyUp(int key) {
		if (heldKey == key)
			heldKey = -1;
	}

	/**
	 * Fires repeated keyPressed events.
	 * @param delta milliseconds since last frame
	 */
	void updateRepeat(int delta) {
		if (!keyRepeat || heldKey < 0 || game == null)
			return;
		heldTime += delta;
		if (heldTime < REPEAT_START_MS)
			return;
		repeatAccum += delta;
		while (repeatAccum >= REPEAT_INTERVAL_MS) {
			repeatAccum -= REPEAT_INTERVAL_MS;
			game.keyPressed(heldKey, heldChar);
		}
	}
}
