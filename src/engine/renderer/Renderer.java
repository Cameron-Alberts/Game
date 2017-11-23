package engine.renderer;

import engine.entity.BaseEntity;
import engine.models.ModelTexture;
import engine.models.TexturedModel;
import engine.shaders.StaticShader;
import engine.util.Math;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

import static engine.loader.Loader.NORMAL_COORDS_ATTRIBUTE_NUMBER;
import static engine.loader.Loader.TEXTURE_COORDS_ATTRIBUTE_NUMBER;
import static engine.loader.Loader.VERTEX_POSITIONS_ATTRIBUTE_NUMBER;

public class Renderer {
    public static final int UNBIND = 0;

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private final Matrix4f projectionMatrix;
    private final StaticShader staticShader;

    public Renderer(final StaticShader staticShader) {
        this.staticShader = staticShader;
        // Do not render faces we can't see
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) (1f / java.lang.Math.tan(java.lang.Math.toRadians(FOV / 2f))) * aspectRatio;
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;

        staticShader.start();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0 /* red */, 0 /* green */, 0 /* blue */, 1 /* alpha */);
    }

    public void render(final Map<TexturedModel, List<BaseEntity>> entitiesToRender) {
        for (TexturedModel texturedModel : entitiesToRender.keySet()) {
            prepareTexturedModel(texturedModel);
            List<BaseEntity> batchEntities = entitiesToRender.get(texturedModel);
            for (BaseEntity entity : batchEntities) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getIndicesCount() /* indices */, GL11.GL_UNSIGNED_INT, 0 /* indices_buffer_offset */);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(final TexturedModel texturedModel) {
        // Enable VAOs
        GL30.glBindVertexArray(texturedModel.getVaoID());
        GL20.glEnableVertexAttribArray(VERTEX_POSITIONS_ATTRIBUTE_NUMBER);
        GL20.glEnableVertexAttribArray(TEXTURE_COORDS_ATTRIBUTE_NUMBER);
        GL20.glEnableVertexAttribArray(NORMAL_COORDS_ATTRIBUTE_NUMBER);
        ModelTexture modelTexture = texturedModel.getModelTexture();
        staticShader.loadShineVariables(modelTexture.getShineDamper(), modelTexture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTexture.getTextureID());
    }

    private void unbindTexturedModel() {
        // Unbind/disable VAO/VBOs
        GL20.glDisableVertexAttribArray(VERTEX_POSITIONS_ATTRIBUTE_NUMBER);
        GL20.glDisableVertexAttribArray(TEXTURE_COORDS_ATTRIBUTE_NUMBER);
        GL20.glDisableVertexAttribArray(NORMAL_COORDS_ATTRIBUTE_NUMBER);
        GL30.glBindVertexArray(UNBIND);
    }

    private void prepareInstance(final BaseEntity entity) {
        Matrix4f transformationMatrix = Math.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotateX(),
                entity.getRotateY(),
                entity.getRotateZ(),
                entity.getScale()
        );
        staticShader.loadTransformationMatrix(transformationMatrix);
    }
}
