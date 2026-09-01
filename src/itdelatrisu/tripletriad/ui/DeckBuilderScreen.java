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
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Triple Triad.  If not, see <http://www.gnu.org/licenses/>.
 */

package itdelatrisu.tripletriad.ui;

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.CardSort;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
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
 * Build or edit a 5-card deck from the full catalog.
 */
public class DeckBuilderScreen extends Screen {
	/** Maximum deck name length. */
	private static final int MAX_NAME = 16;

	/** Game instance. */
	private final TripleTriad game;

	/** Selected card IDs (unique, max 5). */
	private final ArrayList<Integer> selectedIds = new ArrayList<Integer>();

	/** Catalog cursor index. */
	private int cursor;

	/** First visible grid row. */
	private int scrollRow;

	/** True while typing a name to save. */
	private boolean naming;

	/** Deck name buffer. */
	private final StringBuilder name = new StringBuilder();

	/** Original deck being edited, or null if creating. */
	private SavedDeck editing;

	/** Catalog index shown full size, or -1. */
	private int previewIndex = -1;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public DeckBuilderScreen(TripleTriad game) {
		this.game = game;
	}

	/**
	 * Prepares the screen for a new deck or an existing one.
	 * @param existing the deck to edit, or null
	 */
	public void edit(SavedDeck existing) {
		this.editing = existing;
		selectedIds.clear();
		name.setLength(0);
		naming = false;
		cursor = 0;
		scrollRow = 0;
		previewIndex = -1;
		if (existing != null) {
			name.append(existing.getName());
			int[] ids = existing.getCardIds();
			for (int i = 0; i < ids.length; i++)
				selectedIds.add(Integer.valueOf(ids[i]));
		}
	}

