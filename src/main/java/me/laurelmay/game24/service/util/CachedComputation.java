package me.laurelmay.game24.service.util;

import java.util.function.IntSupplier;

public class CachedComputation {

  private final IntSupplier supplier;
  private Integer cachedResult;

  private CachedComputation(IntSupplier supplier) {
    this.supplier = supplier;
  }

  public static CachedComputation computedWith(IntSupplier supplier) {
    return new CachedComputation(supplier);
  }

  public int get() {
    return cachedResult == null ? (cachedResult = supplier.getAsInt()) : cachedResult;
  }
}
