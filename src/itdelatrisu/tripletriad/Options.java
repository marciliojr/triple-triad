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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import itdelatrisu.tripletriad.gfx.AppGameContainer;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.gfx.SlickException;
import itdelatrisu.tripletriad.gfx.UnicodeFont;
import itdelatrisu.tripletriad.gfx.font.effects.ColorEffect;

public class Options {
	/** Card data file. */
	public static final String DATA_FILE = "deck.txt";

	/** File for logging errors. */
	public static final File LOG_FILE = new File(".triple-triad.log");

	/** File for storing user options. */
	private static final File OPTIONS_FILE = new File(".triple-triad.cfg");

	/** Container dimensions. */
	private static int width = 1280, height = 720;

	/** Card length. */
	private static int cardLength = 256;

	/** Volume. */
	private static float musicVolume = 0.6f, soundVolume = 0.8f;

	/** Whether background music is enabled. */
	private static boolean musicEnabled = true;

	/** Whether the cursor navigation sound is enabled. */
	private static boolean cursorSoundEnabled = true;

	/** UI language. */
	private static Lang lang = Lang.PT_BR;

	/** Active player profile id (0 = none). */
	private static int activeProfileId;

	/** Target frame rate. */
	private static int fps = 60;

	/** Default font. */
	private static UnicodeFont font;

	/** Smaller font for menus and hints. */
	private static UnicodeFont smallFont;

	/** Font file. */
	private static File fontFile = new File("OpenSans-Light.ttf");

	/** AI types. */
	public enum AIType { RANDOM, OFFENSIVE, DEFENSIVE, BALANCED };

	/** Default AI types. */
	private static AIType playerAI = AIType.BALANCED, opponentAI = AIType.BALANCED;

	// This class should not be instantiated.
	private Options() {}

	/**
	 * Returns the container width.
	 * @return the width
	 */
	public static int getWidth() { return width; }

	/**
	 * Returns the container height.
	 * @return the height
	 */
	public static int getHeight() { return height; }

	/**
	 * Returns the card length.
	 * @return the card length
	 */
	public static int getCardLength() { return cardLength; }

	/**
	 * Returns the default font.
	 * @return the UnicodeFont
	 */
	public static UnicodeFont getFont() { return font; }

	/**
	 * Returns the smaller UI font.
	 * @return the UnicodeFont
	 */
	public static UnicodeFont getSmallFont() { return smallFont != null ? smallFont : font; }

	/**
	 * Returns the player AI type.
	 * @return the AIType
	 */
	public static AIType getPlayerAI() { return playerAI; }

	/**
	 * Returns the opponent AI type.
	 * @return the AIType
	 */
	public static AIType getOpponentAI() { return opponentAI; }

	/**
	 * Returns whether background music is enabled.
	 * @return true if enabled
	 */
	public static boolean isMusicEnabled() { return musicEnabled; }

	/**
	 * Sets whether background music is enabled.
	 * @param enabled true if enabled
	 */
	public static void setMusicEnabled(boolean enabled) { musicEnabled = enabled; }

	/**
	 * Toggles background music.
	 */
	public static void toggleMusicEnabled() { musicEnabled = !musicEnabled; }

	/**
	 * Returns whether the cursor navigation sound is enabled.
	 * @return true if enabled
	 */
	public static boolean isCursorSoundEnabled() { return cursorSoundEnabled; }

	/**
	 * Toggles the cursor navigation sound.
	 */
	public static void toggleCursorSound() { cursorSoundEnabled = !cursorSoundEnabled; }

	/**
	 * Returns the UI language.
	 * @return the language
	 */
	public static Lang getLang() { return lang; }

	/**
	 * Sets the UI language.
	 * @param value the language
	 */
	public static void setLang(Lang value) {
		if (value != null)
			lang = value;
	}

	/**
	 * Returns the active profile id.
	 * @return the id, or 0
	 */
	public static int getActiveProfileId() { return activeProfileId; }

	/**
	 * Sets the active profile id.
	 * @param id the id
	 */
	public static void setActiveProfileId(int id) {
		activeProfileId = (id > 0) ? id : 0;
	}

	/**
	 * Sets the container size and makes the window borderless if the container
	 * size is identical to the screen resolution.
	 * <p>
	 * If the configured resolution is larger than the screen size, the screen
	 * resolution will be used.
	 * @param app the game container
	 * @throws SlickException failure to set display mode
	 */
	public static void setDisplayMode(AppGameContainer app) throws SlickException {
		int screenWidth = app.getScreenWidth();
		int screenHeight = app.getScreenHeight();
		if (screenWidth < width || screenHeight < height) {
			width = screenWidth;
			height = screenHeight;
		}

		app.setDisplayMode(width, height, false);
		if (screenWidth == width && screenHeight == height)
			app.setDecorated(false);

		// set card length
		cardLength = (int) (width * 0.17f);
	}

