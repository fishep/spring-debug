package com.fishep.calcite.function;


import org.apache.calcite.linq4j.function.Parameter;

public class IsChangeToFunction {

    public IsChangeToFunction() {

    }

    /**
     * 用于描述cdc数据变化是否符合预期
     * <ul>
     *     <li>value非空,field字段变更为value,则为true</li>
     *     <li>value为空,field字段变更,则为true</li>
     *     <li>其他情况下返回false</li>
     * </ul>
     * @param field
     * @param value
     * @return
     */
    public boolean eval(@Parameter(name = "field") String field,
                        @Parameter(name = "value", optional = true) Object value) {
        throw new UnsupportedOperationException("IsChangeToFunction is not implemented.");
    }

}
