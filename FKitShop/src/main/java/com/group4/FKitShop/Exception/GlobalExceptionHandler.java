package com.group4.FKitShop.Exception;


import com.group4.FKitShop.Entity.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
//        ApiResponse apiResponse= new ApiResponse();
//        apiResponse.setCode(9999);
//        apiResponse.setMessage(exception.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ResponseObject> handlingRuntimeException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(errorCode.getCode());
        responseObject.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(responseObject);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ResponseObject> HandlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        return ResponseEntity.badRequest().body(ResponseObject.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}
