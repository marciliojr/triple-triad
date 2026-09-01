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

import java.io.File;

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.SaveTransfer;
import itdelatrisu.tripletriad.TripleTriad;

import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.UnicodeFont;

/**
 * Export and import the album or the championship checkpoint.
 */
public class SaveScreen extends Screen {
	/** Type row. */
	private static final int ROW_KIND = 0;

	/** Export row. */
	private static final int ROW_EXPORT = 1;

	/** Import row. */
	private static final int ROW_IMPORT = 2;

	/** Number of rows. */
	private static final int ROW_COUNT = 3;

	/** Album / Meu Deck. */
	private static final int KIND_DECK = 0;

	/** Championship run. */
	private static final int KIND_CHAMPIONSHIP = 1;

	/** Panel width as a fraction of the screen. */
	private static final float PANEL_WIDTH_FRAC = 0.74f;

	/** Game instance. */
	private final TripleTriad game;

	/** Selected row. */
	private int selected;

	/** Deck vs championship. */
	private int kind = KIND_DECK;

	/** True while replace confirmation is open. */
	private boolean confirm;

	/** 0 = No, 1 = Yes. */
	private int confirmChoice;

	/** Pending import, or null. */
	private SaveTransfer.Snapshot pending;

	/** Footer notice until this epoch ms. */
	private long noticeUntil;

	/** Footer notice text. */
	private String notice;

	/**
	 * Constructor.
	 * @param game the game
	 */
	public SaveScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		selected = 0;
		confirm = false;
		confirmChoice = 0;
		pending = null;
		noticeUntil = 0L;
		notice = null;
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		Ui.drawBackdrop(g);
		UnicodeFont font = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		int height = container.getHeight();

		Ui.drawCentered(font, I18n.saveTitle(), height * 0.08f, Ui.TITLE);

		float panelW = panelW();
		float panelH = panelH();
		float panelX = panelX();
		float panelY = panelY();
		g.setColor(Ui.TITLE);
		g.setLineWidth(2f);
		g.drawRect(panelX, panelY, panelW, panelH);
		g.setLineWidth(1f);

		float ruleLine = ruleLine();
		float ruleStart = ruleStart();
		float nameX = nameX();
		float valuePad = 24f;
		float nameValueGap = 16f;
		Image cursor = GameImage.CURSOR.getImage();
		for (int i = 0; i < ROW_COUNT; i++) {
			float y = ruleStart + i * ruleLine;
			boolean on = (i == selected);
			String name = rowName(i);
			String value = rowValue(i);
			Color nameColor = on ? Ui.SELECTED : Ui.HINT;
			float valueX = panelX + panelW - valuePad - small.getWidth(value);
			float nameMax = Math.max(24f, valueX - nameValueGap - nameX);
			String nameFit = Ui.fit(small, name, nameMax);
			small.drawString(nameX, y, nameFit, nameColor);
			small.drawString(valueX, y, value, on ? Ui.SELECTED : Ui.TITLE);
			if (on) {
				cursor.draw(
					panelX + 16,
					y + (small.getLineHeight() - cursor.getHeight()) / 2f
				);
			}
		}

