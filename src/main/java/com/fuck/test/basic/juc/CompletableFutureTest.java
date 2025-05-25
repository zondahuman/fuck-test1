package com.fuck.test.basic.juc;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureTest {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5);

        List<CompletableFuture<Integer>> futureList = list.parallelStream().map(value -> CompletableFuture.supplyAsync(
                () -> {
                    return value;
         })).collect(Collectors.toList());

        CompletableFuture<Integer> listFuture = CompletableFuture.allOf().thenApplyAsync(
                value -> {
                   int sum = futureList.stream().mapToInt(CompletableFuture::join).sum();
                   return sum;
                });
        int sum = listFuture.join();
        System.out.println(sum);
    }

}
