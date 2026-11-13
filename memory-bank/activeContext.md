# Active context

Último marco: **1.3.0** na `master`, com o modo História integrado em seguida (ainda sem número de release).

## Menu principal

`ui.MenuScreen` desenha o perfil ativo no canto superior esquerdo e as dez opções numa janela azul de borda clara. O nome usa contorno, cai para a fonte pequena e trunca com reticências. A linha de baixo é `I18n.menuProfileMeta` (tamanho do álbum e `championshipWins`). Sem o PNG do avatar, um busto neutro ocupa a moldura. Fundo: `GameImage.BOARD_MAT` escurecido, com parallax e versos já carregados — nenhuma textura nova. Esquerda (código canônico, depois do remap) foca o painel; confirmar chama `showProfileSelectFromMenu`. Esc nessa seleção volta ao menu (`profileSelectExits = false`). Na abertura, `introFinished` deixa `profileSelectExits` verdadeiro e Esc continua saindo.

## Campanha

- Menu **História**. O jogador é um aluno de Balamb (nome escolhido na hora). Squall só orienta. Nilo Vann é o locutor: crachá desenhado (`narrator`), sem retrato de outro personagem.
- Até 15 campanhas em `StorySave` / chaves `CAMP.*`. O perfil guarda álbum, decks, blocos do Campeonato e troféus. A campanha guarda duelos, chegadas, especiais já dados e a escolha pendente. `STORY_CLEARED` antigo migra uma vez e continua gravado.
- Regulamento: seletiva em chave (Garden); grupos com tabela real (Town top 2, Timber top 3, Deling top 2, Dollet top 2, FH e Esthar top 1). A mesa da Rainha em FH não dá ponto. Final: `queen_esthar` no Garden, Bahamut na primeira vitória. Derrota = revanche, sem eliminar.
- Cada vitória: uma carta da mão jogada, confirmada. Especiais do catálogo ficam fora da mão (`StoryCampaign.isSpecial`).
- Vídeo se existir `res/story/<id>/frame_001.jpg` + `<id>.ogg`. Prontos: `intro_historia` e `chegada_balamb_town`. Pendentes: `chegada_timber`, `chegada_deling`, `chegada_dollet`, `chegada_fh`, `chegada_esthar`, `final_historia`. Receita em `res/story/IMPORTACAO.txt`.

## Entregue na 1.3.0 (notas técnicas)

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
- Versus está no menu (local, Open forçado, sem IA)
- Hints novas: escrever no estilo "Z/Enter ação    Esc volta" em funções `hint*` do I18n (passam por `Controls.dyn`) e desenhar com `HintBar.draw`/`Ps1.hint`; telas comparam com `Controls.is(key, Action.X)` ou o código canônico
- Ordenação de cartas: só via `CardSort` (view); não mutar catálogo, álbum nem bag da run
- Export/import: só via `SaveTransfer` + `SaveScreen`; não misturar com o `.profile` automático

## Dívida conhecida

- Vídeo é quadros JPEG (sem decoder de vídeo no libGDX); `res/endgame.mp4` vai no zip só como fonte

- Avisos JNI/`Unsafe` do LWJGL 3.3.3 em JDK 25 (não quebram o jogo)
- `build.xml` cita JARs removidos
- `lib/docs` e `lib/src` ainda têm zips da era Slick/LWJGL 2

- Lote 4: `Controls.Action` tem todas as ações (canônico interno + tecla padrão + papel do pad); `BasicGame.keyDown` só repassa teclas ligadas a uma ação (texto vem por `keyTyped`; Backspace passa para edição). `Controls.dyn()` troca nomes de tecla nas dicas (`hint*` do I18n) pelos atalhos atuais; `Controls.is(key, Action)` nas telas novas. Joystick: Jamepad carrega `/gamecontrollerdb.txt` do classpath (`resources/`); sem controle SDL, `Controls.poll()` lê `/dev/input/js*` (threads) e detecta receptor 8BitDo em HID (hidraw 2dc8) para avisar. `ui.VirtualKeyboard` em ProfileScreen, DeckBuilderScreen (nome) e CheatsScreen. Diagnóstico: 8BitDo Ultimate Wireless em 2dc8:3109 (só hidraw) não vira joystick; precisa X-input (2dc8:3106, driver xpad)

- Legendas: `ui.HintBar.draw(text, y)` (também via `Ps1.hint`) interpreta a dica já passada por `Controls.dyn` ("[tecla]/[botão] ação" separados por 4 espaços) e desenha painel com badges; quebra linhas e usa `Options.getTinyFont()` se passar de 2 linhas. Deploy precisa preservar `bin/.triple-triad.cfg`, `bin/.triple-triad-profiles/` (dados do jogador) e `bin/card-back.png` (ícone do .desktop, agora vem na distribuição)
