/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chart;

import VoteSMA.Simulaton;
import VoteSMA.VoteSMA;
import jade.core.Profile;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author neo4j
 */
public class Main {

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
     */

    /**
     * @param args the command line arguments
     */
   
    private static final Logger logger = LogManager.getLogger(Main.class);
static int NBBASENODE;
    static int DIFFUSIONTYPE;
    static float PROBAEDGE;
    static int GRAPHETYPE;
    public static void main(String[] args) throws ControllerException {

        try {
         final Properties prop = new Properties();
        InputStream input = null;
    
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

          
            NBBASENODE = Integer.parseInt(prop.getProperty("NBBASENODE"));
            DIFFUSIONTYPE = Integer.parseInt(prop.getProperty("DIFFUSIONTYPE"));
            GRAPHETYPE = Integer.parseInt(prop.getProperty("GRAPHETYPE"));
        
            PROBAEDGE = Float.parseFloat(prop.getProperty("PROBAEDGE"));
  
            String[] parametter = new String[3];
            parametter[0] = "Global.csv";
            parametter[1] = "Detailed";
            parametter[2] = args[0];

            //  parametter[3] =g.madeValue();
            Application.launch(Chart.class, parametter);
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
            
        } catch (final Exception ex) {
            logger.error(ex);
        }

    }

}
