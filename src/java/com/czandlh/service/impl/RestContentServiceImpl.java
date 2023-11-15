package com.czandlh.service.impl;

import com.czandlh.entity.RestContent;
import com.czandlh.entity.RestClassification;
import com.czandlh.mapper.RestContentMapper;
import com.czandlh.mapper.RestClassificationMapper;
import com.czandlh.mapper.RestUserMapper;
import com.czandlh.resp.*;
import com.czandlh.service.RestContentService;
import com.czandlh.service.RestUserService;
import com.czandlh.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestContentServiceImpl implements RestContentService {

    @Autowired
    private RestContentMapper mapper;

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestUserMapper userMapper;

    @Autowired
    private RestClassificationMapper classificationMapper;

    @Override
    public ResultResponse add(RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getName())) {
            return resultResponse.fail("参数不能为空");
        }
        try {
            rest.setUid(UUID.randomUUID().toString());
            rest.setOperator(userService.currentUser(request).getUid());
            mapper.insert(rest);
            return resultResponse.success(rest);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse modify(RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid(), rest.getName())) {
            return resultResponse.fail("参数不能为空");
        }
        RestContent rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
        if (Objects.isNull(rest1)) {
            return resultResponse.fail("查询不存在");
        }
        rest1.setOperator(userService.currentUser(request).getUid());
        try {
            mapper.update(rest1);
            return resultResponse.success(rest1);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse delete(RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestContent rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
        if (Objects.isNull(rest1)) {
            return resultResponse.fail("查询不存在");
        }
        rest1.setDeleted(Boolean.TRUE);
        rest1.setOperator(userService.currentUser(request).getUid());
        try {
            mapper.update(rest1);
            return resultResponse.success(rest1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultResponse copy(RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestContent rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
        if (Objects.isNull(rest1)) {
            return resultResponse.fail("查询不存在");
        }
        rest1.setUid(UUID.randomUUID().toString());
        rest1.setOperator(userService.currentUser(request).getUid());
        try {
            mapper.insert(rest1);
            return resultResponse.success(rest1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultResponse detail(RestContent rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestContent rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
        if (Objects.isNull(rest1)) {
            return resultResponse.fail("查询不存在");
        }
        try {
            return resultResponse.success(rest1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultResponse list(CommonParam param, HttpServletRequest request) {
        Map<String, Object> params = Maps.newHashMap();
        ResultResponse resultResponse = new ResultResponse();
        if (param.getPageSize() == null || param.getCurrentPage() == null) {
            return resultResponse.fail("分页信息不能为空");
        }
        List<RestContentResp> list = initList(param, request);
        params.put("page", new PageVO(list.size(), param.getPageSize(), param.getCurrentPage()));
        if (list.size() > param.getPageSize()) {
            if (param.getCurrentPage() * param.getPageSize() > list.size()) {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), list.size());
            } else {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), param.getPageSize() * param.getCurrentPage());
            }
        }
        List<RestClassification> classificationList = classificationMapper.findByDeleted();
        params.put("list", list);
        params.put("classificationList", classificationList);
        return resultResponse.success(params);
    }

    private List<RestContentResp> initList(CommonParam param, HttpServletRequest request) {
        List<RestContent> list = mapper.findByDeleted();
        List<RestContentResp> respList = Lists.newArrayList();
        try {
            for (RestContent rest : list) {
                RestContentResp resp = new RestContentResp();
                BeanUtils.copyProperties(rest, resp);
                resp.setCreateDate(DateUtils.dateToString(rest.getCreateDate()));
                resp.setUpdateDate(DateUtils.dateToString(rest.getUpdateDate()));
                resp.setNumber(StringUtils.substring(rest.getUid(), 0, 8).toUpperCase());
//                resp.setOperator(userService.currentUser(request).getName());
                respList.add(resp);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return respList;
    }

}
