/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import static VoteSMA.VoteSMA.i;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

/**
 *
 * @author claire
 */
public class LegAgent extends Agent {

     HashMap<AID, Opinion> opinionVoter;// opinion des influ
     int nbVoter;
     int nbTalkTot;
     int nbTalkTotLastChange;
    Opinion o;
    Opinion oP;
     int evaCount = 0;
     int lastChangeCount = 0;
    MajorityVote mvP;
     boolean lastW;
     int switchMaj = 0;
     char bestCandidatBorda;
       char bestCandidat;
      char firstCondWin;
        char lastCondWin;
      char winnerBorda;
      char winner;
     Graphe g;
     CsvHelper csvGlobale;
    CsvHelper csvDetail;
     boolean close =false;
    Simulaton simulationControle;
  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger("log1");
    public void setup() {
        
        
        Object[] args = getArguments();
        nbVoter = (int) args[1];
        o = new Opinion((int) args[0]);
        oP = new Opinion((int) args[0]);
        g = (Graphe) args[2];
        mvP = new MajorityVote();
        nbTalkTot=0;
        opinionVoter = new HashMap<>();
        simulationControle = (Simulaton) args[3];
        try {
            String dir=String.valueOf(VoteSMA.DIFFUSIONTYPE);
            if(VoteSMA.GRAPHETYPE==1){
               dir= dir.concat(String.valueOf(VoteSMA.PROBAEDGE));
            }else{
               dir= dir.concat(String.valueOf(VoteSMA.NBBASENODE));
            }
            csvGlobale = new CsvHelper(",", "Global.csv", false, dir);
            csvDetail = new CsvHelper(",", "Detailed" + VoteSMA.i + ".csv", true, dir);
                  System.out.println(VoteSMA.i);
        } catch (IOException ex) {
              logger.error(ex.getMessage());
        }
        addBehaviour(new Routine());
    }

    public class Routine extends CyclicBehaviour {

        public void action() {
            getMessage();
        }

        synchronized void sendMsg(String Content, int Performative, AID reciver) {
            ACLMessage msg = new ACLMessage(Performative);
            msg.setContent(Content);
            msg.addReceiver(reciver);
            myAgent.send(msg);
        }

        public synchronized void csvWritingGlobale(boolean end) throws IOException {
            System.out.println(end);
            List<String> readFile = csvGlobale.readFile();
            List<Map<String, String>> lst = new ArrayList<>();
            Map<String, String> oneData = new HashMap<>();
            // oneData.put("id", "id");
            oneData.put("end", String.valueOf(end));
            //oneData.put("nbSwitch", String.valueOf(switchMaj));
            oneData.put("degMoy", String.valueOf(g.getDegreeMoy()));
             oneData.put("nbInflu", String.valueOf(nbTalkTotLastChange));
             oneData.put("nbElec", String.valueOf(evaCount));
             oneData.put("Concensus", String.valueOf(mvP.isConcensus()));
           if(bestCandidatBorda==winnerBorda){
                oneData.put("switch", String.valueOf(true));
           }else{
               oneData.put("switch", String.valueOf(false));
           }
           oneData.put("FirstWinnerBorda", String.valueOf(bestCandidatBorda));
           oneData.put("FirstWinnerPlu", String.valueOf(bestCandidat));
           oneData.put("FirstCondorcetWinner", String.valueOf(firstCondWin));
              oneData.put("LastWinnerBorda", String.valueOf(winnerBorda));
               oneData.put("LastWinnerPlu", String.valueOf(winner));
           oneData.put("LastCondorcetWinner", String.valueOf(lastCondWin));
            lst.add(oneData);
            if (readFile.isEmpty()) {
                csvGlobale.write(lst, true);
            } else {
                csvGlobale.write(lst, false);
            }

        }

