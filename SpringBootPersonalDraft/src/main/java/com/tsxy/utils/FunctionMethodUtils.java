package com.tsxy.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author Liu_df
 * @Date 2023/11/30 15:19
 */
public class FunctionMethodUtils {

    ExecutorService executor = Executors.newFixedThreadPool(6);

    public Future<?> executorInspect(InspectListSupplier supplier,
                                      InspectListFunction<List<String>, List<String>> function,
                                      List<String> result){
        return executor.submit(() -> {
            try {
                List<String> old = supplier.get();
                function.apply(result, old);
            } catch (Exception e) {
                System.out.println("抓到异常0.0");
            }
        });
    }

    @FunctionalInterface
    public interface InspectListSupplier {
        List<String> get() throws Exception;
    }

    @FunctionalInterface
    public interface InspectListFunction<T, R> {
        void apply(T result, R old);
    }


}
