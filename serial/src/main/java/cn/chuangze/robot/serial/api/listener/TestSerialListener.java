package cn.chuangze.robot.serial.api.listener;

/**
 * 此接口用于 机器人测试app
 */
public interface TestSerialListener extends SerialListener {

    /**
     * 获取版本信息硬件
     *
     * @param id      id
     * @param version 版本
     */
    void onVersion(int id, String version);




    /**
     * 默认的数据回调
     * @param json 数据
     */
    void onDefault(String json);



}
