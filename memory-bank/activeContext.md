# Active context

Último trabalho (ainda sem commit): itens 3, 5, 6 e 8 do modo História na `master` local — regras regionais que se espalham, revanche do chefe, tela Progresso e dificuldade da campanha. Save no formato 5 (`DIFFICULTY`, `CITYRULES`, `CARRY`, `PLAYS`, `QUEEN`, `WINS`, `LOSSES`, `CARDWON`). Saves 4 e anteriores abrem como Normal. `./gradlew clean build test` passou com 65 testes. Sem commit nem push.

Último marco: **1.3.0** na `master`, com o modo História ainda sem número de release. O deck próprio da campanha, as recompensas novas, o balanceamento e o roteiro reescrito estão na `master` depois do merge de `feat/historia-deck-balanceamento`.

## Menu principal

`ui.MenuScreen` desenha o perfil ativo no canto superior esquerdo e as doze opções numa janela azul (as duas últimas: Sobre `ITEM_ABOUT=10` e Sair do jogo `ITEM_EXIT=11`) de borda clara. O nome usa contorno, cai para a fonte pequena e trunca com reticências. A linha de baixo é `I18n.menuProfileMeta` (tamanho do álbum e `championshipWins`). Sem o PNG do avatar, um busto neutro ocupa a moldura. Fundo: `GameImage.BOARD_MAT` escurecido, com parallax e versos já carregados — nenhuma textura nova. Esquerda (código canônico, depois do remap) foca o painel; confirmar chama `showProfileSelectFromMenu`. Esc nessa seleção volta ao menu (`profileSelectExits = false`). Na abertura, `introFinished` deixa `profileSelectExits` verdadeiro e Esc ali pede para sair.

## Sair do jogo e Sobre

- `ui.ExitConfirm` (lógica pura, testada): Não (0, padrão) / Sim (1); setas trocam, Z/Enter confirma, Esc/X fica, clique responde direto. `TripleTriad.requestExit()` abre a janela (ignorado em MATCH); todo caminho de Esc que antes chamava `container.exit()` (MENU, PROFILE_SELECT com `profileSelectExits`, PROFILE com `quitsOnEscape`) chama `requestExit()`. A janela é desenhada por cima da tela ativa e intercepta teclado, mouse e roda. Fechar a janela do SO (closeRequested) ainda sai direto.
- `ui.AboutScreen` (`GameScreen.ABOUT`) mostra `About.paragraphs(Lang)`: original Jeffrey Han / itdelatrisu (primeiro commit f7e18e2, 2014-07-23; 0.1.1 em 2014-08-01), fork Marcilio Junior / marciliojr, adições, GPLv3, aviso Square Enix. Versão vem de `/version.properties` (Gradle `processResources` expande `${version}`; fallback "dev"). Ao mudar adições relevantes, atualizar `About.java` nos três idiomas (`AboutTest` exige o mesmo número de parágrafos).
- `MenuScreen.drawWindow` é público para outras janelas FFVIII reutilizarem.

## Texto da História

- `StoryTextTest` falha se PT/ES perderem acentos comuns (regex), se `{a|b}` estiver malformado, se EN usar marca de gênero, ou se fala > 260, bio > 200, estilo > 90 caracteres. Fujin fala em maiúsculas; Raijin em EN usa "ya know"; Squall não fala de si na terceira pessoa.

## Campanha

