package casmi;

import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;


public class AppletFrame extends JFrame implements ComponentListener{
    private final Applet applet;

    public AppletFrame(GraphicsConfiguration configuration, final Applet applet) {
        super(configuration);

        this.applet = applet;

        this.addComponentListener(this);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
//        Dimension size = this.getSize();
//        applet.setSize((int)size.getWidth(), (int)size.getHeight());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        if (!applet.isFullScreen()) {
            Insets insets = getInsets();
            setSize(applet.getAppletWidth() + insets.left + insets.right,
                    applet.getAppletHeight() + insets.top + insets.bottom);
        } else {
            setSize(applet.getAppletWidth(), applet.getAppletHeight());
        }

        applet.setInitializing(false);
    }
}
