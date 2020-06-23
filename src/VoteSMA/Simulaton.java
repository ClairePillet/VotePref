/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import Chart.Chart;
import static VoteSMA.VoteSMA.DIFFUSIONTYPE;
import static VoteSMA.VoteSMA.GRAPHETYPE;
import static VoteSMA.VoteSMA.NBBASENODE;
import static VoteSMA.VoteSMA.NBVOTER;
import static VoteSMA.VoteSMA.NUMBEROPINION;
import static VoteSMA.VoteSMA.PROBAEDGE;
import com.sun.tools.javac.util.ArrayUtils;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import jade.wrapper.State;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Application;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author claire
 */
public class Simulaton implements Runnable {

    volatile AgentContainer mc;
    volatile jade.core.Runtime runtime;
    volatile Profile config;
   
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Simulaton.class);
  Graphe g;

    public Simulaton() {

        try {
            createSimu();
        } catch (Exception ex) {
             logger.error(ex);
        }
    }

    public synchronized void kill() {
       System.exit(0);
              
    }

    public synchronized void createSimu() throws ControllerException {
        runtime = jade.core.Runtime.instance();
        config = new ProfileImpl("localhost", 8888, null);
        config.setParameter("gui", "false");
        //  config.setParameter("jade_core_messaging_MessageManager_poolsize", "100000");
        // config.setParameter("jade_core_messaging_MessageManager_maxqueuesize", "10000000000");
        logger.error("new sim");
        mc = runtime.createMainContainer(config);
        AgentController acA;
         g = new Graphe(NBVOTER, NBBASENODE, PROBAEDGE, GRAPHETYPE);

        Object[] param = {NUMBEROPINION, NBVOTER, g, this};
    
        int nb = NBVOTER-1;
          acA = mc.createNewAgent("Legislateur", LegAgent.class.getName(), param);
          acA.start();
        while (nb >= 0) {
            Object[] params = {nb, g, NUMBEROPINION, DIFFUSIONTYPE};
            acA = mc.createNewAgent(String.valueOf(nb), VoterAgent.class.getName(), params);
            acA.start();
            nb--;
        }
        //  runtime.invokeOnTermination(this);

        // Process p1 =Process.clas
        //    p1.waitFor()
      //kill();

    }

    @Override
    public void run() {
        kill();
    }
}
