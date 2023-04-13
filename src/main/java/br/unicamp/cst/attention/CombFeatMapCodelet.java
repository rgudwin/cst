/**
 * *****************************************************************************
 * Copyright (c) 2012  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 *     K. Raizer, A. L. O. Paraense, R. R. Gudwin - initial API and implementation
 *****************************************************************************
 */
package br.unicamp.cst.attention;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract codelet implementation of a Combined Feature Map, generated by the 
 * Attentional System of Conscious Attention-Based Integrated Model (CONAIM). 
 * The combined feature map is a weighted sum of the previous defined feature 
 * maps (bottom-up and top-down).
 * 
 * @author L. M. Berto
 * @author L. L. Rossi (leolellisr)
 * @see Codelet
 * @see MemoryObject
 * @see FeatMapCodelet
 */
public abstract class CombFeatMapCodelet extends Codelet {
    protected MemoryObject comb_feature_mapMO;        
    protected List<MemoryObject> feature_maps;
    protected CopyOnWriteArrayList<String> feat_maps_names;
    protected Memory weights;
    protected int timeWindow;
    protected int CFMdimension;
    protected MemoryObject winnersType;
    /**
     * init CombFeatMapCodelet
     * @param featmapsnames
     *          input feature maps names
     * @param timeWin
     *          analysed time window,   Buffer size
     * @param CFMdim 
     *          output combined feature map dimension
     */
    
    protected CombFeatMapCodelet(CopyOnWriteArrayList<String> featmapsnames,int timeWin, int CFMdim){
        feature_maps = new CopyOnWriteArrayList<>();
        feat_maps_names = featmapsnames;
        timeWindow = timeWin;
        CFMdimension = CFMdim;
    }

    @Override
    /**
     * access MemoryObjects: feature_maps and weights
     * feature_maps must be the first inputs and weights must be the last input
     * define outputs: comb_feature_map and winnersType
     * 
     */
    public void accessMemoryObjects() {
        for (int i = 0; i < feat_maps_names.size(); i++) {
            feature_maps.add((MemoryObject) inputs.get(i));
        }
        weights = inputs.get(inputs.size()-1);
        comb_feature_mapMO = (MemoryObject) this.getOutput("COMB_FM");
        winnersType = (MemoryObject) this.getOutput("TYPE");
    }

    @Override
    public void calculateActivation() {
        // We don't use activations in this codelet
    }
    
    public abstract void calculateCombFeatMap();

    @Override
    /**
     * proc: codelet logical process
    */
    
    public void proc(){
        calculateCombFeatMap();
    }
    
}
