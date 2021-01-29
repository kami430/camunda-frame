package com.flow.business.controller;

import com.flow.base.repository.Pager;
import com.flow.base.repository.Param;
import com.flow.base.response.ResponseEntity;
import com.flow.base.shiro.LoginUtils;
import com.flow.base.utils.JxlsUtils;
import com.flow.business.form.UserForm;
import com.flow.business.service.UserService;
import com.flow.dataInterface.dao.CredientialDao;
import com.flow.dataInterface.entity.authorization.LoginUser;
import com.flow.dataInterface.entity.authorization.UserCredential;
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
    @Autowired
    private CredientialDao credientialDao;

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
        for (int i = 0; i < 10; i++) {
            Map<String, Object> model = new HashMap<>();
            model.put("name", "bilibili+" + i);
            model.put("sex", "男+" + i);
            model.put("age", 18 + i);
            model.put("job", "股神+" + i);
            model.put("idCard", "441283199812104623");
            models.add(model);
        }
        Map<String, Object> dataList = new HashMap<>();
        dataList.put("deptname", "肇庆市");
        dataList.put("message", "测试信息");
        Map<String, Object> funcMap = new HashMap<>();
        JxlsUtils.exportExcel("tmpl2.xlsx", response, dataList, funcMap);
    }

    @GetMapping("/hali")
    public List<Map> hali() {
        List<String> accounts = new ArrayList<>();
        accounts.add("haha");
        accounts.add("bilibli");
        return credientialDao.abc(1L, accounts);
    }

}