/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author jkaru
 */
public class configUtils {

    public static final HashMap<String, String> settings = new HashMap();

    public static HashMap<String, String> Settings() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String path = System.getProperty("jboss.server.data.dir");
            path = path + "/safhackprops.properties";  // linux
            input = new FileInputStream(path);
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = "";
                Object element = e.nextElement();
                if (element instanceof String) {
                    key = (String) element;
                }
                String value = prop.getProperty(key);
                settings.put(key, value);
            }

            settings.put("LOADCONFIG", "SUCCESS");
            System.out.println("====== LOADED CONFIG : success =========\n ");
        } catch (IOException e) {
            settings.put("LOADCONFIG", "FAILLED");
            System.out.println("Fatal Error!. Failld to set Configuration");

            System.exit(0);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.exit(0);
                }
            }
        }
        return settings;
    }
}
