package casmi.graphics.shader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import javax.media.opengl.GL2;

public class Shader {

    private int shaderprogram;
    private String vartName;
    private String fragName;
    private GL2 gl;
    private boolean isInit = false;
    private HashMap<String, Integer> uniformMap;
    private HashMap<String, Integer> attribMap;

    public Shader(String shaderName) {
        this.vartName = "/shaders/" + shaderName + "Vert.glsl";
        this.fragName = "/shaders/" + shaderName + "Frag.glsl";
        uniformMap = new HashMap<String, Integer>();
        attribMap = new HashMap<String, Integer>();
    }

    public Shader(String vertName, String fragName) {
        this.vartName = vertName;
        this.fragName = fragName;
        uniformMap = new HashMap<String, Integer>();
        attribMap = new HashMap<String, Integer>();
    }

    public void resetShaders(GL2 gl) {
        isInit = false;
        initShaders(gl);
    }


    public void initShaders(GL2 gl) {
        if (!isInit) {
            System.out.println("init");
            try {
                isInit = true;
                this.gl = gl;
                int v = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
                int f = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

                String vsrc;
                vsrc = readFromStream(Shader.class.getResourceAsStream(this.vartName));

                gl.glShaderSource(v, 1, new String[] {vsrc}, (int[])null, 0);
                gl.glCompileShader(v);

                String fsrc = readFromStream(Shader.class.getResourceAsStream(this.fragName));
                gl.glShaderSource(f, 1, new String[] {fsrc}, (int[])null, 0);
                gl.glCompileShader(f);

                shaderprogram = gl.glCreateProgram();
                gl.glAttachShader(shaderprogram, v);
                gl.glAttachShader(shaderprogram, f);
                gl.glLinkProgram(shaderprogram);
                gl.glValidateProgram(shaderprogram);
                System.out.println("loaded!!");

            } catch (IOException e) {
                System.out.println("load Error!!");
                e.printStackTrace();
            }
        }
    }

    public void setAttribLocation(String attribName) {
        this.enableShader();
        int uni = this.gl.glGetAttribLocation(shaderprogram, attribName);
        attribMap.put(attribName, uni);
    }

    public void setVertexAttrib3(String attribName, double val[]) {
        enableShader();
        if(attribMap.get(attribName)==null)
            setAttribLocation(attribName);
        gl.glVertexAttrib3dv(attribMap.get(attribName), val, 0);
    }

    public void setUniformLocation(String uniformName) {
        enableShader();
        int uni = this.gl.glGetUniformLocation(shaderprogram, uniformName);
        uniformMap.put(uniformName, uni);
    }

    public void setUniform(String uniformName, float val) {
        enableShader();
        if(uniformMap.get(uniformName)==null)
            setUniformLocation(uniformName);
        gl.glUniform1f(uniformMap.get(uniformName), val);
    }

    public void setUniform(String uniformName, int val) {
        enableShader();
        if(uniformMap.get(uniformName)==null)
            setUniformLocation(uniformName);
        gl.glUniform1i(uniformMap.get(uniformName), val);
    }

    public void setUniform(String uniformName, int val[]) {
        enableShader();
        gl.glUniform1iv(uniformMap.get(uniformName), val.length, val, 0);
    }

    public void setUniform(String uniformName, float val[]) {
        enableShader();
        gl.glUniform1fv(uniformMap.get(uniformName), val.length, val, 0);
    }

    public void enableShader() {
        gl.glUseProgram(shaderprogram);
    }

    public void disableShader() {
        gl.glUseProgram(0);

    }

    public int getShaderProgram() {
        return shaderprogram;
    }

    public void enableShader(GL2 gl) {
        if(this.gl!=gl)
            this.resetShaders(gl);
        gl.glUseProgram(shaderprogram);
    }

    public void disableShader(GL2 gl) {
        gl.glUseProgram(0);

    }

    public static String readFromStream(InputStream ins) throws IOException {
        if (ins == null) {
            throw new IOException("Could not read from stream.");
        }
        StringBuffer buffer = new StringBuffer();
        Scanner scanner = new Scanner(ins);
        try {
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine() + "\n");
            }
        } finally {
            scanner.close();
        }

        return buffer.toString();
    }

}
