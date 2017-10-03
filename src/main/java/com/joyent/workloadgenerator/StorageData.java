package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This represents the storage tag in the document.
 *
 * @author DouglasAnderson
 */
public class StorageData {
    /**
     * Storage constant.
     */
    public static final String STOARGE = "storage";
    /**
     * Type attribute.
     */
    private String type;

    /**
     * Config attribute.
     */
    private List<String> configs;

    /**
     * Basic constructor.
     *
     * @param type - type to add.
     */
    public StorageData(final String type) {
        super();
        this.type = type;
        configs = new LinkedList<String>();
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }



    /**
     * @param type the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }



    /**
     * @return the configs
     */
    public List<String> getConfigs() {
        return configs;
    }



    /**
     * @param configs the configs to set
     */
    public void setConfigs(final List<String> configs) {
        this.configs = configs;
    }



    /**
    * @param parent - the document it will attach to.
    * @return a representation fo this element
    */
    public Element toXML(final Document parent) {
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

    /**
     * Adds a config to the internal list.
     * @param string - the config to be added
     */
    public void addConfig(final String string) {
        this.configs.add(string);
    }
}
