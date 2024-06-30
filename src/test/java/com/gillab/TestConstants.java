package com.gillab;

import com.gillab.model.Tool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

import static com.gillab.model.ToolTypeEnum.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {

    public static final String VALID_TEST_TOOL_CODE = "DTC";
    public static final String INVALID_TEST_TOOL_CODE = "NDTC";
    public static final String DEMO_TOOL_BRAND = "Cool Brand";
    public static final UUID CORRELATION_ID = UUID.randomUUID();

    public static final Tool TEST_TOOL = new Tool(CHAINSAW, VALID_TEST_TOOL_CODE, DEMO_TOOL_BRAND);

    public static final String TOOL_CODE_CHNS = "CHNS";
    public static final String TOOL_CODE_LADW = "LADW";
    public static final String TOOL_CODE_JAKD = "JAKD";
    public static final String TOOL_CODE_JAKR = "JAKR";

    public static final Map<String,Tool> TEST_TOOL_STOCK =
        Map.of(
        TOOL_CODE_CHNS, new Tool(CHAINSAW, TOOL_CODE_CHNS, "Stihl"),
        TOOL_CODE_LADW, new Tool(LADDER, TOOL_CODE_LADW, "Werner"),
        TOOL_CODE_JAKD, new Tool(JACKHAMMER, TOOL_CODE_JAKD, "DeWalt"),
        TOOL_CODE_JAKR, new Tool(JACKHAMMER, TOOL_CODE_JAKR, "Ridgid")
        );

}
