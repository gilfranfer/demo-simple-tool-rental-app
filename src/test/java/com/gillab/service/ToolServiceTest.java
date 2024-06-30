package com.gillab.service;

import com.gillab.exception.ApplicationException;
import com.gillab.model.Tool;
import com.gillab.model.ToolTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.gillab.TestConstants.*;
import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_TOOL_CODE;
import static com.gillab.exception.ApplicationErrors.getErrorSummary;
import static com.gillab.exception.ApplicationErrors.getFormattedErrorMessage;
import static com.gillab.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ToolService Tests")
class ToolServiceTest {

    @Mock
    private Map<String, Tool> toolStock;

    @InjectMocks
    private ToolService toolService;

    @Nested
    @DisplayName("Get tool by code")
    class GetToolByCodeTests {

        @Test
        @DisplayName("Should return a tool when tool code is valid")
        void testGetToolByCode_validToolCode() {
            when(toolStock.getOrDefault(VALID_TEST_TOOL_CODE, null)).thenReturn(TEST_TOOL);

            Tool tool = toolService.getToolByCode(VALID_TEST_TOOL_CODE, CORRELATION_ID);

            assertNotNull(tool, "Tool should not be null for a valid tool code");
            
            verify(toolStock).getOrDefault(VALID_TEST_TOOL_CODE, null);
            verifyNoMoreInteractions(toolStock);
        }

        @Test
        @DisplayName("Should throw exception when tool code is invalid")
        void testGetToolByCode_invalidToolCode() {
            when(toolStock.getOrDefault(INVALID_TEST_TOOL_CODE, null)).thenReturn(null);

            ApplicationException exception = assertThrows(
                    ApplicationException.class,
                    () -> toolService.getToolByCode(INVALID_TEST_TOOL_CODE, CORRELATION_ID),
                    "Expected getToolByCode to throw, but it didn't"
            );

            assertAll(
                    ()->{
                        assertEquals(INVALID_TOOL_CODE.getDisplayName(), exception.getErrorCode(), "Error code should match");
                        assertEquals(CORRELATION_ID, exception.getCorrelationId(), "Error summary should match");
                        assertEquals(getErrorSummary(INVALID_TOOL_CODE), exception.getErrorSummary(), "Error summary should match");
                        assertEquals(getFormattedErrorMessage(INVALID_TOOL_CODE, INVALID_TEST_TOOL_CODE), exception.getErrorMessage(), "Error message should match");
                    }
            );

            verify(toolStock).getOrDefault(INVALID_TEST_TOOL_CODE, null);
            verifyNoMoreInteractions(toolStock);
        }

        @Test
        @DisplayName("Should throw NullPointerException when tool code is null")
        void testGetToolByCode_nullToolCode() {
            assertThrows(NullPointerException.class,
                    () -> toolService.getToolByCode(null, CORRELATION_ID),
                    "Expected getToolByCode to throw NullPointerException for null tool code"
            );

            verifyNoInteractions(toolStock);
        }

        @Test
        @DisplayName("Should throw NullPointerException when correlation ID is null")
        void testGetToolByCode_nullCorrelationId() {
            assertThrows(NullPointerException.class,
                    () -> toolService.getToolByCode(VALID_TEST_TOOL_CODE, null),
                    "Expected getToolByCode to throw NullPointerException for null correlation ID"
            );

            verifyNoInteractions(toolStock);
        }

    }

    @Nested
    @DisplayName("Get daily rental charge by tool type")
    class GetDailyRentalChargeByToolTypeTests {

        @Test
        @DisplayName("Should return correct daily charge for LADDER")
        void testGetDailyRentalChargeByToolType_Ladder() {
            double charge = toolService.getDailyRentalChargeByToolType(ToolTypeEnum.LADDER);
            assertEquals(LADDER_DAILY_CHARGE, charge, "Daily charge for LADDER should be correct");
        }

        @Test
        @DisplayName("Should return correct daily charge for CHAINSAW")
        void testGetDailyRentalChargeByToolType_Chainsaw() {
            double charge = toolService.getDailyRentalChargeByToolType(ToolTypeEnum.CHAINSAW);
            assertEquals(CHAINSAW_DAILY_CHARGE, charge, "Daily charge for CHAINSAW should be correct");
        }

        @Test
        @DisplayName("Should return correct daily charge for JACKHAMMER")
        void testGetDailyRentalChargeByToolType_Jackhammer() {
            double charge = toolService.getDailyRentalChargeByToolType(ToolTypeEnum.JACKHAMMER);
            assertEquals(JACKHAMMER_DAILY_CHARGE, charge, "Daily charge for JACKHAMMER should be correct");
        }

