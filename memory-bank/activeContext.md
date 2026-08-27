# Active context

Último marco: **1.0.0** — primeira versão jogável. Seleção híbrida (nome/elemento, painel médio, **R**), preview na galeria Meu Deck, confirmação ao apagar do álbum, painéis sem vazamento de texto, aviso de save no rodapé.

Branch de trabalho típica após o marco: `master` na 1.0.0 (`v1.0.0`).

## Ao continuar

- Não mexer em `CardResult` / regras de captura sem teste de Same/Plus/Combo/Elemental
- Novas telas: implementar `ui.Screen` e um valor em `GameScreen`
- Textos de UI: adicionar em `I18n` (PT/EN/ES), não hardcode
- Assets novos: soltar em `res/` ou `cards/` e carregar pelo nome do arquivo
- Versus: só quando o produto pedir; não está no menu

## Dívida conhecida

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2
