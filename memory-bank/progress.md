# Progress

## Feito

- [x] Gradle oficial (wrapper, `application`, cwd na raiz)
- [x] Java 21 (passo anterior: Java 17)
- [x] libGDX no lugar de Slick2D/LWJGL 2
- [x] Remoção de `slick.jar`, `lwjgl-2.9.1.jar`, `jorbis` e natives LWJGL 2
- [x] Perfil, menu, seletor e construtor de deck
- [x] Jogo Rápido vs IA com regras de captura originais
- [x] Fonte PT-BR/ES (OpenSans + glifos acentuados)
- [x] Painel de regras na seleção de deck
- [x] Tela Como Jogar
- [x] Configurações: música, som do cursor, idioma (pt-BR, en, es)
- [x] Meu Deck (álbum permanente; preview com C; X/Del remove com confirmação)
- [x] Campeonato (8 rounds, lobby Novo/Continuar/Apagar progresso, bag da run, save no pick)
- [x] Esc na partida pede confirmação (Não / Sim) para evitar toque acidental
- [x] Aviso “progresso salvo” no rodapé após Salvar no pick do Campeonato
- [x] libGDX 1.14.2
- [x] `distZip` com `res/` e `cards/`
- [x] GitHub Actions: build no PR; tag + Release no merge na `master`
- [x] Teclas padronizadas: Z seleciona, X/Del remove, C amplia, R aleatório (construtor/picks)
- [x] Dificuldade da IA nas Configurações (Fácil/Normal/Difícil)
- [x] Versus no menu (1.3.0)
- [x] Vários perfis e deck aleatório no Jogo Rápido
- [x] Seleção híbrida: nome/elemento nos slots, painel médio do cursor
- [x] Galeria Meu Deck com o mesmo painel de preview ao navegar
- [x] 1.0.0 (primeira versão jogável)
- [x] Ordenar grids de cartas (T: nível / valor / catálogo) no construtor, Meu Deck e pick do Campeonato
- [x] Parse do level em `Deck` lê a coluna Level (`tokens[4]`)
- [x] Menu Salvar: exportar/importar álbum e campeonato (`.ttsave`, diálogo do sistema, confirmação ao substituir)
- [x] 1.1.0 (save portátil e ordenação das cartas)
- [x] 1.1.1 (diálogo de save fecha de verdade; não reabre Dolphin/file picker)
- [x] 1.2.0 (troca do Campeonato mostra Já possui / Nova!; cartas sem elemento sem quadrado vazio)
- [x] 1.3.0 (intro e vídeo final, Memory Card PS1 15 blocos, Versus, joystick + todas as ações remapeáveis, teclado virtual, Cheats, Troféus, avatares, legendas HintBar)
- [x] Modo História: aluno ou aluna de Balamb (escolha por campanha, retrato nos diálogos e texto PT/ES com concordância; save `CAMP.n.GENDER`, campanha antiga = aluno), Nilo Vann, até 15 campanhas por perfil, tabela de grupos, uma carta escolhida por vitória e especial só na primeira vez. Quatro vídeos prontos (Garden, Balamb Town, Timber, Deling); Dollet, FH, Esthar e o final ainda em fundo estático
- [x] Menu principal no estilo das janelas do FFVIII: perfil ativo no canto superior esquerdo, doze opções (com Sobre e Sair do jogo), HintBar e atalhos remapeados

- [x] Esc no menu / opção Sair do jogo pedem confirmação Não/Sim (padrão Não)
- [x] Tela Sobre (fork de itdelatrisu, ampliado por marciliojr, GPLv3, versão, aviso Square Enix)
- [x] Revisão dos diálogos do modo História em PT/ES/EN, com `StoryTextTest`
- [x] Arte extra no modo História: retrato do Nilo Vann, derrota dos 6 NPCs (e rosto de derrota do adversário só quando o jogador vence), mapa-múndi em Cidades, tela de abertura com logo, arena da final e revelação da CC King em tela cheia, com `StoryArtTest`

## Não feito (de propósito)

- [ ] Trade rules clássicas (One/Diff/Direct/All) e regiões
- [ ] Fat JAR / instalador
- [ ] Testes automatizados do motor de captura
