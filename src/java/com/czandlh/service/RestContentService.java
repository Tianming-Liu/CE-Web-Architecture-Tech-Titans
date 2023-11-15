package com.czandlh.service;

import com.czandlh.entity.RestContent;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;

import javax.servlet.http.HttpServletRequest;

public interface RestContentService {

    ResultResponse add(RestContent rest, HttpServletRequest request);

    ResultResponse modify(RestContent rest, HttpServletRequest request);

    ResultResponse delete(RestContent rest, HttpServletRequest request);

    ResultResponse copy(RestContent rest, HttpServletRequest request);

    ResultResponse detail(RestContent rest, HttpServletRequest request);

    ResultResponse list(CommonParam param, HttpServletRequest request);
}
