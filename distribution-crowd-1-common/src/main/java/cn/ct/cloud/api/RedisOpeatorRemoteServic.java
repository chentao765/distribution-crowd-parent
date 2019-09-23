package cn.ct.cloud.api;

import cn.ct.cloud.dto.ResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "redis-provider")
public interface RedisOpeatorRemoteServic {
    /**
     *
     * @param normalKey
     * @param normalValue
     *
     * @return
     */
    @RequestMapping("/save/normal/string/key/value")
    ResultDTO<String> saveNormalStringKeyValue(@RequestParam("normalKey") String normalKey, @RequestParam("normalValue") String normalValue,
                                               @RequestParam("timeOutMinute") Integer timeOutMinute);

    @RequestMapping("/retrieve/string/value/by/string/key")
    ResultDTO<String> retrieveStringValueByStringKey(@RequestParam("normalKey") String normalKey);

    @RequestMapping("/redis/remove/by/key")
    ResultDTO<String> removeByKey(@RequestParam("key") String key);
}
