package engine.entity;

import engine.models.RawModel;
import engine.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class BaseEntity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private float rotateX;
    private float rotateY;
    private float rotateZ;
    private float scale;

    public BaseEntity(final TexturedModel texturedModel,
                      final Vector3f position,
                      final float rotateX,
                      final float rotateY,
                      final float rotateZ,
                      final float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
        this.scale = scale;
    }

    public void rotate(final float deltaRotateX, final float deltaRotateY, final float deltaRotateZ) {
        rotateX += deltaRotateX;
        rotateY += deltaRotateY;
        rotateZ += deltaRotateZ;
    }

    public void move(final float deltaX, final float deltaY, final float deltaZ) {
        position.x += deltaX;
        position.y += deltaY;
        position.z += deltaZ;
    }

    public RawModel getRawModel() {
        return texturedModel.getRawModel();
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(final TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    public float getRotateX() {
        return rotateX;
    }

    public void setRotateX(final float rotateX) {
        this.rotateX = rotateX;
    }

    public float getRotateY() {
        return rotateY;
    }

    public void setRotateY(final float rotateY) {
        this.rotateY = rotateY;
    }

    public float getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(final float rotateZ) {
        this.rotateZ = rotateZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