        @Test
        @DisplayName("Should throw NullPointerException when tool type is null")
        void testGetDailyRentalChargeByToolType_nullToolType() {
            assertThrows(NullPointerException.class,
                    () -> toolService.getDailyRentalChargeByToolType(null),
                    "Expected getDailyRentalChargeByToolType to throw NullPointerException for null tool type"
            );
        }
    }

    @Nested
    @DisplayName("Is tool type weekday chargeable")
    class IsToolTypeWeekdayChargeableTests {

        @Test
        @DisplayName("Should return true for LADDER")
        void testIsToolTypeWeekdayChargeable_Ladder() {
            boolean chargeable = toolService.isToolTypeWeekdayChargeable(ToolTypeEnum.LADDER);
            assertTrue(chargeable, "LADDER should be chargeable on weekdays");
        }

        @Test
        @DisplayName("Should return true for CHAINSAW")
        void testIsToolTypeWeekdayChargeable_Chainsaw() {
            boolean chargeable = toolService.isToolTypeWeekdayChargeable(ToolTypeEnum.CHAINSAW);
            assertTrue(chargeable, "CHAINSAW should be chargeable on weekdays");
        }

        @Test
        @DisplayName("Should return true for JACKHAMMER")
        void testIsToolTypeWeekdayChargeable_Jackhammer() {
            boolean chargeable = toolService.isToolTypeWeekdayChargeable(ToolTypeEnum.JACKHAMMER);
            assertTrue(chargeable, "JACKHAMMER should be chargeable on weekdays");
        }

        @Test
        @DisplayName("Should throw NullPointerException when tool type is null")
        void testIsToolTypeWeekdayChargeable_nullToolType() {
            assertThrows(NullPointerException.class,
                    () -> toolService.isToolTypeWeekdayChargeable(null),
                    "Expected isToolTypeWeekdayChargeable to throw NullPointerException for null tool type"
            );
        }
    }

    @Nested
    @DisplayName("Is tool type weekend chargeable")
    class IsToolTypeWeekendChargeableTests {

        @Test
        @DisplayName("Should return true for LADDER")
        void testIsToolTypeWeekendChargeable_Ladder() {
            boolean chargeable = toolService.isToolTypeWeekendChargeable(ToolTypeEnum.LADDER);
            assertTrue(chargeable, "LADDER should be chargeable on weekends");
        }

        @Test
        @DisplayName("Should return false for CHAINSAW")
        void testIsToolTypeWeekendChargeable_Chainsaw() {
            boolean chargeable = toolService.isToolTypeWeekendChargeable(ToolTypeEnum.CHAINSAW);
            assertFalse(chargeable, "CHAINSAW should not be chargeable on weekends");
        }

        @Test
        @DisplayName("Should return false for JACKHAMMER")
        void testIsToolTypeWeekendChargeable_Jackhammer() {
            boolean chargeable = toolService.isToolTypeWeekendChargeable(ToolTypeEnum.JACKHAMMER);
            assertFalse(chargeable, "JACKHAMMER should not be chargeable on weekends");
        }

        @Test
        @DisplayName("Should throw NullPointerException when tool type is null")
        void testIsToolTypeWeekendChargeable_nullToolType() {
            assertThrows(NullPointerException.class,
                    () -> toolService.isToolTypeWeekendChargeable(null),
                    "Expected isToolTypeWeekendChargeable to throw NullPointerException for null tool type"
            );
        }
    }

    @Nested
    @DisplayName("Is tool type holiday chargeable")
    class IsToolTypeHolidayChargeableTests {

        @Test
        @DisplayName("Should return false for LADDER")
        void testIsToolTypeHolidayChargeable_Ladder() {
            boolean chargeable = toolService.isToolTypeHolidayChargeable(ToolTypeEnum.LADDER);
            assertFalse(chargeable, "LADDER should not be chargeable on holidays");
        }

        @Test
        @DisplayName("Should return true for CHAINSAW")
        void testIsToolTypeHolidayChargeable_Chainsaw() {
            boolean chargeable = toolService.isToolTypeHolidayChargeable(ToolTypeEnum.CHAINSAW);
            assertTrue(chargeable, "CHAINSAW should be chargeable on holidays");
        }

        @Test
        @DisplayName("Should return false for JACKHAMMER")
        void testIsToolTypeHolidayChargeable_Jackhammer() {
            boolean chargeable = toolService.isToolTypeHolidayChargeable(ToolTypeEnum.JACKHAMMER);
            assertFalse(chargeable, "JACKHAMMER should not be chargeable on holidays");
        }

        @Test
        @DisplayName("Should throw NullPointerException when tool type is null")
        void testIsToolTypeHolidayChargeable_nullToolType() {
            assertThrows(NullPointerException.class,
                    () -> toolService.isToolTypeHolidayChargeable(null),
                    "Expected isToolTypeHolidayChargeable to throw NullPointerException for null tool type"
            );
        }
    }

}

