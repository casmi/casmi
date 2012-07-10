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


import org.OpenNI.ActiveHandEventArgs;
import org.OpenNI.CalibrationProgressEventArgs;
import org.OpenNI.CalibrationStartEventArgs;
import org.OpenNI.Context;
import org.OpenNI.DepthGenerator;
import org.OpenNI.DepthMetaData;
import org.OpenNI.GeneralException;
import org.OpenNI.GestureGenerator;
import org.OpenNI.GestureProgressEventArgs;
import org.OpenNI.GestureRecognizedEventArgs;
import org.OpenNI.HandsGenerator;
import org.OpenNI.IObservable;
import org.OpenNI.IObserver;
import org.OpenNI.IRGenerator;
import org.OpenNI.IRMetaData;
import org.OpenNI.ImageGenerator;
import org.OpenNI.ImageMetaData;
import org.OpenNI.InactiveHandEventArgs;
import org.OpenNI.MapOutputMode;
import org.OpenNI.PixelFormat;
import org.OpenNI.Point3D;
import org.OpenNI.PoseDetectionCapability;
import org.OpenNI.PoseDetectionEventArgs;
import org.OpenNI.PoseDetectionInProgressEventArgs;
import org.OpenNI.ProductionNode;
import org.OpenNI.SceneMetaData;
import org.OpenNI.SkeletonCapability;
import org.OpenNI.SkeletonJointPosition;
import org.OpenNI.SkeletonProfile;
import org.OpenNI.StatusException;
import org.OpenNI.UserEventArgs;
import org.OpenNI.UserGenerator;

import casmi.extension.coni.exception.CONIException;
import casmi.extension.coni.exception.CONIRuntimeException;
import casmi.extension.coni.listener.GestureListener;
import casmi.extension.coni.listener.HandListener;
import casmi.extension.coni.listener.PoseDetectionListener;
import casmi.extension.coni.listener.SkeletonListener;
import casmi.extension.coni.listener.UserListener;
import casmi.matrix.Vertex;

/**
 * A base class of CONI (casmi OpenNI).
 * 
 * @author T. Takeuchi
 */
public class CONI {

    private final CONI instance;
    
    final Context context;
    
    ImageGenerator imageGenerator;
    DepthGenerator depthGenerator;
    IRGenerator    irGenerator;
    
    private UserGenerator           userGenerator;
    private GestureGenerator        gestureGenerator;
    private HandsGenerator          handGenerator;
    private SkeletonCapability      skeletonCap;
    private PoseDetectionCapability poseDetectionCap;
    
    boolean imageEnabled = false;
    boolean depthEnabled = false;
    boolean irEnabled    = false;
    private boolean userEnabled     = false;
    private boolean gestureEnabled  = false;
    private boolean handEnabled    = false;
    private boolean skeletonEnabled = false;
    
    private UserListener          userListener;
    private GestureListener       gestureListener;
    private HandListener          handListener;
    private SkeletonListener      skeletonListener;
    private PoseDetectionListener poseDetectionListener;
    
    private ImageMap imageMap;
    private DepthMap depthMap;
    private IRMap    irMap;
    private UserMap  userMap;
    
    boolean lock = false;
    
