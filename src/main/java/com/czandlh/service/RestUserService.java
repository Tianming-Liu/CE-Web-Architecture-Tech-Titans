package com.czandlh.service;

import com.czandlh.entity.RestUser;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;

import javax.servlet.http.HttpServletRequest;

public interface RestUserService {

    RestUser getRestUser(String uid);

    RestUser currentUser(HttpServletRequest request);

    ResultResponse validate(RestUser user, HttpServletRequest request);

    ResultResponse register(RestUser user, HttpServletRequest request);

    ResultResponse add(RestUser user, HttpServletRequest request);

    ResultResponse modify(RestUser user, HttpServletRequest request);

    ResultResponse delete(RestUser user, HttpServletRequest request);

    ResultResponse detail(RestUser user, HttpServletRequest request);

    ResultResponse list(CommonParam param, HttpServletRequest request);

    ResultResponse reset(CommonParam param, HttpServletRequest request);

    ResultResponse forget(CommonParam param, HttpServletRequest request);

}
