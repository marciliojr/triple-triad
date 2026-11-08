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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * Every action has one keyboard key and one joystick button, all remappable
 * in Settings &gt; Controls and saved in the cfg ({@code BIND_KEY_<ACTION>} /
 * {@code BIND_PAD_<ACTION>}). A pressed key or button is translated into the
 * action's internal "canonical" code the screens react to; a keyboard key
 * that is not bound to any action is ignored (text still arrives as typed
 * characters). Joysticks come from gdx-controllers (SDL, with the bundled
 * {@code gamecontrollerdb.txt}) or, when SDL sees none, from the Linux
 * generic joystick interface {@code /dev/input/js*} (raw button numbers).
 */
public final class Controls {
	/** Pad roles used for the default joystick bindings. */
	private static final int P_NONE = -1, P_A = 0, P_B = 1, P_X = 2, P_Y = 3, P_L1 = 4, P_R1 = 5,
		P_BACK = 6, P_START = 7, P_L3 = 8, P_R3 = 9, P_UP = 10, P_DOWN = 11, P_LEFT = 12, P_RIGHT = 13;

	/** Raw button of each role on a generic (xpad-style) joystick, -1 = hat / none. */
	private static final int[] GENERIC_BUTTON = { 0, 1, 2, 3, 4, 5, 6, 7, 9, 10, -1, -1, -1, -1 };


	/** Remappable actions. */
	public enum Action {
		CONFIRM(Input.KEY_Z, Keys.Z, P_A),
		CANCEL(Input.KEY_ESCAPE, Keys.ESCAPE, P_B),
		SELECT(Input.KEY_T, Keys.T, P_BACK),
		MENU(Input.KEY_ENTER, Keys.ENTER, P_START),
		SKIP(Input.KEY_S, Keys.S, P_X),
		DELETE(Input.KEY_DELETE, Keys.FORWARD_DEL, P_Y),
		ZOOM(Input.KEY_C, Keys.C, P_L3),
		PAGE_LEFT(Input.KEY_LEFT, Keys.PAGE_UP, P_L1),
		PAGE_RIGHT(Input.KEY_RIGHT, Keys.PAGE_DOWN, P_R1),
		RANDOM(Input.KEY_R, Keys.R, P_R3),
		SAVE(Input.KEY_S, Keys.S, P_X),
		REMATCH(Input.KEY_F5, Keys.F5, P_BACK),
		AUTO_PLAY(Input.KEY_F1, Keys.F1, P_R3),
		UP(Input.KEY_UP, Keys.UP, P_UP),
		DOWN(Input.KEY_DOWN, Keys.DOWN, P_DOWN),
		LEFT(Input.KEY_LEFT, Keys.LEFT, P_LEFT),
		RIGHT(Input.KEY_RIGHT, Keys.RIGHT, P_RIGHT);

		/** Code the screens react to. */
		public final int canonical;
		/** Default keyboard key. */
		public final int defaultKey;
		/** Default pad role. */
		final int role;

		Action(int canonical, int defaultKey, int role) {
			this.canonical = canonical;
			this.defaultKey = defaultKey;
			this.role = role;
		}
	}

	/** Pad binding value: use the default button of the action. */
	public static final int PAD_DEFAULT = -1;

	/** Pad binding value: no button. */
	public static final int PAD_NONE = -2;

	/** Input source: keyboard / mouse. */
	public static final int SOURCE_KEYBOARD = 0;

	/** Stick deflection that counts as a press. */
	private static final float AXIS_THRESHOLD = 0.55f;

	private static final int[] keyBinding = new int[Action.values().length];
	private static final int[] padBinding = new int[Action.values().length];

	/** Source of the last dispatched event: 0 keyboard/mouse, n = controller n (1-based). */
	private static int lastSource = SOURCE_KEYBOARD;

	/** Capture target for the remap screen, or null. */
	private static Capture capture;

	/** Last stick / hat direction per controller and axis. */
	private static final int[][] axisDir = new int[8][16];

