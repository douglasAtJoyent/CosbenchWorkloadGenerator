package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Workflow data representation.
 * @author DouglasAnderson
 *
 */
public class WorkflowData implements CosbenchGeneratorData {
    /**
     * A list of child workstage.
     */
    private List<WorkstageData> workstages;
    /**
     * Constructor.
     */
    public WorkflowData() {
        super();
        workstages = new LinkedList<WorkstageData>();
    }
    /**
     * Adds a workstage data for this object.
     * @param data - data to be added.
     */
    public void addWorkStage(final WorkstageData data) {
        workstages.add(data);
    }
    /**
     * @return List<WorkstageData> - a list of workstage data.
     */
    public List<WorkstageData> getWorkstages() {
        return workstages;
    }
    /**
     * @param workstages -data to replace the field.
     */
    public void setWorkstages(final List<WorkstageData> workstages) {
        this.workstages = workstages;
    }

    /**
     * This will return a XML representation of the object.
     *
     * @param doc - The document that the element will attach to.
     * @return Element - the XML representation of the object.
     */
    public Element toXML(final Document doc) {
        Element workflow = doc.createElement("workflow");
        for (WorkstageData stage : workstages) {
            workflow.appendChild(stage.toXML(doc));
        }
        return workflow;
    }

}
