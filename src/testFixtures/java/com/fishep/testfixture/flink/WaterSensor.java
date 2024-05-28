package com.fishep.testfixture.flink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author fly.fei
 * @Date 2024/5/28 11:53
 * @Desc
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterSensor {

    private String id;

    // seconds
    private Long ts;

    private Long val;

}
