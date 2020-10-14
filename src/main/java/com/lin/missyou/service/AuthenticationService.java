package com.lin.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lin.missyou.dto.TokenGetDTO;

public interface AuthenticationService {

    void validateByWx(TokenGetDTO tokenGetDTO);

    String code2Session(String code) throws JsonProcessingException;
}
