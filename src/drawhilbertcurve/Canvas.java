/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawhilbertcurve;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javafx.util.Pair;
import javax.swing.JFrame;

/**
 *
 * @author Nicolas Benatti
 */
public class Canvas extends JFrame {

    private HilbertCurve curve;
    
    private int unitDim;
    
    private final int H, W;
    
    public Canvas() {
        
        setLayout(null);
        setResizable(false);
        
        H = W = 400;
        
        setPreferredSize(new Dimension(W, H));
        pack();
    }

    @Override
    public void paint(Graphics grphcs) {
        
        Graphics2D g = (Graphics2D)grphcs;
        
        Pair<Integer, Integer> coord, prev = null, screenifiedCoord = null;
        
        g.setColor(Color.RED);
        
        g.setStroke(new BasicStroke(3));
        
        for(int i = 0; (coord = curve.getNextNode()) != null; ++i) {
            
            if(i == 0) {
                prev = screenify(coord);
                //System.out.println("node "+i+": ("+prev.getKey()+", "+prev.getValue()+"), ("+coord.getKey()+", "+coord.getValue()+")");
                continue;
            }
            
            screenifiedCoord = screenify(coord);

            //System.out.println("node "+i+": ("+screenifiedCoord.getKey()+", "+screenifiedCoord.getValue()+"), ("+coord.getKey()+", "+coord.getValue()+")");
            
            g.drawLine(prev.getKey(), prev.getValue(), screenifiedCoord.getKey(), screenifiedCoord.getValue());
            
            prev = screenifiedCoord;
        }
    }   
    
    private void refreshUnitDim(int newOrder) {
        
        unitDim = W / newOrder;
    }
    
    public void attachCurve(HilbertCurve hc) {
        
        curve = hc;
        
        refreshUnitDim(curve.getOrder());
    }
    
    private Pair<Integer, Integer> screenify(Pair<Integer, Integer> in) {
        
        return new Pair(in.getKey()*unitDim, H - in.getValue()*unitDim);
    }
}
