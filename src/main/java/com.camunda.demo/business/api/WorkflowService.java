package com.camunda.demo.business.api;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 查询待办任务
     *
     * @param account
     * @return
     */
    public List<Task> taskList(String account) {
        return taskList(account, null);
    }

    /**
     * 查询待办任务(分页)
     *
     * @param account
     * @return
     */
    public List<Task> taskList(String account, Pageable pageable) {
        TaskQuery query = taskService.createTaskQuery()
                .taskCandidateUser(account).or()
                .taskAssignee(account);
        if (pageable != null) {
            return query.listPage((int) pageable.getOffset(), pageable.getPageSize());
        }
        return query.list();
    }

    /**
     * 查询任务明细
     *
     * @param taskId
     * @return
     */
    public Task task(String taskId) {
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    /**
     * 查询待办流程
     *
     * @param account
     * @return
     */
    public List<ProcessInstance> processInstanceList(String account) {
        return processInstanceList(account, null);
    }

    /**
     * 查询待办流程(分页)
     *
     * @param account
     * @return
     */
    public List<ProcessInstance> processInstanceList(String account, Pageable pageable) {
        List<Task> taskList = taskList(account, pageable);
        return taskList.stream()
                .map(task -> runtimeService.createProcessInstanceQuery()
                        .processInstanceId(task.getProcessInstanceId())
                        .active().orderByProcessInstanceId().desc()
                        .singleResult())
                .filter(processInstance -> processInstance != null)
                .collect(Collectors.toList());
    }

    /**
     * 查询流程明细
     *
     * @param processInstanceId
     * @return
     */
    public ProcessInstance processInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    /**
     * 指定任务处理人
     * @param taskId
     * @param account
     */
    public void assignee(String taskId,String account){
        taskService.setAssignee(taskId,account);
    }

    /**
     * 主动接收任务(任务已被他人接收会报异常)
     * @param taskId
     * @param account
     */
    public void claim(String taskId,String account){
        taskService.claim(taskId,account);
    }

    /**
     * 完成任务
     * @param taskId
     */
    public void complete(String taskId){
        taskService.complete(taskId);
    }

    /**
     * 完成任务
     * @param taskId
     * @param variables
     */
    public void complete(String taskId, Map<String,Object> variables){
        taskService.complete(taskId,variables);
    }


}
