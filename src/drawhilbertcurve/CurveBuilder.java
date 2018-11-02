/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawhilbertcurve;

import javafx.util.Pair;

/**
 * calcolo parallelo delle coordinate del pattern
 * @author nicoTux-PC
 */
public class CurveBuilder extends Thread {

    private int startNode, endNode; // indici del set di nodi di cui calcolare le coordinate

    private static int SPAN;
    
    /**
     * Oggetto curva di cui calcolare le coordinate.<br>
     * Viene condiviso da tutti i thread.
     */
    HilbertCurve curve;
    
    public CurveBuilder(HilbertCurve sharedCrv, int startIndex, String string) {
        
        super(string);
        
        startNode = startIndex * SPAN;
        endNode = startNode + (SPAN-1);
        
        curve = sharedCrv;
    }
    
    public static void setSpan(int v) {
        SPAN = v;
    }
    
    @Override
    public void run() {
        
        int nodeIndex;
        
        System.out.println(getName()+": SN = "+startNode+", EN = "+endNode);
        
        for(nodeIndex = this.startNode; nodeIndex <= this.endNode; nodeIndex++) {
            
            //Pair<Integer, Integer> tmp = curve.nodeLocation(nodeIndex);
            
            //System.out.println(getName()+": processed node #"+nodeIndex+", ("+tmp.getKey()+","+tmp.getValue()+")");
            
            curve.addNode(nodeIndex, curve.nodeLocation(nodeIndex));
        }
    }
}
