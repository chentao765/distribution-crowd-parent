package cn.ct.cloud.controller;

import cn.ct.cloud.dto.ResultDTO;
import cn.ct.cloud.model.Member;
import cn.ct.cloud.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;
    //根据用户账号判断账号的数量
    @RequestMapping("/retrieve/login/acct/count")
    ResultDTO<Integer> retrieveLoginAcctCount(@RequestParam("loginAcct") String loginAcct){
        //loginacct 判断之后传过来
        int count=0;
        try {
            count=memberService.getLoginAcctCount(loginAcct);
        } catch (Exception e) {
            e.printStackTrace();
            ResultDTO.failed(e.getMessage());
        }
        //返回结果
        return ResultDTO.successWithData(count);
    }

    //保存注册用户信息
    @RequestMapping("/save/member/remote")
    ResultDTO<String> saveMemberRemote(@RequestBody Member member){
        try {
            memberService.saveMember(member);
        } catch (Exception e) {
            e.printStackTrace();
            return  ResultDTO.failed(e.getMessage());
        }

        return ResultDTO.successNoData();
    }
}
