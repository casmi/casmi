/*
 *   casmi
 *   http://casmi.github.io/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 *  casmi is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package casmi.graphics.shader;

import java.nio.IntBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import casmi.graphics.object.FrameBufferObject;

/**
 * BlurShader.
 *
 * @author Y. Ban
 */
public class BlurShader {

    private int width;
    private int height;
    public Shader blurShader = new Shader("Blur");
    public Shader motionBlurShader;
    private FrameBufferObject fbo;
    private boolean init = false;

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public BlurShader(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init(GL2 gl) {
        this.blurShader.initShaders(gl);
        fbo = new FrameBufferObject(this.width, this.height);
        fbo.init(gl);
        this.init = true;
    }

    public boolean isInit() {
        return this.init;
    }

    public void disableShader() {
        this.blurShader.disableShader();
    }

    public void enableShader(GL2 gl) {
        this.blurShader.enableShader(gl);
    }

    public void blur(FrameBufferObject fbo1, FrameBufferObject fbo2, GL2 gl, GLU glu) {
        blur(fbo1, gl, glu);
        gl.glActiveTexture(GL2.GL_TEXTURE2);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo2.getTextureID());
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        this.blurShader.setUniform("motion", 1.0f);
        this.blurShader.setUniform("motionMask", 2);
    }

    public void blur(FrameBufferObject fbo1, GL2 gl, GLU glu) {
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo1.getTextureID(0));
        gl.glActiveTexture(GL2.GL_TEXTURE1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo1.getTextureID(1));
        // gl.glActiveTexture(GL2.GL_TEXTURE2);
        // gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo1.getTextureID(2));

        this.blurShader.enableShader(gl);
        this.blurShader.setUniform("sampler", 0);
        this.blurShader.setUniform("mask", 1);
        this.blurShader.setUniform("path", 0.0f);

        fbo.bindFrameBuffer(gl);

        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        this.drawPlaneFillScreen(gl, glu);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);

        gl.glColor4f(0, 0, 0, 1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
        // gl.glActiveTexture(GL2.GL_TEXTURE0);
        // gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo1.getTextureID(0));
        // gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glActiveTexture(GL2.GL_TEXTURE0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo.getTextureID());
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        gl.glActiveTexture(GL2.GL_TEXTURE1);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, fbo1.getTextureID(1));
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
        this.blurShader.setUniform("sampler2", 0);
        this.blurShader.setUniform("mask", 1);
        this.blurShader.setUniform("path", 1.0f);
        this.blurShader.setUniform("motion", 0.0f);
    }

    public void drawPlaneFillScreen(GL2 gl, GLU glu) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        IntBuffer viewPorts = IntBuffer.allocate(4);
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPorts);
        gl.glViewport(0, 0, this.width, this.height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho(0, this.width, 0, this.height, -1.0e10, 1.0e10);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glBegin(GL2.GL_POLYGON);
        {
            gl.glTexCoord2d(0, 0);
            gl.glVertex2d(0, 0);
            gl.glTexCoord2d(0, 1);
            gl.glVertex2d(0, this.height);
            gl.glTexCoord2d(1, 1);
            gl.glVertex2d(this.width, this.height);
            gl.glTexCoord2d(1, 0);
            gl.glVertex2d(this.width, 0);
        }
        gl.glEnd();
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        int[] viewArray = viewPorts.array();
        gl.glViewport(viewArray[0], viewArray[1], viewArray[2], viewArray[3]);
    }
}