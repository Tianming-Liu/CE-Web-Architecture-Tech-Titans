package com.czandlh.controller;

import com.czandlh.entity.RestClassification;
import com.czandlh.entity.RestUser;
import com.czandlh.mapper.RestContentMapper;
import com.czandlh.mapper.RestClassificationMapper;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestContentService;
import com.czandlh.service.RestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ContentController {

    @Autowired
    private RestContentService service;

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestContentMapper mapper;

    @Autowired
    private RestClassificationMapper classificationMapper;

    @RequestMapping("/content/list")
    public ModelAndView list(HttpServletRequest request, CommonParam param) {
        ModelAndView mav = new ModelAndView("Content");
        RestUser user = userService.currentUser(request);
        if (user == null) {
            mav = new ModelAndView("Login");
            return mav;
        }
        param.setPageSize(10);
        param.setCurrentPage(1);
        ResultResponse resultResponse = service.list(param, request);
        List<RestClassification> classificationList = classificationMapper.findByDeleted();
        mav.addObject("classificationList", classificationList);
        mav.addObject("data", resultResponse.getReturnData());
        mav.addObject("user", userService.currentUser(request));
        return mav;
    }
}
