package cn.ct.cloud.service.impl;

import cn.ct.cloud.mapper.MemberMapper;
import cn.ct.cloud.model.Member;
import cn.ct.cloud.model.MemberExample;
import cn.ct.cloud.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public int getLoginAcctCount(String loginAcct) {
        MemberExample memberExample=new MemberExample();
        memberExample.createCriteria().andLoginacctEqualTo(loginAcct);
        long i = memberMapper.countByExample(memberExample);
        return (int)i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMember(Member member) {
        try {
            memberMapper.insertSelective(member);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
}
