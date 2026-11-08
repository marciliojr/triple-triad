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

import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.gfx.SlickException;
import itdelatrisu.tripletriad.gfx.VideoLoop;

/**
 * Opening video ({@code res/intro/} frames + {@code res/intro.ogg}), played once
 * before the main menu. S skips it.
 */
public class IntroScreen extends Screen {
	/** Frame rate of the extracted frames. */
	private static final float FPS = 24f;

	/** Game. */
	private final TripleTriad game;

	/** Video, or null if unavailable. */
	private VideoLoop video;

	/** Whether the intro already ended (guards double exit). */
	private boolean done;

	/**
	 * Creates the intro screen.
	 * @param game the game
	 */
	public IntroScreen(TripleTriad game) {
		this.game = game;
	}

	/**
	 * Loads and starts the intro.
	 * @return false if the video cannot be played (go straight to the menu)
	 */
	public boolean start() {
		try {
			video = new VideoLoop("intro", FPS);
		} catch (SlickException e) {
			Log.error("Intro video unavailable; skipping.", e);
			return false;
		}
		video.setLooping(false);
		video.play(Options.isMusicEnabled());
		done = false;
		return true;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		if (done || video == null)
			return;
		g.setColor(new itdelatrisu.tripletriad.gfx.Color(0f, 0f, 0f));
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		if (!video.draw(0, 0, container.getWidth(), container.getHeight())) {
			finish();
			return;
		}
		Ui.drawCentered(Options.getSmallFont(), I18n.hintSkipIntro(),
			container.getHeight() * 0.94f, Ui.DISABLED);
	}

	@Override
	public void update(GameContainer container, int delta) {
		if (!done && video != null && video.isFinished())
			finish();
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_S)
			finish();
	}

	/**
	 * Stops the video, frees it and hands over to the menu.
	 */
	private void finish() {
		if (done)
			return;
		done = true;
		if (video != null) {
			video.dispose();
			video = null;
		}
		game.introFinished();
	}
}
