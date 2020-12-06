package config;

import exception.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import response.ExceptionResponse;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public final ExceptionResponse exceptionHandler(BaseException exception, WebRequest request, HttpServletResponse response) {
        response.setStatus(exception.getHttpStatus().value());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(false);
        exceptionResponse.setPath(request.getContextPath());
        exceptionResponse.setMessage(exception.getMessage());
        exceptionResponse.setError(exception.getHttpStatus().getReasonPhrase());
        return exceptionResponse;
    }
}
