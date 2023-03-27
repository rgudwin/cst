
/***********************************************************************************************
 * Copyright (c) 2012  DCA-FEEC-UNICAMP
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Contributors:
 * K. Raizer, A. L. O. Paraense, E. M. Froes, R. R. Gudwin - initial API and implementation
 ***********************************************************************************************/
package br.unicamp.cst.sensory;

import br.unicamp.cst.core.entities.Codelet;

import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.core.entities.Mind;
import br.unicamp.cst.support.TimeStamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;



/**
 *
 * Test for Codelet implementation of Feature Maps generated by the Attentional System of 
 * Conscious Attention-Based Integrated Model (CONAIM). The combined feature 
 * map is a weighted sum of the previous defined feature maps (bottom-up and
 * top-down).
 * @author L. L. Rossi (leolellisr)
 * @see Codelet
 * @see MemoryObject
 * @see FeatMapCodelet
 */
public class CombFeapMapCodeletTest {

    
    
    public MemoryObject source,source2,source3,weights;
    public MemoryObject destination,destination_type;
    public CFM testFeapMapCodelet;
    
    public CombFeapMapCodeletTest() {
        Mind testMind = new Mind();
        weights = testMind.createMemoryObject("FM_WEIGHTS");
        source = testMind.createMemoryObject("SOURCE");
        source2 = testMind.createMemoryObject("SOURCE2");
        source3 = testMind.createMemoryObject("SOURCE3");
        //source.setI(0);
        destination_type = testMind.createMemoryObject("TYPE");
        destination_type.setI(new CopyOnWriteArrayList<Float>());
        destination = testMind.createMemoryObject("COMB_FM");
        destination.setI(new CopyOnWriteArrayList<Float>());
        CopyOnWriteArrayList<String> FMnames = new CopyOnWriteArrayList<>();
        FMnames.add("SOURCE");
        FMnames.add("SOURCE2");
        FMnames.add("SOURCE3");
        testFeapMapCodelet = new CFM(3, FMnames, 100, 16, false);
        testMind.insertCodelet(testFeapMapCodelet);
        testFeapMapCodelet.addInput(source);
        testFeapMapCodelet.addInput(source2);
        testFeapMapCodelet.addInput(source3);
        testFeapMapCodelet.addInput(weights);
        testFeapMapCodelet.addOutput(destination);
        testFeapMapCodelet.addOutput(destination_type);
        testFeapMapCodelet.setIsMemoryObserver(true);
	source.addMemoryObserver(testFeapMapCodelet);
        source2.addMemoryObserver(testFeapMapCodelet);
        source3.addMemoryObserver(testFeapMapCodelet);
        weights.addMemoryObserver(testFeapMapCodelet);
        testMind.start();
        
        
        //List fulllist = (List)destination.getI();
        
        
    }
    
