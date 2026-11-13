/*
 * Triple Triad - a card game from FFVIII
 * Copyright (C) 2014 Jeffrey Han
 *
 * Triple Triad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package itdelatrisu.tripletriad.story;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import itdelatrisu.tripletriad.gfx.Assets;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Log;

/**
 * Loads story images from {@code res/story/} and checks whether a VideoLoop
 * clip is present. Missing files stay missing; the campaign keeps running.
 */
public final class StoryAssets {
	/** Frame rate used when the mp4 was converted for {@code gfx.VideoLoop}. */
	public static final float VIDEO_FPS = 24f;

	private static final Map<String, Image> CACHE = new HashMap<String, Image>();
	private static final Set<String> MISSING = new HashSet<String>();

	private StoryAssets() {}

	/**
	 * A clip is present when the first extracted frame exists.
	 * The campaign asks this at playback time, so a video added later
	 * starts working without a code change.
	 * @param videoId folder name under {@code res/story/}, or null
	 */
	public static boolean hasVideo(String videoId) {
		if (videoId == null || videoId.isEmpty())
			return false;
		return Assets.file("story/" + videoId + "/frame_001.jpg").isFile();
	}

	/** @return true when the clip has an extracted audio track */
	public static boolean hasVideoAudio(String videoId) {
		if (videoId == null || videoId.isEmpty())
			return false;
		return Assets.file("story/" + videoId + ".ogg").isFile();
	}

	/** @return the image, or null if the file is missing or unreadable */
	public static Image image(String path) {
		if (path == null || MISSING.contains(path))
			return null;
		Image cached = CACHE.get(path);
		if (cached != null)
			return cached;
		if (!Assets.file(path).isFile()) {
			MISSING.add(path);
			return null;
		}
		try {
			Image loaded = new Image(path);
			CACHE.put(path, loaded);
			return loaded;
		} catch (Exception e) {
			Log.error("Failed to load story image " + path, e);
			MISSING.add(path);
			return null;
		}
	}

	/** @return a portrait, preferring the defeat face when requested */
	public static Image portrait(String id, boolean defeat) {
		if (id == null || id.isEmpty())
			return null;
		if (defeat) {
			Image lost = image("story/portraits/" + id + "_derrota.png");
			if (lost != null)
				return lost;
		}
		Image normal = image("story/portraits/" + id + "_normal.png");
		if (normal != null)
			return normal;
		return image("story/portraits/" + id + ".png");
	}

	public static Image background(String file) {
		return image("story/backgrounds/" + file);
	}

	public static Image ui(String file) {
		return image("story/ui/" + file);
	}
}
