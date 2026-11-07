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

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Looping video made of pre-extracted JPEG frames ({@code res/<name>/frame_NNN.jpg})
 * with an optional audio track ({@code res/<name>.ogg}).
 * <p>
 * libGDX has no built-in video decoder, so the source video ({@code res/<name>.mp4})
 * is converted offline with ffmpeg. Frames stay compressed in memory and are decoded
 * one at a time into a single texture.
 */
public class VideoLoop {
	/** Encoded JPEG frames. */
	private final List<byte[]> frames = new ArrayList<byte[]>();

	/** Frames per second. */
	private final float fps;

	/** Looping audio track, or null. */
	private com.badlogic.gdx.audio.Music audio;

	/** Texture holding the current frame. */
	private Texture texture;

	/** Flipped region (y-down camera). */
	private TextureRegion region;

	/** Index of the frame uploaded to the texture. */
	private int shownFrame = -1;

	/** Playback time in seconds. */
	private float time;

	/** Whether playback is running. */
	private boolean playing;

	/**
	 * Loads the frames and audio of a video.
	 * @param name the base name (e.g. {@code endgame})
	 * @param fps the frame rate of the extracted frames
	 * @throws SlickException if no frame can be loaded
	 */
	public VideoLoop(String name, float fps) throws SlickException {
		this.fps = fps;
		try {
			for (int i = 1; ; i++) {
				File f = Assets.file(String.format("%s/frame_%03d.jpg", name, i));
				if (!f.isFile())
					break;
				frames.add(Files.readAllBytes(f.toPath()));
			}
		} catch (Exception e) {
			throw new SlickException("Failed to load video " + name, e);
		}
		if (frames.isEmpty())
			throw new SlickException("No frames found for video " + name);
		File sound = Assets.file(name + ".ogg");
		if (sound.isFile()) {
			try {
				audio = Gdx.audio.newMusic(Assets.handle(name + ".ogg"));
				audio.setLooping(true);
			} catch (Exception e) {
				Log.error("Failed to load video audio " + name, e);
				audio = null;
			}
		}
	}

	/**
	 * Starts (or restarts) looping playback.
	 * @param withAudio whether to play the audio track
	 */
	public void play(boolean withAudio) {
		time = 0f;
		playing = true;
		if (audio != null && withAudio) {
			audio.setVolume(Gfx.getMusicVolume());
			audio.play();
		}
	}

	/**
	 * Stops playback.
	 */
	public void stop() {
		playing = false;
		if (audio != null)
			audio.stop();
	}

	/** @return whether playback is running */
	public boolean isPlaying() { return playing; }

	/**
	 * Draws the current frame letterboxed into a rectangle.
	 * @return false if the frame could not be decoded
	 */
	public boolean draw(float x, float y, float w, float h) {
		if (!playing)
			return false;
		time += Gdx.graphics.getDeltaTime();
		int index = ((int) (time * fps)) % frames.size();
		if (index != shownFrame && !upload(index))
			return false;
		float scale = Math.min(w / texture.getWidth(), h / texture.getHeight());
		float dw = texture.getWidth() * scale, dh = texture.getHeight() * scale;
		SpriteBatch batch = Gfx.sprites();
		batch.setColor(1f, 1f, 1f, 1f);
		batch.draw(region, x + (w - dw) / 2f, y + (h - dh) / 2f, dw, dh);
		return true;
	}

	/**
	 * Decodes a frame into the texture.
	 */
	private boolean upload(int index) {
		Pixmap pixmap = null;
		Gfx.endSprites();
		try {
			byte[] data = frames.get(index);
			pixmap = new Pixmap(data, 0, data.length);
			if (texture == null || texture.getWidth() != pixmap.getWidth() || texture.getHeight() != pixmap.getHeight()) {
				if (texture != null)
					texture.dispose();
				texture = new Texture(pixmap);
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				region = new TextureRegion(texture);
				region.flip(false, true);
			} else
				texture.draw(pixmap, 0, 0);
			shownFrame = index;
			return true;
		} catch (Exception e) {
			Log.error("Failed to decode video frame " + index, e);
			return false;
		} finally {
			if (pixmap != null)
				pixmap.dispose();
		}
	}

	/**
	 * Frees the texture and audio.
	 */
	public void dispose() {
		stop();
		Gfx.endSprites();
		if (texture != null)
			texture.dispose();
		texture = null;
		if (audio != null)
			audio.dispose();
		audio = null;
	}
}
