package me.laurelmay.game24.service.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SortedListKeyGenerator implements KeyGenerator {
  @Override
  public Object generate(Object target, Method method, Object... params) {
    List<Object> sortedParams = new ArrayList<>();
    for (Object param : params) {
      if (param instanceof List<?> list) {
        sortedParams.add(list.stream().sorted().toList());
      } else {
        sortedParams.add(param);
      }
    }
    return new SimpleKeyGenerator().generate(target, method, sortedParams.toArray());
  }
}
