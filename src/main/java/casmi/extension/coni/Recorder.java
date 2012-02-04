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

import org.OpenNI.CodecID;
import org.OpenNI.GeneralException;
import org.OpenNI.RecordMedium;
import org.OpenNI.StatusException;

import casmi.extension.coni.exception.CONIException;

public class Recorder {

    private static final CodecID DEFAULT_IMAGE_CODEC = CodecID.Jpeg;
    private static final CodecID DEFAULT_DEPTH_CODEC = CodecID.Z16;
    private static final CodecID DEFAULT_IR_CODEC    = CodecID.Z16;
    
    private final CONI coni;
    private org.OpenNI.Recorder recorder;
    
    public Recorder(CONI coni, File file) throws CONIException {
        this.coni = coni;
        coni.lock = true;
        try {
            recorder = org.OpenNI.Recorder.create(coni.context, null);
            recorder.setDestination(RecordMedium.FILE, file.getAbsolutePath());
            addNodes();
            recorder.Record();
        } catch (GeneralException e) {
            throw new CONIException(e);
        } finally {
            coni.lock = false;
        }
    }
    
    private final void addNodes() throws StatusException {
        if (coni.imageEnabled) {
            recorder.addNodeToRecording(coni.imageGenerator, DEFAULT_IMAGE_CODEC);
        }
        if (coni.depthEnabled) {
            recorder.addNodeToRecording(coni.depthGenerator, DEFAULT_DEPTH_CODEC);
        }
        if (coni.irEnabled) {
            recorder.addNodeToRecording(coni.irGenerator, DEFAULT_IR_CODEC);
        }
     }
    
    public void close() {
        coni.lock = true;
        recorder = null;
        coni.lock = false;
    }
}
