package com.fuck.test.midware.hbase;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;
import org.junit.Test;

public class RowKeyUtil {

    public static String getRowKey(String driverId, String logStatus, String logType, long timestamp) {
        Preconditions.checkNotNull(driverId, "driverId 不能为空");
        Preconditions.checkArgument(StringUtils.equals(logStatus, "1") || StringUtils.equals(logStatus, "2"), "状态只能为1, 2");
        Preconditions.checkArgument(StringUtils.equals(logStatus, "1") || StringUtils.equals(logStatus, "2"), "类型只能为1, 2");
        Preconditions.checkArgument(Long.valueOf(timestamp).toString().length() == 13, "时间戳长度必须为13");

//        String salt = MD5Hash.getMD5AsHex(Bytes.toBytes(driverId)).substring(0, 5);
        long ts = Long.MAX_VALUE / 1000000 - timestamp;

        return driverId + logStatus + logType + ts;

    }

    public static String getSaltRowKey(String driverId, String logStatus, String logType, long timestamp) {
        Preconditions.checkNotNull(driverId, "driverId 不能为空");
        Preconditions.checkArgument(StringUtils.equals(logStatus, "1") || StringUtils.equals(logStatus, "2"), "状态只能为1, 2");
        Preconditions.checkArgument(StringUtils.equals(logStatus, "1") || StringUtils.equals(logStatus, "2"), "类型只能为1, 2");
        Preconditions.checkArgument(Long.valueOf(timestamp).toString().length() == 13, "时间戳长度必须为13");

        String salt = MD5Hash.getMD5AsHex(Bytes.toBytes(driverId)).substring(0, 5);
        System.out.println("salt=" + salt);
        long ts = Long.MAX_VALUE / 1000000 - timestamp;

        return salt + driverId + logStatus + logType + ts;

    }


    public static void main(String[] args) {
        String driverId = "123456789";
        String logStatus = "0";
        String logType = "auditLogType";
        long timestamp = System.currentTimeMillis();
        long ts = Long.MAX_VALUE / 1000000 - timestamp;

        String key = getSaltRowKey(driverId, logStatus, logType, timestamp);

    }

    @Test
    public void test1() {
        String driverId = "123456789";
        String logStatus = "1";
        String logType = "2";
        long timestamp = System.currentTimeMillis();
        long ts = Long.MAX_VALUE / 1000000 - timestamp;

        String key = getSaltRowKey(driverId, logStatus, logType, timestamp);
        System.out.println("key=" + key+",length="+key.length());
    }



    

}
