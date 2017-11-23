package engine.renderer;

import engine.entity.BaseEntity;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import engine.view.Camera;
import engine.view.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RendererManager implements AutoCloseable {
    private final StaticShader staticShader;
    private final Renderer renderer;
    private Map<TexturedModel, List<BaseEntity>> entitiesToRender;

    public RendererManager(final StaticShader staticShader, final Renderer renderer) {
        this.staticShader = staticShader;
        this.renderer = renderer;
        this.entitiesToRender = new HashMap<>();
    }

    public void render(final Light light, final Camera camera) {
        renderer.prepare();
        staticShader.start();
        staticShader.loadLight(light);
        staticShader.loadViewMatrix(camera);
        renderer.render(entitiesToRender);
        staticShader.stop();
        entitiesToRender.clear();
    }

    public void processEntity(final BaseEntity entity) {
        TexturedModel texturedModel = entity.getTexturedModel();
        entitiesToRender.computeIfAbsent(texturedModel, t -> new ArrayList<BaseEntity>())
                .add(entity);
    }

    @Override
    public void close() {
        staticShader.close();
    }
}