    /**
     * Constructor.
     */
    public CONI() {
        instance = this;
        try {
            context = new Context();
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void enableImage(int width, int height, int fps) {
        disableImage();
        try {
            innerEnableImage(width, height, fps);
            imageEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    void innerEnableImage(int width, int height, int fps) throws GeneralException {
        imageGenerator = ImageGenerator.create(context);
        MapOutputMode mom = new MapOutputMode(width, height, fps);
        imageGenerator.setMapOutputMode(mom);
        imageGenerator.setPixelFormat(PixelFormat.RGB24);
        imageGenerator.startGenerating();
    }
    
    public final void disableImage() {
        if (imageGenerator != null) {
            try {
                imageGenerator.stopGenerating();
            } catch (StatusException e) {
                // ignore
            }
            imageGenerator = null;
            imageEnabled = false;
        }
    }
    
    /**
     * Returns true if image data is enabled.
     * 
     * @return
     *     Returns true if image data is enabled.
     */
    public final boolean isImageEnabled() {
        return imageEnabled;
    }
    
    public final void enableDepth(int width, int height, int fps) {
        disableDepth();
        try {
            innerEnableDepth(width, height, fps);
            depthEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    void innerEnableDepth(int width, int height, int fps) throws GeneralException {
        depthGenerator = DepthGenerator.create(context);
        MapOutputMode mom = new MapOutputMode(width, height, fps);
        depthGenerator.setMapOutputMode(mom);
        depthGenerator.startGenerating();
    }
    
    public final void disableDepth() {
        if (depthGenerator != null) {
            try {
                depthGenerator.stopGenerating();
            } catch (StatusException e) {
                // ignore
            }
            depthGenerator = null;
            depthEnabled = false;
        }
    }
    
    /**
     * Returns true if depth data is enabled.
     * 
     * @return
     *     Returns true if depth data is enabled.
     */
    public final boolean isDepthEnabled() {
        return depthEnabled;
    }
    
    public final void enableIR(int width, int height, int fps) {
        disableIR();
        try {
            innerEnableIR(width, height, fps);
            irEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    void innerEnableIR(int width, int height, int fps) throws GeneralException {
        irGenerator = IRGenerator.create(context);
        MapOutputMode mom = new MapOutputMode(width, height, fps);
        irGenerator.setMapOutputMode(mom);
        irGenerator.startGenerating();
    }
    
    public final void disableIR() {
        if (irGenerator != null) {
            try {
                irGenerator.stopGenerating();
            } catch (StatusException e) {
                // ignore
            }
            irGenerator = null;
            irEnabled = false;
        }
    }
    
    /**
     * Returns true if infrared rays (IR) data is enabled.
     * 
     * @return
     *     Returns true if IR data is enabled.
     */
    public final boolean isIREnabled() {
        return irEnabled;
    }
    
    public final void enableUser() {
        disableUser();
        
        if (!depthEnabled) {
            throw new CONIRuntimeException("must enable depth previously");
        }
        
        try {
            userGenerator = UserGenerator.create(context);
            userGenerator.getNewUserEvent().addObserver(new IObserver<UserEventArgs>() {
                @Override
                public void update(IObservable<UserEventArgs> observable, UserEventArgs args) {
                    if (userListener != null) {
                        userListener.newUser(instance, args.getId());
                    }
                }
            });
            userGenerator.getLostUserEvent().addObserver(new IObserver<UserEventArgs>() {
                @Override
                public void update(IObservable<UserEventArgs> observable, UserEventArgs args) {
                    if (userListener != null) {
                        userListener.lostUser(instance, args.getId());
                    }
                }
            });
            userGenerator.startGenerating();
            userEnabled = true;
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void disableUser() {
        if (userGenerator != null) {
            try {
                userGenerator.stopGenerating();
            } catch (StatusException e) {
                // ignore
            }
            userListener = null;
            userGenerator = null;
        }
        userEnabled = false;
    }
    
    /**
     * Returns true if user tracking is enabled.
     * 
     * @return
     *     Returns true if user tracking is enabled.
     */
    public final boolean isUserEnabled() {
        return userEnabled;
    }
    
    public final void enableSkeleton() {
        try {
            skeletonCap = userGenerator.getSkeletonCapability();
            skeletonCap.getCalibrationCompleteEvent().addObserver(
                new IObserver<CalibrationProgressEventArgs>() {
                    @Override
                    public void update(IObservable<CalibrationProgressEventArgs> observable, 
                                       CalibrationProgressEventArgs args) {
                        if (skeletonListener != null) {
                            skeletonListener.calibrationComplete(instance, 
                                                                 args.getUser(),
                                                                 CalibrationStatus.valueOf(args.getStatus()));
                        }
                    }
                });
            skeletonCap.getCalibrationInProgressEvent().addObserver(
                new IObserver<CalibrationProgressEventArgs>() {
                    @Override
                    public void update(IObservable<CalibrationProgressEventArgs> observable, 
                                       CalibrationProgressEventArgs args) {
                        if (skeletonListener != null) {
                            // todo
                        }
                    }
                });
            skeletonCap.getCalibrationStartEvent().addObserver(
                new IObserver<CalibrationStartEventArgs>() {
                    @Override
                    public void update(IObservable<CalibrationStartEventArgs> observable, 
                                       CalibrationStartEventArgs args) {
                        if (skeletonListener != null) {
                            skeletonListener.calibrationStart(instance, args.getUser());
                        }
                    }
                });
            
            poseDetectionCap = userGenerator.getPoseDetectionCapability();
            poseDetectionCap.getOutOfPoseEvent().addObserver(new IObserver<PoseDetectionEventArgs>() {
                @Override
                public void update(IObservable<PoseDetectionEventArgs> observable, 
                                   PoseDetectionEventArgs args) {
                    if (poseDetectionListener != null) {
                        poseDetectionListener.outOfPose(instance);
                    }
                }
            });
            poseDetectionCap.getPoseDetectedEvent().addObserver(new IObserver<PoseDetectionEventArgs>() {
                @Override
                public void update(IObservable<PoseDetectionEventArgs> observable, 
                                   PoseDetectionEventArgs args) {
                    if (poseDetectionListener != null) {
                        poseDetectionListener.poseDetected(instance, args.getPose(), args.getUser());
                    }
                }
            });
            poseDetectionCap.getPoseDetectionInProgressEvent().addObserver(new IObserver<PoseDetectionInProgressEventArgs>() {
                @Override
                public void update(IObservable<PoseDetectionInProgressEventArgs> observable, 
                                   PoseDetectionInProgressEventArgs args) {
                    if (poseDetectionListener != null) {
                        poseDetectionListener.poseDetectionInProgress(instance, args.getUser());
                    }
                }
            });
            
            skeletonCap.setSkeletonProfile(SkeletonProfile.ALL);
            skeletonEnabled = true;
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void disableSkeleton() {
        skeletonListener      = null;
        poseDetectionListener = null;
        skeletonCap           = null;
        poseDetectionCap      = null;
        skeletonEnabled       = false;
    }
    
    /**
     * Returns true if skeleton tracking is enabled.
     * 
     * @return
     *     Returns true if skeleton tracking is enabled.
     */
    public final boolean isSkeletonEnabled() {
        return skeletonEnabled;
    }
    
    public void enableGesture(Gesture... gesture) {
        disableGesture();
        try {
            gestureGenerator = GestureGenerator.create(context);
            for (Gesture g : gesture) {
                gestureGenerator.addGesture(Gesture.toString(g));
            }
            gestureGenerator.getGestureRecognizedEvent().addObserver(new IObserver<GestureRecognizedEventArgs>() {
                @Override
                public void update(IObservable<GestureRecognizedEventArgs> observable, GestureRecognizedEventArgs args) {
                    if (gestureListener != null) {
                        Gesture gesture = Gesture.toGesture(args.getGesture());
                        Vertex idPosition = CONIUtil.toVertex(args.getIdPosition());
                        Vertex endPosition = CONIUtil.toVertex(args.getEndPosition());
                        gestureListener.gestureRecognized(instance, gesture, idPosition, endPosition);
                    }
                }
            });
            gestureGenerator.getGestureProgressEvent().addObserver(new IObserver<GestureProgressEventArgs>() {
                @Override
                public void update(IObservable<GestureProgressEventArgs> observable, GestureProgressEventArgs args) {
                    if (gestureListener != null) {
                        Gesture gesture = Gesture.toGesture(args.getGesture());
                        Vertex position = CONIUtil.toVertex(args.getPosition());
                        gestureListener.gestureProgress(instance, gesture, position, args.getProgress());
                    }
                }
            });
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
        gestureEnabled = true;
    }
    
    public void disableGesture() {
        if (gestureGenerator != null) {
            try {
                gestureGenerator.stopGenerating();
            } catch (StatusException e) {
                // ignore
            }
            gestureGenerator = null;
        }
        gestureListener = null;
        gestureEnabled = false;
    }
    
    public boolean isGestureEnabled() {
        return gestureEnabled;
    }
    
    public void enableHand() {
        disableHand();
        
        try {
            handGenerator = HandsGenerator.create(context);
            handGenerator.getHandCreateEvent().addObserver(new IObserver<ActiveHandEventArgs>() {
                @Override
                public void update(IObservable<ActiveHandEventArgs> observable, ActiveHandEventArgs args) {
                    Vertex position = CONIUtil.toVertex(args.getPosition());
                    handListener.handCreate(instance, args.getId(), position, args.getTime());
                }
            });
            handGenerator.getHandDestroyEvent().addObserver(new IObserver<InactiveHandEventArgs>() {
                @Override
                public void update(IObservable<InactiveHandEventArgs> observable, InactiveHandEventArgs args) {
                    handListener.handDestroy(instance, args.getId(), args.getTime());
                }
            });
            handGenerator.getHandUpdateEvent().addObserver(new IObserver<ActiveHandEventArgs>() {
                @Override
                public void update(IObservable<ActiveHandEventArgs> observable, ActiveHandEventArgs args) {
                    Vertex position = CONIUtil.toVertex(args.getPosition());
                    handListener.handUpdate(instance, args.getId(), position, args.getTime());
                }
            });
            handGenerator.startGenerating();
        } catch (GeneralException e) {
            throw new CONIRuntimeException(e);
        }
        handEnabled = true;
    }
    
    public void disableHand() {
        if (handGenerator != null) {
            try {
                handGenerator.stopGenerating();
            } catch (StatusException e) {
                // ignore
            }
            handGenerator = null;
        }
        handListener = null;
        handEnabled = false;
    }
    
    public boolean isHandEnabled() {
        return handEnabled;
    }
    
    public void update() throws CONIException {
        if (lock) return;
        
        try {
            context.waitAnyUpdateAll();
            
            if (imageMap != null) imageMap.update();
            if (depthMap != null) depthMap.update();
            if (irMap    != null) irMap.update();
            if (userMap  != null) userMap.update();
        } catch (StatusException e) {
            throw new CONIException(e);
        }
    }
    
    /**
     * Retrieves ImageMap instance.
     * Requires calling {@link #enableImage(int, int, int)} previously.
     * 
     * @return
     *     ImageMap instance.
     * 
     * @see casmi.extension.coni.ImageMap
     */
    public ImageMap getImageMap() {
        ImageMetaData imd = imageGenerator.getMetaData();
        if (imageMap == null) {
            imageMap = new ImageMap(imd);
        }
        return imageMap;
    }
    
    /**
     * Retrieves DepthMap instance.
     * Requires calling {@link #enableDepth(int, int, int)} previously.
     * 
     * @return
     *     DepthMap instance.
     * 
     * @see casmi.extension.coni.DepthMap
     */
    public DepthMap getDepthMap() {
        DepthMetaData dmd = depthGenerator.getMetaData();
        if (depthMap == null) {
            depthMap = new DepthMap(dmd);
        }
        return depthMap;
    }
    
    /**
     * Retrieves ImageMap instance.
     * Requires calling {@link #enableIR(int, int, int)} previously.
     * 
     * @return
     *     IRMap instance.
     * 
     * @see casmi.extension.coni.IRMap
     */
    public IRMap getIRMap() {
        if (irGenerator == null) {
            throw new CONIRuntimeException("must enable IR previously");
        }
        
        IRMetaData irmd = irGenerator.getMetaData();
        if (irMap == null) {
            irMap = new IRMap(irmd);
        }
        return irMap;
    }
    
    /**
     * Retrieves UserMap instance.
     * Requires calling {@link #enableUser()} previously.
     * 
     * @return
     *     UserMap instance.
     * 
     * @see casmi.extension.coni.UserMap
     */
    public UserMap getUserMap() {
        SceneMetaData smd = userGenerator.getUserPixels(0);
        if (userMap == null) {
            userMap = new UserMap(smd);
        }
        return userMap;
    }
    
    public final int[] getUsers() {
        if (userGenerator == null) {
            return new int[0];
        }
        
        try {
            return userGenerator.getUsers();
        } catch (StatusException e) {
            return new int[0];
        }
    }
    
    public final Vertex getUserCoM(int userID) {
        if (userGenerator == null) {
            throw new CONIRuntimeException("must enable user previously");
        }
        
        try {
            Point3D p = userGenerator.getUserCoM(userID);
            return new Vertex(p.getX(), p.getY(), p.getZ());
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final Joint getJoint(int userID, SkeletonJoint skeletonJoint) {
        if (skeletonCap == null) {
            throw new CONIRuntimeException("must enable skeleton previously");
        }
        
        try {
            SkeletonJointPosition sjp = 
                skeletonCap.getSkeletonJointPosition(userID,
                                                     SkeletonJoint.toOpenNISkeletonJoint(skeletonJoint));
           
            Vertex v =  new Vertex((double)sjp.getPosition().getX(),
                                   (double)sjp.getPosition().getY(),
                                   (double)sjp.getPosition().getZ());
            double c = (double)sjp.getConfidence();
            
            return new Joint(v, c);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final boolean isMirror() {
        return context.getGlobalMirror();
    }
    
    public final void setMirror(boolean mirror) {
        try {
            context.setGlobalMirror(mirror);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void setImageViewpointToDepth() {
        if (imageGenerator == null || depthGenerator == null) {
            throw new CONIRuntimeException("must enable image and depth previously");
        }
        setViewpoint(imageGenerator, depthGenerator);
    }
    
    public final void resetImageViewpoint() {
        if (imageGenerator == null) {
            throw new CONIRuntimeException("must enable image previously");
        }
        resetViewpoint(imageGenerator);
    }
    
    public final void setDepthViewpointToImage() {
        if (imageGenerator == null || depthGenerator == null) {
            throw new CONIRuntimeException("must enable image and depth previously");
        }
        setViewpoint(depthGenerator, imageGenerator);
    }
    
    public final void resetDepthViewpoint() {
        if (depthGenerator == null) {
            throw new CONIRuntimeException("must enable depth previously");
        }
        resetViewpoint(depthGenerator);
    }
    
    private final void setViewpoint(ProductionNode from, ProductionNode to) {
        try {
            if (from instanceof ImageGenerator) {
                ((ImageGenerator)from).getAlternativeViewpointCapability().setViewpoint(to);
            } else if (from instanceof DepthGenerator) {
                ((DepthGenerator)from).getAlternativeViewpointCapability().setViewpoint(to);
            } else if (from instanceof IRGenerator) {
                ((IRGenerator)from).getAlternativeViewpointCapability().setViewpoint(to);
            } else if (from instanceof UserGenerator) {
                ((UserGenerator)from).getAlternativeViewpointCapability().setViewpoint(to);
            }
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    private final void resetViewpoint(ProductionNode node) {
        try {
            if (node instanceof ImageGenerator) {
                ((ImageGenerator)node).getAlternativeViewpointCapability().resetViewpoint();
            } else if (node instanceof DepthGenerator) {
                ((DepthGenerator)node).getAlternativeViewpointCapability().resetViewpoint();
            } else if (node instanceof IRGenerator) {
                ((IRGenerator)node).getAlternativeViewpointCapability().resetViewpoint();
            } else if (node instanceof UserGenerator) {
                ((UserGenerator)node).getAlternativeViewpointCapability().resetViewpoint();
            }
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final Vertex convertRealWorldToProjective(Vertex vertex) {
        Point3D p = new Point3D((float)vertex.getX(),
                                (float)vertex.getY(),
                                (float)vertex.getZ());
        try {
            p = depthGenerator.convertRealWorldToProjective(p);
            Vertex v = new Vertex((double)p.getX(),
                                  (double)p.getY(),
                                  (double)p.getZ());
            return v;
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final Vertex convertProjectiveToRealWorld(Vertex vertex) {
        Point3D p = new Point3D((float)vertex.getX(),
                                (float)vertex.getY(),
                                (float)vertex.getZ());
        try {
            p = depthGenerator.convertProjectiveToRealWorld(p);
            Vertex v = new Vertex((double)p.getX(),
                                  (double)p.getY(),
                                  (double)p.getZ());
            return v;
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public void addGesture(Gesture... gestures) {
        if (!gestureEnabled) {
            throw new CONIRuntimeException("must enable gesture previously");
        }
        
        try {
            for (Gesture g : gestures) {
                gestureGenerator.addGesture(Gesture.toString(g));
            }
        } catch (StatusException e) {
            throw new CONIRuntimeException();
        }
    }
    
    public void removeGesture(Gesture... gestures) {
        if (!gestureEnabled) {
            throw new CONIRuntimeException("must enable gesture previously");
        }
        
        try {
            for (Gesture g : gestures) {
                gestureGenerator.removeGesture(Gesture.toString(g));
            }
        } catch (StatusException e) {
            throw new CONIRuntimeException();
        }
    }
    
    public void startHandTracking(Vertex position) throws CONIException {
        if (!handEnabled) {
            throw new CONIRuntimeException("must enable hand previously");
        }
        
        try {
            handGenerator.StartTracking(CONIUtil.toPoint3D(position));
        } catch (StatusException e) {
            throw new CONIException(e);
        }
    }
    
    public void stopHandTracking(int userID) throws CONIException {
        if (!handEnabled) {
            throw new CONIRuntimeException("must enable hand previously");
        }
        
        try {
            handGenerator.StopTracking(userID);
        } catch (StatusException e) {
            throw new CONIException(e);
        }
    }
    
    public void stopHandTrackingAll() throws CONIException {
        if (!handEnabled) {
            throw new CONIRuntimeException("must enable hand previously");
        }
        
        try {
            handGenerator.StopTrackingAll();
        } catch (StatusException e) {
            throw new CONIException(e);
        }
    }
    
    public final void startPoseDetection(int userID) {
        if (skeletonCap == null || poseDetectionCap == null) {
            throw new CONIRuntimeException("must enable skeleton previously");
        }
        
        try {
            poseDetectionCap.startPoseDetection(skeletonCap.getSkeletonCalibrationPose(), userID);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void stopPoseDetection(int userID) {
        if (poseDetectionCap == null) {
            throw new CONIRuntimeException("must enable skeleton previously");
        }
        
        try {
            poseDetectionCap.stopPoseDetection(userID);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void requestSkeletonCalibration(int userID) {
        if (skeletonCap == null) {
            throw new CONIRuntimeException("must enable skeleton previously");
        }
        
        try {
            skeletonCap.requestSkeletonCalibration(userID);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void startSkeletonTracking(int userID) {
        if (skeletonCap == null) {
            throw new CONIRuntimeException("must enable skeleton previously");
        }
        
        try {
            skeletonCap.startTracking(userID);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final void stopSkeletonTracking(int userID){
        if (skeletonCap == null) {
            throw new CONIRuntimeException("must enable skeleton previously");
        }
        
        try {
            skeletonCap.stopTracking(userID);
        } catch (StatusException e) {
            throw new CONIRuntimeException(e);
        }
    }
    
    public final boolean isSkeletonCalibrating(int userID) {
        return skeletonCap.isSkeletonCalibrating(userID);
    }
    
    public final boolean isSkeletonCalibrated(int userID) {
        return skeletonCap.isSkeletonCalibrated(userID);
    }
    
    public final boolean isSkeletonTracking(int userID) {
        return skeletonCap.isSkeletonTracking(userID);
    }
    
    public void addUserListener(UserListener listener) {
        this.userListener = listener;
    }
    
    public void addGestureListener(GestureListener listener) {
        this.gestureListener = listener;
    }
    
    public void addHandListener(HandListener listener) {
        this.handListener = listener;
    }
    
    public void addSkeletonListener(SkeletonListener listener) {
        this.skeletonListener = listener;
    }
    
    public void addPoseDetectionListener(PoseDetectionListener listener) {
        this.poseDetectionListener = listener;
    }
    
//    public final int getVersion() {
//        // TODO
//        return 0;
//    }
}
