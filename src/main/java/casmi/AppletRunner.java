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

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;

import javax.swing.JFrame;

/**
 * @author T. Aoki
 */
public class AppletRunner {

    static JFrame frame;
    static GraphicsDevice displayDevice;

    public static void run(String className, String title) {

        final Applet applet;
        try {
            Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
            applet = (Applet)c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        runApplet(applet, title);
    }

    private static void runApplet(Applet applet, String title) {

        applet.setRunAsApplication(true);

        if (displayDevice == null) {
            GraphicsEnvironment environment =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
            displayDevice = environment.getDefaultScreenDevice();
        }

        frame = new JFrame(displayDevice.getDefaultConfiguration());
        frame.setTitle(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setResizable(true);

        frame.setLayout(null);

        frame.add(applet);
        
        applet.init();

        frame.pack();
        
        if (!applet.isFullScreen()) {
            Insets insets = frame.getInsets();
            frame.setSize(applet.getWidth() + insets.left + insets.right,
                applet.getHeight() + insets.top + insets.bottom);
        } else {
            frame.setSize(applet.getWidth(), applet.getHeight());
        }

        frame.setBackground(Color.BLACK); // TODO better to setup applet's default background color
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
