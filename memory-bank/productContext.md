# Product context

O jogador cria um perfil, monta decks de 5 cartas do catálogo (`cards/deck.txt` + PNGs) e entra em Jogo Rápido contra a IA.

Fluxo: **Perfil → Menu → Decks / Construtor → Partida → Menu**.

Campeonato e Versus aparecem no menu como “Em breve”.

Persistência local (cwd = raiz do repo):

- `.triple-triad.cfg` — resolução, volume, FPS, fonte, regras, tipo de IA
- `.triple-triad-profile` — nome e decks
- `.triple-triad.log` — erros

Arte e áudio originais em `res/` (não redesenhar). Configuração de regras no cfg, não em UI dedicada.
