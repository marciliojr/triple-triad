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

import com.badlogic.gdx.Gdx;

/**
 * Window / timing façade used by screens.
 */
public class GameContainer {
	/** Window width. */
	private final int width;

	/** Window height. */
	private final int height;

	/** Input helper. */
	private final Input input;

	/**
	 * Constructor.
	 * @param width the width
	 * @param height the height
	 * @param input the input
	 */
	public GameContainer(int width, int height, Input input) {
		this.width = width;
		this.height = height;
		this.input = input;
	}

	/**
	 * Returns the window width.
	 * @return the width
	 */
	public int getWidth() { return width; }

	/**
	 * Returns the window height.
	 * @return the height
	 */
	public int getHeight() { return height; }

	/**
	 * Returns the input helper.
	 * @return the input
	 */
	public Input getInput() { return input; }

	/**
	 * Sets the target frame rate.
	 * @param fps the fps
	 */
	public void setTargetFrameRate(int fps) {
		Gdx.graphics.setForegroundFPS(fps);
	}

	/**
	 * Shows or hides the FPS counter (no-op; FPS overlay is unused).
	 * @param show ignored
	 */
	public void setShowFPS(boolean show) { }

	/**
	 * Requests continuous rendering.
	 * @param always ignored (always on)
	 */
	public void setAlwaysRender(boolean always) { }

	/**
	 * Sets the music volume.
	 * @param volume the volume [0, 1]
	 */
	public void setMusicVolume(float volume) {
		Gfx.setMusicVolume(volume);
	}

	/**
	 * Sets the sound volume.
	 * @param volume the volume [0, 1]
	 */
	public void setSoundVolume(float volume) {
		Gfx.setSoundVolume(volume);
	}

	/**
	 * Closes the application.
	 */
	public void exit() {
		Gdx.app.exit();
	}
}
