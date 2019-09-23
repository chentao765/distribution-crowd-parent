package cn.ct.cloud.service.api;

import cn.ct.cloud.model.Member;

public interface  MemberService {

    //查询用户
    int getLoginAcctCount(String loginAcct);

    //保存用户
    void saveMember(Member member);
}
