package com.khapilov.gui;

import java.awt.*;
import java.net.URL;

/**
 * Just a helper for GUI builder.
 *
 * @author Ross Khapilov
 * @version 1.0 23.05.2018
 */
class IconHelper {
    /**
     * Helper for get Image for menu items.
     *
     * @param pathAndFileName path and name of image.
     * @return Image.
     */
    static Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
    }
}
