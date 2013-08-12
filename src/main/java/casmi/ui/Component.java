package casmi.ui;

import java.awt.Dimension;

import javax.swing.JComponent;


abstract public class Component {

    protected JComponent instance = null;

    public Component() {
    }

    public JComponent getInstance() {
        return instance;
    }

    public void setWidth(int width) {
        if (instance != null) {
            Dimension d = instance.getPreferredSize();
            instance.setPreferredSize(new Dimension(width, (int) d.getHeight()));

            d = instance.getMaximumSize();
            instance.setMaximumSize(new Dimension(width, (int) d.getHeight()));
        }
    }
}
