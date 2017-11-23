package engine.models;

public class RawModel {
    private final int vaoID;
    private final int indicesCount;

    public RawModel(final int vaoID, final int indicesCount) {
        this.vaoID = vaoID;
        this.indicesCount = indicesCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getIndicesCount() {
        return indicesCount;
    }
}
