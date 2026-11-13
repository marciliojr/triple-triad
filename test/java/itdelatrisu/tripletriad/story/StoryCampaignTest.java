package itdelatrisu.tripletriad.story;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Random;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.ProfileStore;
import itdelatrisu.tripletriad.gfx.Assets;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Kind;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;
import itdelatrisu.tripletriad.story.StoryCampaign.Row;

/** Save, table and reward rules for the story tournament. */
public class StoryCampaignTest {
	@Test
	public void twoCampaignsStayIndependent() {
		Profile profile = named("Marcilio");
		profile.addCard(3);
		StorySave ana = profile.addCampaign("Ana");
		StorySave bia = profile.addCampaign("Bia");
		assertNotNull(ana);
		assertNotNull(bia);
		assertEquals(2, profile.campaignCount());
		ana.setClearedCsv("king");
		profile.setActiveStory(bia.id());
		assertFalse(profile.isStoryCleared("king"));
		profile.setActiveStory(ana.id());
		assertTrue(profile.isStoryCleared("king"));
		assertEquals(1, copies(profile, 3));

		assertFalse(profile.deleteCampaign(bia.id(), false));
		assertEquals(2, profile.campaignCount());
		assertNull(profile.addCampaign("   "));
		assertEquals(2, profile.campaignCount());

		profile.grantStoryCards(new int[] { 12 });
		assertTrue(profile.deleteCampaign(ana.id(), true));
		assertEquals(1, copies(profile, 12));
		assertEquals(1, copies(profile, 3));
		assertEquals(bia.id(), profile.activeStory().id());
	}

	@Test
	public void fifteenthCampaignIsTheLast() {
		Profile profile = named("Marcilio");
		for (int i = 0; i < StorySave.MAX; i++)
			assertNotNull(profile.addCampaign("A" + i));
		assertNull(profile.addCampaign("extra"));
		assertEquals(StorySave.MAX, profile.campaignCount());
	}

	@Test
	public void saveRoundTripKeepsPendingReward() throws Exception {
		Profile profile = named("Marcilio");
		StorySave save = profile.addCampaign("Ana");
		save.armWin("king", new int[] { 1, 2, 3, 4, 5 }, 85);
		save.advanceToPick();
		save.markArrival("garden");
		File file = Files.createTempFile("story-profile", ".profile").toFile();
		file.deleteOnExit();
		ProfileStore.writeFile(file, profile);

		Profile loaded = ProfileStore.readFile(file);
		assertNotNull(loaded);
		assertEquals(1, loaded.campaignCount());
		StorySave again = loaded.activeStory();
		assertEquals("Ana", again.student());
		assertEquals(StorySave.PICK, again.pendingPhase());
		assertArrayEquals(new int[] { 1, 2, 3, 4, 5 }, again.pendingHand());
		assertEquals(85, again.pendingSpecial());
		assertTrue(again.hasArrival("garden"));
		assertEquals("king", loaded.getStoryClearedCsv().isEmpty() ? again.pendingDuel() : again.pendingDuel());

		int[] granted = again.confirm(2);
		assertArrayEquals(new int[] { 2, 85 }, granted);
		loaded.grantStoryCards(granted);
		ProfileStore.writeFile(file, loaded);
		Profile after = ProfileStore.readFile(file);
		assertEquals(0, after.activeStory().confirm(2).length);
		assertEquals(1, copies(after, 2));
		assertEquals(1, copies(after, 85));
		assertFalse(new File(file.getParentFile(), file.getName() + ".tmp").isFile());
	}

	@Test
	public void legacyListMigratesOnceAndKeepsSpecialsSpent() {
		Profile profile = named("Marcilio");
		profile.setStoryClearedCsv("king,final,desconhecido");
		profile.setStoryArrivalsCsv("garden");
		profile.migrateStoryIfNeeded();

		assertEquals("king,final,desconhecido", profile.getStoryClearedCsv());
		assertEquals(1, profile.campaignCount());
		StorySave save = profile.activeStory();
		assertTrue(save.isFromLegacy());
		assertTrue(save.isComplete());
		assertTrue(save.isCleared("desconhecido"));
		assertTrue(save.specialGranted("king"));
		profile.migrateStoryIfNeeded();
		assertEquals(1, profile.campaignCount());

		save.armWin("king", new int[] { 1, 2, 3, 4, 5 }, 85);
		assertEquals(0, save.pendingSpecial());
	}

	@Test
	public void oneCardPerWinAndSpecialOnlyOnce() {
		StorySave save = StorySave.create("Ana");
		save.armWin("queen_town", new int[] { 1, 2, 3, 4, 84 }, 84);
		assertEquals(0, save.pendingSpecial());
		assertNull(save.confirm(84));
		assertTrue(save.hasPending());
		assertNull(save.confirm(99));
		assertArrayEquals(new int[] { 1 }, save.confirm(1));
		assertFalse(save.specialGranted("queen_town"));

		save.armWin("queen_town", new int[] { 1, 2, 3, 4, 5 }, 84);
		assertEquals(84, save.pendingSpecial());
		assertArrayEquals(new int[] { 5, 84 }, save.confirm(5));
		assertTrue(save.specialGranted("queen_town"));

		save.armWin("queen_town", new int[] { 1, 2, 3, 4, 5 }, 84);
		assertEquals(0, save.pendingSpecial());
		assertArrayEquals(new int[] { 2 }, save.confirm(2));
		assertEquals(0, save.confirm(2).length);
	}