        public synchronized void csvWritingDetailed(MajorityVote mv) throws IOException {
            List<String> readFile = csvDetail.readFile();
            List<Map<String, String>> lst = new ArrayList<>();

         
                Iterator it = mv.getDetailedListOpinion().get(0).entrySet().iterator();
                Map<String, String> oneData = new HashMap<>();
                while (it.hasNext()) {
                    Map.Entry ps = (Map.Entry) it.next();
                    String key = String.valueOf(ps.getKey());
                    Integer count = (Integer) ps.getValue();
                    oneData.put(key, String.valueOf(count));
                    
                }
                lst.add(oneData);
            
            if (readFile.isEmpty()) {
                csvDetail.write(lst, true);
            } else {
                csvDetail.write(lst, false);
            }
        }

        synchronized public void getMessage() {
            ACLMessage msgR = receive();
            while (msgR != null) {
                int performative = msgR.getPerformative();
                if (performative == ACLMessage.PROPAGATE) {
                    if (msgR.getContent() != null && opinionVoter.size() < nbVoter) {
                        OpinionMessage msgContent = new OpinionMessage(msgR);
                        if(!opinionVoter.containsKey(msgR.getSender())){
                            opinionVoter.put(msgR.getSender(), msgContent.getContent());
                        nbTalkTot=nbTalkTot+msgContent.getNbTalk();
                        }
                        
                    }
                }
                if (opinionVoter.size() == nbVoter) {
                     System.out.println("evql");
                    evaluation();
                    opinionVoter.clear();
                    nbTalkTot=0;
                }
                msgR = receive();
            }
        }
   
        public void evaluation() {

            MajorityVote mv = new MajorityVote(opinionVoter, nbVoter, o);
            mv.upASI();
            mv.upPlularity();
            if(evaCount==0){
                bestCandidatBorda=mv.getWinnerBorda();
                bestCandidat=mv.getWinner();
                firstCondWin=mv.getCondorcetWinner();
            }else{
                winnerBorda=mv.getWinnerBorda();
                winner=mv.getWinner();
            }
            evaCount++;
            if (mvP.getDetailedListOpinion() != null && close!=true) {
                      System.out.println(mv);
                if ((mv.equals(mvP))) {
                    lastChangeCount++;
                    if (lastChangeCount > 40) {                
                        try {
                            close=true;
                             lastCondWin=mv.getCondorcetWinner();
                            csvWritingGlobale(true);
                        } catch (IOException ex) {
                            logger.error(ex.getMessage());
                        }
                        SendStop();
                    }
                } else {
                    lastChangeCount = 0;
                    nbTalkTotLastChange=nbTalkTot;
                    /*    if (!oHere.equals(oP)) {
                        switchMaj++;
                       System.out.println("switCh");
                    }*/
                }
                if (evaCount == 1000) {              
                    System.out.println(evaCount + " " + mv);
                    try {
                        close=true;
                        lastCondWin=mv.getCondorcetWinner();
                        csvWritingGlobale(false);
                    } catch (IOException ex) {
                        logger.error(ex.getMessage());
                    }
                    SendStop();
                }
            }
            try {
                csvWritingDetailed(mv);
            } catch (IOException ex) {
              logger.error(ex.getMessage());
            }
          //  oP.setTabOpinion(oHere.getTabOpinion().clone());
            mvP.setDetailedListOpinion((ArrayList<HashMap<Object, Integer>>)mv.getDetailedListOpinion().clone());
            mvP.setConcensus(mv.isConcensus());
                    
        }

        public synchronized void SendStop() {

            for (Map.Entry ps : opinionVoter.entrySet()) {
                AID key = (AID) ps.getKey();
                sendMsg("END", ACLMessage.INFORM, key);
            }
            Codec codec = new SLCodec();
            Ontology jmo = JADEManagementOntology.getInstance();
            getContentManager().registerLanguage(codec);
            getContentManager().registerOntology(jmo);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(getAMS());
            msg.setLanguage(codec.getName());
            msg.setOntology(jmo.getName());
            try {
                getContentManager().fillContent(msg, new Action(getAID(), new ShutdownPlatform()));
                send(msg);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            // simulationControle.kill();
        }
    }
}
