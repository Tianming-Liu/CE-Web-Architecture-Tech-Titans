package com.czandlh.controller;

import com.czandlh.entity.RestClassification;
import com.czandlh.entity.RestUser;
import com.czandlh.mapper.RestClassificationMapper;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestClassificationService;
import com.czandlh.service.RestUserService;
import com.czandlh.type.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@Controller
public class ClassificationController {

    @Autowired
    private RestClassificationService service;

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestClassificationMapper mapper;

    @RequestMapping("/classification/list")
    public ModelAndView list(HttpServletRequest request, CommonParam param) {
        ModelAndView mav = new ModelAndView("Classification");
        RestUser user = userService.currentUser(request);
        if (user == null) {
            mav = new ModelAndView("Login");
            return mav;
        }
        if (RoleEnum.manager.getCode().equals(user.getRole())) {
            mav = new ModelAndView("Classification");
        }
        param.setPageSize(10);
        param.setCurrentPage(1);
        ResultResponse resultResponse = service.list(param, request);
        mav.addObject("data", resultResponse.getReturnData());
        mav.addObject("user", userService.currentUser(request));
        return mav;
    }

    @RequestMapping("/classification/edit")
    public ModelAndView edit(HttpServletRequest request, @PathParam("uid") String uid) {
        RestClassification rest = mapper.findTop1ByUidAndDeleted(uid);
        ModelAndView mav = new ModelAndView("ClassificationEdit");
        mav.addObject("rest", rest);
        return mav;
    }

    @RequestMapping("/classification/add")
    public ModelAndView add(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("ClassificationAdd");
        return mav;
    }
}
