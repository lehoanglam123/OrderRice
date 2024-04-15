package com.bezkoder.spring.datajpa.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    @NotNull
    public static ResponseEntity <Object> generateResponse(HttpStatus status, String message, Object responseObj)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        try
        {
            map.put("status", status.value());
            map.put("message", message);
            map.put("data",responseObj);
            return new ResponseEntity<Object>(map,status);
        }
        catch (Exception e)
        {
            map.clear();
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", e.getMessage());
            map.put("data", null);
            return new ResponseEntity<Object>(map,status);
        }
    }
}
