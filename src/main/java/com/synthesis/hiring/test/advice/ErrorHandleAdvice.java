package com.synthesis.hiring.test.advice;

import com.synthesis.hiring.test.model.ContactInfoResponse;

import com.synthesis.hiring.test.model.Violation;
import javax.validation.ValidationException;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ErrorHandleAdvice {

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageConversionException.class)
  ContactInfoResponse onHttpMessageConversionException(HttpMessageConversionException e) {
    return new ContactInfoResponse("unable to convert message to valid pipelineCreateRequest",400);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(InvalidConfigurationPropertyValueException.class)
  public ContactInfoResponse resourceNotFoundException(InvalidConfigurationPropertyValueException ex, WebRequest request) {
    return new ContactInfoResponse(ex.getMessage(), 404);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  ContactInfoResponse onHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return new ContactInfoResponse(e.getMessage(), 400);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  ContactInfoResponse onValidationException(ValidationException e) {
    return new ContactInfoResponse("unable to validate data posted to PipelineCreateRequest Object Please check request " + e.getLocalizedMessage(), 400);
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ContactInfoResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    ContactInfoResponse response = new ContactInfoResponse("validations failed for attributes",400);
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      response.getViolations().add(
          new Violation( fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()==null?"null":fieldError.getRejectedValue().toString()));
    }
    return response;
  }
}
