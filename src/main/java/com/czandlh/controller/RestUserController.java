package com.czandlh.controller;

import com.czandlh.entity.RestUser;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestUserService;
import com.czandlh.utils.ImageVerificationCode;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/rest/user")
public class RestUserController {

    private final static Map<String, String> imageCodeMap = Maps.newHashMap();

    @Autowired
    private RestUserService service;

    @RequestMapping(value = "/imageCode/{uid}")
    public void imageCode(HttpServletResponse response, @PathVariable("uid") String uid) throws Exception {
        ImageVerificationCode imageVerificationCode = new ImageVerificationCode();
        BufferedImage image = imageVerificationCode.getImage();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        imageCodeMap.put(uid, imageVerificationCode.getText());
        imageVerificationCode.output(image, response.getOutputStream());
    }

    @RequestMapping(value = "/validate")
    public ResultResponse validate(@RequestBody CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        String imageCode = imageCodeMap.get(param.getUid());
        if (StringUtils.isBlank(imageCode)) {
            return resultResponse.fail("验证码不存在");
        }
        if (!imageCode.equalsIgnoreCase(param.getText())) {
            return resultResponse.fail("验证码错误");
        }
        RestUser user = new RestUser();
        user.setPassword(param.getPassword());
        user.setUsername(param.getUsername());
        return service.validate(user, request);
    }

    @RequestMapping(value = "/currentUser")
    public ResultResponse currentUser(HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return resultResponse.success(user);
    }

    @RequestMapping(value = "/register")
    public ResultResponse register(@RequestBody RestUser techUser, HttpServletRequest request) {
        return service.register(techUser, request);
    }

    @RequestMapping(value = "/add")
    public ResultResponse add(@RequestBody RestUser techUser, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.add(techUser, request);
    }

    @RequestMapping(value = "/modify")
    public ResultResponse modify(@RequestBody RestUser techUser, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.modify(techUser, request);
    }

    @RequestMapping(value = "/delete")
    public ResultResponse delete(@RequestBody RestUser techUser, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.delete(techUser, request);
    }

    @RequestMapping(value = "/list")
    public ResultResponse list(@RequestBody CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.list(param, request);
    }

    @RequestMapping(value = "/reset")
    public ResultResponse reset(@RequestBody CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.reset(param, request);
    }

    @RequestMapping(value = "/forget")
    public ResultResponse forget(@RequestBody CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.forget(param, request);
    }

    @RequestMapping(value = "/detail")
    public ResultResponse detail(@RequestBody RestUser techUser, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        RestUser user = service.currentUser(request);
        if (Objects.isNull(user)) {
            return resultResponse.failLogin(ResultResponse.Code.TOKEN_EXPIRED);
        }
        return service.detail(techUser, request);
    }

}
