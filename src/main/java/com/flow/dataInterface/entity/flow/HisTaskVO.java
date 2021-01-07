package com.flow.dataInterface.entity.flow;

import com.flow.base.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import java.time.LocalDateTime;

@Data
public class HisTaskVO {

    private String id;
    private String name;
    private String assignee;
    private String owner;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    public HisTaskVO(HistoricTaskInstance instance){
        this.setId(instance.getId());
        this.setName(instance.getName());
        this.setOwner(instance.getOwner());
        this.setAssignee(instance.getAssignee());
        this.setDescription(instance.getDescription());
        this.setStartTime(DateUtils.asLocalDateTime(instance.getStartTime()));
        this.setEndTime(DateUtils.asLocalDateTime(instance.getEndTime()));
    }
}
