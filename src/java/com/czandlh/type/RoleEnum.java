package com.czandlh.type;

import com.czandlh.resp.RestResult;

import java.util.ArrayList;
import java.util.List;

public enum RoleEnum {
    user("1", "员工"),
    manager("0", "管理员");

    private String code;
    private String name;

    RoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName(String code) {
        for (RoleEnum e : RoleEnum.values()) {
            if (code.equals(e.getCode())) {
                return e.getName();
            }
        }
        return null;
    }

    public static List<RestResult> getAllResults() {
        List<RestResult> results = new ArrayList<>();
        for (RoleEnum e : RoleEnum.values()) {
            RestResult result = new RestResult();
            result.setLabel(e.getName());
            result.setValue(e.getCode());
            results.add(result);
        }
        return results;
    }

}
