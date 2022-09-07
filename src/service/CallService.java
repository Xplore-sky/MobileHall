package service;

import entity.MobileCard;

//通话服务
public interface CallService {
    /**
     * 通话
     * @param minCount 通话分钟数
     * @param card     超出套餐内的通话时长时需消费哪张卡的余额
     */
    int call(int minCount, MobileCard card)throws Exception;

}

