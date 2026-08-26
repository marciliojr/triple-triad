# Product context

O jogador cria um perfil, monta decks de 5 cartas do catálogo (`cards/deck.txt` + PNGs) e entra em Jogo Rápido contra a IA.

Fluxo: **Perfil → Menu → Decks / Construtor / Como Jogar / Configurações → Partida → Menu**.

Campeonato e Versus aparecem no menu como “Em breve”.

Persistência local (cwd = raiz do repo):

- `.triple-triad.cfg` — resolução, volume, FPS, fonte, regras, tipo de IA, música ligada, som do cursor, idioma
- `.triple-triad-profile` — nome e decks
- `.triple-triad.log` — erros

Arte e áudio originais em `res/` (não redesenhar). Regras da partida são ligadas na tela de Jogo Rápido. Idioma e áudio em Configurações.
