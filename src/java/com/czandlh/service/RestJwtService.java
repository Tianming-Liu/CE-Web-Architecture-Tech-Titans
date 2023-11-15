package com.czandlh.service;


import com.czandlh.entity.RestUser;

public interface RestJwtService {

    String signUser(RestUser user);

    RestUser getUser(String token);

}
