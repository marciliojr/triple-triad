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

/**
 * UI strings for Portuguese (Brazil), English and Spanish.
 */
public final class I18n {
	// This class should not be instantiated.
	private I18n() {}

	public static String menuQuick() {
		return pick("Jogo R\u00e1pido", "Quick Game", "Partida r\u00e1pida");
	}

	public static String menuHowTo() {
		return pick("Como - Jogar", "How to Play", "C\u00f3mo jugar");
	}

	public static String menuSettings() {
		return pick("Configura\u00e7\u00f5es", "Settings", "Ajustes");
	}

	public static String menuChampionship() {
		return pick("Campeonato", "Championship", "Campeonato");
	}

	public static String menuMyDeck() {
		return pick("Meu Deck", "My Deck", "Mi mazo");
	}

	public static String championshipHint() {
		return pick("Novo jogo usa Meu Deck. Se estiver vazio, a 1\u00aa partida cria o pack.",
			"New game uses My Deck. If it is empty, match 1 creates the pack.",
			"Nuevo juego usa Mi mazo. Si est\u00e1 vac\u00edo, la 1\u00aa partida crea el sobre.");
	}

	public static String championshipCards(int n) {
		return pick(n + " cartas", n + " cards", n + " cartas");
	}

	public static String championshipCups(int n) {
		return pick(n + " campeonatos vencidos", n + " championships won", n + " campeonatos ganados");
	}

	public static String championshipNew() {
		return pick("Novo", "New", "Nuevo");
	}

	public static String championshipNewGame() {
		return pick("Novo jogo", "New game", "Nuevo juego");
	}

	public static String championshipContinue() {
		return pick("Continuar", "Continue", "Continuar");
	}

	public static String championshipSave() {
		return pick("Salvar", "Save", "Guardar");
	}

	public static String championshipSaved() {
		return pick("Progresso do campeonato salvo.",
			"Championship progress saved.",
			"Progreso del campeonato guardado.");
	}

	public static String championshipClearProgress() {
		return pick("Apagar progresso", "Clear progress", "Borrar progreso");
	}

	public static String championshipStart() {
		return pick("Iniciar", "Start", "Empezar");
	}

	public static String hintChampionshipLobby() {
		return pick("Cima/baixo escolhe    Z/Enter confirma    Esc voltar",
			"Up/down selects    Z/Enter confirms    Esc back",
			"Arriba/abajo elige    Z/Enter confirma    Esc volver");
	}

	public static String championshipPackTitle() {
		return pick("Seu pacote inicial", "Your starter pack", "Tu sobre inicial");
	}

	public static String championshipPackHint() {
		return pick("Cinco cartas aleat\u00f3rias. C amplia. Z/Enter inicia a luta.",
			"Five random cards. C enlarges. Z/Enter starts the match.",
			"Cinco cartas aleatorias. C amplia. Z/Enter inicia el combate.");
	}

	public static String championshipPickTitle() {
		return pick("Escolha 5 cartas", "Choose 5 cards", "Elige 5 cartas");
	}

	public static String championshipRound(int round) {
		return pick("Partida " + round, "Match " + round, "Partida " + round);
	}

	public static String championshipRoundOf(int round) {
		return pick("Partida " + round + " / 8", "Match " + round + " / 8", "Partida " + round + " / 8");
	}

	public static String hintChampionshipPick() {
		return pick("Z seleciona    X tira    C amplia    R aleat\u00f3rio    S salva    Enter jogar    Esc lobby",
			"Z selects    X removes    C enlarges    R random    S save    Enter play    Esc lobby",
			"Z selecciona    X quita    C amplia    R aleatorio    S guarda    Enter jugar    Esc lobby");
	}

	public static String championshipTradeWin() {
		return pick("Voc\u00ea venceu. Escolha uma carta do advers\u00e1rio.",
			"You won. Choose one of the opponent's cards.",
			"Ganaste. Elige una carta del rival.");
	}

	public static String championshipTradeLose() {
		return pick("A IA levou uma carta.",
			"The AI took a card.",
			"La IA se llev\u00f3 una carta.");
	}

	public static String championshipWon() {
		return pick("Campeonato vencido", "Championship won", "Campeonato ganado");
	}

