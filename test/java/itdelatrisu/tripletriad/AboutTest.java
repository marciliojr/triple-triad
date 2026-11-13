package itdelatrisu.tripletriad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class AboutTest {
	@Test
	public void versionComesFromGradleProperties() throws Exception {
		Properties gradle = new Properties();
		try (FileInputStream in = new FileInputStream("gradle.properties")) {
			gradle.load(in);
		}
		assertEquals(gradle.getProperty("version"), About.version());
	}

	@Test
	public void everyLanguageCreditsBothAuthorsLicenseAndSquareEnix() {
		int size = -1;
		for (Lang lang : Lang.values()) {
			String[] paras = About.paragraphs(lang);
			String all = String.join("\n", paras);
			assertTrue(all.contains("Jeffrey Han"), lang.name());
			assertTrue(all.contains("github.com/itdelatrisu/triple-triad"), lang.name());
			assertTrue(all.contains("Marcilio Junior"), lang.name());
			assertTrue(all.contains("github.com/marciliojr/triple-triad"), lang.name());
			assertTrue(all.contains("fork"), lang.name());
			assertTrue(all.contains("GPLv3"), lang.name());
			assertTrue(all.contains("Square Enix"), lang.name());
			assertTrue(all.contains(About.version()), lang.name());
			if (size < 0)
				size = paras.length;
			assertEquals(size, paras.length, "same layout in every language: " + lang.name());
		}
	}
}
