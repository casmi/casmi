package casmi;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
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

    public void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point center = new Point(screenSize.width / 2 - applet.getAppletWidth() / 2,
                                 screenSize.height / 2 - applet.getAppletHeight() / 2);
        setLocation(center);
    }
}
