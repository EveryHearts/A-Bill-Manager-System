package cn.welfarezhu.billweb.controller;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ManagerController {

    @GetMapping("/system/loginFail")
    public String loginFail(Model model, HttpSession session){
        if (session.getAttribute(LOGIN_FAIL)==null||!(boolean)session.getAttribute(LOGIN_FAIL)){
            return "system/system-login";
        }
        model.addAttribute(SIMPLE_MSG,true);
        session.setAttribute(LOGIN_FAIL,false);
        return "system/system-login";
    }
}
