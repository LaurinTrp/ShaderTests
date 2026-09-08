# ShaderTests

OpenGL shader experiments in Java, built with [LWJGL](https://www.lwjgl.org/) and GLFW. Each demo lives in its own render pass so you can switch techniques without restructuring the app.

## Requirements

- Java 15+
- Eclipse IDE (or any setup that can compile the Eclipse project)
- Linux natives for LWJGL (included under `lib/lwjgl/`)
- Sibling Eclipse project **GLM** (referenced from `.classpath` as `/GLM`) for math types (`glm.*`)

## Run

1. Import this project and the `GLM` project into Eclipse.
2. Run `GUI.LWJGL_Main` as a Java application from the project root (so `res/` resolves correctly).
3. Press **Esc** to quit.

The window opens fullscreen on the primary monitor.

## Active demo

The currently enabled pass is selected in `Main/Render/Renderer.java`. Comment/uncomment the `*.render()` calls to switch demos.

Right now that is the **compute particle** pipeline (`ComputePass` + `ParticlePass`).

## Demos

| Pass | Package | What it covers |
|------|---------|----------------|
| `TrianglePass` / `RectanglePass` | `Default` | Basic VAO/VBO drawing |
| `MetaBallPass` | `Metaballs` | 2D metaballs driven by mouse position |
| `TexturePass` | `Texture` | Textured quad |
| `TransformationPass` | `Transformation` | Model/view/projection transforms |
| `LightSourcePass` | `Transformation` | Simple lighting |
| `GeometryShader` | `GeometryShader` | Geometry shader stage |
| `ComputePass` + `ParticlePass` | `Compute` | Compute shader + SSBO particle field |

Shaders live under `res/Shader/<DemoName>/`.

## Controls

When camera movement is enabled (`camera.moveCamera()` in `Renderer`):

| Key | Action |
|-----|--------|
| `W` `A` `S` `D` | Move |
| `Q` / `X` | Up / down |
| Mouse | Look |
| `Esc` | Exit |

## Layout

```
Main/
  GUI/          Window + main loop (LWJGL_Main)
  Render/       Renderer, Camera, Model
  Shader/       ShaderProgram helpers
  Utils/        Loader, input, math helpers
Passes/         One package per demo
res/
  Shader/       GLSL sources
  Textures/     Sample images
  Models/       OBJ meshes (e.g. Cube.obj)
lib/
  lwjgl/        LWJGL jars + Linux natives
  IOUtils/      Apache Commons IO
```

## Notes

- Resource paths are resolved from the working directory via `LWJGL_Main.PATHS` (`res/Shader/`, `res/Textures/`, `res/Models/`).
- Bundled LWJGL natives are Linux-only; other platforms need matching natives from LWJGL.
