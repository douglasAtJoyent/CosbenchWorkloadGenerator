package com.joyent.workloadgenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Simple interface so that I can guarantee a toXML.
 * @author DouglasAnderson
 */
public interface CosbenchGeneratorData {

    /**
     * For each of these classes, I want a toXML to make it easier to create
     * the final Document.
     *
     * @param rootDoc - document that this element will be attached to.
     * @return - An XML representation of the class.
     */
    Element toXML(Document rootDoc);

}
