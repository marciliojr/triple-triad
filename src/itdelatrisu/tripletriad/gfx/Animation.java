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
 * Frame animation (Slick {@code Animation} stand-in).
 */
public class Animation {
	/** Frames. */
	private final Image[] frames;

	/** Duration per frame at speed 1, in milliseconds. */
	private final int frameDuration;

	/** Playback speed multiplier. */
	private float speed = 1f;

	/** Elapsed time in the current cycle. */
	private long elapsed;

	/** Time of last draw. */
	private long lastTime = -1;

	/**
	 * Constructor.
	 * @param frames the frames
	 * @param duration milliseconds per frame
	 */
	public Animation(Image[] frames, int duration) {
		this.frames = frames;
		this.frameDuration = Math.max(1, duration);
	}

	/**
	 * Sets the playback speed.
	 * @param speed the speed (1 = default)
	 */
	public void setSpeed(float speed) {
		this.speed = (speed <= 0f) ? 0.01f : speed;
	}

	/**
	 * Returns a frame by index.
	 * @param frame the index
	 * @return the image
	 */
	public Image getImage(int frame) {
		if (frames == null || frame < 0 || frame >= frames.length)
			return null;
		return frames[frame];
	}

	/**
	 * Returns the frame width.
	 * @return the width
	 */
	public int getWidth() {
		return (frames != null && frames[0] != null) ? frames[0].getWidth() : 0;
	}

	/**
	 * Returns the frame height.
	 * @return the height
	 */
	public int getHeight() {
		return (frames != null && frames[0] != null) ? frames[0].getHeight() : 0;
	}

	/**
	 * Draws the current frame.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void draw(float x, float y) {
		Image frame = current();
		if (frame != null)
			frame.draw(x, y);
	}

	/**
	 * Returns the current frame based on elapsed time.
	 */
	private Image current() {
		if (frames == null || frames.length == 0)
			return null;
		long now = System.currentTimeMillis();
		if (lastTime > 0)
			elapsed += (long) ((now - lastTime) * speed);
		lastTime = now;
		int duration = (int) (frameDuration / speed);
		if (duration < 1)
			duration = 1;
		int index = (int) ((elapsed / duration) % frames.length);
		return frames[index];
	}
}
