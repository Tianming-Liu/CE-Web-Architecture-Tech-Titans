package com.czandlh.mapper;

import com.czandlh.entity.RestContent;
import com.czandlh.entity.RestHotel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RestHotelMapper {

    RestHotel findTop1ByUidAndDeleted(String uid);

    List<RestHotel> findByDeleted();

    int insert(RestHotel rest);

    int update(RestHotel rest);
}
