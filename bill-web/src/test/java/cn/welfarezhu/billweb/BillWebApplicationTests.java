package cn.welfarezhu.billweb;

import cn.welfarezhu.billservice.dto.BillBookDTO;
import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.BillStatusCode;
import cn.welfarezhu.billservice.enums.RoleCode;
import cn.welfarezhu.billservice.enums.RoleStatusCode;
import cn.welfarezhu.billservice.mapper.BillBookMapper;
import cn.welfarezhu.billservice.mapper.BillKindMapper;
import cn.welfarezhu.billservice.mapper.UnitMapper;
import cn.welfarezhu.billservice.mapper.UserMapper;
import cn.welfarezhu.billservice.model.BillBook;
import cn.welfarezhu.billservice.model.BillKind;
import cn.welfarezhu.billservice.model.UserInfo;
import cn.welfarezhu.billservice.service.*;
import cn.welfarezhu.billweb.controller.BillController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class BillWebApplicationTests {

    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UnitMapper unitMapper;
    @Resource
    private SimpleDateFormat dateFormat;
    @Resource
    private UnitService unitService;
    @Resource
    private BillKindService kindService;
    @Resource
    private BillBookService bookService;
    @Resource
    private BillBookMapper bookMapper;
    @Resource
    private RecordService recordService;
    @Resource
    private BillController billController;

    @Test
    void contextLoads() throws ParseException {
        //System.out.println(unitMapper.queryUnitName(userMapper.selectByPrimaryKey(1000).getUnitId()))
        //UserDTO userDTO=userService.queryUserByUserId(1000);
        //System.out.println(dateFormat.format(userDTO.getCreateDate()));
        //System.out.println(unitMapper.selectByPrimaryKey(1));
        //System.out.println(kindMapper.selectAll());
        //System.out.println(unitService.queryUnitByUnitId(1));
        //System.out.println(userService.queryUserList(1));
        //System.out.println(bookMapper.queryBillBookByUnitId(1));
        //ResultDataMap<BillBookDTO> resultDataMap=bookService.queryBillBookByUnitId(1,1,10);
        //for (BillBookDTO bookDTO:resultDataMap.getResult()){
          //  System.out.println(bookDTO);
        //}
        //System.out.println(resultDataMap.getPageInfo());
        //String pass=userService.queryUserPassword(1001);
        //System.out.println(passwordEncoder.matches("123456",pass));
        //System.out.println(recordService.queryRecordByUnitId(1,1,10));
        //System.out.println(unitService.countUnitName("测试企业"));
        System.out.println(bookService.countVerifyMoneyByDate(1,"2020-02-15 00:00:00","2020-02-29 00:00:00"));
        System.out.println(bookService.queryVerifyBookByDate(1,"2020-02-15 00:00:00","2020-02-29 00:00:00"));
    }
}
