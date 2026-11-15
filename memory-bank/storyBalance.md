# Modo História: deck, balanceamento e elenco

O álbum do perfil não entra na campanha e as cartas ganhas na campanha não vão para o álbum. Cada uma das até 15 campanhas do perfil tem a própria coleção, a própria semente e o próprio progresso, no save formato 4.

## Deck inicial

Cartas de nível 1 e 2, sem raras, concedidas ao criar a campanha (`StoryBalance.STARTER`):

| Id | Carta | Nível |
| --- | --- | --- |
| 1 | Geezard | 1 |
| 4 | Red Bat | 1 |
| 9 | Blood Soul | 1 |
| 11 | Cockatrice | 1 |
| 12 | Grat | 2 |
| 15 | Glacial Eye | 2 |
| 17 | Thrustaevis | 2 |
| 22 | Grand Mantis | 2 |

Antes de cada duelo o jogador monta a mão de 5 cartas nessa coleção.

## Recompensas

- Vitória comum: escolhe 1 carta do deck de 5 que o adversário acabou de usar.
- Vitória no chefe da cidade (`rewardId > 0`): recebe 1 carta forte fixa e depois escolhe 1 do deck do chefe.
- Derrota: não dá e não tira carta; a revanche é na mesma mesa.
- A pendência (`PENDING` + `GIVEN`) sobrevive ao fechar o jogo, sem duplicar a carta fixa.

## Especiais aleatórias

Nos adversários comuns, há 18% de chance (`SPECIAL_CHANCE`) de 1 carta acima da curva entrar no deck, com a semente da campanha. No máximo 1 portador por cidade (`MAX_SPECIALS_PER_REGION`). O nível dessa carta nunca passa do `maxLevel` do próximo chefe.

## Conjuntos de regras

| Ruleset | Regras |
| --- | --- |
| OPEN_BASIC | Open, Same, Combo |
| BASIC | Same, Combo |
| PLUS | Same, Plus, Combo |
| ELEMENT | Same, Plus, Combo, Elemental |
| FINAL | Same, Same Wall, Plus, Combo, Elemental, Sudden Death |

A campanha não usa Random (a mão montada pelo jogador permanece). A troca não é One/Diff/Direct/All: é a escolha descrita acima.

## Curva e tabela por adversário

Ids antigos foram mantidos (saves e retratos). Nomes oficiais de FFVIII não mudam. O chefe da cidade é o duelo com carta fixa.

