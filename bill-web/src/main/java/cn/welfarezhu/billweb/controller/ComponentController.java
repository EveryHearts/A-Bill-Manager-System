package cn.welfarezhu.billweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ComponentController {

    @GetMapping("/component/nav")
    public String nav(){
        return "component/navigator";
    }
}
