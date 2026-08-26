# Triple Triad

Implementação desktop do Triple Triad, o jogo de cartas de *Final Fantasy VIII*.

Este repositório é um **fork** do projeto original de Jeffrey Han:
**[itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)**.

O original entregava a partida contra a IA, com todas as regras clássicas. Daqui para frente o cliente está em Java 21 + libGDX, com perfil, decks e Jogo Rápido — sem reescrever o motor de captura.

Regras e tutorial: [Triple Triad (wiki)](http://finalfantasy.wikia.com/wiki/Triple_Triad).

## Créditos do fork

O código-base, o desenho da partida, as regras (Same, Plus, Combo, Elemental, Sudden Death), as IAs e a organização dos assets vêm do trabalho de **Jeffrey Han** ([@itdelatrisu](https://github.com/itdelatrisu/)).

Conceitos e designs do jogo são da Square Enix. A música é propriedade da Square Enix.

Imagens de jogo a partir dos mods de FFVIII do MCINDUS:

- [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0)
- [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0)

Sprites dos personagens: [UltimeciaFFB](http://ultimeciaffb.deviantart.com/). Efeitos sonoros extraídos por [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

## O que mudamos neste fork

Em relação ao original (Java 7, Ant/Eclipse, Slick2D + LWJGL 2.9.1, partida que começava na hora):

**Build e plataforma**
- Build oficial passou a ser **Gradle** (`./gradlew run` / `./gradlew build`); Ant deixou de ser o caminho suportado
- Linguagem-alvo: **Java 21** (o original era Java 7; o passo intermediário deste fork foi Java 17)
- Dependências no Maven Central; natives do SO baixadas pelo Gradle

**Apresentação**
- Slick2D + LWJGL 2 e a pasta `native/` foram trocados por **libGDX** 1.13.1 (LWJGL 3 no backend)
- Camada fina em `itdelatrisu.tripletriad.gfx` para janela, input, sprites, fonte TTF com acentos PT-BR e áudio
- Arte e som continuam em `res/` e `cards/` (nada redesenhado)

**Meta-jogo (não existia no original)**
- Perfil obrigatório na primeira execução (`.triple-triad-profile`)
- Menu principal: Jogo Rápido (Campeonato e Versus ainda como “Em breve”)
- Construtor e seletor de decks de 5 cartas (criar, editar, apagar, jogar)

**Partida**
- Same / Plus / Combo / Elemental / Sudden Death e as IAs **permanecem os do original**
- Esc volta ao menu em vez de fechar o jogo; F5 refaz a partida com o mesmo deck

Detalhe das versões: [CHANGELOG.md](CHANGELOG.md).

## Requisitos

- JDK **21** ou superior
- Gradle Wrapper no repositório (`./gradlew`); não é preciso instalar Gradle à parte

## Como rodar

Na raiz do projeto:

```bash
./gradlew run
```

O diretório de trabalho é a raiz: sprites, fontes e áudio em `res/`, catálogo de cartas em `cards/`.

```bash
./gradlew build   # compila (bytecode Java 21)
./gradlew clean
```

## Fluxo do jogo

1. **Perfil** — nome na primeira execução
2. **Menu** — Jogo Rápido
3. **Decks** — montar, editar, apagar ou escolher um deck de 5 cartas
4. **Partida** — contra a IA

Vídeo, volume, FPS, fonte e regras: `.triple-triad.cfg` (criado na primeira execução). Erros: `.triple-triad.log`.

## Controles

Além do mouse:

| Ação | Teclas |
| --- | --- |
| Movimento | setas |
| Selecionar / confirmar | Z ou Enter |
| Cancelar | X ou Backspace |
| Rematch (mesmo deck) | F5 |
| Lance automático (IA do jogador) | F1 |
| Voltar / sair | Esc |

No construtor de deck: **Z** adiciona/remove carta, **S** salva, **Enter** joga. Na lista: **E** edita, **Del** apaga.

## Licença

Licença **GNU GPL versão 3**, igual ao projeto original. Texto completo em [LICENSE](LICENSE).
