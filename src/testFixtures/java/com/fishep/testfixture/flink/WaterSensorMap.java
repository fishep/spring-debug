package com.fishep.testfixture.flink;

import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.functions.RichMapFunction;

/**
 * @Author fly.fei
 * @Date 2024/5/28 11:58
 * @Desc
 **/
public class WaterSensorMap extends RichMapFunction<String, WaterSensor> {

    @Override
    public WaterSensor map(String value) throws Exception {
        String[] data = value.split(",");

        if (data.length == 3) {
            WaterSensor sensor = new WaterSensor();
            sensor.setId(data[0]);
            sensor.setTs(Long.valueOf(data[1]));
            sensor.setVal(Long.valueOf(data[2]));

            return sensor;
        }

        return null;
    }

    @Override
    public void open(OpenContext openContext) throws Exception {
        super.open(openContext);
    }

}