	/**
	 * Initializes options.
	 * @param container the game container
	 */
	@SuppressWarnings("unchecked")
	public static void init(GameContainer container) {
		container.setTargetFrameRate(fps);
		container.setShowFPS(false);
		container.setAlwaysRender(true);
		container.getInput().enableKeyRepeat();
		container.setMusicVolume(musicVolume);
		container.setSoundVolume(soundVolume);

		try {
			String extraGlyphs = "áàâãéêíóôõúüçÁÀÂÃÉÊÍÓÔÕÚÜÇñÑ¿¡";
			font = new UnicodeFont(fontFile.getName(),
					(int) (32 * (cardLength / 256f) * 1.6f), false, false);
			font.addAsciiGlyphs();
			font.addGlyphs(extraGlyphs);
			font.getEffects().add(new ColorEffect());
			font.loadGlyphs();

			smallFont = new UnicodeFont(fontFile.getName(),
					Math.max(14, (int) (18 * (cardLength / 256f) * 1.6f)), false, false);
			smallFont.addAsciiGlyphs();
			smallFont.addGlyphs(extraGlyphs);
			smallFont.getEffects().add(new ColorEffect());
			smallFont.loadGlyphs();
		} catch (SlickException e) {
			Log.error("Failed to load fonts.", e);
		}
	}

	/**
	 * Reads user options from the options file, if it exists.
	 */
	public static void parseOptions() {
		// if no config file, use default settings
		if (!OPTIONS_FILE.isFile()) {
			saveOptions();
			return;
		}

		try (BufferedReader in = new BufferedReader(new FileReader(OPTIONS_FILE))) {
			String line;
			String name, value;
			int i;
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.length() < 2 || line.charAt(0) == '#')
					continue;
				int index = line.indexOf('=');
				if (index == -1)
					continue;
				name = line.substring(0, index).trim();
				value = line.substring(index + 1).trim();
				switch (name) {
				case "WIDTH":
					i = Integer.parseInt(value);
					if (i > 0)
						width = i;
					break;
				case "HEIGHT":
					i = Integer.parseInt(value);
					if (i > 0)
						height = i;
					break;
				case "MUSIC":
					i = Integer.parseInt(value);
					if (i >= 0 && i <= 100)
						musicVolume = i / 100f;
					break;
				case "SOUND":
					i = Integer.parseInt(value);
					if (i >= 0 && i <= 100)
						soundVolume = i / 100f;
					break;
				case "FPS":
					i = Integer.parseInt(value);
					if (i > 0 && i < 240)
						fps = i;
					break;
				case "FONT":
					File newFont = new File(value);
					if (newFont.isFile())
						fontFile = newFont;
					break;
				case "AI_PLAYER":
					playerAI = AIType.valueOf(value);
					break;
				case "AI_OPPONENT":
					opponentAI = AIType.valueOf(value);
					break;
				case "MUSIC_ENABLED":
					musicEnabled = Boolean.parseBoolean(value);
					break;
				case "CURSOR_SOUND":
					cursorSoundEnabled = Boolean.parseBoolean(value);
					break;
				case "LANGUAGE":
					try {
						lang = Lang.valueOf(value);
					} catch (IllegalArgumentException e) {
						lang = Lang.PT_BR;
					}
					break;
				case "ACTIVE_PROFILE":
					try {
						int profileId = Integer.parseInt(value);
						if (profileId > 0)
							activeProfileId = profileId;
					} catch (NumberFormatException e) {
						activeProfileId = 0;
					}
					break;
				default:
					try {
						Rule rule = Rule.valueOf(name);
						rule.setState(Boolean.parseBoolean(value));
					} catch (IllegalArgumentException e) {
						Log.warn(String.format("Failed to read line: %s", line));
						continue;
					}
				}
			}
		} catch (IOException e) {
			Log.error(String.format("Failed to read file '%s'.", OPTIONS_FILE.getAbsolutePath()), e);
		} catch (IllegalArgumentException e) {
			Log.warn("Format error in options file.", e);
			return;
		}
	}

	/**
	 * (Over)writes user options to a file.
	 */
	public static void saveOptions() {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(OPTIONS_FILE), "utf-8"))) {
			// header
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
			String date = dateFormat.format(new Date());
			writer.write("# Triple Triad configuration");
			writer.newLine();
			writer.write("# last updated on ");
			writer.write(date);
			writer.newLine();
			writer.newLine();

			// game settings
			writer.write("# Game Settings");
			writer.newLine();
			writer.write(String.format("WIDTH = %d", width));
			writer.newLine();
			writer.write(String.format("HEIGHT = %d", height));
			writer.newLine();
			writer.write(String.format("MUSIC = %d", (int) (musicVolume * 100)));
			writer.newLine();
			writer.write(String.format("SOUND = %d", (int) (soundVolume * 100)));
			writer.newLine();
			writer.write(String.format("FPS = %d", fps));
			writer.newLine();
			writer.write(String.format("FONT = %s", fontFile.getName()));
			writer.newLine();
			writer.write(String.format("MUSIC_ENABLED = %b", musicEnabled));
			writer.newLine();
			writer.write(String.format("CURSOR_SOUND = %b", cursorSoundEnabled));
			writer.newLine();
			writer.write(String.format("LANGUAGE = %s", lang.toString()));
			writer.newLine();
			writer.write(String.format("ACTIVE_PROFILE = %d", activeProfileId));
			writer.newLine();
			writer.newLine();

			// AI
			writer.write("# AI Type (RANDOM, OFFENSIVE, DEFENSIVE, BALANCED)");
			writer.newLine();
			writer.write(String.format("AI_PLAYER = %s", playerAI.toString()));
			writer.newLine();
			writer.write(String.format("AI_OPPONENT = %s", opponentAI.toString()));
			writer.newLine();
			writer.newLine();

			// rules
			writer.write("# Rules");
			writer.newLine();
			for (Rule rule : Rule.values()) {
				writer.write(String.format("%s = %b", rule.toString(), rule.isActive()));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			Log.error(String.format("Failed to write to file '%s'.", OPTIONS_FILE.getAbsolutePath()), e);
		}
	}
}