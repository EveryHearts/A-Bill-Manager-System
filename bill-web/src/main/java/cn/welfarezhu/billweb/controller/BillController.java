package cn.welfarezhu.billweb.controller;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import cn.welfarezhu.billservice.dto.BillBookDTO;
import cn.welfarezhu.billservice.dto.BillDTO;
import cn.welfarezhu.billservice.dto.BillKindDTO;
import cn.welfarezhu.billservice.dto.UserDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.*;
import cn.welfarezhu.billservice.model.BillBook;
import cn.welfarezhu.billservice.model.BillInfo;
import cn.welfarezhu.billservice.model.BillKind;
import cn.welfarezhu.billservice.service.BillBookService;
import cn.welfarezhu.billservice.service.BillKindService;
import cn.welfarezhu.billservice.service.BillService;
import cn.welfarezhu.billservice.service.RecordService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Slf4j
public class BillController {

    @Resource
    private BillService billService;
    @Resource
    private BillBookService bookService;
    @Resource
    private BillKindService kindService;
    @Resource
    private RecordService recordService;

    //查询kind
    @PostMapping("/bill/kind/query")
    @ResponseBody
    public List<BillKindDTO> getBillKind(HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (user==null){
            return null;
        }
        ResultDataMap<BillKindDTO> map=kindService.queryBillKindByUnitId(user.getUnitId(),1,10);
        if (map==null){
            return null;
        }
        return map.getResult();
    }

    //添加kind
    @PostMapping("/bill/kind/add")
    @ResponseBody
    public int BillKindAdd(@RequestParam("kindName") String kindName,HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (user==null){
            return 0;
        }
        if (kindService.isKindNameExists(user.getUnitId(),kindName)){
            return -1;
        }
        BillKind kind=new BillKind();
        kind.setUnitId(user.getUnitId());
        kind.setStatusCode(BillKindStatusCode.NORMAL.statusCode());
        kind.setKindName(kindName);
        kind.setCreateDate(new Date());
        if (!kindService.addNewBillKind(kind)){
            return 0;
        }
        recordService.addNewRecord(
                user.getUnitId(),
                user.getUserId(),
                OperationCode.ADD_NEW_KIND.operationNum(),
                ObjectSortCode.BILL_KIND.sortCode(),
                kind.getBillKindId());
        return kind.getBillKindId();
    }

    //跳转book浏览
    @GetMapping("/bill/book/browse")
    public String billBookBrowse(){
        return "bill/bill-book-browse";
    }

    @GetMapping("/bill/bill/browse")
    public String billBookQuery(@RequestParam("bookId") int bookId, Model model,HttpSession session){
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        BillBookDTO book=bookService.queryBookByBookId(bookId);
        if (book==null||book.getUnitId()!=user.getUnitId()){
            model.addAttribute(QUERY_BOOK,null);
        }else{
            model.addAttribute(QUERY_BOOK,book);
        }
        return "bill/bill-bill-browse";
    }

    @PostMapping("/bill/book/verify/confirm")
    @ResponseBody
    public int billBookVerifyConfirm(@RequestParam("bookId") int bookId,HttpSession session){
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        BillBookDTO book = bookService.queryBookByBookId(bookId);
        if (book.getUnitId()!=user.getUnitId()){
            return NO_AUTH;
        }
        if (book.getOperatorId()!=user.getUserId()){
            if (user.getRoleNum()!=RoleCode.REGISTER.roleNum()){
                return NO_AUTH;
            }else if (book.getStatusCode()!=BillStatusCode.WAIT_VERIFY.statusCode()){
                return NO_AUTH;
            }
        }else if (book.getStatusCode()!=BillStatusCode.USED.statusCode()){
            return NO_AUTH;
        }
        if (book.getTotalMoney()!=book.getVerifyMoney()){
            return FAIL;
        }
        return SUCCESS;
    }

    @PostMapping("/bill/book/verify/sum")
    @ResponseBody
    public int billBookVerifySum(@RequestParam("beginDate") String beginDate,@RequestParam("endDate") String endDate,HttpSession session) throws ParseException {
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        return bookService.countVerifyMoneyByDate(user.getUnitId(),beginDate+" 00:00:00",endDate+" 00:00:00");
    }

    @PostMapping("/bill/book/verify/query")
    @ResponseBody
    public List<BillBookDTO> billBookVerifyQuery(@RequestParam("beginDate") String beginDate,@RequestParam("endDate") String endDate,HttpSession session) throws ParseException{
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        return bookService.queryVerifyBookByDate(user.getUnitId(),beginDate+" 00:00:00",endDate+" 00:00:00");
    }

