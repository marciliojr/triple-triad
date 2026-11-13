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

/**
 * Tournament stops on {@code story/backgrounds/mapa_mundi.png}.
 * Points are in the art's own 640x360 space (story-assets/backgrounds/MANIFESTO_extras.txt):
 * Deling 94,91; Timber 247,116; Dollet 69,247; Balamb 314,200; FH 398,175; Esthar 541,125.
 * Garden, Balamb Town and the final share the Balamb island, so they sit a little apart.
 * The map has no labels: the game writes the names.
 */
public final class StoryMap {
	public static final String BACKGROUND = "mapa_mundi.png";

	/** Size of the art the points refer to. */
	public static final int WIDTH = 640, HEIGHT = 360;

	/** Where the name goes, relative to the marker. */
	public enum Label { ABOVE, BELOW, LEFT, RIGHT }

	/** One stop: region id, point in 640x360, label side. */
	public static final class Spot {
		public final String region;
		public final int x, y;
		public final Label label;

		Spot(String region, int x, int y, Label label) {
			this.region = region;
			this.x = x;
			this.y = y;
			this.label = label;
		}
	}

	private static final Spot[] SPOTS = {
		new Spot("garden", 314, 192, Label.ABOVE),
		new Spot("town", 336, 212, Label.RIGHT),
		new Spot("timber", 247, 116, Label.ABOVE),
		new Spot("deling", 94, 91, Label.BELOW),
		new Spot("dollet", 69, 247, Label.BELOW),
		new Spot("fh", 398, 175, Label.BELOW),
		new Spot("esthar", 541, 125, Label.BELOW),
		new Spot("final", 294, 212, Label.LEFT)
	};

	private StoryMap() {}

	/** @return the spot of a region id, or null */
	public static Spot spot(String regionId) {
		if (regionId == null)
			return null;
		for (int i = 0; i < SPOTS.length; i++) {
			if (SPOTS[i].region.equals(regionId))
				return SPOTS[i];
		}
		return null;
	}

	/**
	 * Maps a spot into a letterboxed area of {@code w x h} at {@code (ox, oy)},
	 * with the same scale the background uses.
	 * @return {x, y} on screen
	 */
	public static float[] toScreen(Spot spot, float ox, float oy, float w, float h) {
		float scale = Math.min(w / WIDTH, h / HEIGHT);
		float left = ox + (w - WIDTH * scale) / 2f;
		float top = oy + (h - HEIGHT * scale) / 2f;
		return new float[] { left + spot.x * scale, top + spot.y * scale };
	}

	/**
	 * @return the index of the region whose marker is under the point, or -1
	 */
	public static int hit(float px, float py, float w, float h, float radius) {
		int best = -1;
		float bestD = radius * radius;
		for (int i = 0; i < StoryCampaign.REGIONS.length; i++) {
			Spot s = spot(StoryCampaign.REGIONS[i].id);
			if (s == null)
				continue;
			float[] p = toScreen(s, 0, 0, w, h);
			float dx = p[0] - px, dy = p[1] - py;
			float d = dx * dx + dy * dy;
			if (d <= bestD) {
				bestD = d;
				best = i;
			}
		}
		return best;
	}
}
