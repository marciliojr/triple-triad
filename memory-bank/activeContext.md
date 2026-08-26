# Active context

Branch de trabalho típica: `feat/libgdx-java17`.

Último marco: cliente libGDX + Java 17 + Gradle + perfil/decks/Jogo Rápido, validado com `./gradlew run` (janela abre, sessão fecha com código 0).

## Ao continuar

- Não mexer em `CardResult` / regras de captura sem teste de Same/Plus/Combo/Elemental
- Novas telas: implementar `ui.Screen` e um valor em `GameScreen`
- Assets novos: soltar em `res/` ou `cards/` e carregar pelo nome do arquivo
- Campeonato / Versus: só quando o produto pedir; o menu já reserva as entradas

## Dívida conhecida

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2
