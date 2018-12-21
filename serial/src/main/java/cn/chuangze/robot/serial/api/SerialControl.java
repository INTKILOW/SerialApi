package cn.chuangze.robot.serial.api;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.chuangze.robot.serial.DataHelper;
import cn.chuangze.robot.serial.SerialPort;
import cn.chuangze.robot.serial.api.listener.SerialListener;

public class SerialControl extends BaseSerial {

    private static SerialControl mSerialControl = null;

    private SerialListener mSerialListener = null;//事件回调


    private SerialPort mSerialPort = null;//串口通讯核心类

    private DataHelper mDataHelper;//解析数据帮助类


    private boolean mIsUpgrade = false;//升级时候关闭串口发送其他数据来保证数据完整性

    private SerialControl() {

        try {
            mSerialPort = new SerialPort(new File("/dev/" + SerialParams.DEVICE), SerialParams.BAUD_RATE, 0);
            mDataHelper = new DataHelper(true);
            mDataHelper.setCallBack(new DataHelper.CallBack() {
                @Override
                public void call(String json, boolean success) {
                    SerialDataManager.parseData(json, mSerialListener);
                }
            });
            mSerialPort.startListening(new SerialPort.OnReceiveListener() {
                @Override
                public void onReceived(byte[] data) {
                    mDataHelper.parseData(data);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SerialControl getInstance() {
        if (mSerialControl == null) {
            synchronized (SerialControl.class) {
                if (mSerialControl == null)
                    mSerialControl = new SerialControl();
            }

        }
        return mSerialControl;
    }

    /**
     * @param type 头 左手 右手
     * @param var1 参数
     * @return
     */
    private void sendCmdWithCompose(int type, int var1) {
        sendCmdWithCompose(type, var1, null);
    }

    /**
     * 发送数据
     *
     * @param bt 格式化的数据
     */
    private void sendData(byte[] bt) {
        if (bt != null && mSerialPort != null) mSerialPort.send(bt);
    }

    /**
     * 构造主控消息方法
     *
     *  https://docs.qq.com/sheet/DTE5UZmpWYU9sZVRacVVK?tab=BB08J2
     * @param type 见链接
     * @param var1 见链接
     * @param var2 见链接
     */
    public void sendCmdWithCompose(int type, Object var1, Object var2) {
        if (mSerialPort == null || type < 0) return;
        JSONObject jsonObject = new JSONObject();
        String cmd = "";
        try {
            jsonObject.put("type", type);
            if (var1 != null) jsonObject.put("var1", var1);
            if (var2 != null) jsonObject.put("var2", var2);
            cmd = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("SerialControl", cmd);
        //发送数据 数据不为空并且不再升级
        if (!TextUtils.isEmpty(cmd) && !mIsUpgrade) {
            sendData(mDataHelper.compress(cmd.getBytes(), 0, 1));
        }else{
            Log.e("SerialControl","数据为空 或者 正在升级!");
        }
    }

    /**
     * 发送自定义命令
     * @param bt 合成
     */
    public void customWithCompose(byte[] bt) {
        if(bt == null ) return;
        sendData(mDataHelper.compress(bt, 0, 1));
    }

    /**
     * 发送自定义命令
     * @param bt  不合成
     */
    public void customWithoutCompose(byte[] bt) {
        sendData(bt);
    }
    public void setSerialListener(SerialListener mSerialListener) {
        this.mSerialListener = mSerialListener;
    }

    @Override
    public void putLeftHandTop() {
        sendCmdWithCompose(BaseSerial.SEND_LEFT_HAND, 1);

    }

    @Override
    public void putLeftHandReturn() {
        sendCmdWithCompose(BaseSerial.SEND_LEFT_HAND, 2);
    }

    @Override
    public void putLeftHandLowest() {
        sendCmdWithCompose(BaseSerial.SEND_LEFT_HAND, 3);
    }

    @Override
    public void putLeftHandUp() {
        sendCmdWithCompose(BaseSerial.SEND_LEFT_HAND, 4);
    }

    @Override
    public void putLeftHandDown() {
        sendCmdWithCompose(BaseSerial.SEND_LEFT_HAND, 5);
    }

    @Override
    public void putRightHandTop() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 1);
    }

    @Override
    public void putRightHandReturn() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 2);
    }

    @Override
    public void putRightHandLowest() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 3);
    }