    @GetMapping("/bill/book/verify/browse")
    public String billBookVerifyBrowse(){
        return "bill/bill-book-verify-browse";
    }

    @PostMapping("/bill/book/status/receive")
    @ResponseBody
    public int billBookStatusReceive(@RequestParam("bookId") int bookId,HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (bookService.queryUnitId(bookId)!=user.getUnitId()){
            return NO_AUTH;
        }
        if (bookService.queryStatusCode(bookId)!=BillStatusCode.PROVIDE.statusCode()){
            return NO_AUTH;
        }
        if (user.getRoleNum()==RoleCode.REGISTER.roleNum()){
            return FAIL;
        }
        billService.setOperatorIdByBookId(bookId,user.getUserId());
        bookService.setOperatorId(bookId,user.getUserId());
        billService.modifyStatusCodeByBookId(bookId,BillStatusCode.PROVIDE.statusCode(),BillStatusCode.RECEIVE.statusCode());
        bookService.modifyStatusCode(bookId,BillStatusCode.RECEIVE.statusCode());
        recordService.addNewRecord(user.getUnitId(),
                user.getUserId(),
                OperationCode.RECEIVE.operationNum(),
                ObjectSortCode.BILL_BOOK.sortCode(),
                bookId);
        return SUCCESS;
    }

    @PostMapping("/bill/book/status/ban")
    @ResponseBody
    public int billBookStatusBan(@RequestParam("bookId") int bookId,HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (bookService.queryUnitId(bookId)!=user.getUnitId()){
            return NO_AUTH;
        }
        if (user.getRoleNum()!=RoleCode.REGISTER.roleNum()){
            return NO_AUTH;
        }
        if (bookService.queryStatusCode(bookId)!=BillStatusCode.PROVIDE.statusCode()){
            return NO_AUTH;
        }
        if (billService.modifyStatusCodeByBookId(bookId,BillStatusCode.PROVIDE.statusCode(),BillStatusCode.BANNED.statusCode())){
            bookService.modifyStatusCode(bookId,BillStatusCode.BANNED.statusCode());
            recordService.addNewRecord(user.getUnitId(),
                    user.getUserId(),
                    OperationCode.BANNED_BILL.operationNum(),
                    ObjectSortCode.BILL_BOOK.sortCode(),
                    bookId);
            return SUCCESS;
        }
        return FAIL;
    }

    @PostMapping("/bill/book/status/verify")
    @ResponseBody
    public int billBookStatusVerift(@RequestParam("bookId") int bookId,HttpSession session){
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (user.getRoleNum()==RoleCode.REGISTER.roleNum()){
            if (bookService.queryStatusCode(bookId)==BillStatusCode.USED.statusCode()){
                if (billService.modifyStatusCodeByBookId(bookId,BillStatusCode.USED.statusCode(),BillStatusCode.CANCEL.statusCode())){
                    if (billService.modifyStatusCodeByBookId(bookId, BillStatusCode.PROVIDE.statusCode(), BillStatusCode.BANNED.statusCode())) {
                        bookService.modifyStatusCode(bookId,BillStatusCode.CANCEL.statusCode());
                        recordService.addNewRecord(user.getUnitId(),
                                user.getUserId(),
                                OperationCode.CANCELED.operationNum(),
                                ObjectSortCode.BILL_BOOK.sortCode(),
                                bookId);
                        return SUCCESS;
                    }
                }
            }else if (bookService.queryStatusCode(bookId)==BillStatusCode.WAIT_VERIFY.statusCode()){
                if (billService.modifyStatusCodeByBookId(bookId,BillStatusCode.WAIT_VERIFY.statusCode(),BillStatusCode.CANCEL.statusCode())){
                    if (billService.modifyStatusCodeByBookId(bookId,BillStatusCode.BANNING.statusCode(),BillStatusCode.BANNED.statusCode())){
                        bookService.modifyStatusCode(bookId,BillStatusCode.CANCEL.statusCode());
                        recordService.addNewRecord(user.getUnitId(),
                                user.getUserId(),
                                OperationCode.CANCELED.operationNum(),
                                ObjectSortCode.BILL_BOOK.sortCode(),
                                bookId);
                        return SUCCESS;
                    }
                }
            }
        }else if (user.getRoleNum()==RoleCode.OPERATOR.roleNum()){
            if (billService.modifyStatusCodeByBookId(bookId,BillStatusCode.USED.statusCode(),BillStatusCode.WAIT_VERIFY.statusCode())){
                if (billService.modifyStatusCodeByBookId(bookId,BillStatusCode.RECEIVE.statusCode(),BillStatusCode.BANNING.statusCode())){
                    bookService.modifyStatusCode(bookId,BillStatusCode.WAIT_VERIFY.statusCode());
                    recordService.addNewRecord(user.getUnitId(),
                            user.getUserId(),
                            OperationCode.CANCELED.operationNum(),
                            ObjectSortCode.BILL_BOOK.sortCode(),
                            bookId);
                    return SUCCESS;
                }
            }
        }
        return FAIL;
    }

