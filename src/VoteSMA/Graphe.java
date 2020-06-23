/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javafx.application.Application;

/**
 *
 * @author claire
 */
public class Graphe {

    private Set<Node> nodes = new HashSet<Node>();
    private Set<Edge> edges = new HashSet<>();
    int numberVertex; // sommet
    int numberBase;// arrete diriger d influence ( mettre les influenceurs dans les noeuds)
    float probaEdge;
    int sumDegree;
    int degreeMoy;

    public Graphe(int numberVertex, int numberEdge, float probaEdge, int type) {
        this.numberVertex = numberVertex;
        this.numberBase = numberEdge;
        this.probaEdge = probaEdge;
        createGraph(type);
        this.initSumDegree();
    }

    public void see() {
        GraphView gv = new GraphView(this);
    }

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public int getDegreeMoy() {
        initSumDegree();
        this.degreeMoy = (this.sumDegree/2)/this.nodes.size();
        return  this.degreeMoy;
    }
    
    public void initSumDegree() {
        this.sumDegree = 0;
        nodes.forEach((n) -> {
            this.sumDegree =  this.sumDegree +n.getInfluNode().size() + n.getfriendNode().size();
        });
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public Node getNode(int p) {
        for (Node n : nodes) {
            if (n.getId() == (p)) {
                return n;
            }
        }
        return null;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    private void createNode(int number) {
        for (int i = 0; i < number; i++) {
            addNode(new Node(i));
        }
    }

    public void graphFullConnected() {
        this.nodes.forEach((n) -> {
            for (Node n2 : this.nodes) {
                if (n != n2 && !edgeExist(n, n2)) {       // *2 because edge i,j and j,i but j in fluemce only by i and two edge exist                      
                    n2.addInfluNode(n);
                    n2.addfriendNode(n);
                    n.addInfluNode(n2);
                    n.addfriendNode(n2);
                    addEdge(new Edge(n.getId(), n2.getId()));
                    addEdge(new Edge(n2.getId(), n.getId()));
                }
            }
        });
    }

    public boolean edgeExist(Node n, Node n2) {
        Edge testE = new Edge(n.getId(), n2.getId());
        if (this.edges.stream().anyMatch((e) -> (e.equals(testE)))) {
            return true;
        }
        return false;
    }

    private void createGraph(int type) {
        switch (type) {
            case 1://Erdös-Renyi 
                createNode(this.numberVertex);
                createEdgesUndirectedErdos();
                break;
            case 2://bar
                createNode(this.numberBase);
                graphFullConnected();                
                createEdgesUndirectedBar();
                break;
            default:
            // code block
        }
    }

    public String[] madeValue() {
        HashMap<Integer, Integer> map = new HashMap<>();

        this.nodes.stream().map((n) -> n.getInfluNode().size() + n.getfriendNode().size()).forEachOrdered((degree) -> {
            if (map.containsKey(degree)) {
                map.put(degree, map.get(degree) + 1);
            } else {
                map.put(degree, 1);
            }
        });
        int i = 0;
        String[] tab = new String[map.size() * 2];
        for (Map.Entry ps : map.entrySet()) {
            tab[i] = String.valueOf(ps.getKey());
            i++;
            tab[i] = String.valueOf(ps.getValue());
            i++;
        }
        return tab;
    }

    public void importGraphUndirective() {
        // add node 
        //add edge
    }

    private void createEdgesUndirectedErdos() {
        Random random = new Random();
        this.nodes.forEach((n) -> {
            this.nodes.forEach((n2) -> {
                // *2 because edge i,j and j,i but j in fluemce only by i and two edge exist
                float r = random.nextFloat();
                if (n != n2 && !edgeExist(n, n2)) {
                    if (r <= this.probaEdge) {
                        n2.addInfluNode(n);
                        n2.addfriendNode(n);
                        n.addInfluNode(n2);
                        n.addfriendNode(n2);
                        addEdge(new Edge(n.getId(), n2.getId()));
                        addEdge(new Edge(n2.getId(), n.getId()));
                    }
                }
            });
        });
    }

    private void createEdgesUndirectedBar() {
        Random random = new Random();
        int i = this.numberBase;
        int edge;
        double pn;
        float r;
        while (i < this.numberVertex) {
            Node addN = new Node(i);
            addNode(addN);
            edge = this.numberBase;
            for (Node n : this.nodes) {
                int deg=n.getInfluNode().size() + n.getfriendNode().size();
                pn = deg / this.nodes.size();
                r = random.nextFloat();
                if (r <= pn) {
                    addN.addInfluNode(n);
                    addN.addfriendNode(n);
                    n.addInfluNode(addN);
                    n.addfriendNode(addN);
                    addEdge(new Edge(n.getId(), addN.getId()));
                    addEdge(new Edge(addN.getId(), n.getId()));
                    edge--;                         
                }
                
                if (edge == 0) {
                    break;
                }
            }
            i++;
        }
    }


}