		if (confirm)
			Ui.drawYesNoConfirm(g, confirmTitle(), confirmChoice);
		else {
			Ui.drawCentered(small, I18n.hintSave(), height * 0.88f, Ui.HINT);
			if (notice != null && System.currentTimeMillis() < noticeUntil)
				Ui.drawCentered(small, notice, height * 0.93f, Ui.TITLE);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if (confirm) {
			handleConfirmKey(key);
			return;
		}
		switch (key) {
		case Input.KEY_ESCAPE:
			AudioController.Effect.BACK.play();
			game.showMenu();
			break;
		case Input.KEY_DOWN:
			selected = (selected + 1) % ROW_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_UP:
			selected = (selected + ROW_COUNT - 1) % ROW_COUNT;
			AudioController.playCursor();
			break;
		case Input.KEY_LEFT:
			if (selected == ROW_KIND)
				cycleKind(false);
			break;
		case Input.KEY_RIGHT:
			if (selected == ROW_KIND)
				cycleKind(true);
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			activate();
			break;
		default:
			break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		if (confirm) {
			handleConfirmClick(y);
			return;
		}
		int row = hitRow(x, y);
		if (row < 0)
			return;
		if (row != selected) {
			selected = row;
			AudioController.playCursor();
			return;
		}
		activate();
	}

	private void activate() {
		if (selected == ROW_KIND) {
			cycleKind(true);
			return;
		}
		if (selected == ROW_EXPORT)
			exportSave();
		else
			importSave();
	}

	private void cycleKind(boolean forward) {
		kind = forward ? 1 - kind : 1 - kind;
		AudioController.playCursor();
	}

	private void exportSave() {
		Profile profile = game.getProfile();
		if (profile == null || !profile.isValid()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		if (kind == KIND_CHAMPIONSHIP && !profile.hasChampionshipSave()) {
			showNotice(I18n.saveNoChampionship());
			AudioController.Effect.INVALID.play();
			return;
		}
		String defaultName = (kind == KIND_DECK)
			? I18n.saveFileDeck() : I18n.saveFileChampionship();
		File file = SaveTransfer.chooseExportFile(I18n.saveDialogExport(), defaultName);
		if (file == null)
			return;
		boolean ok = (kind == KIND_DECK)
			? SaveTransfer.writeDeck(file, profile)
			: SaveTransfer.writeChampionship(file, profile);
		if (ok) {
			showNotice(I18n.saveExported());
			AudioController.Effect.SELECT.play();
		} else {
			showNotice(I18n.saveBadFile());
			AudioController.Effect.INVALID.play();
		}
	}

	private void importSave() {
		Profile profile = game.getProfile();
		if (profile == null || !profile.isValid()) {
			AudioController.Effect.INVALID.play();
			return;
		}
		File file = SaveTransfer.chooseImportFile(I18n.saveDialogImport());
		if (file == null)
			return;
		SaveTransfer.Snapshot snap = SaveTransfer.read(file);
		if (snap == null) {
			showNotice(I18n.saveBadFile());
			AudioController.Effect.INVALID.play();
			return;
		}
		SaveTransfer.Kind expected = (kind == KIND_DECK)
			? SaveTransfer.Kind.DECK : SaveTransfer.Kind.CHAMPIONSHIP;
		if (snap.getKind() != expected) {
			showNotice(I18n.saveWrongKind());
			AudioController.Effect.INVALID.play();
			return;
		}
		if (kind == KIND_DECK && !snap.isDeckValid()) {
			showNotice(I18n.saveBadFile());
			AudioController.Effect.INVALID.play();
			return;
		}
		if (kind == KIND_CHAMPIONSHIP && !snap.isChampionshipValid()) {
			showNotice(I18n.saveBadFile());
			AudioController.Effect.INVALID.play();
			return;
		}
		pending = snap;
		confirmChoice = 0;
		confirm = true;
		AudioController.Effect.SELECT.play();
	}

	private void handleConfirmKey(int key) {
		switch (key) {
		case Input.KEY_UP:
		case Input.KEY_DOWN:
			confirmChoice = 1 - confirmChoice;
			AudioController.playCursor();
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_X:
		case Input.KEY_BACK:
			cancelConfirm();
			break;
		case Input.KEY_Z:
		case Input.KEY_ENTER:
			if (confirmChoice == 1)
				commitImport();
			else
				cancelConfirm();
			break;
		default:
			break;
		}
	}

	private void handleConfirmClick(int y) {
		int hit = Ui.hitYesNoConfirm(y);
		if (hit < 0)
			return;
		if (hit != confirmChoice) {
			confirmChoice = hit;
			AudioController.playCursor();
			return;
		}
		if (confirmChoice == 1)
			commitImport();
		else
			cancelConfirm();
	}

	private void commitImport() {
		Profile profile = game.getProfile();
		boolean ok = SaveTransfer.apply(profile, pending);
		pending = null;
		confirm = false;
		confirmChoice = 0;
		if (ok) {
			game.saveProfile();
			showNotice(I18n.saveImported());
			AudioController.Effect.SELECT.play();
		} else {
			showNotice(I18n.saveBadFile());
			AudioController.Effect.INVALID.play();
		}
	}

	private void cancelConfirm() {
		pending = null;
		confirm = false;
		confirmChoice = 0;
		AudioController.Effect.BACK.play();
	}

	private String confirmTitle() {
		return (kind == KIND_DECK)
			? I18n.saveConfirmReplaceDeck()
			: I18n.saveConfirmReplaceChampionship();
	}

	private void showNotice(String text) {
		notice = text;
		noticeUntil = System.currentTimeMillis() + 2500L;
	}

	private String rowName(int row) {
		if (row == ROW_KIND)
			return I18n.saveKind();
		if (row == ROW_EXPORT)
			return I18n.saveExport();
		return I18n.saveImport();
	}

	private String rowValue(int row) {
		if (row != ROW_KIND)
			return "";
		Profile profile = game.getProfile();
		if (kind == KIND_DECK) {
			int n = (profile != null) ? profile.getCollection().size() : 0;
			return I18n.saveKindDeck() + "  " + I18n.championshipCards(n);
		}
		if (profile != null && profile.hasChampionshipSave())
			return I18n.saveKindChampionship() + "  "
				+ I18n.championshipRoundOf(profile.getRunRound());
		return I18n.saveKindChampionship() + "  " + I18n.saveNone();
	}

	private int hitRow(int x, int y) {
		float panelX = panelX();
		float panelW = panelW();
		if (x < panelX || x > panelX + panelW)
			return -1;
		float ruleLine = ruleLine();
		float ruleStart = ruleStart();
		for (int i = 0; i < ROW_COUNT; i++) {
			float top = ruleStart + i * ruleLine;
			if (y >= top && y < top + ruleLine)
				return i;
		}
		return -1;
	}

	private float panelW() {
		return Options.getWidth() * PANEL_WIDTH_FRAC;
	}

	private float panelX() {
		return (Options.getWidth() - panelW()) / 2f;
	}

	private float panelY() {
		return Options.getHeight() * 0.22f;
	}

	private float panelH() {
		return topPad() + ROW_COUNT * ruleLine() + bottomPad();
	}

	private float ruleLine() {
		return Options.getSmallFont().getLineHeight() * 1.85f;
	}

	private float topPad() {
		return Options.getSmallFont().getLineHeight() * 1.4f;
	}

	private float bottomPad() {
		return Options.getSmallFont().getLineHeight() * 1.2f;
	}

	private float ruleStart() {
		return panelY() + topPad();
	}

	private float nameX() {
		Image cursor = GameImage.CURSOR.getImage();
		return panelX() + 16 + cursor.getWidth() * 1.15f + 8;
	}
}
