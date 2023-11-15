package com.czandlh.service;

import com.czandlh.entity.RestClassification;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;

import javax.servlet.http.HttpServletRequest;

public interface RestClassificationService {

    ResultResponse add(RestClassification rest, HttpServletRequest request);

    ResultResponse modify(RestClassification rest, HttpServletRequest request);

    ResultResponse delete(RestClassification rest, HttpServletRequest request);

    ResultResponse detail(RestClassification rest, HttpServletRequest request);

    ResultResponse list(CommonParam param, HttpServletRequest request);

}
