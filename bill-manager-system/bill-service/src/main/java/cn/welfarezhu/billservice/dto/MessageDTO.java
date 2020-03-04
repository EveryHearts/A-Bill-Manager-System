package cn.welfarezhu.billservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageDTO {

    private int messageId;
    private int userId;
    private String user;
    private int receptorId;
    private String receptor;
    private String content;
    private String sort;
    private int targetId;
    private String status;
    private String createDate;
}
