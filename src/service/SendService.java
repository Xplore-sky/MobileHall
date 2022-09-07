package service;

import entity.MobileCard;

//短信服务
public interface SendService {
    /**
     * 发短信
     * @param count 短信数
     * @param card  超过套餐范围短信需要使用哪张卡付费
     */
    int send(int count, MobileCard card)throws Exception;
}