	public static String championshipLost() {
		return pick("Campeonato encerrado", "Championship over", "Campeonato terminado");
	}

	public static String hintChampionshipConfirm() {
		return pick("Z/Enter continua    C amplia    Esc menu",
			"Z/Enter continues    C enlarges    Esc menu",
			"Z/Enter sigue    C amplia    Esc men\u00fa");
	}

	public static String hintChampionshipEnd() {
		return pick("Esc volta ao menu", "Esc returns to the menu", "Esc vuelve al men\u00fa");
	}

	public static String hintMenu() {
		return pick("Z/Enter seleciona    Esc sai", "Z/Enter selects    Esc quits", "Z/Enter selecciona    Esc sale");
	}

	public static String hintEscBack() {
		return pick("Cima/baixo escolhe    Z/Enter altera    Esc voltar",
			"Up/down selects    Z/Enter changes    Esc back",
			"Arriba/abajo elige    Z/Enter cambia    Esc volver");
	}

	public static String confirmLeaveMatch() {
		return pick("Sair da partida?", "Leave the match?", "\u00bfSalir de la partida?");
	}

	public static String confirmLeaveYes() {
		return pick("Sim", "Yes", "S\u00ed");
	}

	public static String confirmLeaveNo() {
		return pick("N\u00e3o", "No", "No");
	}

	public static String hintLeaveConfirm() {
		return pick("Cima/baixo escolhe    Enter confirma    Esc cancela",
			"Up/down selects    Enter confirms    Esc cancels",
			"Arriba/abajo elige    Enter confirma    Esc cancela");
	}

	public static String profilePrompt() {
		return pick("Crie um perfil para jogar", "Create a profile to play", "Crea un perfil para jugar");
	}

	public static String profileRenamePrompt() {
		return pick("Edite o nome do jogador", "Edit the player name", "Edita el nombre del jugador");
	}

	public static String profileCreatePrompt() {
		return pick("Nome do novo perfil", "New profile name", "Nombre del nuevo perfil");
	}

	public static String profileName() {
		return pick("Nome", "Name", "Nombre");
	}

	public static String hintProfile() {
		return pick("Enter confirma    Esc sai", "Enter confirms    Esc quits", "Enter confirma    Esc sale");
	}

	public static String hintProfileEdit() {
		return pick("Enter confirma    Esc voltar", "Enter confirms    Esc back", "Enter confirma    Esc volver");
	}

	public static String settingsPlayerName() {
		return pick("Nome do jogador", "Player name", "Nombre del jugador");
	}

	public static String settingsProfiles() {
		return pick("Perfis", "Profiles", "Perfiles");
	}

	public static String profilesTitle() {
		return pick("Perfis", "Profiles", "Perfiles");
	}

	public static String newProfile() {
		return pick("+  Novo perfil", "+  New profile", "+  Perfil nuevo");
	}

	public static String profileActive() {
		return pick("ativo", "active", "activo");
	}

	public static String hintProfiles() {
		return pick("Z/Enter troca    Del apaga    Esc voltar",
			"Z/Enter switches    Del deletes    Esc back",
			"Z/Enter cambia    Del borra    Esc volver");
	}

	public static String profilesCount(int n) {
		return pick(n + " perfil" + (n == 1 ? "" : "s"),
			n + " profile" + (n == 1 ? "" : "s"),
			n + " perfil" + (n == 1 ? "" : "es"));
	}

	public static String deckSelectTitle() {
		return menuQuick();
	}

	public static String deckSelectHint() {
		return pick("Escolha um deck e as regras da partida",
			"Choose a deck and the match rules",
			"Elige un mazo y las reglas de la partida");
	}

	public static String randomDeck() {
		return pick("Deck aleat\u00f3rio", "Random deck", "Mazo aleatorio");
	}

	public static String newDeck() {
		return pick("+  Novo deck", "+  New deck", "+  Mazo nuevo");
	}

	public static String myDeckEmpty() {
		return pick("Ganhe cartas no Campeonato para encher Meu Deck.",
			"Win cards in Championship to fill My Deck.",
			"Gana cartas en el Campeonato para llenar Mi mazo.");
	}

