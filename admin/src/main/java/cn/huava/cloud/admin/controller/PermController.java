package cn.huava.cloud.admin.controller;

import cn.huava.cloud.admin.mapper.PermMapper;
import cn.huava.cloud.admin.pojo.dto.PermDto;
import cn.huava.cloud.admin.pojo.po.PermPo;
import cn.huava.cloud.admin.service.perm.AcePermService;
import java.util.List;

import cn.huava.cloud.common.controller.BaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 权限控制器
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/perm")
public class PermController extends BaseController<AcePermService, PermMapper, PermPo> {

  /** Uses in menu page */
  @GetMapping("/auth/getAll")
  public ResponseEntity<List<PermDto>> getAll(boolean excludeElement) {
    return ResponseEntity.ok(service.getAllPerm(excludeElement));
  }
}
