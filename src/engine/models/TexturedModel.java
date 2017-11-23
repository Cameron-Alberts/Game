package engine.models;

public class TexturedModel {
    private final RawModel rawModel;
    private final ModelTexture modelTexture;

    public TexturedModel(final RawModel rawModel,
                         final ModelTexture modelTexture) {
        this.rawModel = rawModel;
        this.modelTexture = modelTexture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }

    public int getVaoID() {
        return rawModel.getVaoID();
    }
}
