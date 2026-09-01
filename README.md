# Triple Triad

[Português (Brasil)](#português-brasil) · [English](#english)

**1.1.1** — diálogo de save que fecha de verdade. Zip na [GitHub Release](https://github.com/marciliojr/triple-triad/releases/tag/v1.1.1).

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

A [Release v1.1.1](https://github.com/marciliojr/triple-triad/releases/tag/v1.1.1) traz o mesmo zip (`res/` + `cards/` + jar).

### Fluxo

1. **Perfil** — nome na primeira execução
2. **Menu** — Jogo Rápido, Campeonato, Meu Deck, Salvar, Como Jogar, Configurações
3. **Decks** — montar, editar, apagar ou escolher 5 cartas; ligar/desligar regras
4. **Campeonato** — 8 rounds contra a IA
5. **Partida** — Esc pede confirmação para sair

### Telas

**Meu Deck** — álbum permanente (duplicatas ok). Ao navegar, o painel direito mostra a carta média com id, nome e elemento. **C** (ou clique na carta média) abre o overlay em tamanho de partida. **T** cicla a ordem do grid (catálogo, nível, valor). **X**/Del pede Não/Sim antes de apagar uma cópia.

**Jogo Rápido** — Meu Deck (pick 5), deck aleatório da lista, construtor ou deck salvo. No construtor e no pick: 5 slots com nome/elemento, grade à esquerda, painel da carta do cursor à direita. **R** preenche 5 aleatórias. **T** ordena o grid. **X** só tira da mão.

**Salvar** — exportar ou importar o álbum (Meu Deck) ou o checkpoint do Campeonato em um arquivo `.ttsave` pelo diálogo do sistema. Importar pede Não/Sim (default Não) antes de substituir.

**Campeonato** — lobby: Novo jogo, Continuar, Apagar progresso (não zera o álbum). Pack se o álbum tiver menos de 5 cartas; senão pick. **T** ordena o grid no pick. **S** / Salvar (da 2ª luta em diante) grava a run; o aviso aparece no rodapé. Vitória adiciona a carta ao álbum e à bag; derrota tira só da run.

**Configurações** — música, som do cursor, idioma (pt-BR, en, es), dificuldade da IA (Fácil/Normal/Difícil), nome e perfis.

### Controles

Além do mouse:

| Ação | Teclas |
| --- | --- |
| Movimento | setas |
| Selecionar / confirmar | Z (Enter confirma a tela quando for distinto, ex.: jogar com 5) |
| Ampliar carta | C (Esc/C/clique fecha o overlay) |
| Deck aleatório (construtor / pick) | R |
| Ordenar o grid (nível / valor / catálogo) | T |
| Remover da mão | X ou Del (só os 5 slots) |
| Remover do álbum | X ou Del, depois Não/Sim (default Não) |
| Cancelar casa (partida) | X ou Backspace |
| Rematch (Jogo Rápido) | F5 |
| Lance automático | F1 |
| Voltar / sair | Esc (na partida, Não/Sim; default Não) |

Lista de decks: **Z**/Enter joga, **C** amplia as minis, **E** edita, **X**/Del apaga. Configurações: Enter ou esquerda/direita altera; **Z** confirma.

### Persistência

- `.triple-triad.cfg` — vídeo, volume, FPS, fonte, regras, música, som do cursor, idioma, perfil ativo, IA
- `.triple-triad-profiles/` — um arquivo por jogador (decks, álbum, run do Campeonato, copas)
- arquivos `.ttsave` — export manual do álbum ou do checkpoint do Campeonato (caminho escolhido no diálogo do sistema)
- `.triple-triad.log` — erros

### O que mudamos neste fork

Em relação ao original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1):

- Build **Gradle**; alvo **Java 21**; dependências no Maven Central
- Apresentação **libGDX** 1.14.2 (LWJGL 3); camada `itdelatrisu.tripletriad.gfx`
- Arte e som em `res/` e `cards/` (nada redesenhado)
- Meta-jogo: perfil, menu, construtor, Meu Deck, Campeonato, Salvar, vários perfis, dificuldade da IA
- Same / Plus / Combo / Elemental / Sudden Death e as IAs **permanecem os do original**
- Versus não está no menu

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

The [v1.1.1 Release](https://github.com/marciliojr/triple-triad/releases/tag/v1.1.1) ships the same zip (`res/` + `cards/` + jar).

### Flow

1. **Profile** — name on first run
2. **Menu** — Quick Game, Championship, My Deck, Save, How to Play, Settings
3. **Decks** — build, edit, delete, or pick 5 cards; toggle rules
4. **Championship** — 8 rounds vs the AI
5. **Match** — Esc asks for confirmation to leave

### Screens

**My Deck** — permanent album (duplicates allowed). While browsing, the right panel shows a medium card with id, name, and element. **C** (or clicking the medium card) opens the match-size overlay. **T** cycles the grid order (catalog, level, value). **X**/Del asks No/Yes before deleting a copy.

**Quick Game** — My Deck (pick 5), random deck from the list, builder, or a saved deck. In the builder and picker: five named slots, grid on the left, cursor card on the right. **R** fills five random cards. **T** sorts the grid. **X** only removes from the hand.

**Save** — export or import the album (My Deck) or the Championship checkpoint as a `.ttsave` file through the system dialog. Import asks No/Yes (default No) before replacing.

**Championship** — lobby: New game, Continue, Clear progress (does not wipe the album). Pack if the album has fewer than 5 cards; otherwise pick. **T** sorts the pick grid. **S** / Save (from match 2) writes the run; the notice appears in the footer. A win adds the card to the album and bag; a loss removes it only from the run.

**Settings** — music, cursor sound, language (pt-BR, en, es), AI difficulty (Easy/Normal/Hard), name and profiles.

### Controls

Besides the mouse:

| Action | Keys |
| --- | --- |
| Move | arrows |
| Select / confirm | Z (Enter confirms the screen when that is a distinct action, e.g. play with 5) |
| Enlarge card | C (Esc/C/click closes the overlay) |
| Random deck (builder / pick) | R |
| Sort the grid (level / value / catalog) | T |
| Remove from hand | X or Del (the 5 slots only) |
| Remove from album | X or Del, then No/Yes (default No) |
| Cancel square (match) | X or Backspace |
| Rematch (Quick Game) | F5 |
| Auto play | F1 |
| Back / quit | Esc (in a match, No/Yes; default No) |

Deck list: **Z**/Enter plays, **C** enlarges the minis, **E** edits, **X**/Del deletes. Settings: Enter or left/right changes a value; **Z** confirms.

### Persistence

- `.triple-triad.cfg` — video, volume, FPS, font, rules, music, cursor sound, language, active profile, AI
- `.triple-triad-profiles/` — one file per player (decks, album, Championship run, cups)
- `.ttsave` files — manual export of the album or Championship checkpoint (path chosen in the system dialog)
- `.triple-triad.log` — errors

### What we changed in this fork

Compared with the original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1):

- **Gradle** build; **Java 21** target; Maven Central dependencies
- **libGDX** 1.14.2 (LWJGL 3); layer `itdelatrisu.tripletriad.gfx`
- Art and sound in `res/` and `cards/` (nothing redrawn)
- Meta-game: profile, menu, builder, My Deck, Championship, Save, multiple profiles, AI difficulty
- Same / Plus / Combo / Elemental / Sudden Death and the AIs **remain the original ones**
- Versus is not on the menu

### Credits

Base code, match, rules, and assets: **Jeffrey Han** ([@itdelatrisu](https://github.com/itdelatrisu/)).

Concepts and music: Square Enix. In-game art: MCINDUS FFVIII mods — [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0), [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0). Sprites: [UltimeciaFFB](http://ultimeciaffb.deviantart.com/). SFX: [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

### License

**GNU GPL version 3**. Full text in [LICENSE](LICENSE).