	public static String hintMyDeck() {
		return pick("C amplia    X/Del remove    Esc voltar",
			"C enlarges    X/Del removes    Esc back",
			"C amplia    X/Del quita    Esc volver");
	}

	public static String confirmRemoveAlbum(String name) {
		if (name == null)
			name = "";
		return pick("Remover " + name + " do Meu Deck?",
			"Remove " + name + " from My Deck?",
			"\u00bfQuitar " + name + " de Mi mazo?");
	}

	public static String hintMyDeckPreview() {
		return pick("Esquerda/direita    X/Del remove    Esc/C fecha",
			"Left/right    X/Del removes    Esc/C closes",
			"Izquierda/derecha    X/Del quita    Esc/C cierra");
	}

	public static String hintMyDeckPick() {
		return pick("Z seleciona    X tira    C amplia    R aleat\u00f3rio    Enter jogar    Esc voltar",
			"Z selects    X removes    C enlarges    R random    Enter play    Esc back",
			"Z selecciona    X quita    C amplia    R aleatorio    Enter jugar    Esc volver");
	}

	public static String hintCardPreview() {
		return pick("Esquerda/direita cartas    Esc/C fecha",
			"Left/right cards    Esc/C closes",
			"Izquierda/derecha cartas    Esc/C cierra");
	}

	public static String rulesTitle() {
		return pick("Regras", "Rules", "Reglas");
	}

	public static String on() {
		return pick("Ligada", "On", "Activada");
	}

	public static String off() {
		return pick("Desligada", "Off", "Desactivada");
	}

	public static String hintDeckRules() {
		return pick("Cima/baixo escolhe    Z/Enter liga/desliga    Esquerda decks    Esc voltar",
			"Up/down selects    Z/Enter toggles    Left decks    Esc back",
			"Arriba/abajo elige    Z/Enter activa/desactiva    Izquierda mazos    Esc volver");
	}

	public static String hintDeckList() {
		return pick("Z/Enter jogar    C amplia    E editar    X/Del apagar    Direita regras    Esc voltar",
			"Z/Enter play    C enlarges    E edit    X/Del delete    Right rules    Esc back",
			"Z/Enter jugar    C amplia    E editar    X/Del borrar    Derecha reglas    Esc volver");
	}

	public static String hintDeckPreview() {
		return pick("Esquerda/direita cartas    Esc/C fecha    X apaga deck",
			"Left/right cards    Esc/C closes    X deletes deck",
			"Izquierda/derecha cartas    Esc/C cierra    X borra mazo");
	}

	public static String buildDeck() {
		return pick("Montar deck", "Build deck", "Armar mazo");
	}

	public static String editDeck() {
		return pick("Editar deck", "Edit deck", "Editar mazo");
	}

	public static String cardCount(int n) {
		return pick(n + " / 5 cartas", n + " / 5 cards", n + " / 5 cartas");
	}

	public static String deckNamePrompt() {
		return pick("Nome do deck", "Deck name", "Nombre del mazo");
	}

	public static String hintNaming() {
		return pick("Enter salva    Esc cancela", "Enter saves    Esc cancels", "Enter guarda    Esc cancela");
	}

	public static String hintBuilder() {
		return pick("Z seleciona    X tira    C amplia    R aleat\u00f3rio    S salvar    Enter jogar    Esc voltar",
			"Z selects    X removes    C enlarges    R random    S save    Enter play    Esc back",
			"Z selecciona    X quita    C amplia    R aleatorio    S guardar    Enter jugar    Esc volver");
	}

	public static String randomFill() {
		return pick("R  aleat\u00f3rio", "R  random", "R  aleatorio");
	}

	public static String elementName(Element element) {
		if (element == null || element == Element.NEUTRAL)
			return "\u2014";
		switch (element) {
		case FIRE:
			return pick("Fogo", "Fire", "Fuego");
		case WATER:
			return pick("\u00c1gua", "Water", "Agua");
		case EARTH:
			return pick("Terra", "Earth", "Tierra");
		case THUNDER:
			return pick("Trov\u00e3o", "Thunder", "Trueno");
		case ICE:
			return pick("Gelo", "Ice", "Hielo");
		case WIND:
			return pick("Vento", "Wind", "Viento");
		case POISON:
			return pick("Veneno", "Poison", "Veneno");
		case HOLY:
			return pick("Sagrado", "Holy", "Sagrado");
		default:
			return "\u2014";
		}
	}

