package com.group4.FKitShop.Exception;

import com.group4.FKitShop.Entity.ResponseObject;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.Console;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalHandlingException {

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ResponseObject> handlingAppException(AppException exception) {
        ErrorCode code = exception.getCode();
        System.out.println(exception);
        return ResponseEntity.badRequest().body(
                new ResponseObject(code.getCode(), code.getMessage(), "")
        );
    }

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ResponseObject> handlingRuntimeException(RuntimeException exception) {
//        return ResponseEntity.badRequest().body(
//                new ResponseObject(ErrorCode.UNCATEGORIZE.getCode(), ErrorCode.UNCATEGORIZE.getMessage(), "")
//        );
//    }

//     xử lí lỗi sai constraint
    @ExceptionHandler(value = ConstraintViolationException.class)
    ResponseEntity<ResponseObject> handlingConstraintedException(ConstraintViolationException exception) {
        String message = exception.getMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        return ResponseEntity.badRequest().body(
                new ResponseObject(errorCode.getCode(), message, "")
        );
    }

}
