package com.gillab.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gillab.TestConstants.CORRELATION_ID;
import static com.gillab.TestConstants.INVALID_TEST_TOOL_CODE;
import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_TOOL_CODE;
import static com.gillab.exception.ApplicationErrors.getErrorSummary;
import static com.gillab.exception.ApplicationErrors.getFormattedErrorMessage;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Exception builder Tests")
class ExceptionBuilderTest {

    @Nested
    @DisplayName("Build business application exception")
    class BuildBusinessApplicationExceptionTests {

        @Test
        @DisplayName("Should build ApplicationException with correct details")
        void testBuildBusinessApplicationException() {
            ApplicationException exception = ExceptionBuilder.buildBusinessApplicationException(CORRELATION_ID, INVALID_TOOL_CODE, INVALID_TEST_TOOL_CODE);

            assertNotNull(exception, "ApplicationException should not be null");
            assertEquals(CORRELATION_ID, exception.getCorrelationId(), "Correlation ID should match");
            assertEquals(INVALID_TOOL_CODE.getDisplayName(), exception.getErrorCode(), "Error code should match");
            assertEquals(getErrorSummary(INVALID_TOOL_CODE), exception.getErrorSummary(), "Error summary should match");
            assertEquals(getFormattedErrorMessage(INVALID_TOOL_CODE, INVALID_TEST_TOOL_CODE), exception.getErrorMessage(), "Error message should match");
        }

        @Test
        @DisplayName("Null args should not generate NullPointerException")
        void testBuildBusinessApplicationException_NullArgs() {
            ApplicationException exception = ExceptionBuilder.buildBusinessApplicationException(CORRELATION_ID, INVALID_TOOL_CODE, null);

            assertNotNull(exception, "ApplicationException should not be null");
            assertEquals(CORRELATION_ID, exception.getCorrelationId(), "Correlation ID should match");
            assertEquals(INVALID_TOOL_CODE.getDisplayName(), exception.getErrorCode(), "Error code should match");
            assertEquals(getErrorSummary(INVALID_TOOL_CODE), exception.getErrorSummary(), "Error summary should match");
            assertEquals(getFormattedErrorMessage(INVALID_TOOL_CODE), exception.getErrorMessage(), "Error message should match");
        }

        @Test
        @DisplayName("Should throw NullPointerException when correlation ID is null")
        void testBuildBusinessApplicationException_nullCorrelationId() {
            assertThrows(NullPointerException.class,
                    () -> ExceptionBuilder.buildBusinessApplicationException(null, INVALID_TOOL_CODE, INVALID_TEST_TOOL_CODE),
                    "Expected buildBusinessApplicationException to throw NullPointerException for null correlation ID"
            );
        }

        @Test
        @DisplayName("Should throw NullPointerException when error codes is null")
        void testBuildBusinessApplicationException_nullErrorCode() {
            assertThrows(NullPointerException.class,
                    () -> ExceptionBuilder.buildBusinessApplicationException(CORRELATION_ID, null, INVALID_TEST_TOOL_CODE),
                    "Expected buildBusinessApplicationException to throw NullPointerException for null error codes"
            );
        }
    }

}