	@Test
	public void firstWinOffersTheSpecialBesideTheHand() {
		StorySave save = StorySave.create("Ana");
		save.armWin("seifer", new int[] { 10, 11, 12, 13, 14 }, 109);
		assertEquals(StorySave.TALK, save.pendingPhase());
		assertEquals(109, save.pendingSpecial());
		save.armWin("outro", new int[] { 1, 2, 3, 4, 5 }, 85);
		assertEquals("seifer", save.pendingDuel());
	}

	@Test
	public void fullSweepQualifiesAndALossScoresNothing() {
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			StorySave empty = StorySave.create("Ana");
			assertFalse(StoryCampaign.stageCleared(region, empty));
			if (region.arrival == null || region.arrival.length == 0)
				throw new AssertionError(region.id);
		}
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			StorySave save = StorySave.create("Ana");
			clear(save, region);
			assertTrue(StoryCampaign.stageCleared(region, save), region.id);
			if (region.kind != Kind.GROUP)
				continue;
			assertTrue(StoryCampaign.playerRank(region, save) <= region.cutoff, region.id);
			int scored = 0;
			for (int i = 0; i < region.duels.length; i++) {
				if (region.duels[i].inTable)
					scored++;
			}
			assertEquals(scored * 3, playerPoints(region, save), region.id);
		}
		Region town = StoryCampaign.REGIONS[1];
		StorySave partial = StorySave.create("Ana");
		partial.setClearedCsv(town.duels[0].id);
		assertEquals(town.duels[0].inTable ? 3 : 0, playerPoints(town, partial));
		assertFalse(StoryCampaign.stageCleared(town, partial));
	}

	@Test
	public void rolledHandsNeverContainSpecials() throws Exception {
		int[][] catalog = readCatalog();
		Random random = new Random(7);
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Duel[] duels = StoryCampaign.REGIONS[r].duels;
			for (int d = 0; d < duels.length; d++) {
				Duel duel = duels[d];
				if (duel.rewardId > 0)
					assertTrue(StoryCampaign.isSpecial(duel.rewardId), duel.id);
				for (int n = 0; n < 8; n++) {
					int[] hand = StoryCampaign.rollHand(catalog[0], catalog[1], duel.minLevel, duel.maxLevel, random);
					assertEquals(5, hand.length, duel.id);
					for (int i = 0; i < hand.length; i++)
						assertFalse(StoryCampaign.isSpecial(hand[i]), duel.id);
				}
			}
		}
	}

	@Test
	public void onlyImportedStoryVideosExist() {
		String[] ready = { "intro_historia", "chegada_balamb_town", "chegada_timber", "chegada_deling" };
		for (int i = 0; i < ready.length; i++) {
			assertTrue(StoryAssets.hasVideo(ready[i]), ready[i]);
			assertTrue(StoryAssets.hasVideoAudio(ready[i]), ready[i]);
		}
		for (int i = 0; i < StoryCampaign.PENDING_VIDEOS.length; i++) {
			String id = StoryCampaign.PENDING_VIDEOS[i];
			assertFalse(StoryAssets.hasVideo(id), id);
			assertFalse(StoryAssets.hasVideoAudio(id), id);
			assertNotNull(StoryCampaign.findDuel("queen_esthar"));
		}
		assertTrue(StoryAssets.hasVideo(null) == false);
		File still = Assets.file("story/backgrounds/mesa_timber.png");
		assertTrue(still.isFile());
	}

	private static Profile named(String name) {
		Profile profile = new Profile();
		profile.setName(name);
		return profile;
	}

	private static int copies(Profile profile, int cardId) {
		int n = 0;
		for (int i = 0; i < profile.getCollection().size(); i++) {
			if (profile.getCollection().get(i).intValue() == cardId)
				n++;
		}
		return n;
	}

	private static void clear(StorySave save, Region region) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < region.duels.length; i++) {
			if (i > 0)
				builder.append(',');
			builder.append(region.duels[i].id);
		}
		save.setClearedCsv(builder.toString());
	}

	private static int playerPoints(Region region, StorySave save) {
		Row[] rows = StoryCampaign.table(region, save);
		for (int i = 0; i < rows.length; i++) {
			if (rows[i].player)
				return rows[i].points;
		}
		return -1;
	}

	/** Catalog id and level columns from cards/deck.txt. */
	private static int[][] readCatalog() throws Exception {
		File file = Assets.file("deck.txt");
		int[] ids = new int[256];
		int[] levels = new int[256];
		int n = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\\t");
				if (tokens.length != 5)
					continue;
				ids[n] = Integer.parseInt(tokens[0]);
				levels[n] = Integer.parseInt(tokens[4]);
				n++;
			}
		}
		int[] idOut = new int[n];
		int[] levelOut = new int[n];
		System.arraycopy(ids, 0, idOut, 0, n);
		System.arraycopy(levels, 0, levelOut, 0, n);
		return new int[][] { idOut, levelOut };
	}
}
