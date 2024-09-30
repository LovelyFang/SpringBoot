package com.tsxy.riskcontrol.client.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Author Liu_df
 * @Date 2024/9/29 14:25
 */
public enum RCStatus {

    None(0),
    LogOnly(1),
    Effective(2);

    int code;

    public final static Map<Integer, RCStatus> map = new HashMap<Integer, RCStatus>() {
        private static final long serialVersionUID = -181439234726740427L;

        {
            Arrays.stream(RCStatus.values()).forEach(rcs -> {
                put(rcs.code, rcs);
            });
        }};


    RCStatus(int code) {
        this.code = code;
    }

    public static RCStatus fromCode(int code) {
        return map.getOrDefault(code, None);
    }

    public static RCStatus fromCode(String code) {
        return fromCode(NumberUtils.toInt(code, None.code));
    }
}
