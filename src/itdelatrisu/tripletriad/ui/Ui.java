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

import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Shared drawing helpers for menu screens.
 */
public final class Ui {
	/** Dim overlay. */
	private static final Color DIM = new Color(0f, 0f, 0f, 0.5f);

	/** Title color. */
	public static final Color TITLE = new Color(1f, 0.92f, 0.55f);

	/** Hint / secondary text. */
	public static final Color HINT = new Color(0.85f, 0.85f, 0.85f);

	/** Disabled menu item. */
	public static final Color DISABLED = new Color(0.55f, 0.55f, 0.55f);

	/** Selected menu item. */
	public static final Color SELECTED = Color.white;

	// This class should not be instantiated.
	private Ui() {}

	/**
	 * Draws the board background with a dark overlay.
	 * @param g the graphics context
	 */
	public static void drawBackdrop(Graphics g) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		GameImage.BOARD_MAT.getImage().drawCentered(width / 2, height / 2);
		g.setColor(DIM);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Draws a string centered horizontally.
	 */
	public static void drawCentered(UnicodeFont font, String text, float y, Color color) {
		if (text == null)
			text = "";
		float x = (Options.getWidth() - font.getWidth(text)) / 2f;
		font.drawString(x, y, text, color);
	}

	/**
	 * Returns whether a character can be used in a profile or deck name.
	 * @param c the character
	 * @return true if allowed
	 */
	public static boolean isNameChar(char c) {
		return Character.isLetterOrDigit(c) || c == ' ' || c == '-' || c == '_';
	}

