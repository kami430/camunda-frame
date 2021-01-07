package com.flow.dataInterface.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flow.base.constant.IConst;
import com.flow.base.repository.BaseFlowEntity;
import com.flow.dataInterface.constant.ILeaveReason;
import com.flow.dataInterface.entity.authorization.LoginUser;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "leave")
public class Leave extends BaseFlowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private LoginUser loginUser;
    @IConst(ILeaveReason.class)
    private Integer reason;
    private String remarks;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
}
