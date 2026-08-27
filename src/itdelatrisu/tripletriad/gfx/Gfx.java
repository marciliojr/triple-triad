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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Shared libGDX rendering state (y-down, like Slick2D).
 */
public final class Gfx {
	/** Sprite batch. */
	private static SpriteBatch batch;

	/** Shape renderer. */
	private static ShapeRenderer shapes;

	/** Camera with origin at the top-left. */
	private static OrthographicCamera camera;

	/** Screen graphics wrapper. */
	private static Graphics graphics;

	/** Game container wrapper. */
	private static GameContainer container;

	/** Input wrapper. */
	private static Input input;

	/** Whether the sprite batch is begun. */
	private static boolean batching;

	/** Whether the shape renderer is begun. */
	private static boolean shaping;

	/** Current shape type, or null. */
	private static ShapeType shapeType;

	/** Global music volume. */
	private static float musicVolume = 1f;

	/** Global sound volume. */
	private static float soundVolume = 1f;

	// This class should not be instantiated.
	private Gfx() {}

	/**
	 * Creates GPU resources.
	 * @param width window width
	 * @param height window height
	 */
	public static void create(int width, int height) {
		camera = new OrthographicCamera();
		camera.setToOrtho(true, width, height);
		batch = new SpriteBatch();
		shapes = new ShapeRenderer();
		graphics = new Graphics();
		input = new Input();
		container = new GameContainer(width, height, input);
	}

	/**
	 * Starts a frame.
	 */
	public static void beginFrame() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shapes.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Ends a frame.
	 */
	public static void endFrame() {
		endShapes();
		endSprites();
	}

	/**
	 * Returns the sprite batch, beginning it if needed.
	 * @return the batch
	 */
	public static SpriteBatch sprites() {
		endShapes();
		if (!batching) {
			batch.begin();
			batching = true;
		}
		return batch;
	}

	/**
	 * Returns the shape renderer in the requested mode.
	 * @param type filled or line
	 * @return the renderer
	 */
	public static ShapeRenderer shapes(ShapeType type) {
		endSprites();
		if (shaping && shapeType != type)
			endShapes();
		if (!shaping) {
			Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
			try {
				shapes.begin(type);
			} catch (GdxRuntimeException e) {
				shapes.end();
				shapes.begin(type);
			}
			shaping = true;
			shapeType = type;
		}
		return shapes;
	}

	/**
	 * Ends the sprite batch if it is active.
	 */
	public static void endSprites() {
		if (batching) {
			batch.end();
			batching = false;
		}
	}

	/**
	 * Ends the shape renderer if it is active.
	 */
	public static void endShapes() {
		if (shaping) {
			shapes.end();
			shaping = false;
			shapeType = null;
		}
	}

	/**
	 * Returns the screen graphics context.
	 * @return the graphics
	 */
	public static Graphics getGraphics() { return graphics; }

	/**
	 * Returns the game container.
	 * @return the container
	 */
	public static GameContainer getContainer() { return container; }

	/**
	 * Returns the input helper.
	 * @return the input
	 */
	public static Input getInput() { return input; }

	/**
	 * Sets the global music volume.
	 * @param volume the volume [0, 1]
	 */
	public static void setMusicVolume(float volume) { musicVolume = volume; }

	/**
	 * Returns the global music volume.
	 * @return the volume
	 */
	public static float getMusicVolume() { return musicVolume; }

	/**
	 * Sets the global sound volume.
	 * @param volume the volume [0, 1]
	 */
	public static void setSoundVolume(float volume) { soundVolume = volume; }

	/**
	 * Returns the global sound volume.
	 * @return the volume
	 */
	public static float getSoundVolume() { return soundVolume; }

	/**
	 * Disposes GPU resources.
	 */
	public static void dispose() {
		endFrame();
		if (batch != null)
			batch.dispose();
		if (shapes != null)
			shapes.dispose();
		batch = null;
		shapes = null;
	}
}
