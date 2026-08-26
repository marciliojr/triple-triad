# System patterns

## Separação

| Camada | Onde | Regra |
| --- | --- | --- |
| Regras / captura | `CardResult`, `Rule`, `Card` (ranks/owner), `Deck` | Não reescrever por estética |
| IA | `ai/*` | Mesmos contratos de mão/tabuleiro/elementos |
| Meta-jogo | `Profile`, `ProfileStore`, `SavedDeck`, `ui/*` | Telas; não alteram captura |
| Partida | `TripleTriad` quando `GameScreen.MATCH` | Loop original (animações, score, turno) |
| Apresentação | `gfx/*`, `GameImage`, `AudioController`, `Spinner`, `Element` (frames) | Só desenho/som/input |

## Telas

`GameScreen`: PROFILE, MENU, DECK_SELECT, DECK_BUILDER, MATCH.

`Screen` define `enter` / `render` / `update` / `keyPressed` / `mousePressed` / `mouseWheelMoved`. `TripleTriad` despacha conforme `currentScreen`.

## Cartas

`Card.loadCardImage()` compõe ranks e elemento no pixmap e escala para `Options.getCardLength()`. `drawSized` é só UI de deck (não afeta o tabuleiro).

## Input

Constantes `gfx.Input` = códigos libGDX. Z/Enter confirmam; Esc no menu/perfil sai, na partida volta ao menu; F5 rematch com `currentPlayerDeckIds`.
