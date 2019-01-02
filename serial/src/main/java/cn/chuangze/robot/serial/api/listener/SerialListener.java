package cn.chuangze.robot.serial.api.listener;

/**
 * 此接口用于机器人 app
 */
public interface SerialListener {

    /**
     * 休眠
     */
    void onSleep();

    /**
     * 语音唤醒 回调
     *
     * @param angle 唤醒角度  0-360
     */
    void onWake(int angle);

    /**
     * 关机命令
     */
    void shutDown();

    /**
     *
     * 1：按下
     * 2：弹开
     * 3：长按
     * 左手被触摸回调
     */
    void onLeftHandTouched(int type);

    /**
     *
     * 1：按下
     * 2：弹开
     * 3：长按
     * 右手被触摸回调
     */
    void onRightHandTouched(int type);

    /**
     *
     * 1：按下
     * 2：弹开
     * 3：长按
     * 头部被触摸回调
     */
    void onHeadTouched(int type);


    /**
     * 出错事件
     * @param var1 类型
     * @param var2 原因
     */
    void onError(int var1,String var2);

    /**
     * 人体感应
     */
    void onObstruct();


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
     *             默认 default
     *             <p>
     *             <p>
     *             Intent intent = new Intent();
     *             intent.setPackage("com.cz.robot.expression");
     *             intent.setAction("changeImage");
     *             intent.putExtra("type", type);
     *             context.sendBroadcast(intent);
     */
    void onExpression(int type);
}
