# Active context

Branch de trabalho típica: `feat/libgdx-java17`.

Último marco: alvo **Java 21** (bytecode major 65). README, CHANGELOG 0.3.0 e memory bank atualizados.

## Ao continuar

- Não mexer em `CardResult` / regras de captura sem teste de Same/Plus/Combo/Elemental
- Novas telas: implementar `ui.Screen` e um valor em `GameScreen`
- Assets novos: soltar em `res/` ou `cards/` e carregar pelo nome do arquivo
- Campeonato / Versus: só quando o produto pedir; o menu já reserva as entradas

## Dívida conhecida

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2
