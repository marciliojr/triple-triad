package itdelatrisu.tripletriad.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.Profile;
import itdelatrisu.tripletriad.ProfileStore;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/** Campaign deck, rewards, migration and the level curve. */
public class StoryBalanceTest {
	@Test
	public void starterDeckIsFixedLevelOneAndTwoWithNoRares() {
		assertEquals(8, StoryBalance.STARTER.length);
		assertEquals(StoryBalance.STARTER.length, StoryBalance.STARTER_NAMES.length);
		StorySave save = StorySave.create("Ana");
		assertNotNull(save);
		assertEquals(8, save.collectionSize());
		for (int i = 0; i < StoryBalance.STARTER.length; i++) {
			assertEquals(1, save.copies(StoryBalance.STARTER[i]), "card " + StoryBalance.STARTER[i]);
			int level = StoryBalance.levelOf(StoryBalance.STARTER[i]);
			assertTrue(level == 1 || level == 2, StoryBalance.STARTER_NAMES[i]);
			assertFalse(StoryBalance.isRare(StoryBalance.STARTER[i]));
		}
	}

	@Test
	public void campaignDeckIsIndependentFromTheProfileAlbum() {
		Profile profile = new Profile();
		profile.setName("Marcilio");
		profile.addCard(85);
		StorySave save = profile.addCampaign("Ana");
		assertNotNull(save);
		assertEquals(0, save.copies(85));
		assertTrue(save.containsCard(StoryBalance.STARTER[0]));
		profile.grantStoryCards(new int[] { 30 });
		assertEquals(1, save.copies(30));
		int album30 = 0;
		for (int i = 0; i < profile.getCollection().size(); i++) {
			if (profile.getCollection().get(i).intValue() == 12)
				album30++;
		}
		assertEquals(0, album30);
		assertEquals(1, profile.getCollection().stream().filter(id -> id.intValue() == 85).count());
	}

	@Test
	public void defeatDoesNotChangeTheCollection() {
		StorySave save = StorySave.create("Ana");
		int[] before = StorySave.parseIds(save.collectionCsv());
		// a loss never calls armWin
		assertFalse(save.hasPending());
		assertArrayEquals(before, StorySave.parseIds(save.collectionCsv()));
	}

	@Test
	public void pendingBossRewardDoesNotDuplicateAfterReload() throws Exception {
		Profile profile = new Profile();
		profile.setName("Marcilio");
		StorySave save = profile.addCampaign("Ana");
		save.armWin("fujin", new int[] { 20, 21, 22, 23, 24 }, 83);
		save.advanceToPick();
		int prize = save.takeBossReward();
		assertEquals(83, prize);
		profile.grantStoryCards(new int[] { prize });
		File file = Files.createTempFile("story-award", ".profile").toFile();
		file.deleteOnExit();
		ProfileStore.writeFile(file, profile);
		Profile loaded = ProfileStore.readFile(file);
		StorySave again = loaded.activeStory();
		assertEquals(1, again.copies(83));
		assertEquals(0, again.takeBossReward());
		assertEquals(1, again.copies(83));
		int[] granted = again.confirm(20);
		assertArrayEquals(new int[] { 20 }, granted);
		loaded.grantStoryCards(granted);
		ProfileStore.writeFile(file, loaded);
		StorySave done = ProfileStore.readFile(file).activeStory();
		assertEquals(1, done.copies(83));
		assertEquals(1, done.copies(20));
		assertFalse(done.hasPending());
	}

	@Test
	public void format3SaveGetsStarterPlusProgressCards() throws Exception {
		Profile profile = new Profile();
		profile.setName("Marcilio");
		StorySave raw = profile.addCampaign("Ana");
		raw.setClearedCsv("cadete,spade,joker,club,heart,king");
		raw.clearCollection();
		raw.setCollectionReady(false);
		File file = Files.createTempFile("story-v3", ".profile").toFile();
		file.deleteOnExit();
		ProfileStore.writeFile(file, profile);
		StringBuilder old = new StringBuilder();
		for (String line : Files.readAllLines(file.toPath())) {
			if (line.startsWith("CAMP.1.COLLECTION") || line.startsWith("CAMP.1.READY")
					|| line.startsWith("CAMP.1.SEED") || line.startsWith("CAMP.1.SEEN")
					|| line.startsWith("CAMP.1.GIVEN"))
				continue;
			old.append(line.startsWith("STORY_FORMAT") ? "STORY_FORMAT = 3" : line).append('\n');
		}
		Files.write(file.toPath(), old.toString().getBytes("UTF-8"));

		Profile loaded = ProfileStore.readFile(file);
		StorySave save = loaded.activeStory();
		assertTrue(save.isCollectionReady());
		for (int id : StoryBalance.STARTER)
			assertTrue(save.containsCard(id), "starter " + id);
		assertTrue(save.containsCard(85), "Ifrit from the cleared Garden boss");
		assertTrue(save.collectionSize() > StoryBalance.STARTER.length);
		assertEquals(0, loaded.getCollection().size());
	}