	public static String cardIdentity(Card card) {
		if (card == null)
			return "";
		return String.format("%03d  %s  \u2014  %s",
			card.getID(), card.getName(), elementName(card.getElement()));
	}

	public static String hintHowTo() {
		return pick("Cima/baixo ou roda    Esc voltar",
			"Up/down or wheel    Esc back",
			"Arriba/abajo o rueda    Esc volver");
	}

	public static String settingsTitle() {
		return menuSettings();
	}

	public static String settingsMusic() {
		return pick("M\u00fasica de fundo", "Background music", "M\u00fasica de fondo");
	}

	public static String settingsCursor() {
		return pick("Som do cursor", "Cursor sound", "Sonido del cursor");
	}

	public static String settingsLanguage() {
		return pick("Idioma", "Language", "Idioma");
	}

	public static String settingsDifficulty() {
		return pick("Dificuldade da IA", "AI difficulty", "Dificultad de la IA");
	}

	public static String difficultyEasy() {
		return pick("F\u00e1cil", "Easy", "F\u00e1cil");
	}

	public static String difficultyNormal() {
		return pick("Normal", "Normal", "Normal");
	}

	public static String difficultyHard() {
		return pick("Dif\u00edcil", "Hard", "Dif\u00edcil");
	}

	public static String languageName(Lang lang) {
		switch (lang) {
			case EN: return pick("Ingl\u00eas", "English", "Ingl\u00e9s");
			case ES: return pick("Espanhol", "Spanish", "Espa\u00f1ol");
			case PT_BR:
			default: return pick("Portugu\u00eas (Brasil)", "Portuguese (Brazil)", "Portugu\u00e9s (Brasil)");
		}
	}

	public static String ruleName(Rule rule) {
		switch (rule) {
			case OPEN:
				return pick("Aberta", "Open", "Abierta");
			case SAME:
				return "Same";
			case SAME_WALL:
				return "Same Wall";
			case PLUS:
				return "Plus";
			case COMBO:
				return "Combo";
			case ELEMENTAL:
				return "Elemental";
			case SUDDEN_DEATH:
				return pick("Morte s\u00fabita", "Sudden Death", "Muerte s\u00fabita");
			default:
				return rule.toString();
		}
	}

	/**
	 * How-to-play paragraphs. Lines starting with '#' are section headings.
	 * @return the paragraphs
	 */
	public static String[] howToParagraphs() {
		switch (Options.getLang()) {
			case EN: return HOW_TO_EN;
			case ES: return HOW_TO_ES;
			case PT_BR:
			default: return HOW_TO_PT;
		}
	}

	private static String pick(String pt, String en, String es) {
		switch (Options.getLang()) {
			case EN: return en;
			case ES: return es;
			case PT_BR:
			default: return pt;
		}
	}

