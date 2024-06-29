package com.gillab.exception;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

import static com.gillab.exception.ApplicationErrors.getErrorSummary;
import static com.gillab.exception.ApplicationErrors.getFormattedErrorMessage;
import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionBuilder {

    /**
     * Build an Application Exception object with details about the
     * @param correlationId ID to track the request.
     * @param errorCode ErrorCodesEnum
     * @param args
     * @return
     */
    public static ApplicationException buildBusinessApplicationException(final UUID correlationId, final ApplicationErrors.ErrorCodesEnum errorCode, String... args) {
        return new ApplicationException(isNull(correlationId)?UUID.randomUUID():correlationId, errorCode.getDisplayName(), getErrorSummary(errorCode), getFormattedErrorMessage(errorCode, args));
    }

}
