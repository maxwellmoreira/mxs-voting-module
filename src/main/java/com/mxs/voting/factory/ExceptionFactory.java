package com.mxs.voting.factory;

import com.mxs.voting.dto.ExceptionDto;
import com.mxs.voting.exception.InternalErrorException;
import com.mxs.voting.exception.NotFoundException;
import com.mxs.voting.type.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionFactory {
    @ExceptionHandler(value = {InternalErrorException.class})
    public ResponseEntity<ExceptionDto> resourceInternalErrorException(InternalErrorException internalErrorException, WebRequest webRequest) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setType(ExceptionType.INFRASTRUCTURE);
        exceptionDto.setMessage(internalErrorException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionDto> resourceNotFoundException(NotFoundException notFoundException, WebRequest webRequest) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setType(ExceptionType.BUSINESS);
        exceptionDto.setMessage(notFoundException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }
}
