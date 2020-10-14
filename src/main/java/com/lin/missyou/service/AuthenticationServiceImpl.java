package com.lin.missyou.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.dto.TokenGetDTO;
import com.lin.missyou.exception.http.ParameterException;
import com.lin.missyou.model.User;
import com.lin.missyou.repository.UserRepository;
import com.lin.missyou.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${wx.appid}")
    private String appId;
    @Value("${wx.appsecret}")
    private String appSecret;
    @Value("${wx.code2session}")
    private String code2SessionUrl;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validateByWx(TokenGetDTO tokenGetDTO) {

    }

    // 返回JWT令牌
    public String code2Session(String code) throws JsonProcessingException {
        String url = MessageFormat.format(this.code2SessionUrl, this.appId, this.appSecret, code);
        RestTemplate restTemplate = new RestTemplate();
        String sessionText = restTemplate.getForObject(url, String.class);
        Map<String, Object> session = new HashMap<>();
        try{
            session = new ObjectMapper().readValue(sessionText, Map.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return this.registerUser(session);
    }

    // 用户注册并返回JWT令牌
    private String registerUser(Map<String, Object> session){
        String openId = (String) session.get("openid");
        if(openId == null){
            throw new ParameterException(20004);
        }
        Optional<User> userOptional = userRepository.findByOpenid(openId);
        if(userOptional.isPresent()){  // 用户已存在系统用户表中，直接返回令牌
            return JwtToken.makeToken(userOptional.get().getId());
        }
        // 用户不存在系统用户表中，即第一次登陆，系统自动注册
        User user = User.builder()
                .openid(openId)
                .build();
        userRepository.save(user);
        // 然后再返回JWT令牌
        Long uid = user.getId();
        return JwtToken.makeToken(uid);
    }
}
