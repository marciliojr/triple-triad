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

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * TTF font wrapper (Slick {@code UnicodeFont} stand-in) with PT-BR glyphs.
 */
public class UnicodeFont {
	/** Font file name. */
	private final String filename;

	/** Pixel size. */
	private final int size;

	/** Extra glyphs to include. */
	private final StringBuilder extra = new StringBuilder();

	/** Generated font. */
	private BitmapFont font;

	/** Layout helper. */
	private final GlyphLayout layout = new GlyphLayout();

	/**
	 * Constructor.
	 * @param filename the TTF file name
	 * @param size the size
	 * @param bold unused
	 * @param italic unused
	 */
	public UnicodeFont(String filename, int size, boolean bold, boolean italic) {
		this.filename = filename;
		this.size = Math.max(8, size);
	}

	/**
	 * Adds ASCII glyphs (applied at {@link #loadGlyphs()}).
	 */
	public void addAsciiGlyphs() {}

	/**
	 * Adds extra glyphs (Portuguese, etc.).
	 * @param glyphs the characters
	 */
	public void addGlyphs(String glyphs) {
		if (glyphs != null)
			extra.append(glyphs);
	}

	/**
	 * Dummy effects list so existing Options code can call {@code getEffects().add}.
	 * @return a throwaway list
	 */
	public ArrayList<Object> getEffects() {
		return new ArrayList<Object>();
	}

	/**
	 * Builds the bitmap font.
	 * @throws SlickException if the file cannot be loaded
	 */
	public void loadGlyphs() throws SlickException {
		try {
			FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Assets.handle(filename));
			FreeTypeFontParameter param = new FreeTypeFontParameter();
			param.size = size;
			param.flip = true;
			param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + extra;
			font = gen.generateFont(param);
			gen.dispose();
		} catch (Exception e) {
			throw new SlickException("Failed to load font " + filename, e);
		}
	}

	/**
	 * Returns the width of a string.
	 * @param text the text
	 * @return the width
	 */
	public int getWidth(String text) {
		if (font == null || text == null)
			return 0;
		layout.setText(font, text);
		return (int) Math.ceil(layout.width);
	}

	/**
	 * Returns the line height.
	 * @return the height
	 */
	public int getLineHeight() {
		return (font != null) ? (int) font.getLineHeight() : size;
	}

	/**
	 * Draws a string.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param text the text
	 * @param color the color
	 */
	public void drawString(float x, float y, String text, Color color) {
		if (font == null || text == null)
			return;
		font.setColor(color.r, color.g, color.b, color.a);
		font.draw(Gfx.sprites(), text, x, y);
	}
}
