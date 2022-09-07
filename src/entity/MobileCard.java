package entity;

/**
 * 嗖嗖移动卡
 */
public class MobileCard {
    private String cardNumber; //卡号
    private String userName; //用户名
    private String passWord; //密码
    private ServicePackage serPackage; //所属套餐
    private double consumAmount; //当月消费金额
    private double money;  //账户余额
    private int realTalkTime; //当月实际通话时长
    private int realSMSCount; //当月实际发送短信条数
    private int realFlow; //当月实际上网流量
    //展示卡号信息
    public void showMeg(){
        System.out.println("卡号："+this.cardNumber+"，用户名："+this.userName+"，当前余额："+this.money+"元");
        this.serPackage.showInfo();

    }
