package cn.welfarezhu.billservice.exceptions;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class SQLIResultErrorException extends Exception {

    @Getter
    @Setter
    private String message;
}
