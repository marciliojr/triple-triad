package itdelatrisu.tripletriad.story;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.gfx.Assets;
import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/** Every image the Story Mode script asks for is in res/story/, at the expected size. */
public class StoryArtTest {
	private static final String[] NPCS = {
		"npc_cadete", "npc_soldado_galbadia", "npc_soldado_dollet",
		"npc_pescador", "npc_cientista_esthar", "npc_pub_dollet"
	};

	@Test
	public void backgroundsOfEveryStageAndScreenExist() throws Exception {
		Set<String> files = new HashSet<String>();
		for (Region region : StoryCampaign.REGIONS)
			files.add(region.background);
		files.add(StoryAssets.TITLE_BACKGROUND);
		files.add(StoryMap.BACKGROUND);
		files.add(StoryCampaign.KING_REVEAL);
		files.add(StoryCampaign.FINAL_ARENA);
		for (Line line : allLines()) {
			if (line.hasScene())
				files.add(line.scene);
		}
		for (String file : files)
			assertSize("story/backgrounds/" + file, 640, 360);
	}

	@Test
	public void finalIsPlayedInTheArena() {
		Region last = StoryCampaign.REGIONS[StoryCampaign.REGIONS.length - 1];
		assertEquals("final", last.id);
		assertEquals(StoryCampaign.FINAL_ARENA, last.background);
		assertEquals(last, StoryCampaign.regionOf(StoryCampaign.FINAL_DUEL));
	}

	@Test
	public void everySpeakerHasAPortraitFile() throws Exception {
		for (Line line : allLines()) {
			if (line.isPlayer())
				continue;
			String id = line.portrait;
			File normal = Assets.file("story/portraits/" + id + "_normal.png");
			File bare = Assets.file("story/portraits/" + id + ".png");
			assertTrue(normal.isFile() || bare.isFile(), "missing portrait " + id + " for: " + line.pt);
		}
		for (Region region : StoryCampaign.REGIONS) {
			for (Duel d : region.duels) {
				File normal = Assets.file("story/portraits/" + d.portrait + "_normal.png");
				File bare = Assets.file("story/portraits/" + d.portrait + ".png");
				assertTrue(normal.isFile() || bare.isFile(), d.portrait);
				assertSize("story/portraits/" + d.portrait + "_derrota.png", 128, 128);
			}
		}
	}

	@Test
	public void narratorUsesNiloVannPortraits() throws Exception {
		assertEquals("narrador", StoryCampaign.NARRATOR_PORTRAIT);
		assertSize("story/portraits/narrador_normal.png", 128, 128);
		assertSize("story/portraits/narrador_derrota.png", 128, 128);
		int narrated = 0;
		for (Line line : allLines()) {
			if (line.narrator) {
				assertEquals(StoryCampaign.NARRATOR_PORTRAIT, line.portrait);
				narrated++;
			}
		}
		assertTrue(narrated > 50, "narrator lines: " + narrated);
	}

	@Test
	public void npcsHaveBothFaces() throws Exception {
		for (String npc : NPCS) {
			assertSize("story/portraits/" + npc + ".png", 128, 128);
			assertSize("story/portraits/" + npc + "_derrota.png", 128, 128);
		}
	}

	@Test
	public void opponentShowsDefeatOnlyWhenBeaten() {
		for (Region region : StoryCampaign.REGIONS) {
			for (Duel d : region.duels) {
				if (d.portrait.equals(d.win.portrait))
					assertTrue(d.win.defeat, d.id + " win line should use the defeat face");
				if (d.portrait.equals(d.lose.portrait))
					assertFalse(d.lose.defeat, d.id + " lose line should keep the normal face");
				for (Line line : d.afterLose)
					assertFalse(d.portrait.equals(line.portrait) && line.defeat, d.id);
				assertFalse(d.before.defeat, d.id);
			}
		}
		Duel cadete = StoryCampaign.findDuel("cadete");
		assertEquals("npc_cadete", cadete.win.portrait);
		assertTrue(cadete.win.defeat);
	}

	@Test
	public void kingRevealPlaysOverTheStill() {
		Duel king = StoryCampaign.findDuel("king");
		assertNotNull(king);
		assertEquals("cc_king", king.portrait);
		assertTrue(king.open.length > 0);
		for (Line line : king.open)
			assertEquals(StoryCampaign.KING_REVEAL, line.scene);
		assertEquals(StoryCampaign.KING_REVEAL, king.before.scene);
		int stills = 0;
		for (Line line : allLines()) {
			if (line.hasScene())
				stills++;
		}
		assertEquals(king.open.length + 1, stills);
	}

	@Test
	public void mapHasEveryStopInsideTheArt() throws Exception {
		Set<String> seen = new HashSet<String>();
		for (int i = 0; i < StoryCampaign.REGIONS.length; i++) {
			StoryMap.Spot spot = StoryMap.spot(StoryCampaign.REGIONS[i].id);
			assertNotNull(spot, StoryCampaign.REGIONS[i].id);
			assertTrue(spot.x > 0 && spot.x < StoryMap.WIDTH && spot.y > 0 && spot.y < StoryMap.HEIGHT);
			assertTrue(seen.add(spot.x + "," + spot.y), "two stops on the same point");
			float[] p = StoryMap.toScreen(spot, 0, 0, 1280, 720);
			assertEquals(spot.x * 2f, p[0], 0.01f);
			assertEquals(spot.y * 2f, p[1], 0.01f);
			assertEquals(i, StoryMap.hit(p[0] + 1f, p[1] - 1f, 1280, 720, 12f));
		}
		StoryMap.Spot deling = StoryMap.spot("deling");
		assertEquals(94, deling.x);
		assertEquals(91, deling.y);
		float[] wide = StoryMap.toScreen(deling, 0, 0, 1920, 1200);
		assertEquals(94 * 3f, wide[0], 0.01f);
		assertEquals((1200 - 1080) / 2f + 91 * 3f, wide[1], 0.01f);
		assertEquals(-1, StoryMap.hit(5f, 5f, 1280, 720, 12f));
		assertSize("story/ui/no_bloqueado.png", 32, 32);
		assertSize("story/ui/no_disponivel.png", 32, 32);
		assertSize("story/ui/no_vencido.png", 32, 32);
	}

	private static void assertSize(String path, int w, int h) throws Exception {
		File file = Assets.file(path);
		assertTrue(file.isFile(), "missing " + path);
		BufferedImage img = ImageIO.read(file);
		assertNotNull(img, path);
		assertEquals(w, img.getWidth(), path);
		assertEquals(h, img.getHeight(), path);
	}

	private static List<Line> allLines() {
		List<Line> all = new ArrayList<Line>();
		add(all, StoryCampaign.STARTER);
		add(all, StoryCampaign.ceremony());
		for (Region region : StoryCampaign.REGIONS) {
			add(all, region.arrival);
			for (Duel d : region.duels) {
				add(all, d.bio);
				add(all, d.open);
				add(all, new Line[] { d.before, d.win, d.lose, StoryCampaign.tie(d) });
				add(all, d.afterWin);
				add(all, d.afterLose);
			}
		}
		return all;
	}

	private static void add(List<Line> out, Line[] lines) {
		if (lines == null)
			return;
		for (Line line : lines) {
			if (line != null)
				out.add(line);
		}
	}
}
