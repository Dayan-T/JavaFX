package model;

public class Shoes extends Product {
    private int shoeSize;

    public Shoes(String name, double purprice, double sellprice, int shoeSize) {
        super(name, purprice, sellprice);
        setShoeSize(shoeSize);
    }

    public int getShoeSize() { return shoeSize; }

    public void setShoeSize(int shoeSize) {
        if (shoeSize < 36 || shoeSize > 50) {
            throw new IllegalArgumentException("Pointure invalide (36-50)");
        }
        this.shoeSize = shoeSize;
    }

    @Override
    public void applyDiscount() {
        this.discprice = this.sellprice * (1 - SHOES_DISCOUNT);
    }

    @Override
    public void unApplyDiscount() {
        this.discprice = 0;
    }

    @Override
    public String toString() {
        return super.toString() + ", Pointure: " + shoeSize;
    }
}