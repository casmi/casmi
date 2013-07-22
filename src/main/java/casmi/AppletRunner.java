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

package casmi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.UIManager;

import casmi.exception.CasmiRuntimeException;
import casmi.util.OS;
import casmi.util.SystemUtil;

/**
 * @author Takashi AOKI <federkasten@me.com>
 *
 */
public class AppletRunner {

    public static AppletFrame run(String className, String title) {
        final Applet applet;

        try {
            Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
            initBeforeCreateApplet(title);
            applet = (Applet)c.newInstance();
        } catch (Exception e) {
            throw new CasmiRuntimeException("Failed to create instance of " + className, e);
        }

        return runApplet(applet, title);
    }

    public static AppletFrame run(final Applet applet, String title) {
        return runApplet(applet, title);
    }

    private static void initBeforeCreateApplet(String title) {
        OS os = SystemUtil.getOS();
        if (os == OS.MAC || os == OS.MAC_64) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", title);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore and use Swing UI.
        }
    }

    private static AppletFrame runApplet(Applet applet, String title) {
        applet.setRunAsApplication(true);

        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice displayDevice = environment.getDefaultScreenDevice();

        AppletFrame frame = new AppletFrame(displayDevice.getDefaultConfiguration(), applet);
        applet.setWindowFrame(frame);

        frame.setTitle(title);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setResizable(true);

        frame.setLayout(new BorderLayout());

        frame.add(applet, BorderLayout.CENTER);

        applet.setDisplayDevice(displayDevice);

        applet.init();

        frame.pack();

        frame.setBackground(Color.BLACK);  // TODO better to setup applet's default background color
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }
}