	/**
	 * Draws a full-size card preview over a dimmed screen.
	 * @param g the graphics context
	 * @param catalog the card catalog
	 * @param cardId the card ID
	 */
	public static void drawCardPreview(Graphics g, Deck catalog, int cardId) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		g.setColor(new Color(0f, 0f, 0f, 0.72f));
		g.fillRect(0, 0, width, height);
		if (catalog == null)
			return;
		Card source = catalog.getCardById(cardId);
		if (source == null)
			return;
		Card card = new Card(source);
		card.setOwner(TripleTriad.PLAYER);
		float size = Options.getCardLength();
		card.draw((width - size) / 2f, (height - size) / 2f);
	}

	/**
	 * Draws a dimmed Yes/No confirmation (0 = No, 1 = Yes).
	 * @param g the graphics context
	 * @param title the question
	 * @param choice the highlighted option
	 */
	public static void drawYesNoConfirm(Graphics g, String title, int choice) {
		int width = Options.getWidth();
		int height = Options.getHeight();
		g.setColor(new Color(0f, 0f, 0f, 0.72f));
		g.fillRect(0, 0, width, height);
		drawCentered(Options.getFont(), title, height * 0.38f, TITLE);
		drawCentered(Options.getSmallFont(), I18n.confirmLeaveNo(), height * 0.50f,
			choice == 0 ? SELECTED : HINT);
		drawCentered(Options.getSmallFont(), I18n.confirmLeaveYes(), height * 0.58f,
			choice == 1 ? SELECTED : HINT);
		drawCentered(Options.getSmallFont(), I18n.hintLeaveConfirm(), height * 0.78f, HINT);
	}

	/**
	 * Hit-test for the Yes/No overlay.
	 * @param y mouse Y
	 * @return 0 for No, 1 for Yes, or -1
	 */
	public static int hitYesNoConfirm(int y) {
		UnicodeFont small = Options.getSmallFont();
		float h = small.getLineHeight();
		float noY = Options.getHeight() * 0.50f;
		float yesY = Options.getHeight() * 0.58f;
		if (y >= noY && y < noY + h)
			return 0;
		if (y >= yesY && y < yesY + h)
			return 1;
		return -1;
	}

	/**
	 * Left edge of the cursor-card panel (~28% of the width).
	 */
	public static float pickPanelX() {
		return Options.getWidth() * 0.72f;
	}

	/**
	 * Hand-slot size used by builder and pick screens.
	 */
	public static float handSlotSize() {
		return Options.getCardLength() * 0.32f;
	}

	/**
	 * Top of the five hand slots.
	 */
	public static float handSlotsY() {
		return Options.getHeight() * 0.14f;
	}

	/**
	 * Y where the catalog grid starts, below slot name/element labels.
	 */
	public static float handGridY() {
		UnicodeFont small = Options.getSmallFont();
		return handSlotsY() + handSlotSize() + small.getLineHeight() * 2.05f
			+ Options.getHeight() * 0.015f;
	}

	/**
	 * Grid column count that fits in the left area beside the cursor panel.
	 */
	public static int pickGridColumns(int cellSize, int gap) {
		float avail = pickPanelX() - Options.getWidth() * 0.06f;
		return Math.max(4, (int) (avail / (cellSize + gap)));
	}

	/**
	 * Left X of a grid centered in the left pick area.
	 */
	public static int pickGridX(int cols, int cellSize, int gap) {
		float left = Options.getWidth() * 0.03f;
		float avail = pickPanelX() - left;
		float total = cols * (cellSize + gap) - gap;
		return (int) (left + (avail - total) / 2f);
	}

	/**
	 * Packs up to five selected catalog IDs into a length-5 array (0 = empty).
	 */
	public static int[] packHandIds(ArrayList<Integer> selectedIds) {
		int[] ids = new int[5];
		if (selectedIds == null)
			return ids;
		for (int i = 0; i < selectedIds.size() && i < 5; i++)
			ids[i] = selectedIds.get(i).intValue();
		return ids;
	}

	/**
	 * Packs up to five bag indices into catalog IDs (0 = empty).
	 */
	public static int[] idsFromBag(ArrayList<Integer> bag, ArrayList<Integer> selected) {
		int[] ids = new int[5];
		if (bag == null || selected == null)
			return ids;
		for (int i = 0; i < selected.size() && i < 5; i++) {
			int index = selected.get(i).intValue();
			if (index >= 0 && index < bag.size())
				ids[i] = bag.get(index).intValue();
		}
		return ids;
	}

	/**
	 * Draws five hand slots with truncated name and element under each mini.
	 * Slots are centered in the left pick area.
	 */
	public static void drawHandSlots(Graphics g, Deck catalog, int[] ids) {
		UnicodeFont small = Options.getSmallFont();
		float slotSize = handSlotSize();
		float slotGap = slotSize * 0.12f;
		float slotsW = 5 * slotSize + 4 * slotGap;
		float left = Options.getWidth() * 0.03f;
		float avail = pickPanelX() - left;
		float slotsX = left + Math.max(0f, (avail - slotsW) / 2f);
		float slotsY = handSlotsY();
		for (int i = 0; i < 5; i++) {
			float x = slotsX + i * (slotSize + slotGap);
			g.setColor(DISABLED);
			g.drawRect(x, slotsY, slotSize, slotSize);
			Card card = null;
			if (catalog != null && ids != null && i < ids.length && ids[i] > 0)
				card = catalog.getCardById(ids[i]);
			if (card != null)
				card.drawSized(x, slotsY, slotSize, true, false);
			String name = (card != null) ? card.getName() : "";
			String ele = (card != null) ? I18n.elementName(card.getElement()) : "";
			String nameFit = fit(small, name, slotSize);
			String eleFit = fit(small, ele, slotSize);
			float nameY = slotsY + slotSize + 2f;
			small.drawString(x + (slotSize - small.getWidth(nameFit)) / 2f, nameY, nameFit, HINT);
			small.drawString(x + (slotSize - small.getWidth(eleFit)) / 2f,
				nameY + small.getLineHeight() * 0.92f, eleFit, TITLE);
		}
	}

	/**
	 * Hit-test against the five hand-slot portraits (not the labels).
	 * @return slot index [0, 4], or -1
	 */
	public static int hitHandSlot(int x, int y) {
		float slotSize = handSlotSize();
		float slotGap = slotSize * 0.12f;
		float slotsW = 5 * slotSize + 4 * slotGap;
		float left = Options.getWidth() * 0.03f;
		float avail = pickPanelX() - left;
		float slotsX = left + Math.max(0f, (avail - slotsW) / 2f);
		float slotsY = handSlotsY();
		if (y < slotsY || y > slotsY + slotSize)
			return -1;
		for (int i = 0; i < 5; i++) {
			float sx = slotsX + i * (slotSize + slotGap);
			if (x >= sx && x < sx + slotSize)
				return i;
		}
		return -1;
	}

	/** Inner padding of the cursor-card panel. */
	private static final float PANEL_PAD = 16f;

	/** Gold frame around the medium card. */
	private static final float CARD_FRAME = 6f;

	/**
	 * Draws the right-side medium card of the cursor and identity line.
	 * Builder and pick screens also show the R-random label.
	 */
	public static void drawCursorCardPanel(Graphics g, Deck catalog, int cardId) {
		drawCursorCardPanel(g, catalog, cardId, true);
	}

	/**
	 * Draws the right-side medium card of the cursor and identity line.
	 * @param showRandom true to draw the R-random caption
	 */
	public static void drawCursorCardPanel(Graphics g, Deck catalog, int cardId, boolean showRandom) {
		UnicodeFont small = Options.getSmallFont();
		float panelX = cursorPanelX();
		float panelW = cursorPanelW();
		float panelY = cursorPanelY();
		float panelH = cursorPanelH(showRandom);
		g.setColor(DISABLED);
		g.drawRect(panelX, panelY, panelW, panelH);

		if (catalog != null && cardId > 0) {
			Card source = catalog.getCardById(cardId);
			if (source != null) {
				Card card = new Card(source);
				card.setOwner(TripleTriad.PLAYER);
				float[] box = cursorCardBox();
				card.drawSized(box[0], box[1], box[2], true, false);
				g.setColor(TITLE);
				g.setLineWidth(2f);
				g.drawRect(box[0] - CARD_FRAME, box[1] - CARD_FRAME,
					box[2] + CARD_FRAME * 2f, box[2] + CARD_FRAME * 2f);
				g.setLineWidth(1f);
				String line = I18n.cardIdentity(card);
				String fitted = fit(small, line, panelW - PANEL_PAD * 2f);
				float textY = box[1] + box[2] + PANEL_PAD * 0.6f;
				small.drawString(panelX + (panelW - small.getWidth(fitted)) / 2f, textY, fitted, SELECTED);
			}
		}

		if (showRandom)
			drawPanelLabel(g, I18n.randomFill(), randomLabelY(), TITLE);
	}

	/**
	 * Draws a caption centered in the cursor-card panel.
	 */
	public static void drawPanelLabel(Graphics g, String text, float y, Color color) {
		if (text == null)
			text = "";
		UnicodeFont small = Options.getSmallFont();
		float panelX = cursorPanelX();
		float panelW = cursorPanelW();
		String fitted = fit(small, text, panelW - PANEL_PAD * 2f);
		float x = panelX + (panelW - small.getWidth(fitted)) / 2f;
		small.drawString(x, y, fitted, color);
	}

	/**
	 * Y of the R-random label inside the cursor panel.
	 */
	public static float randomLabelY() {
		UnicodeFont small = Options.getSmallFont();
		return cursorPanelY() + cursorPanelH(true) - PANEL_PAD
			- small.getLineHeight() * 2.9f;
	}

	/**
	 * Y of a second panel action (championship save) under the random label.
	 */
	public static float panelActionY() {
		return randomLabelY() + Options.getSmallFont().getLineHeight() * 1.45f;
	}

	/**
	 * Whether (x, y) hits a panel caption drawn at {@code y}.
	 */
	public static boolean hitPanelLabel(String text, float y, int x, int mouseY) {
		if (text == null)
			text = "";
		UnicodeFont small = Options.getSmallFont();
		float panelX = cursorPanelX();
		float panelW = cursorPanelW();
		String fitted = fit(small, text, panelW - PANEL_PAD * 2f);
		float lx = panelX + (panelW - small.getWidth(fitted)) / 2f;
		float pad = 6f;
		return x >= lx - pad && x <= lx + small.getWidth(fitted) + pad
			&& mouseY >= y - pad && mouseY <= y + small.getLineHeight() + pad;
	}

	/**
	 * Whether (x, y) hits the R-random label.
	 */
	public static boolean hitRandomLabel(int x, int y) {
		return hitPanelLabel(I18n.randomFill(), randomLabelY(), x, y);
	}

	/**
	 * Whether (x, y) hits the medium cursor card.
	 */
	public static boolean hitCursorCard(int x, int y) {
		float[] box = cursorCardBox();
		return x >= box[0] && x <= box[0] + box[2]
			&& y >= box[1] && y <= box[1] + box[2];
	}

	/**
	 * Truncates {@code text} with an ellipsis so it fits {@code maxWidth}.
	 */
	public static String fit(UnicodeFont font, String text, float maxWidth) {
		if (text == null || text.isEmpty())
			return "";
		if (font.getWidth(text) <= maxWidth)
			return text;
		String ellipsis = "...";
		int lo = 0;
		int hi = text.length();
		String best = ellipsis;
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			String candidate = text.substring(0, mid) + ellipsis;
			if (font.getWidth(candidate) <= maxWidth) {
				best = candidate;
				lo = mid + 1;
			} else {
				hi = mid - 1;
			}
		}
		return best;
	}

	private static float cursorPanelX() {
		return pickPanelX();
	}

	private static float cursorPanelY() {
		return Options.getHeight() * 0.14f;
	}

	private static float cursorPanelW() {
		int width = Options.getWidth();
		return width - pickPanelX() - width * 0.02f;
	}

	private static float cursorPanelH(boolean showRandom) {
		UnicodeFont small = Options.getSmallFont();
		float[] box = cursorCardBox();
		float after = PANEL_PAD + small.getLineHeight();
		if (showRandom)
			after += small.getLineHeight() * 1.45f * 2f + PANEL_PAD;
		else
			after += PANEL_PAD;
		float h = (box[1] - cursorPanelY()) + box[2] + after;
		float maxH = Options.getHeight() * 0.90f - cursorPanelY();
		return Math.min(h, maxH);
	}

	/**
	 * Cursor-card box: [x, y, size].
	 */
	private static float[] cursorCardBox() {
		float panelX = cursorPanelX();
		float panelW = cursorPanelW();
		float panelY = cursorPanelY();
		float inner = panelW - PANEL_PAD * 2f - CARD_FRAME * 2f;
		float size = Math.min(inner, Options.getCardLength() * 0.62f);
		float x = panelX + (panelW - size) / 2f;
		float y = panelY + PANEL_PAD + CARD_FRAME;
		return new float[] { x, y, size };
	}
}
