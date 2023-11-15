package com.czandlh.controller;

import com.czandlh.entity.*;
import com.czandlh.mapper.*;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestContentService;
import com.czandlh.service.RestUserService;
import com.czandlh.type.RoleEnum;
import com.czandlh.utils.Constants;
import com.czandlh.utils.CsvUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    RestContentService loginService;

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestRegionMapper regionMapper;

    @Autowired
    private RestClassificationMapper classificationMapper;

    @Autowired
    private RestContentMapper contentMapper;

    @Autowired
    private RestHotelMapper hotelMapper;

    @RequestMapping("/init")
    public void init() throws Exception {
        List<String[]> list = CsvUtils.csvReader();
        List<RestRegion> regionList = Lists.newArrayList();
        List<RestClassification> classificationList = Lists.newArrayList();
        Set<String> regions = Sets.newHashSet();
        Set<String> classifications = Sets.newHashSet();
        List<RestContent> contentList = Lists.newArrayList();
        int value = 0;
        int index = 0;
        for (String[] strings : list) {
            index++;
            if (index == 1) {
                continue;
            }
            int amount = Integer.parseInt(strings[4]) +
                    Integer.parseInt(strings[5]) +
                    Integer.parseInt(strings[6]) +
                    Integer.parseInt(strings[7]) +
                    Integer.parseInt(strings[8]) +
                    Integer.parseInt(strings[9]);
            value += amount;
            String code = strings[0].trim();
            if (!regions.contains(strings[0].trim())) {
                RestRegion restRegion = new RestRegion();
                restRegion.setUid(UUID.randomUUID().toString());
                restRegion.setCode(strings[0].trim());
                restRegion.setStreet(strings[1].trim());
                restRegion.setName(strings[2].trim());
                restRegion.setValue(new BigDecimal(value));
                regionList.add(restRegion);
                regions.add(code);
                code = restRegion.getUid();
                value = 0;
            }
            String classification = strings[3].trim();
            if (!classifications.contains(strings[3].trim())) {
                RestClassification restClassification = new RestClassification();
                restClassification.setUid(UUID.randomUUID().toString());
                restClassification.setName(strings[3].trim());
                classifications.add(strings[3].trim());
                classificationList.add(restClassification);
                classification = restClassification.getUid();
            }
        }
        for (RestClassification restClassification : classificationList) {
            classificationMapper.insert(restClassification);
        }
//        for (RestContent restContent : contentList) {
//            contentMapper.insert(restContent);
//        }
        for (RestRegion restRegion : regionList) {
            regionMapper.insert(restRegion);
        }
    }

    @RequestMapping("/initContent")
    public void initContent() throws Exception {
        List<String[]> list = CsvUtils.csvReader();
        List<RestContent> contentList = Lists.newArrayList();
        int index = 0;
        for (String[] strings : list) {
            index++;
            if (index == 1) {
                continue;
            }
            RestContent restContent = new RestContent();
            restContent.setUid(UUID.randomUUID().toString());
            restContent.setCode(strings[0].trim());
            restContent.setStreet(strings[1].trim());
            restContent.setName(strings[2].trim());
            int amount = Integer.parseInt(strings[4]) +
                    Integer.parseInt(strings[5]) +
                    Integer.parseInt(strings[6]) +
                    Integer.parseInt(strings[7]) +
                    Integer.parseInt(strings[8]) +
                    Integer.parseInt(strings[9]);
            String classification = strings[0].trim();
            restContent.setClassification(classification);
            restContent.setAmount(amount);
            contentList.add(restContent);
        }
        for (RestContent restContent : contentList) {
            contentMapper.insert(restContent);
        }
    }

    @RequestMapping("/initHotel")
    public void initHotel() throws Exception {
        List<String[]> list = CsvUtils.csvReaderHotel();
        List<RestHotel> hotelList = Lists.newArrayList();
        int index = 0;
        for (String[] strings : list) {
            index++;
            if (index == 1) {
                continue;
            }
            if (StringUtils.isBlank(strings[0])) {
                continue;
            }
            RestHotel restHotel = new RestHotel();
            restHotel.setUid(UUID.randomUUID().toString());
            restHotel.setName(strings[0].trim());
            restHotel.setLongitude(new BigDecimal(strings[1].trim()));
            restHotel.setLatitude(new BigDecimal(strings[2].trim()));
            restHotel.setRegion(strings[3].trim());
            hotelList.add(restHotel);
        }
        for (RestHotel restHotel : hotelList) {
            hotelMapper.insert(restHotel);
        }
    }


    @RequestMapping("/map")
    public ModelAndView map(HttpServletRequest request, @PathParam("uid") String uid) {
        ModelAndView mav = new ModelAndView("Crime");
        RestHotel restHotel = hotelMapper.findTop1ByUidAndDeleted(uid);
        mav.addObject("rest", restHotel);
        RestRegion restRegion = regionMapper.findTop1ByUidAndDeleted(restHotel.getRegion());
        if (restRegion.getValue().compareTo(new BigDecimal(100.00)) < 0) {
            mav.addObject("color", "green");
        }
        if (restRegion.getValue().compareTo(new BigDecimal(100.00)) >= 0 && restRegion.getValue().compareTo(new BigDecimal(500.00)) < 0) {
            mav.addObject("color", "yellow");
        }
        if (restRegion.getValue().compareTo(new BigDecimal(500.00)) >= 0 && restRegion.getValue().compareTo(new BigDecimal(1000.00)) < 0) {
            mav.addObject("color", "orange");
        }
        if (restRegion.getValue().compareTo(new BigDecimal(1000.00)) >= 0 && restRegion.getValue().compareTo(new BigDecimal(5000.00)) < 0) {
            mav.addObject("color", "blue");
        }
        if (restRegion.getValue().compareTo(new BigDecimal(5000.00)) >= 0) {
            mav.addObject("color", "red");
        }
        return mav;
    }

    @RequestMapping("/crime")
    public ModelAndView crime(HttpServletRequest request, @PathParam("uid") String uid) {
        ModelAndView mav = new ModelAndView("Crime");
        RestHotel restHotel = hotelMapper.findTop1ByUidAndDeleted(uid);
        mav.addObject("rest", restHotel);
        return mav;
    }

    @RequestMapping("/login1")
    public String login1() {
        return "Login1";
    }

    @RequestMapping("/")
    public String login() {
        return "Login";
    }

    @RequestMapping("/register")
    public String register() {
        return "Register";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(Constants.CURRENT_USER);
        return "Login";
    }

    @RequestMapping("/user/list")
    public ModelAndView list(HttpServletRequest request, CommonParam param) {
        ModelAndView mav = new ModelAndView("User");
        param.setRole(RoleEnum.manager.getCode());
        param.setPageSize(10);
        param.setCurrentPage(1);
        ResultResponse resultResponse = userService.list(param, request);
        mav.addObject("data", resultResponse.getReturnData());
        mav.addObject("user", userService.currentUser(request));
        return mav;
    }
}
