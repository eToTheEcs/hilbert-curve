/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawhilbertcurve;

import javax.swing.JFrame;

/**
 *
 * @author Nicolas Benatti
 */
public class DrawHilbertCurve {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        HilbertCurve hc = new HilbertCurve(32);
        
        System.out.println(hc.getNumNodes());
        
        hc.build();
        
        // ditribuisci il calcolo della curva su più thread
                
        /*int NTHREADS = 4;
        
        int SPAN = hc.getNumNodes() / NTHREADS;
        
        CurveBuilder[] cbs = new CurveBuilder[NTHREADS];
        
        CurveBuilder.setSpan(SPAN);
        
        int i;
        
        for(i = 0; i < cbs.length; ++i) {
            cbs[i] = new CurveBuilder(hc, i, "builder " + i);
            cbs[i].start();
        }
         
        try {
            for(i = 0; i < cbs.length; ++i)
                cbs[i].join();
            }
        catch(InterruptedException e) {
            System.out.println(e);
        }
        
        //hc.dump();
        
        /*Canvas c = new Canvas();
        c.attachCurve(hc);
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.setVisible(true);*/
        
        //System.out.println(hc.extractLocation(7, 1));
    }
    
    
}
