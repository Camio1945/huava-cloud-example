package cn.huava.cloud.member.controller;

import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.common.validation.Create;
import cn.huava.cloud.member.pojo.dto.MemberJwtDto;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.pojo.po.RegisterPo;
import cn.huava.cloud.member.pojo.qo.DecreaseBalanceQo;
import cn.huava.cloud.member.pojo.qo.LoginQo;
import cn.huava.cloud.member.service.member.AceMemberService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/mbr/member")
public class MemberMbrController {
  private final AceMemberService memberService;

  @PostMapping("/free/login")
  public ResponseEntity<MemberJwtDto> login(
      @NonNull final HttpServletRequest req, @RequestBody @NonNull final LoginQo loginQo) {
    return ResponseEntity.ok(memberService.login(req, loginQo));
  }

  @PostMapping("/free/register")
  public ResponseEntity<MemberJwtDto> register(
      @NonNull final HttpServletRequest req,
      @RequestBody @NonNull @Validated({Create.class}) final RegisterPo registerPo) {
    return ResponseEntity.ok(memberService.register(req, registerPo));
  }

  @GetMapping("/auth/get/{id}")
  public ResponseEntity<MemberPo> getById(@PathVariable @NonNull final Long id) {
    return ResponseEntity.ok(memberService.getById(id));
  }

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
