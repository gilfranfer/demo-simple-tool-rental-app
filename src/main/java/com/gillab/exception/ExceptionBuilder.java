package com.gillab.exception;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

import static com.gillab.exception.ApplicationErrors.getErrorSummary;
import static com.gillab.exception.ApplicationErrors.getFormattedErrorMessage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionBuilder {

    /**
     * Build an Application Exception object with details about the
     * @param correlationId ID to track the request.
     * @param errorCode ErrorCodesEnum.
     * @param args the arguments for interpolation in the error message.
     * @return
     */
    public static ApplicationException buildBusinessApplicationException(@NonNull final UUID correlationId, @NonNull final ApplicationErrors.ErrorCodesEnum errorCode, String... args) {
        return new ApplicationException(correlationId, errorCode.getDisplayName(), getErrorSummary(errorCode), getFormattedErrorMessage(errorCode, args));
    }

}
