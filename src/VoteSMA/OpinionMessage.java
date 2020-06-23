/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author claire
 */
public class OpinionMessage {
     private ACLMessage msg;
    private Opinion content;
    private int nbTalk;
private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OpinionMessage.class);
    public OpinionMessage(ACLMessage msg) {
        try {
            this.msg = msg;
            Object o =msg.getContentObject();          
            content=  Opinion.class.cast(((Object[])o)[1]);
            nbTalk=  Integer.class.cast(((Object[])o)[0]);
        } catch (UnreadableException ex) {
         logger.error(ex);
        }
    }

    public int getNbTalk() {
        return nbTalk;
    }

    public void setNbTalk(int nbTalk) {
        this.nbTalk = nbTalk;
    }

    public ACLMessage getMsg() {
        return msg;
    }

    public void setMsg(ACLMessage msg) {
        this.msg = msg;
    }

    public Opinion getContent() {
        return content;
    }

    public void setContent(Opinion content) {
        this.content = content;
    }
}
