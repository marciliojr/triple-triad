# Active context

Último marco: **1.2.0** — troca do Campeonato mostra se a carta já está no Meu Deck; NEUTRAL sem rótulo (sem quadrado vazio).

Branch de trabalho típica: `master` na 1.2.0 (`v1.2.0`).

## Em andamento

- Branch `feat/1.3.0-video-endgame`: vídeo em loop na zerada do Campeonato (`gfx.VideoLoop` + `ChampionshipScreen.MODE_WON`). Fonte `res/endgame.mp4`; o jogo usa `res/endgame/frame_NNN.jpg` + `res/endgame.ogg`. Para trocar o vídeo: `ffmpeg -i endgame.mp4 -vf scale=640:360 -r 24 -q:v 7 res/endgame/frame_%03d.jpg` e `ffmpeg -i endgame.mp4 -vn -c:a libvorbis -q:a 4 res/endgame.ogg`

- Mesma branch: abertura `ui.IntroScreen` (`GameScreen.INTRO`, `VideoLoop.setLooping(false)`, S pula, `AudioController.init(false)` + `introFinished()` liga a BGM) e save estilo Memory Card PS1 (`ui.MemoryCardScreen`, `GameScreen.MEMORY_CARD`). Saves em `Profile` como `ChampionshipSlot[15]` gravados no perfil como `SLOT.N.ROUND/WINS/HAND/OPPONENT/BAG/RULES/PLAY_MS/SAVED_AT`; `RUN_*` antigo migra para o bloco 01 em `migrateLegacyRun()`. Fim do auto-delete: `endChampionshipSave()` só grava o álbum

- Mesma branch: menu Cheats (`ui.CheatsScreen`, `GameScreen.CHEATS`). Categorias pelo nível em `cards/deck.txt`: 1-7 monstros (6-7 chefes contam como monstros), 8-9 GFs, 10 personagens. Adiciona só cartas que faltam e salva o perfil

- Mesma branch (lote 3): `Controls` traduz teclas remapeadas e botões/analógico do joystick para as teclas canônicas (Z, Esc, Enter, Del, S, Esq/Dir), então as telas não mudam; bindings `BIND_KEY_*`/`BIND_PAD_*` no cfg; `Controls.allows(playerA)` decide quem joga no Versus. Versus = flag `versus` em `TripleTriad` (B usa a mão `OPPONENT`, sem IA, Open forçado e restaurado em `endVersus`). Telas novas estilo PS1 usam `ui.Ps1` (frame/painel/linhas): `RemoveCardsScreen`, `ProfileSelectScreen`, `TrophiesScreen`, `ControlsScreen`, `VersusScreen`. Perfil ganhou `AVATAR` e `STAT_*`. Vitória: `AudioController.playVictory/stopVictory`

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

- Vídeo é quadros JPEG (sem decoder de vídeo no libGDX); `res/endgame.mp4` vai no zip só como fonte

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2
