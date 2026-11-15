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
	/** Which looping bed should be audible on the current screen. */
	public enum Bed { MENU, BATTLE }

	/** Battle/match track ({@code res/bgm.ogg}). */
	private static Music battle;

	/** Menu-and-hub track ({@code res/menu.ogg}), or unused if the file is missing. */
	private static Music menu;

	/** Bed selected for the current screen. */
	private static Bed bed = Bed.MENU;

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
	 * Bed for a top-level screen: {@code null} means leave music alone
	 * (the intro has its own audio).
	 */
	public static Bed bedFor(GameScreen screen) {
		if (screen == null || screen == GameScreen.INTRO)
			return null;
		return (screen == GameScreen.MATCH) ? Bed.BATTLE : Bed.MENU;
	}

	/**
	 * Selects the bed for {@code screen} and applies it, without restarting
	 * a track that is already playing. No-op during the intro.
	 */
	public static void onScreen(GameScreen screen) {
		Bed next = bedFor(screen);
		if (next == null)
			return;
		if (next != bed) {
			Music outgoing = track(bed);
			Music incoming = track(next);
			if (outgoing != null && outgoing != incoming)
				outgoing.stop();
			bed = next;
		}
		if (!victoryPlaying)
			applyMusic();
	}

	/** @return the loaded track for a bed, never starting playback */
	private static Music track(Bed which) {
		if (which == Bed.BATTLE)
			return battle;
		return (menu != null) ? menu : battle;
	}

	/**
	 * Starts, resumes or pauses the current bed according to the music option.
	 * Does not restart a track that is already playing. While the victory
	 * theme is up, only that theme is toggled.
	 */
	public static void applyMusic() {
		if (victoryPlaying) {
			if (victory == null)
				return;
			if (Options.isMusicEnabled())
				victory.loop();
			else
				victory.pause();
			return;
		}
		Music want = track(bed);
		if (want == null)
			return;
		Music other = track(bed == Bed.BATTLE ? Bed.MENU : Bed.BATTLE);
		if (other != null && other != want && other.isPlaying())
			other.pause();
		if (Options.isMusicEnabled())
			want.loop();
		else
			want.pause();
	}

	/**
	 * Pauses the current bed (e.g. while a video with its own audio plays).
	 * Call {@link #applyMusic()} to resume from the same place.
	 */
	public static void pauseMusic() {
		if (victoryPlaying && victory != null)
			victory.pause();
		Music want = track(bed);
		if (want != null)
			want.pause();
	}

	/** Victory track (lazy), or null. */
	private static Music victory;

	/** Whether the victory track is playing. */
	private static boolean victoryPlaying;

	/**
	 * Loops the victory track ({@code res/victory.ogg}) instead of the current bed.
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
	 * Stops the victory track and resumes the current bed.
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
	 * Initializes sounds and starts the menu bed.
	 */
	public static void init() {
		init(true);
	}

	/**
	 * Initializes sounds; optionally holds the beds (e.g. until the intro ends).
	 * @param startMusic false to load the tracks without playing them
	 */
	public static void init(boolean startMusic) {
		try {
			battle = new Music("bgm.ogg");
		} catch (SlickException e) {
			Log.error("Failed to load battle music.", e);
		}
		try {
			menu = new Music("menu.ogg");
		} catch (SlickException e) {
			Log.error("Failed to load menu music; battle theme will be used on menus.", e);
			menu = null;
		}
		if (startMusic)
			applyMusic();
		Effect.init();
	}
}
