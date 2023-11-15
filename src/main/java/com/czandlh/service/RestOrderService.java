package com.czandlh.service;

import com.czandlh.entity.RestRegion;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.RestRegionResp;
import com.czandlh.resp.ResultResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestOrderService {

    ResultResponse add(RestRegion rest, HttpServletRequest request);

    ResultResponse modify(RestRegion rest, HttpServletRequest request);

    ResultResponse delete(RestRegion rest, HttpServletRequest request);

    ResultResponse detail(RestRegion rest, HttpServletRequest request);

    ResultResponse list(CommonParam param, HttpServletRequest request);

    List<RestRegionResp> initList(CommonParam param, HttpServletRequest request);

}
