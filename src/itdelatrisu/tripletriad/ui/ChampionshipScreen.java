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
import itdelatrisu.tripletriad.ChampionshipRun;
import itdelatrisu.tripletriad.Deck;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.Rule;
import itdelatrisu.tripletriad.SavedDeck;
import itdelatrisu.tripletriad.TripleTriad;

import java.util.ArrayList;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Championship lobby, starter pack, hand pick, card trade and result.
 */
public class ChampionshipScreen extends Screen {
	private static final int MODE_LOBBY = 0;
	private static final int MODE_PACK = 1;
	private static final int MODE_PICK = 2;
	private static final int MODE_TRADE_WIN = 3;
	private static final int MODE_TRADE_LOSE = 4;
	private static final int MODE_WON = 5;
	private static final int MODE_LOST = 6;

	private static final int FOCUS_INFO = 0;
	private static final int FOCUS_RULES = 1;

	private static final int LOBBY_NEW = 0;
	private static final int LOBBY_CONTINUE = 1;
	private static final int LOBBY_CLEAR = 2;
	private static final int LOBBY_COUNT = 3;

	/** Game instance. */
	private final TripleTriad game;

	/** Current mode. */
	private int mode = MODE_LOBBY;

	/** Lobby focus. */
	private int focus = FOCUS_INFO;

	/** Novo jogo, Continuar, Apagar progresso. */
	private int lobbyIndex;

	/** Selected rule. */
	private int ruleIndex;

	/** Starter pack or trade cards. */
	private int[] shownIds = new int[0];

	/** Stolen card (AI win). */
	private int stolenId;

	/** Collection cursor (pick grid). */
	private int cursor;

	/** First visible grid row. */
	private int scrollRow;

	/** Selected collection indices (max 5). */
	private final ArrayList<Integer> selected = new ArrayList<Integer>();

	/** Trade cursor. */
	private int tradeIndex;

