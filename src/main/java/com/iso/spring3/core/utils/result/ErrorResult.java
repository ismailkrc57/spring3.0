package com.iso.spring3.core.utils.result;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ErrorResult extends Result {

    public ErrorResult(String message) {
        super(message, false);
    }

    public ErrorResult() {
        super(false);
    }

    public static ErrorResult of(String message) {
        return new ErrorResult(message);
    }

    public static ErrorResult of() {
        return new ErrorResult();
    }
}
