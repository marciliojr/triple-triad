/*
 * Triple Triad - a card game from FFVIII
 * Copyright (C) 2014 Jeffrey Han
 *
 * Triple Triad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package itdelatrisu.tripletriad.ui;

import java.util.ArrayList;

import itdelatrisu.tripletriad.AudioController;
import itdelatrisu.tripletriad.Card;
import itdelatrisu.tripletriad.GameImage;
import itdelatrisu.tripletriad.I18n;
import itdelatrisu.tripletriad.Options;
import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.SavedDeck;
import itdelatrisu.tripletriad.TripleTriad;
import itdelatrisu.tripletriad.gfx.Color;
import itdelatrisu.tripletriad.gfx.GameContainer;
import itdelatrisu.tripletriad.gfx.Graphics;
import itdelatrisu.tripletriad.gfx.Image;
import itdelatrisu.tripletriad.gfx.Input;
import itdelatrisu.tripletriad.gfx.Log;
import itdelatrisu.tripletriad.gfx.SlickException;
import itdelatrisu.tripletriad.gfx.UnicodeFont;
import itdelatrisu.tripletriad.gfx.VideoLoop;
import itdelatrisu.tripletriad.story.StoryAssets;
import itdelatrisu.tripletriad.story.StoryCampaign;
import itdelatrisu.tripletriad.story.StoryMap;
import itdelatrisu.tripletriad.story.StorySave;

/**
 * Story campaign: several saves per profile, tournament stages, Nilo Vann
 * and one chosen card after every win.
 */
public class StoryScreen extends Screen {
	private enum Mode {
		HUB, PROTAGONIST, NAME, CAMPAIGNS, DELETE, STAGE, MAP, DUELS, TABLE, RULES, DOSSIER,
		SCENE, PICK, CONFIRM, SHOW
	}

	private static final int NAME_MAX = StorySave.NAME_MAX;

	private final TripleTriad game;
	private final VirtualKeyboard vk = new VirtualKeyboard();

	private Mode mode = Mode.HUB;
	private int index;
	private int yesNo;
	private final StringBuilder name = new StringBuilder();
	private String deleteId = "";

	/** Protagonist being chosen for a new campaign: 0 aluno, 1 aluna. */
	private int protagonist;

	/** True while the scene follows a lost match: the protagonist shows the defeat face. */
	private boolean sceneDefeat;

	private StoryCampaign.Region region;
	private StoryCampaign.Duel duel;
	private StoryCampaign.After after = StoryCampaign.After.HUB;
	private StoryCampaign.Line[] lines = new StoryCampaign.Line[0];
	private int lineIndex;
	private int page;
	private boolean showingVideo;
	private boolean pausedBgm;
	private VideoLoop video;

	private int cardIndex;
	private int[] shown = new int[0];
	private int[] liveOpp = new int[0];
	private final int[] lastHand = new int[SavedDeck.SIZE];
	private long savedUntil;
	private int rulesPage;

	public StoryScreen(TripleTriad game) {
		this.game = game;
	}

	@Override
	public void enter() {
		disposeVideo();
		vk.close();
		index = 0;
		mode = Mode.HUB;
	}

	/** The hand the opponent is using. Not written until the win. */
	public void noteOpponentHand(int[] ids) {
		liveOpp = (ids == null) ? new int[0] : ids.clone();
	}

	public void onMatchAbort() {
		resumeBgm();
		mode = Mode.DUELS;
	}

	/**
	 * @param result 1 win, -1 loss, 0 draw
	 * @param opponentHand the five cards that were played
	 */
	public void onMatchResult(int result, int[] opponentHand) {
		resumeBgm();
		StorySave save = camp();
		if (duel == null || region == null || save == null) {
			mode = Mode.HUB;
			return;
		}
		if (opponentHand != null && opponentHand.length > 0)
			liveOpp = opponentHand.clone();
		if (result > 0) {
			save.armWin(duel.id, liveOpp, duel.rewardId);
			game.saveProfile();
			markSaved();
			play(join(new StoryCampaign.Line[] { duel.win }, duel.afterWin), StoryCampaign.After.CARD);
			return;
		}
		StoryCampaign.Line beat = (result < 0) ? duel.lose : StoryCampaign.tie(duel);
		play(join(new StoryCampaign.Line[] { beat }, duel.afterLose), StoryCampaign.After.LIST);
		sceneDefeat = result < 0;
	}

	@Override
	public void update(GameContainer container, int delta) {
		if (mode == Mode.SCENE && showingVideo && video != null && video.isFinished())
			finishVideo();
	}

	@Override
	public void render(GameContainer container, Graphics g) {
		int width = container.getWidth();
		int height = container.getHeight();
		drawBackground(g, width, height, backgroundFile());
		switch (mode) {
			case HUB: renderHub(g, height); break;
			case PROTAGONIST: renderProtagonist(g, width, height); break;
			case NAME: renderName(g, height); break;
			case CAMPAIGNS: renderCampaigns(g, height); break;
			case DELETE: renderYesNo(g, height, I18n.storyDeleteAsk(deleteName())); break;
			case STAGE: renderStage(g, height); break;
			case MAP: renderMap(g, height); break;
			case DUELS: renderDuels(g, height); break;
			case TABLE: renderTable(g, height); break;
			case RULES: renderRules(g, height); break;
			case DOSSIER: renderDossier(g, width, height); break;
			case SCENE: renderScene(g, width, height); break;
			case PICK: renderPick(g, width, height, false); break;
			case CONFIRM: renderPick(g, width, height, true); break;
			case SHOW: renderShow(g, width, height); break;
			default: break;
		}
		if (System.currentTimeMillis() < savedUntil)
			Ui.drawCentered(Options.getSmallFont(), I18n.storySaved(),
				HintBar.lastTop() - Options.getSmallFont().getLineHeight() * 1.3f, Ui.TITLE);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch (mode) {
			case HUB: keyHub(key); break;
			case PROTAGONIST: keyProtagonist(key); break;
			case NAME: keyName(key, c); break;
			case CAMPAIGNS: keyCampaigns(key); break;
			case DELETE: keyDelete(key); break;
			case STAGE: keyStage(key); break;
			case MAP: keyMap(key); break;
			case DUELS: keyDuels(key); break;
			case TABLE: keyBack(key, Mode.STAGE); break;
			case RULES: keyRules(key); break;
			case DOSSIER: keyDossier(key); break;
			case SCENE: keyScene(key); break;
			case PICK: keyPick(key); break;
			case CONFIRM: keyConfirm(key); break;
			case SHOW: keyShow(key); break;
			default: break;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button != Input.MOUSE_LEFT_BUTTON)
			return;
		if (mode == Mode.HUB)
			clickHub(y);
		else if (mode == Mode.PROTAGONIST)
			clickProtagonist(x, y);
		else if (mode == Mode.CAMPAIGNS)
			clickList(y, game.getProfile() == null ? 0 : game.getProfile().campaignCount(), this::openListed);
		else if (mode == Mode.STAGE)
			clickList(y, stageItems().length, this::activateStage);
		else if (mode == Mode.MAP)
			clickMap(x, y);
		else if (mode == Mode.DUELS && region != null)
			clickList(y, region.duels.length, this::activateDuel);
		else if (mode == Mode.SCENE)
			keyScene(Input.KEY_Z);
		else if (mode == Mode.PICK)
			keyPick(Input.KEY_Z);
		else if (mode == Mode.SHOW || mode == Mode.RULES || mode == Mode.DOSSIER || mode == Mode.TABLE)
			keyPressed(Input.KEY_Z, (char) 0);
	}

	private void keyHub(int key) {
		int n = 3;
		if (key == Input.KEY_DOWN || key == Input.KEY_UP) {
			index = (key == Input.KEY_DOWN) ? (index + 1) % n : (index + n - 1) % n;
			AudioController.playCursor();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			if (index == 0)
				beginProtagonist();
			else if (index == 1)
				openCampaignList();
			else
				leaveToMenu();
		} else if (key == Input.KEY_ESCAPE) {
			leaveToMenu();
		}
	}

