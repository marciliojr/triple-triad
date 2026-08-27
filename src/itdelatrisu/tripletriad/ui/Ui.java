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

import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.TripleTriad;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Shared drawing helpers for menu screens.
 */
public final class Ui {
	/** Dim overlay. */
	private static final Color DIM = new Color(0f, 0f, 0f, 0.5f);

	/** Title color. */
	public static final Color TITLE = new Color(1f, 0.92f, 0.55f);

	/** Hint / secondary text. */
	public static final Color HINT = new Color(0.85f, 0.85f, 0.85f);

	/** Disabled menu item. */
	public static final Color DISABLED = new Color(0.55f, 0.55f, 0.55f);

	/** Selected menu item. */
	public static final Color SELECTED = Color.white;

	// This class should not be instantiated.
	private Ui() {}

	/**
	 * Draws the board background with a dark overlay.
	 * @param g the graphics context
	 */
	public static void drawBackdrop(Graphics g) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		GameImage.BOARD_MAT.getImage().drawCentered(width / 2, height / 2);
		g.setColor(DIM);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Draws a string centered horizontally.
	 */
	public static void drawCentered(UnicodeFont font, String text, float y, Color color) {
		if (text == null)
			text = "";
		float x = (Options.getWidth() - font.getWidth(text)) / 2f;
		font.drawString(x, y, text, color);
	}

	/**
	 * Returns whether a character can be used in a profile or deck name.
	 * @param c the character
	 * @return true if allowed
	 */
	public static boolean isNameChar(char c) {
		return Character.isLetterOrDigit(c) || c == ' ' || c == '-' || c == '_';
	}

	/**
	 * Draws a full-size card preview over a dimmed screen.
	 * @param g the graphics context
	 * @param catalog the card catalog
	 * @param cardId the card ID
	 */
	public static void drawCardPreview(Graphics g, Deck catalog, int cardId) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		g.setColor(new Color(0f, 0f, 0f, 0.72f));
		g.fillRect(0, 0, width, height);
		if (catalog == null)
			return;
		Card source = catalog.getCardById(cardId);
		if (source == null)
			return;
		Card card = new Card(source);
		card.setOwner(TripleTriad.PLAYER);
		float size = Options.getCardLength();
		card.draw((width - size) / 2f, (height - size) / 2f);
	}
}
