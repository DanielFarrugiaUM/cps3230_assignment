package com.cps3230assignment.task1.models;

import com.cps3230assignment.task1.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductTests {
    Product product;

    @BeforeEach
    public void setProduct(){
        product = new Product();
    }

    @Test
    public void testGetPriceAsCentsWithEuroSignAndComma(){
        //Setup
        product.setPrice("\u20ac6,000");
        //Exercise
        int actual = product.getPriceAsCents();
        //Verify
        Assertions.assertEquals(600000, actual);
    }

    @Test
    public void testGetPriceAsCentsWithEuroSignAndDecimal(){
        //Setup
        product.setPrice("\u20ac60.00");
        //Exercise
        int actual = product.getPriceAsCents();
        //Verify
        Assertions.assertEquals(6000, actual);
    }

    @Test
    public void testGetPriceAsCentsWitNoSign(){
        //Setup
        product.setPrice("\u20ac60");
        //Exercise
        int actual = product.getPriceAsCents();
        //Verify
        Assertions.assertEquals(6000, actual);
    }

    @Test
    public void testGetPriceAsCentsWithCommaAndDecimal(){
        //Setup
        product.setPrice("\u20ac6,000.58");
        //Exercise
        int actual = product.getPriceAsCents();
        //Verify
        Assertions.assertEquals(600058, actual);
    }

    @Test
    public void testGetPriceAsCentsEmptyStr(){
        //Setup
        product.setPrice("");
        //Exercise
        int actual = product.getPriceAsCents();
        //Verify
        Assertions.assertEquals(0, actual);
    }
}
