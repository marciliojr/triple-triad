# Modo História: deck, balanceamento e elenco

O álbum do perfil não entra na campanha e as cartas ganhas na campanha não vão para o álbum. Cada uma das até 15 campanhas do perfil tem a própria coleção, a própria semente e o próprio progresso, no save formato 5. Campanhas no formato 4 ou anterior abrem como Normal, com as regras nativas de cada cidade, sem regra carregada e com o placar zerado.

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
- Derrota no Fácil e no Normal: não dá e não tira carta; o mesmo duelo pode ser jogado de novo.
- Derrota no Difícil: cobra 1 carta da mão que o jogador levou à mesa, sem deixar a coleção com menos de 5.
- Revanche do chefe: depois de vencer o chefe da cidade, abre um duelo opcional (`id_rematch`), fora da lista que libera a etapa. Níveis e IA sobem 1 degrau (nível teto 10). A primeira vitória dá uma segunda rara distinta; as seguintes só dão a escolha de 1 carta do deck.
- A pendência (`PENDING` + `GIVEN`) sobrevive ao fechar o jogo, sem duplicar a carta fixa.

## Especiais aleatórias

Nos adversários comuns, há 18% de chance (`SPECIAL_CHANCE`) de 1 carta acima da curva entrar no deck, com a semente da campanha. No máximo 1 portador por cidade (`MAX_SPECIALS_PER_REGION`). O nível dessa carta nunca passa do `maxLevel` do próximo chefe.

## Conjuntos de regras

A partida usa o conjunto ativo da cidade (`StoryRules.effective`), não o `Ruleset` gravado no duelo (esse valor ficou só como metadado). Cada cidade começa com o conjunto nativo abaixo. Regras espalhadas e mudanças da Rainha entram nesse conjunto e ficam no save (`CITYRULES`).

| Cidade | Regras nativas | Assinatura carregada |
| --- | --- | --- |
| Balamb Garden | Open, Same, Plus, Combo | Open |
| Balamb Town | Same, Plus, Combo | Plus |
| Timber | Same, Plus, Combo, Elemental | Elemental |
| Deling | Same, Plus, Combo, Elemental | Elemental |
| Dollet | Same, Plus, Combo, Elemental | Elemental |
| Fisherman's Horizon | Same, Same Wall, Plus, Combo, Elemental | Same Wall |
| Esthar | Same, Same Wall, Plus, Combo, Elemental | Same Wall |
| Final no Garden | Same, Same Wall, Plus, Combo, Elemental, Sudden Death | Same Wall |

Depois de cada partida (vitória, derrota ou empate) na cidade, o jogo incrementa `PLAYS` e tenta plantar a regra carregada (`CARRY`) ali: chance `min(50, 15 + 8 * (plays - 1))` por cento. Em seguida escolhe uma nova regra para carregar a partir da cidade atual. Combo acompanha Same ou Plus. Podem se espalhar: Open, Same, Same Wall, Plus, Elemental. Sudden Death fica só na final e nunca se espalha. Random não entra na campanha e não se espalha.

A Rainha das Cartas, na primeira visita de cada cidade (`QUEEN`), pode acrescentar, tirar ou manter uma regra. Same Wall só é oferecida a partir de Timber. A coleção da mesa precisa continuar jogável (pelo menos uma regra de captura). Squall explica qualquer regra nova na primeira vez, inclusive uma que chegou por espalhamento ou pela Rainha.

A campanha não usa Random (a mão montada pelo jogador permanece). A troca não é One/Diff/Direct/All: é a escolha descrita acima.

Na tabela de adversários, a coluna Regras é o ruleset legado do duelo e não governa mais a partida.

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


## Dificuldade

Escolhida ao criar a campanha, depois do nome, e gravada em `DIFFICULTY`.

| Nível | IA | Níveis das cartas do adversário | Derrota |
| --- | --- | --- | --- |
| Fácil | 1 degrau mais suave | `maxLevel - 1` (mínimo 1) | não tira carta |
| Normal | a da tabela | a da tabela | não tira carta |
| Difícil | 1 degrau mais dura | `minLevel + 1` / `maxLevel + 1` (teto 10) | cobra 1 carta da mão jogada, coleção nunca abaixo de 5 |

Saves no formato 4 ou anterior abrem como Normal.

## Revanche do chefe

A revanche não entra em `region.duels`, então não trava `stageCleared`. Aparece como linha extra depois de vencer o chefe. A carta fixa da primeira vitória na revanche (uma vez, via `GIVEN` / `specialGranted`):

| Chefe | Etapa | Carta da revanche | Níveis (1a luta → revanche) |
| --- | --- | --- | --- |
| Quistis | Balamb Garden | Shiva (84) | 4-5 → 5-6 |
| Fujin | Balamb Town | MiniMog (81) | 5-6 → 6-7 |
| Rinoa | Timber | Gilgamesh (80) | 5-7 → 6-8 |
| Rainha (Deling) | Deling | Leviathan (91) | 6-7 → 7-8 |
| Rainha (Dollet) | Dollet | Odin (92) | 6-8 → 7-9 |
| Dobe e Flo | Fisherman's Horizon | Cerberus (94) | 7-8 → 8-9 |
| Seifer | Esthar | Edea (108) | 8-9 → 9-10 |
| Rainha (final) | Final no Garden | Eden (99) | 8-10 → 9-10 |

Rainha em Balamb Town e em FH não são chefes da cidade (sem carta fixa) e não têm revanche com rara.

## Progresso

A porcentagem é o número de ids únicos da coleção que pertencem ao pool obtível (starter + faixas de nível de todos os duelos, inclusive revanchas, + todas as cartas fixas), de 0 a 100. A tela lista as raras que faltam com a cidade sempre visível; o nome do adversário fica como ??? até aquele duelo (ou a revanche) ser vencido. Vitórias, derrotas e cartas ganhas ficam em `WINS`, `LOSSES` e `CARDWON`.

## Migração do formato 5

Além da migração do formato 3 (deck inicial e stand-ins, abaixo), um save 4 ou mais antigo recebe dificuldade Normal, conjuntos nativos, `PLAYS` zerado, sem `CARRY` e sem visitas da Rainha. As chaves novas são `CAMP.n.DIFFICULTY`, `CITYRULES`, `CARRY`, `PLAYS`, `QUEEN`, `WINS`, `LOSSES` e `CARDWON`.

## Migração

Campanhas em formato 3 ou anterior recebem o deck inicial, as cartas especiais antigas dos duelos já vencidos (mapa legado, inclusive `final` → Bahamut) e um stand-in da faixa de cada adversário já derrotado. Nada do progresso gravado é apagado.

## Simulação

`StoryBalanceTest` compara a força esperada do jogador (melhores 5 cartas do starter + stand-ins e cartas de chefe já obtidos) com a força típica dos adversários daquela etapa (média de 8 rolagens com a semente). A faixa aceita é 70% a 145% da força típica do adversário.
