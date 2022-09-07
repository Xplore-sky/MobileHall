package entity;

import service.CallService;
import service.NetService;
import service.SendService;

/**
 * 超人套餐
 */
public class SuperPackage extends ServicePackage implements CallService, SendService , NetService {
    private int talkTime; //通话时长
    private int smsCount; //可发送短信条数
    private int flow; //上网流量

    public int getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(int talkTime) {
        this.talkTime = talkTime;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public SuperPackage() {
        this.talkTime=200;
        this.smsCount=100;
        this.flow=1*1024;
        super.setPrice(78);
    }

    @Override
    public void showInfo() {
        System.out.println("超人套餐：通话时长为"+this.talkTime+"分钟/月,短信条数为："+this.smsCount+"条/月，上网流量为"+(this.flow/1024)+"GB/月");
    }
    @Override
    public int call(int minCount, MobileCard card) throws Exception {
        // 重写通话接口功能 获得套餐使用详情
        int temp = 0;// 实际消耗分钟数
        // 循环判断使用详情
        for (int i = 0; i < minCount; i++) {
            // 第一种情况 套餐余额充足还可支持1分钟通话
            if (this.getTalkTime() - card.getRealTalkTime() >= 1) {
                card.setRealTalkTime(card.getRealTalkTime() + 1);// 实际通话数据+1
                temp++;
            } else if (card.getMoney() >= 0.2) {
                // 情况二：套餐通话时长已经用完，但是账户余额还可以支持1分钟通话，直接使用账户余额支付
                card.setRealTalkTime(card.getRealTalkTime() + 1);// 实际使用通话时长分钟+1
                temp++;
                // 剩余金额减少0.2元
                card.setMoney(card.getMoney() - 0.2);
                // 总消费增加0.2元
                card.setConsumAmount(card.getConsumAmount() + 0.2);
            } else {
                try {
                    throw new Exception("本次已通话" + temp + "分钟，您的余额已不足，请充值后在使用！");
                } catch (Exception e){
                e.printStackTrace();
            } finally {
                // 报错结束返回一个实际通话时长
                return temp;
            }
        }
    }
        return temp;// 返回一个实际通话时长
}

    @Override
    public int netPlay(int flow, MobileCard card) throws Exception {
        // 重写上网接口功能 获得套餐使用详情
        int temp = 0;// 实际消耗流量数据
        // 循环判断使用详情
        for (int i = 0; i < flow; i++) {
            // 第一种情况 套餐余额充足还可以使用1MB的流量
            if (this.getFlow() - card.getRealFlow() >= 1) {
                card.setRealFlow(card.getRealFlow() + 1);
                ;// 实际流量数据+1
                temp++;
            } else if (card.getMoney() >= 0.1) {
                // 情况二：套餐剩余流量已经用完，但是账户余额还可以支持使用1MB流量，直接使用账户余额支付
                card.setRealSMSCount(card.getRealSMSCount() + 1);// 实际流量数据+1
                temp++;
                // 剩余金额减少0.1元
                card.setMoney(card.getMoney() - 0.1);
                // 总消费增加0.1元
                card.setConsumAmount(card.getConsumAmount() + 0.1);
            } else {
                try {
                    throw new Exception("本次已使用" + temp + "MB的流量，您的余额已不足，请充值后在使用！");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 报错结束返回一个实际流量数据
                    return temp;
                }
            }
        }
        return temp;// 返回一个实际流量数据
    }

    @Override
    public int send(int count, MobileCard card) throws Exception {
        // 重写短信接口功能 获得套餐使用详情
        int temp = 0;// 实际消耗短信次数
        // 循环判断使用详情
        for (int i = 0; i < count; i++) {
            // 第一种情况 套餐余额充足还可支持发送1次短信
            if (this.getSmsCount() - card.getRealSMSCount() >= 1) {
                card.setRealSMSCount(card.getRealSMSCount() + 1);// 实际短信数据+1
                temp++;
            } else if (card.getMoney() >= 0.1) {
                // 情况二：套餐短信次数已经用完，但是账户余额还可以支持发一次短信，直接使用账户余额支付
                card.setRealSMSCount(card.getRealSMSCount() + 1);// 实际短信数据+1
                temp++;
                // 剩余金额减少0.1元
                card.setMoney(card.getMoney() - 0.1);
                // 总消费增加0.1元
                card.setConsumAmount(card.getConsumAmount() + 0.1);
            } else {
                try {
                    throw new Exception("本次已发送" + temp + "次短信，您的余额已不足，请充值后在使用！");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 报错结束返回一个实际短信次数
                    return temp;
                }
            }
        }
        return temp;// 返回一个实际短信次数
    }

}

