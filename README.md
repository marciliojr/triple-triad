# Triple Triad

[Português (Brasil)](#português-brasil) · [English](#english)

**1.3.0** — abertura e vídeo final, Memory Card PS1 com 15 blocos, Versus local, joystick com todos os botões personalizáveis, Cheats, Troféus e perfis com avatar. Zip na [GitHub Release](https://github.com/marciliojr/triple-triad/releases/tag/v1.3.0).

---

## Português (Brasil)

Implementação desktop do Triple Triad, o jogo de cartas de *Final Fantasy VIII*.

Fork do projeto de Jeffrey Han: **[itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)**. O original entregava a partida contra a IA com as regras clássicas. Este cliente é Java 21 + libGDX, com perfil, decks, Jogo Rápido, Campeonato, Meu Deck, Salvar, Como Jogar e Configurações — sem reescrever o motor de captura.

Regras: [Triple Triad (wiki)](http://finalfantasy.wikia.com/wiki/Triple_Triad). Versões: [CHANGELOG.md](CHANGELOG.md).

### Requisitos e como rodar

- JDK **21** ou superior
- Gradle Wrapper no repositório (`./gradlew`)

```bash
./gradlew run      # na raiz (usa res/ e cards/)
./gradlew build    # bytecode Java 21
./gradlew distZip  # zip jogável em build/distributions/
./gradlew clean
```

A [Release v1.3.0](https://github.com/marciliojr/triple-triad/releases/tag/v1.3.0) traz o mesmo zip (`res/` + `cards/` + jar).

### Fluxo

1. **Abertura** — vídeo de intro (Pular intro pula)
2. **Perfil** — nome e avatar na primeira execução; depois, escolha do perfil em blocos estilo memory card
3. **Menu** — Jogo Rápido, Campeonato, História, Versus, Meu Deck, Salvar, Troféus, Cheats, Como Jogar, Configurações. O perfil ativo (avatar, nome, cartas e campeonatos) fica no canto superior esquerdo; Esquerda abre a troca de perfil
4. **Decks** — montar, editar, apagar ou escolher 5 cartas; ligar/desligar regras
5. **Campeonato** — 8 rounds contra a IA
6. **Partida** — Cancelar pede confirmação para sair

### Telas

**Meu Deck** — álbum permanente (duplicatas ok). Ao navegar, o painel direito mostra a carta média com id, nome e elemento. **C** (ou clique na carta média) abre o overlay em tamanho de partida. **T** cicla a ordem do grid (catálogo, nível, valor). **X**/Del pede Não/Sim antes de apagar uma cópia.

**Jogo Rápido** — Meu Deck (pick 5), deck aleatório da lista, construtor ou deck salvo. No construtor e no pick: 5 slots com nome/elemento, grade à esquerda, painel da carta do cursor à direita. **R** preenche 5 aleatórias. **T** ordena o grid. **X** só tira da mão.

**Salvar** — exportar ou importar o álbum (Meu Deck) ou o checkpoint do Campeonato em um arquivo `.ttsave` pelo diálogo do sistema. Importar pede Não/Sim (default Não) antes de substituir.

**História** — campanha original no mundo de FFVIII. O jogador é um aluno de Balamb (não é o Squall) e escolhe o nome no teclado ou no teclado virtual. Squall orienta. Nilo Vann, o locutor, aparece num crachá desenhado na tela (não reutiliza outro rosto) e apresenta cada partida. Um perfil guarda até 15 campanhas; criar ou apagar uma não mexe no álbum nem nas outras. Cada vitória deixa escolher uma carta da mão jogada. A carta especial do encontro sai só na primeira vitória, separada dessa escolha. Derrota não elimina: a revanche é na mesma mesa. Fases de pontos mostram a tabela de verdade (vitória = 3; derrota não entra até a revanche). Sair no meio da partida volta ao último ponto salvo.

O caminho é seletiva no Garden, classificatória em Balamb Town, grupos em Timber, fase principal em Deling, rodada decisiva em Dollet, quartas em Fisherman's Horizon, semifinal em Esthar e grande final de volta ao Garden. Há vídeo em `intro_historia` e `chegada_balamb_town`. As outras chegadas usam o fundo da cidade até os clipes existirem (`res/story/IMPORTACAO.txt`).

**Campeonato** — lobby: Novo jogo, Continuar, Apagar progresso (não zera o álbum). Pack se o álbum tiver menos de 5 cartas; senão pick. **T** ordena o grid no pick. **S** / Salvar (da 2ª luta em diante) grava a run; o aviso aparece no rodapé. Vitória adiciona a carta ao álbum e à bag; na escolha, cada carta do adversário mostra **Já possui** (com quantas cópias no Meu Deck) ou **Nova!**. Derrota tira só da run.

**Campeonato (1.3.0)** — save estilo Memory Card do PS1: 15 blocos (salvar, carregar, apagar), o save não some ao terminar a run. Ao vencer dá para **não pegar nenhuma carta**; a vitória toca música própria e zerar o Campeonato mostra o vídeo final em loop.

**Versus** — 2 jogadores locais com a regra Open. Cada um escolhe 5 cartas do Meu Deck do perfil atual, sem repetir carta na própria mão (os dois podem escolher as mesmas). Joystick 1 = Jogador A, joystick 2 ou teclado = B. Não dá nem tira cartas.

**Troféus** — campeonatos vencidos, partidas do campeonato e vitórias/derrotas/empates do Jogo Rápido, por perfil.

**Cheats** — digite um código: `cid` (todas as cartas), `edea` (Guardian Forces, níveis 8-9), `balamb` (personagens, nível 10), `monster` (monstros, níveis 1-7). Cada carta que falta entra uma vez no Meu Deck.

**Meu Deck → Remover todas as cartas** — tudo ou por categoria (monstros, chefes, GFs, personagens), com confirmação.

**Configurações** — música, som do cursor, idioma (pt-BR, en, es), dificuldade da IA (Fácil/Normal/Difícil), nome, avatar, perfis e **Controles**.

### Controles

Teclado, mouse e joystick (gdx-controllers / SDL, com `gamecontrollerdb.txt`; sem SDL, `/dev/input/js*` como joystick genérico). **Todas as ações são personalizáveis** em Configurações > Controles (1 tecla + 1 botão cada; Del limpa um atalho; Restaurar padrão). A tela mostra o controle detectado e um teste de botões ao vivo. As legendas do rodapé e o Como Jogar mostram os atalhos atuais. Padrões:

| Ação | Teclado | Joystick |
| --- | --- | --- |
| Confirmar | Z | A |
| Cancelar / voltar | Esc | B |
| Selecionar (ordenar / editar) | T | Select |
| Menu / iniciar | Enter | Start |
| Pular intro | S | X |
| Remover / apagar | Del | Y |
| Zoom (ampliar carta) | C | L3 |
| Página esquerda / direita | PgUp / PgDn | L1 / R1 |
| Aleatório | R | R3 |
| Salvar | S | X |
| Revanche (Jogo Rápido) | F5 | Select |
| Jogada automática | F1 | R3 |
| Mover | setas | direcional / analógico |

Campos de texto (nome do perfil, nome do deck, Cheats) abrem um **teclado virtual estilo PS1** quando a entrada vem do joystick. Controle 8BitDo: use o modo X-input (2dc8:3106); no modo HID (3109) o jogo avisa na tela de Controles.

### Persistência

- `.triple-triad.cfg` — vídeo, volume, FPS, fonte, regras, música, som do cursor, idioma, perfil ativo, IA, atalhos (`BIND_KEY_*` / `BIND_PAD_*`)
- `.triple-triad-profiles/` — um arquivo por jogador (decks, álbum, 15 blocos do Memory Card, copas, avatar, troféus)
- arquivos `.ttsave` — export manual do álbum ou do checkpoint do Campeonato (caminho escolhido no diálogo do sistema)
- `.triple-triad.log` — erros

### O que mudamos neste fork

Em relação ao original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1):

- Build **Gradle**; alvo **Java 21**; dependências no Maven Central
- Apresentação **libGDX** 1.14.2 (LWJGL 3); camada `itdelatrisu.tripletriad.gfx`
- Arte e som em `res/` e `cards/` (nada redesenhado)
- Meta-jogo: perfil, menu, construtor, Meu Deck, Campeonato, História, Salvar, vários perfis, dificuldade da IA
- Same / Plus / Combo / Elemental / Sudden Death e as IAs **permanecem os do original**
- Versus local (sem IA), joystick, Cheats e Troféus

### Créditos

Código-base, partida, regras e assets: **Jeffrey Han** ([@itdelatrisu](https://github.com/itdelatrisu/)).

Conceitos e música: Square Enix. Arte in-game: mods FFVIII do MCINDUS — [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0), [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0). Sprites: [UltimeciaFFB](http://ultimeciaffb.deviantart.com/). SFX: [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

### Licença

**GNU GPL versão 3**. Texto em [LICENSE](LICENSE).

---

## English

Desktop Triple Triad, the card game from *Final Fantasy VIII*.

Fork of Jeffrey Han’s **[itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)**. The original shipped a match against the AI with every classic rule. This client is Java 21 + libGDX, with profiles, decks, Quick Game, Championship, My Deck, Save, How to Play, and Settings — without rewriting the capture engine.

Rules: [Triple Triad (wiki)](http://finalfantasy.wikia.com/wiki/Triple_Triad). Versions: [CHANGELOG.md](CHANGELOG.md).

### Requirements and how to run

- JDK **21** or later
- Gradle Wrapper in the repo (`./gradlew`)

```bash
./gradlew run      # from the repo root (uses res/ and cards/)
./gradlew build    # Java 21 bytecode
./gradlew distZip  # playable zip in build/distributions/
./gradlew clean
```

The [v1.3.0 Release](https://github.com/marciliojr/triple-triad/releases/tag/v1.3.0) ships the same zip (`res/` + `cards/` + jar).

### Flow

1. **Intro** — opening video (Skip intro skips it)
2. **Profile** — name and avatar on first run; then profile selection in memory-card blocks
3. **Menu** — Quick Game, Championship, Story, Versus, My Deck, Save, Trophies, Cheats, How to Play, Settings. The active profile (avatar, name, cards and championships) sits in the top-left; Left opens the profile switch
4. **Decks** — build, edit, delete, or pick 5 cards; toggle rules
5. **Championship** — 8 rounds vs the AI
6. **Match** — Cancel asks for confirmation to leave

### Screens

**My Deck** — permanent album (duplicates allowed). While browsing, the right panel shows a medium card with id, name, and element. **C** (or clicking the medium card) opens the match-size overlay. **T** cycles the grid order (catalog, level, value). **X**/Del asks No/Yes before deleting a copy.

**Quick Game** — My Deck (pick 5), random deck from the list, builder, or a saved deck. In the builder and picker: five named slots, grid on the left, cursor card on the right. **R** fills five random cards. **T** sorts the grid. **X** only removes from the hand.

**Save** — export or import the album (My Deck) or the Championship checkpoint as a `.ttsave` file through the system dialog. Import asks No/Yes (default No) before replacing.

**Story** — an original campaign in the world of FFVIII. The player is a Balamb student (not Squall) and chooses the name on the keyboard or the virtual keyboard. Squall advises. Nilo Vann, the announcer, appears as a badge drawn on screen (he does not reuse another face) and introduces each match. One profile holds up to 15 campaigns; creating or deleting one does not touch the album or the others. Each win lets you choose one card from the hand that was played. The meeting's special card is given only on the first win, apart from that choice. A loss does not eliminate you: the rematch is the same table. Points phases show a real table (win = 3; a loss stays off until the rematch). Leaving mid-match returns to the last saved point.

The path is the Garden qualifier, Balamb Town qualifying, Timber groups, Deling main phase, Dollet decisive round, Fisherman's Horizon quarters, Esthar semifinal and the grand final back at Garden. Video exists for `intro_historia` and `chegada_balamb_town`. The other arrivals use the city background until the clips exist (`res/story/IMPORTACAO.txt`).

**Championship** — lobby: New game, Continue, Clear progress (does not wipe the album). Pack if the album has fewer than 5 cards; otherwise pick. **T** sorts the pick grid. **S** / Save (from match 2) writes the run; the notice appears in the footer. A win adds the card to the album and bag; on the pick, each opponent card shows **Owned** (with how many copies are in My Deck) or **New!**. A loss removes it only from the run.

**Championship (1.3.0)** — PS1 Memory Card style save: 15 blocks (save, load, delete); the save stays when the run ends. After a win you can **take no card**; victories play their own music and beating the Championship loops the ending video.

**Versus** — 2 local players with the Open rule. Each picks 5 cards from the current profile's My Deck, with no repeated card in their own hand (both may pick the same cards). Joystick 1 = Player A, joystick 2 or keyboard = B. No cards are won or lost.

**Trophies** — championships won, championship matches and Quick Game wins/losses/draws, per profile.

**Cheats** — type a code: `cid` (all cards), `edea` (Guardian Forces, levels 8-9), `balamb` (characters, level 10), `monster` (monsters, levels 1-7). Each missing card enters My Deck once.

**My Deck → Remove all cards** — everything or by category (monsters, bosses, GFs, characters), with confirmation.

**Settings** — music, cursor sound, language (pt-BR, en, es), AI difficulty (Easy/Normal/Hard), name, avatar, profiles and **Controls**.

### Controls

Keyboard, mouse and joystick (gdx-controllers / SDL with `gamecontrollerdb.txt`; without SDL, `/dev/input/js*` as a generic joystick). **Every action is customizable** in Settings > Controls (1 key + 1 button each; Del clears a binding; Restore defaults). The screen shows the detected controller and a live button test. Footer legends and How to Play show the current bindings. Defaults:

| Action | Keyboard | Joystick |
| --- | --- | --- |
| Confirm | Z | A |
| Cancel / back | Esc | B |
| Select (sort / edit) | T | Select |
| Menu / start | Enter | Start |
| Skip intro | S | X |
| Remove / delete | Del | Y |
| Zoom (enlarge card) | C | L3 |
| Page left / right | PgUp / PgDn | L1 / R1 |
| Random | R | R3 |
| Save | S | X |
| Rematch (Quick Game) | F5 | Select |
| Auto play | F1 | R3 |
| Move | arrows | D-pad / stick |

Text fields (profile name, deck name, Cheats) open a **PS1-style virtual keyboard** when input comes from the joystick. 8BitDo controllers: use X-input mode (2dc8:3106); in HID mode (3109) the Controls screen shows a warning.

### Persistence

- `.triple-triad.cfg` — video, volume, FPS, font, rules, music, cursor sound, language, active profile, AI, bindings (`BIND_KEY_*` / `BIND_PAD_*`)
- `.triple-triad-profiles/` — one file per player (decks, album, 15 Memory Card blocks, cups, avatar, trophies)
- `.ttsave` files — manual export of the album or Championship checkpoint (path chosen in the system dialog)
- `.triple-triad.log` — errors

### What we changed in this fork

Compared with the original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1):

- **Gradle** build; **Java 21** target; Maven Central dependencies
- **libGDX** 1.14.2 (LWJGL 3); layer `itdelatrisu.tripletriad.gfx`
- Art and sound in `res/` and `cards/` (nothing redrawn)
- Meta-game: profile, menu, builder, My Deck, Championship, Story, Save, multiple profiles, AI difficulty
- Same / Plus / Combo / Elemental / Sudden Death and the AIs **remain the original ones**
- Local Versus (no AI), joystick, Cheats and Trophies

### Credits

Base code, match, rules, and assets: **Jeffrey Han** ([@itdelatrisu](https://github.com/itdelatrisu/)).

Concepts and music: Square Enix. In-game art: MCINDUS FFVIII mods — [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0), [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0). Sprites: [UltimeciaFFB](http://ultimeciaffb.deviantart.com/). SFX: [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

### License

**GNU GPL version 3**. Full text in [LICENSE](LICENSE).
