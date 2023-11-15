package com.czandlh.controller;

import com.czandlh.entity.RestContent;
import com.czandlh.entity.RestUser;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestContentService;
import com.czandlh.service.RestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping(value = "/rest/content")
public class RestContentController {

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestContentService service;

    @RequestMapping(value = "/add")
    public ResultResponse add(@RequestBody RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.add(rest, request);
    }

    @RequestMapping(value = "/modify")
    public ResultResponse modify(@RequestBody RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.modify(rest, request);
    }

    @RequestMapping(value = "/delete")
    public ResultResponse delete(@RequestBody RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.delete(rest, request);
    }

    @RequestMapping(value = "/copy")
    public ResultResponse copy(@RequestBody RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.copy(rest, request);
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
    public ResultResponse detail(@RequestBody RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = userService.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.detail(rest, request);
    }

}
