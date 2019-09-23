package cn.ct.cloud.controller;

import cn.ct.cloud.dto.ResultDTO;
import cn.ct.cloud.utils.CrowdConstant;
import jdk.jfr.events.ThrowablesEvent;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedisOperationController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/save/normal/string/key/value")
    ResultDTO<String> saveNormalStringKeyValue(@RequestParam("normalKey") String normalKey, @RequestParam("normalValue") String normalValue,
                                               @RequestParam("timeOutMinute") Integer timeOutMinute){
        if(StringUtils.isBlank(normalKey)){
            return ResultDTO.failed(CrowdConstant.NORMALKEY_IS_NULL);
        }

        if(StringUtils.isBlank(normalValue)){
            return ResultDTO.failed(CrowdConstant.NORMALVALUE_IS_NYLL);
        }

        if(StringUtils.isBlank(timeOutMinute+"")){
            return ResultDTO.failed(CrowdConstant.TIMEOUTMINUTE_IS_NULL);
        }
        try {
            if(timeOutMinute==-1){
                //不设置过期时间
                stringRedisTemplate.opsForValue().set(normalKey,normalValue);
            }else if(timeOutMinute<=0){
                return ResultDTO.failed(CrowdConstant.TIMEOUTMINUTE_IS_NOT_VALID);
            }else{
                //设置过期时间，按分钟
                stringRedisTemplate.opsForValue().set(normalKey,normalValue, timeOutMinute,TimeUnit.MINUTES);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultDTO.failed(e.getMessage());
        }

        return ResultDTO.successNoData();


    }

    @RequestMapping("/retrieve/string/value/by/string/key")
    ResultDTO<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey){
        if(StringUtils.isBlank(normalKey)){
            return ResultDTO.failed(CrowdConstant.NORMALKEY_IS_NULL);
        }
        String value= null;
        try {
            value = stringRedisTemplate.opsForValue().get(normalKey);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.failed(e.getMessage());
        }
        return ResultDTO.successWithData(value);

    }

    @RequestMapping("/redis/remove/by/key")
    ResultDTO<String> removeByKey(@RequestParam("key") String key){
        if(StringUtils.isBlank(key)){
            return ResultDTO.failed(CrowdConstant.NORMALKEY_IS_NULL);
        }
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.failed(e.getMessage());
        }
        return ResultDTO.successNoData();
    }

    //保存token，id
    @RequestMapping("/save/token/memberId")
    ResultDTO saveTokenOfSignedMemberRemote(String token, Integer memberId){
        try {
            stringRedisTemplate.opsForValue().set(token,memberId.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultDTO.failed(e.getMessage());
        }
        return  ResultDTO.successNoData();
    }
}