	/** Game receiving pad events. */
	private static itdelatrisu.tripletriad.gfx.BasicGame game;

	/** Live test: last pad input. */
	private static String lastPadInput = null;
	private static long lastPadTime = 0;

	/** Generic /dev/input/js* joysticks. */
	private static final List<JsDevice> jsDevices = new ArrayList<JsDevice>();
	private static final ConcurrentLinkedQueue<int[]> jsEvents = new ConcurrentLinkedQueue<int[]>();
	private static long nextScan = 0;

	/** A USB controller is present only as a raw HID device (e.g. 8BitDo 2dc8:3109). */
	private static boolean incompatibleHid = false;

	/** Receives the next raw key or pad button (remap screen). */
	public interface Capture {
		void key(int keycode);
		void button(int button);
	}

	static {
		resetDefaults();
	}

	private Controls() {}

	/** Restores the default bindings. */
	public static void resetDefaults() {
		for (Action a : Action.values()) {
			keyBinding[a.ordinal()] = a.defaultKey;
			padBinding[a.ordinal()] = PAD_DEFAULT;
		}
	}

	/** Starts listening to gamepads. */
	public static void init(itdelatrisu.tripletriad.gfx.BasicGame target) {
		game = target;
		try {
			Controllers.addListener(new ControllerAdapter() {
				@Override
				public boolean buttonDown(Controller controller, int buttonCode) {
					onButton(indexOf(controller), controller.getMapping(), buttonCode);
					return true;
				}

				@Override
				public void connected(Controller controller) {
					Log.warn("Controller connected: " + controller.getName());
				}

				@Override
				public void disconnected(Controller controller) {
					Log.warn("Controller disconnected: " + controller.getName());
				}

				@Override
				public boolean axisMoved(Controller controller, int axisCode, float value) {
					onGdxAxis(controller, axisCode, value);
					return true;
				}
			});
			Log.warn("Controllers: " + gdxCount() + " connected (SDL)" + (gdxCount() > 0 ? ": " + Controllers.getControllers().first().getName() : "") + ".");
		} catch (Throwable t) {
			Log.error("Gamepad support unavailable.", t);
		}
		scanDevices();
	}

	private static int gdxCount() {
		try {
			return Controllers.getControllers().size;
		} catch (Throwable t) {
			return 0;
		}
	}

	/** @return number of connected controllers (SDL or generic) */
	public static int controllerCount() {
		int n = gdxCount();
		return (n > 0) ? n : jsDevices.size();
	}

	/** @return the name of the first controller, or null */
	public static String controllerName() {
		try {
			if (gdxCount() > 0)
				return Controllers.getControllers().first().getName();
		} catch (Throwable t) {
			// ignore
		}
		if (!jsDevices.isEmpty())
			return jsDevices.get(0).name + " (" + jsDevices.get(0).path + ")";
		return null;
	}

	/** @return true if the controller is a generic (unmapped) joystick */
	public static boolean isGeneric() { return gdxCount() == 0 && !jsDevices.isEmpty(); }

	/** @return true if a controller is visible only in an incompatible HID mode */
	public static boolean incompatibleMode() { return incompatibleHid && controllerCount() == 0; }

	/** @return last pad input for the live test, or null after 4 s */
	public static String lastPadInput() {
		return (lastPadInput != null && System.currentTimeMillis() - lastPadTime < 4000) ? lastPadInput : null;
	}

	public static int lastSource() { return lastSource; }

	public static void fromKeyboard() { lastSource = SOURCE_KEYBOARD; }

	/** @return true if the last input came from a joystick */
	public static boolean fromPad() { return lastSource != SOURCE_KEYBOARD; }

	/**
	 * Versus: may this source act for the player?
	 * Joystick 1 = Player A, joystick 2 = Player B. The keyboard/mouse is
	 * Player B when a joystick is connected, otherwise both (hot seat).
	 */
	public static boolean allows(boolean playerA) {
		if (lastSource == 1)
			return playerA;
		if (lastSource >= 2)
			return !playerA;
		return controllerCount() == 0 || !playerA;
	}