    @Override
    public void putRightHandUp() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 4);
    }

    @Override
    public void putRightHandDown() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 5);
    }

    @Override
    public void salute() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 6);
    }

    @Override
    public void handShake() {
        sendCmdWithCompose(BaseSerial.SEND_RIGHT_HAND, 7);
    }

    @Override
    public void embrace() {
        sendCmdWithCompose(BaseSerial.SEND_BOTH_HAND , 8);
    }

    @Override
    public void putBothHandTop() {
        sendCmdWithCompose(BaseSerial.SEND_BOTH_HAND, 1);
    }

    @Override
    public void putBothHandReturn() {
        sendCmdWithCompose(BaseSerial.SEND_BOTH_HAND, 2);
    }

    @Override
    public void putBothHandLowest() {
        sendCmdWithCompose(BaseSerial.SEND_BOTH_HAND, 3);
    }

    @Override
    public void putBothHandUp() {
        sendCmdWithCompose(BaseSerial.SEND_BOTH_HAND, 4);
    }

    @Override
    public void putBothHandDown() {
        sendCmdWithCompose(BaseSerial.SEND_BOTH_HAND, 5);
    }

    @Override
    public void rotateLeftHead(float angle) {
        sendCmdWithCompose(BaseSerial.SEND_HEAD, 2);
    }

    @Override
    public void rotateRightHead(float angle) {
        sendCmdWithCompose(BaseSerial.SEND_HEAD, 3);
    }

    @Override
    public void shakeHead() {
        sendCmdWithCompose(BaseSerial.SEND_HEAD, 1);
    }

    @Override
    public void expression(int type) {
        if (mSerialListener != null) mSerialListener.onExpression(type);

    }

    @Override
    public void wakeUp(int mic) {
        mic = mic < 0 ? 0 : mic;
        mic = mic > 5 ? 5 : mic;
        sendCmdWithCompose(BaseSerial.SEND_SWITCH, 6,mic);
    }

    @Override
    public void wakeUp() {
        sendCmdWithCompose(BaseSerial.SEND_SWITCH, 6,0);
    }
    @Override
    public void sleep() {
        sendCmdWithCompose(BaseSerial.SEND_SWITCH, 5);
    }

    @Override
    public void lampOn() {
        sendCmdWithCompose(BaseSerial.SEND_SWITCH, 4);
    }

    @Override
    public void lampOff() {
        sendCmdWithCompose(BaseSerial.SEND_SWITCH, 3);
    }

    @Override
    public int upGrade(String path) {
        mIsUpgrade = true;
        int code ;

        if (TextUtils.isEmpty(path)) {
            mIsUpgrade = false;
            return -4;
        }

        File f = new File(path);

        //判断文件是否存在
        if (!f.exists()){
            mIsUpgrade = false;
            return -3;
        }

        String name = f.getName();
        String suffix = name.substring(name.lastIndexOf(".") + 1);

        //判断文件类型是否符合
        if (!suffix.equals("bin")){
            mIsUpgrade = false;
            return -2;
        }

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(f);
            byte[] buff = new byte[2048];

            int len = -1;
            int packages = 0;

            while ((len = fis.read(buff)) != -1) {
                byte[] temp = new byte[len];
                System.arraycopy(buff, 0, temp, 0, len);

                byte[] data = mDataHelper.compress(temp, packages, 2);

                sendData(data);
                packages++;

            }
            fis.close();
            code = 0;
        } catch (IOException e) {
            e.printStackTrace();
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            code = -1;
        }

        mIsUpgrade = false;
        return code;

    }

    @Override
    public void buildSN(String sn) {

        if(sn.length()!=12) {
            Log.e("SerialControl","(:299)"+"sn 长度不正确");

            return;
        }
        sendCmdWithCompose(BaseSerial.SEND_SET_LAMP, 4, sn);
    }

    @Override
    public void buildLampColor(int mode, int r, int g, int b, int a) {

        r = r < 0 ? 0 : r;
        r = r > 255 ? 255 : r;

        g = g < 0 ? 0 : g;
        g = g > 255 ? 255 : g;

        b = b < 0 ? 0 : b;
        b = b > 255 ? 255 : b;

        a = a < 0 ? 0 : a;
        a = a > 100 ? 100 : a;


        String rgba = Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b) + Integer.toHexString(a);

        buildLampColor(mode, rgba);
    }

    @Override
    public void buildLampColor(int mode, String rgba) {

        if (rgba.startsWith("0x")) rgba = rgba.substring(2, rgba.length());
        long l = Long.parseLong(rgba, 16);
        sendCmdWithCompose(BaseSerial.SEND_SET_LAMP, mode, l);

    }

    @Override
    public void switchLampColor(int mode) {
        sendCmdWithCompose(BaseSerial.SEND_CHANGE_LAMP_MODE, mode);

    }
}
