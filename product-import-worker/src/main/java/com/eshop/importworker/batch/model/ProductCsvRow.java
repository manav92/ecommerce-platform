package com.eshop.importworker.batch.model;

import java.math.BigDecimal;

public class ProductCsvRow {
    private String productCode;
    private String name;
    private String brand;
    private String category;
    private String description;
    private String sku;
    private String size;
    private String design;
    private BigDecimal priceAmount;
    private String currency;

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getDesign() { return design; }
    public void setDesign(String design) { this.design = design; }
    public BigDecimal getPriceAmount() { return priceAmount; }
    public void setPriceAmount(BigDecimal priceAmount) { this.priceAmount = priceAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
