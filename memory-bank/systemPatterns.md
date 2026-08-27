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

`GameScreen`: PROFILE, MENU, DECK_SELECT, DECK_BUILDER, HOW_TO_PLAY, SETTINGS, MY_DECK, CHAMPIONSHIP, MATCH.

`Screen` define `enter` / `render` / `update` / `keyPressed` / `mousePressed` / `mouseWheelMoved`. `TripleTriad` despacha conforme `currentScreen`.

Textos de UI passam por `I18n` + `Lang` (PT_BR, EN, ES). Nomes de regras em `Rule.getDisplayName()`.

Campeonato: `ChampionshipRun` (round, mãos, bag da run, IA por round). Álbum em `Profile` (`COLLECTION`). Save da run: `RUN_ROUND`, `RUN_WINS`, `RUN_HAND`, `RUN_OPPONENT`, `RUN_BAG`. `championshipOpponentAI` em `TripleTriad` não grava no cfg. Aviso de save fica em `ChampionshipScreen` (`saveNoticeUntil`).

## Áudio

`AudioController.applyMusic()` liga/pausa o BGM. `AudioController.playCursor()` toca o SELECT só se `Options.isCursorSoundEnabled()`.

## Cartas

`Card.loadCardImage()` compõe ranks e elemento no pixmap e escala para `Options.getCardLength()`. `drawSized` é só UI de deck (não afeta o tabuleiro).

## Input

Constantes `gfx.Input` = códigos libGDX. Mapa da UI: **Z** seleciona, **X**/Del remove, **C** amplia (`Ui.drawCardPreview`), **Esc** volta. Nos menus Z e Enter confirmam. Na partida, Esc abre overlay de confirmação (Não / Sim; default Não); **X** cancela a casa do tabuleiro. F5 rematch só no Jogo Rápido.