	@Override
	public void enter() {
		if (selectedIds.isEmpty() && editing == null) {
			cursor = 0;
			scrollRow = 0;
			naming = false;
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int width = container.getWidth();
		int height = container.getHeight();
		Deck catalog = game.getDeck();
		ArrayList<Card> cards = catalogView();

		Ui.drawCentered(font, editing == null ? I18n.buildDeck() : I18n.editDeck(), height * 0.03f, Ui.TITLE);
		Ui.drawCentered(small, I18n.cardCount(selectedIds.size()) + "    " + I18n.sortLine(),
			height * 0.09f, Ui.HINT);

		Ui.drawHandSlots(g, catalog, Ui.packHandIds(selectedIds));

		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int gridX = Ui.pickGridX(cols, gridSize, gap);
		int gridY = (int) Ui.handGridY();
		int visibleRows = Math.max(1, (height - gridY - (int) (height * 0.12f)) / (gridSize + gap));
		int rows = (cards.size() + cols - 1) / cols;
		clampScroll(rows, visibleRows);

		for (int row = scrollRow; row < rows && row < scrollRow + visibleRows; row++) {
			for (int col = 0; col < cols; col++) {
				int index = row * cols + col;
				if (index >= cards.size())
					break;
				Card card = cards.get(index);
				float x = gridX + col * (gridSize + gap);
				float y = gridY + (row - scrollRow) * (gridSize + gap);
				boolean inDeck = containsId(card.getID());
				card.drawSized(x, y, gridSize, inDeck, !inDeck);
				if (index == cursor) {
					g.setColor(Ui.TITLE);
					g.setLineWidth(2f);
					g.drawRect(x - 2, y - 2, gridSize + 4, gridSize + 4);
					g.setLineWidth(1f);
				}
			}
		}

		int cursorId = (cursor >= 0 && cursor < cards.size()) ? cards.get(cursor).getID() : 0;
		Ui.drawCursorCardPanel(g, catalog, cursorId);

		if (previewIndex >= 0 && previewIndex < cards.size())
			Ui.drawCardPreview(g, catalog, cards.get(previewIndex).getID());

		if (naming) {
			g.setColor(new Color(0f, 0f, 0f, 0.7f));
			g.fillRect(0, height * 0.38f, width, height * 0.24f);
			Ui.drawCentered(small, I18n.deckNamePrompt(), height * 0.42f, Ui.HINT);
			String shown = name.length() == 0 ? "_" : name.toString() + "_";
			Ui.drawCentered(font, shown, height * 0.48f, Ui.SELECTED);
			Ui.drawCentered(small, I18n.hintNaming(), height * 0.56f, Ui.HINT);
		} else if (previewIndex >= 0) {
			Ui.drawCentered(small, I18n.hintCardPreview(), height * 0.92f, Ui.HINT);
		} else {
			Ui.drawCentered(small, I18n.hintBuilder(),
					height * 0.92f, Ui.HINT);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if (naming) {
			handleNaming(key, c);
			return;
		}

		ArrayList<Card> cards = catalogView();
		if (previewIndex >= 0) {
			if (key == Input.KEY_LEFT) {
				if (previewIndex > 0) {
					previewIndex--;
					cursor = previewIndex;
					AudioController.playCursor();
				}
			} else if (key == Input.KEY_RIGHT) {
				if (previewIndex < cards.size() - 1) {
					previewIndex++;
					cursor = previewIndex;
					AudioController.playCursor();
				}
			} else if (key == Input.KEY_ESCAPE || key == Input.KEY_C) {
				previewIndex = -1;
				AudioController.Effect.BACK.play();
			} else if (key == Input.KEY_DELETE || key == Input.KEY_X || key == Input.KEY_BACK) {
				removeFromTray();
			} else if (key == Input.KEY_Z) {
				toggleCursor();
			} else if (key == Input.KEY_ENTER) {
				playIfReady();
			}
			return;
		}

		int cols = columns();
		int rows = (cards.size() + cols - 1) / cols;
		int row = cursor / cols;

		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showDeckSelect();
			break;
		case Input.KEY_RIGHT:
			if (cursor < cards.size() - 1) {
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
				int next = Math.min(cards.size() - 1, cursor + cols);
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
			toggleCursor();
			break;
		case Input.KEY_ENTER:
			playIfReady();
			break;
		case Input.KEY_C:
			openPreview();
			break;
		case Input.KEY_DELETE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			removeFromTray();
			break;
		case Input.KEY_S:
			beginSave();
			break;
		case Input.KEY_R:
			fillRandom();
			break;
		case Input.KEY_T:
			cycleSort();
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON || naming)
			return;
		if (previewIndex >= 0) {
			previewIndex = -1;
			AudioController.Effect.BACK.play();
			return;
		}

		if (Ui.hitRandomLabel(x, y)) {
			fillRandom();
			return;
		}
		if (Ui.hitCursorCard(x, y)) {
			openPreview();
			return;
		}

		int slot = Ui.hitHandSlot(x, y);
		if (slot >= 0) {
			if (slot < selectedIds.size()) {
				selectedIds.remove(slot);
				AudioController.Effect.BACK.play();
			}
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
		toggleCursor();
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (naming || previewIndex >= 0)
			return;
		ArrayList<Card> cards = catalogView();
		int cols = columns();
		if (change < 0)
			cursor = Math.min(cards.size() - 1, cursor + cols);
		else if (change > 0)
			cursor = Math.max(0, cursor - cols);
	}

	private void handleNaming(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			naming = false;
			AudioController.Effect.BACK.play();
			return;
		}
		if (key == Input.KEY_ENTER) {
			String trimmed = name.toString().trim();
			if (trimmed.isEmpty() || selectedIds.size() != SavedDeck.SIZE) {
				AudioController.Effect.INVALID.play();
				return;
			}
			int[] ids = toIdArray();
			SavedDeck deck = new SavedDeck(trimmed, ids);
			game.getProfile().upsertDeck(deck);
			game.saveProfile();
			naming = false;
			editing = deck;
			AudioController.Effect.SELECT.play();
			return;
		}
		if (key == Input.KEY_BACK && name.length() > 0) {
			name.deleteCharAt(name.length() - 1);
			AudioController.Effect.BACK.play();
			return;
		}
		if (Ui.isNameChar(c) && name.length() < MAX_NAME)
			name.append(c);
	}

	private void cycleSort() {
		ArrayList<Card> view = catalogView();
		int focusId = (cursor >= 0 && cursor < view.size()) ? view.get(cursor).getID() : 0;
		CardSort.cycle();
		view = catalogView();
		int next = CardSort.indexOfCardId(view, focusId);
		cursor = (next >= 0) ? next : 0;
		if (previewIndex >= 0)
			previewIndex = cursor;
		AudioController.playCursor();
	}

	private ArrayList<Card> catalogView() {
		return CardSort.sortedCatalog(game.getDeck().getCards());
	}

	private void openPreview() {
		ArrayList<Card> cards = catalogView();
		if (cursor < 0 || cursor >= cards.size()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		previewIndex = cursor;
		AudioController.Effect.SELECT.play();
	}

	private void removeFromTray() {
		ArrayList<Card> cards = catalogView();
		int index = (previewIndex >= 0) ? previewIndex : cursor;
		if (index < 0 || index >= cards.size()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		int pos = indexOfId(cards.get(index).getID());
		if (pos < 0) {
			AudioController.Effect.INVALID.play();
			return;
		}
		selectedIds.remove(pos);
		AudioController.Effect.BACK.play();
	}

	private void beginSave() {
		if (selectedIds.size() != SavedDeck.SIZE) {
			AudioController.Effect.INVALID.play();
			return;
		}
		naming = true;
		AudioController.Effect.SELECT.play();
	}

	private void fillRandom() {
		ArrayList<Card> cards = game.getDeck().getCards();
		if (cards.size() < SavedDeck.SIZE) {
			AudioController.Effect.INVALID.play();
			return;
		}
		ArrayList<Card> copy = new ArrayList<Card>(cards);
		Collections.shuffle(copy);
		selectedIds.clear();
		for (int i = 0; i < SavedDeck.SIZE; i++)
			selectedIds.add(Integer.valueOf(copy.get(i).getID()));
		AudioController.Effect.SELECT.play();
	}

	private void playIfReady() {
		if (selectedIds.size() != SavedDeck.SIZE) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.startQuickMatch(toIdArray());
	}

	private void toggleCursor() {
		ArrayList<Card> cards = catalogView();
		if (cursor < 0 || cursor >= cards.size())
			return;
		int id = cards.get(cursor).getID();
		int pos = indexOfId(id);
		if (pos >= 0) {
			selectedIds.remove(pos);
			AudioController.Effect.BACK.play();
		} else if (selectedIds.size() < SavedDeck.SIZE) {
			selectedIds.add(Integer.valueOf(id));
			AudioController.Effect.SELECT.play();
		} else {
			AudioController.Effect.INVALID.play();
		}
	}

	private int[] toIdArray() {
		int[] ids = new int[selectedIds.size()];
		for (int i = 0; i < selectedIds.size(); i++)
			ids[i] = selectedIds.get(i).intValue();
		return ids;
	}

	private boolean containsId(int id) {
		return indexOfId(id) >= 0;
	}

	private int indexOfId(int id) {
		for (int i = 0; i < selectedIds.size(); i++) {
			if (selectedIds.get(i).intValue() == id)
				return i;
		}
		return -1;
	}

	private int columns() {
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		return Ui.pickGridColumns(gridSize, gap);
	}

	private int cellSize() {
		return Math.max(48, Options.getCardLength() / 3);
	}

	private void clampScroll(int rows, int visibleRows) {
		int cols = columns();
		int row = cursor / cols;
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
		ArrayList<Card> cards = catalogView();
		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int gridX = Ui.pickGridX(cols, gridSize, gap);
		int gridY = (int) Ui.handGridY();
		if (x < gridX || y < gridY)
			return -1;
		int col = (x - gridX) / (gridSize + gap);
		int row = (y - gridY) / (gridSize + gap) + scrollRow;
		if (col < 0 || col >= cols)
			return -1;
		int index = row * cols + col;
		if (index < 0 || index >= cards.size())
			return -1;
		int cellX = gridX + col * (gridSize + gap);
		int cellY = gridY + (row - scrollRow) * (gridSize + gap);
		if (x > cellX + gridSize || y > cellY + gridSize)
			return -1;
		return index;
	}
}
