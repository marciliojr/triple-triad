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

import java.io.InputStream;
import java.util.Properties;

/**
 * Text and version for the About screen: fork credits, additions, license and notice.
 * Paragraphs starting with '#' are headings; an empty string is a blank line.
 */
public final class About {
	private About() {}

	/** Original project. */
	public static final String ORIGINAL_AUTHOR = "Jeffrey Han";
	public static final String ORIGINAL_HANDLE = "itdelatrisu";
	public static final String ORIGINAL_URL = "github.com/itdelatrisu/triple-triad";

	/** This fork. */
	public static final String FORK_AUTHOR = "Marcilio Junior";
	public static final String FORK_HANDLE = "marciliojr";
	public static final String FORK_URL = "github.com/marciliojr/triple-triad";

	/** Cached version string. */
	private static String version;

	/**
	 * Version from {@code version.properties}, filled in by Gradle from {@code gradle.properties}.
	 * @return the version, or "dev" when the resource is missing (e.g. an IDE run without Gradle)
	 */
	public static synchronized String version() {
		if (version != null)
			return version;
		String v = null;
		try (InputStream in = About.class.getResourceAsStream("/version.properties")) {
			if (in != null) {
				Properties p = new Properties();
				p.load(in);
				v = p.getProperty("version");
			}
		} catch (Exception e) {
			v = null;
		}
		if (v == null || v.isBlank() || v.contains("$"))
			v = "dev";
		version = v.trim();
		return version;
	}

