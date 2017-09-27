/**
 * 
 */
package com.joyent.workloadgenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author DouglasAnderson
 *
 */
public interface CosbenchGeneratorData {

    public Element toXML(Document rootDoc);
    
}
