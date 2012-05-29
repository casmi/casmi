/*   casmi examples
 *   http://casmi.github.com/
 *   Copyright (C) 2011, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package casmi.extension.bvh;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import casmi.matrix.Matrix3D;
import casmi.matrix.Vertex;

/**
 * BvhParser Class.
 * 
 * @author Y. Ban
 */

public class BvhParser {

  private Boolean motionLoop;
  
  private int currentFrame = 0;
  
  private List<BvhLine> lines;
  
  private int currentLine;
  private BvhBone currentBone;
  
  private BvhBone rootBone;
  private List<List<Float>> frames;
  private int nbFrames;
  private float frameTime;
  
  private BvhEndCallback endCallback;
  private boolean end = false;
  
  private List<BvhBone> bones;
  
  public BvhParser()
  {
    motionLoop = true;
  }

  /**
   * if set to True motion will loop at end
   */
  public Boolean getMotionLoop()
  {
    return motionLoop;
  }
  
  /**
   * set Loop state
   * @param value
   */
  public void setMotionLoop(Boolean value)
  {
    motionLoop = value;
  }

  /**
   * to string
   * @return
   */
  public String toStr()
  {
	return rootBone.structureToString();
  }
  
  /**
   * get frame total
   * @return
   */
  public int getNbFrames()
  {
    return nbFrames;
  }

  /**
   * get bones list
   * @return
   */
  public List<BvhBone> getBones()
  {
    return bones;
  }


  /**
   * call before parse BVH
   * 	create array instance
   * 	and setloopstatus true
   */
  public void init()
  {
    bones = new ArrayList<BvhBone>();
    motionLoop = true;
  }
  
  /**
   * go to the frame at index
   */
  int preindex=0;
  public void moveFrameTo(int index)
  {
    if(!motionLoop)
    {
      if(index >= nbFrames)
        currentFrame = nbFrames-1;//last frame
      	callEndCallback();
    }else{
      while (index >= nbFrames){
        index -= nbFrames;     
      }

      if(preindex >= index){
    	  callEndCallback();
      } else {
    	  end = false;
      }
      preindex = index;
      
      currentFrame = index; //looped frame
    }
    updateFrame();
  }

  /**
   * go to millisecond of the BVH
   * @param mills millisecond
   * @param loopSec the default loopsec for 
   */
  public void moveMsTo( int mills )
  {
    float currentFrameTime = frameTime * 1000;
    int curFrame = (int)(mills / currentFrameTime); 
    moveFrameTo( curFrame ); 
  }
  
  /**
   * update bone position and rotation
   */
  public void update()
  {
	  update( getBones().get(0) );
  }
  
  public void addEndCallback(BvhEndCallback bec){
	  this.endCallback = bec;
  }
  
  public void callEndCallback(){
	  if(end==false){
		  end = true;
		  if(this.endCallback!=null)
			  this.endCallback.run();
	  }
  }
  

  protected void update(BvhBone bone )
  {
	   
	    Matrix3D m = new Matrix3D();
	    m.translate(bone.getXposition(), bone.getYposition(), bone.getZposition());
	    m.translate(bone.getOffsetX(), bone.getOffsetY(), bone.getOffsetZ());
	    
	    m.rotateY(Math.toRadians(bone.getYrotation()));
	    m.rotateX(Math.toRadians(bone.getXrotation()));
	    m.rotateZ(Math.toRadians(bone.getZrotation()));
	    
	    bone.global_matrix = m;

	    if (bone.getParent() != null && bone.getParent().global_matrix != null)
	      m.preApply(bone.getParent().global_matrix);
	    
	    bone.setAbsPosition(m.mult(new Vertex()));
	    
	    
	    if (bone.getChildren().size() > 0)
	    {
	      for (BvhBone child : bone.getChildren())
	      {
	        update(child);
	      }
	    }
	    else
	    {
	      m.translate(bone.getEndOffsetX(), bone.getEndOffsetY(), bone.getEndOffsetZ());
	      bone.setAbsEndPosition(m.mult(new Vertex()));
	     }
  }
  
  
  private void updateFrame()
  {
    if (currentFrame >= frames.size()) return;
    List<Float> frame = frames.get(currentFrame);
    int count = 0;
    for (float n : frame)
    {
      BvhBone bone = getBoneInFrameAt(count);
      String prop = getBonePropInFrameAt(count);
      if(bone != null) {
        Method getterMethod;
        try {
          getterMethod = bone.getClass().getDeclaredMethod("set".concat(prop), new Class[]{float.class});
          getterMethod.invoke(bone, n);
        } catch (SecurityException e) {
          e.printStackTrace();
          System.err.println("ERROR WHILST GETTING FRAME - 1");
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
          System.err.println("ERROR WHILST GETTING FRAME - 2");
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
          System.err.println("ERROR WHILST GETTING FRAME - 3");
        } catch (IllegalAccessException e) {
          e.printStackTrace();
          System.err.println("ERROR WHILST GETTING FRAME - 4");
        } catch (InvocationTargetException e) {
          e.printStackTrace();
          System.err.println("ERROR WHILST GETTING FRAME - 5");
        }
      }
      count++;
    }      
  }    
  
