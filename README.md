# Triple Triad

[Português (Brasil)](#português-brasil) · [English](#english)

---

## Português (Brasil)

Implementação desktop do Triple Triad, o jogo de cartas de *Final Fantasy VIII*.

Este repositório é um **fork** do projeto original de Jeffrey Han:
**[itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)**.

O original entregava a partida contra a IA, com todas as regras clássicas. Daqui para frente o cliente está em Java 21 + libGDX, com perfil, decks, Jogo Rápido, Campeonato, Como Jogar e Configurações — sem reescrever o motor de captura.

Regras e tutorial: [Triple Triad (wiki)](http://finalfantasy.wikia.com/wiki/Triple_Triad).

### Créditos do fork

O código-base, o desenho da partida, as regras (Same, Plus, Combo, Elemental, Sudden Death), as IAs e a organização dos assets vêm do trabalho de **Jeffrey Han** ([@itdelatrisu](https://github.com/itdelatrisu/)).

Conceitos e designs do jogo são da Square Enix. A música é propriedade da Square Enix.

Imagens de jogo a partir dos mods de FFVIII do MCINDUS:

- [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0)
- [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0)

Sprites dos personagens: [UltimeciaFFB](http://ultimeciaffb.deviantart.com/). Efeitos sonoros extraídos por [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

### O que mudamos neste fork

Em relação ao original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1, partida que começava na hora):

**Build e plataforma**
- Build oficial passou a ser **Gradle** (`./gradlew run` / `./gradlew build`); Ant deixou de ser o caminho suportado
- Linguagem-alvo: **Java 21** (o original era Java 7; o passo intermediário deste fork foi Java 17)
- Dependências no Maven Central; natives do SO baixadas pelo Gradle

**Apresentação**
- Slick2D + LWJGL 2 e a pasta `native/` foram trocados por **libGDX** 1.14.2 (LWJGL 3 no backend)
- Camada fina em `itdelatrisu.tripletriad.gfx` para janela, input, sprites, fonte TTF com acentos PT-BR/ES e áudio
- Arte e som continuam em `res/` e `cards/` (nada redesenhado)

**Meta-jogo (não existia no original)**
- Perfil obrigatório na primeira execução (`.triple-triad-profiles/`)
- Menu principal: Jogo Rápido, Campeonato, Meu Deck, Como Jogar, Configurações
- Construtor e seletor de decks de 5 cartas (criar, editar, apagar, jogar); Jogo Rápido também pode usar Meu Deck ou deck aleatório
- Regras da partida ligadas/desligadas na tela de Jogo Rápido e no lobby do Campeonato
- Meu Deck: álbum permanente (cartas ganhas, duplicatas ok; **C** amplia; **X**/Del remove)
- Vários perfis e dificuldade da IA (Fácil/Normal/Difícil) em Configurações
- Interface em português (Brasil), inglês e espanhol

**Partida**
- Same / Plus / Combo / Elemental / Sudden Death e as IAs **permanecem os do original**
- Esc na partida pede confirmação antes de voltar ao menu (evita toque acidental); F5 refaz a partida só no Jogo Rápido

Detalhe das versões: [CHANGELOG.md](CHANGELOG.md).

### Requisitos

- JDK **21** ou superior
- Gradle Wrapper no repositório (`./gradlew`); não é preciso instalar Gradle à parte

### Como rodar

Na raiz do projeto:

```bash
./gradlew run
```

O diretório de trabalho é a raiz: sprites, fontes e áudio em `res/`, catálogo de cartas em `cards/`.

```bash
./gradlew build   # compila (bytecode Java 21)
./gradlew distZip # zip jogável em build/distributions/ (inclui res/ e cards/)
./gradlew clean
```

### Fluxo do jogo

1. **Perfil** — nome na primeira execução
2. **Menu** — Jogo Rápido, Campeonato, Meu Deck, Como Jogar, Configurações
3. **Decks** — montar, editar, apagar ou escolher um deck de 5 cartas; ligar/desligar regras
4. **Campeonato** — 8 rounds contra a IA; Meu Deck é o álbum; save da run no pick
5. **Partida** — contra a IA; Esc pede confirmação para sair

Vídeo, volume, FPS, fonte, regras, música, som do cursor, idioma, perfil ativo e tipo da IA: `.triple-triad.cfg` (criado na primeira execução). Perfis, decks do Jogo Rápido e coleção do Campeonato: `.triple-triad-profiles/`. Erros: `.triple-triad.log`.

Em **Configurações** dá para ligar/desligar a música de fundo e o som do cursor, escolher o idioma (pt-BR, inglês, espanhol) e a dificuldade da IA (Fácil/Normal/Difícil).

No **Campeonato**, o lobby tem **Novo jogo**, **Continuar** e **Apagar progresso**. Se Meu Deck estiver vazio, a 1ª partida cria o pack e grava no álbum; se já houver 5+ cartas, Novo jogo abre o pick. Continuar retoma a partida salva. **Apagar progresso** limpa só a run (`RUN_*`), não o deck. Vitória adiciona a carta ao álbum e à bag da run; derrota tira só da run. **Salvar** no pick grava a partida e mostra que o progresso foi salvo. Em **Meu Deck** **C** amplia e **X**/Del remove uma cópia.

### Controles

Além do mouse:

| Ação | Teclas |
| --- | --- |
| Movimento | setas |
| Selecionar / confirmar | Z (Enter confirma a tela quando for distinto, ex.: jogar com 5 cartas) |
| Ampliar carta | C (telas de deck; Esc/C/clique fecha o preview) |
| Remover | X ou Del (álbum, mão de 5, deck salvo) |
| Cancelar casa (só na partida) | X ou Backspace |
| Rematch (mesmo deck) | F5 |
| Lance automático (IA do jogador) | F1 |
| Voltar / sair | Esc (na partida, pede confirmação) |

No construtor e no pick: **Z** seleciona, **X** tira da mão, **C** amplia, **S** salva (se houver), **Enter** joga. Na lista de decks: **Z**/Enter joga, **C** amplia as minis, **E** edita, **X**/Del apaga. Em Configurações: **Enter** ou esquerda/direita altera; **Z** também confirma. No Campeonato: **Novo jogo**, **Continuar** ou **Apagar progresso**; da 2ª em diante **S** / **Salvar** grava a run. Na partida: **Esc** abre Não/Sim (default Não); **X** cancela a casa escolhida.

### Licença

Licença **GNU GPL versão 3**, igual ao projeto original. Texto completo em [LICENSE](LICENSE).

---

## English

Desktop Triple Triad, the card game from *Final Fantasy VIII*.

This repository is a **fork** of Jeffrey Han’s original project:
**[itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)**.

The original shipped a match against the AI with every classic rule. This client is Java 21 + libGDX, with profiles, decks, Quick Game, Championship, How to Play, and Settings — without rewriting the capture engine.

Rules and tutorial: [Triple Triad (wiki)](http://finalfantasy.wikia.com/wiki/Triple_Triad).

### Fork credits

The match layout, capture rules (Same, Plus, Combo, Elemental, Sudden Death), AIs, and asset layout come from **Jeffrey Han** ([@itdelatrisu](https://github.com/itdelatrisu/)).

Game concepts and designs are Square Enix. The music is Square Enix property.

In-game art from MCINDUS’s FFVIII mods:

- [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0)
- [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0)

Character sprites: [UltimeciaFFB](http://ultimeciaffb.deviantart.com/). Sound effects extracted by [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

### What we changed in this fork

Compared with the original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1, match starting immediately):

**Build and platform**
- Official build is **Gradle** (`./gradlew run` / `./gradlew build`); Ant is no longer the supported path
- Language target: **Java 21** (the original was Java 7; this fork’s intermediate step was Java 17)
- Dependencies from Maven Central; OS natives downloaded by Gradle

**Presentation**
- Slick2D + LWJGL 2 and the `native/` folder were replaced by **libGDX** 1.14.2 (LWJGL 3 backend)
- Thin layer in `itdelatrisu.tripletriad.gfx` for window, input, sprites, TTF with PT-BR/ES accents, and audio
- Art and sound stay in `res/` and `cards/` (nothing redrawn)

**Meta-game (not in the original)**
- Required profile on first run (`.triple-triad-profiles/`)
- Main menu: Quick Game, Championship, My Deck, How to Play, Settings
- 5-card deck builder and selector (create, edit, delete, play); Quick Game can also use My Deck or a random deck
- Match rules toggled on the Quick Game screen and the Championship lobby
- My Deck: permanent album (won cards, duplicates allowed; **C** enlarges; **X**/Del removes)
- Multiple profiles and AI difficulty (Easy/Normal/Hard) in Settings
- UI in Brazilian Portuguese, English, and Spanish

**Match**
- Same / Plus / Combo / Elemental / Sudden Death and the AIs **remain the original ones**
- Esc during a match asks for confirmation before returning to the menu (avoids accidental exit); F5 rematches only in Quick Game

Version details: [CHANGELOG.md](CHANGELOG.md).

### Requirements

- JDK **21** or later
- Gradle Wrapper in the repo (`./gradlew`); no separate Gradle install needed

### How to run

From the project root:

```bash
./gradlew run
```

The working directory is the root: sprites, fonts, and audio in `res/`, card catalog in `cards/`.

```bash
./gradlew build   # compile (Java 21 bytecode)
./gradlew distZip # playable zip in build/distributions/ (includes res/ and cards/)
./gradlew clean
```

### Game flow

1. **Profile** — name on first run
2. **Menu** — Quick Game, Championship, My Deck, How to Play, Settings
3. **Decks** — build, edit, delete, or pick a 5-card deck; toggle rules
4. **Championship** — 8 rounds vs the AI; My Deck is the album; save the run on the pick screen
5. **Match** — vs the AI; Esc asks for confirmation to leave

Video, volume, FPS, font, rules, music, cursor sound, language, active profile, and AI type: `.triple-triad.cfg` (created on first run). Profiles, Quick Game decks, and Championship collection: `.triple-triad-profiles/`. Errors: `.triple-triad.log`.

In **Settings** you can toggle background music and cursor sound, pick the language (pt-BR, English, Spanish), and set AI difficulty (Easy/Normal/Hard).

In **Championship**, the lobby has **New game**, **Continue**, and **Clear progress**. If My Deck is empty, match 1 creates a pack and writes it to the album; if there are already 5+ cards, New game opens the picker. Continue resumes the saved match. **Clear progress** wipes only the run (`RUN_*`), not the album. A win adds the card to the album and the run bag; a loss removes it only from the run. **Save** on the pick screen writes the match and shows that progress was saved. In **My Deck**, **C** enlarges and **X**/Del removes one copy.

### Controls

Besides the mouse:

| Action | Keys |
| --- | --- |
| Move | arrows |
| Select / confirm | Z (Enter confirms the screen when that is a distinct action, e.g. play with 5 cards) |
| Enlarge card | C (deck screens; Esc/C/click closes the preview) |
| Remove | X or Del (album, 5-card hand, saved deck) |
| Cancel square (match only) | X or Backspace |
| Rematch (same deck) | F5 |
| Auto play (player AI) | F1 |
| Back / quit | Esc (in a match, asks for confirmation) |

In the builder and picker: **Z** selects, **X** removes from the hand, **C** enlarges, **S** saves (when available), **Enter** plays. On the deck list: **Z**/Enter plays, **C** enlarges the minis, **E** edits, **X**/Del deletes. In Settings: **Enter** or left/right changes a value; **Z** also confirms. In Championship: **New game**, **Continue**, or **Clear progress**; from match 2 onward **S** / **Save** writes the run. In a match: **Esc** opens No/Yes (default No); **X** cancels the chosen square.

### License

**GNU GPL version 3**, same as the original project. Full text in [LICENSE](LICENSE).
