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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Immediate-mode drawing (screen or onto an {@link Image}).
 */
public class Graphics {
	/** Off-screen target, or null for the back buffer. */
	private final Image target;

	/** Current color. */
	private Color color = Color.white;

	/** Line width for rectangles. */
	private float lineWidth = 1f;

	/**
	 * Screen graphics.
	 */
	public Graphics() {
		this.target = null;
	}

	/**
	 * Image graphics (compositing).
	 * @param target the image
	 */
	public Graphics(Image target) {
		this.target = target;
	}

	/**
	 * Sets the draw color.
	 * @param color the color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the line width used by {@link #drawRect}.
	 * @param width the width
	 */
	public void setLineWidth(float width) {
		this.lineWidth = width;
	}

	/**
	 * Draws an image at a position.
	 * @param image the image
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void drawImage(Image image, float x, float y) {
		if (target != null)
			target.blit(image, Math.round(x), Math.round(y));
		else
			image.draw(x, y);
	}

	/**
	 * Fills a rectangle.
	 */
	public void fillRect(float x, float y, float width, float height) {
		if (target != null) {
			target.fillPixmapRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height), color);
			return;
		}
		ShapeRenderer sr = Gfx.shapes(ShapeType.Filled);
		sr.setColor(color.r, color.g, color.b, color.a);
		sr.rect(x, y, width, height);
	}

	/**
	 * Strokes a rectangle.
	 */
	public void drawRect(float x, float y, float width, float height) {
		if (target != null)
			return;
		Gdx.gl.glLineWidth(lineWidth);
		ShapeRenderer sr = Gfx.shapes(ShapeType.Line);
		sr.setColor(color.r, color.g, color.b, color.a);
		sr.rect(x, y, width, height);
	}

	/**
	 * Flushes off-screen drawing to the image texture.
	 */
	public void flush() {
		if (target != null)
			target.upload();
		else
			Gfx.endFrame();
	}
}
