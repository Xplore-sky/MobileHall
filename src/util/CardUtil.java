package util;

import entity.*;
//import jdk.swing.interop.SwingInterOpUtils;
import service.CallService;
import service.NetService;
import service.SendService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 工具类
 */
public class CardUtil {
    //已注册用户列表
    private  Map<String, MobileCard> cards=new HashMap<>();
    //所有卡号消费记录列表
    private  Map<String, List<ConsumInfo>> consumInfos=new HashMap<>();
    //使用场景列表
    private List<Scene> scenes = new ArrayList<>();

    Scanner sc=new Scanner(System.in);
    /**
     * 初始化场景
     * */
    public void initScene(){
        //初始化场景
        scenes.add(new Scene("通话", 90, "问候客户，谁知其如此难缠，通话90分钟"));
        scenes.add(new Scene("通话", 30, "询问妈妈身体状况，本地通话30分钟"));
        scenes.add(new Scene("短信", 5, "参与环境保护实施方案问卷调查，发送短信5条"));
        scenes.add(new Scene("短信", 50, "通知朋友手机换号，发送短信50条"));
        scenes.add(new Scene("上网", 1024, "和女朋友用微信视频聊天，使用流量1GB"));
        scenes.add(new Scene("上网", 2 * 1024, "晚上手机在线看韩剧，不留神睡着啦！使用2GB"));
    }

    /**
     * 初始化
     * 电话卡  使用记录
     */
    public void init(){
        //话痨
        MobileCard card1=new MobileCard("13901234567", "小陈", "123456", new TalkPackage(), 58,30,600,30,0 );
        //网虫
        MobileCard card2=new MobileCard("13908765431", "小许", "987654", new NetPackage(), 68,200,0,0,0);
        //超级
        MobileCard card3=new MobileCard("13092322791", "小薛", "123123", new SuperPackage(), 78,300,0,0,0);
        cards.put(card1.getCardNumber(),card1);
        cards.put(card2.getCardNumber(),card2);
        cards.put(card3.getCardNumber(),card3);

        //初始化消费记录列表

        //卡1消费记录
        List<ConsumInfo> c1= new ArrayList<>();
        c1.add(new ConsumInfo(card1.getCardNumber(),"通话",100));
        card1.setRealTalkTime(card1.getRealTalkTime()+100);
        consumInfos.put(card1.getCardNumber(),c1);
        //卡2消费记录
        List<ConsumInfo> c2= new ArrayList<>();
        c2.add(new ConsumInfo(card2.getCardNumber(),"上网",1024));
        card2.setRealFlow(card2.getRealFlow()+1024);
        consumInfos.put(card2.getCardNumber(),c2);
        //消费记录卡3
        List<ConsumInfo> c3= new ArrayList<>();
        c3.add(new ConsumInfo(card3.getCardNumber(),"通话",100));
        c3.add(new ConsumInfo(card3.getCardNumber(),"上网",1024));
        c3.add(new ConsumInfo(card3.getCardNumber(),"发短信",20));
        card3.setRealTalkTime(card3.getRealTalkTime()+100);
        card3.setRealFlow(card3.getRealFlow()+1024);
        card3.setRealSMSCount(card3.getRealSMSCount()+20);
        consumInfos.put(card3.getCardNumber(),c3);

    }


