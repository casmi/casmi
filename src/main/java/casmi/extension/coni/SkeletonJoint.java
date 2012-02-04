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

/**
 * Enum of skeleton joints.
 *  
 * @author T. Takeuchi
 */
public enum SkeletonJoint {

    HEAD,
    
    NECK,
    
    TORSO,
    
    WAIST,
    
    LEFT_COLLAR,
    
    LEFT_SHOULDER,
    
    LEFT_ELBOW,
    
    LEFT_WRIST,
    
    LEFT_HAND,
    
    LEFT_FINGERTIP,
    
    RIGHT_COLLAR,
    
    RIGHT_SHOULDER,

    RIGHT_ELBOW,
    
    RIGHT_WRIST,
    
    RIGHT_HAND,
    
    RIGHT_FINGERTIP,

    LEFT_HIP,
    
    LEFT_KNEE,
    
    LEFT_ANKLE,
    
    LEFT_FOOT,
    
    RIGHT_HIP,
    
    RIGHT_KNEE,
    
    RIGHT_ANKLE,
    
    RIGHT_FOOT;
    
    /**
     * Converts from casmi.extension.camera.coni.SkeletonJoint to org.OpenNI.SkeletonJoing.
     * 
     * @param joint
     *     casmi.extension.camera.coni.SkeletonJoint.
     * 
     * @return
     *     org.OpenNI.SkeletonJoint.
     */
    static final org.OpenNI.SkeletonJoint toOpenNISkeletonJoint(SkeletonJoint joint) {
        switch(joint) {
        case HEAD:
            return org.OpenNI.SkeletonJoint.HEAD;
        case NECK:
            return org.OpenNI.SkeletonJoint.NECK;
        case TORSO:
            return org.OpenNI.SkeletonJoint.TORSO;
        case WAIST:
            return org.OpenNI.SkeletonJoint.WAIST;
        case LEFT_COLLAR:
            return org.OpenNI.SkeletonJoint.LEFT_COLLAR;
        case LEFT_SHOULDER:
            return org.OpenNI.SkeletonJoint.LEFT_SHOULDER;
        case LEFT_ELBOW:
            return org.OpenNI.SkeletonJoint.LEFT_ELBOW;
        case LEFT_WRIST:
            return org.OpenNI.SkeletonJoint.LEFT_WRIST;
        case LEFT_HAND:
            return org.OpenNI.SkeletonJoint.LEFT_HAND;
        case LEFT_FINGERTIP:
            return org.OpenNI.SkeletonJoint.LEFT_FINGER_TIP;
        case RIGHT_COLLAR:
            return org.OpenNI.SkeletonJoint.RIGHT_COLLAR;
        case RIGHT_SHOULDER:
            return org.OpenNI.SkeletonJoint.RIGHT_SHOULDER;
        case RIGHT_ELBOW:
            return org.OpenNI.SkeletonJoint.RIGHT_ELBOW;
        case RIGHT_WRIST:
            return org.OpenNI.SkeletonJoint.RIGHT_WRIST;
        case RIGHT_HAND:
            return org.OpenNI.SkeletonJoint.RIGHT_HAND;
        case RIGHT_FINGERTIP:
            return org.OpenNI.SkeletonJoint.RIGHT_FINGER_TIP;
        case LEFT_HIP:
            return org.OpenNI.SkeletonJoint.LEFT_HIP;
        case LEFT_KNEE:
            return org.OpenNI.SkeletonJoint.LEFT_KNEE;
        case LEFT_ANKLE:
            return org.OpenNI.SkeletonJoint.LEFT_ANKLE;
        case LEFT_FOOT:
            return org.OpenNI.SkeletonJoint.LEFT_FOOT;
        case RIGHT_HIP:
            return org.OpenNI.SkeletonJoint.RIGHT_HIP;
        case RIGHT_KNEE:
            return org.OpenNI.SkeletonJoint.RIGHT_KNEE;
        case RIGHT_ANKLE:
            return org.OpenNI.SkeletonJoint.RIGHT_ANKLE;
        case RIGHT_FOOT:
            return org.OpenNI.SkeletonJoint.RIGHT_FOOT;
        }
        
        return null; // dummy
    }
}
