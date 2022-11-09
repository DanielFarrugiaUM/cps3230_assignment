package com.cps3230assignment.task1.models;

public class Product {

    private String heading;
    private String description;
    private String url;
    private String imageUrl;
    private String price;

    public Product(){

    }
    public Product(String heading, String description, String url, String imageUrl, String price){
        this.heading = heading;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.price = price;

    }

    public int getPriceAsCents(){
        String trimmedPrice = price.trim();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < trimmedPrice.length(); i++) {
            char temp = trimmedPrice.charAt(i);
            if(Character.isDigit(temp) || temp == '.'){
                stringBuilder.append(temp);
            }
        }

        double priceAsDouble = Double.parseDouble(stringBuilder.toString());

        return (int)(priceAsDouble * 100);
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
