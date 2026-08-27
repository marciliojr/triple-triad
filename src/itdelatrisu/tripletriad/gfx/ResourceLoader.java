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

import java.io.InputStream;

/**
 * Resource lookup compatible with Slick {@code ResourceLoader}.
 */
public final class ResourceLoader {
	// This class should not be instantiated.
	private ResourceLoader() {}

	/**
	 * Opens a named resource from {@code res/} or {@code cards/}.
	 * @param ref the file name
	 * @return the stream
	 */
	public static InputStream getResourceAsStream(String ref) {
		try {
			return Assets.open(ref);
		} catch (Exception e) {
			Log.error(String.format("Missing resource '%s'.", ref), e);
			return null;
		}
	}
}
