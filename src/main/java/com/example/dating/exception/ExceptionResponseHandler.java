package com.example.dating.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionResponseHandler {

    // 예외 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> customRuntimeException(RuntimeException e, HttpServletResponse response) {
        response.setStatus(response.SC_BAD_REQUEST);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", e.getMessage());

        return body;
    }

    // valid 불충
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> validationException(MethodArgumentNotValidException e, HttpServletResponse response) {

        BindingResult bindingResult = e.getBindingResult();

        final Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("errorMessage", "Valid 불충 오류 발생");

        for (FieldError error : bindingResult.getFieldErrors()) {
            errorResponse.put(error.getField(), error.getDefaultMessage());
        }
        return errorResponse;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception e) {
        return "An unexpected error occurred!";
    }


    // 웹소켓 예외 처리
    public static class CustomWebSocketHandler extends TextWebSocketHandler {
        private static final Logger logger = LoggerFactory.getLogger(CustomWebSocketHandler.class);

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            logger.error("WebSocket transport error:", exception);
            super.handleTransportError(session, exception);
        }
    }

}