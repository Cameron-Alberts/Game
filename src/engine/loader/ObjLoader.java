package engine.loader;

import engine.models.RawModel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ObjLoader {
    private static final String RESOURCES_PATH = "res";

    private final Loader loader;

    public ObjLoader(final Loader loader) {
        this.loader = loader;
    }

    public RawModel loadObjModel(final String fileName) {
        final long startTime = System.nanoTime();

        String objPath = Paths.get(RESOURCES_PATH, fileName + ".obj").toString();
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        List<Float> normalCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        float[] textureCoordsArray = null;
        float[] normalCoordsArray = null;
        boolean startedProcessingFaces = false;
        try (Scanner scanner = new Scanner(new FileReader(new File(objPath)))) {
            while (scanner.hasNext()) {
                String type = scanner.next(); // read until whitespace
                switch (type) {
                    case "v": // vertex
                        processFloats(scanner, vertices, 3 /* x, y, z */);
                        break;
                    case "vt": // vertex texture
                        processFloats(scanner, textureCoords, 2 /* u, v */);
                        break;
                    case "vn": // vertex normal
                        processFloats(scanner, normalCoords, 3 /* x, y, z */);
                        break;
                    case "f":
                        if (!startedProcessingFaces) {
                            textureCoordsArray = new float[(vertices.size() / 3) * 2];
                            normalCoordsArray = new float[vertices.size()];
                            scanner.useDelimiter("[\\s/]"); // use slashes to delimit between integers in partial face vertices
                            startedProcessingFaces = true;
                        }
                        // Each face has three lists of coords
                        processPartialFace(scanner, textureCoords, normalCoords, indices, textureCoordsArray, normalCoordsArray);
                        processPartialFace(scanner, textureCoords, normalCoords, indices, textureCoordsArray, normalCoordsArray);
                        processPartialFace(scanner, textureCoords, normalCoords, indices, textureCoordsArray, normalCoordsArray);
                        break;
                }
                scanner.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final float[] verticesArray = new float[vertices.size()];
        IntStream.range(0, vertices.size()).forEach(i -> verticesArray[i] = vertices.get(i));
        final int[] indicesArray = indices.stream().mapToInt(Integer::intValue).toArray();

        long endTime = System.nanoTime();

        System.out.println("Time to load object " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");

        return loader.transformRawVectors(verticesArray, textureCoordsArray, normalCoordsArray, indicesArray);
    }

    private static void processPartialFace(final Scanner scanner,
                                           final List<Float> textureCoords,
                                           final List<Float> normalCoords,
                                           final List<Integer> indices,
                                           final float[] textureCoordsArray,
                                           final float[] normalCoordsArray) {
        int vertexIndex = scanner.nextInt() - 1;
        int textureIndex = scanner.nextInt() - 1;
        int normalIndex = scanner.nextInt() - 1;

        indices.add(vertexIndex);
        textureCoordsArray[vertexIndex * 2] = textureCoords.get(textureIndex * 2);
        textureCoordsArray[vertexIndex * 2 + 1] = 1 - textureCoords.get(textureIndex * 2 + 1);
        normalCoordsArray[vertexIndex * 3] = normalCoords.get(normalIndex * 3);
        normalCoordsArray[vertexIndex * 3 + 1] = normalCoords.get(normalIndex * 3 + 1);
        normalCoordsArray[vertexIndex * 3 + 2] = normalCoords.get(normalIndex * 3 + 2);
    }

    private static void processFloats(final Scanner scanner, final List<Float> values, final int numberOfFloats) {
        IntStream.range(0, numberOfFloats).forEach(i -> values.add(scanner.nextFloat()));
    }
}
