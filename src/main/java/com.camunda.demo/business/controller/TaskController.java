package com.camunda.demo.business.controller;

import com.camunda.demo.base.utils.RuleUtils;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;


    @GetMapping("/start")
    public void startProcess(@RequestParam("procid") String procid) {
        Map<String, Object> var = new HashMap<>();
        var.put("owner", "jordan");
        runtimeService.startProcessInstanceById(procid, var);
    }

    @GetMapping("/list")
    public List<String> taskList(@RequestParam("account") String account) {
        return taskService.createTaskQuery().taskCandidateUser(account).list()
                .stream().map(task -> task.getId()).collect(Collectors.toList());
    }

    @GetMapping("/deploy")
    public void deployProc(@RequestParam("bpmn") String bpmn) {
        String bpmnPath = "proc/" + bpmn + ".bpmn";
        DeploymentBuilder builder = repositoryService
                .createDeployment()
                .addClasspathResource(bpmnPath);
        builder.deploy();
    }

    @GetMapping("/processList")
    public List<Map<String, Object>> instances(@RequestParam("account") String account) {
        return repositoryService.createProcessDefinitionQuery().list().stream()
                .map(defi -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", defi.getId());
                    map.put("key", defi.getKey());
                    return map;
                }).collect(Collectors.toList());
    }

    @GetMapping("/complete")
    public void complete(@RequestParam("taskid") String taskid) {
        Map<String, Object> var = taskService.getVariables(taskid);
        taskService.complete(taskid);
    }

    @GetMapping("/rule")
    public LoginUser rule() throws IOException {
        KieSession session = RuleUtils.newSession("rule01", "rule02");
        LoginUser user = new LoginUser();
        user.setName("haha");
        user.setId(90L);
        session.insert(user);
        session.fireAllRules();
        return user;
    }
}
