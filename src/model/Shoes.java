package model;

public class Shoes extends Product {
    //Attributs
    private int shoeSize;

    //Constructeur
    public Shoes(String name, double purprice, double sellprice, int shoeSize){
        super(name,purprice,sellprice);
        this.shoeSize = shoeSize;
    }

    //Getter
    public int getShoeSize() {
        return shoeSize;
    }

    //Setter
    public void setShoeSize(int shoeSize){
        try{
            if (shoeSize <= 36 || shoeSize >= 50 ){
                throw new IllegalArgumentException("Wrong Shoe size");
            }
            this.shoeSize = shoeSize;
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur ! La taille doit etre comprise entre 36 et 50");
            throw e;
        }
    }

    //Méthode ToString
    @Override
    public String toString(){
        return super.toString() + ", shoeSize = " + shoeSize;
    }

}
