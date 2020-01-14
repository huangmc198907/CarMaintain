package com.exanmple.db;

public class CarMaintainBean {
    /** 数据库自增ID */
    public int id;
    /** 汽车名称 */
    public String name;
    /** 汽车保养周期公里数 */
    public int maintain_mileage_cycle;
    /** 汽车保养周期时间 */
    public int maintain_time_cycle;

    public String icon_path;

    /**
     * 转化成字符串
     * @return
     */
    public String toString(){
        StringBuilder builder = new StringBuilder("[");
        builder.append(id).append("--");
        builder.append("汽车名称：").append(name).append("--");
        builder.append("公里数周期：").append(maintain_mileage_cycle).append("--");
        builder.append("时间周期：").append(maintain_time_cycle).append("--");
        builder.append("图标路径：").append(icon_path).append("]");
        return builder.toString();
    }
}
