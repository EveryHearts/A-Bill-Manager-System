package cn.welfarezhu.billweb.controller;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.enums.*;
import cn.welfarezhu.billservice.model.MessageInfo;
import cn.welfarezhu.billservice.model.UserInfo;
import cn.welfarezhu.billservice.model.WorkingUnit;
import cn.welfarezhu.billservice.service.MessageService;
import cn.welfarezhu.billservice.service.RecordService;
import cn.welfarezhu.billservice.service.UnitService;
import cn.welfarezhu.billservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserController {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private RecordService recordService;
    @Resource
    private UserService userService;
    @Resource
    private UnitService unitService;
    @Resource
    private MessageService messageService;

    @GetMapping("/user/login")
    public String loginPage(){
        return "user/user-login";
    }

    @GetMapping("/user/info/query")
    public String userInfoQuery(@RequestParam("userId") int userId, Model model,HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        UserDTO queryUser=userService.queryUserByUserId(userId);
        if (queryUser==null||user.getUnitId()!=queryUser.getUnitId()){
            model.addAttribute(QUERY_USER,null);
            return "user/user-info-query";
        }
        model.addAttribute(QUERY_USER,queryUser);
        return "user/user-info-query";
    }

    @PostMapping("/user/account/check")
    @ResponseBody
    public int checkAccount(@RequestParam("account") String account){
        if (userService.isAccountExists(account)){
            return FAIL;
        }
        return SUCCESS;
    }

    @PostMapping("/user/login/valid")
    @ResponseBody
    public int userLoginValid(@RequestParam("account") String account,@RequestParam("password") String password,HttpSession session){
        UserDTO user=userService.queryUserByAccount(account);
        if (user==null){
            return FAIL;
        }
        if (user.getStatusCode()==RoleStatusCode.BAN.statusCode()||user.getStatusCode()==RoleStatusCode.LOGOUT.statusCode()){
            return IS_BANNED;
        }
        if (!passwordEncoder.matches(password,userService.queryUserPassword(user.getUserId()))){
            return FAIL;
        }
        session.setAttribute(LOGIN_USER,user);
        session.setAttribute(IS_LOGIN,true);
        //System.out.println(session.getAttribute(LOGIN_USER));
        return SUCCESS;
    }

    @GetMapping("/user/user/list")
    public String userListBrowse(@RequestParam("pageNo") int pageNo,Model model,HttpSession session){
        if (pageNo==0){
            pageNo=1;
        }
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        int pageSize=(int)session.getAttribute(PAGE_SIZE);
        model.addAttribute(USER_LIST,userService.queryUserList(user.getUnitId(),pageNo,pageSize));
        return "user/user-list";
    }

    @PostMapping("/user/logout")
    @ResponseBody
    public int userLogout(HttpSession session){
        session.removeAttribute(LOGIN_USER);
        session.setAttribute(IS_LOGIN,false);
        return SUCCESS;
    }

    @PostMapping("/user/register/submit")
    @ResponseBody
    public int registerPage(UserInfo user, HttpSession session){
        if (user==null){
            return FAIL;
        }
        WorkingUnit unit=(WorkingUnit)session.getAttribute(REGISTER_UNIT);
        if (unit==null){
            return FAIL;
        }
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRoleCode(RoleCode.REGISTER.roleNum());
        user.setStatusCode(RoleStatusCode.NORMAL.statusCode());
        user.setCreateDate(new Date());
        if (!userService.addNewUser(user)){
            return FAIL;
        }
        unit.setUnitPrincipalId(user.getUserId());
        if (!unitService.addNewUnit(unit)){
            return FAIL;
        }
        user.setUnitId(unit.getUnitId());
        userService.modifyUser(user);
        recordService.addNewRecord(
                user.getUnitId(),
                user.getUserId(),
                OperationCode.ADD_NEW_ROLE.operationNum(),
                ObjectSortCode.USER.sortCode(),
                user.getUserId());
        UserDTO userDTO=userService.queryUserByUserId(user.getUserId());
        if (userDTO==null){
            return FAIL;
        }
        session.setAttribute(LOGIN_USER,userDTO);
        session.setAttribute(IS_LOGIN,true);
        return SUCCESS;
    }

    @PostMapping("/user/role/add")
    @ResponseBody
    public int userRoleAdd(UserInfo role,HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (user.getRoleNum()!=RoleCode.REGISTER.roleNum()){
            return NO_AUTH;
        }
        role.setUnitId(user.getUnitId());
        role.setRoleCode(RoleCode.OPERATOR.roleNum());
        role.setStatusCode(RoleStatusCode.NORMAL.statusCode());
        role.setGender(0);
        role.setAge(20);
        role.setAddress("中国");
        role.setCreateDate(new Date());
        String pass=passwordEncoder.encode(role.getPassword());
        role.setPassword(pass);
        if (!userService.addNewUser(role)){
            return FAIL;
        }
        recordService.addNewRecord(user.getUnitId(),
                user.getUserId(),
                OperationCode.ADD_NEW_ROLE.operationNum(),
                ObjectSortCode.USER.sortCode(),
                role.getUserId());
        return SUCCESS;
    }

    @PostMapping("/user/password/modify")
    @ResponseBody
    public int userPasswordModify(@RequestParam("oldPass") String oldPass,@RequestParam("newPass") String newPass,HttpSession session){
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        if (!passwordEncoder.matches(oldPass,userService.queryUserPassword(user.getUserId()))){
            return NO_AUTH;
        }
        if (!userService.modifyUserPassword(user.getUserId(),passwordEncoder.encode(newPass))){
            return FAIL;
        }
        return SUCCESS;
    }

    @GetMapping("/user/info/modify")
    public String userInfoModify(){
        return "user/user-info-modify";
    }

    @PostMapping("/user/status/ban")
    @ResponseBody
    public int userStatusBan(@RequestParam("userId") int userId,HttpSession session){
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        if (user.getRoleNum() != RoleCode.REGISTER.roleNum()){
            return NO_AUTH;
        }
        if (userId == user.getUserId()){
            return FAIL;
        }
        if (userService.queryStatusCode(userId)==RoleStatusCode.BAN.statusCode()){
            return FAIL;
        }
        if (!userService.modifyStatusCode(userId,RoleStatusCode.BAN.statusCode())){
            return FAIL;
        }
        return SUCCESS;
    }

    @PostMapping("/user/manager/msg/send")
    @ResponseBody
    public int userManagerMsgSend(@RequestParam("content") String content,HttpSession session){
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        MessageInfo msg=new MessageInfo();
        msg.setUserId(user.getUserId());
        msg.setReceptorId(0);
        msg.setStatusCode(MessageStatusCode.WAIT_READ.statusCode());
        msg.setMessageSort(MessageSortCode.SYSTEM_MSG.sortCode());
        msg.setContent(content);
        msg.setCreateDate(new Date());
        if (messageService.addNewMessage(msg)){
            return SUCCESS;
        }
        return FAIL;
    }
}
