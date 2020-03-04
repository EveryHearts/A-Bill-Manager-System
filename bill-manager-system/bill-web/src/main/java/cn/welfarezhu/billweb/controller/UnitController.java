package cn.welfarezhu.billweb.controller;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.model.WorkingUnit;
import cn.welfarezhu.billservice.service.RecordService;
import cn.welfarezhu.billservice.service.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@Slf4j
public class UnitController {

    @Resource
    private UnitService unitService;
    @Resource
    private RecordService recordService;

    @GetMapping("/unit/register")
    public String register(){
        return "unit/unit-register";
    }

    @PostMapping("/unit/register/checkName")
    @ResponseBody
    public int checkUnitName(@RequestParam("unitName") String name){
        if (!unitService.countUnitName(name)){
            return FAIL;
        }
        return SUCCESS;
    }

    @PostMapping("/unit/register/submit")
    @ResponseBody
    public int registerValid(WorkingUnit unit, HttpSession session){
        if (unit.getUnitName()==null||unit.getUnitAddress()==null||unit.getUnitPhone()==null){
            return FAIL;
        }
        if (unit.getUnitName().trim().equals("")||unit.getUnitAddress().trim().equals("")||unit.getUnitPhone().trim().equals("")){
            return FAIL;
        }
        unit.setRegisterDate(new Date());
        session.setAttribute(REGISTER_UNIT,unit);
        log.info(" The unit :{} is already story ",unit);
        return SUCCESS;
    }

    @GetMapping("/unit/operation/query")
    public String unitOperationRecord(@RequestParam(value = "pageNo",required = false) int pageNo,
                                      Model model,HttpSession session){
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        int pageSize=(int)session.getAttribute(PAGE_SIZE);
        if (pageNo==0){
            pageNo=1;
        }
        model.addAttribute(QUERY_RECORD,recordService.queryRecordByUnitId(user.getUnitId(),pageNo,pageSize));
        return "unit/unit-operation-query";
    }
}
