/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawhilbertcurve;

import java.util.ArrayList;

/**
 *
 * @author Nicolas Benatti
 */
public enum Orientation {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    
    public ArrayList<String> getNames() {
        
        Orientation[] vals = values();
        
        ArrayList<String> names = new ArrayList<>();
                
        for(Orientation it : vals)
            names.add(it.name());
        
        return names;
    }
}
