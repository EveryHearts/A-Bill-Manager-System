package cn.welfarezhu.billservice.mapper;

import cn.welfarezhu.billservice.model.MessageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MessageMapper extends Mapper<MessageInfo> {

    //根据receptorId和statusCode查询Message
    @Select(value = "select * from message_info where receptor_id = #{receptorId} and status_code = #{statusCode} order by create_date desc")
    List<MessageInfo> queryMessage(@Param("receptorId") int receptorId,@Param("statusCode") int statusCode);

    //根据messageId修改statusCode
    @Select(value = "update message_info set modify_date = now() , status_code = #{statusCode} where message_id = #{messageId}")
    int updateStatusCode(@Param("messageId") int messageId,@Param("statusCode") int statusCode);

    //根据receptorId和statusCode查询messageId
    @Select(value = "select message_id from message_info where receptor_id = #{receptorId} and status_code = #{statusCode}")
    List<Integer> queryMessageIdByReceptorIdAndStatusCode(@Param("receptorId") int receptorId,@Param("statusCode") int statusCode);

    //根据messageId List批量修改statusCode
    @Update(value = "<script>" +
            "update message_info set modify_date = now() , status_code = #{statusCode} where message_id in" +
            "<foreach item='msgId' index='index' collection='idList' open='(' separator=',' close=')'>" +
            "#{msgId}" +
            "</foreach>" +
            "</script>")
    int updateStatusCodebyMessageIdList(@Param("idList") List<Integer> idList,@Param("statusCode") int statusCode);
}
