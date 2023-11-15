package com.czandlh.controller;

import com.czandlh.entity.RestClassification;
import com.czandlh.entity.RestUser;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestClassificationService;
import com.czandlh.service.RestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping(value = "/rest/classification")
public class RestClassificationController {

    @Autowired
    private RestUserService service;

    @Autowired
    private RestClassificationService applyService;

    @RequestMapping(value = "/add")
    public ResultResponse add(@RequestBody RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return applyService.add(rest, request);
    }

    @RequestMapping(value = "/modify")
    public ResultResponse modify(@RequestBody RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return applyService.modify(rest, request);
    }

    @RequestMapping(value = "/delete")
    public ResultResponse delete(@RequestBody RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return applyService.delete(rest, request);
    }

    @RequestMapping(value = "/list")
    public ResultResponse list(@RequestBody CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return applyService.list(param, request);
    }

    @RequestMapping(value = "/detail")
    public ResultResponse detail(@RequestBody RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return applyService.detail(rest, request);
    }

}
