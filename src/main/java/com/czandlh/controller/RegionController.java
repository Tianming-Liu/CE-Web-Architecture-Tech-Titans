package com.czandlh.controller;

import com.czandlh.entity.*;
import com.czandlh.mapper.*;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestOrderService;
import com.czandlh.service.RestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@Controller
public class RegionController {

    @Autowired
    private RestOrderService service;

    @Autowired
    private RestUserService userService;

    @RequestMapping("/region/list")
    public ModelAndView list(HttpServletRequest request, CommonParam param) {
        ModelAndView mav = new ModelAndView("Region");
        RestUser user = userService.currentUser(request);
        if (user == null) {
            mav = new ModelAndView("Login");
            return mav;
        }
        param.setPageSize(10);
        param.setCurrentPage(1);
        ResultResponse resultResponse = service.list(param, request);
        mav.addObject("data", resultResponse.getReturnData());
        mav.addObject("user", userService.currentUser(request));
        return mav;
    }
}
