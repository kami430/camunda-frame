package com.flow.base.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseFlowEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    // 流程实例（运行中）id
    protected String processInstanceId;
    // 流程启动时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime procStartTime;
    // 流程结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime procEndTime;
    // 流程任务
    @Transient
    protected Task task;
    // 流程参数
    @Transient
    protected Map<String,Object> variables;
    // 流程实例（运行中）
    @Transient
    protected ProcessInstance processInstance;
    // 历史流程实例
    @Transient
    protected HistoricProcessInstance historicProcessInstance;
    // 流程定义
    @Transient
    protected ProcessDefinition processDefinition;
}
