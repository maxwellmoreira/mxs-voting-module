package com.mxs.voting.factory;

import com.mxs.voting.dto.ExceptionDto;
import com.mxs.voting.exception.InternalErrorException;
import com.mxs.voting.exception.NotFoundException;
import com.mxs.voting.type.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionFactory {
    @ExceptionHandler(value = {InternalErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> resourceInternalErrorException(InternalErrorException internalErrorException, WebRequest webRequest) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setType(ExceptionType.INFRASTRUCTURE);
        exceptionDto.setMessage(internalErrorException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDto> resourceNotFoundException(NotFoundException notFoundException, WebRequest webRequest) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setType(ExceptionType.BUSINESS);
        exceptionDto.setMessage(notFoundException.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }
}
