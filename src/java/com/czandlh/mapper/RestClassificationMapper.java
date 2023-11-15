package com.czandlh.mapper;

import com.czandlh.entity.RestClassification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestClassificationMapper {

    RestClassification findTop1ByUidAndDeleted(String uid);

    List<RestClassification> findByDeleted();

    int insert(RestClassification rest);

    int update(RestClassification rest);
}
