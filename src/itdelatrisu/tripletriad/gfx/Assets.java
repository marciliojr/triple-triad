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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.badlogic.gdx.files.FileHandle;

/**
 * Resolves game files from {@code res/} and {@code cards/} relative to the
 * working directory (project root).
 */
public final class Assets {
	// This class should not be instantiated.
	private Assets() {}

	/**
	 * Finds a resource by file name.
	 * @param name the file name (not a directory)
	 * @return the file (may not exist)
	 */
	public static File file(String name) {
		File res = new File("res", name);
		if (res.isFile())
			return res;
		File cards = new File("cards", name);
		if (cards.isFile())
			return cards;
		return new File(name);
	}

	/**
	 * Returns a libGDX file handle for a resource.
	 * @param name the file name
	 * @return the handle
	 */
	public static FileHandle handle(String name) {
		return new FileHandle(file(name));
	}

	/**
	 * Opens a resource as a stream.
	 * @param name the file name
	 * @return the stream
	 * @throws FileNotFoundException if missing
	 */
	public static InputStream open(String name) throws FileNotFoundException {
		return new FileInputStream(file(name));
	}
}
