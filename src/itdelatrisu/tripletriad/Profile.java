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

import java.util.ArrayList;
import java.util.List;

import itdelatrisu.tripletriad.story.StorySave;

/**
 * Local player profile: display name and saved decks.
 */
public class Profile {
	/** Player display name. */
	private String name;

	/** Saved decks. */
	private final ArrayList<SavedDeck> decks;

	/** Championship collection (card IDs, duplicates allowed). */
	private final ArrayList<Integer> collection;

	/** Completed championship runs. */
	private int championshipWins;

	/** Number of avatars in {@code res/avatars/}. */
	public static final int AVATARS = 8;

	/** Avatar index [1, AVATARS] (old profiles = 1). */
	private int avatar = 1;

	/** Trophy counters: Quick Game wins / losses / draws, championship matches won / lost. */
	private int quickWins, quickLosses, quickDraws, champMatchWins, champMatchLosses;

	/** @return avatar index [1, AVATARS] */
	public int getAvatar() { return avatar; }

	/** @param a avatar index (clamped to [1, AVATARS]) */
	public void setAvatar(int a) { this.avatar = Math.max(1, Math.min(AVATARS, a)); }

	public int getQuickWins() { return quickWins; }
	public int getQuickLosses() { return quickLosses; }
	public int getQuickDraws() { return quickDraws; }
	public int getChampMatchWins() { return champMatchWins; }
	public int getChampMatchLosses() { return champMatchLosses; }
	public void setQuickWins(int n) { quickWins = Math.max(0, n); }
	public void setQuickLosses(int n) { quickLosses = Math.max(0, n); }
	public void setQuickDraws(int n) { quickDraws = Math.max(0, n); }
	public void setChampMatchWins(int n) { champMatchWins = Math.max(0, n); }
	public void setChampMatchLosses(int n) { champMatchLosses = Math.max(0, n); }

	/**
	 * Records a finished Quick Game.
	 * @param result 1 win, -1 loss, 0 draw
	 */
	public void recordQuickGame(int result) {
		if (result > 0) quickWins++;
		else if (result < 0) quickLosses++;
		else quickDraws++;
	}

	/**
	 * Records a finished championship match.
	 * @param won true if the player won
	 */
	public void recordChampionshipMatch(boolean won) {
		if (won) champMatchWins++;
		else champMatchLosses++;
	}

	/** Number of save blocks on the championship "memory card". */
	public static final int SLOTS = 15;

	/** Championship save blocks (null = free). */
	private final ChampionshipSlot[] slots = new ChampionshipSlot[SLOTS];

	/** Legacy (pre-1.3.0) single save: round (0 = none). Migrated to block 01. */
	private int runRound;

	/** Saved run wins. */
	private int runWins;

	/** Saved 5-card hand for the current run match. */
	private int[] runHand;

	/** Saved opponent hand for the current run match. */
	private int[] runOpponent;

	/** Saved card bag for the current championship run. */
	private final ArrayList<Integer> runBag;

	/**
	 * Legacy story progress (pre-campaign). Kept on disk as a snapshot.
	 * Play uses {@link #campaigns} after migration.
	 */
	private final ArrayList<String> storyCleared = new ArrayList<String>();

	/** Legacy arrivals. Kept as a snapshot. */
	private final ArrayList<String> storyArrivals = new ArrayList<String>();

	/** Independent story campaigns. The album is not inside them. */
	private final ArrayList<StorySave> campaigns = new ArrayList<StorySave>();

	/** Id of the campaign the story menu will open. */
	private String activeStoryId = "";

	/** True after the legacy STORY_CLEARED list has been considered. */
	private boolean storyMigrated;

	/**
	 * Creates an empty profile.
	 */
	public Profile() {
		this.name = "";
		this.decks = new ArrayList<SavedDeck>();
		this.collection = new ArrayList<Integer>();
		this.runBag = new ArrayList<Integer>();
		this.championshipWins = 0;
	}

	/**
	 * Returns the player name.
	 * @return the name
	 */
	public String getName() { return name; }

	/**
	 * Sets the player name.
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = (name != null) ? name.trim() : "";
	}

	/**
	 * Returns whether this profile can unlock the main menu.
	 * @return true if a name is set
	 */
	public boolean isValid() {
		return name != null && !name.isEmpty();
	}

	/**
	 * Returns the saved decks.
	 * @return the list of decks
	 */
	public ArrayList<SavedDeck> getDecks() { return decks; }

	/**
	 * Inserts or replaces a deck by name (case-insensitive).
	 * @param deck the deck to store
	 */
	public void upsertDeck(SavedDeck deck) {
		if (deck == null || deck.getName().isEmpty())
			return;
		for (int i = 0; i < decks.size(); i++) {
			if (decks.get(i).getName().equalsIgnoreCase(deck.getName())) {
				decks.set(i, deck);
				return;
			}
		}
		decks.add(deck);
	}

