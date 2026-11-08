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
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.ChampionshipSlot;
import itdelatrisu.tripletriad.Deck;
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
 * Championship save/load in the style of the FF8 (PS1) memory card screen:
 * INFO / HELP boxes, numbered gray blocks, hand cursor and page arrows.
 */
public class MemoryCardScreen extends Screen {
	/** Blocks shown per page. */
	private static final int PER_PAGE = 3;

	/** Page count. */
	private static final int PAGES = (Profile.SLOTS + PER_PAGE - 1) / PER_PAGE;

	/** Panel gradient top. */
	static final com.badlogic.gdx.graphics.Color PANEL_TOP = new com.badlogic.gdx.graphics.Color(0.50f, 0.50f, 0.54f, 1f);

	/** Panel gradient bottom. */
	static final com.badlogic.gdx.graphics.Color PANEL_BOTTOM = new com.badlogic.gdx.graphics.Color(0.24f, 0.24f, 0.27f, 1f);

	/** Inner (location) panel top. */
	static final com.badlogic.gdx.graphics.Color INNER_TOP = new com.badlogic.gdx.graphics.Color(0.40f, 0.40f, 0.44f, 1f);

	/** Inner (location) panel bottom. */
	static final com.badlogic.gdx.graphics.Color INNER_BOTTOM = new com.badlogic.gdx.graphics.Color(0.18f, 0.18f, 0.21f, 1f);

	/** Panel border. */
	private static final Color BORDER = new Color(0.82f, 0.82f, 0.86f);

	/** Box tag (INFO / HELP) color. */
	static final Color TAG = new Color(0.70f, 0.70f, 0.74f);

	/** Plain text. */
	private static final Color TEXT = Color.white;

	/** Game. */
	private final TripleTriad game;

	/** True = save mode, false = load mode. */
	private boolean saveMode;

	/** Selected block [0, SLOTS). */
	private int selected;

	/** Confirmation overlay: 0 none, 1 overwrite, 2 delete. */
	private int confirm;

	/** Highlighted confirm option (0 = No, 1 = Yes). */
	private int confirmChoice;

	/** Notice text and expiry. */
	private String notice;
	private long noticeUntil;

	/**
	 * Creates the screen.
	 * @param game the game
	 */
	public MemoryCardScreen(TripleTriad game) {
		this.game = game;
	}

	/**
	 * Opens the screen.
	 * @param save true to save, false to load
	 * @param preferred block to select first (-1 = first free / latest)
	 */
	public void open(boolean save, int preferred) {
		this.saveMode = save;
		this.confirm = 0;
		this.notice = null;
		Profile profile = game.getProfile();
		int start = preferred;
		if (start < 0 && profile != null)
			start = save ? profile.firstFreeSlot() : profile.latestSlot();
		this.selected = (start >= 0 && start < Profile.SLOTS) ? start : 0;
	}

	// ---------------------------------------------------------------- layout

	private float margin() { return Options.getWidth() * 0.035f; }

	private float arrowSpace() { return Options.getWidth() * 0.035f; }

	private float boxHeight() { return Options.getSmallFont().getLineHeight() * 1.9f; }

	private float listTop() { return Options.getHeight() * 0.03f + boxHeight() * 2f + gap() * 2f; }

	private float listBottom() { return Options.getHeight() * 0.90f; }

	private float gap() { return Options.getHeight() * 0.012f; }

	private float blockX() { return margin() + arrowSpace(); }

	private float blockW() { return Options.getWidth() - 2f * (margin() + arrowSpace()); }

	private float blockH() { return (listBottom() - listTop() - gap() * (PER_PAGE - 1)) / PER_PAGE; }

	private float blockY(int row) { return listTop() + row * (blockH() + gap()); }

	private int page() { return selected / PER_PAGE; }

	// ---------------------------------------------------------------- render