| Etapa | Id | Nome (PT) | Papel | Níveis | IA | Regras | Chefe / carta fixa | Especial aleatória |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Balamb Garden | cadete | Lina Faris | Cadete de Balamb | 1-2 | DEFENSIVE | OPEN_BASIC | não | sim, teto 5 |
| Balamb Garden | spade | Rik Calden (antes CC Spade) | Cadete de Balamb | 1-2 | DEFENSIVE | BASIC | não | sim, teto 5 |
| Balamb Garden | joker | Toma Breck (antes CC Joker) | Cadete de Balamb | 1-3 | DEFENSIVE | BASIC | não | sim, teto 5 |
| Balamb Garden | club | Nira Voss (antes CC Club) | Cadete veterana de Balamb | 2-3 | BALANCED | PLUS | não | sim, teto 5 |
| Balamb Garden | heart | Xu | Instrutora do Garden | 3-4 | BALANCED | PLUS | não | sim, teto 5 |
| Balamb Garden | king | Quistis | Instrutora do Garden | 4-5 | BALANCED | PLUS | Ifrit (85) | não (chefe) |
| Balamb Town | queen_town | Rainha das Cartas | Viajante da mesa | 3-5 | BALANCED | PLUS | não | sim, teto 6 |
| Balamb Town | raijin | Raijin | Discípulo de Balamb | 4-5 | BALANCED | PLUS | não | sim, teto 6 |
| Balamb Town | fujin | Fujin | Discípula de Balamb | 5-6 | OFFENSIVE | PLUS | Quezacotl (83) | não (chefe) |
| Timber | zone | Zone e Watts | Repórteres de Timber | 4-5 | BALANCED | PLUS | não | sim, teto 7 |
| Timber | jack | Edd Brask (antes CC Jack) | Ferroviário de Timber | 4-6 | BALANCED | ELEMENT | não | sim, teto 7 |
| Timber | soldier_timber | Bram Holt | Soldado de Galbadia | 5-6 | OFFENSIVE | PLUS | não | sim, teto 7 |
| Timber | rinoa | Rinoa | Visitante de Timber | 5-7 | BALANCED | ELEMENT | Siren (86) | não (chefe) |
| Deling | diamond | Hal Vektor (antes CC Diamond) | Oficial de Galbadia | 5-6 | BALANCED | ELEMENT | não | sim, teto 7 |
| Deling | soldier_deling | Mara Quill | Oficial de Galbadia | 6-7 | OFFENSIVE | ELEMENT | não | sim, teto 7 |
| Deling | queen_deling | Rainha das Cartas | Viajante da mesa | 6-7 | OFFENSIVE | ELEMENT | Alexander (95) | não (chefe) |
| Dollet | pub | Tomas Reed | Dono de pub em Dollet | 5-6 | DEFENSIVE | PLUS | não | sim, teto 8 |
| Dollet | soldier_dollet | Enne Doss | Guarda de Dollet | 6-7 | BALANCED | ELEMENT | não | sim, teto 8 |
| Dollet | queen_dollet | Rainha das Cartas | Viajante da mesa | 6-8 | OFFENSIVE | ELEMENT | Diablos (90) | não (chefe) |
| Fisherman's Horizon | queen_fh | Rainha das Cartas | Viajante da mesa | 6-7 | OFFENSIVE | FINAL | não (mesa semente, sem ponto) | sim, teto 8 |
| Fisherman's Horizon | pescador | Ryn Kess (antes Nash Pell) | Engenheiro de FH | 5-6 | BALANCED | PLUS | não | sim, teto 8 |
| Fisherman's Horizon | shumi | Ancião Muum | Ancião Shumi | 6-7 | BALANCED | ELEMENT | não | sim, teto 8 |
| Fisherman's Horizon | flo | Dobe e Flo | Moradores de FH | 7-8 | OFFENSIVE | ELEMENT | Phoenix (96) | não (chefe) |
| Esthar | cientista | Ivo Lenn | Pesquisador de Esthar | 6-7 | OFFENSIVE | ELEMENT | não | sim, teto 9 |
| Esthar | odine | Odine | Cientista de Esthar | 7-8 | OFFENSIVE | FINAL | não | sim, teto 9 |
| Esthar | seifer | Seifer | Candidato da outra chave | 8-9 | OFFENSIVE | FINAL | Seifer (109) | não (chefe) |
| Final no Garden | queen_esthar | Rainha das Cartas | Campeã mundial | 8-10 | OFFENSIVE | FINAL | Bahamut (97) | não (chefe) |

Personagens oficiais que mantêm o nome: Lina Faris, Xu, Quistis, Rainha das Cartas, Raijin, Fujin, Zone e Watts, Bram Holt, Rinoa, Mara Quill, Tomas Reed, Enne Doss, Ancião Muum, Dobe e Flo, Ivo Lenn, Odine e Seifer.

## Migração

Campanhas em formato 3 ou anterior recebem o deck inicial, as cartas especiais antigas dos duelos já vencidos (mapa legado, inclusive `final` → Bahamut) e um stand-in da faixa de cada adversário já derrotado. Nada do progresso gravado é apagado.

## Simulação

`StoryBalanceTest` compara a força esperada do jogador (melhores 5 cartas do starter + stand-ins e cartas de chefe já obtidos) com a força típica dos adversários daquela etapa (média de 8 rolagens com a semente). A faixa aceita é 70% a 145% da força típica do adversário.
