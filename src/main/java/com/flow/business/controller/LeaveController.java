package com.flow.business.controller;

import com.flow.base.repository.BaseFlowEntity;
import com.flow.business.form.LeaveForm;
import com.flow.dataInterface.dao.LeaveDao;
import com.flow.dataInterface.entity.Leave;
import com.flow.dataInterface.entity.flow.ProcessInstanceVO;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        ProcessInstanceVO processInstanceVO;
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

    public ProcessInstanceVO startProc(BaseFlowEntity baseFlowEntity) {
        String businessKey = baseFlowEntity.getId().toString();
        ProcessInstanceVO processInstanceVO;
        try {
            identityService.setAuthenticatedUserId(null);
            processInstanceVO = new ProcessInstanceVO(runtimeService.startProcessInstanceByKey("leave", businessKey, new HashMap<>()));
            String processInstanceId = processInstanceVO.getProcessInstanceId();
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstanceVO;
    }

//    public List<BaseFlowEntity> findTodoTasks(String account){
//        List<BaseFlowEntity> results = new ArrayList<>();
//        TaskQuery taskQuery = taskService.createTaskQuery().taskCandidateUser(account).or().taskAssignee(account);
//        List<Task> tasks = taskQuery.list();
//        for(Task task:tasks){
//            String processInstanceId = task.getProcessInstanceId();
//            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
//            if(processInstance==null) continue;
//            String businessKey = processInstance.getBusinessKey();
//
//        }
//    }
//
//    public ResponseEntity approveLeave(LeaveForm leaveForm) {
//
//    }
}