    @PostMapping("/bill/bill/list/query")
    @ResponseBody
    public ResultDataMap<BillDTO> billListQuery(
            @RequestParam("bookId") int bookId,
            @RequestParam(value = "pageNo",required = false) int pageNo,
            HttpSession session){
        if (pageNo==0){
            pageNo=1;
        }
        int pageSize=(int)session.getAttribute(PAGE_SIZE);
        return billService.queryBillByBookId(bookId,pageNo,pageSize);
    }

    @PostMapping("/bill/bill/status/use")
    @ResponseBody
    public int billUse(@RequestBody List<Integer> billIdList,HttpSession session){
        if (billIdList.size()==0){
            return SUCCESS;
        }
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        for (int billId:billIdList){
            int bookId=billService.queryBookIdByBillId(billId);
            int bookStatusCode=bookService.queryStatusCode(bookId);
            int bookUnitId=bookService.queryUnitId(bookId);
            if (bookStatusCode!=BillStatusCode.PROVIDE.statusCode()&&bookStatusCode!=BillStatusCode.RECEIVE.statusCode()&&bookStatusCode!=BillStatusCode.USED.statusCode()){
                System.out.println("book Status is wrong");
                continue;
            }
            if (bookUnitId!=user.getUnitId()){
                System.out.println("book unit is wrong");
                continue;
            }
            if (user.getRoleNum()== RoleCode.OPERATOR.roleNum()&&bookStatusCode==BillStatusCode.PROVIDE.statusCode()){
                System.out.println("user role is wrong");
                continue;
            }
            int billStatusCode=billService.queryStatusCode(billId);
            if (billStatusCode==BillStatusCode.USED.statusCode()){
                continue;
            }
            if (bookStatusCode!=BillStatusCode.USED.statusCode()){
                bookService.setOperatorId(bookId,user.getUserId());
                bookService.modifyStatusCode(bookId,BillStatusCode.USED.statusCode());
                recordService.addNewRecord(user.getUnitId(),
                        user.getUserId(),
                        OperationCode.USED.operationNum(),
                        ObjectSortCode.BILL_BOOK.sortCode(),
                        bookId);
            }
            int bookVerifyMoney=bookService.queryVerifyMoney(bookId);
            int billMoney=billService.queryBillMoney(billId);
            bookService.modifyVerifyMoney(bookId,bookVerifyMoney+billMoney);
            billService.setOperatorId(billId,user.getUserId());
            System.out.println(billService.modifyStatusCode(billId,BillStatusCode.USED.statusCode()));
            recordService.addNewRecord(user.getUnitId(),
                    user.getUserId(),
                    OperationCode.USED.operationNum(),
                    ObjectSortCode.BILL.sortCode(),
                    billId);
        }
        return SUCCESS;
    }

    @PostMapping("/bill/bill/list/query/status")
    @ResponseBody
    public ResultDataMap<BillDTO> billListQueryStatus(
            @RequestParam("bookId") int bookId,
            @RequestParam(value = "pageNo",required = false) int pageNo,
            @RequestParam(value = "statusCode" , required = false) int statusCode,
            HttpSession session){
        if (pageNo==0){
            pageNo=1;
        }
        int pageSize=(int)session.getAttribute(PAGE_SIZE);
        return billService.queryBillByBookIdAndStatusCode(bookId,statusCode,pageNo,pageSize);
    }

    @PostMapping("/bill/book/code/query")
    @ResponseBody
    public BillBookDTO billBookCodeQuery(@RequestParam("billCode") String billCode,HttpSession session){
        if (billCode==null){
            return null;
        }
        BillBookDTO book=bookService.queryBookByBillCode(billCode);
        if (book==null){
            return null;
        }
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        if (book.getUnitId()!=user.getUnitId()){
            return null;
        }
        return book;
    }

    //查询book List : pageNo
    @PostMapping("/bill/book/list/query")
    @ResponseBody
    public ResultDataMap<BillBookDTO> billBookListQuery(
            @RequestParam(value = "pageNo",required = false) int pageNo
            ,@RequestParam(value = "statusCode",required = false) int statusCode
            ,HttpSession session){
        if (pageNo == 0){
            pageNo=1;
        }
        int pageSize=(int)session.getAttribute(PAGE_SIZE);
        //System.out.println(pageSize);
        UserDTO user=(UserDTO) session.getAttribute(LOGIN_USER);
        if (statusCode==10){
            return bookService.queryBillBookByUnitId(user.getUnitId(),pageNo,pageSize);
        }
        return bookService.queryBillBookByUnitAndStatus(user.getUnitId(),statusCode,pageNo,pageSize);
    }

