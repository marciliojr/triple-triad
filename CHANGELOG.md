# Changelog

Todas as mudanças relevantes deste fork são registradas aqui.

O formato segue [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/), e o projeto adota [SemVer](https://semver.org/lang/pt-BR/).

## [Unreleased]

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
