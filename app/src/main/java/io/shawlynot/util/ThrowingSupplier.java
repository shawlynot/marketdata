package io.shawlynot.util;


import java.util.function.Supplier;

public interface ThrowingSupplier<T> {

    T get() throws Exception;

    static <T> T wrap(ThrowingSupplier<T> s) {
        try {
            return s.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
