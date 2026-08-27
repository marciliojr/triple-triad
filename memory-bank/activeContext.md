# Active context

Branch de trabalho típica: `feat/libgdx-java17`.

Último marco: 0.6.0 — Meu Deck, save da run, Esc com confirmação, libGDX 1.14.2 e esteira CI/Release na `master`.

## Ao continuar

- Não mexer em `CardResult` / regras de captura sem teste de Same/Plus/Combo/Elemental
- Novas telas: implementar `ui.Screen` e um valor em `GameScreen`
- Textos de UI: adicionar em `I18n` (PT/EN/ES), não hardcode
- Assets novos: soltar em `res/` ou `cards/` e carregar pelo nome do arquivo
- Versus: só quando o produto pedir; o menu já reserva a entrada

## Dívida conhecida

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2
