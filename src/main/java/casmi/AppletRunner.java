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
import java.lang.reflect.Field;

import javax.swing.JFrame;

import casmi.util.OS;
import casmi.util.SystemUtil;

public class AppletRunner {

    static JFrame frame;
    static GraphicsDevice displayDevice;
    
    
    // TODO need to refactoring
    // following static initialization code is duplicated with Applet.java
    static {
        String defaultPath = System.getProperty("java.library.path");
        String newPath = "../casmi/lib/native/";

        OS os = SystemUtil.getOS();
        switch (os) {
        case MAC:
            newPath += "mac";
            break;
        case MAC_64:
            newPath += "mac";
            break;
        case WIN:
            newPath += "win";
            break;
        case WIN_64:
            newPath += "win_64";
            break;
        case LINUX:
            newPath += "linux";
            break;
        case LINUX_64:
            newPath += "linux_64";
            break;
        default:
            break;
        }

        System.setProperty("java.library.path", defaultPath + java.io.File.pathSeparatorChar + newPath);

        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println(System.getProperty("java.library.path"));
    }

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
