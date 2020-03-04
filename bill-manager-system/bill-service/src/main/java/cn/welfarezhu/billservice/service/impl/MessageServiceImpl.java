package cn.welfarezhu.billservice.service.impl;

import cn.welfarezhu.billservice.dto.MessageDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.enums.MessageSortCode;
import cn.welfarezhu.billservice.enums.MessageStatusCode;
import cn.welfarezhu.billservice.exceptions.SQLIResultErrorException;
import cn.welfarezhu.billservice.mapper.MessageMapper;
import cn.welfarezhu.billservice.mapper.UserMapper;
import cn.welfarezhu.billservice.model.MessageInfo;
import cn.welfarezhu.billservice.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class MessageServiceImpl implements MessageService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private SimpleDateFormat dateFormat;

    @Override
    public boolean addNewMessage(MessageInfo messageInfo) {
        try {
            int count=messageMapper.insert(messageInfo);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 添加消息失败 | user id :"+messageInfo.getUserId());
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public ResultDataMap<MessageDTO> queryMessageByReceptorIdAndStatusCode(int receptorId, int statusCode, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<MessageInfo> messageInfoList=messageMapper.queryMessage(receptorId,statusCode);
        PageInfo pageInfo=new PageInfo(messageInfoList);
        if (messageInfoList.size()==0){
            return null;
        }
        List<MessageDTO> messageDTOList=new ArrayList<>();
        for (MessageInfo messageInfo:messageInfoList){
            messageDTOList.add(getMessageDTO(messageInfo));
        }
        ResultDataMap<MessageDTO> resultDataMap=new ResultDataMap<>();
        resultDataMap.setResult(messageDTOList);
        resultDataMap.setPageInfo(pageInfo);
        return resultDataMap;
    }

    @Override
    public boolean modifyStatusCode(int messageId, int statusCode) {
        try {
            int count=messageMapper.updateStatusCode(messageId,statusCode);
            if (count != 1){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改消息状态失败 | message id : "+messageId);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyStatusCodeByReceptorId(int receptorId, int oldStatusCode, int newStatusCode) {
        List<Integer> idList=messageMapper.queryMessageIdByReceptorIdAndStatusCode(receptorId,oldStatusCode);
        if (idList.size()==0){
            return true;
        }
        try {
            int count=messageMapper.updateStatusCodebyMessageIdList(idList,newStatusCode);
            if (count != idList.size()){
                throw new SQLIResultErrorException(dateFormat.format(new Date())+" | 修改消息状态失败 | message id list : "+idList);
            }
        } catch (SQLIResultErrorException e) {
            System.out.println(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    private String getStatus(int statusCode){
        String value;
        switch (statusCode){
            case 1:
                value= MessageStatusCode.READED.status();
                break;
            default:
                value=MessageStatusCode.WAIT_READ.status();
        }
        return value;
    }

    private String getSort(int sortCode){
        String value;
        switch (sortCode){
            case 1:
                value= MessageSortCode.SYSTEM_MSG.sort();
                break;
            default:
                value=MessageSortCode.USER_MSG.sort();
        }
        return value;
    }

    private MessageDTO getMessageDTO(MessageInfo messageInfo){
        MessageDTO messageDTO=new MessageDTO();
        messageDTO.setMessageId(messageInfo.getMessageId());
        messageDTO.setUserId(messageInfo.getUserId());
        messageDTO.setUser(userMapper.queryUserName(messageInfo.getUserId()));
        messageDTO.setReceptorId(messageInfo.getReceptorId());
        messageDTO.setReceptor(userMapper.queryUserName(messageInfo.getReceptorId()));
        messageDTO.setContent(messageInfo.getContent());
        messageDTO.setSort(getSort(messageInfo.getMessageSort()));
        messageDTO.setStatus(getStatus(messageInfo.getStatusCode()));
        messageDTO.setTargetId(messageInfo.getTargetId()==null?0:messageInfo.getTargetId());
        messageDTO.setCreateDate(dateFormat.format(messageInfo.getCreateDate()));
        return messageDTO;
    }
}
