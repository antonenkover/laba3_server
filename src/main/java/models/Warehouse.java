package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Warehouse {
    private int id;
    private String title;
    private List<Department> departments = new ArrayList<>();

    public Warehouse(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Warehouse() {

    }

    public void addDepartment(Department department) {
        boolean exists = departments.stream().anyMatch(department1 -> department1.getId() == department.getId());
        if (!exists) {
            departments.add(department);
        }
    }

    public void addProduct(Product product, int departmentId) {
        Optional<Department> department = getDepartmentById(departmentId);
        department.ifPresent(value -> value.addProduct(product));
    }

    public void deleteProduct(Product product, int departmentId) {
        Optional<Department> department = getDepartmentById(departmentId);
        department.ifPresent(value -> value.deleteProduct(product));
    }

    public void deleteDepartment(Department department) {
        departments.remove(department);
    }

    public Predicate<Department> isDepartmentIdEqual(int id) {
        return p -> p.getId() == id;
    }

    public Optional<Department> getDepartmentById(int id) {
        return departments.stream().filter(isDepartmentIdEqual(id)).findFirst();
    }

    public Predicate<Department> isDepartmentNameEqual(String name) {
        return p -> p.getName().equals(name);
    }

    public Optional<Department> getDepartmentByName(String name) {
        return departments.stream().filter(isDepartmentNameEqual(name)).findFirst();
    }

    public Optional<Product> getProductsByBarcode(int barcode) {
        Optional<Product> searchedProduct = Optional.empty();
        for (Department department :
                departments) {
            if (department.getProductByBarcode(barcode).isPresent())
                searchedProduct = department.getProductByBarcode(barcode);
        }
        return searchedProduct;
    }

    public Department getDepartmentByIndex(int index) {
        Department returnDepartment = null;
        try {
            returnDepartment = departments.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Wrong index. Please try again");
        }
        return returnDepartment;
    }

    public int countDepartments() {
        return departments.size();
    }

    public int countProducts() {
        int countProducts = 0;
        for (Department d :
                departments) {
            countProducts += d.countProductsForDepartment();
        }
        return countProducts;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public String printDepartments(List<Department> listOfDepartments) {
        StringBuilder sb = new StringBuilder();
        sb.append("Warehouse{");
        for (Department d :
                listOfDepartments) {
            sb.append("\nDepartment{\n").append("id=").append(d.getId()).append(",\n title='").append(d.getName()).append('\'');
        }
        sb.append("\n}");
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Warehouse{\n" +
                "id=" + id +
                ",\n title='" + title + '\'' +
                ",\n departments=" + departments +
                '}';
    }
}
