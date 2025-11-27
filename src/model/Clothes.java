package model;

public class Clothes extends Product {
    //Atributs
    private int size;

    public Clothes(String name, double purprice, double sellprice, int size) {
        super(name, purprice, sellprice);
        setSize(size);
    }

    //getter
    public int getSize() { return size; }
    //setter
    public void setSize(int size) {
        if (size < 34 || size > 54 || size % 2 != 0) {
            throw new IllegalArgumentException("Taille vêtement invalide (34-54)");
        }
        this.size = size;
    }

    @Override
    public void applyDiscount() {
        this.discprice = this.sellprice * (1 - CLOTHES_DISCOUNT);
    }

    @Override
    public void unApplyDiscount() {
        this.discprice = 0;
    }

    @Override
    public String toString() {
        return super.toString() + ", Taille: " + size;
    }


    @Override
    public void display() {
        System.out.println(this.toString());
    }
}
