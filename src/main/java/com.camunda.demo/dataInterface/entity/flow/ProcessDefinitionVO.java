package com.camunda.demo.dataInterface.entity.flow;

import lombok.Data;
import org.camunda.bpm.engine.repository.ProcessDefinition;

@Data
public class ProcessDefinitionVO {

    private String id;
    private String key;
    private String name;
    private String description;
    private String deploymentId;
    private String resourceName;
    private String DiagramResourceName;
    private String tenantId;
    private int version;
    private String versionTag;

    public ProcessDefinitionVO(ProcessDefinition processDefinition) {
        this.setId(processDefinition.getId());
        this.setKey(processDefinition.getKey());
        this.setName(processDefinition.getName());
        this.setDescription(processDefinition.getDescription());
        this.setDeploymentId(processDefinition.getDeploymentId());
        this.setResourceName(processDefinition.getResourceName());
        this.setDiagramResourceName(processDefinition.getDiagramResourceName());
        this.setTenantId(processDefinition.getTenantId());
        this.setVersion(processDefinition.getVersion());
        this.setVersionTag(processDefinition.getVersionTag());
    }
}
