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
 * Sound effect (Slick {@code Sound} stand-in).
 */
public class Sound {
	/** libGDX sound. */
	private final com.badlogic.gdx.audio.Sound sound;

	/**
	 * Loads a sound from {@code res/}.
	 * @param ref the file name
	 * @throws SlickException if loading fails
	 */
	public Sound(String ref) throws SlickException {
		try {
			sound = Gdx.audio.newSound(Assets.handle(ref));
		} catch (Exception e) {
			throw new SlickException("Failed to load sound " + ref, e);
		}
	}

	/**
	 * Plays the effect at the current sound volume.
	 */
	public void play() {
		if (sound != null)
			sound.play(Gfx.getSoundVolume());
	}
}
