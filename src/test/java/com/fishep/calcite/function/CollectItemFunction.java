package com.fishep.calcite.function;

import org.apache.calcite.linq4j.function.Parameter;

/**
 * @Classname CollectItemFunction
 * @Description TODO
 * @Date 2024/8/8 18:45
 * @Created by carlo.yan
 */
public class CollectItemFunction {

    public CollectItemFunction() {

    }

    /**
     * CDC数据处理用，此处只做定义
     * @param fields
     * @return
     */
    public boolean eval(@Parameter(name = "fields") Object... fields) {
        throw new UnsupportedOperationException("CollectItemFunction is not implemented.");
    }

}
