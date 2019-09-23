package cn.ct.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装登录数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo {
    private String loginacct;

    private String userpswd;

    private String phone;

    private String randomCode;


}