	/**
	 * Translates a raw keyboard key into its action's canonical code.
	 * @return the canonical code, the raw key for text editing keys, or 0
	 */
	public static int translateKey(int keycode) {
		for (Action a : Action.values())
			if (keyBinding[a.ordinal()] == keycode)
				return a.canonical;
		if (keycode == Keys.BACKSPACE)
			return keycode; // text editing
		return 0;
	}

	/** @return true if {@code key} is the canonical code of the action */
	public static boolean is(int key, Action a) { return key != 0 && key == a.canonical; }

	public static boolean isCapturing() { return capture != null; }

	public static void setCapture(Capture c) { capture = c; }

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

	/** @return a short display name for a keyboard key */
	public static String keyName(int keycode) {
		switch (keycode) {
			case Keys.ESCAPE: return "Esc";
			case Keys.FORWARD_DEL: return "Del";
			case Keys.BACKSPACE: return "Bksp";
			case Keys.PAGE_UP: return "PgUp";
			case Keys.PAGE_DOWN: return "PgDn";
			case Keys.UP: return I18n.controlAction(Action.UP);
			case Keys.DOWN: return I18n.controlAction(Action.DOWN);
			case Keys.LEFT: return I18n.controlAction(Action.LEFT);
			case Keys.RIGHT: return I18n.controlAction(Action.RIGHT);
			case Keys.SPACE: return I18n.keySpace();
			case 0: return "-";
			default:
				String s = Keys.toString(keycode);
				return (s != null) ? s : ("#" + keycode);
		}
	}

	/** @return a display name for the pad binding of an action */
	public static String padName(Action a) {
		ControllerMapping m = firstMapping();
		int b = padButton(a, m);
		if (b >= 0)
			return buttonName(b, m);
		if (b == PAD_NONE || a.role == P_NONE)
			return "-";
		return dpadName(a.role); // D-pad / hat
	}

	/** @return "[key]" or "[key]/[pad]" with the current bindings */
	public static String label(Action a) {
		String s = "[" + keyName(getKey(a)) + "]";
		if (controllerCount() > 0) {
			String p = padName(a);
			if (!"-".equals(p))
				s += "/[" + p + "]";
		}
		return s;
	}

	private static final Pattern TOKENS = Pattern.compile(
		"(?<![\\w/\\[\\-])(Esq/Dir(?= p)|Left/Right(?= p)|Izq/Der(?= p)|Z/Enter|X/Del|Esc/C|Esc|Enter|Backspace|Del|F5|F1|Z|X|C|R|T|S|D|E)(?![\\w/\\]\\-])", Pattern.UNICODE_CHARACTER_CLASS);

	/**
	 * Replaces the key names of a hint ("Z/Enter", "Esc", "C"...) with the
	 * current bindings of the matching actions.
	 */
	public static String dyn(String text) {
		if (text == null)
			return null;
		Matcher m = TOKENS.matcher(text);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String t = m.group(1), r;
			switch (t) {
				case "Esq/Dir": case "Left/Right": case "Izq/Der":
					r = label(Action.PAGE_LEFT) + label(Action.PAGE_RIGHT); break;
				case "Z/Enter": case "Z": r = label(Action.CONFIRM); break;
				case "X/Del": case "X": case "Del": case "D": case "Backspace": r = label(Action.DELETE); break;
				case "Esc/C": case "Esc": r = label(Action.CANCEL); break;
				case "Enter": r = label(Action.MENU); break;
				case "F5": r = label(Action.REMATCH); break;
				case "F1": r = label(Action.AUTO_PLAY); break;
				case "C": r = label(Action.ZOOM); break;
				case "R": r = label(Action.RANDOM); break;
				case "T": case "E": r = label(Action.SELECT); break;
				case "S": r = label(Action.SAVE); break;
				default: r = t;
			}
			m.appendReplacement(sb, Matcher.quoteReplacement(r));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static ControllerMapping firstMapping() {
		try {
			if (gdxCount() > 0)
				return Controllers.getControllers().first().getMapping();
		} catch (Throwable t) {
			// ignore
		}
		return null;
	}

	private static String buttonName(int b, ControllerMapping m) {
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
			if (b == m.buttonLeftStick) return "L3";
			if (b == m.buttonRightStick) return "R3";
			if (b == m.buttonDpadUp) return dpadName(P_UP);
			if (b == m.buttonDpadDown) return dpadName(P_DOWN);
			if (b == m.buttonDpadLeft) return dpadName(P_LEFT);
			if (b == m.buttonDpadRight) return dpadName(P_RIGHT);
		}
		return I18n.padButton(b + 1);
	}

