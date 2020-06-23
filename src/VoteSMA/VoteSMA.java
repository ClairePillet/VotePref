/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import com.sun.tools.javac.util.Pair;
import jade.core.Profile;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

/**
 *
 * @author claire
 */
public class VoteSMA {

    /**
     * @param args the command line arguments
     */
    static int NBVOTER;
    static int NUMBEROPINION;
    static int NBBASENODE;
    static int DIFFUSIONTYPE;
    static float PROBAEDGE;
    static int GRAPHETYPE;
    static AgentContainer mc;
    static Runtime runtime;
    static Profile config;
    static int i = 0;
        private static final Logger logger = LogManager.getLogger(VoteSMA.class);
 
    public static void main(String[] args) throws ControllerException {
        final Properties prop = new Properties();
          
        InputStream input = null;
    
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            NBVOTER = Integer.parseInt(prop.getProperty("NBVOTER"));
            NUMBEROPINION = Integer.parseInt(prop.getProperty("NUMBEROPINION"));
            NBBASENODE = Integer.parseInt(prop.getProperty("NBBASENODE"));
            DIFFUSIONTYPE = Integer.parseInt(prop.getProperty("DIFFUSIONTYPE"));
            GRAPHETYPE = Integer.parseInt(prop.getProperty("GRAPHETYPE"));
            i = Integer.parseInt(args[0]);
            PROBAEDGE = Float.parseFloat(prop.getProperty("PROBAEDGE"));
       logger.warn(i);
       
            Simulaton s = new Simulaton();
        } catch (final IOException ex) {
            logger.error(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                     logger.error(e);
                }
            }
        }

    }

}
