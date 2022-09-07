package manager;

import entity.MobileCard;
import util.CardUtil;

import java.io.IOException;
import java.util.Scanner;

public class SosoMgr {
    private Scanner sc=new Scanner(System.in);
    private CardUtil cardUtil=new CardUtil();
    /**
     * 启动
     */
    public void start(){
        //初始化
        cardUtil.init();
        cardUtil.initScene();
        int choose; //用户选择
        boolean isExit=false;// 标记用户是否退出系统，true为退出
        String cardNum=null; //手机卡号
        //遍历菜单
        do {
            mainMenu();
            choose=sc.nextInt();
            switch (choose){
                case 1:
                    System.out.println("***用户登录***");
                    System.out.print("请输入手机号:");
                    cardNum = sc.next();
                    System.out.print("请输入密码:");
                    String passWord=sc.next();
                    //判断注册状态
                    if (cardUtil.isExistCard(cardNum,passWord)){
                        cardMenu(cardNum);
                    }else{
                        System.out.println("账号或密码输入错误");
                    }
                    break;
                case 2:
                    System.out.println("***用户注册***");
                    MobileCard card=null;
                    cardUtil.addCard(card);
                    break;
                case 3:
                    System.out.println("***使用嗖嗖***");
                    System.out.print("请输入手机卡号");
                    cardNum=sc.next();
                    if(cardUtil.isExistCard(cardNum)){
                        try {
                            cardUtil.userSoso(cardNum);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("该手机号不存在");
                    }
                    break;
                case 4:
                    System.out.println("***话费充值***");
                    System.out.print("请输入要充值的手机号:");
                    cardNum=sc.next();
                    System.out.print("请输入充值金额:");
                    double money=sc.nextDouble();
                    cardUtil.chargeMoney(cardNum,money);
                    break;
                case 5:
                    System.out.println("***资费说明***");
                    try {
                        cardUtil.showDescription();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("退出系统");
                    isExit=true;
                    break;
                default:
                    System.out.println("业务开发中...");
                    continue;
            }
            if(!isExit){
                System.out.println("按任意数字键返回");
                try {
                    choose=sc.nextInt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        } while (true);
        System.out.println("欢迎下次光临嗖嗖移动服务大厅");

    }
    //主菜单
    public static void mainMenu(){
        System.out.println("******欢迎使用嗖嗖移动业务大厅******");
        System.out.println("1.用户登录\t2.用户注册\t3.使用嗖嗖\t4.话费充值\t5.资费说明\t6.退出系统");
        System.out.println("请选择(输入1-6选择功能,其他键返回主菜单):");

    }
    //二级菜单
    public  void cardMenu(String number){
        do {
            int choice;
            System.out.println("******嗖嗖移动用户菜单******");
            System.out.println("1.本月账单查询\t2.套餐余量查询\t3.打印消费详单\t4.套餐变更\t5.办理退网");
            System.out.println("请选择(输入1-5选择功能,其他键返回上一级):");
            choice=sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("1、本月账单查询");
                    cardUtil.showAmountDetail(number);
                    break;
                case 2:
                    System.out.println("2、套餐余量查询");
                    cardUtil.showRemainDetail(number);
                    break;
                case 3:
                    System.out.println("3、打印消费祥单");
                    cardUtil.printAmountDetail(number);
                    break;
                case 4:
                    System.out.println("4、套餐变更");
                    cardUtil.changingPack(number);
                    break;
                case 5:
                    System.out.println("5、办理退网");
                    cardUtil.delCard(number);
                    break;
                default:
                    System.out.println("返回主菜单");
                    return;
            }
        }while (true);


    }


}