- Menu **História**. O jogador é aluno ou aluna de Balamb: Nova campanha abre a escolha do protagonista (`Mode.PROTAGONIST`, retratos `aluno_m` / `aluna`) e depois o nome. A escolha fica em `CAMP.n.GENDER` (M/F); campanha antiga sem a chave abre como aluno (`StorySave.LEGACY_GENDER`). Texto de PT/ES usa `{masc|fem}` resolvido em `StoryCampaign.fill`. Squall só orienta. Nilo Vann é o locutor: `Line.narrator` com retrato `StoryCampaign.NARRATOR_PORTRAIT` = `narrador` (`narrador_normal/derrota`; derrota quando `sceneDefeat`); o crachá desenhado (`drawNarrator`) só aparece se o PNG faltar.
- Até 15 campanhas em `StorySave` / chaves `CAMP.*` (formato 4: `SEED`, `COLLECTION`, `READY`, `SEEN`, `GIVEN`). O perfil guarda álbum, decks, blocos do Campeonato e troféus, que não entram na campanha. Cada campanha tem o próprio deck. Formatos 1 a 3 ainda abrem: a migração entrega o deck inicial mais cartas equivalentes ao progresso. `STORY_CLEARED` antigo migra uma vez e continua gravado. Tabela de níveis, decks e elenco: `memory-bank/storyBalance.md`.
- Regulamento: seletiva em chave (Garden); grupos com tabela real (Town top 2, Timber top 3, Deling top 2, Dollet top 2, FH e Esthar top 1). A mesa da Rainha em FH não dá ponto e não é o chefe da cidade (introduz Same Wall e Sudden Death). Final: `queen_esthar` no Garden, com Bahamut como carta fixa do chefe. Derrota = revanche, sem eliminar e sem alterar a coleção.
- Cada vitória: escolha 1 carta do deck do adversário (a mão dele pode trazer 1 especial aleatória nos comuns). No chefe (`Duel.isBoss()`, `rewardId > 0`), a fase `AWARD` entrega a carta fixa e depois a escolha. `takeBossReward()` marca `pendingSpecialGiven` para não duplicar ao recarregar. `Profile.grantStoryCards` grava na coleção da campanha ativa.
- Protagonista: `Mode.PROTAGONIST` em `StoryScreen` (esq/dir ou mouse, Z/Enter, Esc volta ao hub). Retrato em `StoryAssets.portrait(save.portraitId(), derrota)`: lista de campanhas (ícone), topo da etapa, tela do nome e à direita da caixa de diálogo (derrota depois de partida perdida, `sceneDefeat`). `Line` com retrato `@player` (`StoryCampaign.PLAYER_PORTRAIT`) faz o protagonista falar; nenhuma fala usa ainda.
- Arte em jogo (`res/story/`): 7 fundos `mesa_*`; `arena_final` (fundo da região `final`, `StoryCampaign.FINAL_ARENA`, também na cerimônia); `titulo_modo_historia` (`StoryAssets.TITLE_BACKGROUND`: HUB, PROTAGONIST, NAME, CAMPAIGNS, DELETE; logo `I18n.storyLogoTitle/Subtitle` desenhado no canto superior esquerdo, sem travessão porque a fonte não tem U+2013); `mapa_mundi` (Mode.MAP, `story.StoryMap`: pontos 640x360 do MANIFESTO_extras, Garden/Town/Final separados na ilha de Balamb, `toScreen` com o mesmo letterbox do fundo, `hit` para clique; ícones `no_*`); `revelacao_cc_king` permanece no disco (`StoryCampaign.KING_REVEAL`) mas não é mais encadeado na apresentação da Quistis; `StoryScreen.renderStill` ainda desenha uma cena em tela cheia se algum `Line.scene` existir; 40 retratos de adversários/NPCs + `npc_*_derrota` (6) + `narrador_*`, `aluno_m_*`, `aluna_*`; UI (`caixa_dialogo`, `no_*`, `rare_card_banner`). `aluno_normal/derrota` em `story-assets/` são cópias antigas da aluna e não são usadas. `StoryArtTest` confere tudo isso.
- Rosto de derrota do adversário: `StoryScript.duel()` passa `win` e `afterWin` (falas do próprio adversário) por `beaten(..., true)` e `lose`/`afterLose` por `beaten(..., false)`; o helper `lost()` do roteiro não decide mais o rosto dessas falas. Dossiê (T) usa a derrota se o duelo já foi vencido.
- Nova cena em tela cheia: `line.withScene("<arquivo em story/backgrounds>")`; paginação em `keyScene` usa `STILL_LINES`/`stillTextWidth()` quando `sceneStill()` não é null.
- Vídeo se existir `res/story/<id>/frame_001.jpg` + `<id>.ogg`. Prontos: `intro_historia`, `chegada_balamb_town`, `chegada_timber` (video_3), `chegada_deling` (video_4), `chegada_fh` (video_6), `chegada_esthar` (video_7), `final_historia` (video_8, chegada da grande final) e `encerramento_campeao` (video_9, `StoryCampaign.ENDING_VIDEO`, toca depois da vitória na final e antes das falas da cerimônia; sem o clipe, usa o fundo da arena). Pendente: `chegada_dollet` (video_5 ainda não chegou). Receita em `res/story/IMPORTACAO.txt`.

## Entregue na 1.3.0 (notas técnicas)

