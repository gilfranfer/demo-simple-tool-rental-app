package com.gillab.model;

import lombok.Getter;

@Getter
public enum ToolTypeEnum {
    CHAINSAW("Chainsaw"),
    LADDER("Ladder"),
    JACKHAMMER("Jackhammer");

    private final String displayName;

    ToolTypeEnum(String displayName) {
        this.displayName = displayName;
    }

}