	/** Until when to show the championship-saved notice (epoch ms). */
	private long saveNoticeUntil;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public ChampionshipScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		mode = MODE_LOBBY;
		focus = FOCUS_INFO;
		lobbyIndex = LOBBY_NEW;
		ruleIndex = 0;
		selected.clear();
		cursor = 0;
		scrollRow = 0;
		tradeIndex = 0;
		stolenId = 0;
		shownIds = new int[0];
		saveNoticeUntil = 0L;
	}

	/**
	 * Shows the starter pack.
	 * @param ids five card IDs
	 */
	public void showPack(int[] ids) {
		mode = MODE_PACK;
		shownIds = (ids != null) ? ids.clone() : new int[0];
	}

	/**
	 * Shows the 5-card picker.
	 */
	public void showPick() {
		showPick(null);
	}

	/**
	 * Shows the 5-card picker, optionally pre-selecting a saved hand.
	 * @param preselect card IDs to mark if still in the collection
	 */
	public void showPick(int[] preselect) {
		mode = MODE_PICK;
		selected.clear();
		cursor = 0;
		scrollRow = 0;
		saveNoticeUntil = 0L;
		preselectHand(preselect);
	}

	/**
	 * Player won: pick one opponent card.
	 * @param ids the opponent hand
	 */
	public void showTradeWin(int[] ids) {
		mode = MODE_TRADE_WIN;
		shownIds = (ids != null) ? ids.clone() : new int[0];
		tradeIndex = 0;
	}

	/**
	 * AI won: show the stolen card.
	 * @param cardId the stolen ID
	 */
	public void showTradeLose(int cardId) {
		mode = MODE_TRADE_LOSE;
		stolenId = cardId;
	}

	/**
	 * Championship victory.
	 */
	public void showWon() {
		mode = MODE_WON;
	}

	/**
	 * Championship defeat.
	 */
	public void showLost() {
		mode = MODE_LOST;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		switch (mode) {
		case MODE_PACK:
			renderPack(container, g);
			break;
		case MODE_PICK:
			renderPick(container, g);
			break;
		case MODE_TRADE_WIN:
			renderTradeWin(container, g);
			break;
		case MODE_TRADE_LOSE:
			renderTradeLose(container, g);
			break;
		case MODE_WON:
			renderEnd(container, true);
			break;
		case MODE_LOST:
			renderEnd(container, false);
			break;
		case MODE_LOBBY:
		default:
			renderLobby(container, g);
			break;
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (mode) {
		case MODE_LOBBY:
			keyLobby(key);
			break;
		case MODE_PACK:
			if (key == Input.KEY_ENTER || key == Input.KEY_Z) {
				AudioController.Effect.SELECT.play();
				game.championshipPackConfirmed();
			} else if (key == Input.KEY_ESCAPE) {
				AudioController.Effect.BACK.play();
				game.championshipPackConfirmed();
			}
			break;
		case MODE_PICK:
			keyPick(key);
			break;
		case MODE_TRADE_WIN:
			keyTradeWin(key);
			break;
		case MODE_TRADE_LOSE:
			if (key == Input.KEY_ENTER || key == Input.KEY_Z || key == Input.KEY_ESCAPE) {
				AudioController.Effect.SELECT.play();
				game.championshipStealAck();
			}
			break;
		case MODE_WON:
		case MODE_LOST:
			if (key == Input.KEY_ESCAPE || key == Input.KEY_ENTER || key == Input.KEY_Z) {
				AudioController.Effect.BACK.play();
				game.showMenu();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		if (mode == MODE_LOBBY) {
			mouseLobby(x, y);
			return;
		}
		if (mode == MODE_PACK) {
			game.championshipPackConfirmed();
			return;
		}
		if (mode == MODE_PICK) {
			mousePick(x, y);
			return;
		}
		if (mode == MODE_TRADE_WIN) {
			int hit = hitShownCard(x, y);
			if (hit < 0)
				return;
			if (hit != tradeIndex) {
				tradeIndex = hit;
				AudioController.playCursor();
				return;
			}
			takeTrade();
			return;
		}
		if (mode == MODE_TRADE_LOSE) {
			game.championshipStealAck();
			return;
		}
		if (mode == MODE_WON || mode == MODE_LOST)
			game.showMenu();
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (mode == MODE_PICK) {
			ArrayList<Integer> bag = collection();
			int cols = columns();
			if (change < 0)
				cursor = Math.min(bag.size() - 1, cursor + cols);
			else if (change > 0)
				cursor = Math.max(0, cursor - cols);
		} else if (mode == MODE_TRADE_WIN && shownIds.length > 0) {
			if (change < 0)
				tradeIndex = Math.min(shownIds.length - 1, tradeIndex + 1);
			else if (change > 0)
				tradeIndex = Math.max(0, tradeIndex - 1);
		} else if (mode == MODE_LOBBY && focus == FOCUS_RULES) {
			int n = Rule.values().length;
			if (change < 0)
				ruleIndex = Math.min(n - 1, ruleIndex + 1);
			else if (change > 0)
				ruleIndex = Math.max(0, ruleIndex - 1);
		}
	}

	private void renderLobby(GameContainer container, Graphics g) {
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();
		Profile profile = game.getProfile();
		Rule[] rules = Rule.values();

		Ui.drawCentered(font, I18n.menuChampionship(), height * 0.06f, Ui.TITLE);
		Ui.drawCentered(small, I18n.championshipHint(), height * 0.13f, Ui.HINT);
		if (profile != null) {
			Ui.drawCentered(small, profile.getName(), height * 0.20f, Ui.SELECTED);
			String stats = I18n.championshipCards(profile.getCollection().size())
				+ "    " + I18n.championshipCups(profile.getChampionshipWins());
			Ui.drawCentered(small, stats, height * 0.26f, Ui.HINT);
		}

		boolean canContinue = hasSave();
		float line = small.getLineHeight() * 2.05f;
		float startY = height * 0.32f;
		for (int i = 0; i < LOBBY_COUNT; i++) {
			float y = startY + i * line;
			boolean on = (focus == FOCUS_INFO && lobbyIndex == i);
			boolean enabled = lobbyEnabled(i);
			Color color = !enabled ? Ui.DISABLED : (on ? Ui.SELECTED : Ui.HINT);
			small.drawString(infoLeft(), y, lobbyLabel(i), color);
			if (on)
				drawCursor(infoLeft(), y, small);
			if (i == LOBBY_CONTINUE && canContinue && profile != null)
				small.drawString(infoLeft(), y + small.getLineHeight(),
					I18n.championshipRoundOf(profile.getRunRound()), Ui.HINT);
		}

		float panelX = rulesLeft();
		float panelY = height * 0.32f;
		float panelW = rulesRight() - panelX;
		float panelH = height * 0.52f;
		g.setColor(Ui.TITLE);
		g.setLineWidth(focus == FOCUS_RULES ? 2f : 1f);
		g.drawRect(panelX, panelY, panelW, panelH);
		g.setLineWidth(1f);
		small.drawString(panelX + 16, panelY + 12, I18n.rulesTitle(), Ui.TITLE);
		float ruleLine = small.getLineHeight() * 1.45f;
		float ruleStart = panelY + small.getLineHeight() * 2.1f;
		for (int i = 0; i < rules.length; i++) {
			float y = ruleStart + i * ruleLine;
			boolean on = (focus == FOCUS_RULES && i == ruleIndex);
			boolean active = rules[i].isActive();
			Color nameColor = on ? Ui.SELECTED : (active ? Ui.TITLE : Ui.HINT);
			Color stateColor = active ? Ui.TITLE : Ui.DISABLED;
			small.drawString(panelX + 36, y, rules[i].getDisplayName(), nameColor);
			String state = active ? I18n.on() : I18n.off();
			small.drawString(panelX + panelW - 18 - small.getWidth(state), y, state,
				on ? Ui.SELECTED : stateColor);
			if (on)
				drawCursor(panelX + 36, y, small);
		}

		Ui.drawCentered(small, I18n.hintChampionshipLobby(), height * 0.92f, Ui.HINT);
	}

	private void renderPack(GameContainer container, Graphics g) {
		int height = container.getHeight();
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		Ui.drawCentered(font, I18n.championshipPackTitle(), height * 0.08f, Ui.TITLE);
		Ui.drawCentered(small, I18n.championshipPackHint(), height * 0.16f, Ui.HINT);
		drawShownRow(g, height * 0.36f);
		Ui.drawCentered(small, I18n.hintChampionshipConfirm(), height * 0.88f, Ui.HINT);
	}

	private void renderPick(GameContainer container, Graphics g) {
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int width = container.getWidth();
		int height = container.getHeight();
		ChampionshipRun run = game.getChampionshipRun();
		int round = (run != null) ? run.getRound() : 1;
		Ui.drawCentered(font, I18n.championshipPickTitle(), height * 0.03f, Ui.TITLE);
		Ui.drawCentered(small, I18n.championshipRoundOf(round) + "    " + I18n.cardCount(selected.size()),
			height * 0.09f, Ui.HINT);

		float slotSize = Options.getCardLength() * 0.32f;
		float slotGap = slotSize * 0.12f;
		float slotsW = 5 * slotSize + 4 * slotGap;
		float slotsX = (width - slotsW) / 2f;
		float slotsY = height * 0.14f;
		Deck catalog = game.getDeck();
		ArrayList<Integer> bag = collection();
		for (int i = 0; i < 5; i++) {
			float x = slotsX + i * (slotSize + slotGap);
			g.setColor(Ui.DISABLED);
			g.drawRect(x, slotsY, slotSize, slotSize);
			if (i < selected.size()) {
				int id = bag.get(selected.get(i).intValue()).intValue();
				Card card = catalog.getCardById(id);
				if (card != null)
					card.drawSized(x, slotsY, slotSize, true, false);
			}
		}

		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int gridX = (width - cols * (gridSize + gap) + gap) / 2;
		int gridY = (int) (slotsY + slotSize + height * 0.04f);
		int visibleRows = Math.max(1, (height - gridY - (int) (height * 0.18f)) / (gridSize + gap));
		int rows = (bag.size() + cols - 1) / Math.max(1, cols);
		clampScroll(rows, visibleRows, cols);

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
		boolean canSave = game.canChampionshipSave();
		String saveLabel = I18n.championshipSave();
		float saveX = (width - small.getWidth(saveLabel)) / 2f;
		float saveY = height * 0.86f;
		small.drawString(saveX, saveY, saveLabel, canSave ? Ui.TITLE : Ui.DISABLED);
		if (System.currentTimeMillis() < saveNoticeUntil)
			Ui.drawCentered(small, I18n.championshipSaved(), height * 0.80f, Ui.TITLE);
		Ui.drawCentered(small, I18n.hintChampionshipPick(), height * 0.93f, Ui.HINT);
	}

	private void renderTradeWin(GameContainer container, Graphics g) {
		int height = container.getHeight();
		Ui.drawCentered(Options.getFont(), I18n.menuChampionship(), height * 0.08f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), I18n.championshipTradeWin(), height * 0.16f, Ui.HINT);
		drawShownRow(g, height * 0.36f);
		Ui.drawCentered(Options.getSmallFont(), I18n.hintChampionshipConfirm(), height * 0.88f, Ui.HINT);
	}

	private void renderTradeLose(GameContainer container, Graphics g) {
		int height = container.getHeight();
		int width = container.getWidth();
		Ui.drawCentered(Options.getFont(), I18n.menuChampionship(), height * 0.08f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), I18n.championshipTradeLose(), height * 0.18f, Ui.HINT);
		Card card = game.getDeck().getCardById(stolenId);
		float size = Options.getCardLength() * 0.55f;
		if (card != null)
			card.drawSized((width - size) / 2f, height * 0.32f, size, false, true);
		Ui.drawCentered(Options.getSmallFont(), I18n.hintChampionshipConfirm(), height * 0.88f, Ui.HINT);
	}

	private void renderEnd(GameContainer container, boolean won) {
		int height = container.getHeight();
		Ui.drawCentered(Options.getFont(),
			won ? I18n.championshipWon() : I18n.championshipLost(),
			height * 0.40f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), I18n.hintChampionshipEnd(), height * 0.55f, Ui.HINT);
	}

	private void drawShownRow(Graphics g, float y) {
		Deck catalog = game.getDeck();
		float size = Options.getCardLength() * 0.42f;
		float gap = size * 0.12f;
		int n = shownIds.length;
		if (n <= 0)
			return;
		float total = n * size + (n - 1) * gap;
		float x0 = (Options.getWidth() - total) / 2f;
		for (int i = 0; i < n; i++) {
			float x = x0 + i * (size + gap);
			Card card = catalog.getCardById(shownIds[i]);
			boolean on = (mode == MODE_TRADE_WIN && i == tradeIndex);
			if (card != null)
				card.drawSized(x, y, size, on, false);
			if (on) {
				g.setColor(Ui.TITLE);
				g.setLineWidth(2f);
				g.drawRect(x - 3, y - 3, size + 6, size + 6);
				g.setLineWidth(1f);
			}
		}
	}

	private void keyLobby(int key) {
		int ruleCount = Rule.values().length;
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_LEFT:
			if (focus == FOCUS_RULES) {
				focus = FOCUS_INFO;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_RIGHT:
			if (focus == FOCUS_INFO) {
				focus = FOCUS_RULES;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_DOWN:
			if (focus == FOCUS_RULES)
				ruleIndex = (ruleIndex + 1) % ruleCount;
			else
				lobbyIndex = (lobbyIndex + 1) % LOBBY_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			if (focus == FOCUS_RULES)
				ruleIndex = (ruleIndex + ruleCount - 1) % ruleCount;
			else
				lobbyIndex = (lobbyIndex + 1) % LOBBY_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (focus == FOCUS_RULES)
				toggleSelectedRule();
			else
				activateLobby();
			break;
		default:
			break;
		}
	}

	private void keyPick(int key) {
		ArrayList<Integer> bag = collection();
		int cols = columns();
		int rows = (bag.size() + cols - 1) / Math.max(1, cols);
		int row = (cols > 0) ? cursor / cols : 0;
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.championshipBackToLobby();
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
			togglePick();
			break;
		case Input.KEY_S:
			tryPickSave();
			break;
		case Input.KEY_ENTER:
			if (selected.size() != SavedDeck.SIZE) {
				AudioController.Effect.INVALID.play();
				return;
			}
			AudioController.Effect.SELECT.play();
			game.championshipHandChosen(selectedIds());
			break;
		default:
			break;
		}
	}

	private void keyTradeWin(int key) {
		switch (key) {
		case Input.KEY_LEFT:
			if (tradeIndex > 0) {
				tradeIndex--;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_RIGHT:
			if (tradeIndex < shownIds.length - 1) {
				tradeIndex++;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			takeTrade();
			break;
		default:
			break;
		}
	}

	private void mouseLobby(int x, int y) {
		int ruleHit = hitRule(x, y);
		if (ruleHit >= 0) {
			focus = FOCUS_RULES;
			ruleIndex = ruleHit;
			toggleSelectedRule();
			return;
		}
		UnicodeFont small = Options.getSmallFont();
		float line = small.getLineHeight() * 2.05f;
		float startY = Options.getHeight() * 0.32f;
		if (x >= rulesLeft())
			return;
		for (int i = 0; i < LOBBY_COUNT; i++) {
			float top = startY + i * line;
			float h = (i == LOBBY_CONTINUE) ? line : small.getLineHeight();
			if (y >= top && y < top + h) {
				if (focus != FOCUS_INFO || lobbyIndex != i) {
					focus = FOCUS_INFO;
					lobbyIndex = i;
					AudioController.playCursor();
					return;
				}
				activateLobby();
				return;
			}
		}
	}

	private void mousePick(int x, int y) {
		if (hitSaveLabel(x, y)) {
			tryPickSave();
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
		togglePick();
	}

	private void takeTrade() {
		if (tradeIndex < 0 || tradeIndex >= shownIds.length) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.championshipCardTaken(shownIds[tradeIndex]);
	}

	private void togglePick() {
		ArrayList<Integer> bag = collection();
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

	private void activateLobby() {
		if (lobbyIndex == LOBBY_CONTINUE) {
			if (!hasSave()) {
				AudioController.Effect.INVALID.play();
				return;
			}
			AudioController.Effect.SELECT.play();
			game.championshipContinue();
			return;
		}
		if (lobbyIndex == LOBBY_CLEAR) {
			if (!hasSave()) {
				AudioController.Effect.INVALID.play();
				return;
			}
			AudioController.Effect.SELECT.play();
			game.championshipClearProgress();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.championshipNew();
	}

	private boolean lobbyEnabled(int index) {
		if (index == LOBBY_CONTINUE || index == LOBBY_CLEAR)
			return hasSave();
		return true;
	}

	private String lobbyLabel(int index) {
		switch (index) {
			case LOBBY_NEW: return I18n.championshipNewGame();
			case LOBBY_CONTINUE: return I18n.championshipContinue();
			case LOBBY_CLEAR: return I18n.championshipClearProgress();
			default: return "";
		}
	}

	private void tryPickSave() {
		if (!game.canChampionshipSave()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		AudioController.Effect.SELECT.play();
		game.championshipSave();
		saveNoticeUntil = System.currentTimeMillis() + 2500L;
	}

	private boolean hitSaveLabel(int x, int y) {
		UnicodeFont small = Options.getSmallFont();
		String label = I18n.championshipSave();
		float lx = (Options.getWidth() - small.getWidth(label)) / 2f;
		float ly = Options.getHeight() * 0.86f;
		return x >= lx && x <= lx + small.getWidth(label)
			&& y >= ly && y <= ly + small.getLineHeight();
	}

	private boolean hasSave() {
		Profile profile = game.getProfile();
		return profile != null && profile.hasChampionshipSave();
	}

	private void preselectHand(int[] ids) {
		if (ids == null || ids.length == 0)
			return;
		ArrayList<Integer> bag = collection();
		boolean[] used = new boolean[bag.size()];
		for (int p = 0; p < ids.length && selected.size() < SavedDeck.SIZE; p++) {
			int want = ids[p];
			for (int i = 0; i < bag.size(); i++) {
				if (!used[i] && bag.get(i).intValue() == want) {
					used[i] = true;
					selected.add(Integer.valueOf(i));
					break;
				}
			}
		}
	}

	private void toggleSelectedRule() {
		Rule[] rules = Rule.values();
		if (ruleIndex < 0 || ruleIndex >= rules.length)
			return;
		Rule rule = rules[ruleIndex];
		boolean turningOn = !rule.isActive();
		rule.toggle();
		if (rule == Rule.SAME_WALL && turningOn)
			Rule.SAME.setState(true);
		Options.saveOptions();
		AudioController.Effect.SELECT.play();
	}

	private int[] selectedIds() {
		ArrayList<Integer> bag = collection();
		int[] ids = new int[selected.size()];
		for (int i = 0; i < selected.size(); i++)
			ids[i] = bag.get(selected.get(i).intValue()).intValue();
		return ids;
	}

	private ArrayList<Integer> collection() {
		ChampionshipRun run = game.getChampionshipRun();
		if (run != null)
			return run.getBag();
		return new ArrayList<Integer>();
	}

	private void drawCursor(float textX, float textY, UnicodeFont small) {
		Image cursorImg = GameImage.CURSOR.getImage();
		cursorImg.draw(
			textX - cursorImg.getWidth() * 1.15f,
			textY + (small.getLineHeight() - cursorImg.getHeight()) / 2f
		);
	}

	private float infoLeft() { return Options.getWidth() * 0.10f; }

	private float rulesLeft() { return Options.getWidth() * 0.48f; }

	private float rulesRight() { return Options.getWidth() * 0.94f; }

	private int hitRule(int x, int y) {
		if (x < rulesLeft() || x > rulesRight())
			return -1;
		UnicodeFont small = Options.getSmallFont();
		float panelY = Options.getHeight() * 0.32f;
		float ruleLine = small.getLineHeight() * 1.45f;
		float ruleStart = panelY + small.getLineHeight() * 2.1f;
		Rule[] rules = Rule.values();
		for (int i = 0; i < rules.length; i++) {
			float top = ruleStart + i * ruleLine;
			if (y >= top && y < top + ruleLine)
				return i;
		}
		return -1;
	}

	private int columns() {
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		return Math.max(5, (Options.getWidth() - 80) / (gridSize + gap));
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
		ArrayList<Integer> bag = collection();
		int cols = columns();
		int gridSize = cellSize();
		int gap = Math.max(4, gridSize / 12);
		int gridX = (Options.getWidth() - cols * (gridSize + gap) + gap) / 2;
		float slotSize = Options.getCardLength() * 0.32f;
		int gridY = (int) (Options.getHeight() * 0.14f + slotSize + Options.getHeight() * 0.04f);
		if (x < gridX || y < gridY)
			return -1;
		int col = (x - gridX) / (gridSize + gap);
		int row = (y - gridY) / (gridSize + gap) + scrollRow;
		if (col < 0 || col >= cols)
			return -1;
		int index = row * cols + col;
		if (index < 0 || index >= bag.size())
			return -1;
		int cellX = gridX + col * (gridSize + gap);
		int cellY = gridY + (row - scrollRow) * (gridSize + gap);
		if (x > cellX + gridSize || y > cellY + gridSize)
			return -1;
		return index;
	}

	private int hitShownCard(int x, int y) {
		float size = Options.getCardLength() * 0.42f;
		float gap = size * 0.12f;
		int n = shownIds.length;
		if (n <= 0)
			return -1;
		float total = n * size + (n - 1) * gap;
		float x0 = (Options.getWidth() - total) / 2f;
		float top = Options.getHeight() * 0.36f;
		if (y < top || y > top + size)
			return -1;
		for (int i = 0; i < n; i++) {
			float sx = x0 + i * (size + gap);
			if (x >= sx && x < sx + size)
				return i;
		}
		return -1;
	}
}
