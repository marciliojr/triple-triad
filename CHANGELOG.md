# Changelog

[Português (Brasil)](#changelog-português-brasil) · [English](#changelog-english)

---

## Changelog (Português Brasil)

Todas as mudanças relevantes deste fork são registradas aqui.

O formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/), e o projeto adota [SemVer](https://semver.org/lang/pt-BR/).

### [Unreleased]

#### Added

- Modo História: campanha original em que o jogador é um aluno de Balamb Garden. Squall orienta e não joga. O locutor Nilo Vann aparece num crachá desenhado na interface
- Até 15 campanhas por perfil (`CAMP.*`), com nome do aluno, etapa, duelos, especiais já entregues e recompensa pendente. O álbum, os decks, os blocos do Campeonato e os troféus continuam do perfil. Apagar uma campanha pede confirmação e não apaga cartas
- Save antigo `STORY_CLEARED` / `STORY_ARRIVALS` migra para a primeira campanha e permanece no arquivo. Gravação do perfil em arquivo temporário e troca atômica
- Cada vitória oferece uma carta da mão jogada, com confirmação. A carta especial do encontro (Ifrit, Shiva, Bahamut e as demais do catálogo já mapeadas) entra só na primeira vitória e não aparece nessa mão
- Regulamento jogável: seletiva em chave no Garden; grupos com tabela em Balamb, Timber, Deling, Dollet, FH e Esthar; final no Garden. Derrota não elimina. Vídeo pronto em `intro_historia` e `chegada_balamb_town`; as outras seis chegadas usam o fundo estático

### [1.3.0] - 2026-11-08

Abertura e vídeo final, save estilo Memory Card PS1 com 15 blocos, Versus local, joystick com todos os botões personalizáveis e teclado virtual, Cheats, Troféus, perfis com avatar e legendas de botões estilo PS1.

#### Added

- Legendas de botões redesenhadas no estilo PS1 (`ui/HintBar`): painel escuro com uma entrada por ação, tecla como keycap cinza e botão do joystick colorido (A verde, B vermelho, X azul, Y amarelo), quebra em várias linhas e fonte menor quando precisa, sem cortar texto. Usado no Campeonato, Versus, decks, Memory Card, Cheats, Controles e demais telas com dica no rodapé
- Controles: todas as ações são personalizáveis (1 tecla + 1 botão do joystick cada): Confirmar, Cancelar, Selecionar (ordenar/editar), Menu, Pular intro, Remover/Apagar, Zoom, Página esquerda/direita, Aleatório, Salvar, Revanche, Jogada automática e Cima/Baixo/Esquerda/Direita. Teclas fora dos atalhos não fazem mais nada (sem atalhos fixos); Del na tela de Controles limpa um atalho
- Dicas do rodapé e o Como Jogar mostram os atalhos atuais (ex.: "[S]/[X] para pular"); o Como Jogar ganhou a lista completa de botões
- Configurações > Controles mostra o controle detectado, um teste ao vivo do último botão/eixo e, se houver um receptor 8BitDo só em modo HID (2dc8:3109), a dica "Controle em modo incompatível: ligue no modo X-input"
- `gamecontrollerdb.txt` (SDL_GameControllerDB) vai junto no jar e é carregado pelo Jamepad; se o SDL não achar controle, o jogo lê `/dev/input/js*` como joystick genérico (botões numerados, analógico e direcional)
- Teclado virtual estilo PS1 (letras, números, espaço, apagar, shift, OK, cancelar) no nome do perfil, no nome do deck e nos Cheats; abre sozinho quando a entrada vem do joystick e some ao usar o teclado físico
- Vitória no Campeonato: na escolha da carta há a opção **Não pegar nenhuma carta** (evita duplicatas) e a música `res/victory.ogg` toca em loop nessa tela; ao sair, a música normal volta
- Menu **Versus**: 2 jogadores locais com a regra Open forçada. Jogador A escolhe 5 cartas do Meu Deck do perfil atual (pode repetir), depois o B; turnos alternados, vencedor na tela e volta ao menu. Não dá nem tira cartas e não salva nada. Joystick 1 = A, joystick 2 ou teclado = B (sem joystick, teclado para os dois)
- Meu Deck: **Remover todas as cartas** (R ou o rótulo no painel) abre uma tela estilo PS1 para remover tudo ou por categoria (monstros 1-5, chefes 6-7, GFs 8-9, personagens 10) com confirmação Sim/Não
- Joystick (gdx-controllers 2.2.4): direcional e analógico navegam em todas as telas. Nova tela **Configurações > Controles** para remapear Confirmar, Cancelar, Menu, Apagar, Pular e Página esquerda/direita no teclado e no joystick ("pressione..."), salva no cfg, com Restaurar padrão
- Seleção de perfil depois da abertura, em blocos estilo memory card (avatar, nome, cartas, troféus) com opção de criar perfil novo. 8 avatares em `res/avatars/` escolhidos no cadastro e na edição do perfil; perfis antigos usam o avatar 1
- Menu **Troféus**: campeonatos vencidos, partidas do campeonato (V/D) e vitórias, derrotas, empates e jogadas no Jogo Rápido, persistentes no perfil. Versus não conta
- Como jogar: seções Versus, Troféus, Controles e Cheats (cid, edea, balamb, monster)
- Menu **Cheats**: uma linha de texto para digitar códigos (sem diferenciar maiúsculas). `cid` libera todas as cartas, `edea` as Guardian Forces (níveis 8-9), `balamb` os personagens (nível 10) e `monster` os monstros (níveis 1-7, incluindo os chefes dos níveis 6-7). Cada carta que falta entra uma vez no Meu Deck e o perfil é salvo; código errado mostra "Código inválido"
- Abertura: ao abrir o jogo toca uma vez o vídeo `res/intro.mp4` (quadros em `res/intro/` + `res/intro.ogg`) antes do menu; S pula na hora e a dica "S para pular" aparece no rodapé. A música do menu só começa depois da abertura; se o vídeo falhar, vai direto pro menu
- Save do Campeonato no estilo Memory Card do PS1 (tela Load/Cargar do FF8): cabeçalho "MEMORY CARD em uso: Slot 1" + Salvar/Carregar, caixa AJUDA, 15 blocos numerados em páginas de 3 com cursor de mão e setas, cartas da mão como retratos, Nv (vitórias), rodada, tempo de jogo, cartas e local (lugar do FF8 + partida). Bloco vazio mostra Livre/Vazio
- Vários saves ao mesmo tempo: salvar num bloco (confirma ao sobrescrever), carregar e apagar (D ou Delete, com confirmação). O bloco guarda rodada, vitórias, mão, mão do adversário, cartas da run, regras ativas e tempo de jogo; carregar restaura regras e esse ponto
- Ao vencer o Campeonato, o vídeo `res/endgame.mp4` toca em loop (com o áudio, se a música estiver ligada) até o jogador apertar qualquer tecla ou clicar; depois volta ao menu. Se o vídeo não carregar, aparece a mensagem de parabéns de antes
- O vídeo é tocado a partir de quadros JPEG pré-extraídos em `res/endgame/` (24 fps, 640x360) e do áudio `res/endgame.ogg`, porque o libGDX não decodifica vídeo

#### Changed

- Versus: a mesma carta não pode entrar duas vezes na mão de um jogador (o aleatório também não repete); os dois jogadores ainda podem escolher as mesmas cartas
- Na partida, Cancelar primeiro desfaz a posição escolhida no tabuleiro, depois oferece sair
- O save do Campeonato não é mais apagado ao vencer ou perder a run: o bloco só some quando o jogador apaga. "Continuar" e "Apagar progresso" do lobby viraram "Carregar" (abre o Memory Card)
- O save único antigo (`RUN_*` no perfil) migra sozinho para o bloco 01. Exportar/importar `.ttsave` continua: exporta o bloco salvo por último e importa para o primeiro bloco livre

### [1.2.0] - 2026-10-18

Troca do Campeonato mostra se você já tem a carta, e cartas sem elemento deixam de mostrar um quadrado vazio.

#### Added

- Na escolha de carta após vencer no Campeonato, cada carta do adversário mostra **Já possui / no deck: N** ou **Nova!**, e uma linha abaixo diz quantas cópias da carta do cursor já estão no Meu Deck

#### Fixed

- Cartas sem elemento (NEUTRAL) não mostram mais um quadrado vazio no rótulo de elemento dos slots e do painel da carta: o texto fica em branco
- Linha id / nome / elemento usa `-` como separador em vez de travessão (U+2014), que não existe na fonte gerada e aparecia como quadrado

### [1.1.1] - 2026-09-01

O diálogo de exportar/importar save fecha de verdade e não reabre sozinho.

#### Fixed

- `FileDialog` do menu Salvar roda no EDT, tem janela dona, faz `dispose` e ignora Z/Enter/clique residual — o gerenciador de arquivos (Dolphin etc.) não fica reabrindo depois de salvar ou cancelar

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

#### Added

- Story mode: an original campaign where the player is a Balamb Garden student. Squall advises and does not play. Announcer Nilo Vann appears as a badge drawn by the interface
- Up to 15 campaigns per profile (`CAMP.*`), with the student name, stage, duels, specials already given and a pending reward. The album, decks, Championship blocks and trophies stay on the profile. Deleting a campaign asks for confirmation and does not remove cards
- An old `STORY_CLEARED` / `STORY_ARRIVALS` save migrates into the first campaign and stays in the file. The profile is written to a temp file and replaced atomically
- Each win offers one card from the hand that was played, with a confirmation. The meeting's special card (Ifrit, Shiva, Bahamut and the other mapped catalog ids) is given only on the first win and is not in that hand
- Playable rules: Garden ladder qualifier; real tables at Balamb, Timber, Deling, Dollet, FH and Esthar; final at Garden. A loss does not eliminate you. Video is ready for `intro_historia` and `chegada_balamb_town`; the other six arrivals use the still background

### [1.3.0] - 2026-11-08

Opening and ending videos, PS1 Memory Card style save with 15 blocks, local Versus, joystick with every button customizable and a virtual keyboard, Cheats, Trophies, profiles with avatars and PS1-style button legends.

#### Added

- Button legends redesigned in PS1 style (`ui/HintBar`): dark panel with one entry per action, keys as gray keycaps and joystick buttons colored (A green, B red, X blue, Y yellow), wrapping to several rows and a smaller font when needed, never cutting text. Used in Championship, Versus, decks, Memory Card, Cheats, Controls and the other screens with a footer hint
- Controls: every action is customizable (1 key + 1 joystick button each): Confirm, Cancel, Select (sort/edit), Menu, Skip intro, Remove/Delete, Zoom, Page left/right, Random, Save, Rematch, Auto play and Up/Down/Left/Right. Keys outside the bindings do nothing (no fixed shortcuts); Del on the Controls screen clears a binding
- Footer hints and How to Play show the current bindings (e.g. "[S]/[X] to skip"); How to Play lists every button
- Settings > Controls shows the detected controller, a live test of the last button/axis and, when an 8BitDo receiver is only in HID mode (2dc8:3109), the hint "Controller in an incompatible mode: switch it to X-input mode"
- `gamecontrollerdb.txt` (SDL_GameControllerDB) ships in the jar and is loaded by Jamepad; when SDL finds no controller, the game reads `/dev/input/js*` as a generic joystick (numbered buttons, stick and D-pad)
- PS1-style virtual keyboard (letters, numbers, space, delete, shift, OK, cancel) for profile name, deck name and Cheats; opens when input comes from the joystick and hides when the physical keyboard is used
- Championship victory: the card pick has a **Don't take any card** option (avoids duplicates) and `res/victory.ogg` loops on that screen; leaving it brings back the normal music
- **Versus** menu: 2 local players with the Open rule forced. Player A picks 5 cards from the current profile's My Deck (repeats allowed), then B; alternating turns, winner shown, back to the menu. No cards change hands and nothing is saved. Joystick 1 = A, joystick 2 or keyboard = B (no joystick: keyboard plays both)
- My Deck: **Remove all cards** (R or the panel label) opens a PS1-style screen to remove everything or one category (monsters 1-5, bosses 6-7, GFs 8-9, characters 10) with a Yes/No confirmation
- Joystick (gdx-controllers 2.2.4): D-pad and stick navigate every screen. New **Settings > Controls** screen remaps Confirm, Cancel, Menu, Delete, Skip and Page left/right for keyboard and joystick ("press..."), saved in the cfg, with Restore defaults
- Profile selection after the intro as memory card blocks (avatar, name, cards, trophies) with a new-profile option. 8 avatars in `res/avatars/` chosen when creating/editing a profile; old profiles use avatar 1
- **Trophies** menu: championships won, championship matches (W/L) and Quick Game wins, losses, draws and games played, stored in the profile. Versus does not count
- How to play: Versus, Trophies, Controls and Cheats (cid, edea, balamb, monster) sections
- **Cheats** menu: one text line to type codes (case-insensitive). `cid` unlocks every card, `edea` the Guardian Forces (levels 8-9), `balamb` the characters (level 10) and `monster` the monsters (levels 1-7, bosses at 6-7 included). Each missing card is added once to My Deck and the profile is saved; a wrong code shows "Invalid code"
- Opening: the game plays `res/intro.mp4` once (frames in `res/intro/` + `res/intro.ogg`) before the menu; S skips it right away and a "S to skip" hint shows at the bottom. Menu music only starts after the intro; if the video fails, the menu opens directly
- PS1 Memory Card style Championship save (FF8 Load screen): "MEMORY CARD in use: Slot 1" header + Save/Load, HELP box, 15 numbered blocks in pages of 3 with hand cursor and arrows, hand cards as portraits, LV (wins), round, play time, cards and location (FF8 place + match). Empty blocks show Free/Empty
- Several saves at once: save to a block (confirm on overwrite), load and delete (D or Delete, with confirmation). A block stores round, wins, hand, opponent hand, run cards, active rules and play time; loading restores the rules and that point
- Winning the Championship loops the `res/endgame.mp4` video (with audio when music is on) until the player presses any key or clicks, then returns to the menu. If the video fails to load, the previous congratulations message is shown
- The video plays from JPEG frames pre-extracted into `res/endgame/` (24 fps, 640x360) plus the `res/endgame.ogg` audio, since libGDX has no video decoder

#### Changed

- Versus: the same card cannot enter a player's hand twice (random fill does not repeat either); both players may still pick the same cards
- In a match, Cancel first undoes the chosen board position, then offers to leave
- The Championship save is no longer erased when the run is won or lost: a block only goes away when the player deletes it. Lobby "Continue" and "Clear progress" became "Load" (opens the Memory Card)
- The old single save (`RUN_*` in the profile) migrates to block 01 automatically. `.ttsave` export/import still works: export writes the most recently saved block, import goes to the first free block

### [1.2.0] - 2026-10-18

The Championship trade shows whether you already own the card, and cards with no element no longer show an empty square.

#### Added

- On the card pick after a Championship win, each opponent card shows **Owned / in deck: N** or **New!**, and a line below says how many copies of the cursor card are already in My Deck

#### Fixed

- Cards with no element (NEUTRAL) no longer show an empty square in the element label of the hand slots and the card panel: the text is left blank
- The id / name / element line uses `-` as separator instead of an em dash (U+2014), which is missing from the generated font and rendered as a square

### [1.1.1] - 2026-09-01

The save export/import dialog actually closes and does not reopen by itself.

#### Fixed

- Save-menu `FileDialog` runs on the EDT, uses an owner window, is disposed, and ignores leftover Z/Enter/click — the file manager (Dolphin, etc.) no longer keeps reopening after save or cancel

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
