/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import jade.core.AID;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author claire
 */
public class Node {

    private Set<Node> influNode = new HashSet<>();
    private Set<Node> friendNode = new HashSet<>();
    private int id;
    private AID aid;


    public Node(int i) {
        this.id = i;
    }

    public Set<Node> getInfluNode() {
        return influNode;
    }

    public void addInfluNode(Node nodeA) {
        influNode.add(nodeA);
    }
    public Set<Node> getfriendNode() {
        return friendNode;
    }

    public void addfriendNode(Node nodeA) {
        friendNode.add(nodeA);
    }
    public void setAdjnode(Set<Node> adjnode) {
        this.influNode = adjnode;
    }

    public int getId() {
        return id;
    }

    public void setId(int Pos) {
        this.id = Pos;
    }

  
    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node n = (Node) obj;
        if (this.id==n.getId()) {
            return true;
        }
        return false;
    }

}