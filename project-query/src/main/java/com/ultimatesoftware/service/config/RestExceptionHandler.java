package com.ultimatesoftware.service.config;

import static java.util.stream.Collectors.mapping;

import com.ultimatesoftware.launch.datatype.ExceptionHelper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import org.axonframework.commandhandling.interceptors.JSR303ViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles all exceptions for the REST controllers.
 */
@ControllerAdvice
public class RestExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  /**
   * Catches and translates {@link MethodArgumentNotValidException} exceptions into {@link ValidationErrorMessage}.
   * @param ex The exception to translate.
   * @return The HTTP response with the translated error message.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorMessage> processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    Map<String, List<String>> errors = result.getAllErrors().stream()
        .collect(Collectors.groupingBy(ObjectError::getCode, mapping(ObjectError::getDefaultMessage, Collectors.toList())));

    ValidationErrorMessage msg = new ValidationErrorMessage(this.messageSource, errors);
    return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
  }

  /**
   * Catches and translates {@link IllegalArgumentException} exceptions into {@link ValidationErrorMessage}.
   * @param ex The exception to translate.
   * @return The HTTP response with the translated error message.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ValidationErrorMessage> processAssertionExceptions(Throwable ex) {
    String message = ExceptionHelper.getExceptionMessage(ex);
    ValidationErrorMessage errorMessage = new ValidationErrorMessage(this.messageSource, Collections.singletonList(message));

    HttpStatus status = HttpStatus.BAD_REQUEST;

    if (message.contains(".notfound")) {
      status = HttpStatus.NOT_FOUND;
    }

    return new ResponseEntity<>(errorMessage, status);
  }

  /**
   * Catches and translates {@link ExecutionException} exceptions into {@link ValidationErrorMessage}.
   * @param ex The exception to translate.
   * @return The HTTP response with the translated error message.
   */
  @ExceptionHandler(ExecutionException.class)
  public ResponseEntity<ValidationErrorMessage> processAxonExceptions(ExecutionException ex) {
    String messageCode = ExceptionHelper.getExceptionMessage(ex);
    Throwable cause = ex.getCause();
    if (cause instanceof IllegalArgumentException) {
      return processAssertionExceptions(cause);
    }

    if (cause instanceof JSR303ViolationException) {
      JSR303ViolationException violation = (JSR303ViolationException) cause;
      Optional<ConstraintViolation<Object>> optional = violation.getViolations().stream().findFirst();
      messageCode = optional.isPresent() ? optional.get().getMessageTemplate() : "";
    }

    ValidationErrorMessage errorMessage = new ValidationErrorMessage(this.messageSource, Collections.singletonList(messageCode));
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }
}
