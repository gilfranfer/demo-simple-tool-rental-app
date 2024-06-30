package com.gillab;

import com.gillab.model.Tool;
import com.gillab.model.ToolTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {

    public static final String VALID_TEST_TOOL_CODE = "DTC";
    public static final String INVALID_TEST_TOOL_CODE = "NDTC";
    public static final String DEMO_TOOL_BRAND = "Cool Brand";
    public static final UUID CORRELATION_ID = UUID.randomUUID();

    public static final Tool TEST_TOOL = new Tool(ToolTypeEnum.CHAINSAW, VALID_TEST_TOOL_CODE, DEMO_TOOL_BRAND);

}
