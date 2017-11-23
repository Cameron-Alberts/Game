package engine.loader;

import engine.models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static engine.renderer.Renderer.UNBIND;

public class Loader implements AutoCloseable {
    private static final String PNG = "PNG";
    private static final String RESOURCES_PATH = "res";

    public static final int VERTEX_POSITIONS_ATTRIBUTE_NUMBER = 0;
    public static final int TEXTURE_COORDS_ATTRIBUTE_NUMBER = 1;
    public static final int NORMAL_COORDS_ATTRIBUTE_NUMBER = 2;
    public static final int DIMENSIONS = 3;

    private final List<Integer> vaoIDs;
    private final List<Integer> vboIDs;
    private final List<Integer> textureIDs;

    public Loader() {
        vaoIDs = new ArrayList<>();
        vboIDs = new ArrayList<>();
        textureIDs = new ArrayList<>();
    }

    public RawModel transformRawVectors(final float[] vertexPositions,
                                        final float[] textureCoords,
                                        final float[] normalCoords,
                                        final int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(VERTEX_POSITIONS_ATTRIBUTE_NUMBER, DIMENSIONS, vertexPositions);
        storeDataInAttributeList(TEXTURE_COORDS_ATTRIBUTE_NUMBER, DIMENSIONS - 1, textureCoords);
        storeDataInAttributeList(NORMAL_COORDS_ATTRIBUTE_NUMBER, DIMENSIONS, normalCoords);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public int loadTexture(final String fileName) {
        Texture texture = null;
        try {
            String texturePath = Paths.get(RESOURCES_PATH, fileName + ".png").toString();
            texture = TextureLoader.getTexture(PNG, new FileInputStream(texturePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textureIDs.add(textureID);
        return textureID;
    }

    @Override
    public void close() {
        vaoIDs.forEach(GL30::glDeleteVertexArrays);
        vboIDs.forEach(GL15::glDeleteBuffers);
        textureIDs.forEach(GL11::glDeleteTextures);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vaoIDs.add(vaoID);
        return vaoID;
    }

    private void bindIndicesBuffer(final int[] indices) {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIndexBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        vboIDs.add(vboID);
    }

    private void storeDataInAttributeList(final int attributeNumber, final int coordinateSize, final float[] data) {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = transformRawData(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false /* normalized */, 0 /* stride */, 0 /* offset */);
        unbindVBO();
        vboIDs.add(vboID);
    }

    private static void unbindVAO() {
        GL30.glBindVertexArray(UNBIND);
    }

    private static void unbindVBO() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, UNBIND);
    }

    private static FloatBuffer transformRawData(final float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer storeDataInIndexBuffer(final int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
