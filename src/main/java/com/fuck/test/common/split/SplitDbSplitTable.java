package com.fuck.test.common.split;

import org.junit.Test;

public class SplitDbSplitTable {

    @Test
    public void test1(){
        for (int i = 0; i <10 ; i++) {
            Integer randomId = (int) (Math.random() * 10);
            System.out.println("randomId=" + randomId);
        }
    }

    /**
     * 分库分表目前默认使用的是取模算法，
     * 分表算法为 (#shard_key % (group_shard_num * table_shard_num))，
     * 分库算法为 (#shard_key % (group_shard_num * table_shard_num)) / table_shard_num，
     * 其中group_shard_num为分库个数，table_shard_num为每个库的分表个数。
     */
    @Test
    public void test2(){
        Integer group_shard_num = 8 ;
        Integer table_shard_num = 32;
        for (int i = 0; i <10 ; i++) {
            Integer shard_key = (int) (Math.random() * 10);
            System.out.println("shard_key=" + shard_key);
            Integer dbId = shard_key % (group_shard_num * table_shard_num);
            System.out.println("shard_key=" + shard_key + "dbId=" + dbId);

            Integer tableId = (shard_key % (group_shard_num * table_shard_num)) / table_shard_num;
            System.out.println("shard_key=" + shard_key + "dbId=" + dbId + "tableId=" + tableId);
        }

    }






}
