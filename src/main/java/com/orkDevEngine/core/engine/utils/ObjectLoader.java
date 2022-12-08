package com.orkDevEngine.core.engine.utils;

import com.orkDevEngine.core.engine.game.objects.Model;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class ObjectLoader {

    private static final Logger logger = LoggerFactory.getLogger(ObjectLoader.class);

    private static final List<Integer> vaos = new ArrayList<>();
    private static final List<Integer> vbos = new ArrayList<>();
    private static final List<Integer> textures = new ArrayList<>();

    public Model loadOBJModel(String filename) {

        logger.debug("loading model from obj file...{}", filename);

        List<String> lines = Utilities.readAllLine(filename);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3i> faces = new ArrayList<>();

        for(String line: lines) {
            String[] tokens = line.split("\\s+");
            try {
                switch (tokens[0]) {
                    case "v" -> {
                        Vector3f verts = new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        );
                        vertices.add(verts);
                    }
                    case "vt" -> {
                        Vector2f vertTextures = new Vector2f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2])
                        );
                        textures.add(vertTextures);
                    }
                    case "vn" -> {
                        Vector3f norms = new Vector3f(
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]),
                                Float.parseFloat(tokens[3])
                        );
                        normals.add(norms);
                    }
                    case "f" -> {
                        faces.add(processFaces(tokens[1]));
                        faces.add(processFaces(tokens[2]));
                        faces.add(processFaces(tokens[3]));
                    }
                    default -> {
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        if(normals.size() == 0) {

            Vector3f[] normalMap = generateNormals(faces, vertices);

            logger.debug("Obj model {} doesn't contain vertex normals. generating...", filename);

            for (Vector3f vector3f : normalMap)
                normals.add(vector3f.normalize());

            logger.debug("Generated {} normals for {}", normalMap.length, filename);
        }

        logger.debug("Loaded {} vertices",vertices.size());

        List<Integer> indices = new ArrayList<>();
        float[] verticesArr = new float[vertices.size()*3];
        IntStream.range(0, vertices.size()).forEachOrdered(i -> {
            verticesArr[i * 3] = vertices.get(i).x;
            verticesArr[i * 3 + 1] = vertices.get(i).y;
            verticesArr[i * 3 + 2] = vertices.get(i).z;
        });

        float[] texArr = new float[vertices.size()*2];
        float[] normalArr = new float[vertices.size()*3];

        for(Vector3i face: faces){
            processVertex(face.x, face.y, face.z, textures, normals, indices, texArr, normalArr);
        }

        int[] indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();

        return loadModel(verticesArr, texArr, normalArr, indicesArr);
    }
    private static void processVertex(int pos,
                                      int texCoord,
                                      int normal,
                                      List<Vector2f> texCoordList,
                                      List<Vector3f> normalList,
                                      List<Integer> indexList,
                                      float[] texArr,
                                      float[] normalArr) {
        indexList.add(pos);

        if(texCoord >= 0) {
            Vector2f texVec = texCoordList.get(texCoord);
            texArr[pos * 2] = texVec.x;
            texArr[pos * 2 + 1] = texVec.y;
        }

        if(normal >= 0) {
            Vector3f normalVec = normalList.get(normal);
            normalArr[pos * 3] = normalVec.x;
            normalArr[pos * 3 + 1] = normalVec.y;
            normalArr[pos * 3 + 2] = normalVec.z;
        }
    }
    private static Vector3i processFaces(String token) {
        String[] lineToken = token.split("/");
        int length = lineToken.length;
        int pos = -1, coords = -1, normal = -1;
        pos = Integer.parseInt(lineToken[0]) - 1;
        if(length > 1) {
            String textCoord = lineToken[1];
            coords = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : -1;
            if(length > 2) {
                normal = Integer.parseInt(lineToken[2]) - 1;
            }
        }
        return new Vector3i(pos, coords, normal);
    }

    private static Vector3f[] generateNormals(List<Vector3i> faces, List<Vector3f> vertices){

        Vector3f[] normalMap = new Vector3f[vertices.size()];

        for(int i = 0; i < faces.size(); i+=3) {
            int[] points = new int[3];
            Vector3f[] pointPos = new Vector3f[3];

            points[0] = faces.get(i).get(0);
            points[1] = faces.get(i + 1).get(0);
            points[2] = faces.get(i + 2).get(0);

            pointPos[0] = new Vector3f(vertices.get(points[0]));
            pointPos[1] = new Vector3f(vertices.get(points[1]));
            pointPos[2] = new Vector3f(vertices.get(points[2]));

            Vector3f edge0 = new Vector3f();
            Vector3f edge1 = new Vector3f();
            pointPos[0].sub(pointPos[1], edge0);
            pointPos[2].sub(pointPos[1], edge1);
            Vector3f triNormal = edge1.cross(edge0);

            faces.get(i).z = points[0];
            faces.get(i + 1).z = points[1];
            faces.get(i + 2).z = points[2];

            if(normalMap[points[0]] != null)
                normalMap[points[0]].add(triNormal, normalMap[points[0]]);
            else
                normalMap[points[0]] = triNormal;

            if(normalMap[points[1] ] != null)
                normalMap[points[1] ].add(triNormal, normalMap[points[1] ]);
            else
                normalMap[points[1]] = triNormal;

            if(normalMap[points[2]] != null)
                normalMap[points[2]].add(triNormal, normalMap[points[2]]);
            else
                normalMap[points[2]] = triNormal;
        }

        return normalMap;
    }

    public Model loadModel(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInAttribList(0, 3, vertices);
        storeDataInAttribList(1, 2, textureCoords);
        storeDataInAttribList(2, 3, normals);

        unbind();
        return new Model(id, indices.length);
    }

    public int loadTexture(String filename) throws Exception{
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w,h,c,4);

            if(buffer == null)
                throw new Exception("Texture File "  + filename + " not loaded " + STBImage.stbi_failure_reason());

            width = w.get();
            height = h.get();
        }

        int id = glGenTextures();
        textures.add(id);
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVAO() {
        int id = glGenVertexArrays();
        vaos.add(id);
        glBindVertexArray(id);
        return id;

    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utilities.storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }
    private void storeDataInAttribList(int attribNo, int vertexCount, float[] data) {
        int vbo = glGenBuffers();
        vbos.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utilities.storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attribNo, vertexCount, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {

    }

    public void cleanUp() {
        for (int vao: vaos){
            glDeleteVertexArrays(vao);
        }
        for (int vbo: vbos){
            glDeleteBuffers(vbo);
        }
        for (int texture: textures){
            glDeleteTextures(texture);
        }
    }
}
