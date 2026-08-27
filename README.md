# Triple Triad

Implementação desktop do Triple Triad, o jogo de cartas de *Final Fantasy VIII*.

Este repositório é um **fork** do projeto original de Jeffrey Han:
**[itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)**.

O original entregava a partida contra a IA, com todas as regras clássicas. Daqui para frente o cliente está em Java 21 + libGDX, com perfil, decks, Jogo Rápido, Campeonato, Como Jogar e Configurações — sem reescrever o motor de captura.

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
./gradlew distZip # zip jogável em build/distributions/ (inclui res/ e cards/)
./gradlew clean
```

## Fluxo do jogo

1. **Perfil** — nome na primeira execução
2. **Menu** — Jogo Rápido, Campeonato, Meu Deck, Como Jogar, Configurações
3. **Decks** — montar, editar, apagar ou escolher um deck de 5 cartas; ligar/desligar regras
4. **Campeonato** — 8 rounds contra a IA; Meu Deck é o álbum; save da run no pick
5. **Partida** — contra a IA; Esc pede confirmação para sair

Vídeo, volume, FPS, fonte, regras, música, som do cursor, idioma, perfil ativo e tipo da IA: `.triple-triad.cfg` (criado na primeira execução). Perfis, decks do Jogo Rápido e coleção do Campeonato: `.triple-triad-profiles/`. Erros: `.triple-triad.log`.

Em **Configurações** dá para ligar/desligar a música de fundo e o som do cursor, escolher o idioma (pt-BR, inglês, espanhol) e a dificuldade da IA (Fácil/Normal/Difícil).

No **Campeonato**, o lobby tem **Novo jogo**, **Continuar** e **Apagar progresso**. Se Meu Deck estiver vazio, a 1ª partida cria o pack e grava no álbum; se já houver 5+ cartas, Novo jogo abre o pick. Continuar retoma a partida salva. **Apagar progresso** limpa só a run (`RUN_*`), não o deck. Vitória adiciona a carta ao álbum e à bag da run; derrota tira só da run. **Salvar** no pick grava a partida e mostra que o progresso foi salvo. Em **Meu Deck** **C** amplia e **X**/Del remove uma cópia.

## Controles

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

## Licença

Licença **GNU GPL versão 3**, igual ao projeto original. Texto completo em [LICENSE](LICENSE).
