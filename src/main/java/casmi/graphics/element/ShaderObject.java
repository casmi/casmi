package casmi.graphics.element;

import casmi.graphics.shader.Shader;


abstract public class ShaderObject {

    protected Shader shader;
    protected boolean enableShader = false;
    protected Texture normalMap;

    public void setShader(Shader shader) {
        this.shader = shader;
        this.enableShader = true;
    }

    public void enableShader() {
        this.enableShader = true;
    }

    public void disableShader() {
        this.enableShader = false;
    }

    public boolean isEnableShader() {
        return this.enableShader;
    }

    public void setNormalMap(Texture normal) {
        this.normalMap = normal;
    }

}
