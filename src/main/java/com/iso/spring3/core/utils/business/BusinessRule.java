package com.iso.spring3.core.utils.business;

import com.iso.spring3.core.utils.result.ErrorResult;
import com.iso.spring3.core.utils.result.Result;

import java.util.ArrayList;
import java.util.List;

public class BusinessRule {
    private BusinessRule() {
    }

    public static Result run(Result... logics) {
        List<String> messages = new ArrayList<>();
        for (Result logic : logics) {
            if (!logic.isSuccess()) {
                messages.add(logic.getMessage());
            }
        }
        if (!messages.isEmpty()) {
            return new ErrorResult(messages.toString());
        }
        return null;
    }
}