  private String getBonePropInFrameAt(int n)
  {
    int c = 0;      
    for (BvhBone bone : bones)
    {
      if (c + bone.getNbChannels() > n)
      {
        n -= c;
        return bone.getChannels().get(n);
      }else{
        c += bone.getNbChannels();  
      }
    }
    return null;
  }
  
  private BvhBone getBoneInFrameAt( int n)
  {
    int c = 0;      
    for (BvhBone bone : bones)
    {
      c += bone.getNbChannels();
      if ( c > n )
        return bone;
    }
    return null;
  }    
  
  public void parse(String[] srces)
  {
    String[] linesStr = srces;
    // liste de BvhLines
    lines = new ArrayList<BvhLine>();
    
    for ( String lineStr : linesStr){
      lines.add(new BvhLine(lineStr));
    }
      
    currentLine = 1;
    rootBone = parseBone();
    
    // center locs
    //_rootBone.offsetX = _rootBone.offsetY = _rootBone.offsetZ = 0; 
    
    parseFrames();
  }    
  
  private void parseFrames()
  {
    int currentLineNum = currentLine;
    for (; currentLineNum < lines.size(); currentLineNum++)
      if(lines.get(currentLineNum).getLineType() == BvhLine.MOTION) break; 

    if ( lines.size() > currentLineNum) 
    {
      currentLineNum++; //Frames
      nbFrames = lines.get(currentLineNum).getNbFrames();
      currentLineNum++; //FrameTime
      frameTime = lines.get(currentLineNum).getFrameTime();
      currentLineNum++;
  
      frames = new ArrayList<List<Float>>();
      for (; currentLineNum < lines.size(); currentLineNum++)
      {
        frames.add(lines.get(currentLineNum).getFrames());
      }
    }
  }
  
  private BvhBone parseBone()
  {
    //_currentBone is Parent
    BvhBone bone = new BvhBone( currentBone );
    
    bones.add(bone);
    
    bone.setName(  lines.get(currentLine).boneName ); //1
    
    // +2 OFFSET
    currentLine++; // 2 {
    currentLine++; // 3 OFFSET
    bone.setOffsetX( lines.get(currentLine).getOffsetX() );
    bone.setOffsetY( lines.get(currentLine).getOffsetY() );
    bone.setOffsetZ( lines.get(currentLine).getOffsetZ() );
      
    // +3 CHANNELS
    currentLine++;
    bone.setnbChannels( lines.get(currentLine).getNbChannels() );
    bone.setChannels( lines.get(currentLine).getChannelsProps() );
      
    // +4 JOINT or End Site or }
    currentLine++;
    while(currentLine < lines.size())
    {
      String lineType = lines.get(currentLine).getLineType();
      if ( BvhLine.BONE.equals( lineType ) ) //JOINT or ROOT
      {
        BvhBone child = parseBone(); //generate new BvhBONE
        child.setParent( bone );
        bone.getChildren().add(child);
      }
      else if( BvhLine.END_SITE.equals( lineType ) )
      {
        currentLine++; // {
        currentLine++; // OFFSET
        bone.setEndOffsetX( lines.get(currentLine).getOffsetX() );
        bone.setEndOffsetY( lines.get(currentLine).getOffsetY() );
        bone.setEndOffsetZ( lines.get(currentLine).getOffsetZ() );
        currentLine++; //}
        currentLine++; //}
        return bone;
      } 
      else if( BvhLine.BRACE_CLOSED.equals( lineType ) )
      {
        return bone; //}
      }
      currentLine++;
    }
    System.out.println("//Something strage");
    return bone;  
  }    
  
  private class BvhLine
  {
  
    public static final String HIERARCHY = "HIERARCHY";
    public static final String BONE = "BONE";
    public static final String BRACE_OPEN = "BRACE_OPEN";
    public static final String BRACE_CLOSED = "BRACE_CLOSED";
    public static final String OFFSET = "OFFSET";
    public static final String CHANNELS = "CHANNELS";
    public static final String END_SITE = "END_SITE";
    
