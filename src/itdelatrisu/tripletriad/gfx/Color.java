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

/**
 * RGBA color (Slick-compatible).
 */
public class Color {
	/** Opaque white. */
	public static final Color white = new Color(1f, 1f, 1f, 1f);

	/** Red component [0, 1]. */
	public final float r;
	/** Green component [0, 1]. */
	public final float g;
	/** Blue component [0, 1]. */
	public final float b;
	/** Alpha component [0, 1]. */
	public final float a;

	/**
	 * Constructor.
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param a alpha
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * Opaque color.
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public Color(float r, float g, float b) {
		this(r, g, b, 1f);
	}

	/**
	 * Converts to a libGDX color.
	 * @return the color
	 */
	public com.badlogic.gdx.graphics.Color toGdx() {
		return new com.badlogic.gdx.graphics.Color(r, g, b, a);
	}
}
