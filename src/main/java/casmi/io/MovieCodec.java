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
package casmi.io;

import com.xuggle.xuggler.ICodec;

/**
 * Codec of an output movie.
 * 
 * @author T. Takeuchi
 */
public enum MovieCodec {

    H264,
    
    MPEG4,
    
    /** wmv3. */
    WMV,
    
    /** flv1. */
    FLV,
    
    XVID;
    
    public static final MovieCodec getDefaultCodec() {
        return MPEG4;
    }
    
    public static final ICodec.ID toXugglerCodec(MovieCodec codec) {
        switch (codec) {
        case H264:
            return ICodec.ID.CODEC_ID_H264;
        case MPEG4:
            return ICodec.ID.CODEC_ID_MPEG4;
        case WMV:
            return ICodec.ID.CODEC_ID_WMV3;
        case FLV:
            return ICodec.ID.CODEC_ID_FLV1;
        case XVID:
            return ICodec.ID.CODEC_ID_XVID;
        }
        
        return null; // dummy
    }
}