    @Test
    public void testCombFeapMapCodelet() {
        CombFeapMapCodeletTest test = new CombFeapMapCodeletTest();
        //for (int i=0;i<64;i++) {
            System.out.println("Testing ... ");
            long oldtimestamp = test.destination.getTimestamp();
            System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp before: "+TimeStamp.getStringTimeStamp(oldtimestamp, "dd/MM/yyyy HH:mm:ss.SSS"));
            
            CopyOnWriteArrayList<CopyOnWriteArrayList<Float>> mo_arrList = new CopyOnWriteArrayList<CopyOnWriteArrayList<Float>>();
            //MemoryObject source_arrList = new MemoryObject();
            
            // Test 1
            CopyOnWriteArrayList<Float> int_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 256*3; i++) {
                int_arrList.add((float)(i % 3) + 1);
            }
            CopyOnWriteArrayList<Float> ass_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 5; i++) {
                ass_arrList.add((float) 3.0);
                ass_arrList.add((float) 6.0);
                ass_arrList.add((float) 9.0);
            }
            ass_arrList.add((float) 3.0);
                    
            //source_arrList.setI(int_arrList);
            mo_arrList.add(int_arrList);
            long newtimestamp = test.destination.getTimestamp();
            test.testFeapMapCodelet.resetTriggers();
            test.source.setI(mo_arrList);
            test.source2.setI(mo_arrList);
            test.source3.setI(mo_arrList);
            while(test.testFeapMapCodelet.steps < 4) {
                newtimestamp = test.destination.getTimestamp();
                System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp while waiting: "+TimeStamp.getStringTimeStamp(newtimestamp,"dd/MM/yyyy HH:mm:ss.SSS"));
            }
            System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp after: "+TimeStamp.getStringTimeStamp(newtimestamp,"dd/MM/yyyy HH:mm:ss.SSS"));
            
            CopyOnWriteArrayList<Float> weig_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 3; i++) {
                weig_arrList.add((float) 1.0);
            }
            test.weights.setI(weig_arrList);
            System.out.println("   Inputs 11: "+test.source.getI());
            System.out.println("   Inputs 12: "+test.source2.getI());
            System.out.println("   Inputs 13: "+test.source3.getI());
            System.out.print("   Output 1: "+ test.destination.getI());
            List fulllist = (List) test.destination.getI();
            if (fulllist != null && fulllist.size() > 0) {
                //printList(fulllist);
                System.out.println("          sizef: "+fulllist.size()+"\n");
                //List first = (List)fulllist.get(0);
                //System.out.print("\n  first 1: "+ first);
                
                //List last = (List)fulllist.get(fulllist.size()-1);
                //System.out.print("\n  last 1: "+ last);
                
                assertEquals(((List)(test.destination.getI())).size(),16);
                assertEquals(((List)(test.destination.getI())),ass_arrList);
                
            }  
            
            // Test 2
            oldtimestamp = test.destination.getTimestamp();
            System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp before: "+TimeStamp.getStringTimeStamp(oldtimestamp, "dd/MM/yyyy HH:mm:ss.SSS"));
            
            int_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 256; i++) {
                int_arrList.add((float) 1);
            }
            ass_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 16; i++) {
                ass_arrList.add((float) 3.0);
                
            }
                    
            newtimestamp = test.destination.getTimestamp();
            test.testFeapMapCodelet.resetTriggers();
            //source_arrList.setI(int_arrList);
            mo_arrList.add(int_arrList);
            test.source.setI(mo_arrList);
            test.source2.setI(mo_arrList);
            test.source3.setI(mo_arrList);
            
            
            while(test.testFeapMapCodelet.steps < 3) {
                newtimestamp = test.destination.getTimestamp();
                System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp while waiting: "+TimeStamp.getStringTimeStamp(newtimestamp,"dd/MM/yyyy HH:mm:ss.SSS"));
            }
            
            weig_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 3; i++) {
                weig_arrList.add((float) 1.0);
            }
            test.weights.setI(weig_arrList);
            System.out.println("  Inputs 21: "+test.source.getI());
            System.out.println("  Inputs 22: "+test.source2.getI());
            System.out.println("  Inputs 23: "+test.source3.getI());
            System.out.print("  Output 2: "+ test.destination.getI());
            fulllist = (List) test.destination.getI();
            if (fulllist != null && fulllist.size() > 0) {
                //printList(fulllist);
                System.out.println("          sizef: "+((List)(test.destination.getI())).size()+"\n");
                //List first = (List)fulllist.get(0);
                //System.out.print("\n  first 2: "+ first);
                
                //List last = (List)fulllist.get(fulllist.size()-1);
                //System.out.print("\n  last 2: "+ last);
                
                assertEquals(fulllist.size(),16);
                assertEquals(fulllist,ass_arrList);
                
            } 
            
            // Test 3
            oldtimestamp = test.destination.getTimestamp();
            System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp before: "+TimeStamp.getStringTimeStamp(oldtimestamp, "dd/MM/yyyy HH:mm:ss.SSS"));
            
            int_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 256; i++) {
                int_arrList.add((float) 1);
            }
            ass_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 0; i < 16; i++) {
                ass_arrList.add((float) 6.0);
                
            }
            
            newtimestamp = test.destination.getTimestamp();
            test.testFeapMapCodelet.resetTriggers();
            //source_arrList.setI(int_arrList);
            mo_arrList.add(int_arrList);
            test.source.setI(mo_arrList);
            test.source2.setI(mo_arrList);
            test.source3.setI(mo_arrList);
            
            while(test.testFeapMapCodelet.steps < 3) {
                newtimestamp = test.destination.getTimestamp();
                System.out.println("steps: "+test.testFeapMapCodelet.steps+" Timestamp while waiting: "+TimeStamp.getStringTimeStamp(newtimestamp,"dd/MM/yyyy HH:mm:ss.SSS"));
            }
            weig_arrList = new CopyOnWriteArrayList<Float>();
            for (int i = 1; i < 4; i++) {
                weig_arrList.add((float) i);
            }
            
            test.weights.setI(weig_arrList);
            System.out.println("  Inputs 31: "+test.source.getI());
            System.out.println("  Inputs 32: "+test.source2.getI());
            System.out.println("  Inputs 33: "+test.source3.getI());
            System.out.print("  Output 3: "+ test.destination.getI());
            fulllist = (List) test.destination.getI();
            if (fulllist != null && fulllist.size() > 0) {
                //printList(fulllist);
                System.out.println("          sizef: "+fulllist.size()+"\n");
                //List first = (List)fulllist.get(0);
                //System.out.print("\n  first 3: "+ first);
                
                //List last = (List)fulllist.get(fulllist.size()-1);
                //System.out.print("\n  last 3: "+ last);
                
                assertEquals(fulllist.size(),16);
                assertEquals(fulllist,ass_arrList);
                
            }
        //}
    }
    // This class contains tests covering some core Codelet methods
    
    // This method is used to generate a new Codelet
    /*CombFeatMapCodelet generateCombFeatMapCodelet() {
                //Buffers list
        CopyOnWriteArrayList<String> sensor_names_vision = new CopyOnWriteArrayList<>();
        sensor_names_vision.add("TEST");
        CombFeatMapCodelet testCombFeatMapCodelet = new CombFeatMapCodelet(1, sensor_names_vision, 10, 32) {

        @Override
        public void accessMemoryObjects() {}
   
        
        
        @Override
        public void proc() {
            System.out.println("proc method in CombFeapMapCodeletTest ran correctly!");
        }
        @Override
        public void calculateActivation() {}

            @Override
            public void calculateCombFeatMap() {
                System.out.println("calculateCombFeatMap method ran correctly!");
            }
    };
     return(testCombFeatMapCodelet);   
    }*/
}

