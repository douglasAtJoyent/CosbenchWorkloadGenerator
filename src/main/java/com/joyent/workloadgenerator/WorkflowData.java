package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WorkflowData implements CosbenchGeneratorData {

    List<WorkstageData> workstages;
    
    public WorkflowData() {
        super();
        workstages = new LinkedList<WorkstageData>();
    }
    
    public void addWorkStage(WorkstageData data) { 
       workstages.add(data);
    }
    
    
    public List<WorkstageData> getWorkstages() {
        return workstages;
    }

    public void setWorkstages(List<WorkstageData> workstages) {
        this.workstages = workstages;
    }

    public Element toXML(Document doc) {
        Element workflow = doc.createElement("workflow");
        for (WorkstageData stage : workstages) {
            workflow.appendChild(stage.toXML(doc));
        }
        return workflow;
    }
    


}
