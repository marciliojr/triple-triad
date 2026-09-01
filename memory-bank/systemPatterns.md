# System patterns

## Separação

| Camada | Onde | Regra |
| --- | --- | --- |
| Regras / captura | `CardResult`, `Rule`, `Card` (ranks/owner), `Deck` | Não reescrever por estética |
| IA | `ai/*` | Mesmos contratos de mão/tabuleiro/elementos |
| Meta-jogo | `Profile`, `ProfileStore`, `SavedDeck`, `ChampionshipRun`, `ui/*` | Telas; não alteram captura |
| Partida | `TripleTriad` quando `GameScreen.MATCH` | Loop original (animações, score, turno) |
| Apresentação | `gfx/*`, `GameImage`, `AudioController`, `Spinner`, `Element` (frames) | Só desenho/som/input |

## Telas

`GameScreen`: PROFILE, PROFILES, MENU, DECK_SELECT, DECK_BUILDER, HOW_TO_PLAY, SETTINGS, MY_DECK, SAVE, CHAMPIONSHIP, MATCH.

`Screen` define `enter` / `render` / `update` / `keyPressed` / `mousePressed` / `mouseWheelMoved`. `TripleTriad` despacha conforme `currentScreen`.

Textos de UI passam por `I18n` + `Lang` (PT_BR, EN, ES). Nomes de regras em `Rule.getDisplayName()`.

Campeonato: `ChampionshipRun` (round, mãos, bag da run, IA por round). Álbum em `Profile` (`COLLECTION`). Save da run: `RUN_ROUND`, `RUN_WINS`, `RUN_HAND`, `RUN_OPPONENT`, `RUN_BAG`. `championshipOpponentAI` em `TripleTriad` não grava no cfg. Aviso de save fica em `ChampionshipScreen` (`saveNoticeUntil`).

Export/import: `SaveTransfer` (AWT `FileDialog` no EDT, com janela dona e `dispose`; formato `KEY = value` com `KIND = DECK` ou `CHAMPIONSHIP`). Depois do diálogo, `Input.suppress` ignora Z/Enter/clique residual. `SaveScreen` escolhe o tipo, exporta ou importa; import aplica `Profile.replaceCollection` ou os `RUN_*` depois da confirmação, e chama `saveProfile()`. Decks nomeados do Jogo Rápido não entram no `.ttsave`.

## Áudio

`AudioController.applyMusic()` liga/pausa o BGM. `AudioController.playCursor()` toca o SELECT só se `Options.isCursorSoundEnabled()`.

## Cartas

`Card.loadCardImage()` compõe ranks e elemento no pixmap e escala para `Options.getCardLength()`. `drawSized` é só UI de deck (não afeta o tabuleiro).

`Deck` lê o level da 5ª coluna de `cards/deck.txt` (`tokens[4]`). `CardSort` (enum + modo estático da sessão) produz views: cópia do catálogo ou permutação de índices da bag. Construtor, Meu Deck e pick do Campeonato usam a view para cursor/preview/hit; `selected` continua com IDs ou índices originais.

## Input

Constantes `gfx.Input` = códigos libGDX. Mapa da UI: **Z** seleciona, **X**/Del remove, **C** amplia (`Ui.drawCardPreview`), **R** preenche 5 cartas aleatórias no construtor e nos picks (Jogo Rápido / Campeonato), **T** cicla `CardSort` (catálogo → nível → valor) no construtor, na galeria/pick de Meu Deck e no pick do Campeonato, **Esc** volta. T é ignorada em preview, confirmação e nomeação. Nos menus Z e Enter confirmam. Na galeria Meu Deck, o painel direito mostra a carta média do cursor (como no pick); C continua overlay; X/Del pede confirmação (Não / Sim; default Não) antes de apagar uma cópia do álbum; no construtor e nos picks X só tira da mão de 5. Na partida, Esc abre overlay de confirmação (Não / Sim; default Não); **X** cancela a casa do tabuleiro. F5 rematch só no Jogo Rápido.