- Branch `feat/1.3.0-video-endgame`: vídeo em loop na zerada do Campeonato (`gfx.VideoLoop` + `ChampionshipScreen.MODE_WON`). Fonte `res/endgame.mp4`; o jogo usa `res/endgame/frame_NNN.jpg` + `res/endgame.ogg`. Para trocar o vídeo: `ffmpeg -i endgame.mp4 -vf scale=640:360 -r 24 -q:v 7 res/endgame/frame_%03d.jpg` e `ffmpeg -i endgame.mp4 -vn -c:a libvorbis -q:a 4 res/endgame.ogg`

- Mesma branch: abertura `ui.IntroScreen` (`GameScreen.INTRO`, `VideoLoop.setLooping(false)`, S pula, `AudioController.init(false)` + `introFinished()` muda a tela e então `syncMusic()` liga o tema do menu) e save estilo Memory Card PS1 (`ui.MemoryCardScreen`, `GameScreen.MEMORY_CARD`). Saves em `Profile` como `ChampionshipSlot[15]` gravados no perfil como `SLOT.N.ROUND/WINS/HAND/OPPONENT/BAG/RULES/PLAY_MS/SAVED_AT`; `RUN_*` antigo migra para o bloco 01 em `migrateLegacyRun()`. Fim do auto-delete: `endChampionshipSave()` só grava o álbum

- Mesma branch: menu Cheats (`ui.CheatsScreen`, `GameScreen.CHEATS`). Categorias pelo nível em `cards/deck.txt`: 1-7 monstros (6-7 chefes contam como monstros), 8-9 GFs, 10 personagens. Adiciona só cartas que faltam e salva o perfil

- Mesma branch (lote 3): `Controls` traduz teclas remapeadas e botões/analógico do joystick para as teclas canônicas (Z, Esc, Enter, Del, S, Esq/Dir), então as telas não mudam; bindings `BIND_KEY_*`/`BIND_PAD_*` no cfg; `Controls.allows(playerA)` decide quem joga no Versus. Versus = flag `versus` em `TripleTriad` (B usa a mão `OPPONENT`, sem IA, Open forçado e restaurado em `endVersus`). Telas novas estilo PS1 usam `ui.Ps1` (frame/painel/linhas): `RemoveCardsScreen`, `ProfileSelectScreen`, `TrophiesScreen`, `ControlsScreen`, `VersusScreen`. Perfil ganhou `AVATAR` e `STAT_*`. Vitória: `AudioController.playVictory/stopVictory`

## Saída da História e menu da etapa

- Esc nas telas da campanha (mapa, diálogo, vídeo, protagonista, lista de mesas, recompensa, etapa, hub) abre `ExitConfirm` com `Purpose.RETURN_MENU` ("Voltar ao menu? O progresso será salvo."). Sim grava o perfil (`save.touch()` + `saveProfile()`) e volta ao menu principal. Não/Esc/X fecha a janela. Diálogos locais (apagar campanha, confirmar carta, nome) continuam usando Esc para cancelar.
- X volta uma tela (mapa/duelos/protagonista/campanhas -> etapa ou hub; no diálogo, X pula o resto da cena). Z/S continuam pulando o vídeo.
- Menu da etapa: **Cidades** (mapa) é o caminho para escolher os jogos. **Coleção** abre a galeria da campanha (`MyDeckScreen.enterStoryGallery`, sem apagar cartas). **Tabela** só aparece nas fases `Kind.GROUP`.
- Falas: Nilo Vann só apresenta o jogo (nome, origem, regras, recompensa, etapa/placar; convite à revanche; anúncio da carta). Squall explica a regra na primeira aparição (`StoryScript.lessons`, chaves em `SEEN`), com texto e, quando cabe, um tabuleiro miniatura (`Line.example` / `StoryRuleExample`) desenhado acima da caixa de diálogo. Os exemplos passam por `CardResult`. Lição já vista não se repete. Adversários falam curto na mesa. Fujin em maiúsculas; Raijin com "ya know" em EN; Squall sem terceira pessoa. `StoryTextTest` também recusa glifos fora de ASCII + `áàâãéêíóôõúüçÁÀÂÃÉÊÍÓÔÕÚÜÇñÑ¿¡`.

## Ao continuar

- A coleta da campanha e o pick de 5 cartas usam `MyDeckScreen` com `campaignBag`; atalhos remapeáveis e joystick passam pelas teclas canônicas
- Balanceamento e elenco da História: ver `memory-bank/storyBalance.md` antes de mexer em níveis, cartas fixas ou nomes

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
