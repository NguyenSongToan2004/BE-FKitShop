package com.group4.FKitShop.Exception;


import com.group4.FKitShop.Entity.ResponseObject;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlingException {

    //Runtime exception
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ResponseObject> HandlingRuntimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(ResponseObject.builder()
                .status(1000)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ResponseObject> HandlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.badRequest().body(ResponseObject.builder()
                .status(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    //validation
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ResponseObject> HandlingValidation(MethodArgumentNotValidException exception) {
//        String enumKey =exception.getFieldError().getDefaultMessage();
//        ErrorCode errorCode =ErrorCode.valueOf(enumKey);
//        return ResponseEntity.badRequest().body(ResponseObject.builder()
//                .status(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .build());
//    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ResponseObject> HandlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        ResponseObject response = new ResponseObject();
        response.setStatus(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }


}
