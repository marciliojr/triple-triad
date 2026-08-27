# Changelog

Todas as mudanças relevantes deste fork são registradas aqui.

O formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/), e o projeto adota [SemVer](https://semver.org/lang/pt-BR/).

## [Unreleased]

## [0.6.0] - 2026-08-26

Meu Deck, persistência da run do Campeonato, confirmação ao sair da partida, libGDX 1.14.2 e esteira de CI/Release.

### Added

- Esc na partida abre confirmação (Não / Sim; default Não) para evitar saída acidental
- Aviso na tela ao salvar o progresso do Campeonato no pick
- **Meu Deck**: álbum permanente (duplicatas ok; preview no tamanho da partida; Del remove)
- Campeonato: **Novo jogo** (pack se o deck estiver vazio, senão pick do álbum), **Continuar**, **Apagar progresso** (não zera o deck)
- Save da run: `RUN_ROUND` / `RUN_BAG` / `RUN_HAND` / `RUN_OPPONENT` (Salvar no pick)
- Jogo Rápido pode montar 5 cartas a partir de Meu Deck
- Empate no Campeonato refaz a luta com as 5 originais de cada lado (sem misturar donos)
- Menu: Jogo Rápido, Campeonato, Meu Deck, Versus, Como Jogar, Configurações
- Distribuição `./gradlew distZip` inclui `res/` e `cards/` (artefato da GitHub Release)
- Esteira GitHub Actions: build no PR; tag SemVer e Release no merge na `master`

### Changed

- libGDX **1.13.1 → 1.14.2**
- Assets também resolvem a partir da pasta de instalação do zip (`lib/../res`, `lib/../cards`)

## [0.5.0] - 2026-08-26

Modo Campeonato: gauntlet de 8 vitórias, coleção persistente e troca de cartas a partir da 2ª luta.

### Added

- Item de menu **Campeonato** (8 rounds contra a IA)
- Coleção no perfil (`COLLECTION`, `CHAMPIONSHIP_WINS`), com duplicatas
- Pacote inicial de 5 cartas com pelo menos um lado A
- Troca de cartas da 2ª luta em diante (jogador escolhe; IA pega a de maior soma de ranks)
- Lobby com as mesmas regras do Jogo Rápido

### Changed

- Esc no campeonato aborta a run sem aplicar o roubo da partida atual
- F5 (rematch) só funciona no Jogo Rápido

## [0.4.0] - 2026-08-26

Tela de Configurações, idioma da interface e regras na seleção de deck.

### Added

- Item de menu **Configurações**: música de fundo, som do cursor e idioma (português do Brasil, inglês, espanhol)
- Persistência `MUSIC_ENABLED`, `CURSOR_SOUND` e `LANGUAGE` em `.triple-triad.cfg`
- Painel de regras na tela de Jogo Rápido (Open, Same, Same Wall, Plus, Combo, Elemental, Sudden Death)
- Tela **Como - Jogar** com manual resumido no idioma escolhido

### Changed

- Textos de menu, perfil, decks e dicas passam por `I18n`
- Fonte inclui glifos extras do espanhol (`ñ`, `¿`, `¡`)

## [0.3.0] - 2026-08-26

Sobe o cliente de Java 17 para **Java 21**. Sem mudança de regras, telas ou assets.

### Changed

- `sourceCompatibility` / `targetCompatibility` 21 no Gradle
- `.classpath` em JavaSE-21
- README, requisitos e memory bank alinhados ao JDK 21

## [0.2.0] - 2026-08-26

Modernização do cliente desktop: build Gradle, Java 17, libGDX e meta-jogo local. As regras de captura (Same, Plus, Combo, Elemental, Sudden Death) e a IA não foram reescritas.

### Added

- Build Gradle (`./gradlew run` / `./gradlew build`) com wrapper 9.1.0
- Perfil obrigatório persistido em `.triple-triad-profile`
- Menu principal (Jogo Rápido; Campeonato e Versus reservados)
- Construtor e seletor de decks de 5 cartas
- Camada gráfica `itdelatrisu.tripletriad.gfx` sobre libGDX 1.13.1 (janela, input, sprites, fonte TTF com glifos PT-BR, BGM/SFX)
- Fonte menor para menus e dicas
- Memory bank em `memory-bank/`

### Changed

- Linguagem-alvo de Java 7 para **Java 17**
- Apresentação de Slick2D + LWJGL 2.9.1 para **libGDX** (LWJGL 3 via backend)
- Assets continuam em `res/` e `cards/`, resolvidos pelo cwd da raiz
- `.classpath` aponta para JavaSE-17
- README descreve o fluxo atual e o build Gradle

### Removed

- Dependência de JARs locais `lib/slick.jar`, `lib/lwjgl-2.9.1.jar`, `lib/jorbis-0.0.17-2.jar`
- Natives LWJGL 2 em `native/`
- Ant como caminho oficial de build (`build.xml` permanece no repo, sem suporte)

### Fixed

- Esc na partida volta ao menu em vez de encerrar o processo
- F5 faz rematch com o mesmo deck do Jogo Rápido
- `isGameOver()` tolera mãos ainda não inicializadas (telas de menu)

## [0.1.1] - 2014-08-01

Release original de Jeffrey Han ([itdelatrisu/triple-triad](https://github.com/itdelatrisu/triple-triad)): Slick2D, LWJGL 2, Ant/Eclipse, partida imediata contra a IA.
