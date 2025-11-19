package model;

public class Clothes extends Product {
    //Atributs
    public int size;

    public Clothes(String name,double purprice, double sellprice, int size) {
        super(name,purprice,sellprice); //On appelle le constructeur de product
        this.size = size;
    }

    //Getter
    public int getSize() {
        return size;
    }

    //Setter
    public void setSize(int size) {
        try {
            if (size < 34 || size > 54) {
                throw new IllegalArgumentException("wrong size !");
            }
            this.size = size;
        }
        catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public String toString() {
        return super.toString() + "\nSize: " + size;
    }


    @Override
    public void display() {
        System.out.println(this.toString());
    }
}
