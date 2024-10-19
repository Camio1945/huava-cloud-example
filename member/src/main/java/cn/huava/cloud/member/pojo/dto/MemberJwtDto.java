package cn.huava.cloud.member.pojo.dto;

import lombok.Data;

/**
 * 用户 token 信息
 *
 * @author Camio1945
 */
@Data
public class MemberJwtDto {
  private String accessToken;
  private String refreshToken;
}
