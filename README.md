# Triple Triad

Implementação desktop do Triple Triad, o jogo de cartas de *Final Fantasy VIII*: regras clássicas (Same, Plus, Combo, Elemental, Sudden Death), IA e um meta-jogo local (perfil, decks e Jogo Rápido).

Regras e tutorial oficiais: [Triple Triad (wiki)](http://finalfantasy.wikia.com/wiki/Triple_Triad).

## Requisitos

- JDK **17** ou superior
- Gradle Wrapper incluso (`./gradlew`); não é preciso instalar Gradle

## Como rodar

Na raiz do repositório:

```bash
./gradlew run
```

O working directory é a raiz do projeto: sprites, fontes e áudio vêm de `res/`, o catálogo de cartas de `cards/`.

Outros alvos úteis:

```bash
./gradlew build   # compila (bytecode Java 17)
./gradlew clean
```

O build oficial é **Gradle**. O `build.xml` (Ant/Eclipse + Java 7 + Slick2D) ficou no histórico e não é mais o caminho suportado.

## Fluxo do jogo

1. **Perfil** — nome obrigatório na primeira execução (salvo em `.triple-triad-profile`)
2. **Menu** — Jogo Rápido (Campeonato e Versus ainda bloqueados)
3. **Decks** — criar, editar, apagar ou escolher um deck de 5 cartas
4. **Partida** — contra a IA, com captura Same / Plus / Combo / Elemental intacta

Configuração de vídeo, volume, FPS, fonte e regras: `.triple-triad.cfg` (gerado na primeira execução). Log de erros: `.triple-triad.log`.

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

No construtor de deck: **Z** adiciona/remove carta, **S** salva, **Enter** joga, **E** edita na lista, **Del** apaga um deck.

## Stack

- Java 17
- [libGDX](https://libgdx.com/) 1.13.1 (backend LWJGL 3; natives por SO via Maven)
- Camada fina em `itdelatrisu.tripletriad.gfx` (API no estilo Slick2D)
- Motor de regras em `CardResult`, `Rule`, `Card`, `Deck` e `ai/`

## Credits

This software was created by Jeffrey Han
([@itdelatrisu](https://github.com/itdelatrisu/)). All game concepts and
designs are based on work by Square Enix, and music is property of Square Enix.

Game images used in this project are part of FFVIII mods by MCINDUS:

 * [Tripod v1.1](http://forums.qhimm.com/index.php?topic=15301.0)
 * [SeeD Reborn v3.2](http://forums.qhimm.com/index.php?topic=15320.0)

Character sprites were created by
[UltimeciaFFB](http://ultimeciaffb.deviantart.com/), and sound effects were
extracted by [TekkamanChronos](http://www.youtube.com/watch?v=xKzxcJLiitQ).

## License

**This software is licensed under GNU GPL version 3.**
You can find the full text of the license [here](LICENSE).
