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

    public SystemTrayMenu(URL imageUrl, String name) {
        final Frame frame = new Frame("");
        frame.setUndecorated(true);

        // Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            throw new RuntimeException("SystemTray is not supported");
        }

        final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(imageUrl), name);
        final SystemTray tray = SystemTray.getSystemTray();

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

        frame.setResizable(false);
        frame.setVisible(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {
            throw new RuntimeException("Failed to initialize SystemTrayMenu");
        }
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

    public void addSeparator() {
        popup.addSeparator();
    }
}
