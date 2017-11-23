package engine.models;

public class ModelTexture {
    private final int textureID;
    private final float shineDamper;
    private final float reflectivity;

    public ModelTexture(final int textureID) {
        this.textureID = textureID;
        this.shineDamper = 1;
        this.reflectivity = 0;
    }

    public ModelTexture(final int textureID,
                        final float shineDamper,
                        final float reflectivity) {
        this.textureID = textureID;
        this.shineDamper = shineDamper;
        this.reflectivity = reflectivity;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }
}
