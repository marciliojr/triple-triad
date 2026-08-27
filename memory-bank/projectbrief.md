# Project brief

Triple Triad desktop (Final Fantasy VIII): partida local contra IA com as regras clássicas e um meta-jogo simples (perfil + decks + Meu Deck + Jogo Rápido + Campeonato).

## Objetivos

- Preservar o motor de captura (`CardResult`, `Rule`, Same/Plus/Combo/Elemental/Sudden Death) e as IAs
- Cliente moderno: Gradle, Java 21, libGDX
- Interface em português (Brasil), inglês e espanhol
- Não implementar ainda Versus, trade rules clássicas nem regiões

## Fora de escopo (por enquanto)

- Multijogador, ranking, servidor
- Migrar para LWJGL 3 “cru” (o backend do libGDX já o usa)
- Language features novas (17/21) só por estética no motor de regras
