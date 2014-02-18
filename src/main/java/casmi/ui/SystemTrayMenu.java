package casmi.ui;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;


public class SystemTrayMenu {

    private PopupMenu popup;
    private Frame frame;
    private TrayIcon trayIcon;

    public SystemTrayMenu(URL imageUrl, String name) {
        frame = new Frame("");
        frame.setUndecorated(true);

        // Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            throw new RuntimeException("SystemTray is not supported");
        }

        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(imageUrl), name);

        popup = new PopupMenu();

        trayIcon.setPopupMenu(popup);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && !frame.isVisible()) {
                    frame.add(popup);
                    popup.show(frame, e.getXOnScreen(), e.getYOnScreen());
                } else {
                    frame.remove(popup);
                }
            }
        });
    }

    public void show() {
        frame.setResizable(false);
        frame.setVisible(true);

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException("Failed to initialize SystemTrayMenu");
        }
    }

    public void hide() {
        frame.setVisible(false);
        SystemTray.getSystemTray().remove(trayIcon);
    }

    public void setImage(URL imageUrl) {
        trayIcon.setImage(Toolkit.getDefaultToolkit().getImage(imageUrl));
    }

    public void setIcon(URL iconURL) {
    	frame.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
    }

    public void addMenuItem(String name, final SystemTrayMenuActionListener listener) {
        MenuItem item = new MenuItem(name);

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                listener.performed();
            }
        });

        popup.add(item);
    }

    public MenuItem getMenuItem(int index) {
        return popup.getItem(index);
    }

    public void addSeparator() {
        popup.addSeparator();
    }
}