	private static final String[] HOW_TO_PT = {
		"#Vis\u00e3o geral",
		"Triple Triad \u00e9 um jogo de cartas em um tabuleiro 3 x 3. Cada jogador usa cinco cartas. Uma das dez n\u00e3o entra no tabuleiro, mas conta no placar. Quem controlar mais cartas no final vence.",
		"",
		"#As cartas",
		"Cada carta tem quatro valores (cima, esquerda, direita, baixo) de 1 a 9, ou A (vale 10). Algumas t\u00eam elemento (fogo, gelo, trov\u00e3o, \u00e1gua, veneno, terra, vento, sagrado). O elemento s\u00f3 importa com a regra Elemental.",
		"",
		"#Captura b\u00e1sica",
		"Ao colocar uma carta, compare o lado que toca a carta vizinha. Se o seu n\u00famero for maior, voc\u00ea captura a carta advers\u00e1ria. Empate (8 contra 8) n\u00e3o captura. Uma jogada pode atacar v\u00e1rios lados ao mesmo tempo.",
		"",
		"#Placar",
		"A partida termina quando as nove casas est\u00e3o ocupadas. Cada um come\u00e7a com 5 pontos. A carta que ficou na m\u00e3o tamb\u00e9m conta. Empate 5 a 5: com Morte s\u00fabita come\u00e7a outra partida com as cartas da cor de cada um; sem ela, o jogo empata.",
		"",
		"#Regras especiais (ligue na tela de Jogo R\u00e1pido)",
		"Aberta: voc\u00ea v\u00ea a m\u00e3o do advers\u00e1rio. N\u00e3o muda a captura.",
		"Same: se dois ou mais lados tiverem o mesmo valor que o vizinho, essas cartas s\u00e3o capturadas (pelo menos uma precisa ser inimiga).",
		"Same Wall: as bordas do tabuleiro valem A (10) para Same. S\u00f3 funciona com Same ligada.",
		"Plus: se as somas dos lados que se tocam forem iguais em dois ou mais lados, captura essas cartas.",
		"Combo: capturas de Same ou Plus podem virar novas capturas em cadeia.",
		"Elemental: casas ganham um elemento. Carta do mesmo elemento +1 em todos os lados; elemento diferente ou sem elemento -1. Same e Plus usam os valores originais.",
		"Morte s\u00fabita: empate inicia uma nova rodada com as cartas que cada um controlava.",
		"",
		"#Jogo R\u00e1pido",
		"Partida contra a IA. Escolha Meu Deck (cinco cartas do \u00e1lbum), Deck aleat\u00f3rio, um deck novo ou um deck salvo. C amplia as cartas. As regras ficam \u00e0 direita. F5 recome\u00e7a com o mesmo deck.",
		"",
		"#Campeonato",
		"Oito partidas contra a IA. Se o \u00e1lbum tiver menos de cinco cartas, a primeira partida cria um pack inicial. Ao vencer, pegue uma carta do advers\u00e1rio. C amplia as cartas no pack, no pick e na troca. O \u00e1lbum e as copas permanecem quando a run termina.",
		"",
		"#Meu Deck e perfis",
		"Meu Deck \u00e9 o \u00e1lbum da cole\u00e7\u00e3o. C amplia; X ou Del remove uma c\u00f3pia. Em Configura\u00e7\u00f5es d\u00e1 para editar o nome, criar outros jogadores, trocar de perfil e escolher a dificuldade da IA. Cada perfil guarda o pr\u00f3prio \u00e1lbum e os decks.",
		"",
		"#Lembrete",
		"Coloque, compare, capture, domine. N\u00famero maior vence. Same procura igualdade. Plus procura somas iguais. Quem tiver mais cartas no fim ganha."
	};

	private static final String[] HOW_TO_EN = {
		"#Overview",
		"Triple Triad is a card game on a 3 x 3 board. Each player uses five cards. One of the ten stays in hand but still counts toward the score. Whoever controls more cards at the end wins.",
		"",
		"#The cards",
		"Each card has four values (top, left, right, bottom) from 1 to 9, or A (worth 10). Some have an element (fire, ice, thunder, water, poison, earth, wind, holy). The element only matters with the Elemental rule.",
		"",
		"#Basic capture",
		"When you place a card, compare the side that touches a neighbor. If your number is higher, you capture the opponent's card. A tie (8 vs 8) does not capture. One play can attack several sides at once.",
		"",
		"#Score",
		"The match ends when all nine spaces are filled. Each player starts with 5 points. The card left in hand also counts. A 5-5 draw: with Sudden Death another match starts using the cards each player controlled; without it, the game is a draw.",
		"",
		"#Special rules (toggle them on the Quick Game screen)",
		"Open: you see the opponent's hand. It does not change capture.",
		"Same: if two or more sides match the neighbor's value, those cards are captured (at least one must belong to the opponent).",
		"Same Wall: board edges count as A (10) for Same. Only works with Same on.",
		"Plus: if the sums of touching sides are equal on two or more sides, those cards are captured.",
		"Combo: Same or Plus captures can chain into further captures.",
		"Elemental: spaces gain an element. Matching element +1 on all sides; a different element or none -1. Same and Plus use the original values.",
		"Sudden Death: a draw starts a new round with the cards each player controlled.",
		"",
		"#Quick Game",
		"A match against the AI. Choose My Deck (five cards from the album), Random deck, a new deck, or a saved deck. C enlarges cards. Rules are on the right. F5 rematches with the same deck.",
		"",
		"#Championship",
		"Eight matches against the AI. If the album has fewer than five cards, match 1 creates a starter pack. After a win, take one opponent card. C enlarges cards in the pack, pick, and trade. The album and cups stay when the run ends.",
		"",
		"#My Deck and profiles",
		"My Deck is the collection album. C enlarges; X or Del removes one copy. In Settings you can edit the name, create other players, switch profiles, and set AI difficulty. Each profile keeps its own album and decks.",
		"",
		"#Reminder",
		"Place, compare, capture, dominate. Higher number wins. Same looks for equal ranks. Plus looks for equal sums. Whoever has more cards at the end wins."
	};

