package com.camunda.demo.dataInterface.entity;

import com.camunda.demo.dataInterface.constant.IEntityStatus;
import com.camunda.demo.base.constant.IConst;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attachement")
@Data
public class Attachement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ext;

    private Long size;

    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @IConst(IEntityStatus.class)
    private Integer status;
}
