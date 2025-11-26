package model;

public class Shoes extends Product {
    //Attributs
    private int shoeSize;

    //Constructeur
    public Shoes(String name, double purprice, double sellprice, int shoeSize){
        super(name,purprice,sellprice);
        setShoeSize(shoeSize);
    }

    //Getter
    public int getShoeSize() {
        return shoeSize;
    }

    //Setter
    public void setShoeSize(int shoeSize){
        if (shoeSize < 36 || shoeSize > 50 ){
            throw new IllegalArgumentException("Wrong Shoe size");
        }
        this.shoeSize = shoeSize;
    }


    //Méthode ToString
    @Override
    public String toString(){
        return super.toString() + ", shoeSize = " + shoeSize;
    }

    @Override
    public void applyDiscount() {
        this.discprice = this.sellprice * (1 - SHOES_DISCOUNT);
    }

    @Override
    public void unApplyDiscount() {
        this.discprice = 0;
    }
}
