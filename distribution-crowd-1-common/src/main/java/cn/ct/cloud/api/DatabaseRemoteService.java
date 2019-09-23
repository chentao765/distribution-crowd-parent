package cn.ct.cloud.api;

import cn.ct.cloud.dto.ResultDTO;
import cn.ct.cloud.model.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("database-provider")
public interface  DatabaseRemoteService {
    //根据用户账号判断账号的数量
    @RequestMapping("/retrieve/login/acct/count")
    ResultDTO<Integer> retrieveLoginAcctCount(@RequestParam("loginAcct") String loginAcct);


    //保存注册用户信息
    @RequestMapping("/save/member/remote")
    ResultDTO<String> saveMemberRemote(@RequestBody Member member);

    //根据LoginAcct查对象
    @RequestMapping("/retrieve/member/by/login/acct")
    public ResultDTO retrieveMemberByLoninAcctRemote(@RequestParam("loginAcct") String loginAcct);
}