	@Override
	public void render(GameContainer container, Graphics g) {
		int width = container.getWidth();
		int height = container.getHeight();
		UnicodeFont small = Options.getSmallFont();
		g.setColor(new Color(0f, 0f, 0f));
		g.fillRect(0, 0, width, height);

		// header: INFO box + action title box
		float m = margin();
		float y0 = height * 0.03f;
		float bh = boxHeight();
		float titleW = width * 0.22f;
		float infoW = width - 2f * m - titleW - gap();
		drawPanel(g, m, y0, infoW, bh, PANEL_TOP, PANEL_BOTTOM);
		drawTagged(small, "INFO", I18n.memoryCardInUse(), m, y0, bh);
		float titleX = m + infoW + gap();
		drawPanel(g, titleX, y0, titleW, bh, PANEL_TOP, PANEL_BOTTOM);
		String title = saveMode ? I18n.memoryCardSaveTitle() : I18n.memoryCardLoadTitle();
		small.drawString(titleX + (titleW - small.getWidth(title)) / 2f,
			y0 + (bh - small.getLineHeight()) / 2f, title, TEXT);

		// HELP box
		float y1 = y0 + bh + gap();
		drawPanel(g, m, y1, width - 2f * m, bh, PANEL_TOP, PANEL_BOTTOM);
		drawTagged(small, helpTag(), I18n.memoryCardHelp(saveMode), m, y1, bh);

		// blocks
		Profile profile = game.getProfile();
		int first = page() * PER_PAGE;
		for (int row = 0; row < PER_PAGE; row++) {
			int index = first + row;
			if (index >= Profile.SLOTS)
				break;
			drawBlock(g, small, profile, index, blockY(row));
		}

		// hand cursor on the selected block
		Image hand = GameImage.CURSOR.getImage();
		float selY = blockY(selected - first);
		hand.draw(blockX() - hand.getWidth() * 0.85f, selY + small.getLineHeight() * 0.15f);

		// page arrows
		float midY = listTop() + (listBottom() - listTop()) / 2f;
		float aw = arrowSpace() * 0.45f;
		drawArrow(m + arrowSpace() * 0.30f, midY, aw, false, page() > 0);
		drawArrow(width - m - arrowSpace() * 0.30f, midY, aw, true, page() < PAGES - 1);

		// footer
		String pageText = (page() + 1) + "/" + PAGES;
		small.drawString(width - m - small.getWidth(pageText), height * 0.915f, pageText, TAG);
		Ui.drawCentered(small, I18n.hintMemoryCard(saveMode), height * 0.915f, Ui.HINT);
		if (notice != null && System.currentTimeMillis() < noticeUntil)
			Ui.drawCentered(small, notice, height * 0.955f, Ui.TITLE);

		if (confirm == 1)
			Ui.drawYesNoConfirm(g, I18n.memoryCardOverwrite(selected + 1), confirmChoice);
		else if (confirm == 2)
			Ui.drawYesNoConfirm(g, I18n.memoryCardDelete(selected + 1), confirmChoice);
	}

	private static String helpTag() {
		switch (Options.getLang()) {
			case EN: return "HELP";
			case ES: return "AYUDA";
			default: return "AJUDA";
		}
	}