	/**
	 * @param lang the language
	 * @return the About paragraphs in that language
	 */
	public static String[] paragraphs(Lang lang) {
		String v = version();
		switch (lang) {
		case EN:
			return new String[] {
				"#Triple Triad " + v,
				"A fork of Jeffrey Han's Triple Triad, developed and greatly expanded by Marcilio Junior.",
				"",
				"#Original project",
				"Triple Triad, the card game from Final Fantasy VIII, was created as an open-source project by "
					+ ORIGINAL_AUTHOR + " (" + ORIGINAL_HANDLE + ") in 2014: " + ORIGINAL_URL
					+ ". The original release (0.1.1) offered a match against the AI with the classic rules, in Java 7 and Slick2D.",
				"",
				"#This fork",
				"This project is a fork of that original. It was developed and expanded by " + FORK_AUTHOR
					+ " (" + FORK_HANDLE + "): " + FORK_URL + ". The capture rules and the AI are still the original ones.",
				"",
				"#What this fork added",
				"- Modern client: Java 21, Gradle and libGDX (LWJGL 3)",
				"- Profiles with avatars, several profiles, memory-card style profile select",
				"- FFVIII-style main menu, Quick Game, deck builder and My Deck (card album)",
				"- 8-round Championship with a PS1 Memory Card style save (15 blocks)",
				"- Original Story Mode: the World Triple Triad Tournament, with a male or female Balamb student, Squall as mentor and announcer Nilo Vann",
				"- Local 2-player Versus, Trophies and Cheats",
				"- Interface in Portuguese, English and Spanish, and AI difficulty",
				"- Gamepad with every button remappable and a PS1-style virtual keyboard",
				"- Save export/import (.ttsave), opening and ending videos",
				"",
				"#License",
				"GNU General Public License, version 3 (GPLv3), the same as the original project. The full text is in the LICENSE file.",
				"",
				"#Notice",
				"Fan project, non-commercial. Final Fantasy VIII and Triple Triad are trademarks and property of Square Enix. This project is not affiliated with Square Enix.",
				"Card art: MCINDUS's FFVIII mods (Tripod, SeeD Reborn). Sprites: UltimeciaFFB. Sound effects: TekkamanChronos."
			};
		case ES:
			return new String[] {
				"#Triple Triad " + v,
				"Un fork del Triple Triad de Jeffrey Han, desarrollado y ampliado por Marcilio Junior.",
				"",
				"#Proyecto original",
				"Triple Triad, el juego de cartas de Final Fantasy VIII, fue creado como proyecto de código abierto por "
					+ ORIGINAL_AUTHOR + " (" + ORIGINAL_HANDLE + ") en 2014: " + ORIGINAL_URL
					+ ". La versión original (0.1.1) ofrecía la partida contra la IA con las reglas clásicas, en Java 7 y Slick2D.",
				"",
				"#Este fork",
				"Este proyecto es un fork de ese original. Fue desarrollado y ampliado por " + FORK_AUTHOR
					+ " (" + FORK_HANDLE + "): " + FORK_URL + ". Las reglas de captura y la IA siguen siendo las del original.",
				"",
				"#Lo que añadió este fork",
				"- Cliente moderno: Java 21, Gradle y libGDX (LWJGL 3)",
				"- Perfiles con avatar, varios perfiles y selección estilo memory card",
				"- Menú principal estilo FFVIII, Partida rápida, constructor de mazos y Mi mazo (álbum de cartas)",
				"- Campeonato de 8 rondas con guardado estilo Memory Card de PS1 (15 bloques)",
				"- Modo Historia original: el Torneo Mundial de Triple Triad, con un alumno o una alumna de Balamb, Squall como mentor y el locutor Nilo Vann",
				"- Versus local para 2 jugadores, Trofeos y Trucos",
				"- Interfaz en portugués, inglés y español, y dificultad de la IA",
				"- Mando con todos los botones personalizables y teclado virtual estilo PS1",
				"- Exportar/importar partidas (.ttsave), vídeos de apertura y final",
				"",
				"#Licencia",
				"GNU General Public License, versión 3 (GPLv3), la misma del proyecto original. El texto completo está en el archivo LICENSE.",
				"",
				"#Aviso",
				"Proyecto de fans, sin fines comerciales. Final Fantasy VIII y Triple Triad son marcas y propiedad de Square Enix. Este proyecto no está afiliado a Square Enix.",
				"Arte de las cartas: mods de FFVIII de MCINDUS (Tripod, SeeD Reborn). Sprites: UltimeciaFFB. Efectos de sonido: TekkamanChronos."
			};
		case PT_BR:
		default:
			return new String[] {
				"#Triple Triad " + v,
				"Um fork do Triple Triad de Jeffrey Han, desenvolvido e ampliado por Marcilio Junior.",
				"",
				"#Projeto original",
				"Triple Triad, o jogo de cartas de Final Fantasy VIII, foi criado como projeto de código aberto por "
					+ ORIGINAL_AUTHOR + " (" + ORIGINAL_HANDLE + ") em 2014: " + ORIGINAL_URL
					+ ". A versão original (0.1.1) trazia a partida contra a IA com as regras clássicas, em Java 7 e Slick2D.",
				"",
				"#Este fork",
				"Este projeto é um fork desse original. Foi desenvolvido e ampliado por " + FORK_AUTHOR
					+ " (" + FORK_HANDLE + "): " + FORK_URL + ". As regras de captura e as IAs continuam as do original.",
				"",
				"#O que este fork acrescentou",
				"- Cliente moderno: Java 21, Gradle e libGDX (LWJGL 3)",
				"- Perfis com avatar, vários perfis e seleção em blocos estilo memory card",
				"- Menu principal no estilo FFVIII, Jogo Rápido, construtor de decks e Meu Deck (álbum de cartas)",
				"- Campeonato de 8 rodadas com save estilo Memory Card do PS1 (15 blocos)",
				"- Modo História original: o Torneio Mundial de Triple Triad, com aluno ou aluna de Balamb, Squall como mentor e o locutor Nilo Vann",
				"- Versus local para 2 jogadores, Troféus e Cheats",
				"- Interface em português, inglês e espanhol, e dificuldade da IA",
				"- Joystick com todos os botões personalizáveis e teclado virtual estilo PS1",
				"- Exportar/importar save (.ttsave), vídeos de abertura e final",
				"",
				"#Licença",
				"GNU General Public License, versão 3 (GPLv3), a mesma do projeto original. O texto completo está no arquivo LICENSE.",
				"",
				"#Aviso",
				"Projeto de fã, sem fins comerciais. Final Fantasy VIII e Triple Triad são marcas e propriedade da Square Enix. Este projeto não tem ligação com a Square Enix.",
				"Arte das cartas: mods de FFVIII do MCINDUS (Tripod, SeeD Reborn). Sprites: UltimeciaFFB. Efeitos sonoros: TekkamanChronos."
			};
		}
	}
}
