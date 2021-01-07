package com.flow.dataInterface.constant;

import com.flow.base.constant.IConstInfo;

public @interface IEntityStatus {
    @IConstInfo(code = IEntityStatus.ACTIVE, remark = "有效")
    int ACTIVE = 1;
    @IConstInfo(code = IEntityStatus.FREEZE, remark = "冻结")
    int FREEZE = 2;
    @IConstInfo(code = IEntityStatus.INVALID, remark = "失效")
    int INVALID = 0;
}
