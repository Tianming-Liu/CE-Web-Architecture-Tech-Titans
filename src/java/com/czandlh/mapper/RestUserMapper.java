package com.czandlh.mapper;

import com.czandlh.entity.RestUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestUserMapper {

    RestUser getByUsernameAndDeleted(String username);

    RestUser getByCodeAndDeleted(String username);

    RestUser findTop1ByUidAndDeleted(String uid);

    List<RestUser> findByDeleted();

    int insert(RestUser rest);

    int update(RestUser rest);
}