	/**
	 * Draws one numbered block (occupied or free).
	 */
	private void drawBlock(Graphics g, UnicodeFont small, Profile profile, int index, float y) {
		float x = blockX();
		float w = blockW();
		float h = blockH();
		float line = small.getLineHeight();
		drawPanel(g, x, y, w, h, PANEL_TOP, PANEL_BOTTOM);
		String number = String.format("%02d", index + 1);
		small.drawString(x + line * 0.25f, y + line * 0.1f, number, TEXT);

		ChampionshipSlot slot = (profile != null) ? profile.getSlot(index) : null;
		if (slot == null) {
			String empty = saveMode ? I18n.memoryCardFree() : I18n.memoryCardEmpty();
			small.drawString(x + line * 1.2f, y + (h - line) / 2f + line * 0.3f, empty, TEXT);
			return;
		}

		// "portraits": up to three cards of the saved hand (or bag)
		float pad = h * 0.08f;
		float cardSize = h - pad * 2f;
		float cardsX = x + small.getWidth("00") + line * 0.6f;
		int[] ids = portraitIds(slot);
		Deck catalog = game.getDeck();
		for (int i = 0; i < ids.length; i++) {
			Card source = (catalog != null) ? catalog.getCardById(ids[i]) : null;
			if (source != null)
				new Card(source).drawSized(cardsX + i * cardSize * 0.92f, y + pad, cardSize, true, false);
		}

		// text column
		float textX = cardsX + cardSize * 0.92f * 3f + cardSize * 0.12f;
		float right = x + w - line * 0.5f;
		float row1 = y + pad;
		float row2 = row1 + line * 1.05f;
		small.drawString(textX, row1, I18n.memoryCardTitle(), TEXT);
		String play = I18n.memoryCardPlay(formatPlay(slot.getPlayMillis()));
		small.drawString(right - small.getWidth(play), row1, play, TEXT);
		small.drawString(textX, row2,
			I18n.memoryCardLevel(slot.getWins()) + "   " + I18n.memoryCardRound(slot.getRound()), TEXT);
		String cards = I18n.memoryCardCards(slot.getBag().size());
		small.drawString(right - small.getWidth(cards), row2, cards, TEXT);

		// location sub-panel (bottom right), like "Galbadia - Dingo Desert"
		float innerH = line * 1.35f;
		float innerY = y + h - pad - innerH;
		float innerX = textX - line * 0.3f;
		float innerW = x + w - pad - innerX;
		if (innerY > row2 + line * 0.9f) {
			drawPanel(g, innerX, innerY, innerW, innerH, INNER_TOP, INNER_BOTTOM);
			String place = Ui.fit(small, I18n.memoryCardPlace(slot.getRound()), innerW - line);
			small.drawString(innerX + line * 0.5f, innerY + (innerH - line) / 2f, place, TEXT);
		}
	}

	private static int[] portraitIds(ChampionshipSlot slot) {
		int[] hand = slot.getHand();
		int[] ids = new int[3];
		int n = 0;
		if (hand != null) {
			for (int i = 0; i < hand.length && n < 3; i++) {
				if (hand[i] > 0)
					ids[n++] = hand[i];
			}
		}
		for (int i = 0; i < slot.getBag().size() && n < 3; i++)
			ids[n++] = slot.getBag().get(i).intValue();
		int[] out = new int[n];
		System.arraycopy(ids, 0, out, 0, n);
		return out;
	}

	private static String formatPlay(long ms) {
		long total = ms / 1000L;
		return String.format("%d:%02d:%02d", total / 3600L, (total / 60L) % 60L, total % 60L);
	}

	/**
	 * Draws "TAG  text" inside a header box.
	 */
	private static void drawTagged(UnicodeFont small, String tag, String text, float x, float y, float h) {
		float line = small.getLineHeight();
		float ty = y + (h - line) / 2f;
		small.drawString(x + line * 0.4f, ty, tag, TAG);
		small.drawString(x + line * 0.4f + small.getWidth(tag + "   "), ty, text, TEXT);
	}

	/**
	 * Gray vertical-gradient panel with a light border (PS1 window style).
	 */
	static void drawPanel(Graphics g, float x, float y, float w, float h,
			com.badlogic.gdx.graphics.Color top, com.badlogic.gdx.graphics.Color bottom) {
		ShapeRenderer sr = Gfx.shapes(ShapeType.Filled);
		// y-down camera: (x, y) is the top-left corner
		sr.rect(x, y, w, h, top, top, bottom, bottom);
		g.setColor(BORDER);
		g.setLineWidth(2f);
		g.drawRect(x, y, w, h);
		g.setLineWidth(1f);
	}

	/**
	 * Small white page arrow (triangle).
	 */
	private static void drawArrow(float cx, float cy, float size, boolean right, boolean enabled) {
		ShapeRenderer sr = Gfx.shapes(ShapeType.Filled);
		float a = enabled ? 1f : 0.3f;
		sr.setColor(1f, 1f, 1f, a);
		float dx = right ? size / 2f : -size / 2f;
		sr.triangle(cx + dx, cy, cx - dx, cy - size * 0.6f, cx - dx, cy + size * 0.6f);
	}

