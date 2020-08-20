package com.camunda.demo.dataInterface.entity.flow;

import com.camunda.demo.base.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricProcessInstance;

import java.time.LocalDateTime;

@Data
public class HisProcessInstanceVO {

    private String id;
    private String businessKey;
    private String startUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String state;
    private String deleteReason;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime removalTime;

    public HisProcessInstanceVO(HistoricProcessInstance instance){
        this.setId(instance.getId());
        this.setBusinessKey(instance.getBusinessKey());
        this.setDeleteReason(instance.getDeleteReason());
        this.setRemovalTime(DateUtils.asLocalDateTime(instance.getRemovalTime()));
        this.setStartTime(DateUtils.asLocalDateTime(instance.getStartTime()));
        this.setEndTime(DateUtils.asLocalDateTime(instance.getEndTime()));
        this.setStartUserId(instance.getStartUserId());
        this.setState(instance.getState());
    }
}
