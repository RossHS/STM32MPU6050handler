package com.khapilov.gui;

import java.awt.*;
import java.net.URL;

/**
 * @author Ross Khapilov
 * @version 1.0 23.05.2018
 */
class IconHelper {
    static Image getImage(final String pathAndFileName) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        return Toolkit.getDefaultToolkit().getImage(url);
    }
}
