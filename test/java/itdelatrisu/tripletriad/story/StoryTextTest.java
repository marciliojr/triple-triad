package itdelatrisu.tripletriad.story;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import itdelatrisu.tripletriad.story.StoryCampaign.Duel;
import itdelatrisu.tripletriad.story.StoryCampaign.Line;
import itdelatrisu.tripletriad.story.StoryCampaign.Region;

/** Spelling and format checks for the Story Mode script in PT, EN and ES. */
public class StoryTextTest {
	/** Words that only appear here when the accent is missing (PT or ES). */
	private static final Pattern PT_MISSING = Pattern.compile(
		"\\b(nao|voce|voces|tambem|ate|estao|proxim[oa]s?|ultim[oa]s?|titulo|premio|vitorias?|posicao|"
		+ "cabeca|mao|maos|tres|salao|praca|estacao|inscricao|comissao|publico|ninguem|alem|cerimonia|"
		+ "historia|laboratorio|analitica)\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern ES_MISSING = Pattern.compile(
		"\\b(despues|todavia|facil|aqui|alla|asi|dueno|senal|sientate|llevate|quedate|pequeno|tambien|"
		+ "proxim[oa]s?|ultim[oa]s?|titulo|inscripcion|comision|publico|ademas|aun asi|perdio|"
		+ "salio|mas adelante|estan|ensenes|verguenza|ano que)\\b", Pattern.CASE_INSENSITIVE);
	/** Only {name} or {masculine|feminine} groups are allowed. */
	private static final Pattern BRACES = Pattern.compile("\\{[^{}]*\\}");

	@Test
	public void portugueseAndSpanishKeepTheirAccents() {
		List<String> bad = new ArrayList<String>();
		for (Line line : allLines()) {
			check(PT_MISSING, line.pt, "PT", bad);
			check(ES_MISSING, line.es, "ES", bad);
		}
		for (Region region : StoryCampaign.REGIONS) {
			check(PT_MISSING, region.namePt, "PT region", bad);
			for (Duel d : region.duels) {
				check(PT_MISSING, d.namePt + " " + d.rolePt + " " + d.stylePt, "PT duel", bad);
				check(ES_MISSING, d.nameEs + " " + d.roleEs + " " + d.styleEs, "ES duel", bad);
			}
		}
		assertTrue(bad.isEmpty(), bad.size() + " line(s) without accents:\n" + String.join("\n", bad));
	}

	@Test
	public void placeholdersAreWellFormedAndEnglishIsNeutral() {
		for (Line line : allLines()) {
			String[] texts = { line.pt, line.en, line.es };
			for (int i = 0; i < texts.length; i++) {
				String text = texts[i];
				assertFalse(text.isBlank(), "empty line next to: " + line.pt);
				Matcher m = BRACES.matcher(text);
				while (m.find()) {
					String g = m.group();
					boolean ok = g.equals("{name}") || g.matches("\\{[^|{}]+\\|[^|{}]+\\}");
					assertTrue(ok, "bad placeholder " + g + " in: " + text);
					if (i == 1)
						assertTrue(g.equals("{name}"), "English stays neutral: " + text);
				}
				String stripped = BRACES.matcher(text).replaceAll("");
				assertFalse(stripped.contains("{") || stripped.contains("}") || stripped.contains("|"),
					"stray brace or bar in: " + text);
			}
		}
	}

	@Test
	public void linesFitTheirBoxes() {
		// dialogue pages 3 lines at a time; dossier bios show at most 6 lines, styles 3
		for (Line line : allLines()) {
			assertTrue(line.pt.length() <= 260 && line.en.length() <= 260 && line.es.length() <= 260,
				"line too long: " + line.pt);
		}
		for (Region region : StoryCampaign.REGIONS) {
			for (Duel d : region.duels) {
				for (Line bio : d.bio)
					assertTrue(Math.max(bio.pt.length(), Math.max(bio.en.length(), bio.es.length())) <= 200,
						"bio too long for the dossier: " + bio.pt);
				assertTrue(Math.max(d.stylePt.length(), Math.max(d.styleEn.length(), d.styleEs.length())) <= 90,
					"style too long: " + d.stylePt);
			}
		}
	}

	private static void check(Pattern p, String text, String label, List<String> bad) {
		Matcher m = p.matcher(text);
		if (m.find())
			bad.add(label + " '" + m.group() + "': " + text);
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
