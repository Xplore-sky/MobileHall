package service;

import entity.MobileCard;

//上网服务
public interface NetService {
    /**
     * 上网
     * @param flow 上网流量
     * @param card 超出套餐流量部分需要使用哪张卡余额
     */
    int netPlay(int flow, MobileCard card)throws Exception;
}

