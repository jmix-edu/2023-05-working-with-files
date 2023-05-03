package com.company.tm.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum TaskPriority implements EnumClass<String> {

    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private String id;

    TaskPriority(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TaskPriority fromId(String id) {
        for (TaskPriority at : TaskPriority.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}