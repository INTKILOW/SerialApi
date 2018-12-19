package cn.chuangze.robot.serial;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPort {

    private static final String TAG = "SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    boolean flag = true;

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

        //检查访问权限，如果没有读写权限，进行文件操作，修改文件访问权限
        if (!device.canRead() || !device.canWrite()) {
            try {
                //通过挂在到linux的方式，修改文件的操作权限
                Process su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudrate, flags);

        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }

        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }





    /**
     * 开启串口监听
     *
     * @param listener
     */
    public void startListening(final OnReceiveListener listener) {

        new Thread() {
            @Override
            public void run() {

                while (flag) {
                    int size;
                    try {
                        byte[] buffer = new byte[2048];
                        if (mFileInputStream == null)
                            return;
                        synchronized (this) {
                            size = mFileInputStream.read(buffer);
                        }

                        if (size > 0) {
                            byte[] data = new byte[size];
                            System.arraycopy(buffer, 0, data, 0, size);
                            if (listener != null) {
                                listener.onReceived(data);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    /**
     * 关闭监听
     */
    public void stopListening() {
        flag = false;

        if (mFileInputStream != null) {
            try {
                mFileInputStream.close();
                mFileInputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mFileOutputStream != null) {
            try {
                mFileOutputStream.close();
                mFileOutputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 向串口发送数据
     *
     * @param data
     */
    public void send(String data) {
        send(data.getBytes());
    }

    /**
     * 向串口发送数据
     *
     * @param data
     */
    public void send(final byte[] data) {
        // 将发生数据的方法放到线程内部
        new Thread() {
            @Override
            public void run() {
                try {
                    if (mFileOutputStream != null) {
                        mFileOutputStream.write(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 串口数据回调接口
     */
    public interface OnReceiveListener {
        void onReceived(byte[] data);
    }

    // JNI(调用java本地接口，实现串口的打开和关闭)
/**串口有五个重要的参数：串口设备名，波特率，检验位，数据位，停止位
 其中检验位一般默认位NONE,数据位一般默认为8，停止位默认为1*/
    /**
     * @param path     串口设备的绝对路径
     * @param baudrate 波特率
     * @param flags    校验位
     */
    public native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();


    static {//加载jni下的C文件库
        System.loadLibrary("native-lib");
    }
}
