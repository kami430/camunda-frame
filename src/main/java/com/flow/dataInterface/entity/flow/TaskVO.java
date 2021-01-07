package com.flow.dataInterface.entity.flow;

import com.flow.base.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.camunda.bpm.engine.task.Task;

import java.time.LocalDateTime;

@Data
public class TaskVO {
    private String id;
    private String name;
    private String assignee;
    private String owner;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public TaskVO(Task task) {
        this.setId(task.getId());
        this.setName(task.getName());
        this.setAssignee(task.getAssignee());
        this.setOwner(task.getOwner());
        this.setDescription(task.getDescription());
        this.setCreateTime(DateUtils.asLocalDateTime(task.getCreateTime()));
    }

}
