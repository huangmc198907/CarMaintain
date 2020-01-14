package com.exanmple.db;

public class MyCarMaintainRecordBean {
    /** 数据库自增ID */
    public int id;
    /** 汽车名称 */
    public String name;
    /** 车牌号 */
    public String license_plate;
    /** 汽车保养项目 */
    public String item_name;
    /** 保养项目更换或维修公里数 */
    public int item_mileage;
    /** 保养项目更换或维修时间 */
    public String item_time;

    /**
     * 转化成字符串
     * @return
     */
    public String toString(){
        StringBuilder builder = new StringBuilder("[");
        builder.append(id).append("--");
        builder.append("汽车名称：").append(name).append("--");
        builder.append("车牌号：").append(license_plate).append("--");
        builder.append("保养项目：").append(item_name).append("--");
        builder.append("保养公里数：").append(item_mileage).append("--");
        builder.append("保养时间：").append(item_time).append("]");
        return builder.toString();
    }
}