    /**
     * 注册新卡
     * @param card  注册卡号
     */
    public void addCard(MobileCard card){
        //创建选择业务对象 抽象类不能new
        ServicePackage sp;
        //创造数组接收随机生成的手机号
        String counts[]=getNewNumbers(9);
        System.out.println("************可选择的卡号**************");
        //打印随机手机号
        for (int i = 0; i < counts.length; i++) {
            System.out.print((i+1)+"."+counts[i]+"\t\t");
            //输出三次换行
            if((i+1)%3==0){
                System.out.println();
            }
        }
        System.out.print("请选择卡号:");
        int cardNum=sc.nextInt();
        //判断选择正确性
        while ((cardNum-1)>=counts.length||(cardNum-1)<0){
            System.out.print("请输入正确的序号:");
            cardNum=sc.nextInt();
        }
        System.out.println("请选择套餐:1.话痨套餐(58元/月) 2.网虫套餐(68元/月) 3.超人套餐(78元/月)(请输入序号):");
        int choice=sc.nextInt();
        //判断选择正确性
        while (choice<=0||choice>3){
            System.out.println("请输入正确的套餐序号");
            choice=sc.nextInt();
        }
        //获取指定套餐 1.话痨套餐 2.网虫套餐 3.超人套餐
        switch (choice){
            case 1:
                sp=new TalkPackage();
                break;
            case 2:
                sp=new NetPackage();
                break;
            case 3:
                sp=new SuperPackage();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
        System.out.print("请输入姓名:");
        String name=sc.next();
        System.out.print("请输入密码:");
        String password=sc.next();
        System.out.print("请输入预存花费金额:");
        double money=sc.nextDouble();
        //将基本信息添加到手机卡对象中    //卡号 用户名 密码 所属套餐  账户余额
        card=new MobileCard(counts[cardNum - 1], name, password, sp, ( money-sp.getPrice()));
        //循环判断预存金额是否充足 不足则重新赋值
        while(card.getMoney()<0){
            System.out.print("您预存的花费金额不足以支付本月固定套餐资费,请重新充值:");
            money=sc.nextDouble();
            card.setMoney(money-sp.getPrice());
        }
        System.out.println("注册成功!");
        //展示信息
        card.showMeg();

        //添加到集合中
        cards.put(card.getCardNumber(),card);
    }

    /**
     * 话费充值
     * @param number  卡号
     * @param money   充值金额
     */
    public void chargeMoney(String number,double money){
        //判断卡号是否存在
        if(cards.get(number)!=null){
            //最低充值50
            if(money>=50){
                cards.get(number).setMoney(cards.get(number).getMoney()+money);
                System.out.println("充值成功!当前的话费余额为:"+cards.get(number).getMoney()+"元");
            }else{
                System.out.println("最低消费50!");
            }
        }else {
            System.out.println("该手机号不存在");
        }

    }

    //使用嗖嗖(模拟手机消费)
    public void userSoso(String number)throws Exception{
        //通过手机号获取手机卡对象
        MobileCard card=cards.get(number);
        //随机产生场景
        Random rand=new Random();
        int temp=0; //各场景的实际消费数据
        //遍历判断手机卡所有场景的功能类型
        while(true){
            //获取0-5的随机数
            int randNum=rand.nextInt(6);
            //根据随机数创建场景(场景在上面已初始化)
            Scene scene=scenes.get(randNum);
            switch (randNum){
                case 0:
                case 1:
                    //通话 判断该手机卡是否包含语音套餐(网虫套餐无)
                    if(card.getSerPackage() instanceof CallService){
                        //展示场景
                        System.out.println(scene.getDescription()+"\t");
                        //获得实际消费数据  scene中data消费数据即为call方法的通话分钟数
                        temp=((CallService) card.getSerPackage()).call(scene.getData(),card);
                        //添加消费记录
                        //创建消费记录对象,数据是实际消费数据(不是场景中消费数据)
                        ConsumInfo info=new ConsumInfo(number,scene.getType(),temp);
                        //修改电话卡当月实际通话数据
                        card.setRealTalkTime(card.getRealTalkTime()+temp);
                        //添加消费记录
                        addConsumInfo(number,info);
                        break;
                    }
                    continue;
                case 2:
                case 3:
                    //短信场景 判断手机卡对象的套餐是否包含短信业务
                    if(card.getSerPackage() instanceof SendService){
                        //展示场景
                        System.out.println(scene.getDescription()+"\t");
                        //获得实际消费记录 scene中data为send中短信条数
                        temp=((SendService) card.getSerPackage()).send(scene.getData(),card);
                        //添加消费记录
                        //创建消费记录对象,数据是实际消费数据(不是场景中消费数据)
                        ConsumInfo info=new ConsumInfo(number,scene.getType(),temp);
                        //修改电话卡当月实际短信条数
                        card.setRealSMSCount(card.getRealSMSCount()+temp);
                        addConsumInfo(number,info);
                        break;
                    }
                    continue;
                case 4:
                case 5:
                    //上网  判断手机卡对象的套餐是否包含上网业务
                    if(card.getSerPackage() instanceof NetService){
                        //展示场景
                        System.out.println(scene.getDescription()+"\t");
                        //获得实际消费数据 此时scene中data为netplay的流量
                        temp=((NetService)card.getSerPackage()).netPlay(scene.getData(),card);
                        //添加消费记录
                        ConsumInfo info=new ConsumInfo(number,scene.getType(),temp);
                        //修改电话卡当月实际上午数据
                        card.setRealFlow(card.getRealFlow()+temp);
                        addConsumInfo(number,info);
                        break;
                    }
                    continue;
            }
            //退出while循环
            break;
        }
    }


    //资费说明  创建套餐资费说明文件
    public void showDescription()throws IOException {
        //创建FileWriter对象
        FileWriter fw=new FileWriter("src/serviceInfo.txt");
        //创建StringBuffer接收需要写入的信息
        StringBuffer sb=new StringBuffer();
        sb.append("套餐类型：话唠套餐\r\n" + "通话时长：500分钟 \r\n" + "短信条数：30条\r\n" + "月资费：58元\r\n" + "——————————————\r\n"
                + "套餐类型：网虫套餐\r\n" + "上网流量：3GB\r\n" + "月资费：68元\r\n" + "——————————————\r\n" + "套餐类型：超人套餐\r\n"
                + "通话时长：200分钟 \r\n" + "短信条数：50条\r\n" + "上网流量：1GB\r\n" + "月资费：78元\r\n" + "——————————————\r\n"
                + "超出套餐计费：\r\n" + "通话时长：0.2元/分钟\r\n" + "短信条数：0.1元/分钟\r\n" + "上网流量：0.1元/分钟\r\n"
                + "————————————————\r\n");
        fw.write(sb.toString());
        fw.flush();
        //创建FileReader对象
        FileReader fr=new FileReader("src/serviceInfo.txt");
        //创建BufferReader对象接收字符串
        BufferedReader br=new BufferedReader(fr);
        //循环打印到控制台
        String line=br.readLine();
        while(line!=null){
            System.out.println(line);
            line=br.readLine();
        }
        try {
            br.close();
            fr.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //本月账单查询
    public void showAmountDetail(String number){
        //根据手机号查找
        MobileCard card=cards.get(number);
        //判断是否存在
        if(card!=null){
            //创建StringBuffer类 便于字符串拼接
            StringBuffer sb = new StringBuffer("您的卡号:" + number + "的当月账单:\n");
            sb.append("套餐资费:"+card.getSerPackage().getPrice()+"元\n");
            sb.append("合计:"+card.getConsumAmount()+"元\n");
            sb.append("账户余额:"+card.getMoney()+"元。");
            System.out.println(sb);
        }else{
            System.out.println("该手机卡号不存在!");
        }
    }
    //套餐余量查询
    public void showRemainDetail(String number){
        //根据手机号查找
        MobileCard card=cards.get(number);
        //判断是否存在
        if(card!=null) {
            //创建StringBuffer类 便于字符串拼接
            StringBuffer sb = new StringBuffer("您的卡号是：" + number + "\n套餐内剩余：");
            // 获取卡中的真实消费数据和业务数据比较，判定
            ServicePackage serPackage = card.getSerPackage();
            // 判断是哪种套餐
            if (serPackage instanceof SuperPackage) {
                //向下转型获取超人套餐信息和功能
                SuperPackage pack = (SuperPackage) serPackage;
                //获取套餐余量
                int minusTalkData = pack.getTalkTime() - card.getRealTalkTime();
                int minusSMSData = pack.getSmsCount() - card.getRealSMSCount();
                int minusFlowData = pack.getFlow() - card.getRealFlow();
                sb.append("\n通话时长:" + (minusTalkData > 0 ? minusTalkData + "分钟" : "0分钟"));
                sb.append("\n短信条数:" + (minusSMSData > 0 ? minusSMSData + "条" : "0条"));
                sb.append("\n上网流量:" + (minusFlowData > 0 ? minusFlowData/1024 + "GB" : "0GB"));
            } else if (serPackage instanceof TalkPackage) {
                // 向下转型获取话痨套餐信息和功能
                TalkPackage pack = (TalkPackage) serPackage;
                // 获取套餐余量
                int minusTalkData = pack.getTalkTime() - card.getRealTalkTime();
                int minusSMSData = pack.getSmsCount() - card.getRealSMSCount();
                sb.append("\n通话时长：" + (minusTalkData > 0 ? minusTalkData + "分钟" :  "0分钟"));
                sb.append("\n短信条数：" + (minusSMSData > 0 ? minusSMSData + "条" : "0条"));
            } else if (serPackage instanceof NetPackage) {
                // 向下转型获取话痨套餐信息和功能
                NetPackage pack = (NetPackage) serPackage;
                /// 获取套餐余量
                int minusFlowData = pack.getFlow() - card.getRealFlow();
                sb.append("\n上网流量：" + (minusFlowData > 0 ? minusFlowData / 1024 + "GB" :"0GB"));
            }
            System.out.println(sb);
        } else {
            System.out.println("该手机卡号不存在！");
        }
    }
    //打印消费账单
    public void printAmountDetail(String number){
        //判断手机号是否存在
        if(cards.get(number)!=null){
            //打印消费记录到文本文件中 文件名:手机号+消费记录
            try {
                FileWriter fw=new FileWriter(number+"消费记录.txt");

                //创建StringBuffer写入消费记录
                StringBuffer sb=new StringBuffer("***********************"+number+"的消费记录***********************");
                sb.append("\n序号\t类型\t数据(通话(分钟)/上网(MB)/短信(条))\n");
                // 获取消费记录集合
                List<ConsumInfo> infoList=consumInfos.get(number);
                //判断集合是否为空
                if(infoList!=null){
                    //遍历消费记录
                    for (int i = 0; i < infoList.size(); i++) {
                        sb.append((i+1)+".\t"+infoList.get(i).getType()+"\t"+infoList.get(i).getConsumData()+"\n");
                    }
                    System.out.println("打印完成");
                }else{
                    System.out.println("该卡暂无消费记录");
                }
                // 写入文件
                fw.write(sb.toString());
                fw.flush();
                fw.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("改手机号不存在");
        }
    }



    /**
     * 套餐变更
     * @param number  卡号
     */
    public void changingPack(String number){
        //创建一个业务套餐对象 抽象类无法实例化(判断卡号类型向下转型)
        ServicePackage sp;
        //判断卡号是否存在
        if(cards.get(number)!=null){
            System.out.println("**********************");
            System.out.println("1、话痨套餐\t2、网虫套餐\t3、超人套餐  请选择(序号):");
            int choice=sc.nextInt();
            //判断输入正误
            while (choice<=0||choice>3){
                System.out.print("请输入正确的序号");
                choice=sc.nextInt();
            }
            //判断项目类型
            switch (choice){
                case 1:
                    //话痨套餐
                    sp=new TalkPackage();
                    //判断现有套餐是否与要更改套餐相同
                    if(cards.get(number).getSerPackage() instanceof TalkPackage){
                        System.out.println("对不起,您已经是改套餐用户,无需更改");
                    }else{
                        //判断剩余金额是否足以支付套餐费用
                        if(cards.get(number).getMoney()>sp.getPrice()){
                            //更改套餐
                            cards.get(number).setSerPackage(sp);
                            //余额减去套餐费用
                            cards.get(number).setMoney(cards.get(number).getMoney()-sp.getPrice());
                            //当月消费金额加上套餐费
                            cards.get(number).setConsumAmount(cards.get(number).getConsumAmount() + sp.getPrice());
                            System.out.println("更改套餐成功!");
                            //展示套餐信息
                            sp.showInfo();
                        }else {
                            System.out.println("余额不足,无法更改套餐");
                        }
                    }
                    break;
                case 2:
                    //网虫套餐
                    sp=new NetPackage();
                    //判断现有套餐和要更改套餐是否相同
                    if(cards.get(number).getSerPackage() instanceof NetService){
                        System.out.println("您已经是改套餐用户,无需更改");
                    }else{
                        //判断余额要大于套餐费用
                        if(cards.get(number).getMoney()>sp.getPrice()){
                            //更改套餐
                            cards.get(number).setSerPackage(sp);
                            //余额减去套餐费
                            cards.get(number).setMoney(cards.get(number).getMoney()-sp.getPrice());
                            //月消费加上套餐费
                            cards.get(number).setConsumAmount(cards.get(number).getConsumAmount()+sp.getPrice());
                            System.out.println("套餐修改成功");
                        }else{
                            System.out.println("余额不足,无法更改套餐");
                        }
                    }
                    break;
                case 3:
                    //超人套餐
                    sp=new SuperPackage();
                    //判断已有套餐和要修改套餐是否相同
                    if(cards.get(number).getSerPackage() instanceof SuperPackage){
                        System.out.println("您已经是该套餐用户,无法修改");
                    }else {
                        //判断余额是否足以修改套餐
                        if(cards.get(number).getMoney()>sp.getPrice()){
                            //更改套餐
                            cards.get(number).setSerPackage(sp);
                            //更改余额
                            cards.get(number).setMoney(cards.get(number).getMoney()-sp.getPrice());
                            //更改月消费
                            cards.get(number).setConsumAmount(cards.get(number).getConsumAmount()+sp.getPrice());
                        }else{
                            System.out.println("余额不足,无法更改套餐");
                        }
                    }
                    break;
            }
        }else{
            System.out.println("该手机号不存在");
        }
    }

    /**
     * 办理退网
     * @param number 卡号
     */
    public void delCard(String number){
        //判断集合中卡号是否存在
        if(cards.get(number)!=null){
            System.out.println("*********办理退网**********");
            //二次确定
            System.out.print("您确定注销此手机号吗?(Y/N):");
            String choice=sc.next();
            if(choice.equalsIgnoreCase("Y")){
                //通过 key手机号删除
                cards.remove(number);
                System.out.println("卡号:"+number+"退网成功!\n感谢使用");
            }
        }else{
            System.out.println("该手机卡号不存在!");
        }
    }
    //根据卡密验证该卡是否已注册(登录验证)
    public boolean isExistCard(String number,String passWord){
        Set<String> numbers = cards.keySet();
        Iterator<String> it = numbers.iterator();
        boolean flag=false;
        while (it.hasNext()){
            String searchNum = it.next();
            if(searchNum.equals(number) && (cards.get(searchNum)).getPassWord().equals(passWord)){
                flag=true;
            }

        }
        return flag;

    }
    //根据卡号验证卡号是否注册
    public boolean isExistCard(String number){
        Set<String> numbers = cards.keySet();
        Iterator<String> it = numbers.iterator();
        boolean flag=false;
        while (it.hasNext()){
            String searchNum = it.next();
            if(searchNum.equals(number)){
                flag=true;
            }
        }
        return flag;
    }
    //生成随机卡号
    public String createNumber(){
        String fixedNum="130";
        String cardNumber="";
        Random rand=new Random();
        String randNumber="";
        //随机生成八位数字
        for (int i = 0; i < 8; i++) {
            randNumber+=rand.nextInt(10);
        }
        cardNumber=fixedNum+randNumber;
        return cardNumber;
    }

    /**
     * 生成指定个数的卡号列表
     * @param count  返回手机号码数量
     * @return
     */
    public String[] getNewNumbers(int count){
        //创建一个数组接收随机生成卡号
        String[] numbers=new String[count];
        //遍历赋值
        for (int i = 0; i < numbers.length; i++) {
            numbers[i]=createNumber();
            //判断是否注册
            if(isExistCard(numbers[i])){
                //如果被注册就重新赋值
                i--;
            }
            //判断生成是否有重复
            for (int j = 0; j < i; j++) {
                //判断新生成的号码与之前生成的号码是否有重复,如果有重新生成
                if(numbers[i].equals(numbers[j])){
                    //有重复重新赋值
                    i--;
                    break;
                }
            }
        }
        return numbers;
    }

    /**
     * 添加指定卡号的消费记录
     * @param number 手机卡号
     * @param info   一条消费记录
     */
    public void addConsumInfo(String number,ConsumInfo info){
        //创建消费对象集合
        List<ConsumInfo> consumList=null;

        //有消费记录(集合已存在)
        if(consumInfos.containsKey(number)){
            consumList=consumInfos.get(number);
            consumList.add(info);
            System.out.println("添加消费记录成功");
        }else {
            //没有消费记录(该卡号不存在集合中)
            consumList=new ArrayList<>();
            consumList.add(info);
            //添加到map中
            consumInfos.put(number,consumList);
            System.out.println("该卡还未进行消费,新增一条消费记录");
        }

    }


}

