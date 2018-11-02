/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawhilbertcurve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

/**
 * a Hilbert curve in a Cartesian coordinate system.
 * NOTE: this class is thread-safe.
 * @author Nicolas Benatti
 */
class HilbertCurve {
  
    /* == maschere per estrazione informazioni == */
    
    private static final int EXTRACT_LAST_2_BITS = 3;
    
    /**
     * ordine della curva.
     */
    private int order;
    
    /**
     * numero nodi che compongono la curva.
     */
    private int numNodes;
    
    /**
     * coordinate dell'i-esimo nodo in un sistema di riferimento cartesiano.
     */
    private SortedMap<Integer, Pair<Integer, Integer>> coordinates; 
    
    /**
     * descrive il cammino sulla curva<br>
     * indica in che direzione proseguire dopo l'i-esimo nodo.
     */
    //private ArrayList<Orientation> curveData;   
    
    private int lastNodeIndex;
    
    public HilbertCurve(int o) throws IllegalArgumentException {
        
        if(Integer.bitCount(o) != 1)
            throw new IllegalArgumentException("curve order must be a power of 2");
        
        order = o;
        numNodes = computeNumNodes();
        lastNodeIndex = 0;
        
        coordinates = Collections.synchronizedSortedMap(new TreeMap<>());
        //coordinates = new ArrayList<>(numNodes);
        //coordinates = new Pair<Integer, Integer>[numNodes];
    }

    public int getOrder() {
        return order;
    }

    public int getNumNodes() {
        return numNodes;
    }
    
    /**
     * calcola il no. di nodi della curva.
     * @param runnerOrder
     * @return 
     */
    private int computeNumNodes() {
        
        int nNodes = 4;
        
        for(int i = 4; i <= order; i<<=1) {
            
            nNodes += 3*nNodes;
        }
        
        return nNodes;
    }
    
    /**
     * ritorna la prossima coppia di coordinate:<br>
     * la coppia i-esima contiene le coordinate della curva di ordine 2^i<br>
     * all'interno di una di ordine 2^(i+1).
     * @param i indice della coppia da estrarre.
     * @return byte in cui i 2 bit meno significativi contengono l'indice (in una matrice 2x2)
     */
    private int extractLocation(int nodeIndex, int i) {
        
        int MASK = 1 << (2*i);
        
        int location = 0;
        
        for(int j = 1; j <= 2; MASK <<= 1, ++j) {
            //System.out.println("**"+MASK+"**");
            if((nodeIndex & MASK) != 0) {
                //System.out.println("**"+nodeIndex+","+MASK+"**");
                location |= j;
                //System.out.println(location);
            }
        }
       
        
        return location;
    }
    
    public Pair<Integer, Integer> nodeLocation(int nodeIndex) {
        
        int currentOrder;
        int runnerX = 0, runnerY = 0;
        int locationIndex = 0;
        
        //System.out.println("CIAO: " + this.order);
        
        for(currentOrder = 1; currentOrder < this.order; currentOrder <<= 1, locationIndex++) {

            int packedLocation = extractLocation(nodeIndex, locationIndex);
            int tmp;

            //System.out.println("CIAO: " + currentOrder);
            
            // aggiorna posizione nodo (tende a quella finale)
            switch(packedLocation) {
                
                case 0:
                    if(currentOrder == 1) {
                        runnerX = runnerY = 0;
                    }
                    else {
                        tmp = runnerX;
                        runnerX = runnerY;
                        runnerY = tmp;
                    }
                    break;
                case 1:
                    if(currentOrder == 1) {
                        runnerX = 0;
                        runnerY = 1;
                    }
                    else {
                        runnerY += currentOrder;
                        //System.out.println("DOPO x: "+runnerX+",y: "+runnerY);
                    }
                    break;
                case 2:
                    if(currentOrder == 1) {
                        runnerX = runnerY = 1;
                    }
                    else {
                        runnerX += currentOrder;
                        runnerY += currentOrder;
                    }
                    break;
                case 3:
                    if(currentOrder == 1) {
                        runnerX = 1;
                        runnerY = 0;
                    }
                    else {
                        tmp = -runnerX;
                        runnerX = -runnerY;
                        runnerY = tmp;

                        runnerX += (currentOrder-1);
                        runnerX += currentOrder;

                        runnerY += (currentOrder-1);
                    }
                    break;
                default:
                    break;
            }
        }
        
        return new Pair<>(runnerX, runnerY);
    }
    
    /**
     * DEPRECATED
     */
    public void build() {
        
        int currentOrder;
        int nodeIndex = 0;  // indice del nodo da posizionare
        int locationIndex;  // indice delle coordinate da analizzare
        
        int runnerX, runnerY;
        
        for(; nodeIndex <= numNodes; ++nodeIndex) {
            
            /*currentOrder = 1;
            locationIndex = 0;
            runnerX = runnerY = 0;
            
            //System.out.print("**"+nodeIndex+"** ");
            
            for(; currentOrder < this.order; currentOrder <<= 1, locationIndex++) {

                int packedLocation = extractLocation(nodeIndex, locationIndex);

                //System.out.print(packedLocation);
                
                int tmp;
                
                // aggiorna posizione nodo (tende a quella finale)
                switch(packedLocation) {

                    case 0:
                        if(currentOrder == 1) {
                            runnerX = runnerY = 0;
                        }
                        else {
                            tmp = runnerX;
                            runnerX = runnerY;
                            runnerY = tmp;
                        }
                        break;
                    case 1:
                        if(currentOrder == 1) {
                            runnerX = 0;
                            runnerY = 1;
                        }
                        else {
                            //System.out.println(nodeIndex+"->PRIMA x: "+runnerX+",y: "+runnerY);
                            runnerY += currentOrder;
                            //System.out.println("DOPO x: "+runnerX+",y: "+runnerY);
                        }
                        break;
                    case 2:
                        if(currentOrder == 1) {
                            runnerX = runnerY = 1;
                        }
                        else {
                            runnerX += currentOrder;
                            runnerY += currentOrder;
                        }
                        break;
                    case 3:
                        if(currentOrder == 1) {
                            runnerX = 1;
                            runnerY = 0;
                        }
                        else {
                            tmp = -runnerX;
                            runnerX = -runnerY;
                            runnerY = tmp;

                            runnerX += (currentOrder-1);
                            runnerX += currentOrder;
                            
                            runnerY += (currentOrder-1);
                        }
                        break;
                    default:
                        break;
                }
            }*/
            
            //System.out.println("CICLO " + nodeIndex + "; " + numNodes);
            
            coordinates.put(/*new Pair<>(runnerX, runnerY)*/nodeIndex, nodeLocation(nodeIndex));
        }
    }
    
    public Pair<Integer, Integer> getNextNode() {
        
        if(lastNodeIndex < coordinates.size() - 1)
            return coordinates.get(lastNodeIndex++);
        else return null;
    }
    
    public void addNode(int index, Pair<Integer, Integer> n) {
        
        coordinates.put(index, n);
        //coordinates.add(n);
    }
    
    public void dump() {
        
        for(Integer it : coordinates.keySet()) {
            System.out.println("node " + it + ": " + coordinates.get(it));
        }
    }
}
