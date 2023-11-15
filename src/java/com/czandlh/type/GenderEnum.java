package com.czandlh.type;

import com.czandlh.resp.RestResult;

import java.util.ArrayList;
import java.util.List;

public enum GenderEnum {

    male("male","男"),
    female("female","女");

    private String code;
    private String name;

    GenderEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName(String code){
        for(GenderEnum e: GenderEnum.values()){
            if(code.equals(e.getCode())){
                return e.getName();
            }
        }
        return  null;
    }

    public static List<RestResult> getAllResults() {
        List<RestResult> results = new ArrayList<>();
        for (GenderEnum e : GenderEnum.values()) {
            RestResult result = new RestResult();
            result.setLabel(e.getName());
            result.setValue(e.getCode());
            results.add(result);
        }
        return results;
    }

}
