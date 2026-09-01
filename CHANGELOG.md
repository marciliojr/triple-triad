# Changelog

[Português (Brasil)](#changelog-português-brasil) · [English](#changelog-english)

---

## Changelog (Português Brasil)

Todas as mudanças relevantes deste fork são registradas aqui.

O formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/), e o projeto adota [SemVer](https://semver.org/lang/pt-BR/).

### [Unreleased]

### [1.1.0] - 2026-09-01

Menu Salvar (exportar/importar `.ttsave`) e ordenação das grades de cartas.

#### Added

- Item de menu **Salvar**: exportar/importar o álbum (Meu Deck) e o checkpoint do Campeonato via diálogo do sistema (arquivos `.ttsave`)
- Confirmação Não / Sim (default Não) ao importar por cima do álbum ou da run
- Tecla **T** cicla a ordem do grid (catálogo/ID, nível, valor/soma dos ranks) no construtor, Meu Deck e pick do Campeonato
- `CardSort` (view da sessão; não muta catálogo, álbum nem bag)

#### Changed

- Menu passa a incluir Salvar entre Meu Deck e Como Jogar
- Dicas de teclas incluem **T**; Como Jogar cobre Salvar e a ordenação
- README, CHANGELOG e memory-bank alinhados à 1.1.0

#### Fixed

- Parse do level em `Deck` lê a coluna Level (`tokens[4]`) de `cards/deck.txt`

### [1.0.0] - 2026-08-27

Primeira versão jogável: seleção de cartas com identidade visível, preview na galeria e confirmação ao apagar do álbum.

#### Added

- Painel direito com carta média (`drawSized`) e linha id / nome / elemento no construtor e nos picks (Jogo Rápido e Campeonato)
- Nome curto e elemento sob os 5 slots da mão
- Tecla **R** (e o rótulo no painel) preenche 5 cartas aleatórias do conjunto da tela
- Galeria Meu Deck com o mesmo painel do cursor ao navegar; **C** continua overlay em tamanho de partida
- Confirmação Não / Sim (default Não) antes de apagar uma cópia do álbum

#### Changed

- **X** no construtor e nos picks só tira da mão de 5 (não apaga o álbum)
- Painéis de Configurações e do preview da carta passam a caber texto e cursor
- Aviso de progresso salvo do Campeonato fica no rodapé, abaixo da barra de teclas
- README e CHANGELOG em português (Brasil) e inglês no mesmo arquivo

#### Fixed

- Largura da fonte arredonda para cima (`UnicodeFont.getWidth`) para o texto não vazar dos retângulos

### [0.7.0] - 2026-08-27

Preview com C, teclas padronizadas, dificuldade da IA, vários perfis e Versus fora do menu.

#### Added

- Preview em tamanho cheio com **C** em todas as telas de cartas (Meu Deck, pick do Jogo Rápido, construtor, decks salvos, pack/pick/troca do Campeonato)
- Dificuldade da IA nas Configurações (Fácil / Normal / Difícil)
- Vários perfis (criar, trocar, renomear, apagar) em `.triple-triad-profiles/`
- Jogo Rápido: opção **Deck aleatório**

#### Changed

- Teclas padronizadas: **Z** seleciona, **X**/Del remove, **C** amplia, **Esc** volta (na partida, **X** continua cancelando a casa)
- Versus saiu do menu principal (permanece fora de escopo)
- Como Jogar atualizado (Jogo Rápido, Campeonato, Meu Deck e perfis)
- IA menos repetitiva: sorteia entre as melhores jogadas; round 8 do Campeonato usa OFFENSIVE
- Persistência: `ACTIVE_PROFILE` no cfg; um arquivo por perfil

#### Fixed

- Chiado no deal com a regra Aberta (teto de 50 ms e `CARD.playReplacing`)

### [0.6.0] - 2026-08-26

Meu Deck, persistência da run do Campeonato, confirmação ao sair da partida, libGDX 1.14.2 e esteira de CI/Release.

#### Added

- Esc na partida abre confirmação (Não / Sim; default Não) para evitar saída acidental
- Aviso na tela ao salvar o progresso do Campeonato no pick
- **Meu Deck**: álbum permanente (duplicatas ok; preview no tamanho da partida; Del remove)
- Campeonato: **Novo jogo** (pack se o deck estiver vazio, senão pick do álbum), **Continuar**, **Apagar progresso** (não zera o deck)
- Save da run: `RUN_ROUND` / `RUN_BAG` / `RUN_HAND` / `RUN_OPPONENT` (Salvar no pick)
- Jogo Rápido pode montar 5 cartas a partir de Meu Deck
- Empate no Campeonato refaz a luta com as 5 originais de cada lado (sem misturar donos)
- Menu: Jogo Rápido, Campeonato, Meu Deck, Versus, Como Jogar, Configurações
- Distribuição `./gradlew distZip` inclui `res/` e `cards/` (artefato da GitHub Release)
- Esteira GitHub Actions: build no PR; tag SemVer e Release no merge na `master`

#### Changed

- libGDX **1.13.1 → 1.14.2**
- Assets também resolvem a partir da pasta de instalação do zip (`lib/../res`, `lib/../cards`)

### [0.5.0] - 2026-08-26

Modo Campeonato: gauntlet de 8 vitórias, coleção persistente e troca de cartas a partir da 2ª luta.

#### Added

- Item de menu **Campeonato** (8 rounds contra a IA)
- Coleção no perfil (`COLLECTION`, `CHAMPIONSHIP_WINS`), com duplicatas
- Pacote inicial de 5 cartas com pelo menos um lado A
- Troca de cartas da 2ª luta em diante (jogador escolhe; IA pega a de maior soma de ranks)
- Lobby com as mesmas regras do Jogo Rápido

#### Changed

- Esc no campeonato aborta a run sem aplicar o roubo da partida atual
- F5 (rematch) só funciona no Jogo Rápido

### [0.4.0] - 2026-08-26

Tela de Configurações, idioma da interface e regras na seleção de deck.

#### Added

- Item de menu **Configurações**: música de fundo, som do cursor e idioma (português do Brasil, inglês, espanhol)
- Persistência `MUSIC_ENABLED`, `CURSOR_SOUND` e `LANGUAGE` em `.triple-triad.cfg`
- Painel de regras na tela de Jogo Rápido (Open, Same, Same Wall, Plus, Combo, Elemental, Sudden Death)
- Tela **Como Jogar** com manual resumido no idioma escolhido

#### Changed

- Textos de menu, perfil, decks e dicas passam por `I18n`
- Fonte inclui glifos extras do espanhol (`ñ`, `¿`, `¡`)

### [0.3.0] - 2026-08-26

Sobe o cliente de Java 17 para **Java 21**. Sem mudança de regras, telas ou assets.

#### Changed

- `sourceCompatibility` / `targetCompatibility` 21 no Gradle
- `.classpath` em JavaSE-21
- README, requisitos e memory bank alinhados ao JDK 21

### [0.2.0] - 2026-08-26

Modernização do cliente desktop: build Gradle, Java 17, libGDX e meta-jogo local. As regras de captura (Same, Plus, Combo, Elemental, Sudden Death) e a IA não foram reescritas.

#### Added

- Build Gradle (`./gradlew run` / `./gradlew build`) com wrapper 9.1.0
- Perfil obrigatório persistido em `.triple-triad-profile`
- Menu principal (Jogo Rápido; Campeonato e Versus reservados)
- Construtor e seletor de decks de 5 cartas
- Camada gráfica `itdelatrisu.tripletriad.gfx` sobre libGDX 1.13.1 (janela, input, sprites, fonte TTF com glifos PT-BR, BGM/SFX)
- Fonte menor para menus e dicas
- Memory bank em `memory-bank/`

#### Changed

- Linguagem-alvo de Java 7 para **Java 17**
- Apresentação de Slick2D + LWJGL 2.9.1 para **libGDX** (LWJGL 3 via backend)
- Assets continuam em `res/` e `cards/`, resolvidos pelo cwd da raiz
- `.classpath` aponta para JavaSE-17
- README descreve o fluxo atual e o build Gradle

#### Removed

- Dependência de JARs locais `lib/slick.jar`, `lib/lwjgl-2.9.1.jar`, `lib/jorbis-0.0.17-2.jar`
- Natives LWJGL 2 em `native/`
- Ant como caminho oficial de build (`build.xml` permanece no repo, sem suporte)

#### Fixed

- Esc na partida volta ao menu em vez de encerrar o processo
- F5 faz rematch com o mesmo deck do Jogo Rápido
- `isGameOver()` tolera mãos ainda não inicializadas (telas de menu)

### [0.1.1] - 2014-08-01

Release original de Jeffrey Han ([itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)): Slick2D, LWJGL 2, Ant/Eclipse, partida imediata contra a IA.

---

## Changelog (English)

All notable changes to this fork are documented here.

The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project uses [SemVer](https://semver.org/).

### [Unreleased]

### [1.1.0] - 2026-09-01

Save menu (export/import `.ttsave`) and card-grid sorting.

#### Added

- **Save** menu item: export/import the album (My Deck) and the Championship checkpoint via the system file dialog (`.ttsave` files)
- No / Yes confirmation (default No) before importing over the album or the run
- **T** cycles the grid order (catalog/ID, level, value/rank-sum) in the builder, My Deck, and Championship pick
- `CardSort` (session view; does not mutate catalog, album, or bag)

#### Changed

- The menu now includes Save between My Deck and How to Play
- Key hints include **T**; How to Play covers Save and sorting
- README, CHANGELOG, and memory-bank aligned to 1.1.0

#### Fixed

- Level parse in `Deck` reads the Level column (`tokens[4]`) from `cards/deck.txt`

### [1.0.0] - 2026-08-27

First playable release: card selection with visible identity, gallery preview, and confirmation before deleting from the album.

#### Added

- Right-hand panel with a medium card (`drawSized`) and id / name / element on the builder and pickers (Quick Game and Championship)
- Short name and element under the five hand slots
- **R** (and the panel label) fills five random cards from the current screen’s pool
- My Deck gallery uses the same cursor panel while browsing; **C** still opens the match-size overlay
- No / Yes confirmation (default No) before deleting one album copy

#### Changed

- **X** in the builder and pickers only removes from the 5-card hand (it does not delete from the album)
- Settings and card-preview panels fit their text and cursor
- Championship “progress saved” notice sits in the footer, under the key hint bar
- README and CHANGELOG in Brazilian Portuguese and English in the same file

#### Fixed

- Font width rounds up (`UnicodeFont.getWidth`) so labels do not spill out of rectangles

### [0.7.0] - 2026-08-27

Full-size preview with C, standardized keys, AI difficulty, multiple profiles, and Versus removed from the menu.

#### Added

- Full-size preview with **C** on every card screen (My Deck, Quick Game pick, builder, saved decks, Championship pack/pick/trade)
- AI difficulty in Settings (Easy / Normal / Hard)
- Multiple profiles (create, switch, rename, delete) under `.triple-triad-profiles/`
- Quick Game: **Random deck** option

#### Changed

- Standardized keys: **Z** selects, **X**/Del removes, **C** enlarges, **Esc** goes back (in a match, **X** still cancels the square)
- Versus left the main menu (still out of scope)
- How to Play updated (Quick Game, Championship, My Deck and profiles)
- Less repetitive AI: picks at random among the best moves; Championship round 8 uses OFFENSIVE
- Persistence: `ACTIVE_PROFILE` in the cfg; one file per profile

#### Fixed

- Deal hiss with the Open rule (50 ms cap and `CARD.playReplacing`)

### [0.6.0] - 2026-08-26

My Deck, Championship run persistence, confirm-to-leave match, libGDX 1.14.2, and CI/Release pipeline.

#### Added

- Esc during a match opens a confirmation (No / Yes; default No) to avoid accidental exit
- On-screen notice after saving Championship progress on the pick screen
- **My Deck**: permanent album (duplicates allowed; match-size preview; Del removes)
- Championship: **New game** (pack if the album is empty, otherwise album pick), **Continue**, **Clear progress** (does not wipe the album)
- Run save: `RUN_ROUND` / `RUN_BAG` / `RUN_HAND` / `RUN_OPPONENT` (Save on the pick screen)
- Quick Game can build 5 cards from My Deck
- A Championship draw rematches with each side’s original 5 (owners are not mixed)
- Menu: Quick Game, Championship, My Deck, Versus, How to Play, Settings
- `./gradlew distZip` includes `res/` and `cards/` (GitHub Release artifact)
- GitHub Actions: build on PR; SemVer tag and Release on merge to `master`

#### Changed

- libGDX **1.13.1 → 1.14.2**
- Assets also resolve from the zip install folder (`lib/../res`, `lib/../cards`)

### [0.5.0] - 2026-08-26

Championship mode: 8-win gauntlet, persistent collection, and card trade from the second match onward.

#### Added

- **Championship** menu item (8 rounds vs the AI)
- Collection on the profile (`COLLECTION`, `CHAMPIONSHIP_WINS`), duplicates allowed
- Starter pack of 5 cards with at least one A side
- Card trade from match 2 onward (player chooses; AI takes the highest rank-sum)
- Lobby with the same rules as Quick Game

#### Changed

- Esc in Championship aborts the run without applying the current match steal
- F5 (rematch) only works in Quick Game

### [0.4.0] - 2026-08-26

Settings screen, UI language, and rules on the deck select screen.

#### Added

- **Settings** menu item: background music, cursor sound, and language (Brazilian Portuguese, English, Spanish)
- Persistence `MUSIC_ENABLED`, `CURSOR_SOUND`, and `LANGUAGE` in `.triple-triad.cfg`
- Rules panel on the Quick Game screen (Open, Same, Same Wall, Plus, Combo, Elemental, Sudden Death)
- **How to Play** screen with a short manual in the chosen language

#### Changed

- Menu, profile, deck, and hint strings go through `I18n`
- Font includes extra Spanish glyphs (`ñ`, `¿`, `¡`)

### [0.3.0] - 2026-08-26

Raises the client from Java 17 to **Java 21**. No change to rules, screens, or assets.

#### Changed

- `sourceCompatibility` / `targetCompatibility` 21 in Gradle
- `.classpath` on JavaSE-21
- README, requirements, and memory bank aligned to JDK 21

### [0.2.0] - 2026-08-26

Desktop client modernization: Gradle build, Java 17, libGDX, and local meta-game. Capture rules (Same, Plus, Combo, Elemental, Sudden Death) and the AIs were not rewritten.

#### Added

- Gradle build (`./gradlew run` / `./gradlew build`) with wrapper 9.1.0
- Required profile persisted in `.triple-triad-profile`
- Main menu (Quick Game; Championship and Versus reserved)
- 5-card deck builder and selector
- Graphics layer `itdelatrisu.tripletriad.gfx` on libGDX 1.13.1 (window, input, sprites, TTF with PT-BR glyphs, BGM/SFX)
- Smaller font for menus and hints
- Memory bank in `memory-bank/`

#### Changed

- Language target from Java 7 to **Java 17**
- Presentation from Slick2D + LWJGL 2.9.1 to **libGDX** (LWJGL 3 via the backend)
- Assets stay in `res/` and `cards/`, resolved from the repo root cwd
- `.classpath` points to JavaSE-17
- README describes the current flow and the Gradle build

#### Removed

- Local JAR dependency on `lib/slick.jar`, `lib/lwjgl-2.9.1.jar`, `lib/jorbis-0.0.17-2.jar`
- LWJGL 2 natives in `native/`
- Ant as the official build path (`build.xml` remains in the repo, unsupported)

#### Fixed

- Esc during a match returns to the menu instead of quitting the process
- F5 rematches with the same Quick Game deck
- `isGameOver()` tolerates hands that are not initialized yet (menu screens)

### [0.1.1] - 2014-08-01

Original Jeffrey Han release ([itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)): Slick2D, LWJGL 2, Ant/Eclipse, immediate match vs the AI.
