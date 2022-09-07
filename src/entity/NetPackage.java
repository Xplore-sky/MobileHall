package entity;

import service.NetService;


/**
 * 网虫套餐
 */
public class NetPackage extends ServicePackage implements NetService{
    private int flow; //上网流量
    public NetPackage() {
        this.flow=5*1024;
        super.setPrice(68);
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    @Override
    public void showInfo() {
        System.out.println("话痨套餐: 通话时长为"+0+"分钟/月,短信条数为"+0+"条/月,上网流量为"+(this.flow/1024.0)+"GB/月");

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
}
