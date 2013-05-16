package casmi.graphics.object;

import java.nio.IntBuffer;

import javax.media.opengl.GL2;


public class FrameBufferObject {

    private int width;
    private int height;

    private int[] texID;
    private int texNumSize;
    private int[] attachments;

    private boolean init;

    private int fboID;
    private int rboID;

    public FrameBufferObject(int width, int height) {
        this.width = width;
        this.height = height;
        this.texNumSize = 1;
        texID = new int[1];
    }

    public FrameBufferObject(int width, int height, int texNumSize) {
        if(this.texNumSize < 1)
            this.texNumSize = 1;
        this.width = width;
        this.height = height;
        this.texNumSize = texNumSize;
        texID = new int[this.texNumSize];
        attachments = new int[this.texNumSize];
    }

    public void init(GL2 gl) {
        this.createTexture(gl);
        this.createFBOandRBO(gl);
        this.init = true;
    }

    public void bindFrameBuffer(GL2 gl) {
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, this.fboID);//描画先の切り替え
    }

    public void unBindFrameBuffer(GL2 gl) {
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
    }

    public void backDrawBuffers(GL2 gl) {
        gl.glDrawBuffer(GL2.GL_BACK);
    }

    public void frontDrawBuffers(GL2 gl) {
        gl.glDrawBuffer(GL2.GL_FRONT);
    }

    public void drawBuffers(GL2 gl) {
        for (int i=0; i<this.texNumSize; i++){
            attachments[i] = GL2.GL_COLOR_ATTACHMENT0 + i;
        }
       gl.glDrawBuffers(this.texNumSize, attachments, 0);
    }

    public int getTextureID() {
        return this.texID[0];
    }

    public int getTextureID(int num) {
        return this.texID[num];
    }

    public int getFboID() {
        return this.fboID;
    }

    public int getRboID() {
        return this.rboID;
    }

    private int genRB(GL2 gl) {
        int[] array = new int[1];
        IntBuffer ib = IntBuffer.wrap(array);
        gl.glGenRenderbuffers(1, ib);
        return ib.get(0);
    }

    private int genFBO(GL2 gl) {
        int[] array = new int[1];
        IntBuffer ib = IntBuffer.wrap(array);
        gl.glGenFramebuffers(1, ib);
        return ib.get(0);
    }

    private void genTexture(GL2 gl) {
        gl.glGenTextures(this.texNumSize, this.texID, 0);
    }

    private void createTexture(GL2 gl)
    {
        genTexture(gl);
        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
        for(int tex: this.texID){
            gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);

            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL2.GL_TRUE);
            gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, this.width, this.height, 0, GL2.GL_RGBA, GL2.GL_FLOAT, null);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        }

    }

    public void createFBOandRBO(GL2 gl)
    {
        fboID = genFBO(gl);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboID);

        rboID = genRB(gl);
        gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, rboID);//バインド

        gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_DEPTH_COMPONENT, this.width, this.height);
        gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);//RBOのデフォルトへバインド

        for(int i=0; i<this.texNumSize; i++)
            gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0 + i, GL2.GL_TEXTURE_2D, texID[i], 0);

        gl.glFramebufferRenderbuffer(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_RENDERBUFFER, rboID);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);//FBOのデフォルトへバインド
        if(checkFramebufferStatus(gl) == false )
            System.out.println("failed to make framebuffer");
    }

    public boolean isInit()
    {
        return this.init;
    }

    private boolean checkFramebufferStatus(GL2 gl)
    {
        int status = gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER);
        switch(status){
          case GL2.GL_FRAMEBUFFER_COMPLETE:
                  System.out.println( "Framebuffer complete.");
                  return true;

          case GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                  System.out.println( "[ERROR] Framebuffer incomplete: Attachment is NOT complete.");
                  return false;

          case GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                  System.out.println( "[ERROR] Framebuffer incomplete: No image is attached to FBO.");
                  return false;

          case GL2.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
                  System.out.println( "[ERROR] Framebuffer incomplete: Attached images have different dimensions.");
                  return false;

          case GL2.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
                  System.out.println( "[ERROR] Framebuffer incomplete: Color attached images have different internal formats.");
                  return false;

          case GL2.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                  System.out.println( "[ERROR] Framebuffer incomplete: Draw buffer.") ;
                  return false;

          case GL2.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
                  System.out.println( "[ERROR] Framebuffer incomplete: Read buffer.");
                  return false;

          case GL2.GL_FRAMEBUFFER_UNSUPPORTED:
                  System.out.println( "[ERROR] Unsupported by FBO implementation.");
                  return false;

          default:
                  System.out.println( "[ERROR] Unknow error.");
                  return false;
          }
    }


}
