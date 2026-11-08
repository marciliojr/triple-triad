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

package itdelatrisu.tripletriad;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;

import itdelatrisu.tripletriad.gfx.Gfx;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.Log;

/**
 * Keyboard remapping and joystick support.
 * <p>
 * Every action is translated into the "canonical" key the screens already
 * understand (Z confirm, Esc cancel, Enter menu, Del delete, S skip,
 * Left/Right page), so all screens work with a gamepad without changes.
 * D-pad and left stick send the arrow keys. Bindings are saved in the cfg
 * ({@code BIND_KEY_<ACTION>} / {@code BIND_PAD_<ACTION>}).
 */
public final class Controls {
	/** Remappable actions. */
	public enum Action {
		CONFIRM(Input.KEY_Z),
		CANCEL(Input.KEY_ESCAPE),
		MENU(Input.KEY_ENTER),
		DELETE(Input.KEY_DELETE),
		SKIP(Input.KEY_S),
		PAGE_LEFT(Input.KEY_LEFT),
		PAGE_RIGHT(Input.KEY_RIGHT);

		/** Key the screens react to. */
		public final int canonical;

		Action(int canonical) { this.canonical = canonical; }
	}

	/** Input source: keyboard / mouse. */
	public static final int SOURCE_KEYBOARD = 0;

	/** Stick deflection that counts as a press. */
	private static final float AXIS_THRESHOLD = 0.55f;

	/** Keyboard bindings (default = canonical key). */
	private static final int[] keyBinding = new int[Action.values().length];

	/** Pad button bindings (-1 = controller mapping default). */
	private static final int[] padBinding = new int[Action.values().length];

	/** Source of the last dispatched event: 0 keyboard/mouse, n = controller n (1-based). */
	private static int lastSource = SOURCE_KEYBOARD;

	/** Capture target for the remap screen, or null. */
	private static Capture capture;

	/** Last stick direction per controller (to send one press per push). */
	private static final int[] stickX = new int[8], stickY = new int[8];

	/** Game receiving pad events. */
	private static itdelatrisu.tripletriad.gfx.BasicGame game;

	/** Receives the next raw key or pad button (remap screen). */
	public interface Capture {
		/** A keyboard key was pressed. */
		void key(int keycode);
		/** A pad button was pressed. */
		void button(int button);
	}

	static {
		resetDefaults();
	}

	private Controls() {}

	/** Restores the default bindings. */
	public static void resetDefaults() {
		for (Action a : Action.values()) {
			keyBinding[a.ordinal()] = a.canonical;
			padBinding[a.ordinal()] = -1;
		}
	}

	/**
	 * Starts listening to gamepads.
	 * @param target the game
	 */
	public static void init(itdelatrisu.tripletriad.gfx.BasicGame target) {
		game = target;
		try {
			Controllers.addListener(new ControllerAdapter() {
				@Override
				public boolean buttonDown(Controller controller, int buttonCode) {
					onButton(controller, buttonCode);
					return true;
				}

				@Override
				public boolean axisMoved(Controller controller, int axisCode, float value) {
					onAxis(controller, axisCode, value);
					return true;
				}
			});
			Log.warn("Controllers: " + Controllers.getControllers().size + " connected.");
		} catch (Throwable t) {
			Log.error("Gamepad support unavailable.", t);
		}
	}

	/** @return number of connected controllers */
	public static int controllerCount() {
		try {
			return Controllers.getControllers().size;
		} catch (Throwable t) {
			return 0;
		}
	}

	/** @return source of the last event (0 keyboard/mouse, n = controller n) */
	public static int lastSource() { return lastSource; }

	/** Marks the next event as coming from the keyboard / mouse. */
	public static void fromKeyboard() { lastSource = SOURCE_KEYBOARD; }

	/**
	 * Versus: may this source act for the player?
	 * Joystick 1 = Player A, joystick 2 = Player B. The keyboard/mouse is
	 * Player B when a joystick is connected, otherwise both (hot seat).
	 * @param playerA true for Player A
	 */
	public static boolean allows(boolean playerA) {
		if (lastSource == 1)
			return playerA;
		if (lastSource >= 2)
			return !playerA;
		return controllerCount() == 0 || !playerA;
	}

	/**
	 * Translates a remapped keyboard key into its canonical key.
	 * @param keycode the raw key
	 * @return the key to dispatch
	 */
	public static int translateKey(int keycode) {
		// explicitly remapped keys win over the defaults
		for (Action a : Action.values()) {
			int b = keyBinding[a.ordinal()];
			if (b == keycode && b != a.canonical)
				return a.canonical;
		}
		return keycode;
	}

	/** @return true if a remap capture is active */
	public static boolean isCapturing() { return capture != null; }

	/** Starts (or with null stops) capturing the next raw input. */
	public static void setCapture(Capture c) { capture = c; }

	/**
	 * Called by the game loop for raw keyboard keys during capture.
	 * @return true if consumed
	 */
	public static boolean captureKey(int keycode) {
		if (capture == null)
			return false;
		Capture c = capture;
		capture = null;
		c.key(keycode);
		return true;
	}

	public static int getKey(Action a) { return keyBinding[a.ordinal()]; }

