package com.joyent.workloadgenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Will put together a Operation tag.
 * @author DouglasAnderson
 *
 */
public class OperationData implements CosbenchGeneratorData {
    /**
     * type attribute.
     */
    private String type;
    /**
     * ratio attribute.
     */
    private String ratio;
    /**
     * config attribute.
     */
    private String config;

    /**
     * Generic constructor.
     *
     * @param type - type attribute.
     * @param ratio - ratio attribute.
     * @param config - config attribute.
     */
    public OperationData(final String type, final String ratio, final String config) {
        super();
        this.type = type;
        this.ratio = ratio;
        this.config = config;
    }

    /**
     * This will produce a XML representation of this object.
     * @param rootDoc - The document to attach this element.
     * @return - A fully formed element.
     */
    public Element toXML(final Document rootDoc) {
        Element ops = rootDoc.createElement("operation");
        ops.setAttribute("type", type);
        ops.setAttribute("ratio", ratio);
        ops.setAttribute("config", config);
        return ops;
    }

    @Override
    public String toString() {
        return "OperationData [type=" + type + ", ratio=" + ratio + ", config=" + config + "]";
    }

    /**
     * @return the type
     */
    public final String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(final String type) {
        this.type = type;
    }

    /**
     * @return the ratio
     */
    public final String getRatio() {
        return ratio;
    }

    /**
     * @param ratio the ratio to set
     */
    public final void setRatio(final String ratio) {
        this.ratio = ratio;
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

}
