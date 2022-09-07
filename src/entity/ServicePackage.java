package entity;

/**
 * 业务套餐 抽象类 包含网虫套餐，话痨套餐，超人套餐
 */
public abstract class ServicePackage {
    private double price;  //套餐月资费

    //展示套餐信息
    public abstract void showInfo();

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}