	/**
	 * Removes a saved deck.
	 * @param deck the deck to remove
	 */
	public void removeDeck(SavedDeck deck) {
		decks.remove(deck);
	}

	/**
	 * Returns the championship collection (card IDs).
	 * @return the list
	 */
	public ArrayList<Integer> getCollection() { return collection; }

	/**
	 * Adds a card to the championship collection.
	 * @param cardId the catalog ID
	 */
	public void addCard(int cardId) {
		if (cardId > 0)
			collection.add(Integer.valueOf(cardId));
	}

	/**
	 * Empties the championship collection.
	 */
	public void clearCollection() {
		collection.clear();
	}

	/**
	 * Replaces the album with the given card IDs (IDs of 0 or less are skipped).
	 * @param cards the IDs
	 */
	public void replaceCollection(List<Integer> cards) {
		collection.clear();
		if (cards == null)
			return;
		for (int i = 0; i < cards.size(); i++) {
			int id = cards.get(i).intValue();
			if (id > 0)
				collection.add(Integer.valueOf(id));
		}
	}

	/**
	 * Removes the first instance of a card ID from the collection.
	 * @param cardId the catalog ID
	 * @return true if a card was removed
	 */
	public boolean removeCard(int cardId) {
		for (int i = 0; i < collection.size(); i++) {
			if (collection.get(i).intValue() == cardId) {
				collection.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns how many championships this profile has won.
	 * @return the count
	 */
	public int getChampionshipWins() { return championshipWins; }

	/**
	 * Sets championship wins (used when loading).
	 * @param wins the count
	 */
	public void setChampionshipWins(int wins) {
		this.championshipWins = (wins < 0) ? 0 : wins;
	}

	/**
	 * Records one completed championship.
	 */
	public void addChampionshipWin() {
		championshipWins++;
	}

	/**
	 * Returns whether any championship save block is used.
	 * @return true if at least one block holds a run
	 */
	public boolean hasChampionshipSave() {
		for (ChampionshipSlot slot : slots) {
			if (slot != null)
				return true;
		}
		return false;
	}

	/**
	 * Returns a save block.
	 * @param index the block index [0, SLOTS)
	 * @return the block, or null if free / out of range
	 */
	public ChampionshipSlot getSlot(int index) {
		return (index >= 0 && index < SLOTS) ? slots[index] : null;
	}

	/**
	 * Writes (or clears with null) a save block.
	 * @param index the block index
	 * @param slot the block, or null
	 */
	public void setSlot(int index, ChampionshipSlot slot) {
		if (index < 0 || index >= SLOTS)
			return;
		slots[index] = (slot != null && slot.isValid()) ? slot : null;
	}

	/**
	 * Returns the first free block.
	 * @return the index, or -1 if the card is full
	 */
	public int firstFreeSlot() {
		for (int i = 0; i < SLOTS; i++) {
			if (slots[i] == null)
				return i;
		}
		return -1;
	}

	/**
	 * Returns the most recently written block.
	 * @return the index, or -1 if none
	 */
	public int latestSlot() {
		int best = -1;
		for (int i = 0; i < SLOTS; i++) {
			if (slots[i] != null && (best < 0 || slots[i].getSavedAt() > slots[best].getSavedAt()))
				best = i;
		}
		return best;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunRound(int round) {
		this.runRound = (round < 0) ? 0 : round;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunWins(int wins) {
		this.runWins = (wins < 0) ? 0 : wins;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunHand(int[] ids) {
		this.runHand = (ids != null) ? ids.clone() : null;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunOpponent(int[] ids) {
		this.runOpponent = (ids != null) ? ids.clone() : null;
	}

	/** Legacy setter (pre-1.3.0 profile files). */
	public void setRunBag(ArrayList<Integer> cards) {
		runBag.clear();
		if (cards == null)
			return;
		for (int i = 0; i < cards.size(); i++) {
			int id = cards.get(i).intValue();
			if (id > 0)
				runBag.add(Integer.valueOf(id));
		}
	}

	/**
	 * Moves a legacy single championship save (RUN_* keys) into block 01
	 * (or the first free block) and clears the legacy fields.
	 * @return true if something was migrated
	 */
	public boolean migrateLegacyRun() {
		if (runRound < 2)
			return false;
		ChampionshipSlot slot = new ChampionshipSlot();
		slot.setRound(runRound);
		slot.setWins(runWins);
		slot.setHand(runHand);
		slot.setOpponent(runOpponent);
		slot.setBag(runBag.isEmpty() ? collection : runBag);
		slot.setSavedAt(System.currentTimeMillis());
		int index = (slots[0] == null) ? 0 : firstFreeSlot();
		runRound = 0;
		runWins = 0;
		runHand = null;
		runOpponent = null;
		runBag.clear();
		if (index < 0)
			return false;
		slots[index] = slot;
		return true;
	}

	/** @return true if the active campaign has cleared this duel */
	public boolean isStoryCleared(String id) {
		StorySave save = activeStory();
		return save != null && save.isCleared(id);
	}

	/** Marks the duel on the active campaign. */
	public void markStoryCleared(String id) {
		StorySave save = activeStory();
		if (save != null && id != null && !id.isEmpty() && !save.isCleared(id))
			save.setClearedCsv(save.clearedCsv().isEmpty() ? id : save.clearedCsv() + "," + id);
	}

	/** @return true if the active campaign has seen this arrival */
	public boolean hasSeenArrival(String id) {
		StorySave save = activeStory();
		return save != null && save.hasArrival(id);
	}

	/** Remembers the arrival on the active campaign. */
	public void markArrival(String id) {
		StorySave save = activeStory();
		if (save != null)
			save.markArrival(id);
	}

	public boolean isStoryMigrated() { return storyMigrated; }

	public void setStoryMigrated(boolean value) { storyMigrated = value; }

	public String getActiveStoryId() { return activeStoryId == null ? "" : activeStoryId; }

	public void setActiveStoryId(String id) { activeStoryId = (id == null) ? "" : id; }

	public ArrayList<StorySave> getCampaigns() { return campaigns; }

	public int campaignCount() { return campaigns.size(); }

	public StorySave campaignAt(int index) {
		return (index >= 0 && index < campaigns.size()) ? campaigns.get(index) : null;
	}

	public StorySave findCampaign(String id) {
		if (id == null)
			return null;
		for (int i = 0; i < campaigns.size(); i++) {
			if (id.equals(campaigns.get(i).id()))
				return campaigns.get(i);
		}
		return null;
	}

	public StorySave activeStory() {
		StorySave found = findCampaign(activeStoryId);
		if (found != null)
			return found;
		return campaigns.isEmpty() ? null : campaigns.get(0);
	}

	public void setActiveStory(String id) {
		if (findCampaign(id) != null)
			activeStoryId = id;
	}

	/**
	 * Appends a campaign. Does not touch the album or the other campaigns.
	 * @return the campaign, or null if the name is empty or the list is full
	 */
	public StorySave addCampaign(String student) {
		return addCampaign(student, StorySave.LEGACY_GENDER);
	}

	/**
	 * Appends a campaign with the chosen protagonist.
	 * @param gender {@link StorySave#MALE} or {@link StorySave#FEMALE}
	 * @return the campaign, or null if the name is empty or the list is full
	 */
	public StorySave addCampaign(String student, String gender) {
		if (campaigns.size() >= StorySave.MAX)
			return null;
		StorySave save = StorySave.create(student, gender);
		if (save == null)
			return null;
		campaigns.add(save);
		activeStoryId = save.id();
		return save;
	}

	/**
	 * Removes one campaign. Cards already in the album stay there.
	 * @param confirm must be true; a cancel leaves the list as it is
	 */
	public boolean deleteCampaign(String id, boolean confirm) {
		if (!confirm || id == null)
			return false;
		for (int i = 0; i < campaigns.size(); i++) {
			if (id.equals(campaigns.get(i).id())) {
				campaigns.remove(i);
				if (id.equals(activeStoryId))
					activeStoryId = campaigns.isEmpty() ? "" : campaigns.get(0).id();
				return true;
			}
		}
		return false;
	}

	/** Adds story reward cards to the shared album. */
	public void grantStoryCards(int[] ids) {
		if (ids == null)
			return;
		for (int i = 0; i < ids.length; i++)
			addCard(ids[i]);
	}

	/**
	 * Turns a pre-campaign clear list into campaign 1, once.
	 * The original list stays in memory so the file can keep it.
	 */
	public void migrateStoryIfNeeded() {
		if (storyMigrated)
			return;
		storyMigrated = true;
		if (!campaigns.isEmpty())
			return;
		if (storyCleared.isEmpty() && storyArrivals.isEmpty())
			return;
		String student = StorySave.cleanName(name);
		if (student.isEmpty())
			student = "Aluno";
		StorySave save = StorySave.create(student);
		if (save == null)
			return;
		save.absorbLegacy(getStoryClearedCsv(), getStoryArrivalsCsv());
		campaigns.add(save);
		activeStoryId = save.id();
	}

	public String getStoryClearedCsv() { return joinIds(storyCleared); }

	public String getStoryArrivalsCsv() { return joinIds(storyArrivals); }

	public void setStoryClearedCsv(String csv) { fillIds(storyCleared, csv); }

	public void setStoryArrivalsCsv(String csv) { fillIds(storyArrivals, csv); }

	private static String joinIds(ArrayList<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0)
				builder.append(',');
			builder.append(list.get(i));
		}
		return builder.toString();
	}

	private static void fillIds(ArrayList<String> list, String csv) {
		list.clear();
		if (csv == null || csv.isEmpty())
			return;
		String[] parts = csv.split(",");
		for (int i = 0; i < parts.length; i++) {
			String id = parts[i].trim();
			if (!id.isEmpty() && !list.contains(id))
				list.add(id);
		}
	}
}