    //跳转book添加页面
    @GetMapping("/bill/book/add")
    public String billBookAdd(){
        return "bill/bill-book-add";
    }

    //检查重复code
    @PostMapping("/bill/code/isExists")
    @ResponseBody
    public int billCodeIsExists(@RequestParam("billCode") String code){
        if (code==null){
            return FAIL;
        }
        if (billService.isExistsBillCode(code)){
            return FAIL;
        }
        return SUCCESS;
    }

    //bill及book新建
    @PostMapping("/bill/book/insert")
    @ResponseBody
    public int BillBookInsert(@RequestBody List<BillBook> bookList, HttpSession session){
        if (bookList.size()<1){
            return FAIL;
        }
        UserDTO user=(UserDTO)session.getAttribute(LOGIN_USER);
        for (BillBook book:bookList){
            book.setUnitId(user.getUnitId());
            book.setStatusCode(BillStatusCode.PROVIDE.statusCode());
            book.setCreateDate(new Date());
            book.setVerifyMoney(0);
            if (!bookService.addNewBillBook(book)){
                continue;
            }
            recordService.addNewRecord(
                    user.getUnitId(),
                    user.getUserId(),
                    OperationCode.ADD_NEW_BILL_BOOK.operationNum(),
                    ObjectSortCode.BILL_BOOK.sortCode(),
                    book.getBillBookId()
            );
            if (!billInsert(book)){
                return FAIL;
            }
        }
        return SUCCESS;
    }

    private boolean billInsert(BillBook book){
        String beginCode=book.getBeginCode();
        String endCode=book.getEndCode();
        String beginNum;
        String endNum;
        int begin;
        int end;
        int moneyEvg=0;
        String prefix=null;
        Pattern prefixReg=Pattern.compile("\\D*");
        Pattern zeroReg=Pattern.compile("^[0]+");
        Matcher prefixMcr=prefixReg.matcher(beginCode);
        if (prefixMcr.find()){
            prefix=prefixMcr.group(0);
        }
        if (prefix!=null){
            beginNum=beginCode.substring(
                    beginCode.indexOf(prefix)+prefix.length()
            );
            endNum=endCode.substring(
                    endCode.indexOf(prefix)+prefix.length()
            );
            Matcher zeroMcr=zeroReg.matcher(endNum);
            String numPrefix="";
            if (zeroMcr.find()){
                numPrefix=zeroMcr.group(0);
            }
            begin=Integer.valueOf(beginNum);
            end=Integer.valueOf(endNum);
            if (book.getBillCopies()!=(end-begin+1)){
                return false;
            }
            if (book.getBillCopies()!=0){
                moneyEvg=book.getTotalMoney()/book.getBillCopies();
            }
            for (int i=begin;i<=end;i++){
                String bcode=concatCode(prefix,numPrefix,i,getNumLength(end)-getNumLength(i));
                billService.addNewBill(getBill(book,bcode,moneyEvg));
            }
        }else{
            beginNum=beginCode;
            endNum=endCode;
            begin=Integer.valueOf(beginNum);
            end=Integer.valueOf(endNum);
            if (book.getBillCopies()!=(end-begin+1)){
                return false;
            }
            if (book.getBillCopies()!=0){
                moneyEvg=book.getTotalMoney()/book.getBillCopies();
            }
            for (int i=begin;i<=end;i++){
                billService.addNewBill(getBill(book,Integer.toString(i),moneyEvg));
            }
        }
        return true;
    }

    private String concatCode(String prefix,String numPrefix,int index,int differ){
        StringBuffer codePrefix=new StringBuffer(prefix+numPrefix);
        if (differ==0){
            return codePrefix.append(index).toString();
        }
        for (int i=0;i<differ;i++){
            codePrefix.append(0);
        }
        return codePrefix.append(index).toString();
    }

    private int getNumLength(int num){
        int count=0;
        while (num>=1){
            num/=10;
            count++;
        }
        return count;
    }

    private BillInfo getBill(BillBook book,String code,int moneyEvg){
        BillInfo bill=new BillInfo();
        bill.setBillKindId(book.getBillKindId());
        bill.setBillBookId(book.getBillBookId());
        bill.setBillMoney(moneyEvg);
        bill.setBillCode(code);
        bill.setStatusCode(BillStatusCode.PROVIDE.statusCode());
        bill.setCreateDate(new Date());
        return bill;
    }
}
