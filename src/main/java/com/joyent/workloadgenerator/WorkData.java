package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WorkData implements CosbenchGeneratorData,Comparable {
    String type;
    String workers;
    String config;
    List<OperationData> operation;

    public WorkData(String type, String workers, String config) {
        super();
        this.type = type;
        this.workers = workers;
        this.config = config;
        operation = new LinkedList();

    }

    public void addOperation(OperationData od) {
        operation.add(od);
    }
    
    public void checkRange() { 
        int ratio = Integer.parseInt(operation.get(0).getRatio());
        if(operation.size() % 100 != 0) { 
            for(int i = 0;i < operation.size() % 100;i++)  { 
               operation.get(i).setRatio(new Integer(operation.size() + 1).toString());
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWorkers() {
        return workers;
    }

    public void setWorkers(String workers) {
        this.workers = workers;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<OperationData> getOperation() {
        return operation;
    }

    public void setOperation(List<OperationData> operation) {
        this.operation = operation;
    }

    public Element toXML(Document rootDoc) {
        Element workElement = rootDoc.createElement("work");
        workElement.setAttribute("type", type);
        workElement.setAttribute("workers", workers);
        workElement.setAttribute("config", config);
        for (OperationData op : operation) {
            workElement.appendChild(op.toXML(rootDoc));
        }

        return workElement;
    }
    
    @Override
    public String toString() {
        return "WorkData [type=" + type + ", workers=" + workers + ", config=" + config + ", operation=" + operation
                + "]";
    }
    
    
    public int getLevel() { 
        return config.split("/").length;
    }

    public int compareTo(Object o) {
        try { 
            WorkData cur = (WorkData)o;
            return getLevel() - cur.getLevel();
        } catch(Exception e) { 
            return 0;
        }
    }

}
