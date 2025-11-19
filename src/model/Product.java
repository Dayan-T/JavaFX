package model;

public abstract class Product {
    //Attributs
    private static int counter = 0; // compteur auto-incrémenté
    private int number;
    private String name;
    private double purprice;   // prix d’achat
    private double sellprice;  // prix de vente
    private double discprice;  // prix avec remise
    private int nbitems;       // nombre d’articles
    private double discountPer = 0; // pourcentage de remise

    //Attributs statiques
    private static double capital = 1000;
    private static double cost = 0;
    private static double income = 0;

    //Constructeur
    public Product(String name, double purprice, double sellprice) {
        try {
            if (purprice <= 0 || sellprice <= 0) {
                throw new IllegalArgumentException("Negative price!");
            }
            counter++;
            this.number = counter;
            this.name = name;
            this.purprice = purprice;
            this.sellprice = sellprice;
            this.discprice = sellprice; // par défaut pas de remise
            this.nbitems = 0;
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    protected Product() {
    }

    //Getters
    public int getNumber() { return number; }
    public String getName() { return name; }
    public double getPurprice() { return purprice; }
    public double getSellprice() { return sellprice; }
    public double getDiscprice() { return discprice; }
    public int getNbitems() { return nbitems; }
    public double getDiscountPer() { return discountPer; }



    // Setters
    public void setNbitems(int nbitems) {
        try {
        if (nbitems < 0) {
                throw new IllegalArgumentException("Negative quantity!");
            }
            this.nbitems = nbitems;
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    public void setDiscountPer(double discountPer) {
        this.discountPer = discountPer;
        this.discprice = sellprice - (sellprice * discountPer / 100);
    }

    public String toString(){
        return  "model.Product #" + number +
                "\nName: " + name +
                "\nPurchase price: " + purprice +
                "\nSell price: " + sellprice +
                "\nDiscounted price: " + discprice +
                "\nQuantity: " + nbitems;
    }
    public void setPurprice(double purprice) {
        try {
            if (purprice <= 0) {
                throw new IllegalArgumentException("Negative price!");
            }
            this.purprice = purprice;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    public void setSellprice(double sellprice) {
        try {
            if (sellprice <= 0) {
                throw new IllegalArgumentException("Negative price!");
            }
            this.sellprice = sellprice;

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    //Méthode de vente

    public void sell(int nb) {
        try {
            if (nb > this.nbitems) {
                throw new IllegalArgumentException("model.Product Unavailable");
            }

            this.nbitems -= nb;

            if (this.discountPer != 0) {
                income += this.discprice * nb;
                System.out.println("You sold " + nb + " " + name + "(s) for " + discprice + " each.");
            } else {
                income += this.sellprice * nb;
                System.out.println("You sold " + nb + " " + name + "(s) for " + sellprice + " each.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    //MEthode d'achat

    public void buy(int nb) {
        try {
            if (nb < 0) {
                throw new IllegalArgumentException("Negative quantity!");
            }
            if (capital<nb*this.purprice) {
                throw new IllegalArgumentException("model.Product Unavailable");
            }
            this.nbitems += nb;
            capital -= nb*this.purprice;
            cost+=nb*this.purprice;
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    //Afficher les infos du produit

    public void display() {
        System.out.println(this.toString());
    }

    //Get Financial

    public static void displayStats() {
        System.out.println("Capital: " + capital);
        System.out.println("Income: " + income);
        System.out.println("Cost: " + cost);
    }
}
