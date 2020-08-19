package com.camunda.demo.dataInterface.entity.flow;

import lombok.Data;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Data
public class ProcessInstanceVO {

    private String id;
    private String businessKey;
    private String processInstanceId;
    private String caseInstanceId;
    private String rootProcessInstanceId;

    public ProcessInstanceVO(ProcessInstance instance){
        this.setId(instance.getId());
        this.setBusinessKey(instance.getBusinessKey());
        this.setProcessInstanceId(instance.getProcessInstanceId());
        this.setCaseInstanceId(instance.getCaseInstanceId());
        this.setRootProcessInstanceId(instance.getRootProcessInstanceId());
    }
}
