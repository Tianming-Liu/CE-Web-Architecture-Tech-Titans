package com.czandlh.mapper;

import com.czandlh.entity.RestContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestContentMapper {

    RestContent findTop1ByUidAndDeleted(String uid);

    List<RestContent> findByDeleted();

    int insert(RestContent rest);

    int update(RestContent rest);
}