	private static final String[] HOW_TO_ES = {
		"#Visi\u00f3n general",
		"Triple Triad es un juego de cartas en un tablero 3 x 3. Cada jugador usa cinco cartas. Una de las diez no entra al tablero, pero cuenta en el marcador. Quien controle m\u00e1s cartas al final gana.",
		"",
		"#Las cartas",
		"Cada carta tiene cuatro valores (arriba, izquierda, derecha, abajo) de 1 a 9, o A (vale 10). Algunas tienen elemento (fuego, hielo, trueno, agua, veneno, tierra, viento, sagrado). El elemento solo importa con la regla Elemental.",
		"",
		"#Captura b\u00e1sica",
		"Al colocar una carta, compara el lado que toca a la vecina. Si tu n\u00famero es mayor, capturas la carta rival. Un empate (8 contra 8) no captura. Una jugada puede atacar varios lados a la vez.",
		"",
		"#Marcador",
		"La partida termina cuando las nueve casillas est\u00e1n ocupadas. Cada uno empieza con 5 puntos. La carta que queda en la mano tambi\u00e9n cuenta. Empate 5 a 5: con Muerte s\u00fabita empieza otra partida con las cartas del color de cada uno; sin ella, el juego empata.",
		"",
		"#Reglas especiales (act\u00edvalas en Partida r\u00e1pida)",
		"Abierta: ves la mano del rival. No cambia la captura.",
		"Same: si dos o m\u00e1s lados tienen el mismo valor que el vecino, esas cartas se capturan (al menos una debe ser enemiga).",
		"Same Wall: los bordes del tablero valen A (10) para Same. Solo funciona con Same activada.",
		"Plus: si las sumas de los lados que se tocan son iguales en dos o m\u00e1s lados, captura esas cartas.",
		"Combo: las capturas de Same o Plus pueden encadenar nuevas capturas.",
		"Elemental: las casillas ganan un elemento. Carta del mismo elemento +1 en todos los lados; elemento distinto o sin elemento -1. Same y Plus usan los valores originales.",
		"Muerte s\u00fabita: el empate inicia una nueva ronda con las cartas que cada uno controlaba.",
		"",
		"#Partida r\u00e1pida",
		"Partida contra la IA. Elige Mi mazo (cinco cartas del \u00e1lbum), Mazo aleatorio, un mazo nuevo o un mazo guardado. C amplia las cartas. Las reglas est\u00e1n a la derecha. F5 reinicia con el mismo mazo.",
		"",
		"#Campeonato",
		"Ocho partidas contra la IA. Si el \u00e1lbum tiene menos de cinco cartas, la primera partida crea un sobre inicial. Al ganar, toma una carta del rival. C amplia las cartas en el sobre, la elecci\u00f3n y el intercambio. El \u00e1lbum y las copas permanecen cuando termina la ronda.",
		"",
		"#Mi mazo y perfiles",
		"Mi mazo es el \u00e1lbum de la colecci\u00f3n. C amplia; X o Del quita una copia. En Ajustes puedes editar el nombre, crear otros jugadores, cambiar de perfil y elegir la dificultad de la IA. Cada perfil guarda su propio \u00e1lbum y mazos.",
		"",
		"#Recordatorio",
		"Coloca, compara, captura, domina. El n\u00famero mayor gana. Same busca igualdad. Plus busca sumas iguales. Quien tenga m\u00e1s cartas al final gana."
	};
}
