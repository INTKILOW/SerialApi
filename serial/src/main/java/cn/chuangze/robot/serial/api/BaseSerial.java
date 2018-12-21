package cn.chuangze.robot.serial.api;


public abstract class BaseSerial {

    /**
     * 充电
     */
    public static final int LAMP_MODE_CHARGING = 3;

    /**
     * 正常
     */
    public static final int LAMP_MODE_NORMAL = 1;

    /**
     * 低电量
     */
    public static final int LAMP_MODE_LOW_BATTERY = 2;

    /**
     * 打开关闭设备
     */
    public static final  int SEND_SWITCH = 0;
    /**
     * 头部
     */
    public static final  int SEND_HEAD = 1;
    /**
     * 右手
     */
    public static final  int SEND_RIGHT_HAND = 2;
    /**
     * 左手
     */
    public static final  int SEND_LEFT_HAND = 3;
    /**
     * 双手
     */
    public static final  int SEND_BOTH_HAND = 4;
    /**
     * 查询
     */
    public static final  int SEND_QUERY = 5;
    /**
     * 设置
     */
    public static final  int SEND_SET_LAMP = 6;
    /**
     * 切换
     */
    public static final  int SEND_CHANGE_LAMP_MODE = 7;






    //--------------------------------左手操作--------------------------------

    /**
     * 左手最高
     */
    public abstract void putLeftHandTop();


    /**
     * 左手回位
     */
    public abstract void putLeftHandReturn();


    /**
     * 左手最低
     */
    public abstract void putLeftHandLowest();


    /**
     * 抬起左手
     */
    public abstract void putLeftHandUp();


    /**
     * 放下左手
     */
    public abstract void putLeftHandDown();

    //--------------------------------左手操作--------------------------------


    //--------------------------------右手操作--------------------------------


    /**
     * 右手最高
     */
    public abstract void putRightHandTop();


    /**
     * 右手回位
     */
    public abstract void putRightHandReturn();


    /**
     * 右手最低
     */
    public abstract void putRightHandLowest();


    /**
     * 抬起右手
     */
    public abstract void putRightHandUp();


    /**
     * 放下右手
     */
    public abstract void putRightHandDown();


    /**
     * 敬礼
     */
    public abstract void salute();


    /**
     * 握手
     */
    public abstract void handShake();


    /**
     * 拥抱
     */
    public abstract void embrace();


    //--------------------------------右手操作--------------------------------

    //--------------------------------双手操作--------------------------------

    /**
     * 双手最高
     */
    public abstract void putBothHandTop();


    /**
     * 双手回位
     */
    public abstract void putBothHandReturn();


    /**
     * 双手最低
     */
    public abstract void putBothHandLowest();


    /**
     * 抬起双手
     */
    public abstract void putBothHandUp();


    /**
     * 放下双手
     */
    public abstract void putBothHandDown();


    //--------------------------------双手操作--------------------------------

    //--------------------------------头操作--------------------------------

    /**
     * 头向左转
     *
     * @param angle 旋转的角度
     */
    public abstract void rotateLeftHead(float angle);

    /**
     * 头向右转
     *
     * @param angle 旋转的角度
     */
    public abstract void rotateRightHead(float angle);

    /**
     * 摇头
     */
    public abstract void shakeHead();


    //--------------------------------头操作--------------------------------

    /**
     * 唤醒
     * @param mic 需要加强的mic
     */
    public abstract void wakeUp(int mic);


    /**
     * 唤醒 默认第一个mic加强
     */
    public abstract void wakeUp();

    /**
     * 休眠
     */
    public abstract void sleep();


    /**
     * 切换表情
     *
     * @param type 类型
     *             微笑 smile 1
     *             说话 speak 2
     *             惊讶 surprised 3
     *             哭 cry 4
     *             难过 sad 5
     *             喜欢 like 6
     *             晕 gosh 7
     *             休息 rest 8
     *             亲吻 kiss 9
     *             卖萌 cute 10
     *             调皮 naughty 11
     *             委屈 grievance 12
     *             惊喜 shocker 13
     *             闭嘴 silent 14
     *             生气 angry 15
     *             害羞 shy 16
     *             叹气 sigh 17
     *             尴尬 awkward 18
     *             <p>
     *             默认 default
     */
    public abstract void expression(int type);


    /**
     * 打开灯带
     */
    public abstract void lampOn();

    /**
     * 关闭灯带
     */
    public abstract void lampOff();


    /**
     * 升级 单片机
     * xxx.bin
     * @param path 文件路径
     * @return 返回错误码
     * -1 其他错误
     * -2 文件类型不符合
     * -3 文件不存在
     * -4 文件路径为空
     *  0 写入完成
     */
    public abstract int upGrade(String path);


    /**
     * 设置sn号码
     * @param sn 长度12的字符串
     */
    public abstract void buildSN(String sn);


    /**
     * 设置灯带颜色
     * @param mode
     * LAMP_MODE_CHARGING = 3;//充电
     * LAMP_MODE_NORMAL = 1;//正常
     * LAMP_MODE_LOW_BATTERY = 2;//低电量
     * @param r 0~255
     * @param g 0~255
     * @param b 0~255
     * @param a 0~255 亮度
     */
    public abstract void buildLampColor(int mode,int r,int g,int b,int a);


    /**
     * 设置灯带颜色
     * @param mode
     * LAMP_MODE_CHARGING = 3;//充电
     * LAMP_MODE_NORMAL = 1;//正常
     * LAMP_MODE_LOW_BATTERY = 2;//低电量
     * @param rgba 0xFF FF FF FF (R G B A)
     */
    public abstract void buildLampColor(int mode,String rgba);


    /**
     * 切换灯带颜色
     * @param mode  模式
     * LAMP_MODE_CHARGING = 3;//充电
     * LAMP_MODE_NORMAL = 1;//正常
     * LAMP_MODE_LOW_BATTERY = 2;//低电量
     */
    public abstract void switchLampColor(int mode);




}