	private static String dpadName(int role) {
		Action a = (role == P_UP) ? Action.UP : (role == P_DOWN) ? Action.DOWN : (role == P_LEFT) ? Action.LEFT : Action.RIGHT;
		return "D-pad " + I18n.controlAction(a);
	}

	private static int roleButton(int role, ControllerMapping m) {
		if (role == P_NONE)
			return -1;
		if (m == null)
			return GENERIC_BUTTON[role];
		switch (role) {
			case P_A: return m.buttonA;
			case P_B: return m.buttonB;
			case P_X: return m.buttonX;
			case P_Y: return m.buttonY;
			case P_L1: return m.buttonL1;
			case P_R1: return m.buttonR1;
			case P_BACK: return m.buttonBack;
			case P_START: return m.buttonStart;
			case P_L3: return m.buttonLeftStick;
			case P_R3: return m.buttonRightStick;
			case P_UP: return m.buttonDpadUp;
			case P_DOWN: return m.buttonDpadDown;
			case P_LEFT: return m.buttonDpadLeft;
			case P_RIGHT: return m.buttonDpadRight;
			default: return -1;
		}
	}

	/** Effective pad button of an action (binding or default), or a negative value. */
	private static int padButton(Action a, ControllerMapping m) {
		int b = padBinding[a.ordinal()];
		if (b != PAD_DEFAULT)
			return b;
		return roleButton(a.role, m);
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

	private static void onButton(int source, ControllerMapping m, int button) {
		if (lastPadInput == null)
			Log.warn("First joystick button: #" + button + " (source " + source + (m == null ? ", generic" : "") + ")");
		lastPadInput = buttonName(button, m) + "  (#" + button + ")";
		lastPadTime = System.currentTimeMillis();
		if (capture != null) {
			Capture c = capture;
			capture = null;
			c.button(button);
			return;
		}
		// every action bound to this button, each canonical code once
		Set<Integer> keys = new LinkedHashSet<Integer>();
		for (Action a : Action.values())
			if (padButton(a, m) == button)
				keys.add(Integer.valueOf(a.canonical));
		for (Integer k : keys)
			dispatch(source, k.intValue());
	}

	private static void onDirection(int source, int axisSlot, float value, boolean horizontal) {
		int idx = Math.max(0, Math.min(axisDir.length - 1, source - 1));
		int slot = Math.max(0, Math.min(axisDir[0].length - 1, axisSlot));
		int dir = (value > AXIS_THRESHOLD) ? 1 : (value < -AXIS_THRESHOLD) ? -1 : 0;
		if (dir != 0 && dir != axisDir[idx][slot]) {
			lastPadInput = I18n.padAxis(axisSlot, dir);
			lastPadTime = System.currentTimeMillis();
			if (capture == null) {
				Action a = horizontal ? (dir > 0 ? Action.RIGHT : Action.LEFT) : (dir > 0 ? Action.DOWN : Action.UP);
				dispatch(source, a.canonical);
			}
		}
		axisDir[idx][slot] = dir;
	}

	private static void onGdxAxis(Controller controller, int axis, float value) {
		ControllerMapping m = controller.getMapping();
		if (m == null)
			return;
		if (axis == m.axisLeftX)
			onDirection(indexOf(controller), axis, value, true);
		else if (axis == m.axisLeftY)
			onDirection(indexOf(controller), axis, value, false);
	}

	// --- generic joystick fallback (/dev/input/js*)

	/** A /dev/input/jsN reader. */
	private static final class JsDevice implements Runnable {
		final String path, name;
		final int source;
		volatile boolean alive = true;

		JsDevice(String path, String name, int source) {
			this.path = path;
			this.name = name;
			this.source = source;
		}

		@Override
		public void run() {
			byte[] ev = new byte[8];
			try (InputStream in = new FileInputStream(path)) {
				while (alive) {
					int n = 0;
					while (n < 8) {
						int r = in.read(ev, n, 8 - n);
						if (r < 0)
							throw new IOException("closed");
						n += r;
					}
					int value = (short) ((ev[4] & 0xff) | (ev[5] << 8));
					int type = ev[6] & 0xff;
					int number = ev[7] & 0xff;
					if ((type & 0x80) != 0)
						continue; // initial state
					jsEvents.add(new int[] { source, type, number, value });
				}
			} catch (IOException e) {
				Log.warn("Joystick " + path + " closed: " + e.getMessage());
			}
			alive = false;
		}
	}

	/** Looks for /dev/input/js* devices and incompatible HID controllers. */
	private static void scanDevices() {
		nextScan = System.currentTimeMillis() + 2000;
		for (int i = jsDevices.size() - 1; i >= 0; i--)
			if (!jsDevices.get(i).alive)
				jsDevices.remove(i);
		if (gdxCount() == 0) {
			for (int i = 0; i < 8; i++) {
				String path = "/dev/input/js" + i;
				File f = new File(path);
				if (!f.exists() || !f.canRead() || hasJs(path))
					continue;
				String name = readFirst("/sys/class/input/js" + i + "/device/name", "Joystick " + i);
				JsDevice d = new JsDevice(path, name, jsDevices.size() + 1);
				jsDevices.add(d);
				Thread t = new Thread(d, "js" + i);
				t.setDaemon(true);
				t.start();
				Log.warn("Generic joystick: " + name + " (" + path + ")");
			}
		}
		incompatibleHid = false;
		File[] hid = new File("/sys/class/hidraw").listFiles();
		if (hid != null) {
			for (File h : hid) {
				String ue = readAll(new File(h, "device/uevent"));
				// 8BitDo receivers in a non X-input mode (e.g. 2dc8:3109) expose only hidraw
				if (ue.contains("HID_ID=0003:00002DC8:"))
					incompatibleHid = true;
			}
		}
	}

	private static boolean hasJs(String path) {
		for (JsDevice d : jsDevices)
			if (d.path.equals(path))
				return true;
		return false;
	}

	private static String readFirst(String path, String def) {
		String s = readAll(new File(path)).trim();
		return s.isEmpty() ? def : s;
	}

	private static String readAll(File f) {
		try {
			return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
		} catch (IOException | SecurityException e) {
			return "";
		}
	}

	/** Called every frame: rescans devices and dispatches generic joystick events. */
	public static void poll() {
		if (System.currentTimeMillis() >= nextScan)
			scanDevices();
		boolean use = gdxCount() == 0;
		int[] e;
		while ((e = jsEvents.poll()) != null) {
			if (!use)
				continue;
			int source = e[0], type = e[1], number = e[2], value = e[3];
			if (type == 1) {
				if (value != 0)
					onButton(source, null, number);
			} else if (type == 2) {
				float v = value / 32767f;
				// axes 0/1 = left stick, 6/7 = D-pad hat (xpad); others ignored
				if (number == 0 || number == 6)
					onDirection(source, number, v, true);
				else if (number == 1 || number == 7)
					onDirection(source, number, v, false);
			}
		}
	}

	// --- cfg

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

	public static String formatOptions() {
		StringBuilder sb = new StringBuilder();
		for (Action a : Action.values()) {
			sb.append("BIND_KEY_").append(a.name()).append(" = ").append(keyBinding[a.ordinal()]).append('\n');
			sb.append("BIND_PAD_").append(a.name()).append(" = ").append(padBinding[a.ordinal()]).append('\n');
		}
		return sb.toString();
	}
}
