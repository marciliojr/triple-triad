# Active context

Último marco: **1.2.0** — troca do Campeonato mostra se a carta já está no Meu Deck; NEUTRAL sem rótulo (sem quadrado vazio).

Branch de trabalho típica: `master` na 1.2.0 (`v1.2.0`).

## Ao continuar

- Não mexer em `CardResult` / regras de captura sem teste de Same/Plus/Combo/Elemental
- Novas telas: implementar `ui.Screen` e um valor em `GameScreen`
- Textos de UI: adicionar em `I18n` (PT/EN/ES), não hardcode
- Fonte é gerada com `DEFAULT_CHARS` + acentos (Latin-1): caracteres fora disso (ex.: travessão U+2014) viram quadrado
- Assets novos: soltar em `res/` ou `cards/` e carregar pelo nome do arquivo
- Versus: só quando o produto pedir; não está no menu
- Ordenação de cartas: só via `CardSort` (view); não mutar catálogo, álbum nem bag da run
- Export/import: só via `SaveTransfer` + `SaveScreen`; não misturar com o `.profile` automático

## Dívida conhecida

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2
