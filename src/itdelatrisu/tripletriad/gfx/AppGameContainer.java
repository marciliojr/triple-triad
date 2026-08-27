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

import java.io.File;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;
import com.badlogic.gdx.graphics.glutils.HdpiMode;

/**
 * Desktop window launcher (Slick {@code AppGameContainer} stand-in).
 */
public class AppGameContainer {
	/** Game listener. */
	private final BasicGame game;

	/** LWJGL3 window config. */
	private final Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

	/**
	 * Constructor.
	 * @param game the game
	 */
	public AppGameContainer(BasicGame game) {
		this.game = game;
		config.setTitle(game.getTitle());
		config.setResizable(false);
		config.useVsync(true);
		config.setHdpiMode(HdpiMode.Pixels);
		config.setWindowListener(new Lwjgl3WindowAdapter() {
			@Override
			public boolean closeRequested() {
				return game.closeRequested();
			}
		});
	}

	/**
	 * Returns the current screen width.
	 * @return the width
	 */
	public int getScreenWidth() {
		return Lwjgl3ApplicationConfiguration.getDisplayMode().width;
	}

	/**
	 * Returns the current screen height.
	 * @return the height
	 */
	public int getScreenHeight() {
		return Lwjgl3ApplicationConfiguration.getDisplayMode().height;
	}

	/**
	 * Sets the window size.
	 * @param width the width
	 * @param height the height
	 * @param fullscreen unused (windowed)
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {
		config.setWindowedMode(width, height);
	}

	/**
	 * Makes the window undecorated (borderless).
	 * @param decorated false for borderless
	 */
	public void setDecorated(boolean decorated) {
		config.setDecorated(decorated);
	}

	/**
	 * Sets window icons from {@code res/}.
	 * @param icons file names
	 */
	public void setIcons(String[] icons) {
		if (icons == null || icons.length == 0)
			return;
		String[] paths = new String[icons.length];
		for (int i = 0; i < icons.length; i++)
			paths[i] = Assets.file(icons[i]).getAbsolutePath();
		config.setWindowIcon(FileType.Absolute, paths);
	}

	/**
	 * Starts the application (does not return until the window closes).
	 */
	public void start() {
		new Lwjgl3Application(game, config);
	}

	/**
	 * Sets the window icon from an explicit file (unused helper).
	 * @param file the file
	 */
	public void setIcon(File file) {
		if (file != null && file.isFile())
			config.setWindowIcon(FileType.Absolute, file.getAbsolutePath());
	}
}