	// ---------------------------------------------------------------- input

	@Override
	public void keyPressed(int key, char c) {
		if (confirm != 0) {
			keyConfirm(key);
			return;
		}
		switch (key) {
		case Input.KEY_UP:
			select((selected + Profile.SLOTS - 1) % Profile.SLOTS);
			break;
		case Input.KEY_DOWN:
			select((selected + 1) % Profile.SLOTS);
			break;
		case Input.KEY_LEFT:
			turnPage(-1);
			break;
		case Input.KEY_RIGHT:
			turnPage(1);
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate();
			break;
		case Input.KEY_D:
		case Input.KEY_DELETE:
			askDelete();
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			AudioController.Effect.BACK.play();
			game.memoryCardBack(saveMode);
			break;
		default:
			break;
		}
	}

	private void keyConfirm(int key) {
		switch (key) {
		case Input.KEY_UP:
		case Input.KEY_DOWN:
			confirmChoice = 1 - confirmChoice;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			resolveConfirm(confirmChoice == 1);
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			resolveConfirm(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		if (confirm != 0) {
			int hit = Ui.hitYesNoConfirm(y);
			if (hit >= 0)
				resolveConfirm(hit == 1);
			return;
		}
		if (y >= listTop() && y <= listBottom()) {
			if (x < blockX()) {
				turnPage(-1);
				return;
			}
			if (x > blockX() + blockW()) {
				turnPage(1);
				return;
			}
			int first = page() * PER_PAGE;
			for (int row = 0; row < PER_PAGE; row++) {
				float top = blockY(row);
				int index = first + row;
				if (index < Profile.SLOTS && y >= top && y < top + blockH()) {
					if (index != selected)
						select(index);
					else
						activate();
					return;
				}
			}
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (confirm != 0)
			return;
		if (change < 0)
			select(Math.min(Profile.SLOTS - 1, selected + 1));
		else if (change > 0)
			select(Math.max(0, selected - 1));
	}

	private void select(int index) {
		if (index == selected)
			return;
		selected = index;
		AudioController.playCursor();
	}

	private void turnPage(int dir) {
		int p = page() + dir;
		if (p < 0 || p >= PAGES) {
			AudioController.Effect.INVALID.play();
			return;
		}
		int row = selected % PER_PAGE;
		selected = Math.min(Profile.SLOTS - 1, p * PER_PAGE + row);
		AudioController.playCursor();
	}

	private ChampionshipSlot selectedSlot() {
		Profile profile = game.getProfile();
		return (profile != null) ? profile.getSlot(selected) : null;
	}

	private void activate() {
		ChampionshipSlot slot = selectedSlot();
		if (saveMode) {
			if (slot != null) {
				confirm = 1;
				confirmChoice = 0;
				AudioController.Effect.SELECT.play();
				return;
			}
			doSave();
			return;
		}
		if (slot == null) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.championshipLoad(selected);
	}

	private void askDelete() {
		if (selectedSlot() == null) {
			AudioController.Effect.INVALID.play();
			return;
		}
		confirm = 2;
		confirmChoice = 0;
		AudioController.Effect.SELECT.play();
	}

	private void resolveConfirm(boolean yes) {
		int kind = confirm;
		confirm = 0;
		confirmChoice = 0;
		if (!yes) {
			AudioController.Effect.BACK.play();
			return;
		}
		if (kind == 1)
			doSave();
		else if (kind == 2) {
			game.championshipDeleteSlot(selected);
			AudioController.Effect.SELECT.play();
			showNotice(I18n.memoryCardDeleted(selected + 1));
		}
	}

	private void doSave() {
		if (!game.canChampionshipSave()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		game.championshipSave(selected);
		AudioController.Effect.SELECT.play();
		showNotice(I18n.memoryCardSaved(selected + 1));
	}

	private void showNotice(String text) {
		notice = text;
		noticeUntil = System.currentTimeMillis() + 2500L;
	}
}
