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
 * Looping music (Slick {@code Music} stand-in).
 */
public class Music {
	/** libGDX music. */
	private final com.badlogic.gdx.audio.Music music;

	/**
	 * Loads music from {@code res/}.
	 * @param ref the file name
	 * @throws SlickException if loading fails
	 */
	public Music(String ref) throws SlickException {
		try {
			music = Gdx.audio.newMusic(Assets.handle(ref));
		} catch (Exception e) {
			throw new SlickException("Failed to load music " + ref, e);
		}
	}

	/**
	 * Loops the track at the current music volume.
	 * If the track is already playing, only the volume is updated (no restart).
	 */
	public void loop() {
		if (music == null)
			return;
		music.setLooping(true);
		music.setVolume(Gfx.getMusicVolume());
		if (!music.isPlaying())
			music.play();
	}

	/**
	 * Pauses playback.
	 */
	public void pause() {
		if (music != null)
			music.pause();
	}

	/**
	 * Stops playback and rewinds to the start.
	 */
	public void stop() {
		if (music != null)
			music.stop();
	}

	/** @return true while the track is audible (false when paused or stopped) */
	public boolean isPlaying() {
		return music != null && music.isPlaying();
	}
}
