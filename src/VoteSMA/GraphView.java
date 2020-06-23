/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author claire
 */
public class GraphView extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -2707712944901661771L;

    public GraphView(Graphe g) {

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        ArrayList ar = new ArrayList<Object>();
        try {
            for (Node n : g.getNodes()) {
                if(n.getId()%2==0){
                    ar.add(graph.insertVertex(parent, String.valueOf(n.getId()), String.valueOf(n.getId()), 20, 20 + n.getId() * 20, 30, 30));
                }else{
                    ar.add(graph.insertVertex(parent, String.valueOf(n.getId()), String.valueOf(n.getId()), 180, 20 + n.getId() * 20, 30, 30));
                }
                
            }
            for (Edge e : g.getEdges()) {
                graph.insertEdge(parent, null, "", ar.get(e.from), ar.get(e.to));
            }

        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 320);
        this.setVisible(true);
    }

}
