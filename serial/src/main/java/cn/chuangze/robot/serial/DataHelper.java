package cn.chuangze.robot.serial;

import android.util.Log;

public class DataHelper {
    private final byte[] header = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC};
    private final byte[] footer = {(byte) 0xFF};//未校验

    private byte[] data = null;//数据

    private byte[] buff = null;//缓冲区

    private int dataLen; //数据内容区长度
    private int position = 0;//当前下标位置 第一个合法的头部数据位置
    private int hL = header.length; //头的长度 3
    private CallBack callBack;//解析成功回调

    private boolean stop = true;

    private boolean isCompress = true;//是不是需要新协议

    public void onDestroy(){
        stop = false;
    }
    public DataHelper(){}
    public DataHelper(boolean isCompress){
        this.isCompress = isCompress;

    }



    /**
     * @param bt 0xAA,0xBB,0xCC   0x01,               --0x00,0x06,      0x00,   0x00,0x00,0x00,--  0xEF,        0xFF
     *           <p>
     *           头部             数据类型(1文本，2二进制) 数据长度(--xxxx--) 分包预留  数据内容             校验位动态计算 结束位
     */
    public void parseData(byte[] bt) {
        if(!isCompress){
            callBack.call(new String(bt),true);
            return;
        }
        if (bt == null || bt.length <= 0) return;
        this.data = bt;

        /**
         * 判断缓冲区里面是否有数据 添加到当前数据
         */
        if (buff != null && buff.length > 0) {
            //   int index = 0;
            int l = buff.length;
            int len = bt.length + l;

            byte[] temp = new byte[len];


            System.arraycopy(buff, 0, temp, 0, buff.length);
            System.arraycopy(bt, 0, temp, buff.length, bt.length);


            this.data = temp;
            this.buff = null;

        }
        //  Log.e("DataHelper","(:51)"+bt.length);
//        Log.e("DataHelper","receive"+bytesToHexString(bt));
        //Log.e("DataHelper",bytesToHexString(bt));
        int p = 0;//当前位置

        //循环查找 符合数据 (p = checkHead(p)) < data.length
        while (stop) {
            p = checkHead(p);

            /**
             * 循环判断是否符合 头部信息
             */
            if (p < 0) {
                p = Math.abs(p);

                if (p > data.length) {
                    // Log.e("DataHelper","(:66)"+"no data correct");
                    return;
                }

                continue;
            }


            /**
             * 如果数组大小 <=2时候 并且两位头正确 把信息保存到缓冲区
             */
            if (p == data.length - 1 || p == data.length - 2) {

                if (count > 0) this.buff = cp(count, p);

                count = 0;

                return;
            }

            //判断当前数据长度是否符合 头部长度判断 包括数据位

            if ((p + hL) > data.length || (p + hL + 3) > data.length) {
                int size = data.length - p;
                this.buff = cp(size, p);
                return;
            }

            dataLen = getDataLen(p + hL);//获取数据大小

            if(dataLen < 4) continue;//小于4位数据格式不合法


            //判断数据位是否足够 1（一位数据类型  ） 1 校验位 1 结束位
            if (dataLen + p + hL + 1 + 1 + 1 > data.length) {
                int size = data.length - p;
                this.buff = cp(size, p);
                return;

            }

            int sumPosition = position + hL + dataLen + 1;//校验位位置
//            Log.e("DataHelper", "(:105)" + sumPosition);
            int dataTypePosition = position + hL;//获取到数据位
            byte[] temp = new byte[dataLen];
            for (int i = 0; i < dataLen; i++) {
                temp[i] = data[i + dataTypePosition + 1];//数据位取出来
            }


            boolean b = isCheck(data[sumPosition], temp);//校验位检查

            if (b) {
                Log.e("DataHelper", "校验成功");
                p += hL + 1 + dataLen + 1 + 1; //下标移动到 数据结束位 继续校验
                if (callBack != null) callBack.call(new String(temp, 3, dataLen - 3), true);

            } else {
                Log.e("DataHelper", "校验失败");
                p += hL; //下标移动到 下一个头位置 校验
            }


        }


    }


    /**
     * 检查头部
     *
     * @param position
     * @return
     */
    int count = 0;

    private int checkHead(int position) {
        int hLen = header.length;
        int size = data.length;

        int p = position;
        int offset = 0;
        count = 0;
        boolean isOk = false;
        for (int i = 0; i < hLen; i++) {

            for (int j = p; j < size; j++) {
                //找到第一个位置
                if (header[i] == data[j]) {
                    p = ++j;//赋值当前位置 第二次循环从下个一个位置开始
                    isOk = true;
                    offset++;
                    break;//退出循环找第二个位置
                } else {
                    isOk = false;
                }


            }
            if (!isOk) return (p - offset + 1) * -1;//返回新的的下标
            if (p >= size) break;//防止空循环

        }
        //当循环完成判断是否查找到位置如果找到返回当前下标减寻找头部的长度
        if (isOk) {
            if (offset != hLen) count = offset;
            return p - offset;
        }

        return -1;
    }

    private byte[] cp(int size, int p) {
        byte[] temp = new byte[size];

        for (int i = 0; i < size; i++) {

            temp[i] = data[p + i];
        }


        return temp;
    }

    private int getDataLen(int p) {
//        byte[] b = new byte[2];
//        b[0] = data[p + 1];
//        b[1] = data[p + 2];
//
//        Log.e("DataHelper",bytesToHexString(b));
//        return byte2Int(b);
        return byte2Int(data[p + 1]) * 256 + byte2Int(data[p + 2]);
    }

    public byte[] compress(byte[] bt, int packages, int dataType) {

        if(!isCompress) return bt;

        if (packages < 0) packages = 0;
        if (dataType < 0) dataType = 0;
        int l = bt.length;

        //头 3 数据类型 1 数据长度 2 保留位（分包序号）1 校验位 1 帧尾 1 数据长度 l
        byte[] buff = new byte[l + 3 + 1 + 2 + 1 + 1 + 1];
        //帧头
        buff[0] = header[0];
        buff[1] = header[1];
        buff[2] = header[2];
        //数据类型
        buff[3] = (byte) dataType;

        int ll = 3 + l;
        //数据长度

        buff[4] = (byte) (ll / 256);
        buff[5] = (byte) (ll % 256);
        //分包序号预留
        buff[6] = (byte) packages;


        for (int i = 0; i < l; i++) {
            buff[7 + i] = bt[i];
        }

        int check = (byte2Int(bt) + byte2Int(buff[4]) + byte2Int(buff[5]) + byte2Int(buff[6])) & 0xFF;


        buff[buff.length - 2] = (byte) check;//校验位
        buff[buff.length - 1] = (byte) 0xFF;//结束位
//        Log.e("DataHelper","send"+bytesToHexString(bt));
        return buff;
    }


    /**
     * 校验 和
     *
     * @param b    校验字节
     * @param buff 需要校验的数组
     * @return
     */
    private boolean isCheck(byte b, byte[] buff) {
       // Log.e("DataHelper", "(:244)" + (byte2Int(b) & 0xFF));
        //Log.e("DataHelper", "(:247)" + (byte2Int(buff) & 0XFF));

        return byte2Int(b) == (byte2Int(buff) & 0XFF);
    }


    public int byte2Int(byte[] buff) {
        int sum = 0;

        for (byte b : buff) {
            sum += b < 0 ? 256 + b : b;
        }
        return sum;
    }

    private int byte2Int(byte b) {

        return b < 0 ? 256 + b : b;
    }

    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void call(String json, boolean success);
    }


}
