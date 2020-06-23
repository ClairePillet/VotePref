/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import javafx.util.Pair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import org.apache.logging.log4j.spi.Provider;

/**
 *
 * @author claire
 */
public class Opinion implements Serializable {

    int numberOfCandidate;
    ArrayList<Character> tabOpinion;
    HashMap<Pair, Boolean> hm;

    public Opinion(int numberOfProposition) {
        this.numberOfCandidate = numberOfProposition;
        hm = new HashMap<Pair, Boolean>();
        randomChar(numberOfProposition);
            
    }

    void randomChar(int len) {
        Random random = new Random();
        tabOpinion = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            tabOpinion.add(i, (char) (i + 'a'));           
        }
        Collections.shuffle(tabOpinion);
        genHM();
    }

    void genHM() {
        String s= tabOpinion.toString().replace(",", "").replace("[", "").replace("]", "").replace(" ", "");
        char[] test=s.toCharArray();
        Arrays.sort(test);
        for (char c : test) {
            for (char c2 : test) {
                if (c2 != c) {
                    Pair p = new Pair(c, c2);
                    Pair p2 = new Pair(c2, c);
                    if (!hm.containsKey(p2) && !hm.containsKey(p)) {
                        if (tabOpinion.indexOf(c) > tabOpinion.indexOf(c2)) {
                            hm.put(p, false);
                        } else {
                            hm.put(p, true);
                        }
                    }
                }
            }
        }
    }

    boolean checkTrans() {
        for (Entry<Pair, Boolean> e : hm.entrySet()) {
            Pair p = e.getKey();
            if (e.getValue()) {
                if (tabOpinion.indexOf(p.getKey()) > tabOpinion.indexOf(p.getValue())) {
                    return false;
                }
            } else {
                if (tabOpinion.indexOf(p.getKey()) < tabOpinion.indexOf(p.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void update(Pair p, boolean b) {
        if (!hm.containsKey(p)) {
            p = new Pair(p.getValue(), p.getKey());
        }
        if (hm.containsKey(p)) {
            if (hm.get(p) != b) {
                hm.replace(p, !b, b);
                Collections.swap(tabOpinion, tabOpinion.indexOf(p.getKey()), tabOpinion.indexOf(p.getValue()));
                if (!checkTrans()) {
                    hm.replace(p, b, !b);

                    Collections.swap(tabOpinion, tabOpinion.indexOf(p.getKey()), tabOpinion.indexOf(p.getValue()));
                }
            }
        }else{
            System.out.println("VoteSMA.Opinion.update()");
        }

    }

    public boolean checkAdj(ArrayList<Character> tab) {
        int id = tabOpinion.indexOf(tab.get(0));
        if (id > 0) {
            if (tab.get(1) == tabOpinion.get(id - 1)) {
                return true;
            }
        }
        if (id < numberOfCandidate - 1) {
            if (tab.get(1) == tabOpinion.get(id + 1)) {
                return true;
            }
        }
        return false;
    }

    public Opinion(int numberOfProposition, ArrayList<Character> tab) {
        this.numberOfCandidate = numberOfProposition;
        tabOpinion = tab;
        hm = new HashMap<Pair, Boolean>();
         genHM();
    }

    public ArrayList<Character> getTabOpinion() {
        return tabOpinion;
    }

    public void setTabOpinion(ArrayList<Character> tabOpinion) {
        this.tabOpinion = tabOpinion;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < numberOfCandidate; i++) {
            s = s + " " + i + " " + String.valueOf(this.tabOpinion.get(i));
        }
        return s;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Opinion other = (Opinion) obj;
        if (this.tabOpinion.size() == other.getTabOpinion().size()) {
            for (int i = 0; i < this.tabOpinion.size(); i++) {
                if (Character.compare(this.tabOpinion.get(i), other.getTabOpinion().get(i)) != 0) {
                    System.out.println(this.tabOpinion.get(i) + "+" + other.getTabOpinion().get(i));
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

}
