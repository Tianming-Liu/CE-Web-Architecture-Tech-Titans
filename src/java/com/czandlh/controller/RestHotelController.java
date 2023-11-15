package com.czandlh.controller;

import com.czandlh.entity.RestHotel;
import com.czandlh.entity.RestUser;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestHotelService;
import com.czandlh.service.RestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping(value = "/rest/hotel")
public class RestHotelController {

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestHotelService service;

    @RequestMapping(value = "/add")
    public ResultResponse add(@RequestBody RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.add(rest, request);
    }

    @RequestMapping(value = "/modify")
    public ResultResponse modify(@RequestBody RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.modify(rest, request);
    }

    @RequestMapping(value = "/delete")
    public ResultResponse delete(@RequestBody RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.delete(rest, request);
    }

    @RequestMapping(value = "/list")
    public ResultResponse list(@RequestBody CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.list(param, request);
    }

    @RequestMapping(value = "/detail")
    public ResultResponse detail(@RequestBody RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.detail(rest, request);
    }

}
