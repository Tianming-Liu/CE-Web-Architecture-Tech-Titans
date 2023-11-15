
package com.czandlh.service.impl;

import com.czandlh.entity.RestUser;
import com.czandlh.mapper.RestUserMapper;
import com.czandlh.resp.CommonParam;
import com.czandlh.resp.PageVO;
import com.czandlh.resp.RestUserResp;
import com.czandlh.resp.ResultResponse;
import com.czandlh.service.RestJwtService;
import com.czandlh.service.RestUserService;
import com.czandlh.type.GenderEnum;
import com.czandlh.type.RoleEnum;
import com.czandlh.utils.Constants;
import com.czandlh.utils.DateUtils;
import com.czandlh.utils.Encryption;
import com.czandlh.utils.MobileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestUserServiceImpl implements RestUserService {

    @Resource
    private RestUserMapper userMapper;

    @Autowired
    private RestJwtService jwtService;

    @Override
    public RestUser getRestUser(String uid) {
        return userMapper.findTop1ByUidAndDeleted(uid);
    }

    @Override
    public RestUser currentUser(HttpServletRequest request) {
//        String token = request.getHeader(Constants.AUTHORIZATION);
//        if (StringUtils.isBlank(token)) {
//            return null;
//        }
//        RestUser user = jwtService.getUser(token);
//        if (Objects.isNull(user)) {
//            return null;
//        }
//        RestUser restUser = userMapper.getByUsernameAndDeleted("manager");
//        return restUser;
        HttpSession session = request.getSession();
        if (session.getAttribute(Constants.CURRENT_USER) == null) {
            return null;
        }
        RestUser currentUser = (RestUser) session.getAttribute(Constants.CURRENT_USER);
        RestUser user = userMapper.findTop1ByUidAndDeleted(currentUser.getUid());
        return user;
    }

    @Override
    public ResultResponse validate(RestUser user, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
            return resultResponse.fail("账号或密码不能为空");
        }
        RestUser user1 = userMapper.getByUsernameAndDeleted(user.getUsername());
        if (Objects.isNull(user1)) {
            return resultResponse.fail("账号不存在");
        }
        try {
            if (!user.getPassword().equals(user1.getPassword())) {
                return resultResponse.fail("密码不正确");
            }
            user1.setPassword(null);
            Map<String, Object> param = new HashMap<>();
            param.put("user", user1);
            HttpSession session = request.getSession();
            session.setAttribute(Constants.CURRENT_USER, user1);
            return resultResponse.success(param);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse register(RestUser user, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(user.getName(), user.getPassword())) {
            return resultResponse.fail("参数不能为空");
        }
        RestUser existUser = userMapper.getByUsernameAndDeleted(user.getUsername());
        if (Objects.nonNull(existUser)) {
            return resultResponse.fail("账号已存在");
        }
        try {
//            user.setPassword(Encryption.md5Crypt(Encryption.decrypt(user.getPassword().trim())));
            user.setPassword(user.getPassword().trim());
            user.setUsername(user.getUsername().trim());
            user.setName(user.getName().trim());
            user.setRole(RoleEnum.user.getCode());
            user.setUid(UUID.randomUUID().toString());
            user.setOperator(user.getUid());
            userMapper.insert(user);
            return resultResponse.success(user);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse add(RestUser user, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(user.getName(), user.getUsername(), user.getPassword())) {
            return resultResponse.fail("参数不能为空");
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            if (!user.getEmail().matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
                return resultResponse.fail("请输入正确的邮箱");
            }
        }
        if (StringUtils.isNotBlank(user.getMobile())) {
            if (!MobileUtils.validator(user.getMobile())) {
                return resultResponse.fail("联系电话必须为 7、8、和 11 位数字");
            }
        }
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());
        user.setName(user.getName());
        user.setMobile(user.getMobile());
        RestUser rest1 = userMapper.getByUsernameAndDeleted(user.getUsername());
        if (Objects.nonNull(rest1)) {
            return resultResponse.fail("账号已存在");
        }
        try {
            user.setUid(UUID.randomUUID().toString());
            user.setOperator(currentUser(request).getUid());
            userMapper.insert(user);
            return resultResponse.success(user);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse modify(RestUser user, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(user.getUid(), user.getName(), user.getUsername())) {
            return resultResponse.fail("参数不能为空");
        }
        RestUser user1 = userMapper.findTop1ByUidAndDeleted(user.getUid());
        if (Objects.isNull(user1)) {
            return resultResponse.fail("查询不存在");
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            if (!user.getEmail().matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
                return resultResponse.fail("请输入正确的邮箱");
            }
        }
        if (StringUtils.isNotBlank(user.getMobile())) {
            if (!MobileUtils.validator(user.getMobile())) {
                return resultResponse.fail("联系电话必须为 7、8、和 11 位数字");
            }
        }
        if (StringUtils.isNotBlank(user.getImage())) {
            user1.setImage(user.getImage());
        }
        user.setUid(user1.getUid());
        user.setRole(user1.getRole());
        user.setOperator(currentUser(request).getUid());
        try {
            userMapper.update(user);
            return resultResponse.success(user);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse reset(CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(param.getUid(), param.getConfirmPassword(), param.getNewPassword(), param.getOldPassword())) {
            return resultResponse.fail("编号不能为空");
        }
        if (!param.getNewPassword().equals(param.getConfirmPassword())) {
            return resultResponse.fail("两次密码输入不一致");
        }
        RestUser user = userMapper.findTop1ByUidAndDeleted(param.getUid());
        if (Objects.isNull(user)) {
            return resultResponse.fail("查询不存在");
        }
        try {
            String pass = Encryption.decrypt(StringUtils.trim(param.getOldPassword()));
            pass = Encryption.md5Crypt(pass);
            if (!pass.equals(user.getPassword())) {
                return resultResponse.fail("密码不正确");
            }
            String newPassword = Encryption.decrypt(StringUtils.trim(param.getNewPassword()));
            newPassword = Encryption.md5Crypt(newPassword);
            user.setPassword(newPassword);
            user.setOperator(currentUser(request).getUid());
            userMapper.update(user);
            return resultResponse.success(user);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse forget(CommonParam param, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(param.getUid(), param.getConfirmPassword(), param.getNewPassword(), param.getOldPassword())) {
            return resultResponse.fail("编号不能为空");
        }
        if (!param.getNewPassword().equals(param.getConfirmPassword())) {
            return resultResponse.fail("两次密码输入不一致");
        }
        RestUser user = userMapper.findTop1ByUidAndDeleted(param.getUid());
        if (Objects.isNull(user)) {
            return resultResponse.fail("查询不存在");
        }
        try {
            String pass = Encryption.decrypt(StringUtils.trim(param.getOldPassword()));
            pass = Encryption.md5Crypt(pass);
            if (!pass.equals(user.getPassword())) {
                return resultResponse.fail("密码不正确");
            }
            String newPassword = Encryption.decrypt(StringUtils.trim(param.getNewPassword()));
            newPassword = Encryption.md5Crypt(newPassword);
            user.setPassword(newPassword);
            user.setOperator(currentUser(request).getUid());
            userMapper.update(user);
            return resultResponse.success(user);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse delete(RestUser user, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(user.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestUser user1 = userMapper.findTop1ByUidAndDeleted(user.getUid());
        if (Objects.isNull(user1)) {
            return resultResponse.fail("员工不存在");
        }
        user1.setDeleted(Boolean.TRUE);
        user1.setOperator(currentUser(request).getUid());
        try {
            userMapper.update(user1);
            return resultResponse.success(user1);
        } catch (Exception e) {
            return resultResponse.fail(e.getMessage());
        }
    }

    @Override
    public ResultResponse detail(RestUser user, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isAnyBlank(user.getUid())) {
            return resultResponse.fail("编号不能为空");
        }
        RestUser user1 = userMapper.findTop1ByUidAndDeleted(user.getUid());
        if (Objects.isNull(user1)) {
            return resultResponse.fail("员工不存在");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("user", user1);
        params.put("genderList", GenderEnum.getAllResults());
        return resultResponse.success(params);
    }

    @Override
    public ResultResponse list(CommonParam param, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        ResultResponse resultResponse = new ResultResponse();
        if (param.getPageSize() == null || param.getCurrentPage() == null) {
            return resultResponse.fail("分页信息不能为空");
        }
        List<RestUserResp> list = initList(param, request);
        params.put("page", new PageVO(list.size(), param.getPageSize(), param.getCurrentPage()));
        if (list.size() > param.getPageSize()) {
            if (param.getCurrentPage() * param.getPageSize() > list.size()) {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), list.size());
            } else {
                list = list.subList((param.getCurrentPage() - 1) * param.getPageSize(), param.getPageSize() * param.getCurrentPage());
            }
        }
        params.put("list", list);
        params.put("genderList", GenderEnum.getAllResults());
        return resultResponse.success(params);
    }

    private List<RestUserResp> initList(CommonParam param, HttpServletRequest request) {
        List<RestUser> list = userMapper.findByDeleted();
        list = list.stream().filter(p -> RoleEnum.user.getCode().equals(p.getRole())).collect(Collectors.toList());
        if (StringUtils.isNotBlank(param.getGender())) {
            list = list.stream().filter(p -> StringUtils.isNotBlank(p.getGender()) && p.getGender().equals(param.getGender())).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(param.getName())) {
            list = list.stream().filter(p -> p.getName().indexOf(param.getName().trim()) != -1).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(param.getMobile())) {
            list = list.stream().filter(p -> p.getMobile().equals(param.getMobile().trim())).collect(Collectors.toList());
        }
        List<RestUserResp> respList = new ArrayList<>();
        try {
            for (RestUser rest : list) {
                RestUserResp resp = new RestUserResp();
                BeanUtils.copyProperties(rest, resp);
                resp.setCreateDate(DateUtils.dateToString(rest.getCreateDate()));
                resp.setUpdateDate(DateUtils.dateToString(rest.getUpdateDate()));
                if (StringUtils.isNotBlank(rest.getGender())) {
                    resp.setGenderName(GenderEnum.getName(rest.getGender()));
                }
                if (StringUtils.isNotBlank(rest.getRole())) {
                    resp.setRoleName(RoleEnum.getName(rest.getRole()));
                }
                if (rest.getBirthDate() != null) {
                    resp.setBirthDateName(DateUtils.dateToString(rest.getBirthDate()));
                }
                resp.setNumber(StringUtils.substring(rest.getUid(), 0, 8).toUpperCase());
                respList.add(resp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return respList;
    }

}
