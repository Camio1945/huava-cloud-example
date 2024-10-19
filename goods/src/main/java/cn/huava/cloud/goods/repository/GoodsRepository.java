package cn.huava.cloud.goods.repository;

import cn.huava.cloud.goods.pojo.doc.GoodsDoc;
import java.util.List;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 商品 Repository 持久化层（存入 ES ）
 *
 * @author Camio1945
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<GoodsDoc, Long> {

  // List<GoodsDoc> findByAuthorName(String authorName);
  //
  // Optional<GoodsDoc> findByIsbn(String isbn);
}
