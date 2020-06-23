/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import javafx.util.Pair;
import jade.core.AID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author claire
 */
public class MajorityVote {

    private HashMap<AID, Opinion> mapOpinion;
    private int nbVoter;
    private Opinion o;
    char Bordawinner;
       char winner;
    private boolean Concensus;
    private HashMap<Object, Integer> DetailedOpinionResult;
    private ArrayList<HashMap<Object, Integer>> DetailedListOpinionPlu;
    private ArrayList<HashMap<Object, Integer>> DetailedListOpinion;
       private HashMap<Object, Integer> DetailedOpinionResultPlu;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MajorityVote.class);

    public MajorityVote(HashMap<AID, Opinion> mapOpinion, int nbVoter, Opinion oToFill) {
        this.mapOpinion = mapOpinion;
        this.nbVoter = nbVoter;
        this.Concensus = false;
        DetailedListOpinion = new ArrayList<>();
         DetailedListOpinionPlu = new ArrayList<>();
        o = oToFill;
    }

    public MajorityVote() {

    }

    public char getWinnerBorda() {
        return Bordawinner;
    }
public char getWinner() {
        return winner;
    }
    public void setDetailedListOpinion(ArrayList<HashMap<Object, Integer>> DetailedListOpinion) {
        this.DetailedListOpinion = DetailedListOpinion;
    }

    public ArrayList<HashMap<Object, Integer>> getDetailedListOpinion() {
        return DetailedListOpinion;
    }

    public boolean isConcensus() {
        return this.Concensus;
    }

    public void setConcensus(boolean Concensus) {
        this.Concensus = Concensus;
    }

    synchronized public Opinion updateOMajority() {
        int countTrue, countFalse;

        countTrue = 0;//same
        countFalse = 0;
        Pair p = new Pair('a', 'a');
        for (Map.Entry ps : mapOpinion.entrySet()) {
            Opinion op = (Opinion) ps.getValue();

            p = new Pair(op.getTabOpinion().get(0), op.getTabOpinion().get(1));
            if (!o.hm.containsKey(p)) {
                p = new Pair(op.getTabOpinion().get(1), op.getTabOpinion().get(0));
            }

            if (op.hm.get(p)) {
                countTrue++;
            } else {
                countFalse++;
            }
        }
        DetailedOpinionResult = new HashMap<>();
        DetailedOpinionResult.put(true, countTrue);
        DetailedOpinionResult.put(false, countFalse);
        DetailedListOpinion.add(DetailedOpinionResult);
        if (countTrue == 0 || countFalse == 0) {
            this.Concensus = true;
        } else {
            this.Concensus = false;
        }
        //  logger.error("here");
        if (countFalse >= countTrue) {
            o.update(p, false);

        } else {
            o.update(p, true);

        }

        return o;
    }

    public char getCondorcetWinner() {
        DetailedOpinionResult = new HashMap<>();
        int winnerScore = 0;
        HashMap<Character, HashMap<Object, Integer>> lstHm = new HashMap<>();
        for (Map.Entry ps : mapOpinion.entrySet()) {
            Opinion op = (Opinion) ps.getValue();
            for (Character c : op.getTabOpinion()) {
                  HashMap<Object, Integer> hmPair; 
                   if(lstHm.containsKey(c)){
                    hmPair=lstHm.get(c);
                }else{
                     hmPair=new HashMap<Object, Integer>();
                    }

                for (Character c2 : op.getTabOpinion()) {
                    if (c2 != c) {
                        //crere la pair
                        Pair p = new Pair(c, c2);
                        int score =0;
                        if(op.getTabOpinion().indexOf(c) < op.getTabOpinion().indexOf(c2) ){ // si c pref a c2
                            score=1;
                        }
                        if (hmPair.containsKey(p)) {
                            if(score>0){
                                hmPair.replace(p, hmPair.get(p),hmPair.get(p)+score);
                            }
                               
                        } else {
                            hmPair.put(p, score);
                        }

                        
                       
                    }
                }
                if(lstHm.containsKey(c)){
                    lstHm.remove(c);
                    lstHm.put(c,hmPair);
                }else{
                    lstHm.put(c,hmPair);
                    
                }
                
            }
        }
         boolean win =true;
         char candidat=' ';
        for (Map.Entry<Character, HashMap<Object, Integer>> entry :lstHm.entrySet()){
          HashMap<Object, Integer>  hm=entry.getValue();
          candidat=  entry.getKey();
          win =true;
            for (Map.Entry ps : hm.entrySet()) {
             //always win map.size 
             
             if(((int)ps.getValue())<=(mapOpinion.size()/2)){
                 win=false;
                 candidat=' ';
                 break;
             }
            }
            if(win){
                break;
            }
                        
        }
        return candidat;
         
    }

    synchronized public void upASI() {
        DetailedOpinionResult = new HashMap<>();
        int winnerScore = 0;
        for (Map.Entry ps : mapOpinion.entrySet()) {
            Opinion op = (Opinion) ps.getValue();
            for (Character c : op.getTabOpinion()) {
                if (DetailedOpinionResult.containsKey(c)) {
                    DetailedOpinionResult.put(c, DetailedOpinionResult.get(c) + (o.numberOfCandidate - op.getTabOpinion().indexOf(c)));
                } else {
                    DetailedOpinionResult.put(c, (o.numberOfCandidate - op.getTabOpinion().indexOf(c)));
                }

            }

        }
        DetailedListOpinion.add(DetailedOpinionResult);

        for (Map.Entry ps : DetailedOpinionResult.entrySet()) {
            int count = (int) ps.getValue();
            char c = (char) ps.getKey();
            if (winnerScore == count) {
                if (Bordawinner <= c) {//tie breaking
                    logger.error("Tie");
                    Bordawinner = c;
                }
            }
            if (winnerScore < count) {
                winnerScore = count;
                Bordawinner = c;
            }
        }

    }
 synchronized public void upPlularity() {
        DetailedOpinionResultPlu = new HashMap<>();
        int winnerScore = 0;
        for (Map.Entry ps : mapOpinion.entrySet()) {
            Opinion op = (Opinion) ps.getValue();
            char c = op.getTabOpinion().get(0);
                if (DetailedOpinionResultPlu.containsKey(c)) {
                    DetailedOpinionResultPlu.put(c, DetailedOpinionResultPlu.get(c) + 1);
                } else {
                    DetailedOpinionResultPlu.put(c, 1);
                }         

        }
        DetailedListOpinionPlu.add(DetailedOpinionResultPlu);

        for (Map.Entry ps : DetailedOpinionResultPlu.entrySet()) {
            int count = (int) ps.getValue();
            char c = (char) ps.getKey();
            if (winnerScore == count) {
                if (winner <= c) {//tie breaking
                    logger.error("PluTie");
                    winner = c;
                }
            }
            if (winnerScore < count) {
                winnerScore = count;
               winner = c;
            }
        }

    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.DetailedOpinionResult);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (getClass() != obj.getClass()) {
            return false;
        }
        MajorityVote other = (MajorityVote) obj;
        for (int i = 0; i < DetailedListOpinion.size(); i++) {
            Iterator it = DetailedListOpinion.get(i).entrySet().iterator();
            HashMap<Object, Integer> hm = other.DetailedListOpinion.get(i);
            if (hm != null) {
                while (it.hasNext()) {
                    Map.Entry ps = (Map.Entry) it.next();
                    Object key = ps.getKey();
                    Integer count = (Integer) ps.getValue();
                    Integer count2 = hm.get(key);
                    System.out.println(count + "-" + count2);
                    if (!Objects.equals(count, count2)) {

                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < DetailedListOpinion.size(); i++) {
            for (Map.Entry ps : DetailedListOpinion.get(i).entrySet()) {
                String key = String.valueOf(ps.getKey());
                Integer count = (Integer) ps.getValue();
                s = s + key + " " + count;
            }
            s = s + "\n";
        }
        return s;
    }
}
