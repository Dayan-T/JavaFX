package model;
public interface Discount {
    double CLOTHES_DISCOUNT = 0.30;
    double SHOES_DISCOUNT = 0.20;
    double ACCESSORY_DISCOUNT = 0.50;

    void applyDiscount();
    void unApplyDiscount();
}