package com.camunda.demo.business.controller;

import com.camunda.demo.base.http.ResponseEntity;
import com.camunda.demo.base.shiro.LoginUtils;
import com.camunda.demo.business.DTO.UserDto;
import com.camunda.demo.business.service.UserService;
import com.camunda.demo.dataInterface.entity.authorization.LoginCredential;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    public List<LoginCredential> loginCredential() {
        return userService.loginCredential();
    }

    @GetMapping("/newUser")
    public ResponseEntity newUser() {
        try {
            UserDto dto = new UserDto();
            dto.setAccount("jordan");
            dto.setName("jordan");
            dto.setPassword("kermit");
            return ResponseEntity.ok(userService.newUser(dto));
        } catch (Exception e) {
            return ResponseEntity.ok(new Date());
        }
    }

    @GetMapping("/listUser")
    public List<LoginUser> listUser() throws Exception {
        return userService.findPageUser(new HashMap<>(), 1, 20);
    }




}