    public static final String MOTION = "MOTION";
    public static final String FRAMES = "FRAMES";
    public static final String FRAME_TIME = "FRAME_TIME";
    public static final String FRAME = "FRAME";
    
    
    public static final String BONE_TYPE_ROOT = "ROOT";
    public static final String BONE_TYPE_JOINT = "JOINT";
    
    private String lineStr;
    
    private String lineType;
    private String boneType;
    
    private String boneName;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private int nbChannels;
    private List<String> channelsProps;
    private int lineNbFrames;
    private float lineFrameTime;
    private List<Float> lineFrames;
    
    public String toString() 
    {
      return lineStr;
    }
    
    private void parse(String lStr)
    {
      lineStr = lStr;
      lineStr = lineStr.trim();
      lineStr = lineStr.replace("\t", "");
      lineStr = lineStr.replace("\n", "");
      lineStr = lineStr.replace("\r", "");  
      
      String[] words = lineStr.split(" ");
    
      lineType = parseLineType(words);
      
  //    
      if ( HIERARCHY.equals(lineType) )
      {
        return;
      } else if ( BONE.equals(lineType) ) {
          boneType = (words[0] == "ROOT") ? BONE_TYPE_ROOT : BONE_TYPE_JOINT;
          boneName = words[1];
          return;
      } else if ( OFFSET.equals(lineType) ) {
          offsetX = Float.valueOf(words[1]);
          offsetY = Float.valueOf(words[2]);
          offsetZ = Float.valueOf(words[3]);
          return;
      } else if ( CHANNELS.equals(lineType) ) {
          nbChannels = Integer.valueOf(words[1]);
          channelsProps = new ArrayList<String>();
          for (int i = 0; i < nbChannels; i++)
            channelsProps.add(words[i+2]);
          return;
        
      } else if (FRAMES.equals(lineType) ) {
          lineNbFrames = Integer.valueOf(words[1]);
          return;
      } else if ( FRAME_TIME.equals(lineType) ) {
          lineFrameTime = Float.valueOf(words[2]);
          return;
      } else if ( FRAME.equals(lineType) ) {
          lineFrames = new ArrayList<Float>();
          for (String word : words) lineFrames.add(Float.valueOf(word));
          return;
      } else if ( END_SITE.equals(lineType) ||
            BRACE_OPEN.equals(lineType) ||
            BRACE_CLOSED.equals(lineType) ||
            MOTION.equals(lineType)) {
          return;
      }
    }  
    
    private String parseLineType( String[] words) {
      if ( "HIERARCHY".equals(words[ 0 ] ) )
        return HIERARCHY;
      if ( "ROOT".equals(words[ 0 ] ) ||
          "JOINT".equals(words[ 0 ] ) )
        return BONE;
      if ( "{".equals(words[ 0 ] ) )
        return BRACE_OPEN;
      if ( "}".equals(words[ 0 ] ) )
        return BRACE_CLOSED;
      if ( "OFFSET".equals(words[ 0 ] ) )
        return OFFSET;
      if ( "CHANNELS".equals(words[ 0 ] ) )
        return CHANNELS;
      if ( "End".equals(words[ 0 ] ) )
        return END_SITE;
      if ( "MOTION".equals(words[ 0 ] ) )
        return MOTION;
      if ( "Frames:".equals(words[ 0 ] ) )
        return FRAMES;
      if ( "Frame".equals(words[ 0 ] ) )
        return FRAME_TIME;
    
      try {
        Float.parseFloat(words[0]); //check is Parsable
        return FRAME;  
      } catch ( NumberFormatException e) {
        e.printStackTrace();
      }
      return null;
    }
  
    
    public BvhLine(String lStr)
    {
      parse(lStr);
    }
    
    public List<Float> getFrames()
    {
      return lineFrames;
    }
    
    public float getFrameTime()
    {
      return lineFrameTime;
    }
    
    public int getNbFrames()
    {
      return lineNbFrames;
    }
    
    public List<String> getChannelsProps()
    {
      return channelsProps;
    }
    
    public int getNbChannels()
    {
      return nbChannels;
    }
    
    public float getOffsetZ()
    {
      return offsetZ;
    }
    
    public float getOffsetY()
    {
      return offsetY;
    }
    
    public float getOffsetX()
    {
      return offsetX;
    }
    
    @SuppressWarnings("unused")
	public String getBoneName()
    {
      return boneName;
    }
    
    @SuppressWarnings("unused")
	public String getBoneType()
    {
      return boneType;
    }
    
    public String getLineType()
    {
      return lineType;
    }
  }
}
