package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WorkstageData implements CosbenchGeneratorData {
    String name;
    String description;
    List<WorkData> work;
    
    public WorkstageData(String name) {
        super();
        this.name = name;
        description = "This file was generated, feel free to change it";
        work = new LinkedList<WorkData>();
    }

    public void addWork(WorkData newWork) {
        work.add(newWork);
    }

    public Element toXML(Document rootDoc) {
        Element workstage = rootDoc.createElement("workstage");
        workstage.setAttribute("name", name);
        workstage.setAttribute("description", description);
        for (WorkData cur : work) {
            workstage.appendChild(cur.toXML(rootDoc));
        }
        return workstage;
    }

    @Override
    public String toString() {
        return "WorkstageData [name=" + name + ", work=" + work + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WorkData> getWork() {
        return work;
    }

    public void setWork(List<WorkData> work) {
        this.work = work;
    }

}
