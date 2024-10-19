package cn.huava.cloud.goods.service.goods;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;
import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.range;

import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.goods.mapper.GoodsMapper;
import cn.huava.cloud.goods.pojo.doc.GoodsDoc;
import cn.huava.cloud.goods.pojo.po.GoodsPo;
import cn.huava.cloud.goods.pojo.qo.SearchGoodsQo;
import cn.huava.cloud.goods.util.Fn;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.*;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

/**
 * 商品查询（查 ES）<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class GoodsPageMbrService extends BaseService<GoodsMapper, GoodsPo> {
  private final ElasticsearchTemplate elasticsearchTemplate;

  protected List<GoodsDoc> goodsPage4Mbr(
      @NonNull PageQo<GoodsPo> pageQo, @NonNull SearchGoodsQo params) {
    Query query = getQuery(params);
    Sort sort = getSort(pageQo);
    Pageable pageable = PageRequest.of((int) (pageQo.getCurrent() - 1), (int) pageQo.getSize());
    NativeQuery nativeQuery =
        NativeQuery.builder().withQuery(query).withPageable(pageable).withSort(sort).build();
    return elasticsearchTemplate.search(nativeQuery, GoodsDoc.class).stream()
        .map(SearchHit::getContent)
        .toList();
  }

  private static Query getQuery(@NotNull SearchGoodsQo params) {
    Function<BoolQuery.Builder, ObjectBuilder<BoolQuery>> function =
        builder -> {
          List<Query> queries = new ArrayList<>();
          addQueryByName(params, queries);
          addQueryByPrice(params, queries);
          return builder.must(queries);
        };
    return QueryBuilders.bool(function);
  }

  private static void addQueryByPrice(@NotNull SearchGoodsQo params, List<Query> queries) {
    if (params.getStartPrice() == null && params.getEndPrice() == null) {
      return;
    }
    queries.add(
        range(
            build -> {
              build.field("price");
              if (params.getStartPrice() != null) {
                build.gte(JsonData.of(params.getStartPrice()));
              }
              if (params.getEndPrice() != null) {
                build.lte(JsonData.of(params.getEndPrice()));
              }
              return build;
            }));
  }

  private static void addQueryByName(@NotNull SearchGoodsQo params, List<Query> queries) {
    if (Fn.isNotBlank(params.getName())) {
      queries.add(match(build -> build.field("name").query(params.getName())));
    }
  }

  @NotNull
  private static Sort getSort(@NotNull PageQo<GoodsPo> pageQo) {
    String orderBy = pageQo.getOrderBy();
    List<Sort.Order> orderList = new ArrayList<>();
    if (orderBy != null && !orderBy.isEmpty()) {
      String[] orderByArray = orderBy.replace("'", "").replace(" ", "").split(",");
      for (String orderByItem : orderByArray) {
        boolean isAsc = !orderByItem.startsWith("-");
        String fieldName = orderByItem.replace("-", "");
        orderList.add(new Sort.Order(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, fieldName));
      }
    }
    return Sort.by(orderList);
  }
}
