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

import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;

/**
 * A full-screen UI state (menu, profile, deck builder, ...).
 */
public abstract class Screen {
	/**
	 * Called when the screen becomes active.
	 */
	public void enter() {}

	/**
	 * Renders the screen.
	 * @param container the game container
	 * @param g the graphics context
	 */
	public abstract void render(GameContainer container, Graphics g);

	/**
	 * Updates the screen.
	 * @param container the game container
	 * @param delta milliseconds since last update
	 */
	public void update(GameContainer container, int delta) {}

	/**
	 * Handles a key press.
	 * @param key the Slick input key
	 * @param c the character
	 */
	public abstract void keyPressed(int key, char c);

	/**
	 * Handles a mouse press.
	 * @param button the mouse button
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void mousePressed(int button, int x, int y) {}

	/**
	 * Handles mouse wheel movement.
	 * @param change the wheel delta
	 */
	public void mouseWheelMoved(int change) {}
}
