package com.gillab.exception;

import lombok.Getter;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ApplicationErrors {

    @Getter
    public enum ErrorCodesEnum {
        INVALID_TOOL_CODE("ERR-001"),
        INVALID_DISCOUNT("ERR-002"),
        INVALID_RENTAL_DAYS("ERR-003");

        private final String displayName;

        ErrorCodesEnum(String displayName) {
            this.displayName = displayName;
        }

    }

    protected static final Map<ErrorCodesEnum, String> ERROR_SUMMARIES = new HashMap<>();
    protected static final Map<ErrorCodesEnum, String> ERROR_MESSAGES = new HashMap<>();

    static {
        ERROR_SUMMARIES.put(ErrorCodesEnum.INVALID_TOOL_CODE, "Invalid tool code.");
        ERROR_SUMMARIES.put(ErrorCodesEnum.INVALID_DISCOUNT, "Invalid discount percentage.");
        ERROR_SUMMARIES.put(ErrorCodesEnum.INVALID_RENTAL_DAYS, "Invalid rental days.");

        ERROR_MESSAGES.put(ErrorCodesEnum.INVALID_TOOL_CODE, "The tool code {0} does not exist in the stock.");
        ERROR_MESSAGES.put(ErrorCodesEnum.INVALID_DISCOUNT, "The discount percentage {0} is not valid. Discount should be between {1} and {2}.");
        ERROR_MESSAGES.put(ErrorCodesEnum.INVALID_RENTAL_DAYS, "The number of rental days, {0}, is not valid. Rental days should be between {1} and {2}.");
    }

    private static final String NOT_ERROR_SUMMARY_CODE_DEFINED = "No error summary has been defined for this error code. Please contact Support team.";
    private static final String NOT_ERROR_MESSAGE_DEFINED = "No error message has been defined for this error code. Please contact Support team.";

    public static String getErrorSummary(ErrorCodesEnum errorCode) {
        return ERROR_SUMMARIES.getOrDefault(errorCode, NOT_ERROR_SUMMARY_CODE_DEFINED);
    }

    public static String getFormattedErrorMessage(ErrorCodesEnum errorCode, String... args) {
        return MessageFormat.format(ERROR_MESSAGES.getOrDefault(errorCode, NOT_ERROR_MESSAGE_DEFINED), args);
    }
}
