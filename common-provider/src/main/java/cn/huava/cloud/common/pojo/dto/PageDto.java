package cn.huava.cloud.common.pojo.dto;

import java.util.List;
import lombok.Data;

/**
 * 分页结果 DTO
 *
 * @author Camio1945
 */
@Data
public class PageDto<T> {
  private List<T> list;
  private long count;

  public PageDto() {}

  public PageDto(List<T> list, long count) {
    this.list = list;
    this.count = count;
  }
}
