package com.mxs.voting.dto;

import com.mxs.voting.type.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

import static com.mxs.voting.constant.DateFormatConstant.FORMAT_FOR_EXCEPTION;

@Data
@Builder
@AllArgsConstructor
public class ExceptionDto {
    private ExceptionType type;
    private String message;
    private String timestamp;

    public ExceptionDto() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.timestamp = FORMAT_FOR_EXCEPTION.format(now);
    }
}
