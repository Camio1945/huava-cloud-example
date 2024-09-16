package cn.huava.gateway.common.graalvm;

import cn.huava.gateway.GatewayApplication;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.remote.response.ServerCheckResponse;
import com.alibaba.nacos.common.constant.HttpHeaderConsts;
import com.alibaba.nacos.common.http.HttpRestResult;
import com.alibaba.nacos.common.http.param.Header;
import com.alibaba.nacos.common.model.RestResult;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.springframework.aot.hint.*;

/**
 * 为 GraalVM native image 注册资源和类。<br>
 * 当前类你理解为一个附属类，它在 {@link GatewayApplication} 的注解中用到。
 *
 * @author Camio1945
 */
@Slf4j
public class NativeRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(@NonNull RuntimeHints hints, ClassLoader classLoader) {
    registerResources(hints);
    registerClasses(hints);
  }

  protected void registerResources(@NonNull RuntimeHints hints) {
    Stream.of("*.yml", "*.yaml", "*.properties", "**/*.json")
        .forEach(hints.resources()::registerPattern);
  }

  protected void registerClasses(@NonNull RuntimeHints hints) {
    Set<Class<?>> classes = new HashSet<>();
    addMiscellaneousClasses(classes);
    addNacosClasses(classes);
    addHuavaClasses(classes);
    registerClasses(hints, classes);
  }

  protected void addMiscellaneousClasses(Set<Class<?>> classes) {
    Set<Class<?>> miscellaneousClasses =
        Set.of(
            // http response for gzip
            GZIPInputStream.class,
            // Java
            ArrayList.class,
            // Jackson
            ToStringSerializer.class);
    classes.addAll(miscellaneousClasses);
  }

  protected void addNacosClasses(Set<Class<?>> classes) {
    Set<Class<?>> nacosClasses =
        Set.of(
            ServerCheckResponse.class,
            HttpRestResult.class,
            RestResult.class,
            RestResult.ResResultBuilder.class,
            Header.class,
            Constants.class,
            HttpHeaderConsts.class);
    classes.addAll(nacosClasses);
  }

  protected void addHuavaClasses(Set<Class<?>> classes) {
    classes.addAll(ClassUtil.scanPackage("cn.huava").stream().toList());
    classes.addAll(ClassUtil.scanPackage("io.micrometer").stream().toList());
    classes.addAll(ClassUtil.scanPackage("io.netty").stream().toList());
    classes.addAll(ClassUtil.scanPackage("com.alibaba.nacos.api.grpc").stream().toList());
    classes.addAll(
        ClassUtil.scanPackage("com.alibaba.nacos.common.remote.client.grpc").stream().toList());
    classes.addAll(ClassUtil.scanPackage("com.alibaba.nacos.shaded.io").stream().toList());
    classes.addAll(ClassUtil.scanPackage("com.google").stream().toList());
  }

  private static void registerClasses(RuntimeHints hints, Set<Class<?>> classes) {
    classes.forEach(
        clazz -> {
          try {
            hints.reflection().registerType(clazz, MemberCategory.values());
          } catch (Error e) {
            log.warn("Failed to register class [{}]: {}", clazz.getName(), e.getMessage());
          }
        });
  }
}
