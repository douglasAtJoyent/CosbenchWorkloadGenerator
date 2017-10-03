package com.joyent.workloadgenerator;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents <workstagedata>.
 *
 * @author DouglasAnderson
 *
 */
public class WorkstageData implements CosbenchGeneratorData {
    /**
     * name attribute.
     */
    private String name;
    /**
     * description attribute.
     */
    private String description;
    /**
     * work tag.
     */
    private List<WorkData> work;

    /**
     * Basic constructor.
     * @param name - name attribute.
     */
    public WorkstageData(final String name) {
        super();
        this.name = name;
        description = "This file was generated, feel free to change it";
        work = new LinkedList<WorkData>();
    }
    /**
     * Adds a new work child.
     * @param newWork - the WorkData to be added.
     */
    public void addWork(final WorkData newWork) {
        work.add(newWork);
    }

    /**
     * This will return a XML representation of the object.
     *
     * @param rootDoc - The document that the element will attach to.
     * @return Element - the XML representation of the object.
     */
    public Element toXML(final Document rootDoc) {
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
     * @return the work
     */
    public final List<WorkData> getWork() {
        return work;
    }

    /**
     * @param work the work to set
     */
    public final void setWork(final List<WorkData> work) {
        this.work = work;
    }
}
