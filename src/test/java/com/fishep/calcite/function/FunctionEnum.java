package com.fishep.calcite.function;

public enum FunctionEnum {

    IS_CHANGE_TO("IS_CHANGE_TO", IsChangeToFunction.class, "eval"),
    GROUP_BY("GROUP BY", null, null),
    ANY_VALUE("ANY_VALUE", null, null),
    COLLECT_ITEM("COLLECT_ITEM", CollectItemFunction.class, "eval");

    private String name;
    private Class<?> clazz;
    private String method;


    FunctionEnum(String name, Class<?> clazz, String method) {
        this.name = name;
        this.clazz = clazz;
        this.method = method;
    }

    public boolean match(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getMethod() {
        return method;
    }

    public static FunctionEnum getFunctionByName(String name) {
        for (FunctionEnum functionEnum : FunctionEnum.values()) {
            if (functionEnum.getName().equalsIgnoreCase(name)) {
                return functionEnum;
            }
        }
        throw new IllegalArgumentException("not support function ["+name+"]");
    }
}
