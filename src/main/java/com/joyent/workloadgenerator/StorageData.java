package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StorageData {
    public static final String STOARGE = "storage";
    
    String type;
    List<String> configs;

    public StorageData(String type) {
        super();
        this.type = type;
        configs = new LinkedList<String>();
    }
    
    public void addConfig(String conf) { 
        configs.add(conf);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getConfigs() {
        return configs;
    }

    public void setConfigs(List<String> configs) {
        this.configs = configs;
    }


    public Element toXML(Document parent) {
        Element node = parent.createElement(STOARGE);
        node.setAttribute("type", type);
        String configString = "";
        for (String config : configs) {
            configString += config + ";";
        }
        node.setAttribute("type", type);
        node.setAttribute("config", configString);
        return node;

    }
}