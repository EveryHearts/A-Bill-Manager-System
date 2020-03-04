package cn.welfarezhu.billweb.controller;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.enums.MessageStatusCode;
import cn.welfarezhu.billservice.enums.OperationCode;
import cn.welfarezhu.billservice.enums.RoleCode;
import cn.welfarezhu.billservice.enums.RoleStatusCode;
import cn.welfarezhu.billservice.service.MessageService;
import cn.welfarezhu.billservice.service.RecordService;
import cn.welfarezhu.billservice.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class SystemController {

    private static final String QUERY_RECORD="query_record";
    private static final String QUERY_MSG="query_msg";

    @Resource
    private RecordService recordService;
    @Resource
    private UserService userService;
    @Resource
    private MessageService messageService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/system/nav")
    public String systemNav(){
        return "system/system-navigator";
    }

    @GetMapping("/system/login")
    public String loginPage(){
        return "system/system-login";
    }

    @GetMapping("/system/admin")
    public String adminPage(@RequestParam(value = "pageNo",required = false) int pageNo,Model model){
        if (pageNo==0){
            pageNo=1;
        }
        model.addAttribute(QUERY_RECORD,recordService.queryRecordByOperationSort(OperationCode.ADD_NEW_ROLE.operationNum(),pageNo,30));
        return "system/system-record-query";
    }

    @GetMapping("/system/user/info")
    public String SystemUserInfo(@RequestParam("userId") int userId, Model model){
        model.addAttribute(QUERY_USER,userService.queryUserByUserId(userId));
        return "system/system-user-info";
    }

    @PostMapping("/system/user/status/modify")
    @ResponseBody
    public int systemUserStatusModify(@RequestParam("userId") int userId,@RequestParam("statusCode") int statusCode){
        if (statusCode!= RoleStatusCode.BAN.statusCode() && statusCode !=RoleStatusCode.LOGOUT.statusCode()){
            if (userService.modifyStatusCode(userId,statusCode)){
                return SUCCESS;
            }
            return FAIL;
        }
        if (userService.queryRoleCode(userId)!= RoleCode.REGISTER.roleNum()){
            if (userService.modifyStatusCode(userId,statusCode)){
                return SUCCESS;
            }
        }else{
            UserDTO user=userService.queryUserByUserId(userId);
            List<Integer> idList=userService.queryUserIdListByUnitId(user.getUnitId());
            for (int id:idList){
                userService.modifyStatusCode(id,statusCode);
            }
            return SUCCESS;
        }
        return FAIL;
    }

    @PostMapping("/set/pageSize/modify")
    @ResponseBody
    public int pageSizeModify(@Param("pageSize") int pageSize, HttpSession session){
        if (pageSize<10||pageSize>50){
            return FAIL;
        }
        session.setAttribute(PAGE_SIZE,pageSize);
        return SUCCESS;
    }

    @PostMapping("/set/pageSize/get")
    @ResponseBody
    public int getPageSize(HttpSession session){
        return (int)session.getAttribute(PAGE_SIZE);
    }

    @GetMapping("/system/msg/query")
    public String systemMsgQuery(@RequestParam(value = "pageNo",required = false) int pageNo,Model model,HttpSession session){
        if (pageNo==0){
            pageNo=1;
        }
        int pageSize=(int)session.getAttribute(PAGE_SIZE);
        model.addAttribute(QUERY_MSG,messageService.queryMessageByReceptorIdAndStatusCode(0, MessageStatusCode.WAIT_READ.statusCode(),pageNo,pageSize));
        return "system/system-msg";
    }
}