	private void clickHub(int y) {
		int hit = hitRow(y, 3);
		if (hit < 0)
			return;
		if (hit != index) {
			index = hit;
			AudioController.playCursor();
			return;
		}
		keyHub(Input.KEY_Z);
	}

	/** New campaign, step 1: aluno or aluna. The name comes next. */
	private void beginProtagonist() {
		Profile profile = game.getProfile();
		if (profile != null && profile.campaignCount() >= StorySave.MAX) {
			AudioController.Effect.INVALID.play();
			return;
		}
		protagonist = 0;
		name.setLength(0);
		mode = Mode.PROTAGONIST;
		AudioController.Effect.SELECT.play();
	}

	private void keyProtagonist(int key) {
		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT || key == Input.KEY_UP || key == Input.KEY_DOWN) {
			protagonist = 1 - protagonist;
			AudioController.playCursor();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			beginName();
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.HUB;
		}
	}

	private void clickProtagonist(int x, int y) {
		int hit = protagonistAt(x, y);
		if (hit < 0)
			return;
		if (hit != protagonist) {
			protagonist = hit;
			AudioController.playCursor();
			return;
		}
		keyProtagonist(Input.KEY_Z);
	}

	/** @return 0 aluno, 1 aluna, or -1 when the point is outside both frames */
	private int protagonistAt(float x, float y) {
		float size = protagonistSize();
		float top = Options.getHeight() * 0.28f;
		float labelH = Options.getSmallFont().getLineHeight() * 1.6f;
		if (y < top - 8f || y > top + size + labelH)
			return -1;
		for (int i = 0; i < 2; i++) {
			float left = protagonistCenter(i) - size / 2f;
			if (x >= left - 8f && x <= left + size + 8f)
				return i;
		}
		return -1;
	}

	private static float protagonistSize() {
		return Math.min(Options.getHeight() * 0.34f, Options.getWidth() * 0.26f);
	}

	private static float protagonistCenter(int i) {
		return Options.getWidth() * (i == 0 ? 0.34f : 0.66f);
	}

	private String chosenGender() {
		return protagonist == 1 ? StorySave.FEMALE : StorySave.MALE;
	}

	private void beginName() {
		vk.autoOpen();
		mode = Mode.NAME;
		AudioController.Effect.SELECT.play();
	}

	private void keyName(int key, char c) {
		int vkResult = vk.handle(key, c);
		if (vkResult == VirtualKeyboard.CHAR) {
			key = 0;
			c = vk.typed();
		} else if (vkResult == VirtualKeyboard.DELETE) {
			key = Input.KEY_BACK;
		} else if (vkResult == VirtualKeyboard.OK) {
			key = Input.KEY_ENTER;
		} else if (vkResult == VirtualKeyboard.CANCEL) {
			key = Input.KEY_ESCAPE;
		} else if (vkResult != VirtualKeyboard.PASS) {
			return;
		}
		if ((Ui.isNameChar(c) && c != ' ') || (c == ' ' && key == 0)) {
			if (name.length() < NAME_MAX)
				name.append(c);
			return;
		}
		if (key == Input.KEY_ESCAPE) {
			vk.close();
			AudioController.Effect.BACK.play();
			mode = Mode.PROTAGONIST;
			return;
		}
		if (key == Input.KEY_ENTER || key == Input.KEY_Z) {
			commitName();
			return;
		}
		if (key == Input.KEY_BACK && name.length() > 0) {
			name.deleteCharAt(name.length() - 1);
			AudioController.Effect.BACK.play();
		}
	}

	private void commitName() {
		Profile profile = game.getProfile();
		if (profile == null)
			return;
		StorySave save = profile.addCampaign(name.toString(), chosenGender());
		if (save == null) {
			AudioController.Effect.INVALID.play();
			return;
		}
		vk.close();
		AudioController.Effect.SELECT.play();
		if (game.topUpStoryAlbum())
			save.setStarterScene(true);
		game.saveProfile();
		markSaved();
		openSave(save);
	}

	private void openCampaignList() {
		Profile profile = game.getProfile();
		if (profile == null || profile.campaignCount() == 0) {
			AudioController.Effect.INVALID.play();
			return;
		}
		index = 0;
		mode = Mode.CAMPAIGNS;
		AudioController.Effect.SELECT.play();
	}

	private void keyCampaigns(int key) {
		Profile profile = game.getProfile();
		int n = profile == null ? 0 : profile.campaignCount();
		if (n == 0) {
			mode = Mode.HUB;
			return;
		}
		if (key == Input.KEY_DOWN || key == Input.KEY_UP) {
			index = (key == Input.KEY_DOWN) ? (index + 1) % n : (index + n - 1) % n;
			AudioController.playCursor();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			openListed(index);
		} else if (key == Input.KEY_DELETE) {
			StorySave save = profile.campaignAt(index);
			if (save == null)
				return;
			deleteId = save.id();
			yesNo = 0;
			mode = Mode.DELETE;
			AudioController.Effect.BACK.play();
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.HUB;
		}
	}

	private void openListed(int row) {
		Profile profile = game.getProfile();
		StorySave save = profile == null ? null : profile.campaignAt(row);
		if (save == null) {
			AudioController.Effect.INVALID.play();
			return;
		}
		profile.setActiveStory(save.id());
		game.saveProfile();
		AudioController.Effect.SELECT.play();
		openSave(save);
	}

	private void keyDelete(int key) {
		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
			yesNo = 1 - yesNo;
			AudioController.playCursor();
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.CAMPAIGNS;
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			Profile profile = game.getProfile();
			boolean removed = profile != null && profile.deleteCampaign(deleteId, yesNo == 1);
			if (!removed) {
				AudioController.Effect.BACK.play();
				mode = Mode.CAMPAIGNS;
				return;
			}
			game.saveProfile();
			markSaved();
			AudioController.Effect.SELECT.play();
			mode = profile.campaignCount() == 0 ? Mode.HUB : Mode.CAMPAIGNS;
			index = 0;
		}
	}

	private void openSave(StorySave save) {
		region = StoryCampaign.REGIONS[StoryCampaign.currentRegion(save)];
		if (save.hasPending()) {
			resumePending(save);
			return;
		}
		if (save.isCleared(StoryCampaign.FINAL_DUEL) && !save.isComplete()) {
			duel = StoryCampaign.findDuel(StoryCampaign.FINAL_DUEL);
			region = StoryCampaign.regionOf(StoryCampaign.FINAL_DUEL);
			play(StoryCampaign.ceremony(), StoryCampaign.After.CEREMONY);
			return;
		}
		if (save.wantsStarter()) {
			play(StoryCampaign.STARTER, StoryCampaign.After.STAGE);
			return;
		}
		index = 0;
		mode = Mode.STAGE;
	}

	private void resumePending(StorySave save) {
		duel = StoryCampaign.findDuel(save.pendingDuel());
		region = StoryCampaign.regionOf(save.pendingDuel());
		liveOpp = save.pendingHand();
		if (duel == null) {
			mode = Mode.STAGE;
			return;
		}
		if (StorySave.TALK.equals(save.pendingPhase()))
			play(join(new StoryCampaign.Line[] { duel.win }, duel.afterWin), StoryCampaign.After.CARD);
		else {
			cardIndex = 0;
			mode = Mode.PICK;
		}
	}

	private void keyStage(int key) {
		String[] items = stageItems();
		if (key == Input.KEY_DOWN || key == Input.KEY_UP) {
			index = (key == Input.KEY_DOWN) ? (index + 1) % items.length : (index + items.length - 1) % items.length;
			AudioController.playCursor();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			activateStage(index);
		} else if (key == Input.KEY_S) {
			manualSave();
		} else if (key == Input.KEY_ESCAPE) {
			leaveCampaign();
		}
	}

	private void activateStage(int row) {
		StorySave save = camp();
		if (save == null) {
			mode = Mode.HUB;
			return;
		}
		region = StoryCampaign.REGIONS[StoryCampaign.currentRegion(save)];
		AudioController.Effect.SELECT.play();
		if (row == 0) {
			openDuels();
		} else if (row == 1) {
			index = StoryCampaign.currentRegion(save);
			mode = Mode.MAP;
		} else if (row == 2) {
			if (region.kind == StoryCampaign.Kind.GROUP) {
				mode = Mode.TABLE;
			} else {
				openDuels();
			}
		} else if (row == 3) {
			rulesPage = 0;
			mode = Mode.RULES;
		} else if (row == 4) {
			play(region.arrival, StoryCampaign.After.STAGE);
		} else if (row == 5) {
			manualSave();
		} else if (row == 6) {
			leaveCampaign();
		} else {
			manualSave();
			leaveToMenu();
		}
	}

	private String[] stageItems() {
		StorySave save = camp();
		StoryCampaign.Region here = StoryCampaign.REGIONS[StoryCampaign.currentRegion(save)];
		String board = here.kind == StoryCampaign.Kind.GROUP ? I18n.storyTable() : I18n.storyLadder();
		return new String[] {
			I18n.storyDuels(),
			I18n.text("Cidades", "Cities", "Ciudades"),
			board,
			I18n.storyRulesTitle(),
			I18n.storyArrival(StoryAssets.hasVideo(here.video)),
			I18n.storySave(),
			I18n.storyLeave(),
			I18n.storyBackMenu()
		};
	}

	private void keyMap(int key) {
		int n = StoryCampaign.REGIONS.length;
		if (key == Input.KEY_DOWN || key == Input.KEY_UP || key == Input.KEY_RIGHT || key == Input.KEY_LEFT) {
			boolean next = key == Input.KEY_DOWN || key == Input.KEY_RIGHT;
			index = next ? (index + 1) % n : (index + n - 1) % n;
			AudioController.playCursor();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			openRegion(index);
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.STAGE;
		}
	}

	/** Click a marker: the first click selects it, the second enters. */
	private void clickMap(int x, int y) {
		float radius = mapMarkerSize() * 0.9f;
		int hit = StoryMap.hit(x, y, Options.getWidth(), Options.getHeight(), radius);
		if (hit < 0)
			return;
		if (hit != index) {
			index = hit;
			AudioController.playCursor();
			return;
		}
		openRegion(hit);
	}

	private void openRegion(int row) {
		if (!StoryCampaign.regionUnlocked(camp(), row)) {
			AudioController.Effect.INVALID.play();
			return;
		}
		region = StoryCampaign.REGIONS[row];
		AudioController.Effect.SELECT.play();
		if (region.duels.length == 0) {
			play(region.arrival, StoryCampaign.After.STAGE);
			return;
		}
		index = 0;
		mode = Mode.DUELS;
	}

	private void openDuels() {
		StorySave save = camp();
		if (save != null && save.hasPending()) {
			resumePending(save);
			return;
		}
		if (region == null)
			region = StoryCampaign.REGIONS[StoryCampaign.currentRegion(save)];
		index = 0;
		mode = Mode.DUELS;
	}

	private void keyDuels(int key) {
		if (region == null) {
			mode = Mode.STAGE;
			return;
		}
		StorySave save = camp();
		if (save != null && save.hasPending()) {
			resumePending(save);
			return;
		}
		int n = region.duels.length;
		if (key == Input.KEY_DOWN || key == Input.KEY_UP) {
			index = (key == Input.KEY_DOWN) ? (index + 1) % n : (index + n - 1) % n;
			AudioController.playCursor();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			activateDuel(index);
		} else if (key == Input.KEY_T && index >= 0 && index < n) {
			duel = region.duels[index];
			lineIndex = 0;
			page = 0;
			mode = Mode.DOSSIER;
			AudioController.Effect.SELECT.play();
		} else if (key == Input.KEY_S) {
			manualSave();
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.STAGE;
		}
	}

	private void activateDuel(int row) {
		StorySave save = camp();
		if (save != null && save.hasPending()) {
			resumePending(save);
			return;
		}
		if (!StoryCampaign.duelUnlocked(save, region, row)) {
			AudioController.Effect.INVALID.play();
			return;
		}
		duel = region.duels[row];
		AudioController.Effect.SELECT.play();
		play(join(duel.open, new StoryCampaign.Line[] { duel.before }), StoryCampaign.After.PICK);
	}

	private void keyRules(int key) {
		String[] pages = I18n.storyRules();
		if (key == Input.KEY_DOWN || key == Input.KEY_RIGHT) {
			rulesPage = Math.min(pages.length - 1, rulesPage + 1);
			AudioController.playCursor();
		} else if (key == Input.KEY_UP || key == Input.KEY_LEFT) {
			rulesPage = Math.max(0, rulesPage - 1);
			AudioController.playCursor();
		} else if (key == Input.KEY_ESCAPE || key == Input.KEY_Z || key == Input.KEY_ENTER) {
			AudioController.Effect.BACK.play();
			mode = Mode.STAGE;
		}
	}

	private void keyDossier(int key) {
		if (duel == null) {
			mode = Mode.DUELS;
			return;
		}
		if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			lineIndex++;
			if (lineIndex >= duel.bio.length) {
				mode = Mode.DUELS;
			} else {
				AudioController.playCursor();
			}
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.DUELS;
		}
	}

	private void keyBack(int key, Mode back) {
		if (key == Input.KEY_S && back == Mode.STAGE)
			manualSave();
		if (key == Input.KEY_ESCAPE || key == Input.KEY_Z || key == Input.KEY_ENTER) {
			AudioController.Effect.BACK.play();
			mode = back;
		}
	}

	private void keyScene(int key) {
		if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			finishScene();
			return;
		}
		if (showingVideo && (key == Input.KEY_S || key == Input.KEY_Z || key == Input.KEY_ENTER)) {
			finishVideo();
			return;
		}
		if (!showingVideo && (key == Input.KEY_Z || key == Input.KEY_ENTER)) {
			StorySave save = camp();
			if (lineIndex >= 0 && lineIndex < lines.length) {
				boolean still = sceneStill() != null;
				int per = still ? STILL_LINES : 3;
				ArrayList<String> wrapped = wrap(Options.getSmallFont(), lines[lineIndex].text(save),
					still ? stillTextWidth() : sceneTextWidth(lines[lineIndex]));
				int pages = Math.max(1, (wrapped.size() + per - 1) / per);
				if (page + 1 < pages) {
					page++;
					AudioController.playCursor();
					return;
				}
			}
			page = 0;
			lineIndex++;
			if (lineIndex >= lines.length)
				finishScene();
			else
				AudioController.playCursor();
		}
	}

	private void keyPick(int key) {
		StorySave save = camp();
		int n = save == null ? 0 : save.pendingHand().length;
		if (n == 0) {
			mode = Mode.DUELS;
			return;
		}
		if (key == Input.KEY_LEFT || key == Input.KEY_UP) {
			cardIndex = (cardIndex + n - 1) % n;
			AudioController.playCursor();
		} else if (key == Input.KEY_RIGHT || key == Input.KEY_DOWN) {
			cardIndex = (cardIndex + 1) % n;
			AudioController.playCursor();
		} else if (key == Input.KEY_C) {
			AudioController.Effect.SELECT.play();
			preview = !preview;
			return;
		}
		if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			if (!save.eligible(save.pendingHand()[cardIndex])) {
				AudioController.Effect.INVALID.play();
				return;
			}
			yesNo = 0;
			preview = false;
			mode = Mode.CONFIRM;
			AudioController.Effect.SELECT.play();
		} else if (key == Input.KEY_ESCAPE) {
			preview = false;
			AudioController.Effect.BACK.play();
			mode = Mode.DUELS;
		}
	}

	private boolean preview;

	private void keyConfirm(int key) {
		if (key == Input.KEY_LEFT || key == Input.KEY_RIGHT) {
			yesNo = 1 - yesNo;
			AudioController.playCursor();
		} else if (key == Input.KEY_ESCAPE) {
			AudioController.Effect.BACK.play();
			mode = Mode.PICK;
		} else if ((key == Input.KEY_Z || key == Input.KEY_ENTER) && yesNo == 1) {
			grantChosen();
		} else if (key == Input.KEY_Z || key == Input.KEY_ENTER) {
			AudioController.Effect.BACK.play();
			mode = Mode.PICK;
		}
	}

	private void grantChosen() {
		StorySave save = camp();
		Profile profile = game.getProfile();
		if (save == null || profile == null)
			return;
		int[] hand = save.pendingHand();
		if (cardIndex < 0 || cardIndex >= hand.length)
			return;
		int[] granted = save.confirm(hand[cardIndex]);
		if (granted == null) {
			AudioController.Effect.INVALID.play();
			mode = Mode.PICK;
			return;
		}
		profile.grantStoryCards(granted);
		game.saveProfile();
		markSaved();
		shown = granted;
		AudioController.Effect.SELECT.play();
		mode = Mode.SHOW;
	}

	private void keyShow(int key) {
		if (key == Input.KEY_Z || key == Input.KEY_ENTER || key == Input.KEY_ESCAPE) {
			AudioController.Effect.SELECT.play();
			StorySave save = camp();
			if (duel != null && StoryCampaign.FINAL_DUEL.equals(duel.id) && save != null && !save.isComplete()) {
				play(StoryCampaign.ceremony(), StoryCampaign.After.CEREMONY);
				return;
			}
			mode = Mode.DUELS;
		}
	}

	private void finishScene() {
		disposeVideo();
		resumeBgm();
		showingVideo = false;
		StorySave save = camp();
		switch (after) {
			case PICK:
				game.showStoryCardPick();
				break;
			case CARD:
				if (save != null)
					save.advanceToPick();
				game.saveProfile();
				markSaved();
				cardIndex = 0;
				mode = Mode.PICK;
				break;
			case CEREMONY:
				if (save != null)
					save.setComplete(true);
				game.saveProfile();
				markSaved();
				mode = Mode.STAGE;
				break;
			case STAGE:
				if (save != null && region != null && lines == region.arrival)
					save.markArrival(region.id);
				if (save != null && save.wantsStarter())
					save.clearStarterScene();
				game.saveProfile();
				markSaved();
				mode = Mode.STAGE;
				break;
			case LIST:
				mode = Mode.DUELS;
				break;
			case HUB:
			default:
				mode = Mode.HUB;
				break;
		}
	}

	/** @return the opponent of the duel about to start */
	public String opponentName() {
		return (duel != null) ? duel.name() : "";
	}

	public int[] rememberedHand() {
		for (int i = 0; i < lastHand.length; i++) {
			if (lastHand[i] <= 0)
				return null;
		}
		return lastHand.clone();
	}

	public void beginWithHand(int[] ids) {
		StorySave save = camp();
		if (duel == null || ids == null || ids.length != SavedDeck.SIZE)
			return;
		if (save != null && save.hasPending()) {
			resumePending(save);
			return;
		}
		System.arraycopy(ids, 0, lastHand, 0, ids.length);
		if (save != null)
			save.setLastHand(ids);
		game.startStoryMatch(ids, duel.minLevel, duel.maxLevel, duel.ai, duel.rules);
	}

	public void backToList() {
		mode = (region != null) ? Mode.DUELS : Mode.STAGE;
	}

	private void play(StoryCampaign.Line[] script, StoryCampaign.After next) {
		String videoId = null;
		if (next == StoryCampaign.After.STAGE && region != null && script == region.arrival)
			videoId = region.video;
		playScene(videoId, script, next);
	}

	private void playScene(String videoId, StoryCampaign.Line[] script, StoryCampaign.After next) {
		disposeVideo();
		lines = (script != null) ? script : new StoryCampaign.Line[0];
		lineIndex = 0;
		page = 0;
		sceneDefeat = false;
		after = next;
		showingVideo = false;
		mode = Mode.SCENE;
		if (!StoryAssets.hasVideo(videoId)) {
			if (lines.length == 0)
				finishScene();
			return;
		}
		try {
			video = new VideoLoop("story/" + videoId, StoryAssets.VIDEO_FPS);
			video.setLooping(false);
			boolean audio = StoryAssets.hasVideoAudio(videoId) && Options.isMusicEnabled();
			if (audio) {
				AudioController.pauseMusic();
				pausedBgm = true;
			}
			video.play(audio);
			showingVideo = true;
		} catch (SlickException e) {
			Log.error("Story video unavailable: " + videoId, e);
			disposeVideo();
			showingVideo = false;
			if (lines.length == 0)
				finishScene();
		}
	}

	private void finishVideo() {
		disposeVideo();
		showingVideo = false;
		resumeBgm();
		if (lines.length == 0)
			finishScene();
	}

	private void manualSave() {
		StorySave save = camp();
		if (save != null)
			save.touch();
		game.saveProfile();
		markSaved();
		AudioController.Effect.SELECT.play();
	}

	private void markSaved() { savedUntil = System.currentTimeMillis() + 2500L; }

	private void leaveCampaign() {
		manualSave();
		index = 0;
		mode = Mode.HUB;
	}

	private void leaveToMenu() {
		AudioController.Effect.BACK.play();
		game.showMenu();
	}

	private StorySave camp() {
		Profile profile = game.getProfile();
		return profile == null ? null : profile.activeStory();
	}

	private String deleteName() {
		Profile profile = game.getProfile();
		StorySave save = profile == null ? null : profile.findCampaign(deleteId);
		return save == null ? "" : save.student();
	}

	private boolean titleMode() {
		return mode == Mode.HUB || mode == Mode.PROTAGONIST || mode == Mode.NAME
			|| mode == Mode.CAMPAIGNS || mode == Mode.DELETE;
	}

	/** @return the line on screen when it plays over a full-screen still, else null */
	private StoryCampaign.Line sceneStill() {
		if (mode != Mode.SCENE || showingVideo || lineIndex < 0 || lineIndex >= lines.length)
			return null;
		StoryCampaign.Line line = lines[lineIndex];
		if (line == null || !line.hasScene() || StoryAssets.background(line.scene) == null)
			return null;
		return line;
	}

	private String backgroundFile() {
		if (titleMode())
			return StoryAssets.TITLE_BACKGROUND;
		if (mode == Mode.MAP)
			return StoryMap.BACKGROUND;
		StoryCampaign.Line still = sceneStill();
		if (still != null)
			return still.scene;
		if (region != null)
			return region.background;
		StorySave save = camp();
		int i = StoryCampaign.currentRegion(save);
		return StoryCampaign.REGIONS[i].background;
	}

	private void renderHub(Graphics g, int height) {
		Profile profile = game.getProfile();
		int count = profile == null ? 0 : profile.campaignCount();
		drawLogo(height);
		UnicodeFont font = Options.getSmallFont();
		String[] items = { I18n.storyNew(), I18n.storyContinue(), I18n.storyBackMenu() };
		float start = height * 0.36f;
		float step = rowStep(items.length);
		for (int i = 0; i < items.length; i++) {
			boolean off = (i == 0 && count >= StorySave.MAX) || (i == 1 && count == 0);
			drawChoice(font, i, index, items[i], start, step, null, off ? Ui.DISABLED : Ui.HINT);
		}
		if (count >= StorySave.MAX)
			Ui.drawCentered(font, I18n.storyFull(), height * 0.78f, Ui.TITLE);
		HintBar.draw(I18n.hintStoryHub(), height * 0.92f);
	}

	/**
	 * Story Mode logo in the empty sky of titulo_modo_historia.png (upper-left).
	 * Drawn as text so it follows the language; the font has no en dash, so the
	 * title is split in two lines instead.
	 */
	private void drawLogo(int height) {
		float x = Options.getWidth() * 0.05f;
		UnicodeFont big = Options.getFont();
		UnicodeFont small = Options.getSmallFont();
		float y = height * 0.05f;
		shadowed(big, x, y, I18n.storyLogoTitle(), Ui.TITLE);
		y += big.getLineHeight() * 1.05f;
		shadowed(big, x, y, I18n.storyLogoSubtitle(), Color.white);
		y += big.getLineHeight() * 1.15f;
		shadowed(small, x, y, Ui.fit(small, I18n.storyFan(), Options.getWidth() * 0.5f), Ui.HINT);
		y += small.getLineHeight() * 1.1f;
		Profile profile = game.getProfile();
		int count = profile == null ? 0 : profile.campaignCount();
		shadowed(small, x, y, I18n.storyTitle() + "  " + count + "/" + StorySave.MAX, Ui.HINT);
	}

	private static void shadowed(UnicodeFont font, float x, float y, String text, Color color) {
		font.drawString(x + 2f, y + 2f, text, new Color(0f, 0f, 0f, 0.85f));
		font.drawString(x, y, text, color);
	}

	private void renderProtagonist(Graphics g, int width, int height) {
		Ui.drawCentered(Options.getFont(), I18n.storyProtagonistTitle(), height * 0.1f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), I18n.storyProtagonistNote(), height * 0.18f, Ui.HINT);
		float size = protagonistSize();
		float top = height * 0.28f;
		UnicodeFont font = Options.getSmallFont();
		String[] labels = { I18n.storyMale(), I18n.storyFemale() };
		String[] genders = { StorySave.MALE, StorySave.FEMALE };
		for (int i = 0; i < 2; i++) {
			boolean on = i == protagonist;
			float left = protagonistCenter(i) - size / 2f;
			g.setColor(on ? Ui.TITLE : new Color(0.3f, 0.3f, 0.34f));
			g.fillRect(left - 4f, top - 4f, size + 8f, size + 8f);
			g.setColor(new Color(0.05f, 0.07f, 0.12f));
			g.fillRect(left - 1f, top - 1f, size + 2f, size + 2f);
			Image face = StoryAssets.portrait(StorySave.portraitFor(genders[i]), false);
			if (face != null)
				face.draw(left, top, size, size);
			if (!on) {
				g.setColor(new Color(0f, 0f, 0f, 0.5f));
				g.fillRect(left, top, size, size);
			}
			float labelY = top + size + font.getLineHeight() * 0.5f;
			float labelX = protagonistCenter(i) - font.getWidth(labels[i]) / 2f;
			font.drawString(labelX, labelY, labels[i], on ? Ui.TITLE : Ui.HINT);
			if (on) {
				Image cursor = GameImage.CURSOR.getImage();
				cursor.draw(labelX - cursor.getWidth() - 10f, labelY + (font.getLineHeight() - cursor.getHeight()) / 2f);
			}
		}
		HintBar.draw(I18n.hintStoryProtagonist(), height * 0.92f);
	}

	private void renderName(Graphics g, int height) {
		Image face = StoryAssets.portrait(StorySave.portraitFor(chosenGender()), false);
		if (face != null) {
			float size = height * 0.18f;
			face.draw(Options.getWidth() * 0.05f, height * 0.06f, size, size);
		}
		Ui.drawCentered(Options.getFont(), I18n.storyNamePrompt(protagonist == 1), height * 0.16f, Ui.TITLE);
		String shown = name.length() == 0 ? "_" : name.toString() + "_";
		Ui.drawCentered(Options.getFont(), shown, height * 0.28f, Color.white);
		Ui.drawCentered(Options.getSmallFont(), I18n.storyFan(), height * 0.38f, Ui.HINT);
		vk.render(g, height * 0.46f, height * 0.9f);
		if (!vk.isActive())
			HintBar.draw(I18n.hintStoryHub(), height * 0.94f);
	}

	private void renderCampaigns(Graphics g, int height) {
		Profile profile = game.getProfile();
		Ui.drawCentered(Options.getFont(), I18n.storyContinue(), height * 0.08f, Ui.TITLE);
		if (profile == null)
			return;
		UnicodeFont font = Options.getSmallFont();
		int n = profile.campaignCount();
		float start = height * 0.18f;
		float step = rowStep(Math.max(1, n));
		for (int i = 0; i < n; i++) {
			StorySave save = profile.campaignAt(i);
			StoryCampaign.Region here = StoryCampaign.REGIONS[StoryCampaign.currentRegion(save)];
			String label = save.student() + "  " + here.name() + "  "
				+ I18n.storyProgress(StoryCampaign.clearedCount(save), StoryCampaign.duelCount())
				+ "  " + save.when();
			Image face = StoryAssets.portrait(save.portraitId(), false);
			drawChoice(font, i, index, Ui.fit(font, label, Options.getWidth() * 0.72f), start, step, face, Ui.HINT);
		}
		HintBar.draw(I18n.hintStoryCampaigns(), height * 0.92f);
	}

	private void renderYesNo(Graphics g, int height, String question) {
		Ui.drawCentered(Options.getFont(), question, height * 0.36f, Ui.TITLE);
		UnicodeFont font = Options.getSmallFont();
		String no = (yesNo == 0 ? "> " : "") + I18n.storyNo();
		String yes = (yesNo == 1 ? "> " : "") + I18n.confirmLeaveYes();
		font.drawString(Options.getWidth() * 0.32f, height * 0.5f, no, yesNo == 0 ? Ui.TITLE : Color.white);
		font.drawString(Options.getWidth() * 0.55f, height * 0.5f, yes, yesNo == 1 ? Ui.TITLE : Color.white);
		HintBar.draw(I18n.hintStoryYesNo(), height * 0.9f);
	}

	private void renderStage(Graphics g, int height) {
		StorySave save = camp();
		if (save == null) {
			mode = Mode.HUB;
			return;
		}
		StoryCampaign.Region here = StoryCampaign.REGIONS[StoryCampaign.currentRegion(save)];
		Image face = StoryAssets.portrait(save.portraitId(), false);
		if (face != null) {
			float size = height * 0.16f;
			float fx = Options.getWidth() * 0.05f;
			float fy = height * 0.04f;
			g.setColor(Ui.TITLE);
			g.fillRect(fx - 2f, fy - 2f, size + 4f, size + 4f);
			face.draw(fx, fy, size, size);
		}
		Ui.drawCentered(Options.getFont(), save.student(), height * 0.07f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), here.name(), height * 0.14f, Ui.HINT);
		StoryCampaign.Duel next = StoryCampaign.nextDuel(save);
		String goal = save.isComplete() ? I18n.storyChampion(save.isFemale())
			: (next == null ? I18n.storyChampion(save.isFemale()) : I18n.storyObjective(next.role(), next.name()));
		Ui.drawCentered(Options.getSmallFont(), Ui.fit(Options.getSmallFont(), goal, Options.getWidth() * 0.86f),
			height * 0.2f, Color.white);
		Ui.drawCentered(Options.getSmallFont(),
			I18n.storyProgress(StoryCampaign.clearedCount(save), StoryCampaign.duelCount()),
			height * 0.25f, Ui.HINT);
		if (save.hasPending())
			Ui.drawCentered(Options.getSmallFont(), I18n.storyPending(), height * 0.3f, Ui.TITLE);
		String[] items = stageItems();
		float start = height * 0.34f;
		float step = rowStep(items.length);
		for (int i = 0; i < items.length; i++)
			drawChoice(Options.getSmallFont(), i, index, items[i], start, step, null, Ui.HINT);
		HintBar.draw(I18n.hintStoryStage(), height * 0.93f);
	}

	/** Marker size on the world map, in screen pixels. */
	private static float mapMarkerSize() {
		return Math.max(16f, Math.min(Options.getHeight() * 0.055f, 40f));
	}

	/**
	 * World map (mapa_mundi.png) with the tournament stops drawn by the game:
	 * dotted route, state icon (no_bloqueado / no_disponivel / no_vencido), name.
	 */
	private void renderMap(Graphics g, int height) {
		int width = Options.getWidth();
		StorySave save = camp();
		int n = StoryCampaign.REGIONS.length;
		float[][] at = new float[n][];
		for (int i = 0; i < n; i++) {
			StoryMap.Spot spot = StoryMap.spot(StoryCampaign.REGIONS[i].id);
			at[i] = spot == null ? null : StoryMap.toScreen(spot, 0, 0, width, height);
		}
		float marker = mapMarkerSize();
		// route in tournament order
		for (int i = 0; i + 1 < n; i++) {
			if (at[i] == null || at[i + 1] == null)
				continue;
			boolean open = StoryCampaign.regionUnlocked(save, i + 1);
			g.setColor(open ? new Color(1f, 0.92f, 0.55f, 0.85f) : new Color(0.75f, 0.75f, 0.8f, 0.45f));
			float dx = at[i + 1][0] - at[i][0], dy = at[i + 1][1] - at[i][1];
			float len = (float) Math.sqrt(dx * dx + dy * dy);
			int dots = (int) (len / Math.max(6f, marker * 0.3f));
			float dot = Math.max(2f, marker * 0.08f);
			for (int d = 1; d < dots; d++) {
				float t = d / (float) dots;
				g.fillRect(at[i][0] + dx * t - dot / 2f, at[i][1] + dy * t - dot / 2f, dot, dot);
			}
		}
		UnicodeFont tiny = Options.getTinyFont();
		UnicodeFont small = Options.getSmallFont();
		for (int i = 0; i < n; i++) {
			if (at[i] == null)
				continue;
			StoryCampaign.Region item = StoryCampaign.REGIONS[i];
			boolean unlocked = StoryCampaign.regionUnlocked(save, i);
			boolean cleared = StoryCampaign.stageCleared(item, save);
			boolean on = i == index;
			float size = on ? marker * 1.25f : marker;
			float x = at[i][0], y = at[i][1];
			if (on) {
				g.setColor(Ui.TITLE);
				g.fillRect(x - size / 2f - 3f, y - size / 2f - 3f, size + 6f, size + 6f);
				g.setColor(new Color(0.05f, 0.07f, 0.12f));
				g.fillRect(x - size / 2f - 1f, y - size / 2f - 1f, size + 2f, size + 2f);
			}
			Image icon = iconFor(unlocked, cleared);
			if (icon != null)
				icon.draw(x - size / 2f, y - size / 2f, size, size);
			else {
				g.setColor(!unlocked ? Ui.DISABLED : (cleared ? Ui.TITLE : Color.white));
				g.fillRect(x - size / 4f, y - size / 4f, size / 2f, size / 2f);
			}
			UnicodeFont font = on ? small : tiny;
			String label = item.name();
			float lw = font.getWidth(label);
			float lh = font.getLineHeight();
			StoryMap.Spot spot = StoryMap.spot(item.id);
			float lx, ly;
			switch (spot.label) {
				case ABOVE: lx = x - lw / 2f; ly = y - size / 2f - lh - 2f; break;
				case LEFT: lx = x - size / 2f - lw - 6f; ly = y - lh / 2f; break;
				case RIGHT: lx = x + size / 2f + 6f; ly = y - lh / 2f; break;
				case BELOW:
				default: lx = x - lw / 2f; ly = y + size / 2f + 2f; break;
			}
			lx = Math.max(4f, Math.min(width - lw - 4f, lx));
			ly = Math.max(4f, Math.min(height - lh - 4f, ly));
			Color color = !unlocked ? Ui.DISABLED : (cleared ? Ui.TITLE : Color.white);
			shadowed(font, lx, ly, label, on ? Ui.TITLE : color);
		}
		Ui.drawCentered(Options.getFont(), I18n.text("Cidades", "Cities", "Ciudades"), height * 0.03f, Ui.TITLE);
		if (index >= 0 && index < n) {
			StoryCampaign.Region item = StoryCampaign.REGIONS[index];
			boolean unlocked = StoryCampaign.regionUnlocked(save, index);
			boolean cleared = StoryCampaign.stageCleared(item, save);
			String info = item.name() + "  -  " + I18n.storyMapState(unlocked, cleared);
			float lh = small.getLineHeight();
			float iw = small.getWidth(info) + width * 0.04f;
			float iy = height * 0.84f;
			g.setColor(new Color(0.03f, 0.05f, 0.1f, 0.8f));
			g.fillRect((width - iw) / 2f, iy - lh * 0.25f, iw, lh * 1.5f);
			Ui.drawCentered(small, info, iy, unlocked ? Ui.TITLE : Ui.DISABLED);
		}
		HintBar.draw(I18n.hintStoryMap(), height * 0.93f);
	}

	private void renderDuels(Graphics g, int height) {
		if (region == null)
			return;
		Ui.drawCentered(Options.getFont(), region.name(), height * 0.08f, Ui.TITLE);
		UnicodeFont font = Options.getSmallFont();
		float start = height * 0.16f;
		float step = rowStep(Math.max(1, region.duels.length));
		StorySave save = camp();
		for (int i = 0; i < region.duels.length; i++) {
			StoryCampaign.Duel item = region.duels[i];
			boolean unlocked = StoryCampaign.duelUnlocked(save, region, i);
			boolean cleared = save != null && save.isCleared(item.id);
			String label = item.role() + "  " + item.name();
			if (item.rewardId > 0 && (save == null || !save.specialGranted(item.id)))
				label = label + "  *";
			drawChoice(font, i, index, Ui.fit(font, label, Options.getWidth() * 0.7f), start, step,
				iconFor(unlocked, cleared), !unlocked ? Ui.DISABLED : (cleared ? Ui.TITLE : Ui.HINT));
		}
		HintBar.draw(I18n.hintStoryDuels(), height * 0.93f);
	}

	private void renderTable(Graphics g, int height) {
		if (region == null)
			return;
		Ui.drawCentered(Options.getFont(), region.name(), height * 0.08f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), I18n.storyCutoff(region.cutoff), height * 0.15f, Ui.HINT);
		StoryCampaign.Row[] rows = StoryCampaign.table(region, camp());
		UnicodeFont font = Options.getSmallFont();
		float y = height * 0.24f;
		for (int i = 0; i < rows.length; i++) {
			String line = rows[i].rank + ".  " + rows[i].name + "   " + I18n.storyPoints(rows[i].points);
			font.drawString(Options.getWidth() * 0.22f, y, line, rows[i].player ? Ui.TITLE : Color.white);
			y += font.getLineHeight() * 1.35f;
		}
		boolean seed = false;
		for (int i = 0; i < region.duels.length; i++) {
			if (!region.duels[i].inTable)
				seed = true;
		}
		if (seed)
			Ui.drawCentered(font, I18n.storySeedNote(), height * 0.78f, Ui.HINT);
		HintBar.draw(I18n.hintStoryList(), height * 0.93f);
	}

	private void renderRules(Graphics g, int height) {
		String[] pages = I18n.storyRules();
		Ui.drawCentered(Options.getFont(), I18n.storyRulesTitle(), height * 0.12f, Ui.TITLE);
		UnicodeFont font = Options.getSmallFont();
		String body = rulesPage >= 0 && rulesPage < pages.length ? pages[rulesPage] : "";
		ArrayList<String> wrapped = wrap(font, body, Options.getWidth() * 0.8f);
		float y = height * 0.28f;
		for (int i = 0; i < wrapped.size() && i < 8; i++) {
			font.drawString(Options.getWidth() * 0.1f, y, wrapped.get(i), Color.white);
			y += font.getLineHeight();
		}
		Ui.drawCentered(font, (rulesPage + 1) + "/" + pages.length, height * 0.78f, Ui.HINT);
		HintBar.draw(I18n.hintStoryTalk(), height * 0.92f);
	}

	private void renderDossier(Graphics g, int width, int height) {
		if (duel == null)
			return;
		Ui.drawCentered(Options.getFont(), duel.name(), height * 0.08f, Ui.TITLE);
		Ui.drawCentered(Options.getSmallFont(), duel.role(), height * 0.15f, Ui.HINT);
		StorySave beatenBy = camp();
		Image face = StoryAssets.portrait(duel.portrait, beatenBy != null && beatenBy.isCleared(duel.id));
		float size = height * 0.28f;
		if (face != null)
			face.draw(width * 0.08f, height * 0.22f, size, size);
		float textX = width * 0.08f + (face == null ? 0 : size + width * 0.03f);
		float textW = width * 0.92f - textX;
		UnicodeFont font = Options.getSmallFont();
		StoryCampaign.Line line = (lineIndex >= 0 && lineIndex < duel.bio.length) ? duel.bio[lineIndex] : null;
		float y = height * 0.24f;
		if (line != null) {
			ArrayList<String> wrapped = wrap(font, line.text(camp()), textW);
			for (int i = 0; i < wrapped.size() && i < 6; i++) {
				font.drawString(textX, y, wrapped.get(i), Color.white);
				y += font.getLineHeight();
			}
		}
		ArrayList<String> style = wrap(font, duel.style(), width * 0.84f);
		y = height * 0.62f;
		for (int i = 0; i < style.size() && i < 3; i++) {
			font.drawString(width * 0.08f, y, style.get(i), Ui.HINT);
			y += font.getLineHeight();
		}
		HintBar.draw(I18n.hintStoryTalk(), height * 0.93f);
	}

	private void renderScene(Graphics g, int width, int height) {
		if (showingVideo && video != null) {
			if (!video.draw(0, 0, width, height)) {
				finishVideo();
			} else {
				Ui.drawCentered(Options.getSmallFont(), I18n.hintStoryVideo(), height * 0.94f, Ui.HINT);
				return;
			}
		}
		if (lineIndex < 0 || lineIndex >= lines.length)
			return;
		StoryCampaign.Line line = lines[lineIndex];
		if (sceneStill() != null) {
			renderStill(g, width, height, line);
			return;
		}
		float boxW = width * 0.92f;
		float boxH = height * 0.28f;
		float boxX = (width - boxW) / 2f;
		float boxY = height * 0.62f;
		Image box = StoryAssets.ui("caixa_dialogo.png");
		if (box != null)
			box.draw(boxX, boxY, boxW, boxH);
		else {
			g.setColor(new Color(0.05f, 0.07f, 0.12f, 0.92f));
			g.fillRect(boxX, boxY, boxW, boxH);
		}
		StorySave save = camp();
		float face = boxH * 0.72f;
		float faceX = boxX + width * 0.02f;
		float faceY = boxY + (boxH - face) / 2f;
		float textX = sceneTextX(line);
		if (line.narrator) {
			Image nilo = narratorPortrait();
			if (nilo != null)
				nilo.draw(faceX, faceY, face, face);
			else
				drawNarrator(g, faceX, faceY, face);
		} else {
			Image portrait = StoryAssets.portrait(line.portraitFor(save), line.defeat || (line.isPlayer() && sceneDefeat));
			if (portrait != null)
				portrait.draw(faceX, faceY, face, face);
		}
		Image listener = listenerPortrait(line);
		if (listener != null) {
			float lx = boxX + boxW - width * 0.02f - face;
			g.setColor(new Color(0.72f, 0.58f, 0.28f));
			g.fillRect(lx - 2f, faceY - 2f, face + 4f, face + 4f);
			listener.draw(lx, faceY, face, face);
		}
		float textW = sceneTextWidth(line);
		UnicodeFont small = Options.getSmallFont();
		String who = line.name(save);
		if (who != null && !who.isEmpty())
			small.drawString(textX, boxY + boxH * 0.1f, who, Ui.TITLE);
		ArrayList<String> wrapped = wrap(small, line.text(camp()), textW);
		int from = page * 3;
		float textY = boxY + boxH * 0.34f;
		for (int i = from; i < wrapped.size() && i < from + 3; i++) {
			small.drawString(textX, textY, wrapped.get(i), Color.white);
			textY += small.getLineHeight();
		}
		HintBar.draw(I18n.hintStoryTalk(), height * 0.96f);
	}

	/**
	 * Nilo Vann's portrait. After a lost match (the student's rematch scene)
	 * the caller shows his frustrated face.
	 */
	private Image narratorPortrait() {
		return StoryAssets.portrait(StoryCampaign.NARRATOR_PORTRAIT, sceneDefeat);
	}

	/** Lines per page in the left-third panel of a full-screen still. */
	private static final int STILL_LINES = 7;

	/** Left third of the screen for a full-screen still (x, y, w, h). */
	private static float[] stillPanel() {
		float width = Options.getWidth();
		float height = Options.getHeight();
		return new float[] { width * 0.025f, height * 0.08f, width * 0.31f, height * 0.78f };
	}

	/**
	 * Full-screen still (e.g. revelacao_cc_king.png). The art keeps its left third
	 * dark, so the speaker and the text go there.
	 */
	private void renderStill(Graphics g, int width, int height, StoryCampaign.Line line) {
		float[] p = stillPanel();
		g.setColor(new Color(0.02f, 0.03f, 0.07f, 0.55f));
		g.fillRect(p[0], p[1], p[2], p[3]);
		g.setColor(new Color(0.72f, 0.58f, 0.28f));
		g.fillRect(p[0], p[1], p[2], 2f);
		g.fillRect(p[0], p[1] + p[3] - 2f, p[2], 2f);
		StorySave save = camp();
		float pad = width * 0.015f;
		float face = Math.min(p[2] * 0.42f, height * 0.2f);
		float x = p[0] + pad;
		float y = p[1] + pad;
		Image portrait = line.narrator ? narratorPortrait()
			: StoryAssets.portrait(line.portraitFor(save), line.defeat || (line.isPlayer() && sceneDefeat));
		if (portrait != null) {
			portrait.draw(x, y, face, face);
		} else if (line.narrator) {
			drawNarrator(g, x, y, face);
		}
		UnicodeFont small = Options.getSmallFont();
		String who = line.name(save);
		float nameY = y + face + small.getLineHeight() * 0.3f;
		if (who != null && !who.isEmpty())
			small.drawString(x, nameY, who, Ui.TITLE);
		ArrayList<String> wrapped = wrap(small, line.text(save), stillTextWidth());
		int from = page * STILL_LINES;
		float textY = nameY + small.getLineHeight() * 1.3f;
		for (int i = from; i < wrapped.size() && i < from + STILL_LINES; i++) {
			small.drawString(x, textY, wrapped.get(i), Color.white);
			textY += small.getLineHeight();
		}
		HintBar.draw(I18n.hintStoryTalk(), height * 0.96f);
	}

	private static float stillTextWidth() {
		return stillPanel()[2] - Options.getWidth() * 0.03f;
	}

	/** Original mark for Nilo Vann, used when narrador_*.png is missing. */
	private void drawNarrator(Graphics g, float x, float y, float size) {
		g.setColor(new Color(0.72f, 0.58f, 0.28f));
		g.fillRect(x, y, size, size);
		g.setColor(new Color(0.07f, 0.08f, 0.12f));
		g.fillRect(x + 4f, y + 4f, size - 8f, size - 8f);
		g.setColor(new Color(0.78f, 0.66f, 0.36f));
		g.fillRect(x + size * 0.3f, y + size * 0.42f, size * 0.4f, size * 0.18f);
		g.fillRect(x + size * 0.44f, y + size * 0.6f, size * 0.12f, size * 0.2f);
		UnicodeFont font = Options.getSmallFont();
		String mark = "NV";
		font.drawString(x + (size - font.getWidth(mark)) / 2f, y + size * 0.12f, mark,
			new Color(0.93f, 0.84f, 0.55f));
		String tag = I18n.text("LOCUTOR", "CALLER", "LOCUTOR");
		String fit = Ui.fit(font, tag, size - 8f);
		font.drawString(x + (size - font.getWidth(fit)) / 2f, y + size * 0.78f, fit, Ui.HINT);
	}

	private void renderPick(Graphics g, int width, int height, boolean confirming) {
		StorySave save = camp();
		Ui.drawCentered(Options.getFont(), I18n.storyChoose(), height * 0.1f, Ui.TITLE);
		if (duel != null && save != null && save.pendingSpecial() > 0) {
			Card special = game.getDeck().getCardById(save.pendingSpecial());
			String extra = I18n.storySpecialCard() + ": " + (special == null ? "" : special.getName());
			Ui.drawCentered(Options.getSmallFont(), extra, height * 0.17f, Ui.TITLE);
		}
		int[] hand = save == null ? new int[0] : save.pendingHand();
		float size = Options.getCardLength() * 0.42f;
		float gap = size * 0.12f;
		float total = hand.length * size + Math.max(0, hand.length - 1) * gap;
		float x = (width - total) / 2f;
		float y = height * 0.28f;
		for (int i = 0; i < hand.length; i++) {
			Card card = game.getDeck().getCardById(hand[i]);
			if (card != null)
				card.drawSized(x, y, size, false, !save.eligible(hand[i]));
			if (i == cardIndex) {
				g.setColor(Ui.TITLE);
				g.fillRect(x, y + size + 4f, size, 3f);
			}
			x += size + gap;
		}
		if (cardIndex >= 0 && cardIndex < hand.length) {
			Card card = game.getDeck().getCardById(hand[cardIndex]);
			if (card != null)
				Ui.drawCentered(Options.getSmallFont(), card.getName(), height * 0.72f, Color.white);
		}
		if (confirming && cardIndex >= 0 && cardIndex < hand.length) {
			Card card = game.getDeck().getCardById(hand[cardIndex]);
			String label = card == null ? "" : card.getName();
			renderYesNo(g, height, I18n.storyConfirmCard(label));
			return;
		}
		if (preview && cardIndex >= 0 && cardIndex < hand.length)
			Ui.drawCardPreview(g, game.getDeck(), hand[cardIndex]);
		HintBar.draw(I18n.hintStoryPick(), height * 0.92f);
	}

	private void renderShow(Graphics g, int width, int height) {
		Ui.drawCentered(Options.getFont(), I18n.storySaved(), height * 0.1f, Ui.TITLE);
		float size = Options.getCardLength() * 0.48f;
		if (shown.length == 1) {
			drawRewardCard(g, width, height, shown[0], I18n.storyChosen(), width * 0.5f, size);
		} else if (shown.length >= 2) {
			drawRewardCard(g, width, height, shown[0], I18n.storyChosen(), width * 0.32f, size);
			drawRewardCard(g, width, height, shown[1], I18n.storySpecialCard(), width * 0.68f, size);
		}
		HintBar.draw(I18n.hintStoryReward(), height * 0.92f);
	}

	private void drawRewardCard(Graphics g, int width, int height, int id, String label, float center, float size) {
		Card card = game.getDeck().getCardById(id);
		float x = center - size / 2f;
		float y = height * 0.3f;
		if (card != null)
			card.drawSized(x, y, size, true, false);
		String name = card == null ? "" : card.getName();
		UnicodeFont font = Options.getSmallFont();
		font.drawString(center - font.getWidth(name) / 2f, y + size + font.getLineHeight() * 0.4f, name, Color.white);
		font.drawString(center - font.getWidth(label) / 2f, y - font.getLineHeight() * 1.2f, label, Ui.TITLE);
	}

	private void drawChoice(UnicodeFont font, int row, int selected, String label,
			float start, float step, Image icon, Color color) {
		float y = start + row * step;
		float iconSize = Math.min(step * 0.8f, 32f);
		float textX = Options.getWidth() * 0.22f;
		if (icon != null)
			icon.draw(textX - iconSize - 8f, y + (font.getLineHeight() - iconSize) / 2f, iconSize, iconSize);
		font.drawString(textX, y, label, color);
		if (row == selected) {
			Image cursor = GameImage.CURSOR.getImage();
			cursor.draw(textX - iconSize - cursor.getWidth() - 14f,
				y + (font.getLineHeight() - cursor.getHeight()) / 2f);
		}
	}

	private Image iconFor(boolean unlocked, boolean cleared) {
		if (!unlocked)
			return StoryAssets.ui("no_bloqueado.png");
		if (cleared)
			return StoryAssets.ui("no_vencido.png");
		return StoryAssets.ui("no_disponivel.png");
	}

	private void drawBackground(Graphics g, int width, int height, String file) {
		g.setColor(new Color(0f, 0f, 0f));
		g.fillRect(0, 0, width, height);
		Image bg = StoryAssets.background(file);
		if (bg != null)
			letterbox(bg, 0, 0, width, height);
		float shade = 0.45f;
		if (mode == Mode.SCENE && (showingVideo || sceneStill() != null))
			shade = 0f;
		else if (mode == Mode.MAP)
			shade = 0.1f;
		else if (mode == Mode.HUB)
			shade = 0.25f;
		if (shade > 0f) {
			g.setColor(new Color(0f, 0f, 0f, shade));
			g.fillRect(0, 0, width, height);
		}
	}

	/**
	 * The protagonist listens on the right of every box they don't speak in.
	 * After a lost match the defeat face is used.
	 */
	private Image listenerPortrait(StoryCampaign.Line line) {
		StorySave save = camp();
		if (save == null || line == null || line.isPlayer())
			return null;
		return StoryAssets.portrait(save.portraitId(), sceneDefeat);
	}

	/** Left edge of the dialogue text; same math for drawing and paging. */
	private float sceneTextX(StoryCampaign.Line line) {
		float width = Options.getWidth();
		float height = Options.getHeight();
		float boxX = (width - width * 0.92f) / 2f;
		float face = height * 0.28f * 0.72f;
		float faceX = boxX + width * 0.02f;
		if (line.narrator)
			return faceX + face + width * 0.02f;
		if (StoryAssets.portrait(line.portraitFor(camp()), line.defeat) != null)
			return faceX + face + width * 0.015f;
		return faceX;
	}

	private float sceneTextWidth(StoryCampaign.Line line) {
		float width = Options.getWidth();
		float height = Options.getHeight();
		float boxW = width * 0.92f;
		float boxX = (width - boxW) / 2f;
		float right = boxX + boxW - width * 0.03f;
		if (listenerPortrait(line) != null)
			right = boxX + boxW - width * 0.02f - height * 0.28f * 0.72f - width * 0.02f;
		return Math.max(width * 0.2f, right - sceneTextX(line));
	}

	private interface RowAction { void run(int row); }

	private void clickList(int y, int count, RowAction action) {
		int hit = hitRow(y, count);
		if (hit < 0)
			return;
		if (hit != index) {
			index = hit;
			AudioController.playCursor();
			return;
		}
		action.run(hit);
	}

	private int hitRow(int y, int count) {
		float start = mode == Mode.HUB ? Options.getHeight() * 0.36f
			: (mode == Mode.STAGE ? Options.getHeight() * 0.34f : Options.getHeight() * 0.16f);
		if (mode == Mode.CAMPAIGNS)
			start = Options.getHeight() * 0.18f;
		float step = rowStep(Math.max(1, count));
		float line = Options.getSmallFont().getLineHeight();
		for (int i = 0; i < count; i++) {
			float top = start + i * step;
			if (y >= top && y < top + Math.max(line, step * 0.9f))
				return i;
		}
		return -1;
	}

	private static float rowStep(int count) {
		float available = Options.getHeight() * 0.55f;
		float natural = Options.getSmallFont().getLineHeight() * 1.45f;
		if (count < 1)
			return natural;
		return Math.min(natural, available / count);
	}

	private static StoryCampaign.Line[] join(StoryCampaign.Line[] a, StoryCampaign.Line[] b) {
		int na = a == null ? 0 : a.length;
		int nb = b == null ? 0 : b.length;
		StoryCampaign.Line[] out = new StoryCampaign.Line[na + nb];
		for (int i = 0; i < na; i++)
			out[i] = a[i];
		for (int i = 0; i < nb; i++)
			out[na + i] = b[i];
		return out;
	}

	private static ArrayList<String> wrap(UnicodeFont font, String text, float maxWidth) {
		ArrayList<String> out = new ArrayList<String>();
		if (text == null || text.isEmpty())
			return out;
		String[] words = text.split(" ");
		StringBuilder current = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String trial = (current.length() == 0) ? words[i] : current + " " + words[i];
			if (font.getWidth(trial) <= maxWidth)
				current = new StringBuilder(trial);
			else {
				if (current.length() > 0)
					out.add(current.toString());
				current = new StringBuilder(words[i]);
			}
		}
		if (current.length() > 0)
			out.add(current.toString());
		return out;
	}

	private static void letterbox(Image image, float x, float y, float w, float h) {
		float scale = Math.min(w / image.getWidth(), h / image.getHeight());
		float dw = image.getWidth() * scale;
		float dh = image.getHeight() * scale;
		image.draw(x + (w - dw) / 2f, y + (h - dh) / 2f, dw, dh);
	}

	private void resumeBgm() {
		if (!pausedBgm)
			return;
		pausedBgm = false;
		AudioController.applyMusic();
	}

	private void disposeVideo() {
		if (video != null) {
			video.dispose();
			video = null;
		}
		showingVideo = false;
	}
}
