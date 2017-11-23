package game;

import engine.DisplayManager;
import engine.renderer.RendererManager;
import engine.view.Camera;
import engine.entity.BaseEntity;
import engine.view.Light;
import engine.loader.Loader;
import engine.loader.ObjLoader;
import engine.models.RawModel;
import engine.models.TexturedModel;
import engine.renderer.Renderer;
import engine.shaders.StaticShader;
import engine.models.ModelTexture;
import org.lwjgl.LWJGLException;
import org.lwjgl.util.vector.Vector3f;

import java.util.concurrent.TimeUnit;

public class GameRunner {
    public static void main(final String[] args) throws LWJGLException {
        try (final DisplayManager displayManager =  new DisplayManager()) {
            displayManager.create();
            Loader loader = new Loader();
            StaticShader staticShader = new StaticShader();
            Renderer renderer = new Renderer(staticShader);
            RendererManager rendererManager = new RendererManager(staticShader, renderer);
            ObjLoader objLoader = new ObjLoader(loader);

            final long startTime = System.nanoTime();

            Light light = new Light(new Vector3f(0, 0, -10), new Vector3f(1, 1, 1));

            RawModel rawModel = objLoader.loadObjModel("bugatti");
            ModelTexture modelTexture = new ModelTexture(loader.loadTexture("stallTexture"), 10, 5);
            TexturedModel texturedModel = new TexturedModel(rawModel, modelTexture);
            BaseEntity entity = new BaseEntity(
                    texturedModel,
                    new Vector3f(10, 0, -25),
                    0 /* rotateX */,
                    0 /* rotateY */,
                    0 /* rotateZ */,
                    1 /* scale */
            );

            RawModel rawModel1 = objLoader.loadObjModel("stall");
            ModelTexture modelTexture1 = new ModelTexture(loader.loadTexture("stallTexture"),  10, 0);
            TexturedModel texturedModel1 = new TexturedModel(rawModel1, modelTexture1);
            BaseEntity entity1 = new BaseEntity(
                    texturedModel1,
                    new Vector3f(-10, 0, -25),
                    0 /* rotateX */,
                    0 /* rotateY */,
                    0 /* rotateZ */,
                    1 /* scale */
            );

            final long endTime = System.nanoTime();

            System.out.println("Time to load all objects " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");

            Camera camera = new Camera();

            int fps = 0;
            long lastMeasuredSecond = System.nanoTime();
            while (!displayManager.isClosedRequested()) {
                long currentTime = System.nanoTime();
                if ((currentTime - lastMeasuredSecond) <= 1_000_000_000) {
                    fps++;
                } else {
                    System.out.println("FPS: " + fps);
                    lastMeasuredSecond = currentTime;
                    fps = 0;
                }

                entity.rotate(0, 0.4f, 0);
                entity1.rotate(0, 0.4f, 0);
                camera.move();
                rendererManager.processEntity(entity);
                rendererManager.processEntity(entity1);
                rendererManager.render(light, camera);
                displayManager.update();
            }
            staticShader.close();
            loader.close();
        }
    }
}
