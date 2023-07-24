package com.chunlei.bili.common.exception;

import com.chunlei.bili.common.api.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public R handle(RuntimeException e){
        if(e.getMessage() != null){
            return R.failed(e.getMessage());
        }
        return R.failed(e.getMessage());
    }

}
