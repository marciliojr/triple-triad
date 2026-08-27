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

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Texture wrapper with Slick {@code Image} drawing methods.
 */
public class Image {
	/** GPU texture. */
	private Texture texture;

	/** CPU copy used when compositing (ranks onto cards). */
	private Pixmap pixmap;

	/** Draw width. */
	private float width;

	/** Draw height. */
	private float height;

	/** Texture region (flipped for y-down). */
	private TextureRegion region;

	/** Draw alpha. */
	private float alpha = 1f;

	/**
	 * Loads an image from {@code res/} or {@code cards/}.
	 * @param ref the file name
	 */
	public Image(String ref) {
		this.pixmap = new Pixmap(Assets.handle(ref));
		createTexture(pixmap.getWidth(), pixmap.getHeight());
	}

	/**
	 * Internal constructor for scaled views / copies.
	 */
	private Image(Texture texture, Pixmap pixmap, float width, float height) {
		this.texture = texture;
		this.pixmap = pixmap;
		this.width = width;
		this.height = height;
		this.region = new TextureRegion(texture);
		this.region.flip(false, true);
	}

	/**
	 * Uploads the pixmap as a texture.
	 */
	private void createTexture(float drawWidth, float drawHeight) {
		if (texture != null)
			texture.dispose();
		texture = new Texture(pixmap);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.width = drawWidth;
		this.height = drawHeight;
		this.region = new TextureRegion(texture);
		this.region.flip(false, true);
	}

	/**
	 * Returns the image width.
	 * @return the width
	 */
	public int getWidth() { return (int) width; }

	/**
	 * Returns the image height.
	 * @return the height
	 */
	public int getHeight() { return (int) height; }

	/**
	 * Sets the draw alpha.
	 * @param a the alpha [0, 1]
	 */
	public void setAlpha(float a) { this.alpha = a; }

	/**
	 * Draws at the given top-left position.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void draw(float x, float y) {
		draw(x, y, width, height);
	}

	/**
	 * Draws scaled to a box.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param w the width
	 * @param h the height
	 */
	public void draw(float x, float y, float w, float h) {
		SpriteBatch batch = Gfx.sprites();
		batch.setColor(1f, 1f, 1f, alpha);
		batch.draw(region, x, y, w, h);
		batch.setColor(1f, 1f, 1f, 1f);
	}

	/**
	 * Draws centered on a point.
	 * @param x the center x
	 * @param y the center y
	 */
	public void drawCentered(float x, float y) {
		draw(x - width / 2f, y - height / 2f);
	}

	/**
	 * Returns a scaled copy sharing the same texture.
	 * @param scale the scale factor
	 * @return the copy
	 */
	public Image getScaledCopy(float scale) {
		return new Image(texture, pixmap, width * scale, height * scale);
	}

	/**
	 * Returns a copy drawn at an explicit size.
	 * @param w the width
	 * @param h the height
	 * @return the copy
	 */
	public Image getScaledCopy(int w, int h) {
		return new Image(texture, pixmap, w, h);
	}

	/**
	 * Returns a graphics context that draws onto this image.
	 * @return the graphics
	 */
	public Graphics getGraphics() {
		return new Graphics(this);
	}

	/**
	 * Blits another image onto this image's pixmap.
	 * @param src the source
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	void blit(Image src, int x, int y) {
		if (pixmap == null || src.pixmap == null)
			return;
		pixmap.setBlending(Pixmap.Blending.SourceOver);
		pixmap.drawPixmap(src.pixmap, x, y);
	}

	/**
	 * Fills a rectangle on this image's pixmap.
	 */
	void fillPixmapRect(int x, int y, int w, int h, Color color) {
		if (pixmap == null)
			return;
		pixmap.setColor(color.r, color.g, color.b, color.a);
		pixmap.fillRectangle(x, y, w, h);
	}

	/**
	 * Re-uploads the pixmap after off-screen drawing.
	 */
	void upload() {
		if (pixmap == null)
			return;
		createTexture(width, height);
	}
}
