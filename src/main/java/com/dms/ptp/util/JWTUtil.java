package com.dms.ptp.util;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dms.ptp.dto.JWTExtract;
import com.dms.ptp.serviceimplementation.AffinityReachServiceImplementation;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTUtil {
    
    static Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    
    public JWTExtract getIdRoleFromToken(String token) throws JSONException {
        JWTExtract response = new JWTExtract();
        
        String[] split_string = token.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        Base64 base64Url = new Base64(true);
        logger.info("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        logger.info("JWT Body : "+body);
        
        JSONObject obj = new JSONObject(body);
        int userId = (int) obj.get("id");
        String userRole = (String) obj.get("role");
        int roleId = (int) obj.get("roleId");
        logger.info("userId: "+userId + " userRole: "+userRole + " roleId: " +roleId);

        response.setUserId(userId);
        response.setUserRole(userRole);
        response.setRoleId(roleId);
        return response;
    }

}
