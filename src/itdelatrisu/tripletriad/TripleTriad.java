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

import itdelatrisu.tripletriad.ai.AI;
import itdelatrisu.tripletriad.ai.BalancedAI;
import itdelatrisu.tripletriad.ai.DefensiveAI;
import itdelatrisu.tripletriad.ai.OffensiveAI;
import itdelatrisu.tripletriad.ai.RandomAI;
import itdelatrisu.tripletriad.ui.ChampionshipScreen;
import itdelatrisu.tripletriad.ui.DeckBuilderScreen;
import itdelatrisu.tripletriad.ui.DeckSelectScreen;
import itdelatrisu.tripletriad.ui.HowToPlayScreen;
import itdelatrisu.tripletriad.ui.MenuScreen;
import itdelatrisu.tripletriad.ui.MyDeckScreen;
import itdelatrisu.tripletriad.ui.ProfileScreen;
import itdelatrisu.tripletriad.ui.ProfilesScreen;
import itdelatrisu.tripletriad.ui.SaveScreen;
import itdelatrisu.tripletriad.ui.Screen;
import itdelatrisu.tripletriad.ui.SettingsScreen;
import itdelatrisu.tripletriad.ui.Ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import itdelatrisu.tripletriad.gfx.AppGameContainer;
import itdelatrisu.tripletriad.gfx.BasicGame;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.gfx.SlickException;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Main class.
 */
public class TripleTriad extends BasicGame {
	/** Player/opponent constants. */
	public static final boolean PLAYER = true, OPPONENT = false;

	/** Wait time unit, in milliseconds, between actions. */
	private static final int WAIT_TIME = 1000;

	/** Delay timer. */
	private int timer;

	/** The deck of cards. */
	private Deck deck;

	/** Current board. */
	private Card[] board;

	/** Elements on board. */
	private Element[] elements;

	/** Original hands. */
	private Card[] playerCards, opponentCards;

	/** Current hands. */
	private ArrayList<Card> playerHand, opponentHand;

	/** The AIs. */
	private AI playerAI, opponentAI;

	/** Current card result. */
	private CardResult result;

	/** Whether the currently processing result is a "Combo". */
	private boolean isCombo;

	/** Score. */
	private int playerScore, opponentScore;

	/** Turn (PLAYER or OPPONENT). */
	private boolean turn;

	/** Selected card index. */
	private int selectedCard;

	/** Selected board position. */
	private int selectedPosition;

	/** Whether the game has loaded. */
	private boolean init;

	/** Card loading: current count. */
	private int loadCardCount;

	/** Card loading: current offset. */
	private float loadCardOffset;

	/** Alpha level for special text images. */
	private float textAlpha;

	/** Spinner. */
	private Spinner spinner;

	/** True while the leave-match confirmation overlay is open. */
	private boolean leaveConfirm;

	/** 0 = stay, 1 = leave. */
	private int leaveChoice;

	/** Game container. */
	private GameContainer container;

	/** Active screen. */
	private GameScreen currentScreen;

	/** Player profile. */
	private Profile profile;

	/** Active profile file id. */
	private int profileId;

	/** Menu screens. */
	private ProfileScreen profileScreen;
	private Screen menuScreen, deckSelectScreen, howToPlayScreen, settingsScreen;
	private ProfilesScreen profilesScreen;
	private DeckBuilderScreen deckBuilderScreen;
	private ChampionshipScreen championshipScreen;
	private MyDeckScreen myDeckScreen;
	private SaveScreen saveScreen;

	/** Card IDs for the player's current match deck. */
	private int[] currentPlayerDeckIds;

	/** Card IDs for the opponent's current match deck (championship). */
	private int[] currentOpponentDeckIds;

	/** Active championship run, or null. */
	private ChampionshipRun championship;

	/** Opponent AI override for the current championship round. */
	private Options.AIType championshipOpponentAI;

	public TripleTriad() {
		super("Triple Triad");
	}

