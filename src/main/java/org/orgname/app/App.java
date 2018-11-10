package org.orgname.app;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 */
public class App {
    private final static Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        try {
            Service service = new Service();
            //run first time in case of older message are in the "queue".
            service.runFirstTime();
           //run every sec  and print msg.
            service.runWithTimer();

        } catch (Exception e) {
            LOGGER.log( Level.SEVERE, e.toString(), e );

        }
    }
}
