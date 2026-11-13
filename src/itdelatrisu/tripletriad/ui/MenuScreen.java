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

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Gfx;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Main menu: profile panel and twelve entries (ending in About and Quit), drawn as FFVIII windows.
 * Keys here are the canonical codes {@code Controls} already translated.
 */
public class MenuScreen extends Screen {
	/** Number of menu entries. */
	static final int ITEM_COUNT = 12;

	/** Index of the About entry. */
	static final int ITEM_ABOUT = 10;

	/** Index of the Quit entry (last). */
	static final int ITEM_EXIT = 11;

	/** How long the open slide takes, in milliseconds. */
	private static final float OPEN_MS = 320f;

	/** Game instance. */
	private final TripleTriad game;

	/** Selected menu index. */
	private int selected;

	/** True when the profile panel has the cursor. */
	private boolean profileFocus;

	/** Open transition, 0 to 1. */
	private float open;

	/** Animation clock, in milliseconds. */
	private float anim;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public MenuScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		selected = 0;
		profileFocus = false;
		open = 0f;
	}

	@Override
	public void update(GameContainer container, int delta) {
		anim += delta;
		if (open < 1f)
			open = Math.min(1f, open + delta / OPEN_MS);
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		int width = container.getWidth();
		int height = container.getHeight();
		Layout layout = measure();
		float alpha = smooth(open);

		drawBackground(g, width, height);
		drawCards(width, height, alpha);
		drawProfile(g, layout, alpha);
		drawMenu(g, layout, alpha);
		HintBar.draw(I18n.hintMenu(), height * 0.90f);
	}

	@Override
	public void keyPressed(int key, char c) {
		if (profileFocus) {
			if (key == Input.KEY_DOWN || key == Input.KEY_RIGHT) {
				profileFocus = false;
				AudioController.playCursor();
			} else if (key == Input.KEY_Z || key == Input.KEY_ENTER)
				openProfiles();
			return;
		}
		switch (key) {
		case Input.KEY_DOWN:
			selected = (selected + 1) % ITEM_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			selected = (selected + ITEM_COUNT - 1) % ITEM_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_LEFT:
			profileFocus = true;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate(selected);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		Layout layout = measure();
		if (layout.hitsProfile(x, y)) {
			if (!profileFocus) {
				profileFocus = true;
				AudioController.playCursor();
			} else
				openProfiles();
			return;
		}
		int index = layout.hitRow(x, y);
		if (index < 0)
			return;
		profileFocus = false;
		if (index != selected) {
			selected = index;
			AudioController.playCursor();
			return;
		}
		activate(index);
	}

	private void openProfiles() {
		if (game.getProfile() == null || !game.getProfile().isValid()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.showProfileSelectFromMenu();
	}

	/**
	 * Activates a menu entry.
	 */
	private void activate(int index) {
		AudioController.Effect.SELECT.play();
		switch (index) {
			case 0: game.showDeckSelect(); break;
			case 1: game.showChampionship(); break;
			case 2: game.showStory(); break;
			case 3: game.showVersus(); break;
			case 4: game.showMyDeck(); break;
			case 5: game.showTrophies(); break;
			case 6: game.showSave(); break;
			case 7: game.showHowToPlay(); break;
			case 8: game.showSettings(); break;
			case 9: game.showCheats(); break;
			case ITEM_ABOUT: game.showAbout(); break;
			case ITEM_EXIT: game.requestExit(); break;
			default: break;
		}
	}

	private String label(int index) {
		switch (index) {
			case 0: return I18n.menuQuick();
			case 1: return I18n.menuChampionship();
			case 2: return I18n.menuStory();
			case 3: return I18n.menuVersus();
			case 4: return I18n.menuMyDeck();
			case 5: return I18n.menuTrophies();
			case 6: return I18n.menuSave();
			case 7: return I18n.menuHowTo();
			case 8: return I18n.menuSettings();
			case 9: return I18n.menuCheats();
			case ITEM_ABOUT: return I18n.menuAbout();
			case ITEM_EXIT: return I18n.menuExit();
			default: return "";
		}
	}

	/** Board background, slightly larger than the screen so it can drift. */
	private void drawBackground(Graphics g, int width, int height) {
		Image bg = GameImage.BOARD_MAT.getImage();
		if (bg != null && bg.getWidth() > 0 && bg.getHeight() > 0) {
			float cover = Math.max(width / (float) bg.getWidth(), height / (float) bg.getHeight()) * 1.08f;
			float dw = bg.getWidth() * cover;
			float dh = bg.getHeight() * cover;
			float ox = (float) Math.sin(anim * 0.00022f) * width * 0.012f;
			float oy = (float) Math.cos(anim * 0.00016f) * height * 0.012f;
			bg.draw((width - dw) / 2f + ox, (height - dh) / 2f + oy, dw, dh);
		} else {
			g.setColor(new Color(0.02f, 0.04f, 0.10f));
			g.fillRect(0, 0, width, height);
		}
		g.setColor(new Color(0.02f, 0.05f, 0.12f, 0.62f));
		g.fillRect(0, 0, width, height);
	}

	/** A few card backs drifting behind the windows. Uses the shared texture. */
	private void drawCards(int width, int height, float alpha) {
		Image card = GameImage.CARD_BACK.getImage();
		if (card == null)
			return;
		float size = height * 0.125f;
		for (int i = 0; i < 4; i++) {
			float t = anim * 0.0002f + i * 1.7f;
			float nx = (i == 0) ? 0.03f : (i == 1) ? 0.84f : (i == 2) ? 0.72f : 0.05f;
			float ny = (i == 0) ? 0.58f : (i == 1) ? 0.68f : (i == 2) ? 0.04f : 0.78f;
			float x = width * nx + (float) Math.sin(t) * width * 0.012f;
			float y = height * ny + (float) Math.cos(t * 0.8f) * height * 0.014f;
			card.setAlpha(0.22f * alpha);
			card.draw(x, y, size, size);
		}
		card.setAlpha(1f);
	}

	private void drawProfile(Graphics g, Layout layout, float alpha) {
		window(g, layout.px, layout.py, layout.pw, layout.ph, profileFocus, alpha);
		float line = layout.small.getLineHeight();
		float pad = line * 0.35f;
		float frameH = layout.ph - pad * 2f;
		float frameW = frameH * 0.8f;
		float frameX = layout.px + pad;
		float frameY = layout.py + pad;
		window(g, frameX, frameY, frameW, frameH, false, alpha);
		float inset = Math.max(3f, line * 0.12f);
		Profile profile = game.getProfile();
		int avatar = (profile != null) ? profile.getAvatar() : 1;
		boolean drawn = Avatars.draw(avatar, frameX + inset, frameY + inset,
			frameW - inset * 2f, frameH - inset * 2f, alpha);
		if (!drawn)
			placeholder(frameX, frameY, frameW, frameH, alpha);

		float textX = frameX + frameW + pad;
		float textW = layout.px + layout.pw - pad - textX;
		String name = (profile != null && profile.isValid()) ? profile.getName() : "";
		UnicodeFont nameFont = layout.small;
		String shown = name;
		if (textW > 8f && nameFont.getWidth(name) > textW) {
			nameFont = layout.tiny;
			if (nameFont.getWidth(name) > textW)
				shown = Ui.fit(nameFont, name, textW);
		}
		String meta = "";
		if (profile != null && profile.isValid())
			meta = I18n.menuProfileMeta(profile.getCollection().size(), profile.getChampionshipWins());
		UnicodeFont metaFont = layout.tiny;
		if (textW > 8f)
			meta = Ui.fit(metaFont, meta, textW);
		float block = nameFont.getLineHeight() + metaFont.getLineHeight() * 1.15f;
		float textY = frameY + (frameH - block) / 2f;
		if (profileFocus) {
			Image cursor = GameImage.CURSOR.getImage();
			float ch = Math.min(frameH * 0.28f, nameFont.getLineHeight());
			float cw = cursorHeightToWidth(cursor, ch);
			float bob = (float) Math.sin(anim * 0.009f) * 3f;
			cursor.setAlpha(alpha);
			cursor.draw(textX + bob, textY + (nameFont.getLineHeight() - ch) / 2f, cw, ch);
			cursor.setAlpha(1f);
			textX += cw + pad * 0.45f;
			textW = layout.px + layout.pw - pad - textX;
			if (textW > 8f) {
				shown = name;
				if (nameFont.getWidth(shown) > textW)
					shown = Ui.fit(nameFont, name, textW);
				meta = Ui.fit(metaFont, metaSource(profile), textW);
			}
		}
		outline(nameFont, textX, textY, shown, new Color(1f, 1f, 1f, alpha));
		metaFont.drawString(textX, textY + nameFont.getLineHeight() * 1.05f, meta,
			new Color(0.78f, 0.86f, 0.98f, alpha));
	}

	private String metaSource(Profile profile) {
		if (profile == null || !profile.isValid())
			return "";
		return I18n.menuProfileMeta(profile.getCollection().size(), profile.getChampionshipWins());
	}

	private void drawMenu(Graphics g, Layout layout, float alpha) {
		window(g, layout.mx, layout.my, layout.mw, layout.mh, false, alpha);
		UnicodeFont titleFont = layout.title;
		String title = Ui.fit(titleFont, "TRIPLE TRIAD", layout.mw - layout.small.getLineHeight());
		float titleY = layout.my + (layout.header - titleFont.getLineHeight()) / 2f;
		outline(titleFont, layout.mx + (layout.mw - titleFont.getWidth(title)) / 2f, titleY, title,
			new Color(Ui.TITLE.r, Ui.TITLE.g, Ui.TITLE.b, alpha));
		g.setColor(new Color(0.75f, 0.86f, 1f, 0.55f * alpha));
		g.fillRect(layout.mx + 8f, layout.my + layout.header - 2f, layout.mw - 16f, 2f);

		UnicodeFont itemFont = layout.item;
		Image cursor = GameImage.CURSOR.getImage();
		float ch = Math.min(layout.rowH * 0.62f, itemFont.getLineHeight() * 1.15f);
		float cw = cursorHeightToWidth(cursor, ch);
		float bob = (float) Math.sin(anim * 0.009f) * 3.5f;
		float textPad = layout.small.getLineHeight() * 0.45f;
		float textX = layout.mx + textPad + cw + textPad * 0.35f;
		float textW = layout.mx + layout.mw - textPad - textX;
		for (int i = 0; i < ITEM_COUNT; i++) {
			float y = layout.rowTop + i * layout.rowH;
			boolean on = !profileFocus && i == selected;
			if (on) {
				float pulse = 0.22f + 0.10f * (float) Math.sin(anim * 0.006f);
				g.setColor(new Color(0.55f, 0.75f, 1f, pulse * alpha));
				g.fillRect(layout.mx + 6f, y + 1f, layout.mw - 12f, layout.rowH - 2f);
				g.setColor(new Color(Ui.TITLE.r, Ui.TITLE.g, Ui.TITLE.b, 0.9f * alpha));
				g.fillRect(layout.mx + 6f, y + 1f, 3f, layout.rowH - 2f);
			}
			String shown = Ui.fit(itemFont, label(i), Math.max(8f, textW));
			float ty = y + (layout.rowH - itemFont.getLineHeight()) / 2f;
			Color color = on ? new Color(1f, 1f, 1f, alpha) : new Color(0.74f, 0.82f, 0.94f, alpha);
			if (on)
				outline(itemFont, textX, ty, shown, color);
			else
				itemFont.drawString(textX, ty, shown, color);
			if (on) {
				cursor.setAlpha(alpha);
				cursor.draw(layout.mx + textPad + bob, ty + (itemFont.getLineHeight() - ch) / 2f, cw, ch);
				cursor.setAlpha(1f);
			}
		}
	}

	/** Dark blue window with a light border, in the FFVIII menu style. */
	private void window(Graphics g, float x, float y, float w, float h, boolean hot, float alpha) {
		drawWindow(g, x, y, w, h, hot, alpha);
	}

	/**
	 * Dark blue window with a light border, in the FFVIII menu style.
	 * Shared with the quit confirmation and the About screen.
	 */
	public static void drawWindow(Graphics g, float x, float y, float w, float h, boolean hot, float alpha) {
		com.badlogic.gdx.graphics.Color top = new com.badlogic.gdx.graphics.Color(0.20f, 0.34f, 0.66f, 0.94f * alpha);
		com.badlogic.gdx.graphics.Color bottom = new com.badlogic.gdx.graphics.Color(0.04f, 0.08f, 0.24f, 0.96f * alpha);
		ShapeRenderer sr = Gfx.shapes(ShapeType.Filled);
		sr.rect(x, y, w, h, top, top, bottom, bottom);
		g.setColor(new Color(0.55f, 0.72f, 0.95f, 0.28f * alpha));
		g.fillRect(x + 4f, y + 4f, w - 8f, 2f);
		g.setColor(hot ? new Color(0.96f, 0.98f, 1f, alpha) : new Color(0.78f, 0.88f, 1f, alpha));
		g.setLineWidth(2f);
		g.drawRect(x, y, w, h);
		g.setColor(new Color(0.40f, 0.58f, 0.86f, 0.8f * alpha));
		g.setLineWidth(1f);
		g.drawRect(x + 3f, y + 3f, w - 6f, h - 6f);
	}

	/** Neutral bust used when the portrait file is missing. */
	private void placeholder(float x, float y, float w, float h, float alpha) {
		ShapeRenderer sr = Gfx.shapes(ShapeType.Filled);
		sr.setColor(0.62f, 0.74f, 0.90f, 0.95f * alpha);
		float cx = x + w / 2f;
		sr.circle(cx, y + h * 0.38f, Math.min(w, h) * 0.14f);
		float bodyW = w * 0.56f;
		sr.rect(cx - bodyW / 2f, y + h * 0.58f, bodyW, h * 0.24f);
	}

	private static void outline(UnicodeFont font, float x, float y, String text, Color color) {
		if (text == null || text.isEmpty())
			return;
		Color shade = new Color(0.02f, 0.04f, 0.10f, 0.9f * color.a);
		font.drawString(x + 1f, y, text, shade);
		font.drawString(x - 1f, y, text, shade);
		font.drawString(x, y + 1f, text, shade);
		font.drawString(x, y - 1f, text, shade);
		font.drawString(x, y, text, color);
	}

	private static float cursorHeightToWidth(Image cursor, float height) {
		if (cursor == null || cursor.getHeight() <= 0)
			return height;
		return height * cursor.getWidth() / (float) cursor.getHeight();
	}

	private static float smooth(float t) {
		t = Math.max(0f, Math.min(1f, t));
		return t * t * (3f - 2f * t);
	}

	/** Positions for this frame. The open slide is included so clicks match the art. */
	private Layout measure() {
		int width = Options.getWidth();
		int height = Options.getHeight();
		UnicodeFont title = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		UnicodeFont tiny = Options.getTinyFont();
		float margin = Math.max(12f, width * 0.03f);
		float footer = Math.max(height * 0.16f, small.getLineHeight() * 5.6f);
		float bottom = height - footer;
		float gap = Math.max(10f, small.getLineHeight() * 0.45f);
		float slide = (1f - smooth(open)) * height * 0.02f;

		float profileH = Math.min(height * 0.22f, Math.max(small.getLineHeight() * 5.1f, height * 0.18f));
		float profileW = Math.min(width - margin * 2f, Math.max(width * 0.34f, small.getLineHeight() * 14f));
		boolean side = width - margin * 2f - profileW - gap >= small.getLineHeight() * 12f;

		Layout layout = new Layout();
		layout.title = title;
		layout.small = small;
		layout.tiny = tiny;
		layout.item = small;
		layout.px = margin;
		layout.py = margin + slide;
		layout.pw = side ? profileW : width - margin * 2f;
		layout.ph = profileH;

		float areaTop = side ? margin : margin + profileH + gap;
		float areaH = Math.max(small.getLineHeight() * 4f, bottom - areaTop);
		layout.header = title.getLineHeight() * 1.35f;
		float rowsRoom = Math.max(small.getLineHeight(), areaH - layout.header);
		float rowMax = small.getLineHeight() * 1.85f;
		layout.rowH = Math.min(rowMax, rowsRoom / ITEM_COUNT);
		if (layout.rowH < small.getLineHeight() * 1.05f)
			layout.item = tiny;
		float menuH = layout.header + layout.rowH * ITEM_COUNT;
		if (menuH > areaH) {
			layout.rowH = (areaH - layout.header) / ITEM_COUNT;
			menuH = areaH;
		}
		layout.mw = side ? width - margin - (layout.px + layout.pw + gap) : width - margin * 2f;
		layout.mx = side ? layout.px + layout.pw + gap : margin;
		layout.my = areaTop + slide;
		layout.mh = menuH;
		layout.rowTop = layout.my + layout.header;
		return layout;
	}

	/** Menu geometry for one frame. */
	private static final class Layout {
		float px, py, pw, ph;
		float mx, my, mw, mh;
		float header, rowH, rowTop;
		UnicodeFont title, small, tiny, item;

		boolean hitsProfile(int x, int y) {
			return x >= px && x <= px + pw && y >= py && y <= py + ph;
		}

		int hitRow(int x, int y) {
			if (x < mx || x > mx + mw || y < rowTop)
				return -1;
			int i = (int) ((y - rowTop) / rowH);
			return (i >= 0 && i < ITEM_COUNT) ? i : -1;
		}
	}
}
