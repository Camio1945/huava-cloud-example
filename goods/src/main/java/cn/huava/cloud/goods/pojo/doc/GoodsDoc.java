package cn.huava.cloud.goods.pojo.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * 商品文档（用于 ES ）<br>
 *
 * <pre>
 * 注：以下注解不起作用，因此才需要 使用 `@Mapping(mappingPath = "META-INF/es-mappings/goods.json")`
 * `@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")`
 * 但是两者是结合使用的，goods.json 文件中只包含 name 字段，因为它需要用到 IK 分词器，其他字段会自动创建 mapping
 * </pre>
 *
 * @author Camio1945
 */
@Data
@Document(indexName = "goods")
@Mapping(mappingPath = "META-INF/es-mappings/goods.json")
public class GoodsDoc {
  @Id private Long id;

  /** 商品名称 */
  private String name;

  /** 价格 */
  private BigDecimal price;

  /** 库存 */
  private Integer stock;

  /** 销量 */
  private Integer sales;

  /** 是否上架 */
  private Boolean isOn;

  /** 图片，英文逗号分隔 */
  private String imgs;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;
}
