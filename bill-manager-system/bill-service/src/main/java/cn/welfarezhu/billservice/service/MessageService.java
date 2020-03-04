package cn.welfarezhu.billservice.service;

import cn.welfarezhu.billservice.dto.MessageDTO;
import cn.welfarezhu.billservice.dto.vo.ResultDataMap;
import cn.welfarezhu.billservice.model.MessageInfo;

import java.util.List;

public interface MessageService {

    //添加新的Message
    boolean addNewMessage(MessageInfo messageInfo);

    //根据receptorId和statusCode查询Message
    ResultDataMap<MessageDTO> queryMessageByReceptorIdAndStatusCode(int receptorId, int statusCode, int pageNo, int pageSize);

    //根据messageId修改statusCode
    boolean modifyStatusCode(int messageId,int statusCode);

    //根据receptorId和statusCode批量修改statusCode
    boolean modifyStatusCodeByReceptorId(int receptorId,int oldStatusCode,int newStatusCode);
}
