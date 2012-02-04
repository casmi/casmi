/*
 *   casmi
 *   http://casmi.github.com/
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

package casmi.extension.coni;

import java.io.File;

import org.OpenNI.DepthGenerator;
import org.OpenNI.GeneralException;
import org.OpenNI.IRGenerator;
import org.OpenNI.ImageGenerator;
import org.OpenNI.NodeType;
import org.OpenNI.StatusException;

import casmi.extension.coni.exception.CONIException;
import casmi.extension.coni.exception.CONIRuntimeException;

public class Player extends CONI {

    private final org.OpenNI.Player player;
    
    public Player(File file) throws CONIException {
        super();
        
        try {
            context.openFileRecordingEx(file.getAbsolutePath());
            player = (org.OpenNI.Player)context.findExistingNode(NodeType.PLAYER);
        } catch (GeneralException e) {
            throw new CONIException(e);
        }
    }
    
    public void enableImage() {
        disableImage();
        try {
            innerEnableImage(0, 0, 0);
            imageEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    @Override
    void innerEnableImage(int width, int height, int fps) throws GeneralException {
        imageGenerator = (ImageGenerator)context.findExistingNode(NodeType.IMAGE);
        imageGenerator.startGenerating();
    }
    
    public void enableDepth() {
        disableDepth();
        try {
            innerEnableDepth(0, 0, 0);
            depthEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    @Override
    void innerEnableDepth(int width, int height, int fps) throws GeneralException {
        depthGenerator = (DepthGenerator)context.findExistingNode(NodeType.DEPTH);
        depthGenerator.startGenerating();
    }
    
    public void enableIR() {
        disableIR();
        try {
            innerEnableIR(0, 0, 0);
            irEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    @Override
    void innerEnableIR(int width, int height, int fps) throws GeneralException {
        irGenerator = (IRGenerator)context.findExistingNode(NodeType.IR);
        irGenerator.startGenerating();
    }
    
    public void setRepeat(boolean repeat) {
        try {
            player.setRepeat(repeat);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
}
