package com.exanmple.db;

public class CarMaintainItemBean {
    /** 数据库自增ID */
    public int id;
    /** 汽车名称 */
    public String name;
    /** 汽车保养项目 */
    public String item_name;
    /** 保养项目更换或维修周期公里数 */
    public int item_mileage_cycle;
    /** 保养项目更换或维修周期时间间隔int */
    public int item_time_cycle;

    /**
     * 转化成字符串
     * @return
     */
    public String toString(){
        StringBuilder builder = new StringBuilder("[");
        builder.append(id).append("--");
        builder.append("汽车名称：").append(name).append("--");
        builder.append("保养项目名称：").append(item_name).append("--");
        builder.append("公里数周期：").append(item_mileage_cycle).append("--");
        builder.append("时间周期：").append(item_time_cycle).append("]");
        return builder.toString();
    }
}
