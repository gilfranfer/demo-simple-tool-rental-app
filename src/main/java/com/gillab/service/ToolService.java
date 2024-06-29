package com.gillab.service;

import com.gillab.model.Tool;
import com.gillab.model.ToolTypeEnum;
import lombok.NonNull;

import java.util.Map;
import java.util.UUID;

import static com.gillab.exception.ApplicationErrors.ErrorCodesEnum.INVALID_TOOL_CODE;
import static com.gillab.exception.ExceptionBuilder.buildBusinessApplicationException;
import static com.gillab.util.Constants.*;
import static java.util.Objects.isNull;


public class ToolService {

    private static ToolService INSTANCE;

    private final Map<String, Tool> toolStock;

    private ToolService(@NonNull final Map<String, Tool> toolStock) {
        this.toolStock = toolStock;
    }

    public static ToolService getInstance(@NonNull final Map<String, Tool> toolStock) {
        if(isNull(INSTANCE)){
            INSTANCE = new ToolService(toolStock);
        }
        return INSTANCE;
    }

    /**
     * Return a tool Validate that a given tool code is valid (exists in the stock).
     * @param toolCode the tool code to validate.
     * @param correlationId ID to track the request.
     * @return {@link Tool}
     */
    public Tool getToolByCode(final String toolCode, final UUID correlationId) {
        Tool tool = toolStock.getOrDefault(toolCode, null);
        if ( isNull(tool) ) {
            throw buildBusinessApplicationException(correlationId, INVALID_TOOL_CODE, toolCode);
        }
        return tool;
    }

    /**
     * Determine the daily rental charge for a give tool type.
     * @param toolType {@link ToolTypeEnum}
     * @return a double value representing the daily rental charge.
     */
    public double getDailyRentalChargeByToolType(ToolTypeEnum toolType) {
        return
            switch (toolType) {
                case LADDER -> LADDER_DAILY_CHARGE;
                case CHAINSAW -> CHAINSAW_DAILY_CHARGE;
                case JACKHAMMER -> JACKHAMMER_DAILY_CHARGE;
            };
    }

    /**
     * Determine if a tool type generates a charge on a week day.
     * Note: Currently, the 3 tool types generate a charge, but we can still benefit of this method when weekday-free tools
     * are introduced, so we don't affect the final charge calculation during checkout.
     * @param toolType {@link ToolTypeEnum}
     * @return true when the tool type generates a charge on a week day, otherwise false.
     */
    public boolean isToolTypeWeekdayChargeable(ToolTypeEnum toolType) {
        return true;
//             switch (toolType) {
//                case LADDER, CHAINSAW, JACKHAMMER -> true;
//            };
    }

    /**
     * Determine if a tool type generates a charge during the weekend .
     * @param toolType {@link ToolTypeEnum}
     * @return true when the tool type generates a charge during the weekend, otherwise false.
     */
    public boolean isToolTypeWeekendChargeable(ToolTypeEnum toolType) {
        return
            switch (toolType) {
                case LADDER -> true;
                case CHAINSAW, JACKHAMMER -> false;
            };
    }

    /**
     * Determine if a tool type generates a charge during a Holiday.
     * @param toolType {@link ToolTypeEnum}
     * @return true when the tool type generates a charge during a Holiday, otherwise false.
     */
    public boolean isToolTypeHolidayChargeable(ToolTypeEnum toolType) {
        return
            switch (toolType) {
                case CHAINSAW -> true;
                case LADDER, JACKHAMMER -> false;
            };
    }

/*
    public List<Tool> initializeToolStock(){
        return Arrays.asList(
                new Tool(CHAINSAW, "CHNS", "Stihl"),
                new Tool(LADDER, "LADW", "Werner"),
                new Tool(JACKHAMMER, "JAKD", "DeWalt"),
                new Tool(JACKHAMMER, "JAKR", "Ridgid")
        );
    }
*/

}
