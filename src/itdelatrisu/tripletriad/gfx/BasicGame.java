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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * Slick {@code BasicGame} stand-in on top of libGDX.
 */
public abstract class BasicGame extends ApplicationAdapter implements InputProcessor {
	/** Window title. */
	private final String title;

	/** Game container. */
	protected GameContainer container;

	/** Last key seen in {@code keyDown}, for {@code keyTyped}. */
	private int lastKeycode;

	/**
	 * Constructor.
	 * @param title the window title
	 */
	public BasicGame(String title) {
		this.title = title;
	}

	/**
	 * Returns the window title.
	 * @return the title
	 */
	public String getTitle() { return title; }

	/**
	 * Called once after the GL context exists.
	 * @param container the container
	 * @throws SlickException on setup failure
	 */
	public abstract void init(GameContainer container) throws SlickException;

	/**
	 * Renders a frame.
	 * @param container the container
	 * @param g the graphics
	 * @throws SlickException on render failure
	 */
	public abstract void render(GameContainer container, Graphics g) throws SlickException;

	/**
	 * Updates simulation state.
	 * @param container the container
	 * @param delta milliseconds since last update
	 * @throws SlickException on update failure
	 */
	public abstract void update(GameContainer container, int delta) throws SlickException;

	/**
	 * Handles a key press.
	 * @param key the key code ({@link Input})
	 * @param c the character
	 */
	public abstract void keyPressed(int key, char c);

	/**
	 * Handles a mouse press.
	 * @param button the button
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void mousePressed(int button, int x, int y) {}

	/**
	 * Handles mouse wheel movement.
	 * @param change the wheel delta (positive = up)
	 */
	public void mouseWheelMoved(int change) {}

	/**
	 * Called when the window close button is pressed.
	 * @return true to allow close
	 */
	public boolean closeRequested() { return true; }

	@Override
	public void create() {
		Gfx.create(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		container = Gfx.getContainer();
		Gfx.getInput().setGame(this);
		Gdx.input.setInputProcessor(this);
		try {
			init(container);
		} catch (SlickException e) {
			Log.error("Failed to initialize game.", e);
			Gdx.app.exit();
		}
	}

	@Override
	public void render() {
		int delta = (int) (Gdx.graphics.getDeltaTime() * 1000f);
		if (delta < 0)
			delta = 0;
		if (Gfx.getInput().isSuppressed() && delta > 50)
			delta = 50;
		Gfx.getInput().updateRepeat(delta);
		try {
			update(container, delta);
			Gfx.beginFrame();
			render(container, Gfx.getGraphics());
			Gfx.endFrame();
		} catch (SlickException e) {
			Log.error("Error in game loop.", e);
		}
	}

	@Override
	public void dispose() {
		Gfx.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Gfx.getInput().isSuppressed())
			return true;
		if (keycode == com.badlogic.gdx.Input.Keys.NUMPAD_ENTER)
			keycode = Input.KEY_ENTER;
		lastKeycode = keycode;
		char c = charFromKey(keycode);
		Gfx.getInput().onKeyDown(keycode, c);
		if (!isPrintableKey(keycode))
			keyPressed(keycode, c);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		Gfx.getInput().onKeyUp(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		if (Gfx.getInput().isSuppressed())
			return true;
		if (isPrintableKey(lastKeycode))
			keyPressed(lastKeycode, character);
		else if (character != 0 && !Character.isISOControl(character))
			keyPressed(0, character);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (Gfx.getInput().isSuppressed())
			return true;
		mousePressed(button, screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY) { return false; }

	@Override
	public boolean scrolled(float amountX, float amountY) {
		mouseWheelMoved((int) (amountY * 120f));
		return true;
	}

	/**
	 * Whether this key produces a {@code keyTyped} character we should wait for.
	 */
	private static boolean isPrintableKey(int keycode) {
		return (keycode >= com.badlogic.gdx.Input.Keys.A && keycode <= com.badlogic.gdx.Input.Keys.Z)
			|| (keycode >= com.badlogic.gdx.Input.Keys.NUM_0 && keycode <= com.badlogic.gdx.Input.Keys.NUM_9)
			|| keycode == com.badlogic.gdx.Input.Keys.SPACE
			|| keycode == com.badlogic.gdx.Input.Keys.MINUS;
	}

	/**
	 * Best-effort character for action keys (used before {@code keyTyped}).
	 */
	private static char charFromKey(int keycode) {
		if (keycode >= com.badlogic.gdx.Input.Keys.A && keycode <= com.badlogic.gdx.Input.Keys.Z)
			return (char) ('a' + (keycode - com.badlogic.gdx.Input.Keys.A));
		return 0;
	}
}
