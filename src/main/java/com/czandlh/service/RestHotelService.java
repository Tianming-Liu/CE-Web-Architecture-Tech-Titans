package com.czandlh.service;

import com.czandlh.entity.RestContent;
import com.czandlh.entity.RestHotel;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;

import javax.servlet.http.HttpServletRequest;

public interface RestHotelService {

    ResultResponse add(RestHotel rest, HttpServletRequest request);

    ResultResponse modify(RestHotel rest, HttpServletRequest request);

    ResultResponse delete(RestHotel rest, HttpServletRequest request);

    ResultResponse detail(RestHotel rest, HttpServletRequest request);

    ResultResponse list(CommonParam param, HttpServletRequest request);
}
