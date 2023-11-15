package com.czandlh.service.impl;

import com.czandlh.entity.RestClassification;
import com.czandlh.entity.RestUser;
import com.czandlh.mapper.RestClassificationMapper;
import com.czandlh.mapper.RestUserMapper;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.PageVO;
import com.czandlh.resp.RestClassificationResp;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestClassificationService;
import com.czandlh.service.RestUserService;
import com.czandlh.type.RoleEnum;
import com.czandlh.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestClassificationServiceImpl implements RestClassificationService {

    @Autowired
    private RestClassificationMapper mapper;

    @Autowired
    private RestUserService userService;

    @Override
    public ResultResponse add(RestClassification rest, HttpServletRequest request) {
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
    public ResultResponse modify(RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid(), rest.getName())) {
            return resultResponse.fail("参数不能为空");
        }
        RestClassification rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
        if (Objects.isNull(rest1)) {
            return resultResponse.fail("查询不存在");
        }
        rest.setUid(rest1.getUid());
        rest.setOperator(userService.currentUser(request).getUid());
        try {
            mapper.update(rest);
            return resultResponse.success(rest);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse delete(RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestClassification rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
        if (Objects.isNull(rest1)) {
            return resultResponse.fail("查询不存在");
        }
        rest1.setDeleted(Boolean.TRUE);
        rest1.setUpdateDate(new Date());
        rest1.setOperator(userService.currentUser(request).getUid());
        try {
            mapper.update(rest1);
            return resultResponse.success(rest1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultResponse detail(RestClassification rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestClassification rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
        List<RestClassificationResp> list = initList(param, request);
        params.put("page", new PageVO(list.size(), param.getPageSize(), param.getCurrentPage()));
        if (list.size() > param.getPageSize()) {
            if (param.getCurrentPage() * param.getPageSize() > list.size()) {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), list.size());
            } else {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), param.getPageSize() * param.getCurrentPage());
            }
        }
        params.put("list", list);
        return resultResponse.success(params);
    }

    private List<RestClassificationResp> initList(CommonParam param, HttpServletRequest request) {
        List<RestClassification> list = mapper.findByDeleted();
        if (StringUtils.isNotEmpty(param.getName())) {
            list = list.stream().filter(p -> p.getName().indexOf(param.getName().trim()) != -1).collect(Collectors.toList());
        }
        RestUser restUser1 = userService.currentUser(request);
        if (restUser1 != null && StringUtils.isNotBlank(restUser1.getRole()) && !RoleEnum.manager.getCode().equals(restUser1.getRole())) {
            list = list.stream().filter(p -> p.getOperator().equals(restUser1.getUid())).collect(Collectors.toList());
        }
        List<RestClassificationResp> respList = Lists.newArrayList();
        try {
            for (RestClassification rest : list) {
                RestClassificationResp resp = new RestClassificationResp();
                BeanUtils.copyProperties(rest, resp);
                resp.setCreateDate(DateUtils.dateToString(rest.getCreateDate()));
                resp.setUpdateDate(DateUtils.dateToString(rest.getUpdateDate()));
                resp.setNumber(StringUtils.substring(rest.getUid(), 0, 8).toUpperCase());
                resp.setOperator(userService.currentUser(request).getName());
                respList.add(resp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return respList;
    }

}
