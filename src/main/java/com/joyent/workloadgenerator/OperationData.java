package com.joyent.workloadgenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OperationData implements CosbenchGeneratorData {
    String type;
    String ratio;
    String config;

    public OperationData(String type, String ratio, String config) {
        super();
        this.type = type;
        this.ratio = ratio;
        this.config = config;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Element toXML(Document rootDoc) {
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
    
    

}