	public static void setKey(Action a, int keycode) { keyBinding[a.ordinal()] = keycode; }

	public static int getPad(Action a) { return padBinding[a.ordinal()]; }

	public static void setPad(Action a, int button) { padBinding[a.ordinal()] = button; }

	/** @return a display name for a keyboard key */
	public static String keyName(int keycode) {
		String s = Keys.toString(keycode);
		return (s != null) ? s : ("#" + keycode);
	}

	/** @return a display name for a pad binding */
	public static String padName(Action a) {
		int b = padButton(a, firstMapping());
		return (b >= 0) ? padButtonName(b, firstMapping()) : "-";
	}

	private static ControllerMapping firstMapping() {
		try {
			if (Controllers.getControllers().size > 0)
				return Controllers.getControllers().first().getMapping();
		} catch (Throwable t) {
			// ignore
		}
		return null;
	}

	private static String padButtonName(int b, ControllerMapping m) {
		if (m != null) {
			if (b == m.buttonA) return "A";
			if (b == m.buttonB) return "B";
			if (b == m.buttonX) return "X";
			if (b == m.buttonY) return "Y";
			if (b == m.buttonStart) return "START";
			if (b == m.buttonBack) return "SELECT";
			if (b == m.buttonL1) return "L1";
			if (b == m.buttonR1) return "R1";
			if (b == m.buttonL2) return "L2";
			if (b == m.buttonR2) return "R2";
		}
		return "#" + b;
	}

	/** Effective pad button of an action (binding or mapping default). */
	private static int padButton(Action a, ControllerMapping m) {
		int b = padBinding[a.ordinal()];
		if (b >= 0 || m == null)
			return b;
		switch (a) {
			case CONFIRM: return m.buttonA;
			case CANCEL: return m.buttonB;
			case MENU: return m.buttonStart;
			case DELETE: return m.buttonY;
			case SKIP: return m.buttonX;
			case PAGE_LEFT: return m.buttonL1;
			case PAGE_RIGHT: return m.buttonR1;
			default: return -1;
		}
	}

	private static int indexOf(Controller c) {
		int i = Controllers.getControllers().indexOf(c, true);
		return (i < 0) ? 1 : i + 1;
	}

	private static void dispatch(int source, int key) {
		if (game == null || Gfx.getInput() == null || Gfx.getInput().isSuppressed())
			return;
		lastSource = source;
		game.keyPressed(key, (char) 0);
	}

	private static void onButton(Controller controller, int button) {
		if (capture != null) {
			Capture c = capture;
			capture = null;
			c.button(button);
			return;
		}
		int source = indexOf(controller);
		ControllerMapping m = controller.getMapping();
		if (m != null) {
			if (button == m.buttonDpadUp) { dispatch(source, Input.KEY_UP); return; }
			if (button == m.buttonDpadDown) { dispatch(source, Input.KEY_DOWN); return; }
			if (button == m.buttonDpadLeft) { dispatch(source, Input.KEY_LEFT); return; }
			if (button == m.buttonDpadRight) { dispatch(source, Input.KEY_RIGHT); return; }
		}
		for (Action a : Action.values()) {
			if (padButton(a, m) == button) {
				dispatch(source, a.canonical);
				return;
			}
		}
	}

	private static void onAxis(Controller controller, int axis, float value) {
		ControllerMapping m = controller.getMapping();
		if (m == null || capture != null)
			return;
		int idx = Math.min(stickX.length - 1, indexOf(controller) - 1);
		int dir = (value > AXIS_THRESHOLD) ? 1 : (value < -AXIS_THRESHOLD) ? -1 : 0;
		if (axis == m.axisLeftX) {
			if (dir != stickX[idx] && dir != 0)
				dispatch(idx + 1, dir > 0 ? Input.KEY_RIGHT : Input.KEY_LEFT);
			stickX[idx] = dir;
		} else if (axis == m.axisLeftY) {
			if (dir != stickY[idx] && dir != 0)
				dispatch(idx + 1, dir > 0 ? Input.KEY_DOWN : Input.KEY_UP);
			stickY[idx] = dir;
		}
	}

	/**
	 * Parses a {@code BIND_KEY_X} / {@code BIND_PAD_X} cfg line.
	 * @return true if the key was a binding
	 */
	public static boolean parseOption(String name, String value) {
		boolean key = name.startsWith("BIND_KEY_");
		boolean pad = name.startsWith("BIND_PAD_");
		if (!key && !pad)
			return false;
		try {
			Action a = Action.valueOf(name.substring(9));
			int v = Integer.parseInt(value.trim());
			if (key)
				keyBinding[a.ordinal()] = v;
			else
				padBinding[a.ordinal()] = v;
		} catch (IllegalArgumentException e) {
			Log.warn("Bad binding: " + name);
		}
		return true;
	}

	/** @return cfg lines for all bindings */
	public static String formatOptions() {
		StringBuilder sb = new StringBuilder();
		for (Action a : Action.values()) {
			sb.append("BIND_KEY_").append(a.name()).append(" = ").append(keyBinding[a.ordinal()]).append('\n');
			sb.append("BIND_PAD_").append(a.name()).append(" = ").append(padBinding[a.ordinal()]).append('\n');
		}
		return sb.toString();
	}
}
