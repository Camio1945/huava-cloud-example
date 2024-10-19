package cn.huava.cloud.member.cache;

import cn.huava.cloud.common.util.*;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.util.Fn;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.*;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户缓存
 *
 * @author Camio1945
 */
@Service
@RequiredArgsConstructor
public class MemberCache {
  public static final String MEMBER_ID_CACHE_PREFIX = "cache:member:id";
  public static final String MEMBER_USERNAME_CACHE_PREFIX = "cache:member:username";

  private final MemberMapper memberMapper;

  /**
   * 1. Don't add `final` key word, otherwise will get circular dependency error. <br>
   * 2. Don't use it directly, use {@link #getMemberCache()} instead. <br>
   * 3. Don't delete it and use `this`, otherwise the cache will not work. <br>
   */
  private MemberCache memberCache;

  /**
   * 根据 id 获取用户
   *
   * @param id 用户 id
   * @return 用户
   */
  public MemberPo getById(@NonNull Long id) {
    String key = "str:" + MEMBER_ID_CACHE_PREFIX + "::" + id;
    String jsonStr = SingleFlightUtil.execute("getMemberFromRedis" + key, () -> RedisUtil.get(key));
    if (Fn.isBlank(jsonStr)) {
      MemberPo member = SingleFlightUtil.execute("getMemberFromMySQL" + key, () -> memberMapper.selectById(id));
      RedisUtil.set(key, JSONUtil.toJsonStr(member), RedisUtil.randomOffsetDurationInSeconds());
      return member;
    } else {
      return JSONUtil.toBean(jsonStr, MemberPo.class);
    }
  }

  public Long getIdByUsername(@NonNull String membername) {
    String strId = getMemberCache().getStrIdByUsername(membername);
    return strId == null ? null : Long.parseLong(strId);
  }

  /**
   * 根据用户名获取用户 id
   *
   * @param membername 用户名
   * @return 用户 id , has to be type String, not Long, otherwise will get an Exception: <br>
   *     java.lang.ClassCastException: class java.lang.Integer cannot be cast to class
   *     java.lang.Long
   */
  @Cacheable(value = MEMBER_USERNAME_CACHE_PREFIX, key = "#membername")
  public String getStrIdByUsername(@NonNull String membername) {
    String key = MEMBER_USERNAME_CACHE_PREFIX + "::" + membername;
    return SingleFlightUtil.execute(
        key,
        () -> {
          LambdaQueryWrapper<MemberPo> wrapper = Fn.undeletedWrapper(MemberPo::getDeleteInfo);
          MemberPo memberPo =
              memberMapper.selectOne(
                  wrapper.eq(MemberPo::getUsername, membername).select(MemberPo::getId));
          return memberPo == null ? null : memberPo.getId().toString();
        });
  }

  /**
   * 删除缓存
   *
   * @param memberId
   */
  public void deleteById(@NonNull Long memberId) {
    RedisUtil.delete(MEMBER_ID_CACHE_PREFIX + "::" + memberId);
  }

  /**
   * 新增或修改操作后的缓存处理
   *
   * @param after 已经保存到数据库之后的用户
   */
  public void afterSaveOrUpdate(MemberPo after) {
    deleteKeys(after);
  }

  /**
   * 删除操作后的缓存处理
   *
   * @param before 删除前的用户
   */
  public void afterDelete(MemberPo before) {
    deleteKeys(before);
  }

  /**
   * 更新操作后的缓存处理
   *
   * @param before 更新前的用户
   */
  public void beforeUpdate(MemberPo before) {
    RedisUtil.delete(MEMBER_USERNAME_CACHE_PREFIX + "::" + before.getUsername());
  }

  private MemberCache getMemberCache() {
    if (memberCache != null) {
      return memberCache;
    }
    memberCache = SingleFlightUtil.execute("memberCache", () -> Fn.getBean(MemberCache.class));
    return memberCache;
  }

  private void deleteKeys(MemberPo member) {
    String[] keys = {
      MEMBER_ID_CACHE_PREFIX + "::" + member.getId(),
      MEMBER_USERNAME_CACHE_PREFIX + "::" + member.getUsername()
    };
    RedisUtil.delete(keys);
  }
}
