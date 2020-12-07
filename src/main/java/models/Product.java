package models;

import java.util.Objects;

public class Product {
    private int barcode;
    private String name;
    private int weight;
    private int price;
    private int departmentId;

    public Product(int barcode, String title, int weight, int price, int departmentId) {
        this.barcode = barcode;
        this.name = title;
        this.weight = weight;
        this.price = price;
        this.departmentId = departmentId;
    }

    public Product() {

    }

//    public Product(int barcode, String title, int weight, int price) {
//        this.barcode = barcode;
//        this.name = title;
//        this.weight = weight;
//        this.price = price;
//    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "\nProduct{\n" +
                "barcode='" + barcode + '\'' +
                ",\n title='" + name + '\'' +
                ",\n weight=" + weight +
                ",\n price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return weight == product.weight &&
                price == product.price &&
                barcode == (product.barcode) &&
                departmentId == (product.departmentId) &&
                name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode, name, weight, price, departmentId);
    }
}
