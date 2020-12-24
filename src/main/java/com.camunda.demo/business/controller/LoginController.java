package com.camunda.demo.business.controller;

import com.camunda.demo.base.repository.Pager;
import com.camunda.demo.base.response.ResponseEntity;
import com.camunda.demo.base.shiro.LoginUtils;
import com.camunda.demo.base.utils.JxlsUtils;
import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.business.service.UserService;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import com.camunda.demo.dataInterface.entity.authorization.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
            UserForm uf = new UserForm();
            uf.setAccount("haha");
            uf.setName("kele");
            uf.setPassword("bilibili");
            return ResponseEntity.ok(userService.newUser(uf));
        } catch (Exception e) {
            return ResponseEntity.ok(new Date());
        }
    }

    @GetMapping("/listUser")
    public Pager<LoginUser> listUser() {
        return userService.findPageUser(new HashMap<>(), 1, 20);
    }

    @PostMapping("/haha")
    public void haha(@Validated UserForm userForm) {
        System.out.println(userForm);
    }

    @GetMapping("/tmpl")
    public void tmpl(HttpServletResponse response) {
        List<Map<String, Object>> models = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> model = new HashMap<>();
            model.put("name", "bilibili+" + i);
            model.put("sex", "男+" + i);
            model.put("age", 18 + i);
            model.put("job", "股神+" + i);
            model.put("idCard", "441283199812104623");
            models.add(model);
        }
        List<JxlsUtils.Pager> pager = JxlsUtils.getPagers(models,10);
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("dt", models);
        dataList.put("deptname", "肇庆市");
        dataList.put("pages",pager);
        dataList.put("sheetnames",JxlsUtils.getSheetNames(pager));
        Map<String,Object> funcMap = new HashMap<>();
        funcMap.put("fn",new JxlsUtils.JxlsFunc());
        JxlsUtils.exportExcel("multitmpl.xlsx", response, dataList,funcMap);
    }

    @GetMapping("/hali")
    public void hali() {

    }

}
