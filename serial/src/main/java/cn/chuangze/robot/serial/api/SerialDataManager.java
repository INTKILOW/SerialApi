package cn.chuangze.robot.serial.api;

import android.text.TextUtils;

import org.json.JSONObject;

import cn.chuangze.robot.serial.api.listener.SerialListener;
import cn.chuangze.robot.serial.api.listener.TestSerialListener;


/**
 * @Description 串口数据处理
 * @Author wj
 * @Time 2017/7/18 15:37
 */

public class SerialDataManager {

    /**
     * 解析串口数据，并回调触摸事件
     *
     * @param data
     * @param serialListener
     */
    public static void parseData(String data, SerialListener serialListener) {
        if (TextUtils.isEmpty(data) || serialListener == null)
            return;
        try {
            JSONObject jsonObject = new JSONObject(data);
            int type = -1;
            if (jsonObject.has("type")) type = jsonObject.getInt("type");
            if (type != -1) {
                int var1 = -1;
                if (jsonObject.has("var1")) var1 = jsonObject.getInt("var1");
                int var2 = 0;
                if (var1 != -1) {
                    switch (type) {
                        case 0:
                            serialListener.onObstruct();//人体感应
                            break;
                        case 1:
                            switch (var1) {
                                case 1:
                                    serialListener.onHeadTouched();//触摸头
                                    break;
                                case 2:
                                    serialListener.onLeftHandTouched();//左手触摸
                                    break;
                                case 3:
                                    serialListener.onRightHandTouched();//右手触摸
                                    break;
                            }
                            break;
                        case 2:
                            switch (var1) {
                                case 1:
                                    if (jsonObject.has("var2")) var2 = jsonObject.getInt("var2");
                                    serialListener.onWake(var2);//唤醒
                                    break;
                                case 2:
                                    serialListener.onSleep();//休息
                                    break;
                            }
                            break;

                        case 3:
                            if (serialListener instanceof TestSerialListener) {
                                String var = jsonObject.getString("var2");
                                ((TestSerialListener) serialListener).onVersion(var1, var);
                            }

                            break;
                        default:
                            if (serialListener instanceof TestSerialListener) {
                                ((TestSerialListener) serialListener).onDefault(data);
                            }
                            break;


                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
