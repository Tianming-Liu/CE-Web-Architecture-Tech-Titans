package com.czandlh.service.impl;

import com.czandlh.entity.*;
import com.czandlh.mapper.*;
import com.czandlh.resp.*;
import com.czandlh.service.RestOrderService;
import com.czandlh.service.RestUserService;
import com.czandlh.utils.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class RestRegionServiceImpl implements RestOrderService {

    @Autowired
    private RestRegionMapper mapper;

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestUserMapper userMapper;

    @Autowired
    private RestContentMapper contentMapper;

    @Autowired
    private RestClassificationMapper courseMapper;

    @Override
    public ResultResponse add(RestRegion rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            String uid = userService.currentUser(request).getUid();
            rest.setUid(UUID.randomUUID().toString());
            mapper.insert(rest);
            return resultResponse.success(rest);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse modify(RestRegion rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid(), rest.getName())) {
            return resultResponse.fail("参数不能为空");
        }
        RestRegion rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
    public ResultResponse delete(RestRegion rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestRegion rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
    public ResultResponse detail(RestRegion rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestRegion rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
        List<RestRegionResp> list = initList(param, request);
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

    @Override
    public List<RestRegionResp> initList(CommonParam param, HttpServletRequest request) {
        List<RestRegion> list = mapper.findByDeleted();
        List<RestRegionResp> respList = Lists.newArrayList();
        try {
            for (RestRegion rest : list) {
                RestRegionResp resp = new RestRegionResp();
                BeanUtils.copyProperties(rest, resp);
                resp.setCreateDate(DateUtils.dateToString(rest.getCreateDate()));
                resp.setUpdateDate(DateUtils.dateToString(rest.getUpdateDate()));
                resp.setNumber(StringUtils.substring(rest.getUid(), 0, 8).toUpperCase());
                respList.add(resp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return respList;
    }
}
