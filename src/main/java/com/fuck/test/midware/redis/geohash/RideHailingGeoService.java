package com.fuck.test.midware.redis.geohash;
import redis.clients.jedis.resps.GeoRadiusResponse;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.GeoRadiusParam;
import com.google.gson.Gson;
import java.util.*;

/**
 *
 * https://juejin.cn/post/7523256725128232986
 */
public class RideHailingGeoService {
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final String VEHICLE_GEO_KEY = "free_vehicles";

    private final Jedis jedis;
    private final Gson gson;

    public RideHailingGeoService() {
        this.jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        this.gson = new Gson();
    }

    // 添加或更新空闲车辆位置
    public void updateVehicleLocation(String vehicleId, double longitude, double latitude) {
        // 使用GEOADD命令添加或更新车辆位置
        jedis.geoadd(VEHICLE_GEO_KEY, longitude, latitude, vehicleId);
    }

    // 移除空闲车辆（当车辆被占用时）
    public void removeVehicle(String vehicleId) {
        // GeoHash底层使用Sorted Set，通过ZREM删除成员
        jedis.zrem(VEHICLE_GEO_KEY, vehicleId);
    }

    // 查询附近空闲车辆
    public List<Vehicle> findNearbyVehicles(double longitude, double latitude, double radius) {
        // 使用GEORADIUS命令查询指定范围内的车辆
        GeoRadiusParam param = GeoRadiusParam.geoRadiusParam()
                .withDist()          // 返回距离
                .sortAscending()      // 按距离升序排序
                .count(20);           // 限制返回数量

        List<GeoRadiusResponse> responses = jedis.georadius(
                VEHICLE_GEO_KEY,
                longitude,
                latitude,
                radius,
                GeoUnit.KM,
                param
        );

        List<Vehicle> vehicles = new ArrayList<>();
        for (GeoRadiusResponse response : responses) {
            String member = response.getMemberByString();
            double distance = response.getDistance();

            // 在实际应用中，可以从其他存储获取车辆详细信息
            Vehicle vehicle = new Vehicle(member, distance);
            vehicles.add(vehicle);
        }

        return vehicles;
    }

    // 车辆信息类
    public static class Vehicle {
        private String id;
        private double distance; // 距离（公里）

        public Vehicle(String id, double distance) {
            this.id = id;
            this.distance = distance;
        }

        // Getters
        public String getId() { return id; }
        public double getDistance() { return distance; }
    }

    // 关闭Redis连接
    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        RideHailingGeoService service = new RideHailingGeoService();

        // 模拟添加空闲车辆
        Random random = new Random();
        for (int i = 1; i <= 50; i++) {
            // 生成北京范围内的随机位置
            double longitude = 116.3 + random.nextDouble() * 0.5;
            double latitude = 39.8 + random.nextDouble() * 0.4;
            service.updateVehicleLocation("京A" + (10000 + i), longitude, latitude);
        }

        // 模拟乘客位置（北京西站）
        double passengerLongitude = 116.327058;
        double passengerLatitude = 39.89491;

        // 查询附近10公里内的空闲车辆
        List<Vehicle> nearbyVehicles = service.findNearbyVehicles(
                passengerLongitude, passengerLatitude, 10);

        // 输出结果
        System.out.println("附近空闲车辆（按距离排序）：");
        System.out.println("=========================================");
        System.out.printf("%-10s %-10s%n", "车辆ID", "距离(公里)");
        System.out.println("-----------------------------------------");
        for (Vehicle vehicle : nearbyVehicles) {
            System.out.printf("%-10s %-10.2f%n", vehicle.getId(), vehicle.getDistance());
        }
        System.out.println("=========================================");
        System.out.println("找到 " + nearbyVehicles.size() + " 辆空闲车辆");

        service.close();
    }
}




