package com.camunda.demo.business.controller;

import com.camunda.demo.base.response.ResponseEntity;
import com.camunda.demo.base.shiro.LoginUtils;
import com.camunda.demo.base.jxlsEngine.JxlsUtils;
import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.business.service.UserService;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import com.camunda.demo.dataInterface.entity.authorization.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/sign")
    public ResponseEntity login() {
        try {
            return LoginUtils.login("jordan", "kermit");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.error("登陆失败：");
        }
    }

    @GetMapping("/credit")
    public List<UserCredential> loginCredential() {
        return userService.loginCredential();
    }

    @GetMapping("/newUser")
    public ResponseEntity newUser(UserForm userForm) {
        try {
            return ResponseEntity.ok(userService.newUser(userForm));
        } catch (Exception e) {
            return ResponseEntity.ok(new Date());
        }
    }

    @GetMapping("/listUser")
    public List<LoginUser> listUser() throws Exception {
        return userService.findPageUser(new HashMap<>(), 1, 20);
    }

    @PostMapping("/haha")
    public void haha(@RequestBody UserForm userForm) {
        System.out.println(userForm);
    }

    @GetMapping("/tmpl")
    public void tmpl(HttpServletResponse response) {
        File infile = JxlsUtils.getTemplate("C:\\Users\\hxyd_cong\\Desktop\\reporttest\\tmpl.xlsx");
        List<Map<String,Object>> models = new ArrayList<>();
        for(int i=0;i<10;i++){
            Map<String, Object> model = new HashMap<>();
            model.put("name", "bilibili+"+i);
            model.put("sex", "男+"+i);
            model.put("age", 18+i);
            model.put("job", "股神+"+i);
            model.put("birth",new Date());
            models.add(model);
        }
        Map<String,Object> dataList = new HashMap<>();
        dataList.put("dt",models);
        try (FileInputStream inputStream = new FileInputStream(infile);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" +infile.getName());
            response.setCharacterEncoding("UTF-8");
            JxlsUtils.exportExcel(inputStream, outputStream, dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
