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

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.SavedDeck;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;
import java.util.Collections;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Meu Deck: album gallery with full-size preview, or pick 5 for Quick Game.
 */
public class MyDeckScreen extends Screen {
	private static final int MODE_GALLERY = 0;
	private static final int MODE_PICK = 1;

	/** Game instance. */
	private final TripleTriad game;

	/** Gallery vs pick-for-quick. */
	private int mode = MODE_GALLERY;

	/** Grid cursor. */
	private int cursor;

	/** First visible grid row. */
	private int scrollRow;

	/** Card index shown full size, or -1. */
	private int previewIndex = -1;

	/** Selected album indices (pick mode, max 5). */
	private final ArrayList<Integer> selected = new ArrayList<Integer>();

	/** True while the album-delete confirmation is open. */
	private boolean deleteConfirm;

	/** 0 = No, 1 = Yes. */
	private int deleteChoice;

	/** Album index pending deletion. */
	private int deleteIndex = -1;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public MyDeckScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		enterGallery();
	}

	/**
	 * Opens the album gallery.
	 */
	public void enterGallery() {
		mode = MODE_GALLERY;
		cursor = 0;
		scrollRow = 0;
		previewIndex = -1;
		selected.clear();
		clearDeleteConfirm();
	}

	/**
	 * Opens pick-5 from the album for a Quick Game match.
	 */
	public void enterPick() {
		mode = MODE_PICK;
		cursor = 0;
		scrollRow = 0;
		previewIndex = -1;
		selected.clear();
		clearDeleteConfirm();
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();
		ArrayList<Integer> bag = album();

		Ui.drawCentered(font, I18n.menuMyDeck(), height * 0.04f, Ui.TITLE);
		if (mode == MODE_PICK)
			Ui.drawCentered(small, I18n.cardCount(selected.size()), height * 0.10f, Ui.HINT);
		else if (bag.isEmpty())
			Ui.drawCentered(small, I18n.myDeckEmpty(), height * 0.12f, Ui.HINT);
		else
			Ui.drawCentered(small, I18n.championshipCards(bag.size()), height * 0.10f, Ui.HINT);

		if (mode == MODE_PICK) {
			Ui.drawHandSlots(g, game.getDeck(), Ui.idsFromBag(bag, selected));
			if (!bag.isEmpty())
				drawPickGrid(g, bag, height);
			int cursorId = (cursor >= 0 && cursor < bag.size()) ? bag.get(cursor).intValue() : 0;
			Ui.drawCursorCardPanel(g, game.getDeck(), cursorId, true);
		} else if (!bag.isEmpty()) {
			drawGrid(g, bag, height);
			int cursorId = (cursor >= 0 && cursor < bag.size()) ? bag.get(cursor).intValue() : 0;
			Ui.drawCursorCardPanel(g, game.getDeck(), cursorId, false);
		}

		if (previewIndex >= 0 && previewIndex < bag.size())
			Ui.drawCardPreview(g, game.getDeck(), bag.get(previewIndex).intValue());

		if (deleteConfirm)
			Ui.drawYesNoConfirm(g, deleteTitle(), deleteChoice);
		else {
			String hint;
			if (previewIndex >= 0)
				hint = (mode == MODE_GALLERY) ? I18n.hintMyDeckPreview() : I18n.hintCardPreview();
			else if (mode == MODE_PICK)
				hint = I18n.hintMyDeckPick();
			else
				hint = I18n.hintMyDeck();
			Ui.drawCentered(small, hint, height * 0.93f, Ui.HINT);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if (deleteConfirm) {
			handleDeleteConfirmKey(key);
			return;
		}
		ArrayList<Integer> bag = album();
		if (previewIndex >= 0) {
			if (key == Input.KEY_LEFT) {
				if (previewIndex > 0) {
					previewIndex--;
					cursor = previewIndex;
					AudioController.playCursor();
				}
			} else if (key == Input.KEY_RIGHT) {
				if (previewIndex < bag.size() - 1) {
					previewIndex++;
					cursor = previewIndex;
					AudioController.playCursor();
				}
			} else if (key == Input.KEY_ESCAPE || key == Input.KEY_C) {
				previewIndex = -1;
				AudioController.Effect.BACK.play();
			} else if (key == Input.KEY_DELETE || key == Input.KEY_X || key == Input.KEY_BACK) {
				if (mode == MODE_GALLERY)
					beginDeleteConfirm();
				else
					removeFromPick();
			} else if (key == Input.KEY_Z && mode == MODE_PICK) {
				togglePick();
			} else if (key == Input.KEY_ENTER && mode == MODE_PICK) {
				confirmPick();
			}
			return;
		}
		int cols = columns();
		int rows = (bag.size() + cols - 1) / Math.max(1, cols);
		int row = (cols > 0) ? cursor / cols : 0;
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			if (mode == MODE_PICK)
				game.showDeckSelect();
			else
				game.showMenu();
			break;
		case Input.KEY_RIGHT:
			if (cursor < bag.size() - 1) {
				cursor++;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_LEFT:
			if (cursor > 0) {
				cursor--;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_DOWN:
			if (row < rows - 1) {
				int next = Math.min(bag.size() - 1, cursor + cols);
				if (next != cursor) {
					cursor = next;
					AudioController.playCursor();
				}
			}
			break;
		case Input.KEY_UP:
			if (row > 0) {
				cursor -= cols;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_Z:
			if (mode == MODE_PICK)
				togglePick();
			break;
		case Input.KEY_ENTER:
			if (mode == MODE_PICK)
				confirmPick();
			break;
		case Input.KEY_C:
			openPreview();
			break;
		case Input.KEY_R:
			if (mode == MODE_PICK)
				fillRandom();
			break;
		case Input.KEY_DELETE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			if (mode == MODE_GALLERY)
				beginDeleteConfirm();
			else
				removeFromPick();
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		if (deleteConfirm) {
			handleDeleteConfirmClick(y);
			return;
		}
		if (previewIndex >= 0) {
			previewIndex = -1;
			AudioController.Effect.BACK.play();
			return;
		}
		if (mode == MODE_PICK) {
			if (Ui.hitRandomLabel(x, y)) {
				fillRandom();
				return;
			}
			int slot = Ui.hitHandSlot(x, y);
			if (slot >= 0) {
				if (slot < selected.size()) {
					selected.remove(slot);
					AudioController.Effect.BACK.play();
				}
				return;
			}
		}
		if (Ui.hitCursorCard(x, y)) {
			openPreview();
			return;
		}
		int index = hitGrid(x, y);
		if (index < 0)
			return;
		if (index != cursor) {
			cursor = index;
			AudioController.playCursor();
			return;
		}
		if (mode == MODE_PICK)
			togglePick();
		else
			openPreview();
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (deleteConfirm || previewIndex >= 0)
			return;
		ArrayList<Integer> bag = album();
		int cols = columns();
		if (change < 0)
			cursor = Math.min(bag.size() - 1, cursor + cols);
		else if (change > 0)
			cursor = Math.max(0, cursor - cols);
	}

	private void openPreview() {
		ArrayList<Integer> bag = album();
		if (cursor < 0 || cursor >= bag.size()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		previewIndex = cursor;
		AudioController.Effect.SELECT.play();
	}

	private void removeFromPick() {
		if (mode != MODE_PICK)
			return;
		int index = (previewIndex >= 0) ? previewIndex : cursor;
		Integer key = Integer.valueOf(index);
		int pos = selected.indexOf(key);
		if (pos < 0) {
			AudioController.Effect.INVALID.play();
			return;
		}
		selected.remove(pos);
		AudioController.Effect.BACK.play();
	}

	private void beginDeleteConfirm() {
		if (mode != MODE_GALLERY)
			return;
		ArrayList<Integer> bag = album();
		int index = (previewIndex >= 0) ? previewIndex : cursor;
		if (index < 0 || index >= bag.size()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		deleteIndex = index;
		deleteChoice = 0;
		deleteConfirm = true;
		AudioController.Effect.SELECT.play();
	}

	private void handleDeleteConfirmKey(int key) {
		switch (key) {
		case Input.KEY_UP:
		case Input.KEY_DOWN:
			deleteChoice = 1 - deleteChoice;
			AudioController.playCursor();
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			clearDeleteConfirm();
			AudioController.Effect.BACK.play();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (deleteChoice == 1)
				commitDelete();
			else {
				clearDeleteConfirm();
				AudioController.Effect.BACK.play();
			}
			break;
		default:
			break;
		}
	}

	private void handleDeleteConfirmClick(int y) {
		int hit = Ui.hitYesNoConfirm(y);
		if (hit < 0)
			return;
		if (hit != deleteChoice) {
			deleteChoice = hit;
			AudioController.playCursor();
			return;
		}
		if (deleteChoice == 1)
			commitDelete();
		else {
			clearDeleteConfirm();
			AudioController.Effect.BACK.play();
		}
	}

	private void commitDelete() {
		ArrayList<Integer> bag = album();
		if (deleteIndex < 0 || deleteIndex >= bag.size()) {
			clearDeleteConfirm();
			AudioController.Effect.INVALID.play();
			return;
		}
		int cardId = bag.get(deleteIndex).intValue();
		game.removeAlbumCard(cardId);
		previewIndex = -1;
		clearDeleteConfirm();
		AudioController.Effect.BACK.play();
		bag = album();
		if (cursor >= bag.size())
			cursor = Math.max(0, bag.size() - 1);
	}

	private void clearDeleteConfirm() {
		deleteConfirm = false;
		deleteChoice = 0;
		deleteIndex = -1;
	}

	private String deleteTitle() {
		ArrayList<Integer> bag = album();
		if (deleteIndex < 0 || deleteIndex >= bag.size())
			return I18n.confirmRemoveAlbum("");
		Card card = game.getDeck().getCardById(bag.get(deleteIndex).intValue());
		String name = (card != null) ? card.getName() : "";
		return I18n.confirmRemoveAlbum(name);
	}

	private void togglePick() {
		ArrayList<Integer> bag = album();
		if (cursor < 0 || cursor >= bag.size())
			return;
		Integer key = Integer.valueOf(cursor);
		int pos = selected.indexOf(key);
		if (pos >= 0) {
			selected.remove(pos);
			AudioController.Effect.BACK.play();
		} else if (selected.size() < SavedDeck.SIZE) {
			selected.add(key);
			AudioController.Effect.SELECT.play();
		} else {
			AudioController.Effect.INVALID.play();
		}
	}

	private void fillRandom() {
		if (mode != MODE_PICK)
			return;
		ArrayList<Integer> bag = album();
		if (bag.size() < SavedDeck.SIZE) {
			AudioController.Effect.INVALID.play();
			return;
		}
		ArrayList<Integer> idx = new ArrayList<Integer>();
		for (int i = 0; i < bag.size(); i++)
			idx.add(Integer.valueOf(i));
		Collections.shuffle(idx);
		selected.clear();
		for (int i = 0; i < SavedDeck.SIZE; i++)
			selected.add(idx.get(i));
		AudioController.Effect.SELECT.play();
	}

	private void confirmPick() {
		if (selected.size() != SavedDeck.SIZE) {
			AudioController.Effect.INVALID.play();
			return;
		}
		ArrayList<Integer> bag = album();
		int[] ids = new int[SavedDeck.SIZE];
		for (int i = 0; i < selected.size(); i++)
			ids[i] = bag.get(selected.get(i).intValue()).intValue();
		AudioController.Effect.SELECT.play();
		game.startQuickMatch(ids);
	}

	private void drawGrid(Graphics g, ArrayList<Integer> bag, int height) {
		Deck catalog = game.getDeck();
		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int gridY = (int) (height * 0.16f);
		int visibleRows = Math.max(1, (height - gridY - (int) (height * 0.12f)) / (gridSize + gap));
		int rows = (bag.size() + cols - 1) / Math.max(1, cols);
		clampScroll(rows, visibleRows, cols);
		int gridX = Ui.pickGridX(cols, gridSize, gap);
		for (int row = scrollRow; row < rows && row < scrollRow + visibleRows; row++) {
			for (int col = 0; col < cols; col++) {
				int index = row * cols + col;
				if (index >= bag.size())
					break;
				Card card = catalog.getCardById(bag.get(index).intValue());
				float x = gridX + col * (gridSize + gap);
				float y = gridY + (row - scrollRow) * (gridSize + gap);
				if (card != null)
					card.drawSized(x, y, gridSize, true, false);
				if (index == cursor) {
					g.setColor(Ui.TITLE);
					g.setLineWidth(2f);
					g.drawRect(x - 2, y - 2, gridSize + 4, gridSize + 4);
					g.setLineWidth(1f);
				}
			}
		}
	}

	private void drawPickGrid(Graphics g, ArrayList<Integer> bag, int height) {
		Deck catalog = game.getDeck();
		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int gridY = (int) Ui.handGridY();
		int visibleRows = Math.max(1, (height - gridY - (int) (height * 0.12f)) / (gridSize + gap));
		int rows = (bag.size() + cols - 1) / Math.max(1, cols);
		clampScroll(rows, visibleRows, cols);
		int gridX = Ui.pickGridX(cols, gridSize, gap);
		for (int row = scrollRow; row < rows && row < scrollRow + visibleRows; row++) {
			for (int col = 0; col < cols; col++) {
				int index = row * cols + col;
				if (index >= bag.size())
					break;
				Card card = catalog.getCardById(bag.get(index).intValue());
				float x = gridX + col * (gridSize + gap);
				float y = gridY + (row - scrollRow) * (gridSize + gap);
				boolean inHand = selected.contains(Integer.valueOf(index));
				if (card != null)
					card.drawSized(x, y, gridSize, inHand, !inHand);
				if (index == cursor) {
					g.setColor(Ui.TITLE);
					g.setLineWidth(2f);
					g.drawRect(x - 2, y - 2, gridSize + 4, gridSize + 4);
					g.setLineWidth(1f);
				}
			}
		}
	}

	private ArrayList<Integer> album() {
		Profile profile = game.getProfile();
		if (profile == null)
			return new ArrayList<Integer>();
		return profile.getCollection();
	}

	private int columns() {
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		return Ui.pickGridColumns(gridSize, gap);
	}

	private int cellSize() {
		return Math.max(48, Options.getCardLength() / 3);
	}

	private void clampScroll(int rows, int visibleRows, int cols) {
		int row = (cols > 0) ? cursor / cols : 0;
		if (row < scrollRow)
			scrollRow = row;
		if (row >= scrollRow + visibleRows)
			scrollRow = row - visibleRows + 1;
		if (scrollRow < 0)
			scrollRow = 0;
		int maxScroll = Math.max(0, rows - visibleRows);
		if (scrollRow > maxScroll)
			scrollRow = maxScroll;
	}

	private int hitGrid(int x, int y) {
		ArrayList<Integer> bag = album();
		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int height = Options.getHeight();
		int gridY;
		int gridX;
		if (mode == MODE_PICK) {
			gridY = (int) Ui.handGridY();
			gridX = Ui.pickGridX(cols, gridSize, gap);
		} else {
			gridY = (int) (height * 0.16f);
			gridX = Ui.pickGridX(cols, gridSize, gap);
		}
		int visibleRows = Math.max(1, (height - gridY - (int) (height * 0.12f)) / (gridSize + gap));
		int rows = (bag.size() + cols - 1) / Math.max(1, cols);
		clampScroll(rows, visibleRows, cols);
		for (int row = scrollRow; row < rows && row < scrollRow + visibleRows; row++) {
			for (int col = 0; col < cols; col++) {
				int index = row * cols + col;
				if (index >= bag.size())
					break;
				float cx = gridX + col * (gridSize + gap);
				float cy = gridY + (row - scrollRow) * (gridSize + gap);
				if (x >= cx && x < cx + gridSize && y >= cy && y < cy + gridSize)
					return index;
			}
		}
		return -1;
	}
}
