# Product context

O jogador cria um perfil, monta decks de 5 cartas do catálogo (`cards/deck.txt` + PNGs) e entra em Jogo Rápido ou Campeonato contra a IA.

Fluxo: **Perfil → Menu → Decks / Meu Deck / Como Jogar / Configurações / Campeonato → Partida → Menu**.

Versus aparece no menu como “Em breve”.

Persistência local (cwd = raiz do repo):

- `.triple-triad.cfg` — resolução, volume, FPS, fonte, regras, tipo de IA, música ligada, som do cursor, idioma
- `.triple-triad-profile` — nome, decks do Jogo Rápido, álbum (Meu Deck), run do Campeonato (`RUN_*`), campeonatos vencidos
- `.triple-triad.log` — erros

Arte e áudio originais em `res/` (não redesenhar). Regras da partida são ligadas na tela de Jogo Rápido e no lobby do Campeonato. Idioma e áudio em Configurações.

Meu Deck é o álbum permanente (duplicatas ok; Del remove uma cópia). Jogo Rápido pode pickar 5 cartas dali.

Campeonato: lobby com Novo jogo, Continuar e Apagar progresso (não zera o álbum). Novo jogo cria pack se o álbum tiver menos de 5 cartas; senão abre o pick. Vitória adiciona a carta ao álbum e à bag da run; derrota tira só da run. **Salvar** no pick (da 2ª partida em diante) grava a run e mostra confirmação na tela. Esc na luta pede confirmação antes de voltar ao menu.
