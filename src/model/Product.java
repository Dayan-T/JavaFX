package model;

public abstract class Product implements Discount {
    //Attributs
    protected static int counter = 0; // compteur auto-incrémenté
    protected int number;
    protected String name;
    protected double purprice;   // prix d’achat
    protected double sellprice;  // prix de vente
    protected double discprice;  // prix avec remise
    protected int nbitems;       // nombre d’articles
    protected double discountPer = 0; // pourcentage de remise

    //Attributs statiques
    protected static double capital = 30000;
    protected static double cost = 0;
    protected static double income = 0;

    //Constructeur
    public Product(String name, double purprice, double sellprice) {
        try {
            if (purprice <= 0 || sellprice <= 0) {
                throw new IllegalArgumentException("Negative price!");
            }
            counter++;
            this.number = counter;
            this.name = name;
            setPurprice(purprice);
            setSellprice(sellprice);
            this.discprice = 0;
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
    public double getEffectivePrice() {
        return (discprice > 0) ? discprice : sellprice;
    }
    public double getDiscountPer() { return discountPer; }
    public int getId() { return number; }

    public static double getCapital() {
        return capital;
    }

    public static double getIncome() {
        return income;
    }

    public static double getCost() {
        return cost;
    }

    // Setters
    public void setName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty!");
            }
            this.name = name;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void setId(int number) {
        if (number > 0) {
            this.number = number;
        } else {
            System.out.println("Invalid product ID!");
        }
    }
    public void setNbitems(int nbitems) {
        if (nbitems < 0) {
            throw new IllegalArgumentException("Negative quantity!");
        }
        this.nbitems = nbitems;
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
        if (purprice <= 0) {
            throw new IllegalArgumentException("Negative price!");
        }

        this.purprice = purprice;
    }
    public void setSellprice(double sellprice) {
        if (sellprice <= 0) {
            throw new IllegalArgumentException("Negative price!");
        }
        if (sellprice < purprice) {
            throw new IllegalArgumentException("Sellprice can not be lower than purprice!");
        }
        this.sellprice = sellprice;
    }


    //Méthode de vente

    public void sell(int nb) {
        if (nb > this.nbitems) {
            throw new IllegalArgumentException("model.Product Unavailable");
        }

        this.nbitems -= nb;

        double priceUsed;
        if (this.discprice > 0) {           // remise active
            priceUsed = this.discprice;
        } else {                            // pas de remise
            priceUsed = this.sellprice;
        }

        income += priceUsed * nb;
        System.out.println("You sold " + nb + " " + name + "(s) for " + priceUsed + " each.");
    }


    //MEthode d'achat

    public void buy(int nb) {
        if (nb < 0) {
            throw new IllegalArgumentException("Negative quantity!");
        }
        if (capital<nb*this.purprice) {
            throw new IllegalArgumentException("Insuffficient budget!");
        }
        this.nbitems += nb;
        capital -= nb*this.purprice;
        cost+=nb*this.purprice;
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
