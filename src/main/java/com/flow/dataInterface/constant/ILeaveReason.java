package com.flow.dataInterface.constant;

import com.flow.base.constant.IConstInfo;

public @interface ILeaveReason {
    @IConstInfo(code = ILeaveReason.OTHER, remark = "其他")
    int OTHER = 0;
    @IConstInfo(code = ILeaveReason.CASUAL, remark = "事假")
    int CASUAL = 1;
    @IConstInfo(code = ILeaveReason.SICK, remark = "病假")
    int SICK = 2;
    @IConstInfo(code = ILeaveReason.MARRIAGE, remark = "婚假")
    int MARRIAGE = 3;
    @IConstInfo(code = ILeaveReason.BEREAVEMENT, remark = "丧假")
    int BEREAVEMENT = 4;
}
