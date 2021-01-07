package com.flow.business.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flow.base.constant.IConst;
import com.flow.base.repository.BaseForm;
import com.flow.dataInterface.constant.ILeaveReason;
import com.flow.dataInterface.entity.Leave;
import com.flow.dataInterface.entity.authorization.LoginUser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LeaveForm implements BaseForm<Leave> {
    private Long userId;
    @IConst(ILeaveReason.class)
    private Integer reason;
    private String remarks;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    private String procid;
    private Long approver;

    @Override
    public Leave buildEntity() {
        Leave leave = new Leave();
        LoginUser user = new LoginUser();
        user.setId(userId);
        copyProperties(this, leave);
        leave.setLoginUser(user);
        return leave;
    }
}