	@Test
	public void specialsStayAtOrBelowTheNextBossLevel() {
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			int flagged = 0;
			for (int d = 0; d < region.duels.length; d++) {
				Duel duel = region.duels[d];
				if (duel.isBoss())
					continue;
				int cap = StoryBalance.specialCap(duel);
				Duel next = StoryBalance.nextBoss(duel);
				if (next == null)
					continue;
				assertEquals(next.maxLevel, cap, duel.id);
				assertTrue(cap <= next.maxLevel, duel.id);
				StorySave probe = StorySave.create("Probe");
				boolean canCarry = false;
				for (long seed = 1; seed <= 400 && !canCarry; seed++) {
					probe.setSeed(seed);
					if (StoryBalance.carriesSpecial(probe, duel))
						canCarry = true;
				}
				if (canCarry)
					flagged++;
				for (long seed = 1; seed <= 80; seed++) {
					probe.setSeed(seed);
					int[] hand = StoryBalance.opponentHand(probe, duel);
					assertEquals(5, hand.length, duel.id);
					for (int i = 0; i < hand.length; i++) {
						if (StoryBalance.isRare(hand[i]) || StoryBalance.levelOf(hand[i]) > duel.maxLevel)
							assertTrue(StoryBalance.levelOf(hand[i]) <= cap, duel.id + " card " + hand[i]);
					}
				}
			}
			int commons = 0;
			for (int d = 0; d < region.duels.length; d++) {
				if (!region.duels[d].isBoss())
					commons++;
			}
			if (commons > 0)
				assertTrue(flagged <= commons, region.id);
		}
	}

	@Test
	public void atMostOneSpecialCarrierPerRegion() {
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			for (long seed = 1; seed <= 200; seed++) {
				StorySave probe = StorySave.create("Probe");
				probe.setSeed(seed);
				int hits = 0;
				for (int d = 0; d < region.duels.length; d++) {
					if (StoryBalance.carriesSpecial(probe, region.duels[d]))
						hits++;
				}
				assertTrue(hits <= StoryBalance.MAX_SPECIALS_PER_REGION, region.id + " seed " + seed);
			}
		}
	}

	@Test
	public void expectedPlayerDeckIsComparableToOpponentsOfThatStage() {
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			int player = StoryBalance.expectedPlayerPower(r);
			assertTrue(player > 0, "region " + r);
			Region region = StoryCampaign.REGIONS[r];
			int minOpp = Integer.MAX_VALUE;
			int maxOpp = 0;
			for (int d = 0; d < region.duels.length; d++) {
				int opp = StoryBalance.typicalOpponentPower(region.duels[d]);
				minOpp = Math.min(minOpp, opp);
				maxOpp = Math.max(maxOpp, opp);
			}
			// within a generous band: not helpless, not a guaranteed wipe
			assertTrue(player * 100 >= minOpp * 70, region.id + " player=" + player + " minOpp=" + minOpp);
			assertTrue(player * 100 <= maxOpp * 145, region.id + " player=" + player + " maxOpp=" + maxOpp);
		}
	}

	@Test
	public void levelCurveRisesAcrossTheTour() {
		int lastBossMax = 0;
		for (int r = 0; r < StoryCampaign.REGIONS.length; r++) {
			Region region = StoryCampaign.REGIONS[r];
			int commonMax = 0;
			int bossMax = 0;
			for (int d = 0; d < region.duels.length; d++) {
				Duel duel = region.duels[d];
				if (duel.isBoss())
					bossMax = Math.max(bossMax, duel.maxLevel);
				else
					commonMax = Math.max(commonMax, duel.maxLevel);
			}
			assertTrue(bossMax >= commonMax, region.id);
			assertTrue(bossMax >= lastBossMax, region.id);
			lastBossMax = bossMax;
		}
		assertTrue(lastBossMax >= 8);
	}
}
