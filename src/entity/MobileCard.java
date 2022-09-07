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
    public void showMeg() {
        System.out.println("卡号：" + this.cardNumber + "，用户名：" + this.userName + "，当前余额：" + this.money + "元");
        this.serPackage.showInfo();

    }

    public MobileCard() {
    }

    //卡号 用户名 密码 所属套餐  账户余额
    public MobileCard(String cardNumber, String userName, String passWord, ServicePackage serPackage, double money) {
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.passWord = passWord;
        this.serPackage = serPackage;
        this.money = money;
    }

    public MobileCard(String cardNumber, String userName, String passWord, ServicePackage serPackage, double consumAmount, double money, int realTalkTime, int realSMSCount, int realFlow) {
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.passWord = passWord;
        this.serPackage = serPackage;
        this.consumAmount = consumAmount;
        this.money = money;
        this.realTalkTime = realTalkTime;
        this.realSMSCount = realSMSCount;
        this.realFlow = realFlow;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public ServicePackage getSerPackage() {
        return serPackage;
    }

    public void setSerPackage(ServicePackage serPackage) {
        this.serPackage = serPackage;
    }

    public double getConsumAmount() {
        return consumAmount;
    }

    public void setConsumAmount(double consumAmount) {
        this.consumAmount = consumAmount;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getRealTalkTime() {
        return realTalkTime;
    }

    public void setRealTalkTime(int realTalkTime) {
        this.realTalkTime = realTalkTime;
    }

    public int getRealSMSCount() {
        return realSMSCount;
    }

    public void setRealSMSCount(int realSMSCount) {
        this.realSMSCount = realSMSCount;
    }

    public int getRealFlow() {
        return realFlow;
    }

    public void setRealFlow(int realFlow) {
        this.realFlow = realFlow;
    }
}