	public static void main(String[] args) {
		// log all errors to a file
		Log.setVerbose(false);
		try {
			Log.setOut(new PrintStream(new FileOutputStream(Options.LOG_FILE, true)));
		} catch (FileNotFoundException e) {
			Log.error(e);
		}
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				Log.error("** Uncaught Exception! **", e);
			}
		});

		// parse configuration file
		Options.parseOptions();

		// start the game
		try {
			AppGameContainer app = new AppGameContainer(new TripleTriad());

			// game settings
			Options.setDisplayMode(app);
			String[] icons = { "icon16.png", "icon32.png" };
			app.setIcons(icons);

			app.start();
		} catch (SlickException e) {
			// JARs will not run properly inside directories containing '!'
			// http://bugs.java.com/view_bug.do?bug_id=4523159
			if (new File("").getAbsolutePath().contains("!"))
				Log.error("Cannot run JAR from path containing '!'.");
			else
				Log.error("Error while creating game container.", e);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;

		// initialize everything
		Options.init(container);
		AudioController.init();
		GameImage.init();
		Element.init();
		Spinner.init();

		// build deck
		this.deck = new Deck();

		this.profileScreen = new ProfileScreen(this);
		this.profilesScreen = new ProfilesScreen(this);
		this.menuScreen = new MenuScreen(this);
		this.deckSelectScreen = new DeckSelectScreen(this);
		this.deckBuilderScreen = new DeckBuilderScreen(this);
		this.howToPlayScreen = new HowToPlayScreen(this);
		this.settingsScreen = new SettingsScreen(this);
		this.championshipScreen = new ChampionshipScreen(this);
		this.myDeckScreen = new MyDeckScreen(this);
		this.saveScreen = new SaveScreen(this);

		loadActiveProfile();
		if (profile != null && profile.isValid())
			showMenu();
		else
			showProfile();
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (currentScreen != GameScreen.MATCH) {
			getActiveScreen().render(container, g);
			return;
		}

		int width = container.getWidth();
		int height = container.getHeight();
		int cardLength = Options.getCardLength();

		// board
		GameImage.BOARD_MAT.getImage().drawCentered(width / 2, height / 2);

		// card loading
		if (!init) {
			for (int i = 0, N = Math.min(loadCardCount, 5); i < N; i++)
				opponentHand.get(i).drawInHand(i, false);
			if (loadCardCount >= 5) {
				for (int i = 0, N = loadCardCount - 5; i < N; i++)
					playerHand.get(i).drawInHand(i, false);
			}

			if (loadCardCount < 5)
				opponentHand.get(loadCardCount).drawInHand(loadCardOffset, false);
			else if (loadCardCount < 10)
				playerHand.get(loadCardCount % 5).drawInHand(loadCardOffset, false);
			else if (timer < 1000)
				spinner.drawCentered(width / 2, height / 2);
			else
				spinner.getFrame((turn == PLAYER) ? 1 : 3).drawCentered(width / 2, height / 2);
			drawLeaveConfirm(g, height);
			return;
		}

		// cards (hand)
		boolean isPlayerTurn = (turn == PLAYER);
		boolean noSelect = (result != null || isGameOver());
		for (int i = 0, N = playerHand.size(); i < N; i++)
			playerHand.get(i).drawInHand(i + (5 - N), (isPlayerTurn && selectedCard == i && !noSelect));
		for (int i = 0, N = opponentHand.size(); i < N; i++)
			opponentHand.get(i).drawInHand(i + (5 - N), (!isPlayerTurn && selectedCard == i && !noSelect));

		// cards (board)
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null)
				board[i].drawOnBoard();
		}

		// spinner
		if (!isGameOver()) {
			spinner.drawCentered(
				(width / 2) + ((cardLength * 1.95f) * ((turn == PLAYER) ? 1 : -1)),
				(height / 2) - (cardLength * 1.5f)
			);
		}

		// score
		float scoreHeight = (height / 2) + (cardLength * 1.4f);
		GameImage.getScore(playerScore).getImage().drawCentered(
			(width / 2) + (cardLength * 2.1f), scoreHeight
		);
		GameImage.getScore(opponentScore).getImage().drawCentered(
			(width / 2) - (cardLength * 2.1f), scoreHeight
		);

		// elements
		if (elements != null) {
			for (int i = 0; i < elements.length; i++)
				elements[i].drawOnBoard(i, board[i]);
		}

		// card result
		if (result != null) {
			Image img = null;
			if (isCombo)
				img = GameImage.SPECIAL_COMBO.getImage();
			else if (result.isSame())
				img = GameImage.SPECIAL_SAME.getImage();
			else
				img = GameImage.SPECIAL_PLUS.getImage();
			if (img != null) {
				img.setAlpha(textAlpha);
				img.drawCentered(width / 2, height / 2);
			}
			drawLeaveConfirm(g, height);
			return;
		}

		// game over
		if (isGameOver()) {
			GameImage result =
				(playerScore > opponentScore) ? GameImage.RESULT_WIN :
				(playerScore < opponentScore) ? GameImage.RESULT_LOSE :
				                                GameImage.RESULT_DRAW;
			result.getImage().setAlpha(textAlpha);
			result.getImage().drawCentered(width / 2, height / 2);
			drawLeaveConfirm(g, height);
			return;
		}

		// player turn...
		if (isPlayerTurn && !playerHand.isEmpty()) {
			// cursor
			Image cursor = GameImage.CURSOR.getImage();
			cursor.setAlpha(1f);
			if (selectedPosition != -1) {
				cursor.drawCentered(
					(width / 2) - ((1 - (selectedPosition % 3)) * cardLength) - cursor.getWidth(),
					(height / 2) - ((1 - (selectedPosition / 3)) * cardLength)
				);
				cursor.setAlpha(0.5f);
			}
			int pos = selectedCard + (5 - playerHand.size());
			cursor.draw(
				(width / 2) + (cardLength * 1.5f) - (cursor.getWidth() / 1.25f),
				(height / 2) - cardLength + (pos * cardLength / 2f)
			);

			// card name
			String name;
			if (selectedPosition != -1) {
				if (board[selectedPosition] != null)
					name = board[selectedPosition].getName();
				else
					name = "";
			} else
				name = playerHand.get(selectedCard).getName();
			if (!name.isEmpty()) {
				Image infoBox = GameImage.INFO_BOX.getImage();
				float infoX = (width - infoBox.getWidth()) / 2;
				float infoY = (height - infoBox.getHeight()) / 2 + (cardLength * 1.4f);
				infoBox.draw(infoX, infoY);
				GameImage.INFO_TEXT.getImage().draw(
					infoX + (infoBox.getWidth() * 0.015f),
					infoY + (infoBox.getHeight() * 0.015f)
				);
				Options.getFont().drawString(
					(width - Options.getFont().getWidth(name)) / 2,
					infoY + ((infoBox.getHeight() - Options.getFont().getLineHeight()) / 2),
					name, Color.white
				);
			}
		}
		drawLeaveConfirm(g, height);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		if (currentScreen != GameScreen.MATCH) {
			getActiveScreen().update(container, delta);
			return;
		}

		if (leaveConfirm)
			return;

		// card loading
		if (!init) {
			// sound effect timer
			if (timer > 0) {
				if (timer < 1500)  // "start" sound effect length
					timer += delta;
				else {  // start game
					timer = 0;
					spinner.setSpeed(1f);
					init = true;
				}
				return;
			}

			// next card
			int step = Math.min(delta, 50);
			int targetOffset = loadCardCount % 5;
			if (loadCardOffset > targetOffset)
				loadCardOffset -= (step / 25f);

			if (loadCardOffset <= targetOffset) {
				if (++loadCardCount > 9) {  // finished animating: play sound effect
					AudioController.Effect.START.play();
					timer = 1;
				} else {
					loadCardOffset = 3 + (float) container.getHeight() / Options.getCardLength();
					AudioController.Effect.CARD.playReplacing();
				}
			}
			return;
		}

		// card result
		if (result != null) {
			// card playing
			if (Card.isCardPlaying()) {
				Card.update(delta);
				if (!Card.isCardPlaying()) {
					// change card owners and adjust score
					if (result.isSame()) {
						AudioController.Effect.SPECIAL.play();
						cardResult(result.getSameList());
					} else if (result.isPlus()) {
						AudioController.Effect.SPECIAL.play();
						cardResult(result.getPlusList());
					}
					if (result.hasCapture())
						cardResult(result.getCapturedList());
				}
				return;
			}

			Card.update(delta);
			if (!result.isSame() && !result.isPlus()) {
				if (!Card.isColorChange()) {  // finish color change animation
					result = null;
					turn = !turn;
				}
				return;
			}

			if (textAlpha < 1f)  // main text ("same" or "plus")
				textAlpha += (delta / 500f);
			else if (result.hasCombo()) {  // combo action and text
				if (timer < WAIT_TIME / 2)  // delay
					timer += delta;
				else {
					timer = 0;
					cardResult(result.nextCombo());
					if (!isCombo) {
						textAlpha = 0f;
						isCombo = true;
					}
				}
			} else if (timer < WAIT_TIME / 2)  // delay
				timer += delta;
			else if (!Card.isColorChange()) {  // reset
				textAlpha = 0f;
				timer = 0;
				result = null;
				isCombo = false;
				turn = !turn;
			}
			return;
		}

		// game over
		if (isGameOver()) {
			// fade in result
			if (textAlpha < 1f)
				textAlpha += (delta / 750f);

			// championship draw rematches original hands; Sudden Death keeps current owners
			else if (isDrawThatRestarts()) {
				if (timer < WAIT_TIME / 2)
					timer += delta;
				else
					restart(championship != null);
			}
			return;
		}

		// opponent turn
		if (turn == OPPONENT) {
			if (timer == 0) {  // calculate next move
				opponentAI.update(opponentScore, playerScore);
				timer += delta;
			} else if (timer < WAIT_TIME) {  // delay, move card
				int nextIndex = opponentAI.nextIndex();
				if (selectedCard < nextIndex &&
					timer >= (selectedCard + 1) * WAIT_TIME / (nextIndex + 1))
					selectedCard++;
				timer += delta;
			} else {  // play card
				playCard(opponentHand, opponentAI.nextIndex(), opponentAI.nextPosition());
				timer = 0;
			}
			return;
		}
	}

	@Override
	public boolean closeRequested() {
		Options.saveOptions();
		return true;
	}

	@Override
	public void keyPressed(int key, char c) {
		if (currentScreen != GameScreen.MATCH) {
			if (key == Input.KEY_ESCAPE &&
				(currentScreen == GameScreen.MENU
					|| (currentScreen == GameScreen.PROFILE && profileScreen.quitsOnEscape()))) {
				Options.saveOptions();
				container.exit();
				return;
			}
			getActiveScreen().keyPressed(key, c);
			return;
		}

		// confirm leaving the match (Esc)
		if (key == Input.KEY_ESCAPE) {
			if (leaveConfirm) {
				leaveConfirm = false;
				AudioController.Effect.BACK.play();
			} else {
				leaveConfirm = true;
				leaveChoice = 0;
				AudioController.Effect.BACK.play();
			}
			return;
		}

		if (leaveConfirm) {
			handleLeaveConfirmKey(key);
			return;
		}

		// rematch with the same player deck (Quick Game only)
		if (key == Input.KEY_F5) {
			if (championship == null && currentPlayerDeckIds != null)
				restart(true);
			return;
		}

		// after the match
		if (isGameOver() && !isDrawThatRestarts() &&
			textAlpha >= 1f && result == null &&
			(key == Input.KEY_Z || key == Input.KEY_ENTER)) {
			if (championship != null)
				championshipMatchFinished();
			else
				showMenu();
			return;
		}

		// not player turn
		if (turn != PLAYER || !init || result != null || isGameOver())
			return;

		switch (key) {
		case Input.KEY_DOWN:
			if (selectedPosition == -1) {
				selectedCard = (selectedCard + 1) % playerHand.size();
				AudioController.playCursor();
			} else {
				if (selectedPosition < 6) {
					selectedPosition += 3;
					AudioController.playCursor();
				}
			}
			break;
		case Input.KEY_UP:
			if (selectedPosition == -1) {
				int size = playerHand.size();
				selectedCard = (selectedCard + (size - 1)) % size;
				AudioController.playCursor();
			} else {
				if (selectedPosition > 2) {
					selectedPosition -= 3;
					AudioController.playCursor();
				}
			}
			break;
		case Input.KEY_LEFT:
			if (selectedPosition != -1 && selectedPosition % 3 != 0) {
				selectedPosition--;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_RIGHT:
			if (selectedPosition != -1 && selectedPosition % 3 != 2) {
				selectedPosition++;
				AudioController.playCursor();
			}
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (selectedPosition == -1) {
				selectedPosition = 4;
				AudioController.Effect.SELECT.play();
			} else {
				if (playCard(playerHand, selectedCard, selectedPosition))
					AudioController.Effect.SELECT.play();
				else
					AudioController.Effect.INVALID.play();
			}
			break;
		case Input.KEY_X:
		case Input.KEY_BACK:
			if (selectedPosition != -1) {
				selectedPosition = -1;
				AudioController.Effect.BACK.play();
			}
			break;
		case Input.KEY_F1:
			playerAI.update(playerScore, opponentScore);
			selectedCard = playerAI.nextIndex();
			selectedPosition = playerAI.nextPosition();
			playCard(playerHand, selectedCard, selectedPosition);
			AudioController.Effect.SELECT.play();
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;

		if (currentScreen != GameScreen.MATCH) {
			getActiveScreen().mousePressed(button, x, y);
			return;
		}

		if (leaveConfirm) {
			handleLeaveConfirmClick(y);
			return;
		}

		// after the match
		if (isGameOver() && !isDrawThatRestarts() &&
			textAlpha >= 1f && result == null) {
			if (championship != null)
				championshipMatchFinished();
			else
				showMenu();
			return;
		}

		// not player turn
		if (turn != PLAYER || !init || result != null || isGameOver())
			return;

		int cardLength = Options.getCardLength();
		int centerX = container.getWidth() / 2;
		int centerY = container.getHeight() / 2;

		// player hand
		for (int i = 0, handSize = playerHand.size(); i < handSize; i++) {
			int index = handSize - i - 1;
			int posX = centerX + (int) (cardLength * ((selectedCard == index) ? 1.45f : 1.6f));
			int posY = centerY - ((i - 1) * cardLength / 2);
			if (x >= posX && x < posX + cardLength &&
				y >= posY && y < posY + cardLength) {
				if (selectedCard == index) {
					if (selectedPosition == -1) {
						selectedPosition = 4;
						AudioController.Effect.SELECT.play();
					} else {
						selectedPosition = -1;
						AudioController.Effect.BACK.play();
					}
				} else {
					selectedCard = index;
					selectedPosition = -1;
					AudioController.playCursor();
				}
				return;
			}
		}

		// board
		int centerOffset = cardLength * 3 / 2;
		if (x >= centerX - centerOffset && x < centerX + centerOffset &&
			y >= centerY - centerOffset && y < centerY + centerOffset) {
			int boardPosition =
					(x - (centerX - centerOffset)) / cardLength +
					(y - (centerY - centerOffset)) / cardLength * 3;
			if (selectedPosition != boardPosition) {
				selectedPosition = boardPosition;
				AudioController.playCursor();
			} else if (playCard(playerHand, selectedCard, boardPosition))
				AudioController.Effect.SELECT.play();
			else
				AudioController.Effect.INVALID.play();
			return;
		}
	}

	/**
	 * Draw rematches instead of ending: Sudden Death (any mode) or always in championship.
	 * @return true if a draw should restart the board
	 */
	private boolean isDrawThatRestarts() {
		return playerScore == opponentScore &&
			(Rule.SUDDEN_DEATH.isActive() || championship != null);
	}

	/**
	 * Re-initializes the game.
	 * @param newHand whether or not to generate new hands (e.g. false for Sudden Death)
	 */
	private void restart(boolean newHand) {
		if (newHand) {
			playerCards = new Card[5];
			opponentCards = new Card[5];
			if (currentPlayerDeckIds != null && currentPlayerDeckIds.length == 5)
				deck.buildHand(currentPlayerDeckIds, playerCards, PLAYER);
			else
				deck.buildRandomHand(playerCards, PLAYER);
			if (currentOpponentDeckIds != null && currentOpponentDeckIds.length == 5)
				deck.buildHand(currentOpponentDeckIds, opponentCards, OPPONENT);
			else
				deck.buildRandomHand(opponentCards, OPPONENT);
			playerHand = new ArrayList<Card>(Arrays.asList(playerCards));
			opponentHand = new ArrayList<Card>(Arrays.asList(opponentCards));
			spinner = Spinner.getRandomSpinner();
			spinner.setSpeed(5f);
			init = false;
		} else {
			playerHand.clear();
			opponentHand.clear();

			// build new hands from owned cards
			for (int i = 0; i < 5; i++) {
				// determine new owners
				if (playerCards[i].getOwner() == PLAYER)
					playerHand.add(playerCards[i]);
				else
					opponentHand.add(playerCards[i]);
				if (opponentCards[i].getOwner() == PLAYER)
					playerHand.add(opponentCards[i]);
				else
					opponentHand.add(opponentCards[i]);

				// reset the card positions
				playerCards[i].resetPosition();
				opponentCards[i].resetPosition();
			}
		}

		// reset game data
		board = new Card[9];
		elements = (Rule.ELEMENTAL.isActive()) ? Element.getRandomBoard() : null;
		switch ((championshipOpponentAI != null) ? championshipOpponentAI : Options.getOpponentAI()) {
			case RANDOM: opponentAI = new RandomAI(opponentHand, board, elements); break;
			case OFFENSIVE: opponentAI = new OffensiveAI(opponentHand, board, elements); break;
			case DEFENSIVE: opponentAI = new DefensiveAI(opponentHand, board, elements); break;
			case BALANCED: opponentAI = new BalancedAI(opponentHand, board, elements); break;
		}
		switch (Options.getPlayerAI()) {
			case RANDOM: playerAI = new RandomAI(playerHand, board, elements); break;
			case OFFENSIVE: playerAI = new OffensiveAI(playerHand, board, elements); break;
			case DEFENSIVE: playerAI = new DefensiveAI(playerHand, board, elements); break;
			case BALANCED: playerAI = new BalancedAI(playerHand, board, elements); break;
		}
		result = null;
		isCombo = false;
		playerScore = opponentScore = 5;
		turn = new Random().nextBoolean();
		selectedCard = 0;
		selectedPosition = -1;
		timer = 0;
		loadCardCount = 0;
		loadCardOffset = 3 + (float) container.getHeight() / Options.getCardLength();
		textAlpha = 0f;
		leaveConfirm = false;
		leaveChoice = 0;
		Card.resetAnimations();
	}

	/**
	 * Draws the leave-match confirmation overlay.
	 */
	private void drawLeaveConfirm(Graphics g, int height) {
		if (!leaveConfirm)
			return;
		g.setColor(new Color(0f, 0f, 0f, 0.72f));
		g.fillRect(0, 0, Options.getWidth(), height);
		Ui.drawCentered(Options.getFont(), I18n.confirmLeaveMatch(), height * 0.38f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), I18n.confirmLeaveNo(), height * 0.50f,
			leaveChoice == 0 ? Ui.SELECTED : Ui.HINT);
		Ui.drawCentered(Options.getSmallFont(), I18n.confirmLeaveYes(), height * 0.58f,
			leaveChoice == 1 ? Ui.SELECTED : Ui.HINT);
		Ui.drawCentered(Options.getSmallFont(), I18n.hintLeaveConfirm(), height * 0.78f, Ui.HINT);
	}

	/**
	 * Handles keys on the leave confirmation overlay.
	 */
	private void handleLeaveConfirmKey(int key) {
		switch (key) {
		case Input.KEY_UP:
		case Input.KEY_DOWN:
			leaveChoice = 1 - leaveChoice;
			AudioController.playCursor();
			break;
		case Input.KEY_X:
		case Input.KEY_BACK:
			leaveConfirm = false;
			AudioController.Effect.BACK.play();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (leaveChoice == 1) {
				AudioController.Effect.SELECT.play();
				leaveConfirm = false;
				showMenu();
			} else {
				leaveConfirm = false;
				AudioController.Effect.BACK.play();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Handles a click on the leave confirmation overlay.
	 */
	private void handleLeaveConfirmClick(int y) {
		UnicodeFont small = Options.getSmallFont();
		float noY = Options.getHeight() * 0.50f;
		float yesY = Options.getHeight() * 0.58f;
		float h = small.getLineHeight();
		if (y >= noY && y < noY + h) {
			if (leaveChoice != 0) {
				leaveChoice = 0;
				AudioController.playCursor();
				return;
			}
			leaveConfirm = false;
			AudioController.Effect.BACK.play();
			return;
		}
		if (y >= yesY && y < yesY + h) {
			if (leaveChoice != 1) {
				leaveChoice = 1;
				AudioController.playCursor();
				return;
			}
			AudioController.Effect.SELECT.play();
			leaveConfirm = false;
			showMenu();
		}
	}

	/**
	 * Returns whether or not the game is over.
	 * @return true if over
	 */
	private boolean isGameOver() {
		return playerHand != null && opponentHand != null &&
			(playerHand.isEmpty() || opponentHand.isEmpty());
	}

	/**
	 * Plays a card, unless the board position is occupied by another card.
	 * @param hand the hand of cards
	 * @param index the index in the hand [0, 4]
	 * @param position the position on the board [0, 8]
	 * @return true if a card was played, false if position already taken
	 */
	private boolean playCard(ArrayList<Card> hand, int index, int position) {
		if (board[position] != null)
			return false;

		// set card
		Card card = hand.get(index);
		card.playAtPosition(position, index);
		board[position] = card;
		AudioController.Effect.CARD.play();
		hand.remove(index);
		selectedCard = 0;
		selectedPosition = -1;

		// calculate the results
		result = new CardResult(card, position, board, elements);

		return true;
	}

	/**
	 * Processes a card result by changing card owners and adjusting score.
	 * @param resultList the list of affected cards
	 */
	private void cardResult(ArrayList<Card> resultList) {
		boolean owner = result.getSourceCard().getOwner();
		for (Card c : resultList) {
			if (c.getOwner() != owner) {
				c.changeOwner();
				if (c.getOwner() == PLAYER) {
					playerScore++;
					opponentScore--;
				} else {
					playerScore--;
					opponentScore++;
				}
			}
		}
		AudioController.Effect.TURN.play();
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (currentScreen != GameScreen.MATCH)
			getActiveScreen().mouseWheelMoved(change);
	}

	/**
	 * Returns the active non-match screen.
	 */
	private Screen getActiveScreen() {
		switch (currentScreen) {
			case PROFILE: return profileScreen;
			case PROFILES: return profilesScreen;
			case DECK_SELECT: return deckSelectScreen;
			case DECK_BUILDER: return deckBuilderScreen;
			case HOW_TO_PLAY: return howToPlayScreen;
			case SETTINGS: return settingsScreen;
			case MY_DECK: return myDeckScreen;
			case SAVE: return saveScreen;
			case CHAMPIONSHIP: return championshipScreen;
			case MENU:
			default: return menuScreen;
		}
	}

	/**
	 * Returns the loaded card catalog.
	 * @return the deck
	 */
	public Deck getDeck() { return deck; }

	/**
	 * Returns the current player profile.
	 * @return the profile, or null
	 */
	public Profile getProfile() { return profile; }

	/**
	 * Returns the active profile id.
	 * @return the id, or 0
	 */
	public int getProfileId() { return profileId; }

	/**
	 * Persists the current profile to disk.
	 */
	public void saveProfile() {
		ProfileStore.save(profileId, profile);
	}

	/**
	 * Creates the first profile and opens the menu.
	 * @param name the player name
	 * @return true if created
	 */
	public boolean createProfile(String name) {
		if (!canUseProfileName(name, 0))
			return false;
		int id = ProfileStore.nextId();
		profile = new Profile();
		profile.setName(name);
		profileId = id;
		Options.setActiveProfileId(id);
		Options.saveOptions();
		ProfileStore.save(id, profile);
		showMenu();
		return true;
	}

	/**
	 * Creates an extra profile, switches to it, and opens the menu.
	 * @param name the player name
	 * @return true if created
	 */
	public boolean createAdditionalProfile(String name) {
		if (!canUseProfileName(name, 0))
			return false;
		saveProfile();
		int id = ProfileStore.nextId();
		profile = new Profile();
		profile.setName(name);
		profileId = id;
		clearMatchState();
		Options.setActiveProfileId(id);
		Options.saveOptions();
		ProfileStore.save(id, profile);
		showMenu();
		return true;
	}

	/**
	 * Renames the active profile.
	 * @param name the new name
	 * @return true if renamed
	 */
	public boolean renameProfile(String name) {
		if (profile == null || !canUseProfileName(name, profileId))
			return false;
		profile.setName(name);
		saveProfile();
		showSettings();
		return true;
	}

	/**
	 * Switches to another profile and opens the menu.
	 * @param id the profile id
	 * @return true if switched
	 */
	public boolean switchProfile(int id) {
		if (id <= 0 || id == profileId)
			return id == profileId;
		saveProfile();
		Profile next = ProfileStore.load(id);
		if (next == null || !next.isValid())
			return false;
		profile = next;
		profileId = id;
		clearMatchState();
		Options.setActiveProfileId(id);
		Options.saveOptions();
		showMenu();
		return true;
	}

	/**
	 * True if this profile can be deleted.
	 * @param id the profile id
	 * @return true if allowed
	 */
	public boolean canDeleteProfile(int id) {
		return id > 0 && id != profileId && ProfileStore.list().size() > 1;
	}

	/**
	 * Deletes a profile that is not the active one.
	 * @param id the profile id
	 * @return true if deleted
	 */
	public boolean deleteProfile(int id) {
		if (!canDeleteProfile(id))
			return false;
		return ProfileStore.delete(id);
	}

	private void loadActiveProfile() {
		ProfileStore.migrateIfNeeded();
		int id = Options.getActiveProfileId();
		Profile loaded = ProfileStore.load(id);
		if (loaded != null) {
			profile = loaded;
			profileId = id;
			return;
		}
		ArrayList<ProfileStore.Entry> entries = ProfileStore.list();
		if (entries.isEmpty()) {
			profile = null;
			profileId = 0;
			return;
		}
		profileId = entries.get(0).getId();
		profile = ProfileStore.load(profileId);
		if (profile != null) {
			Options.setActiveProfileId(profileId);
			Options.saveOptions();
		} else {
			profileId = 0;
		}
	}

	private boolean canUseProfileName(String name, int exceptId) {
		if (name == null || name.trim().isEmpty())
			return false;
		return !ProfileStore.nameTaken(name, exceptId);
	}

	private void clearMatchState() {
		championship = null;
		championshipOpponentAI = null;
		currentOpponentDeckIds = null;
		currentPlayerDeckIds = null;
	}

	/**
	 * Shows the first-run profile creation screen.
	 */
	public void showProfile() {
		currentScreen = GameScreen.PROFILE;
		profileScreen.enterFirst();
	}

	/**
	 * Shows the rename-profile editor.
	 */
	public void showRenameProfile() {
		String current = (profile != null) ? profile.getName() : "";
		profileScreen.enterRename(current);
		currentScreen = GameScreen.PROFILE;
	}

	/**
	 * Shows the create-profile editor.
	 */
	public void showCreateProfile() {
		profileScreen.enterCreate();
		currentScreen = GameScreen.PROFILE;
	}

	/**
	 * Shows the profile list.
	 */
	public void showProfiles() {
		currentScreen = GameScreen.PROFILES;
		profilesScreen.enter();
	}

	/**
	 * Shows the main menu.
	 */
	public void showMenu() {
		championship = null;
		championshipOpponentAI = null;
		currentOpponentDeckIds = null;
		currentScreen = GameScreen.MENU;
		menuScreen.enter();
	}

	/**
	 * Shows the how-to-play screen.
	 */
	public void showHowToPlay() {
		currentScreen = GameScreen.HOW_TO_PLAY;
		howToPlayScreen.enter();
	}

	/**
	 * Shows the settings screen.
	 */
	public void showSettings() {
		currentScreen = GameScreen.SETTINGS;
		settingsScreen.enter();
	}

	/**
	 * Shows Meu Deck (album gallery).
	 */
	public void showMyDeck() {
		currentScreen = GameScreen.MY_DECK;
		myDeckScreen.enterGallery();
	}

	/**
	 * Shows export/import of album and championship saves.
	 */
	public void showSave() {
		currentScreen = GameScreen.SAVE;
		saveScreen.enter();
	}

	/**
	 * Shows pick-5 from Meu Deck for Quick Game.
	 */
	public void showMyDeckPick() {
		currentScreen = GameScreen.MY_DECK;
		myDeckScreen.enterPick();
	}

	/**
	 * Shows the Quick Game deck list.
	 */
	public void showDeckSelect() {
		currentScreen = GameScreen.DECK_SELECT;
		deckSelectScreen.enter();
	}

	/**
	 * Shows the deck builder.
	 * @param existing the deck to edit, or null to create a new one
	 */
	public void showDeckBuilder(SavedDeck existing) {
		deckBuilderScreen.edit(existing);
		currentScreen = GameScreen.DECK_BUILDER;
		deckBuilderScreen.enter();
	}

	/**
	 * Starts a Quick Game match against the computer.
	 * @param cardIds the player's five card IDs
	 */
	public void startQuickMatch(int[] cardIds) {
		if (cardIds == null || cardIds.length != SavedDeck.SIZE)
			return;
		championship = null;
		championshipOpponentAI = null;
		currentOpponentDeckIds = null;
		currentPlayerDeckIds = cardIds.clone();
		currentScreen = GameScreen.MATCH;
		restart(true);
	}

	/**
	 * Returns the active championship run.
	 * @return the run, or null
	 */
	public ChampionshipRun getChampionshipRun() { return championship; }

	/**
	 * Opens the championship lobby.
	 */
	public void showChampionship() {
		championship = null;
		championshipOpponentAI = null;
		currentOpponentDeckIds = null;
		currentScreen = GameScreen.CHAMPIONSHIP;
		championshipScreen.enter();
	}

	/**
	 * Starts a new championship. Empty Meu Deck gets a starter pack; otherwise pick 5 from the album.
	 */
	public void championshipNew() {
		if (profile == null)
			return;
		profile.clearChampionshipSave();
		championship = new ChampionshipRun();
		if (profile.getCollection().size() < SavedDeck.SIZE) {
			int[] pack = deck.createStarterPack();
			for (int i = 0; i < pack.length; i++) {
				profile.addCard(pack[i]);
				championship.addToBag(pack[i]);
			}
			championship.setPlayerHandIds(pack);
			saveProfile();
			currentScreen = GameScreen.CHAMPIONSHIP;
			championshipScreen.showPack(pack);
			return;
		}
		championship.setBag(profile.getCollection());
		currentScreen = GameScreen.CHAMPIONSHIP;
		championshipScreen.showPick();
	}

	/**
	 * True if Meu Deck has at least five cards.
	 * @return true if the album can be used as a championship/quick deck
	 */
	public boolean hasAlbumDeck() {
		return profile != null && profile.getCollection().size() >= SavedDeck.SIZE;
	}

	/**
	 * Removes one copy of a card from Meu Deck and writes the profile.
	 * @param cardId the catalog ID
	 */
	public void removeAlbumCard(int cardId) {
		if (profile == null || cardId <= 0)
			return;
		if (profile.removeCard(cardId))
			saveProfile();
	}

	/**
	 * Resumes a saved championship from match 2+.
	 */
	public void championshipContinue() {
		if (profile == null || !profile.hasChampionshipSave())
			return;
		championship = ChampionshipRun.fromProfile(profile);
		currentScreen = GameScreen.CHAMPIONSHIP;
		championshipScreen.showPick(profile.getRunHand());
	}

	/**
	 * Returns to the championship lobby without discarding the in-memory run.
	 */
	public void championshipBackToLobby() {
		currentScreen = GameScreen.CHAMPIONSHIP;
		championshipScreen.enter();
	}

	/**
	 * True if the in-memory run can be written (match 2+).
	 * @return true if Salvar is available
	 */
	public boolean canChampionshipSave() {
		return championship != null && championship.getRound() >= 2;
	}

	/**
	 * Writes collection and run to disk when the player chooses Salvar.
	 */
	public void championshipSave() {
		if (profile == null || !canChampionshipSave())
			return;
		profile.storeChampionshipRun(championship);
		saveProfile();
	}

	/**
	 * Clears the championship continue save. Keeps Meu Deck, cups and Quick Game decks.
	 */
	public void championshipClearProgress() {
		if (profile == null || !profile.hasChampionshipSave())
			return;
		profile.clearChampionshipSave();
		championship = null;
		championshipOpponentAI = null;
		currentOpponentDeckIds = null;
		saveProfile();
	}

	/**
	 * Continues after the match-1 pack: start the fight with those five cards.
	 */
	public void championshipPackConfirmed() {
		if (championship == null || championship.getPlayerHandIds() == null)
			return;
		championshipHandChosen(championship.getPlayerHandIds());
	}

	/**
	 * Begins a championship match with the chosen hand.
	 * @param ids five collection card IDs
	 */
	public void championshipHandChosen(int[] ids) {
		if (championship == null || ids == null || ids.length != SavedDeck.SIZE)
			return;
		championship.setPlayerHandIds(ids);
		if (championship.getOpponentHandIds() == null)
			championship.rollOpponent(deck);
		startChampionshipMatch(ids, championship.getOpponentHandIds());
	}

	/**
	 * Leaves championship UI without writing a save.
	 */
	public void abortChampionship() {
		showMenu();
	}

	/**
	 * Player took an opponent card after a win.
	 * @param cardId the stolen card
	 */
	public void championshipCardTaken(int cardId) {
		if (championship == null || profile == null)
			return;
		profile.addCard(cardId);
		championship.addToBag(cardId);
		boolean cup = championship.recordWin();
		if (cup) {
			profile.addChampionshipWin();
			profile.clearChampionshipSave();
			saveProfile();
			currentScreen = GameScreen.CHAMPIONSHIP;
			championshipScreen.showWon();
			return;
		}
		saveProfile();
		championship.rollOpponent(deck);
		currentScreen = GameScreen.CHAMPIONSHIP;
		championshipScreen.showPick();
	}

	/**
	 * Acknowledges the AI steal after a round 2+ loss.
	 */
	public void championshipStealAck() {
		if (championship == null || profile == null)
			return;
		if (championship.getBag().size() < SavedDeck.SIZE) {
			endChampionshipSave();
			currentScreen = GameScreen.CHAMPIONSHIP;
			championshipScreen.showLost();
			return;
		}
		currentScreen = GameScreen.CHAMPIONSHIP;
		championshipScreen.showPick();
	}

	/**
	 * Starts a championship match.
	 */
	private void startChampionshipMatch(int[] playerIds, int[] opponentIds) {
		currentPlayerDeckIds = playerIds.clone();
		currentOpponentDeckIds = opponentIds.clone();
		championshipOpponentAI = championship.aiForRound();
		currentScreen = GameScreen.MATCH;
		restart(true);
	}

	/**
	 * Routes a finished championship match to trade / next round / end.
	 */
	private void championshipMatchFinished() {
		if (championship == null || playerScore == opponentScore)
			return;
		boolean playerWon = playerScore > opponentScore;
		int round = championship.getRound();
		currentScreen = GameScreen.CHAMPIONSHIP;
		if (playerWon) {
			championshipScreen.showTradeWin(championship.getOpponentHandIds());
			return;
		}
		if (round == 1) {
			endChampionshipSave();
			championshipScreen.showLost();
			return;
		}
		int stolen = championship.stealBestPlayerCard(deck);
		championship.removeFromBag(stolen);
		championship.setPlayerHandIds(null);
		championshipScreen.showTradeLose(stolen);
	}

	/**
	 * Clears an in-progress championship save.
	 */
	private void endChampionshipSave() {
		if (profile == null)
			return;
		profile.clearChampionshipSave();
		saveProfile();
	}
}
