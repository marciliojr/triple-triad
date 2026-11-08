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

	public static String menuVersus() {
		return pick("Versus", "Versus", "Versus");
	}

	public static String menuTrophies() {
		return pick("Trof\u00e9us", "Trophies", "Trofeos");
	}

	// --- remove all cards
	public static String removeAllTitle() {
		return pick("Remover todas as cartas", "Remove all cards", "Quitar todas las cartas");
	}

	public static String removeAllInfo() {
		return pick("Meu Deck", "My Deck", "Mi mazo");
	}

	public static String removeAllHelp() {
		return pick("Escolha o que remover do Meu Deck", "Choose what to remove from My Deck", "Elige qu\u00e9 quitar de Mi mazo");
	}

	public static String removeCategory(int i) {
		switch (i) {
			case 1: return pick("Monstros (n\u00edveis 1-5)", "Monsters (levels 1-5)", "Monstruos (niveles 1-5)");
			case 2: return pick("Chefes (n\u00edveis 6-7)", "Bosses (levels 6-7)", "Jefes (niveles 6-7)");
			case 3: return pick("Guardian Forces (n\u00edveis 8-9)", "Guardian Forces (levels 8-9)", "Guardian Forces (niveles 8-9)");
			case 4: return pick("Personagens (n\u00edvel 10)", "Characters (level 10)", "Personajes (nivel 10)");
			default: return pick("Todas as cartas", "All cards", "Todas las cartas");
		}
	}

	public static String removeAllConfirm(String category, int n) {
		return pick("Remover " + n + " cartas: " + category + "?",
			"Remove " + n + " cards: " + category + "?",
			"\u00bfQuitar " + n + " cartas: " + category + "?");
	}

	public static String removeAllDone(int n) {
		return pick(n + " cartas removidas.", n + " cards removed.", n + " cartas quitadas.");
	}

	public static String hintRemoveAll() {
		return pick("Cima/baixo escolhe    Z/Enter remove    Esc volta",
			"Up/down selects    Z/Enter removes    Esc back",
			"Arriba/abajo elige    Z/Enter quita    Esc volver");
	}

	// --- profiles
	public static String profileAvatar() {
		return pick("Escolha o rosto", "Choose a face", "Elige un rostro");
	}

	public static String profileSelectTitle() {
		return pick("Perfil", "Profile", "Perfil");
	}

	public static String profileSelectHelp() {
		return pick("Escolha o perfil para jogar", "Choose the profile to play", "Elige el perfil para jugar");
	}

	public static String profileSelectNew() {
		return pick("Criar perfil novo", "Create new profile", "Crear perfil nuevo");
	}

	public static String profileSelectActive() {
		return pick("\u00daltimo usado", "Last used", "\u00daltimo usado");
	}

	public static String hintProfileSelect() {
		return pick("Cima/baixo escolhe    Esq/Dir p\u00e1gina    Z/Enter confirma    Esc sai",
			"Up/down selects    Left/Right page    Z/Enter confirms    Esc quits",
			"Arriba/abajo elige    Izq/Der p\u00e1gina    Z/Enter confirma    Esc sale");
	}

	// --- trophies
	public static String trophiesHelp() {
		return pick("Contadores deste perfil (Versus n\u00e3o conta)", "This profile's counters (Versus does not count)", "Contadores de este perfil (Versus no cuenta)");
	}

	public static String trophyCups() {
		return pick("Campeonatos vencidos", "Championships won", "Campeonatos ganados");
	}

	public static String trophyChampMatches() {
		return pick("Partidas do campeonato (V / D)", "Championship matches (W / L)", "Partidas del campeonato (V / D)");
	}

	public static String trophyQuickWins() {
		return pick("Jogo R\u00e1pido: vit\u00f3rias", "Quick Game: wins", "Partida r\u00e1pida: victorias");
	}

	public static String trophyQuickLosses() {
		return pick("Jogo R\u00e1pido: derrotas", "Quick Game: losses", "Partida r\u00e1pida: derrotas");
	}

	public static String trophyQuickDraws() {
		return pick("Jogo R\u00e1pido: empates", "Quick Game: draws", "Partida r\u00e1pida: empates");
	}

	public static String trophyQuickPlayed() {
		return pick("Jogo R\u00e1pido: jogadas", "Quick Game: played", "Partida r\u00e1pida: jugadas");
	}

	public static String trophyCards() {
		return pick("Cartas no Meu Deck", "Cards in My Deck", "Cartas en Mi mazo");
	}

	public static String trophyQuickShort(int n) {
		return pick(n + " vit\u00f3rias r\u00e1pidas", n + " quick wins", n + " victorias r\u00e1pidas");
	}

	// --- versus
	public static String versusPlayerA() {
		return pick("Jogador A", "Player A", "Jugador A");
	}

	public static String versusPlayerB() {
		return pick("Jogador B", "Player B", "Jugador B");
	}

	public static String versusChoose(int n) {
		return pick("escolha 5 cartas (" + n + "/5)", "choose 5 cards (" + n + "/5)", "elige 5 cartas (" + n + "/5)");
	}

	public static String versusHelp() {
		return pick("2 jogadores, regra Open. Nada \u00e9 ganho, perdido ou salvo.",
			"2 players, Open rule. Nothing is won, lost or saved.",
			"2 jugadores, regla Open. Nada se gana, pierde ni guarda.");
	}

	public static String versusNoCards() {
		return pick("Meu Deck est\u00e1 vazio.", "My Deck is empty.", "Mi mazo est\u00e1 vac\u00edo.");
	}

	public static String hintVersusPick() {
		return pick("Z adiciona    X tira    R aleat\u00f3rio    Enter confirma    Esc menu",
			"Z adds    X removes    R random    Enter confirms    Esc menu",
			"Z a\u00f1ade    X quita    R aleatorio    Enter confirma    Esc men\u00fa");
	}

	public static String versusTurn(boolean playerA) {
		return pick("Vez do " + (playerA ? "Jogador A" : "Jogador B"),
			(playerA ? "Player A" : "Player B") + "'s turn",
			"Turno del " + (playerA ? "Jugador A" : "Jugador B"));
	}

	public static String versusWinner(boolean playerA) {
		return pick((playerA ? "Jogador A" : "Jogador B") + " venceu!",
			(playerA ? "Player A" : "Player B") + " wins!",
			"\u00a1" + (playerA ? "Jugador A" : "Jugador B") + " gana!");
	}

	public static String versusDraw() {
		return pick("Empate!", "Draw!", "\u00a1Empate!");
	}

	public static String versusBackToMenu() {
		return pick("Z/Enter volta ao menu", "Z/Enter returns to the menu", "Z/Enter vuelve al men\u00fa");
	}

	// --- controls
	public static String settingsControls() {
		return pick("Controles", "Controls", "Controles");
	}

	public static String controllersCount(int n) {
		return pick(n + " joystick(s) conectado(s)", n + " joystick(s) connected", n + " joystick(s) conectado(s)");
	}

	public static String controlsHelp() {
		return pick("Z/Enter: tecla    Esq/Dir: coluna teclado/joystick", "Z/Enter: set    Left/Right: keyboard/joystick column", "Z/Enter: asignar    Izq/Der: columna teclado/joystick");
	}

	public static String controlsPress(boolean pad) {
		return pad ? pick("Pressione o bot\u00e3o do joystick...", "Press the joystick button...", "Pulsa el bot\u00f3n del joystick...")
			: pick("Pressione a tecla... (Esc cancela)", "Press the key... (Esc cancels)", "Pulsa la tecla... (Esc cancela)");
	}

	public static String controlsReset() {
		return pick("Restaurar padr\u00e3o", "Restore defaults", "Restaurar valores");
	}

	public static String controlAction(Controls.Action a) {
		switch (a) {
			case CONFIRM: return pick("Confirmar", "Confirm", "Confirmar");
			case CANCEL: return pick("Cancelar", "Cancel", "Cancelar");
			case MENU: return pick("Menu / Iniciar", "Menu / Start", "Men\u00fa / Iniciar");
			case DELETE: return pick("Apagar", "Delete", "Borrar");
			case SKIP: return pick("Pular", "Skip", "Saltar");
			case PAGE_LEFT: return pick("P\u00e1gina esquerda", "Page left", "P\u00e1gina izquierda");
			default: return pick("P\u00e1gina direita", "Page right", "P\u00e1gina derecha");
		}
	}

	public static String hintControls() {
		return pick("Cima/baixo escolhe    Z/Enter remapeia    Esc volta",
			"Up/down selects    Z/Enter remaps    Esc back",
			"Arriba/abajo elige    Z/Enter reasigna    Esc volver");
	}

	public static String menuCheats() {
		return pick("Cheats", "Cheats", "Trucos");
	}

	public static String cheatsInfo() {
		return pick("Digite um c\u00f3digo", "Type a code", "Escribe un c\u00f3digo");
	}

	public static String cheatsHelpTag() {
		switch (Options.getLang()) {
			case EN: return "HELP";
			case ES: return "AYUDA";
			default: return "AJUDA";
		}
	}

	public static String cheatsHelp() {
		return pick("As cartas liberadas v\u00e3o para o Meu Deck",
			"Unlocked cards go to My Deck",
			"Las cartas desbloqueadas van a Mi mazo");
	}

	public static String cheatsInvalid() {
		return pick("C\u00f3digo inv\u00e1lido", "Invalid code", "C\u00f3digo inv\u00e1lido");
	}

	public static String cheatsUnlocked(int n) {
		if (n == 0)
			return pick("C\u00f3digo aceito: voc\u00ea j\u00e1 tinha todas essas cartas",
				"Code accepted: you already had all those cards",
				"C\u00f3digo aceptado: ya ten\u00edas todas esas cartas");
		return pick("C\u00f3digo aceito: " + n + " cartas liberadas",
			"Code accepted: " + n + " cards unlocked",
			"C\u00f3digo aceptado: " + n + " cartas desbloqueadas");
	}

	public static String hintCheats() {
		return pick("Enter confirma    Backspace apaga    Esc volta",
			"Enter confirms    Backspace deletes    Esc back",
			"Enter confirma    Backspace borra    Esc volver");
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

	public static String menuSave() {
		return pick("Salvar", "Save", "Guardar");
	}

	public static String saveTitle() {
		return menuSave();
	}

	public static String saveKind() {
		return pick("Tipo", "Type", "Tipo");
	}

	public static String saveKindDeck() {
		return pick("Deck", "Deck", "Mazo");
	}

	public static String saveKindChampionship() {
		return menuChampionship();
	}

	public static String saveExport() {
		return pick("Exportar", "Export", "Exportar");
	}

	public static String saveImport() {
		return pick("Importar", "Import", "Importar");
	}

	public static String saveNoChampionship() {
		return pick("N\u00e3o h\u00e1 save de campeonato.",
			"There is no championship save.",
			"No hay partida de campeonato guardada.");
	}

	public static String saveNone() {
		return pick("Sem save", "No save", "Sin partida");
	}

	public static String saveBadFile() {
		return pick("Arquivo inv\u00e1lido.", "Invalid file.", "Archivo no v\u00e1lido.");
	}

	public static String saveWrongKind() {
		return pick("Este arquivo n\u00e3o \u00e9 deste tipo.",
			"This file is not that type of save.",
			"Este archivo no es de este tipo.");
	}

	public static String saveExported() {
		return pick("Arquivo exportado.", "File exported.", "Archivo exportado.");
	}

	public static String saveImported() {
		return pick("Save importado.", "Save imported.", "Partida importada.");
	}

	public static String saveConfirmReplaceDeck() {
		return pick("Substituir o \u00e1lbum?",
			"Replace the album?",
			"\u00bfSustituir el \u00e1lbum?");
	}

	public static String saveConfirmReplaceChampionship() {
		return pick("Substituir o save do campeonato?",
			"Replace the championship save?",
			"\u00bfSustituir la partida del campeonato?");
	}

	public static String saveDialogExport() {
		return pick("Exportar save", "Export save", "Exportar partida");
	}

	public static String saveDialogImport() {
		return pick("Importar save", "Import save", "Importar partida");
	}

	public static String saveFileDeck() {
		return "meu-deck.ttsave";
	}

	public static String saveFileChampionship() {
		return "campeonato.ttsave";
	}

	public static String hintSave() {
		return pick("Cima/baixo escolhe    Esquerda/direita tipo    Z/Enter    Esc voltar",
			"Up/down selects    Left/right type    Z/Enter    Esc back",
			"Arriba/abajo elige    Izquierda/derecha tipo    Z/Enter    Esc volver");
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
		return pick("Z seleciona    X tira    C amplia    R aleat\u00f3rio    T ordena    S salva    Enter jogar    Esc lobby",
			"Z selects    X removes    C enlarges    R random    T sorts    S save    Enter play    Esc lobby",
			"Z selecciona    X quita    C amplia    R aleatorio    T ordena    S guarda    Enter jugar    Esc lobby");
	}

	public static String championshipSkipCard() {
		return pick("N\u00e3o pegar nenhuma carta", "Don't take any card", "No tomar ninguna carta");
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

	public static String hintEndgameVideo() {
		return pick("Campeonato vencido!    Qualquer tecla continua", "Championship won!    Press any key to continue", "\u00a1Campeonato ganado!    Cualquier tecla contin\u00faa");
	}

	public static String hintSkipIntro() {
		return pick("S para pular", "S to skip", "S para saltar");
	}

	public static String memoryCardInUse() {
		return pick("MEMORY CARD em uso: Slot 1", "MEMORY CARD in use: Slot 1", "MEMORY CARD en uso: Ranura 1");
	}

	public static String memoryCardSaveTitle() {
		return pick("Salvar", "Save", "Guardar");
	}

	public static String memoryCardLoadTitle() {
		return pick("Carregar", "Load", "Cargar");
	}

	public static String memoryCardHelp(boolean save) {
		return save
			? pick("Escolha o bloco para salvar", "Choose block to save", "Elige el bloque para guardar")
			: pick("Escolha o bloco para carregar", "Choose block to load", "Elige el bloque para cargar");
	}

	public static String memoryCardFree() {
		return pick("Livre", "Free", "Libre");
	}

	public static String memoryCardEmpty() {
		return pick("Vazio", "Empty", "Vac\u00edo");
	}

	public static String memoryCardTitle() {
		return pick("Campeonato", "Championship", "Campeonato");
	}

	public static String memoryCardLevel(int wins) {
		return pick("Nv " + wins, "LV " + wins, "Nv " + wins);
	}

	public static String memoryCardRound(int round) {
		return pick("RODADA " + round + "/8", "ROUND " + round + "/8", "RONDA " + round + "/8");
	}

	public static String memoryCardPlay(String time) {
		return pick("TEMPO " + time, "PLAY " + time, "TIEMPO " + time);
	}

	public static String memoryCardCards(int n) {
		return pick(n + " cartas", n + " cards", n + " cartas");
	}

	public static String memoryCardUsed(int n) {
		return pick(n + " de 15 blocos usados", n + " of 15 blocks used", n + " de 15 bloques usados");
	}

	public static String memoryCardFull() {
		return pick("Memory Card cheio: apague um bloco.", "Memory Card full: delete a block.", "Memory Card lleno: borra un bloque.");
	}

	public static String memoryCardOverwrite(int block) {
		return pick(String.format("Sobrescrever o bloco %02d?", block),
			String.format("Overwrite block %02d?", block),
			String.format("\u00bfSobrescribir el bloque %02d?", block));
	}

	public static String memoryCardDelete(int block) {
		return pick(String.format("Apagar o bloco %02d?", block),
			String.format("Delete block %02d?", block),
			String.format("\u00bfBorrar el bloque %02d?", block));
	}

	public static String memoryCardSaved(int block) {
		return pick(String.format("Salvo no bloco %02d.", block),
			String.format("Saved to block %02d.", block),
			String.format("Guardado en el bloque %02d.", block));
	}

	public static String memoryCardDeleted(int block) {
		return pick(String.format("Bloco %02d apagado.", block),
			String.format("Block %02d deleted.", block),
			String.format("Bloque %02d borrado.", block));
	}

	public static String hintMemoryCard(boolean save) {
		return save
			? pick("Z/Enter salva    D apaga    Esq/Dir p\u00e1gina    Esc volta",
				"Z/Enter saves    D deletes    Left/Right page    Esc back",
				"Z/Enter guarda    D borra    Izq/Der p\u00e1gina    Esc volver")
			: pick("Z/Enter carrega    D apaga    Esq/Dir p\u00e1gina    Esc volta",
				"Z/Enter loads    D deletes    Left/Right page    Esc back",
				"Z/Enter carga    D borra    Izq/Der p\u00e1gina    Esc volver");
	}

	/** FF8 places used as the "location" of each championship round. */
	private static final String[] ROUND_PLACES = {
		"Balamb", "Timber", "Dollet", "Deling City",
		"Fisherman's Horizon", "Esthar", "Lunar Base", "Ultimecia Castle"
	};

	public static String memoryCardPlace(int round) {
		int i = Math.max(1, Math.min(round, ROUND_PLACES.length)) - 1;
		return ROUND_PLACES[i] + " - " + championshipRound(round);
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
		return pick("Esq/Dir avatar    Enter confirma    Esc sai", "Left/Right avatar    Enter confirms    Esc quits", "Izq/Der avatar    Enter confirma    Esc sale");
	}

	public static String hintProfileEdit() {
		return pick("Esq/Dir avatar    Enter confirma    Esc voltar", "Left/Right avatar    Enter confirms    Esc back", "Izq/Der avatar    Enter confirma    Esc volver");
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
		return pick("C amplia    X/Del remove    T ordena    Esc voltar",
			"C enlarges    X/Del removes    T sorts    Esc back",
			"C amplia    X/Del quita    T ordena    Esc volver");
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
		return pick("Z seleciona    X tira    C amplia    R aleat\u00f3rio    T ordena    Enter jogar    Esc voltar",
			"Z selects    X removes    C enlarges    R random    T sorts    Enter play    Esc back",
			"Z selecciona    X quita    C amplia    R aleatorio    T ordena    Enter jugar    Esc volver");
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
		return pick("Z seleciona    X tira    C amplia    R aleat\u00f3rio    T ordena    S salvar    Enter jogar    Esc voltar",
			"Z selects    X removes    C enlarges    R random    T sorts    S save    Enter play    Esc back",
			"Z selecciona    X quita    C amplia    R aleatorio    T ordena    S guardar    Enter jugar    Esc volver");
	}

	public static String sortLine() {
		String mode;
		switch (CardSort.current()) {
			case LEVEL:
				mode = pick("N\u00edvel", "Level", "Nivel");
				break;
			case VALUE:
				mode = pick("Valor", "Value", "Valor");
				break;
			case ORDER:
			default:
				mode = pick("Cat\u00e1logo", "Order", "Cat\u00e1logo");
				break;
		}
		return pick("Ordem: ", "Sort: ", "Orden: ") + mode;
	}

	public static String randomFill() {
		return pick("R  aleat\u00f3rio", "R  random", "R  aleatorio");
	}

	public static String elementName(Element element) {
		if (element == null || element == Element.NEUTRAL)
			return "";
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
			return "";
		}
	}

	public static String cardIdentity(Card card) {
		if (card == null)
			return "";
		String ele = elementName(card.getElement());
		if (ele.isEmpty())
			return String.format("%03d  %s", card.getID(), card.getName());
		return String.format("%03d  %s  -  %s", card.getID(), card.getName(), ele);
	}

	/** Owned-copies caption under a trade card (first line). */
	public static String ownedYes() {
		return pick("J\u00e1 possui", "Owned", "La tienes");
	}

	/** Copies in Meu Deck under a trade card (second line). */
	public static String ownedCount(int copies) {
		if (copies == 1)
			return pick("1 c\u00f3pia", "1 copy", "1 copia");
		return pick(copies + " c\u00f3pias", copies + " copies", copies + " copias");
	}

	/** Caption under a trade card the player does not own yet. */
	public static String ownedNew() {
		return pick("Nova!", "New!", "\u00a1Nueva!");
	}

	/** Detail line for the card under the trade cursor. */
	public static String ownedDetail(String cardName, int copies) {
		if (copies <= 0)
			return pick("Voc\u00ea ainda n\u00e3o tem " + cardName + " no Meu Deck.",
				"You don't have " + cardName + " in My Deck yet.",
				"A\u00fan no tienes " + cardName + " en Mi mazo.");
		if (copies == 1)
			return pick("Voc\u00ea j\u00e1 tem 1 c\u00f3pia de " + cardName + " no Meu Deck.",
				"You already have 1 copy of " + cardName + " in My Deck.",
				"Ya tienes 1 copia de " + cardName + " en Mi mazo.");
		return pick("Voc\u00ea j\u00e1 tem " + copies + " c\u00f3pias de " + cardName + " no Meu Deck.",
			"You already have " + copies + " copies of " + cardName + " in My Deck.",
			"Ya tienes " + copies + " copias de " + cardName + " en Mi mazo.");
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
		"Partida contra a IA. Escolha Meu Deck (cinco cartas do \u00e1lbum), Deck aleat\u00f3rio, um deck novo ou um deck salvo. C amplia as cartas. T ordena o grid (cat\u00e1logo, n\u00edvel, valor). As regras ficam \u00e0 direita. F5 recome\u00e7a com o mesmo deck.",
		"",
		"#Campeonato",
		"Oito partidas contra a IA. Se o \u00e1lbum tiver menos de cinco cartas, a primeira partida cria um pack inicial. Ao vencer, pegue uma carta do advers\u00e1rio. C amplia as cartas no pack, no pick e na troca. T ordena o grid no pick. O \u00e1lbum e as copas permanecem quando a run termina.",
		"",
		"#Meu Deck e perfis",
		"Meu Deck \u00e9 o \u00e1lbum da cole\u00e7\u00e3o. C amplia; T ordena o grid; X ou Del remove uma c\u00f3pia. Em Configura\u00e7\u00f5es d\u00e1 para editar o nome, criar outros jogadores, trocar de perfil e escolher a dificuldade da IA. Cada perfil guarda o pr\u00f3prio \u00e1lbum e os decks.",
		"",
		"#Salvar",
		"No menu Salvar d\u00e1 para exportar ou importar o \u00e1lbum (Meu Deck) ou o checkpoint do Campeonato em um arquivo .ttsave pelo di\u00e1logo do sistema. Importar pede confirma\u00e7\u00e3o antes de substituir.",
		"",
		"#Versus",
		"Dois jogadores no mesmo computador, com a regra Open. Os dois escolhem cartas do Meu Deck do perfil atual (pode repetir cartas): primeiro o Jogador A (direita), depois o B (esquerda). Joystick 1 joga com o A; joystick 2 ou o teclado jogam com o B (sem joystick, o teclado joga com os dois). Ningu\u00e9m ganha nem perde cartas e nada \u00e9 salvo.",
		"",
		"#Trof\u00e9us",
		"Contadores do perfil: campeonatos vencidos, partidas do campeonato, vit\u00f3rias, derrotas e empates no Jogo R\u00e1pido. Versus n\u00e3o conta.",
		"",
		"#Controles e joystick",
		"Direcional e anal\u00f3gico movem o cursor em todas as telas. Em Configura\u00e7\u00f5es > Controles d\u00e1 para trocar Confirmar, Cancelar, Menu, Apagar, Pular e P\u00e1gina esquerda/direita no teclado e no joystick, ou restaurar o padr\u00e3o.",
		"",
		"#Cheats",
		"No menu Cheats digite um c\u00f3digo e aperte Enter. cid = todas as cartas | edea = Guardian Forces (n\u00edveis 8-9) | balamb = personagens (n\u00edvel 10) | monster = monstros (n\u00edveis 1-7).",
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
		"A match against the AI. Choose My Deck (five cards from the album), Random deck, a new deck, or a saved deck. C enlarges cards. T sorts the grid (catalog, level, value). Rules are on the right. F5 rematches with the same deck.",
		"",
		"#Championship",
		"Eight matches against the AI. If the album has fewer than five cards, match 1 creates a starter pack. After a win, take one opponent card. C enlarges cards in the pack, pick, and trade. T sorts the pick grid. The album and cups stay when the run ends.",
		"",
		"#My Deck and profiles",
		"My Deck is the collection album. C enlarges; T sorts the grid; X or Del removes one copy. In Settings you can edit the name, create other players, switch profiles, and set AI difficulty. Each profile keeps its own album and decks.",
		"",
		"#Save",
		"The Save menu exports or imports the album (My Deck) or the Championship checkpoint as a .ttsave file through the system dialog. Import asks for confirmation before replacing.",
		"",
		"#Versus",
		"Two players on the same computer, with the Open rule. Both pick cards from the current profile's My Deck (repeats allowed): Player A (right) first, then B (left). Joystick 1 plays as A; joystick 2 or the keyboard play as B (with no joystick, the keyboard plays both). No cards are won or lost and nothing is saved.",
		"",
		"#Trophies",
		"Profile counters: championships won, championship matches, Quick Game wins, losses and draws. Versus does not count.",
		"",
		"#Controls and joystick",
		"D-pad and stick move the cursor on every screen. Settings > Controls remaps Confirm, Cancel, Menu, Delete, Skip and Page left/right for keyboard and joystick, or restores the defaults.",
		"",
		"#Cheats",
		"In the Cheats menu type a code and press Enter. cid = all cards | edea = Guardian Forces (levels 8-9) | balamb = characters (level 10) | monster = monsters (levels 1-7).",
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
		"Partida contra la IA. Elige Mi mazo (cinco cartas del \u00e1lbum), Mazo aleatorio, un mazo nuevo o un mazo guardado. C amplia las cartas. T ordena la cuadr\u00edcula (cat\u00e1logo, nivel, valor). Las reglas est\u00e1n a la derecha. F5 reinicia con el mismo mazo.",
		"",
		"#Campeonato",
		"Ocho partidas contra la IA. Si el \u00e1lbum tiene menos de cinco cartas, la primera partida crea un sobre inicial. Al ganar, toma una carta del rival. C amplia las cartas en el sobre, la elecci\u00f3n y el intercambio. T ordena la cuadr\u00edcula en la elecci\u00f3n. El \u00e1lbum y las copas permanecen cuando termina la ronda.",
		"",
		"#Mi mazo y perfiles",
		"Mi mazo es el \u00e1lbum de la colecci\u00f3n. C amplia; T ordena la cuadr\u00edcula; X o Del quita una copia. En Ajustes puedes editar el nombre, crear otros jugadores, cambiar de perfil y elegir la dificultad de la IA. Cada perfil guarda su propio \u00e1lbum y mazos.",
		"",
		"#Guardar",
		"En el men\u00fa Guardar puedes exportar o importar el \u00e1lbum (Mi mazo) o el punto de control del Campeonato en un archivo .ttsave con el di\u00e1logo del sistema. Importar pide confirmaci\u00f3n antes de sustituir.",
		"",
		"#Versus",
		"Dos jugadores en el mismo ordenador, con la regla Open. Ambos eligen cartas de Mi mazo del perfil actual (se pueden repetir): primero el Jugador A (derecha), luego el B (izquierda). El joystick 1 juega con A; el joystick 2 o el teclado juegan con B (sin joystick, el teclado juega con ambos). Nadie gana ni pierde cartas y nada se guarda.",
		"",
		"#Trofeos",
		"Contadores del perfil: campeonatos ganados, partidas del campeonato, victorias, derrotas y empates en Partida r\u00e1pida. Versus no cuenta.",
		"",
		"#Controles y joystick",
		"La cruceta y el stick mueven el cursor en todas las pantallas. En Ajustes > Controles puedes cambiar Confirmar, Cancelar, Men\u00fa, Borrar, Saltar y P\u00e1gina izquierda/derecha en teclado y joystick, o restaurar los valores.",
		"",
		"#Cheats",
		"En el men\u00fa Trucos escribe un c\u00f3digo y pulsa Enter. cid = todas las cartas | edea = Guardian Forces (niveles 8-9) | balamb = personajes (nivel 10) | monster = monstruos (niveles 1-7).",
		"",
		"#Recordatorio",
		"Coloca, compara, captura, domina. El n\u00famero mayor gana. Same busca igualdad. Plus busca sumas iguales. Quien tenga m\u00e1s cartas al final gana."
	};
}
