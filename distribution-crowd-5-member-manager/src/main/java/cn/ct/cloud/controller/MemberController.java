package cn.ct.cloud.controller;

import cn.ct.cloud.api.DatabaseRemoteService;
import cn.ct.cloud.api.RedisOpeatorRemoteServic;
import cn.ct.cloud.dto.ResultDTO;
import cn.ct.cloud.model.Member;
import cn.ct.cloud.utils.CrowdConstant;
import cn.ct.cloud.utils.CrownUtils;
import cn.ct.cloud.vo.MemberSignSuccessVO;
import cn.ct.cloud.vo.MemberVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    private RedisOpeatorRemoteServic redisOpeatorRemoteServic;

    @Autowired
    private DatabaseRemoteService databaseRemoteService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //发送code
    @RequestMapping("/member/send/code")
    public ResultDTO<String> sendCode(@RequestParam("phone") String phone){
        //判断手机号是否合法
        if(!phone.matches("^(13|15|18)\\d{9}$")){
            return ResultDTO.failed(CrowdConstant.PHONENUM_IS_INVALID);
        }

        //生成验证码
        int length=4;
        String code=CrownUtils.randomCode(length);
        int timeOutMinute=15;

        //验证码保存到redis中
        ResultDTO resultDTO=redisOpeatorRemoteServic.saveNormalStringKeyValue(CrowdConstant.REDIS_RANDOM_CODE_PRIFIX+phone,code
        ,timeOutMinute);
        if(resultDTO.getResult()==ResultDTO.FAILED){
            return resultDTO;
        }

        //发送验证码
        CrownUtils.sendShortMessage(CrowdConstant.appCode,code,phone);

      return  ResultDTO.successWithData("成功");
    }

    //用户注册
    @RequestMapping("/member/register")
    public ResultDTO<String> register(@RequestBody MemberVo memberVo){
        //1 判断数据是否为空
        if(StringUtils.isBlank(memberVo.getLoginacct())){
             return  ResultDTO.failed(CrowdConstant.LOGINACCT_IS_NULL);
        }

        if(StringUtils.isBlank(memberVo.getUserpswd())){
            return ResultDTO.failed(CrowdConstant.PASSWROD_IS_NULL);
        }

        if(!memberVo.getPhone().matches("^(13|15|18)\\d{9}$")){
            return ResultDTO.failed(CrowdConstant.PHONENUM_IS_INVALID);
        }

        if(StringUtils.isBlank(memberVo.getRandomCode())){
            return ResultDTO.failed(CrowdConstant.RANDOMCODE_IS_NULL);
        }

        //2.取出redis中验证码 并验证用户码是否合法
        ResultDTO<String> redisCoderesultDTO = redisOpeatorRemoteServic.retrieveStringValueByStringKey(CrowdConstant.REDIS_RANDOM_CODE_PRIFIX + memberVo.getPhone());
        if(redisCoderesultDTO.getResult()==ResultDTO.FAILED){
            return  redisCoderesultDTO;
        }

        String redisCode=redisCoderesultDTO.getData();

        //3.对比验证码
        if(!redisCode.equals(memberVo.getRandomCode())){
            return ResultDTO.failed(CrowdConstant.RANDOMCODE_IS_ERROR);
        }

        //4.验证账号是否被占用
        ResultDTO<Integer> AcctResultDTO = databaseRemoteService.retrieveLoginAcctCount(memberVo.getLoginacct());
        if(AcctResultDTO.getResult().equals(ResultDTO.FAILED)){
            return ResultDTO.failed(AcctResultDTO.getMessage());
        }

        Integer AcctCount = AcctResultDTO.getData();
        if(AcctCount>=1){
            return  ResultDTO.failed(CrowdConstant.LOGINACCOUNT_IS_USED);
        }

        //5.对密码进行加密

        String newPassword=bCryptPasswordEncoder.encode(memberVo.getUserpswd());
        memberVo.setUserpswd(newPassword);

        //6.存入数据库

        Member member=new Member();
        BeanUtils.copyProperties(memberVo,member);
        databaseRemoteService.saveMemberRemote(member);
        return ResultDTO.successWithData("注册成功");
    }

    //执行具体的登录方法
    @RequestMapping("/member/login")
    public ResultDTO<MemberSignSuccessVO> login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd){
        //1.验证用户名密码是否空
        if(StringUtils.isBlank(loginacct)){
            return  ResultDTO.failed(CrowdConstant.LOGINACCT_IS_NULL);
        }

        if(StringUtils.isBlank(userpswd)){
            return ResultDTO.failed(CrowdConstant.PASSWROD_IS_NULL);
        }

        //2.根据用户名查用户
        ResultDTO resultDTO = databaseRemoteService.retrieveMemberByLoninAcctRemote(loginacct);
        if(resultDTO.getResult()==ResultDTO.FAILED){
            return resultDTO;
        }

        Member member= (Member) resultDTO.getData();
        if(member==null){
            return ResultDTO.failed(CrowdConstant.LOGINACCOUTORPASSWROD_IS_ERROR);
        }

        //3.比对密码
        if(!bCryptPasswordEncoder.encode(member.getUserpswd()).equals(member.getUserpswd())){
            return ResultDTO.failed(CrowdConstant.PASSWORD_IS_ERROR);
        }

        //4.生成token
        String token = CrownUtils.generatorToken();

        //5.把token放入redis
        Integer memberId=member.getId();
        redisOpeatorRemoteServic.saveTokenOfSignedMemberRemote(token,memberId);

        //返回MemberSignSuccessVO
        MemberSignSuccessVO memberSignSuccessVO=new MemberSignSuccessVO();
        BeanUtils.copyProperties(member,memberSignSuccessVO);
        memberSignSuccessVO.setToken(token);

        return  ResultDTO.successWithData(memberSignSuccessVO);
    }

    //退出登录：删除redis
    @RequestMapping("/member/loginout")
    public ResultDTO<String> loginOut(String token){
        if(StringUtils.isBlank(token)){
            return  ResultDTO.failed(CrowdConstant.TOKEN_IS_NULL);
        }
        ResultDTO<String> resultDTO = redisOpeatorRemoteServic.removeByKey(token);
        if(resultDTO.getResult()==ResultDTO.FAILED){
            return  resultDTO;
        }
        return  ResultDTO.successWithData("退出成功");


    }


}
