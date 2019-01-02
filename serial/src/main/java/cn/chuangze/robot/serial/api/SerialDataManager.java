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

            int var1 = -1;
            if (jsonObject.has("var1")) var1 = jsonObject.getInt("var1");
            int var2 = 0;
            switch (type) {
                case 0:
                    serialListener.onObstruct();//人体感应
                    break;
                case 1:
                    /**
                     * 1：按下
                     * 2：弹开
                     * 3：长按
                     */
                    if (jsonObject.has("var2")) var2 = jsonObject.getInt("var2");
                    switch (var1) {
                        case 1:
                            serialListener.onHeadTouched(var2);//触摸头
                            break;
                        case 2:
                            serialListener.onLeftHandTouched(var2);//左手触摸
                            break;
                        case 3:
                            serialListener.onRightHandTouched(var2);//右手触摸
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
                        case 3:
                            serialListener.shutDown();//关机
                            break;
                    }
                    break;

                case 3:
                    if (serialListener instanceof TestSerialListener) {
                        String var = jsonObject.getString("var2");
                        ((TestSerialListener) serialListener).onVersion(var1, var);
                    }

                    break;
                case 5:
                    String v = "";
                    if (jsonObject.has("var2")) v = jsonObject.getString("var2");

                    serialListener.onError(var1, v);
                    break;
                default:
                    if (serialListener instanceof TestSerialListener) {
                        ((TestSerialListener) serialListener).onDefault(data);
                    }
                    break;


            }

        } catch (Exception e) {
            if (serialListener instanceof TestSerialListener) {
                ((TestSerialListener) serialListener).onDefaultStr(data);
            }
            e.printStackTrace();
        }
    }


}
