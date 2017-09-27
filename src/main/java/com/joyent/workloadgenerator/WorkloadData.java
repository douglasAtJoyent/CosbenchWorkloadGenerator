package com.joyent.workloadgenerator;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class WorkloadData implements CosbenchGeneratorData {

    String name;
    String description;
    String config;
    StorageData storage;
    WorkflowData wfdata;

    public WorkloadData(String name, String description, String config, StorageData storage, WorkflowData wfdata) {
        super();
        this.name = name;
        this.description = description;
        this.config = config;
        this.storage = storage;
        this.wfdata = wfdata;
    }

    
    /**
     * This will generate the document and write it out to the file.
     * @param toFile
     * @return
     */
    public void generateDocument(String toFile) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            document.appendChild(toXML(document));
            // Now we write the document to a file.
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Result output = new StreamResult(new File(toFile));
            Source input = new DOMSource(document);
            transformer.transform(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Element toXML(Document rootDoc) {
        Element workload = rootDoc.createElement("workload");
        workload.setAttribute("name", name);
        workload.setAttribute("description", description);
        workload.setAttribute("config", config);
        workload.appendChild(storage.toXML(rootDoc));
        workload.appendChild(wfdata.toXML(rootDoc));
        return workload;
    }

}
