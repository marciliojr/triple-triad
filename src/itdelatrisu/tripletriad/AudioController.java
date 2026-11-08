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

import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.gfx.Music;
import itdelatrisu.tripletriad.gfx.SlickException;
import itdelatrisu.tripletriad.gfx.Sound;

/**
 * Controller for all audio.
 */
public class AudioController {
	/** BGM track. */
	private static Music bgm;

	/** Sound effects. */
	public enum Effect {
		BACK ("sound-back.wav"),
		CARD ("sound-card.wav"),
		INVALID ("sound-invalid.wav"),
		SELECT ("sound-select.wav"),
		SPECIAL ("sound-special.wav"),
		START ("sound-start.wav"),
		TURN ("sound-turn.wav");

		/** The associated file name. */
		private String filename;

		/** The associated sound. */
		private Sound sound;

		/**
		 * Initializes all sound effects.
		 */
		private static void init() {
			for (Effect effect : Effect.values()) {
				try {
					effect.sound = new Sound(effect.filename);
				} catch (Exception e) {
					Log.error(String.format("Failed to load sound '%s'.", effect.filename), e);
				}
			}
		}

		/**
		 * Constructor.
		 * @param filename the file name
		 */
		Effect(String filename) {
			this.filename = filename;
		}

		/**
		 * Plays the sound effect.
		 */
		public void play() { if (sound != null) sound.play(); }

		/**
		 * Plays the effect, cutting any instance still ringing.
		 */
		public void playReplacing() { if (sound != null) sound.playReplacing(); }
	}

	/**
	 * Plays the cursor navigation sound if it is enabled.
	 */
	public static void playCursor() {
		if (Options.isCursorSoundEnabled())
			Effect.SELECT.play();
	}

	/**
	 * Starts or pauses the BGM according to the music option.
	 */
	public static void applyMusic() {
		if (bgm == null)
			return;
		if (Options.isMusicEnabled())
			bgm.loop();
		else
			bgm.pause();
	}

	/**
	 * Pauses the BGM (e.g. while a video with its own audio plays).
	 * Call {@link #applyMusic()} to resume.
	 */
	public static void pauseMusic() {
		if (bgm != null)
			bgm.pause();
	}

	/** Victory track (lazy), or null. */
	private static Music victory;

	/** Whether the victory track is playing. */
	private static boolean victoryPlaying;

	/**
	 * Loops the victory track ({@code res/victory.ogg}) instead of the BGM.
	 */
	public static void playVictory() {
		if (victoryPlaying || !Options.isMusicEnabled())
			return;
		try {
			if (victory == null)
				victory = new Music("victory.ogg");
		} catch (SlickException e) {
			Log.error("Failed to load victory music.", e);
			return;
		}
		pauseMusic();
		victory.loop();
		victoryPlaying = true;
	}

	/**
	 * Stops the victory track and resumes the BGM.
	 */
	public static void stopVictory() {
		if (!victoryPlaying)
			return;
		victoryPlaying = false;
		if (victory != null)
			victory.stop();
		applyMusic();
	}

	// This class should not be instantiated.
	private AudioController() {}

	/**
	 * Initializes sounds and starts the BGM track.
	 */
	public static void init() {
		init(true);
	}

	/**
	 * Initializes sounds; optionally holds the BGM (e.g. until the intro ends).
	 * @param startMusic false to load the BGM without playing it
	 */
	public static void init(boolean startMusic) {
		try {
			bgm = new Music("bgm.ogg");
			if (startMusic)
				applyMusic();
			Effect.init();
		} catch (SlickException e) {
			Log.error("Failed to load audio.", e);
		}
	}
}