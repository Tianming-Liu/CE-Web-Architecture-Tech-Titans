package com.czandlh.service.impl;

import com.czandlh.entity.RestClassification;
import com.czandlh.entity.RestContent;
import com.czandlh.entity.RestHotel;
import com.czandlh.entity.RestRegion;
import com.czandlh.mapper.*;
import com.czandlh.resp.*;
import com.czandlh.service.RestContentService;
import com.czandlh.service.RestHotelService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestHotelServiceImpl implements RestHotelService {

    @Autowired
    private RestHotelMapper mapper;

    @Autowired
    private RestUserService userService;

    @Autowired
    private RestRegionMapper regionMapper;

    @Override
    public ResultResponse add(RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getRegion())) {
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
    public ResultResponse modify(RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid(), rest.getRegion())) {
            return resultResponse.fail("参数不能为空");
        }
        RestHotel rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
    public ResultResponse delete(RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestHotel rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
    public ResultResponse detail(RestHotel rest, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(rest.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestHotel rest1 = mapper.findTop1ByUidAndDeleted(rest.getUid());
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
        List<RestHotelResp> list = initList(param, request);
        if (StringUtils.isNotEmpty(param.getRegion())) {
            list = list.stream().filter(p -> p.getRegionName().indexOf(param.getRegion().trim()) != -1).collect(Collectors.toList());
        }
        params.put("page", new PageVO(list.size(), param.getPageSize(), param.getCurrentPage()));
        if (list.size() > param.getPageSize()) {
            if (param.getCurrentPage() * param.getPageSize() > list.size()) {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), list.size());
            } else {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), param.getPageSize() * param.getCurrentPage());
            }
        }
        List<RestRegion> regionList = regionMapper.findByDeleted();
        params.put("list", list);
        params.put("regionList", regionList);
        return resultResponse.success(params);
    }

    private List<RestHotelResp> initList(CommonParam param, HttpServletRequest request) {
        List<RestHotel> list = mapper.findByDeleted();
        if (StringUtils.isNotEmpty(param.getName())) {
            list = list.stream().filter(p -> p.getName().indexOf(param.getName().trim()) != -1).collect(Collectors.toList());
        }
        List<RestRegion> regionList = regionMapper.findByDeleted();
        Map<String, RestRegion> regionMap = regionList.stream().collect(Collectors.toMap(RestRegion::getCode, restRegion -> restRegion));
        List<RestHotelResp> respList = Lists.newArrayList();
        try {
            for (RestHotel rest : list) {
                RestHotelResp resp = new RestHotelResp();
                BeanUtils.copyProperties(rest, resp);
                resp.setCreateDate(DateUtils.dateToString(rest.getCreateDate()));
                resp.setUpdateDate(DateUtils.dateToString(rest.getUpdateDate()));
                RestRegion restRegion = regionMap.get(rest.getRegion());
                if (restRegion == null) {
                    continue;
                }
                resp.setRegionName(restRegion.getStreet());
                resp.setValue(restRegion.getValue());
                resp.setNumber(StringUtils.substring(rest.getUid(), 0, 8).toUpperCase());
                respList.add(resp);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return respList;
    }

}
