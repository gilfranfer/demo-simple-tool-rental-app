package com.gillab.model;

import lombok.Data;

@Data
public class Tool {

    private final ToolTypeEnum type;
    private final String code;
    private final String brand;

}
