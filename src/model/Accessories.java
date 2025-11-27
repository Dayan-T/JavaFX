package model;

public class Accessories extends Product {
    //Constructeur
    public Accessories(String name, double purprice, double sellprice){
        super(name,purprice,sellprice);
    }

    @Override
    public void applyDiscount() {
        this.discprice = this.sellprice * (1 - ACCESSORY_DISCOUNT);
    }

    @Override
    public void unApplyDiscount() {
        this.discprice = 0;
    }


    @Override
    public String toString(){
        return super.toString();
    }
}
