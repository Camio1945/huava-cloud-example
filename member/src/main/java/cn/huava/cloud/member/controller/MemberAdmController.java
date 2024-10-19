package cn.huava.cloud.member.controller;

import cn.huava.cloud.member.pojo.qo.DecreaseBalanceQo;
import cn.huava.cloud.member.service.member.AceMemberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 会员控制器
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/adm/member")
public class MemberAdmController {
  private final AceMemberService memberService;

  @PostMapping("/auth/decreaseBalance")
  public ResponseEntity<Void> decreaseBalance(
      @RequestBody @NonNull @Validated final DecreaseBalanceQo decreaseBalanceQo) {
    memberService.decreaseBalance(decreaseBalanceQo);
    return ResponseEntity.ok(null);
  }

  @PostMapping("/auth/replenishBalance")
  public ResponseEntity<Void> replenishBalance(
      @RequestBody @NonNull @Validated final Long memberId) {
    memberService.replenishBalance(memberId);
    return ResponseEntity.ok(null);
  }
}
