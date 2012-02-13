package casmi.graphics.element;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;

import com.sun.opengl.util.j2d.TextRenderer;

public class TextBox extends Element implements Renderable {

    private static final boolean DEFAULT_FILL         = false;
    private static final Color   DEFAULT_FILL_COLOR   = new RGBColor(ColorSet.BLACK);
    private static final boolean DEFAULT_STROKE       = false;
    private static final Color   DEFAULT_STROKE_COLOR = new RGBColor(ColorSet.WHITE);
    
    private Text text;
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;
    private double width = 0.0;
    private double height = 0.0;
    
    public TextBox(Text text, 
                   double x, double y,
                   double width, double height) {
        
        this(text, x, y, 0.0, width, height);
    }
    
    public TextBox(Text text,
                   double x, double y, double z, 
                   double width, double height) {
        
        this.text   = text;
        this.x      = x;
        this.y      = y;
        this.z      = z;
        this.width  = width;
        this.height = height;
        
        init();
        format();
    }
    
    @Override
    public void render(GL gl, GLU glu, int width, int height) {

        double x1 = x - this.width  / 2.0;
        double y1 = y + this.height / 2.0;
        double x2 = x - this.width  / 2.0;
        double y2 = y - this.height / 2.0;
        double x3 = x + this.width  / 2.0;
        double y3 = y - this.height / 2.0;
        double x4 = x + this.width  / 2.0;
        double y4 = y + this.height / 2.0;
        
        if (fillColor.getAlpha() < 0.001 || strokeColor.getAlpha() < 0.001) {
            gl.glDisable(GL.GL_DEPTH_TEST);
        }
        
        gl.glPushMatrix();
        {
            // fill
            if (fill) {
                getSceneFillColor().setup(gl);
                gl.glBegin(GL.GL_QUADS);
                {
                    gl.glVertex2d(x1, y1);
                    gl.glVertex2d(x2, y2);
                    gl.glVertex2d(x3, y3);
                    gl.glVertex2d(x4, y4);
                }
                gl.glEnd();
            }
            
            // stroke
            if (stroke) {
                gl.glLineWidth(this.strokeWidth);
                getSceneStrokeColor().setup(gl);
                gl.glBegin(GL.GL_LINE_STRIP);
                {
                    gl.glVertex2d(x1, y1);
                    gl.glVertex2d(x2, y2);
                    gl.glVertex2d(x3, y3);
                    gl.glVertex2d(x4, y4);
                    gl.glVertex2d(x1, y1);
                }
                gl.glEnd();
            }
            
            // text
            switch (text.getAlign()) {
            case CENTER:
                text.setX(x);
                break;
            case RIGHT:
                text.setX(x + this.width / 2.0);
                break;
            case LEFT:
            default:
                text.setX(x - this.width / 2.0);
                break;
            }
            text.setY(y + this.height / 2.0 - text.getHeight());
            text.setZ(z);
            text.render(gl, glu, width, height);
        }
        gl.glPopMatrix();
        
        if (fillColor.getAlpha() < 0.001 || strokeColor.getAlpha() < 0.001) {
            gl.glEnable(GL.GL_DEPTH_TEST);
        }
    }
    
    private final void init() {
        this.fill        = DEFAULT_FILL;
        this.fillColor   = DEFAULT_FILL_COLOR;
        this.stroke      = DEFAULT_STROKE;
        this.strokeColor = DEFAULT_STROKE_COLOR;
    }
    
    private final void format() {
        
        String[] strs = text.getArrayText();
        TextRenderer tr = text.getRenderer();
        StringBuilder sb = new StringBuilder();
        
        for (String str : strs) {
            while (1 < str.length() && width < tr.getBounds(str).getWidth()) {
                String tmp = str;
                while (1 < tmp.length() && width < tr.getBounds(tmp).getWidth()) {
                    tmp = tmp.substring(0, tmp.length() - 1);
                }
                sb.append(tmp);
                sb.append('\n');
                str = str.substring(tmp.length());
            }
            sb.append(str);
            sb.append('\n');
        }
        
        text.setArrayText(sb.toString());
    }

    public final Text getText() {
    
        return text;
    }

    public final void setText(Text text) {
    
        this.text = text;
        format();
    }

    public final double getX() {
    
        return x;
    }

    public final void setX(double x) {
    
        this.x = x;
    }

    public final double getY() {
    
        return y;
    }

    public final void setY(double y) {
    
        this.y = y;
    }

    public final double getZ() {
    
        return z;
    }

    public final void setZ(double z) {
    
        this.z = z;
    }

    public final double getWidth() {
    
        return width;
    }

    public final void setWidth(double width) {
    
        this.width = width;
    }

    public final double getHeight() {
    
        return height;
    }

    public final void setHeight(double height) {
    
        this.height = height;
    }
}
