package com.unlikepaladin.pfm.compat.fabric;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URI;

public class MissingDependencyScreen {

    public static void Screen(String reason, String url) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println(reason);
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ignored) {
                // Ignored
            }

            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                int option = JOptionPane.showOptionDialog(null, reason, "Missing Dependency", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                if (option == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().browse(URI.create(url));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // Fallback for Linux, etc users with no "default" browser
                JOptionPane.showMessageDialog(null, reason);
            }
        }
    }
}
