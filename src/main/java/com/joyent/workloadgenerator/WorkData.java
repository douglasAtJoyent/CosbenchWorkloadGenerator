package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This is a representation of the work tag.
 *
 * @author DouglasAnderson
 *
 */
public class WorkData implements CosbenchGeneratorData, Comparable {
    /**
     * The attribute type.
     */
    private String type;
    /**
     * The workers attribute.
     */
    private String workers;

    /**
     * The config attribute.
     */
    private String config;

    /**
     * the name attribute.
     */
    private String name;

    /**
     * The runtime attribute.
     */
    private String runtime;
    /**
     * The totalOps attribute.
     */
    private String totalOps;
    /**
     * The totalBytes attribute.
     */
    private String totalBytes;
    /**
     * A list of sub-elements, a list of OperationData.
     */
    private List<OperationData> operation;

    /**
     * @param type - type attribute.
     * @param workers - workers attribute.
     * @param config - workers attribute.
     */
    public WorkData(final String type, final String workers, final String config) {
        super();
        this.type = type;
        this.name = type;
        this.workers = workers;
        this.config = config;
        this.runtime = "30";
        operation = new LinkedList<OperationData>();
    }

    /**
     * Adding an operation to the internal list.
     *
     * @param od - the operation to add.
     */
    public void addOperation(final OperationData od) {
        operation.add(od);
    }

    /**
     * gets type.
     *
     * @return return type.
     */
    public String getType() {
        return type;
    }

    /**
     * Generic mutator.
     *
     * @param type - mutate.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Workers accessor.
     *
     * @return - works attribute.
     */
    public String getWorkers() {
        return workers;
    }

    /**
     * Mutator.
     *
     * @param workers - new value
     */
    public void setWorkers(final String workers) {
        this.workers = workers;
    }

    /**
     * attribute accessor.
     *
     * @return config value
     */
    public String getConfig() {
        return config;
    }

    /**
     * config mutator.
     *
     * @param config - a replacement value.
     */
    public void setConfig(final String config) {
        this.config = config;
    }

    /**
     * Operation accessor.
     *
     * @return - a list of operation data.
     */
    public List<OperationData> getOperation() {
        return operation;
    }

    /**
     * Mutator for operation.
     *
     * @param operation - a replacement value.
     */
    public void setOperation(final List<OperationData> operation) {
        this.operation = operation;
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
     * @return the runtime
     */
    public final String getRuntime() {
        return runtime;
    }

    /**
     * @param runtime the runtime to set
     */
    public final void setRuntime(final String runtime) {
        this.runtime = runtime;
    }

    /**
     * @return the totalOps
     */
    public final String getTotalOps() {
        return totalOps;
    }

    /**
     * @param totalOps the totalOps to set
     */
    public final void setTotalOps(final String totalOps) {
        this.totalOps = totalOps;
    }

    /**
     * @return the totalBytes
     */
    public final String getTotalBytes() {
        return totalBytes;
    }

    /**
     * @param totalBytes the totalBytes to set
     */
    public final void setTotalBytes(final String totalBytes) {
        this.totalBytes = totalBytes;
    }

    /**
     * This will return a XML representation of the object.
     *
     * @param rootDoc - The document that the element will attach to.
     * @return Element - the XML representation of the object.
     */
    public Element toXML(final Document rootDoc) {
        Element workElement = rootDoc.createElement("work");
        workElement.setAttribute("type", type);
        workElement.setAttribute("workers", workers);
        workElement.setAttribute("config", config);
        workElement.setAttribute("name", name);
        for (OperationData op : operation) {
            workElement.appendChild(op.toXML(rootDoc));
        }
        if (name != null && !name.isEmpty()) {
            workElement.setAttribute("name", name);
        }
        if (runtime != null && !runtime.isEmpty()) {
            workElement.setAttribute("runtime", runtime);
        }
        if (totalOps != null && !totalOps.isEmpty()) {
            workElement.setAttribute("totalOps", totalOps);
        }
        if (totalBytes != null && !totalBytes.isEmpty()) {
            workElement.setAttribute("totalBytes", totalBytes);
        }
        return workElement;
    }

    @Override
    public String toString() {
        return "WorkData [type=" + type + ", workers=" + workers + ", config=" + config + ", operation=" + operation
                + "]";
    }

    /**
     * Gets the current level.
     *
     * @return the current level.
     */
    public int getLevel() {
        return config.split("/").length;
    }

    /**
     * Comparison method.
     *
     * @param o - the object ot compare to.
     * @return - an integer representing if the object is above or below the other one in the tree.
     */
    public int compareTo(final Object o) {
        try {
            WorkData cur = (WorkData) o;
            return getLevel() - cur.getLevel();
        } catch (Exception e) {
            return 0;
        }
    }

}
