package org.workflow.core.activity;

import org.workflow.core.model.Variable;

import java.util.ArrayList;
import java.util.List;

public class ProcessAllTaskSearchRequest {
    private boolean unassigned = true;
    private byte size = 10;
    private int start = 0;
    private String order = "desc";
    private String sort = "createTime";
    private String candidateGroup;
    private  String processDefinitionKey;
    private static final byte MINBATCHSIZE = 0;
    private static final byte MAXBATCHSIZE = 0;
    private static final int MINSTARTINDEX = 0;
    private List<Variable> processInstanceVariables = new ArrayList<Variable>();

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public void addProcessVariable(Variable var) {
        processInstanceVariables.add(var);
    }

    public List<Variable> getProcessInstanceVariables() {
        return processInstanceVariables;
    }

    public void setProcessInstanceVariables(List<Variable> processInstanceVariables) {
        this.processInstanceVariables = processInstanceVariables;
    }

    public String getCandidateGroup() {
        return candidateGroup;
    }

    public void setCandidateGroup(String candidateGroup) {
        this.candidateGroup = candidateGroup;
    }

    public byte getSize() {
        return size;
    }

    public void setSize(byte batchSize) {
        if (batchSize > MINBATCHSIZE && batchSize < MAXBATCHSIZE) {
            this.size = batchSize;
        }
    }

    public int getStart() {

        return start;
    }

    public void setStart(int start) {
        if (start > MINSTARTINDEX) {
            this.start = start;
        }
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String orderBy) {
        if (orderBy != null
                && !orderBy.isEmpty()
                && (orderBy.trim().equalsIgnoreCase("desc")
                || orderBy.trim().equalsIgnoreCase("asc"))) {
            this.order = orderBy;
        }
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sortBy) {
        if (sortBy != null
                && !sortBy.isEmpty()
                && (sortBy.equalsIgnoreCase("createTime"))) {
            this.sort = sortBy;
        }
    }

    public boolean isUnassigned() {
        return unassigned;
    }

    public void setUnassigned(boolean Unassigned) {
        this.unassigned = Unassigned;
    }
}
