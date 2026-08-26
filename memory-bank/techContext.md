# Tech context

## Runtime e build

- JDK **21+** para compilar e rodar (`source/target` 21)
- Gradle Wrapper 9.1.0 (o daemon sobe em JVM 17–25; o jogo exige 21)
- `./gradlew run` com `workingDir` na raiz
- Dependências no Maven Central: libGDX 1.13.1, backend `lwjgl3`, `natives-desktop`, FreeType
- Entry point: `itdelatrisu.tripletriad.TripleTriad`

## Assets

- `res/` — UI, sprites, BGM (`bgm.ogg`), SFX, fonte `OpenSans-Light.ttf`, ícones
- `cards/` — `deck.txt` e `{id:03d}.png`
- Resolução via `itdelatrisu.tripletriad.gfx.Assets` (cwd, não classpath)

## Camada gráfica

Pacote `itdelatrisu.tripletriad.gfx`: fachada no estilo Slick2D (`Image`, `Graphics`, `Input`, `UnicodeFont`, `Animation`, `Music`, `Sound`, `BasicGame`) sobre SpriteBatch / ShapeRenderer / FreeType / OpenAL.

Câmera y-down (`OrthographicCamera.setToOrtho(true)`), como o Slick.

## O que não usar

- Slick2D, LWJGL 2, JARs em `lib/` para o jogo, pasta `native/` antiga
- Misturar libGDX com LWJGL 3 direto
- Ant (`build.xml`) como build oficial
