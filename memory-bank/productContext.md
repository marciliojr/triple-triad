# Product context

O jogador cria um perfil, monta decks de 5 cartas do catálogo (`cards/deck.txt` + PNGs) e entra em Jogo Rápido ou Campeonato contra a IA.

Fluxo: **Perfil → Menu → Decks / Meu Deck / Salvar / Como Jogar / Configurações / Campeonato → Partida → Menu**.

Versus não está no menu (fica fora de escopo até o produto pedir).

Persistência local (cwd = raiz do repo):

- `.triple-triad.cfg` — resolução, volume, FPS, fonte, regras, tipo de IA, música ligada, som do cursor, idioma, perfil ativo
- `.triple-triad-profiles/` — um arquivo por jogador (nome, decks, álbum, run do Campeonato, copas); legado `.triple-triad-profile` é migrado
- `.triple-triad.log` — erros
- arquivos `.ttsave` — export manual do álbum (`COLLECTION`) ou do checkpoint do Campeonato (`RUN_*`); o jogador escolhe o caminho no diálogo do sistema. Importar pede Não/Sim (default Não) antes de substituir.

Arte e áudio originais em `res/` (não redesenhar). Regras da partida são ligadas na tela de Jogo Rápido e no lobby do Campeonato. Idioma e áudio em Configurações.

Meu Deck é o álbum permanente (duplicatas ok). Ao navegar, o painel direito mostra a carta média com id, nome e elemento; **C** amplia em overlay. **X**/Del pede Não/Sim (default Não) antes de apagar uma cópia. Jogo Rápido pode pickar 5 cartas dali. Construtor e picks: 5 slots com nome/elemento, **R** aleatório, **X** só tira da mão.

Campeonato: lobby com Novo jogo, Continuar e Apagar progresso (não zera o álbum). Novo jogo cria pack se o álbum tiver menos de 5 cartas; senão abre o pick. Vitória adiciona a carta ao álbum e à bag da run; derrota tira só da run. **Salvar** no pick (da 2ª partida em diante) grava a run; o aviso fica no rodapé, abaixo da barra de teclas. Esc na luta pede confirmação antes de voltar ao menu.

Teclas da UI: **Z** seleciona, **X**/Del remove (álbum com confirmação; pick só a mão), **C** amplia, **R** aleatório no construtor/picks, **T** cicla a ordem do grid (nível, valor/soma dos ranks, catálogo/ID), **Esc** volta. Na partida, **X** cancela a casa do tabuleiro. A ordenação é só visual (não grava no perfil).
