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

import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Log;

/**
 * Lazily loaded profile avatars ({@code res/avatars/avatar_1..8.png}).
 */
public final class Avatars {
	private static final Image[] IMAGES = new Image[Profile.AVATARS];
	private static boolean failed;

	private Avatars() {}

	/**
	 * Draws an avatar scaled into a box (keeps 4:5 ratio, centered).
	 * @param index 1-based avatar index
	 */
	public static void draw(int index, float x, float y, float w, float h) {
		Image img = get(index);
		if (img == null)
			return;
		float scale = Math.min(w / img.getWidth(), h / img.getHeight());
		float dw = img.getWidth() * scale, dh = img.getHeight() * scale;
		img.draw(x + (w - dw) / 2f, y + (h - dh) / 2f, dw, dh);
	}

	private static Image get(int index) {
		int i = Math.max(1, Math.min(Profile.AVATARS, index)) - 1;
		if (IMAGES[i] == null && !failed) {
			try {
				IMAGES[i] = new Image("avatars/avatar_" + (i + 1) + ".png");
			} catch (Exception e) {
				Log.error("Failed to load avatar " + (i + 1), e);
				failed = true;
			}
		}
		return IMAGES[i];
	}
}
