package com.g10.locomobile.models;

/**
 * This class will store the codes that ensure discounts
 */
public class discountCode implements IDable {

    private String discountCode;
    private double discountRate;
    private int id;
    private static int idCount = 0;

    /**
     * Initializes the discount code and discount rate, also increases the id
     * @param discountCode the code that ensures discount
     * @param discountRate the rate that will be implemented
     */
    public discountCode(String discountCode, double discountRate) {
        this.discountCode = discountCode;
        this.discountRate = discountRate;
        this.id = idCount;
        idCount++;
    }

    public discountCode(){
    }

    /**
     * Gets the discount code
     * @return discountCode
     */
    public String getDiscountCode() {
        return discountCode;
    }

    /**
     * Sets the discount code according to the given parameter
     * @param discountCode the discount code
     */
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }


    /**
     * Gets the discount rate
     * @return discount rate
     */
    public double getDiscountRate() {
        return discountRate;
    }

    /**
     * Sets the discount rate.
     * @param discountRate discount rate
     */
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }


    /**
     * Gets the id of the discount code
     * @return idCount
     */
    public static int getIdCount() {
        return idCount;
    }

    /**
     * Sets the id count of the discount code
     * @param count
     */
    public static void setIdCount(int count) {
        idCount = count;
    }

    @Override
    /**
     * Gets the id of the discount code
     * @return idCount
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the id Count
     * @param count
     */
    @Override
    public void setIDCount(int count) {
        idCount = count;
    }
}
