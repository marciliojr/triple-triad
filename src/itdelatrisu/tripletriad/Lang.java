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

package itdelatrisu.tripletriad;

/**
 * UI language.
 */
public enum Lang {
	PT_BR,
	EN,
	ES;

	/**
	 * Returns the next language in the cycle.
	 * @return the next language
	 */
	public Lang next() {
		Lang[] all = values();
		return all[(ordinal() + 1) % all.length];
	}

	/**
	 * Returns the previous language in the cycle.
	 * @return the previous language
	 */
	public Lang prev() {
		Lang[] all = values();
		return all[(ordinal() + all.length - 1) % all.length];
	}
}
