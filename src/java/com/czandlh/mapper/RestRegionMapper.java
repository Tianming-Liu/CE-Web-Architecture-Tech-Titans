package com.czandlh.mapper;

import com.czandlh.entity.RestRegion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestRegionMapper {

    RestRegion findTop1ByUidAndDeleted(String uid);

    List<RestRegion> findByDeleted();

    int insert(RestRegion rest);

    int update(RestRegion rest);
}
