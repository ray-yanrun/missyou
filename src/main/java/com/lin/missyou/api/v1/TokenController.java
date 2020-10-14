package com.lin.missyou.api.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lin.missyou.dto.TokenDTO;
import com.lin.missyou.dto.TokenGetDTO;
import com.lin.missyou.exception.http.NotFoundException;
import com.lin.missyou.service.AuthenticationService;
import com.lin.missyou.util.JwtToken;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("token")
public class TokenController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    public Map<String, String> getToken(@RequestBody @Validated TokenGetDTO tokenGetDTO) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        String token = null;
        switch (tokenGetDTO.getLoginType()){
            case USER_WX:
                token = authenticationService.code2Session(tokenGetDTO.getAccount());
                break;
            case USER_EMAIL:
                break;
            default:
                throw new NotFoundException(10003);
        }
        map.put("token", token);
        return map;
    }

    @PostMapping("/verify")
    public Map<String, Boolean> verify(@RequestBody TokenDTO tokenDTO){
        Map<String, Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken(tokenDTO.getToken());
        map.put("is_valid", valid);
        return map;
    }
}
