package cn.welfarezhu.billservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class NullResultException extends Exception{

    @Getter
    @Setter
    private String message;
}
