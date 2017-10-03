package com.joyent.workloadgenerator;

import java.io.File;

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

/**
 * This is an object representation of the workload data.
 *
 * @author DouglasAnderson
 *
 */
public class WorkloadData implements CosbenchGeneratorData {
    /**
     * The name attribute for this tag.
     */
    private String name;
    /**
     * The description attribute for this tag.
     */
    private String description;
    /**
     * The config attribute for this tag.
     */
    private String config;
    /**
     * This is a configuration for which storage is to be used.
     */
    private StorageData storage;
    /**
     * The child workflowData.
     */
    private WorkflowData wfdata;

    /**
     * Constructor that will require you to use the setters to attach storage and work flow later.
     *
     * @param name - name attribute
     * @param description - description attribute.
     * @param config - config attribute.
     */
    public WorkloadData(final String name, final String description, final String config) {
        super();
        this.name = name;
        this.description = description;
        this.config = config;
    }

    /**
     * Simple constructor.
     *
     * @param name - name attribute
     * @param description - description attribute.
     * @param config - config attribute.
     * @param storage - storage node
     * @param wfdata - workflow data node.
     */
    public WorkloadData(final String name, final String description, final String config, final StorageData storage,
            final WorkflowData wfdata) {
        super();
        this.name = name;
        this.description = description;
        this.config = config;
        this.storage = storage;
        this.wfdata = wfdata;
    }

    /**
     * This will generate the document and write it out to the file.
     *
     * @param toFile - the location of the file that this will be written to.
     *
     */
    public void generateDocument(final String toFile) {
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

    /**
     * This will return a XML representation of the object.
     *
     * @param rootDoc - The document that the element will attach to.
     * @return Element - the XML representation of the object.
     */
    public Element toXML(final Document rootDoc) {
        Element workload = rootDoc.createElement("workload");
        workload.setAttribute("name", name);
        workload.setAttribute("description", description);
        workload.setAttribute("config", config);
        workload.appendChild(storage.toXML(rootDoc));
        workload.appendChild(wfdata.toXML(rootDoc));
        return workload;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public final void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the config
     */
    public final String getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public final void setConfig(final String config) {
        this.config = config;
    }

    /**
     * @return the storage
     */
    public final StorageData getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public final void setStorage(final StorageData storage) {
        this.storage = storage;
    }

    /**
     * @return the wfdata
     */
    public final WorkflowData getWfdata() {
        return wfdata;
    }

    /**
     * @param wfdata the wfdata to set
     */
    public final void setWfdata(final WorkflowData wfdata) {
        this.wfdata = wfdata;
    }

}
