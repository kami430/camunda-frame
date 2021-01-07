package com.flow.business.controller;

import com.flow.base.response.ResponseEntity;
import com.flow.business.form.LeaveForm;
import com.flow.dataInterface.dao.LeaveDao;
import com.flow.dataInterface.entity.Leave;
import com.flow.dataInterface.entity.flow.ProcessInstanceVO;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private TaskService taskService;

    @PostMapping("/apply")
    public ProcessInstanceVO applyLeave(LeaveForm leaveForm) {
        Leave leave = leaveDao.save(leaveForm.buildEntity());
        String businessKey = leave.getId().toString();
        ProcessInstanceVO processInstanceVO = null;
        try {
            identityService.setAuthenticatedUserId(leave.getLoginUser().getAccount());
            processInstanceVO = new ProcessInstanceVO(runtimeService.startProcessInstanceByKey("leave", businessKey, new HashMap<>()));
            String processInstanceId = processInstanceVO.getProcessInstanceId();
            leave.setProcessInstanceId(processInstanceId);
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstanceVO;
    }

    public List<Leave> findTodoTasks(String account){
    }

    public ResponseEntity approveLeave(LeaveForm leaveForm) {

    }
}
