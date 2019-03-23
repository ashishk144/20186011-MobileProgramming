package com.example.shoppingcart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.function.LongUnaryOperator;

public class Products implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    public Products() {}

    private String Category;
    private String CurrencyCode;
    private String DateOfSale;
    private Long Depth;
    private String Description;
    private String DimUnit;
    private Long Height;
    private String MainCategory;
    private String Name;
    private Long Price;
    private String ProductId;
    private String ProductPicUrl;
    private Long Quantity;
    private String Status;
    private String SupplierName;
    private String TaxTarifCode;
    private String UoM;
    private Double WeightMeasure;
    private String WeightUnit;
    private Long Width;

    public Products(Parcel in) {
        Category = in.readString();
        CurrencyCode = in.readString();
        DateOfSale = in.readString();
        Depth = in.readLong();
        Description = in.readString();
        DimUnit = in.readString();
        Height = in.readLong();
        MainCategory = in.readString();
        Name = in.readString();
        Price = in.readLong();
        ProductId = in.readString();
        ProductPicUrl = "http://msitmp.herokuapp.com"+in.readString();
        Quantity = in.readLong();
        Status = in.readString();
        SupplierName = in.readString();
        TaxTarifCode = in.readString();
        UoM = in.readString();
        WeightMeasure = in.readDouble();
        WeightUnit = in.readString();
        Width = in.readLong();
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getDateOfSale() {
        return DateOfSale;
    }

    public void setDateOfSale(String dateOfSale) {
        DateOfSale = dateOfSale;
    }

    public Long getDepth() {
        return Depth;
    }

    public void setDepth(Long depth) {
        Depth = depth;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDimUnit() {
        return DimUnit;
    }

    public void setDimUnit(String dimUnit) {
        DimUnit = dimUnit;
    }

    public Long getHeight() {
        return Height;
    }

    public void setHeight(Long height) {
        Height = height;
    }

    public String getMainCategory() {
        return MainCategory;
    }

    public void setMainCategory(String mainCategory) {
        MainCategory = mainCategory;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductPicUrl() {
        return ProductPicUrl;
    }

    public void setProductPicUrl(String productPicUrl) {
        String str = "http://msitmp.herokuapp.com";
        ProductPicUrl = str + productPicUrl;
    }

    public Long getQuantity() {
        return Quantity;
    }

    public void setQuantity(Long quantity) {
        Quantity = quantity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getTaxTarifCode() {
        return TaxTarifCode;
    }

    public void setTaxTarifCode(String taxTarifCode) {
        TaxTarifCode = taxTarifCode;
    }

    public String getUoM() {
        return UoM;
    }

    public void setUoM(String uoM) {
        UoM = uoM;
    }

    public double getWeightMeasure() {
        return WeightMeasure;
    }

    public void setWeightMeasure(double weightMeasure) {
        WeightMeasure = weightMeasure;
    }

    public String getWeightUnit() {
        return WeightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        WeightUnit = weightUnit;
    }

    public Long getWidth() {
        return Width;
    }

    public void setWidth(Long width) {
        Width = width;
    }

    public String toString() {
        return getName() + " "  + getProductPicUrl();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Category);
        dest.writeString(this.CurrencyCode);
        dest.writeString(DateOfSale);
        dest.writeLong(Depth);
        dest.writeString(Description);
        dest.writeString(DimUnit);
        dest.writeLong(Height);
        dest.writeString(MainCategory);
        dest.writeString(Name);
        dest.writeLong(Price);
        dest.writeString(ProductId);
        dest.writeString(ProductPicUrl);
        dest.writeLong(Quantity);
        dest.writeString(Status);
        dest.writeString(SupplierName);
        dest.writeString(TaxTarifCode);
        dest.writeString(UoM);
        dest.writeDouble(WeightMeasure);
        dest.writeString(WeightUnit);
        dest.writeLong(Width);
    }
}