package formviews;

import dao.DepartmentDao;
import dao.ProductDao;
import dao.WarehouseDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import models.Department;
import models.Product;
import views.View;

public class FormView extends View {

    //    Warehouse warehouse = WarehouseDao.createWarehouse();
    DepartmentDao departmentDao = new DepartmentDao();
    ProductDao productDao = new ProductDao();

    @FXML
    private void initialize() {
        setToggleGroups();
    }

    @FXML
    public void listAllDepartments(ActionEvent actionEvent) {
//        listAllDepartments();
        DBListAllDepartments();
    }


    @FXML
    public void searchProductOrDepartmentByID(ActionEvent actionEvent) {
//        searchProductOrDepartmentByID();
        DBSearchProductOrDepartmentByID();
    }


    @FXML
    public void departmentCRUD(ActionEvent actionEvent) {
//        departmentCRUD();
        DBDepartmentCRUD();
    }


    @FXML
    public void productCRUD(ActionEvent actionEvent) {
//        productCRUD();
        DBProductCRUD();
    }


    //    @FXML
//    public void viewAllXML(ActionEvent actionEvent) {
//        viewAllXML();
//    }
    @FXML
    public void viewAllData(ActionEvent actionEvent) {
        DBViewAllData();
    }

    @FXML
    public void countAllProducts(ActionEvent actionEvent) {
//        countAllProducts();
        DBCountAllProducts();
    }

    @FXML
    public void countAllDepartments(ActionEvent actionEvent) {
//        countAllDepartments();
        DBCountAllDepartments();
    }

//    private void listAllDepartments() {
//        setMainTextPaneText(warehouse.printDepartments(warehouse.getDepartments()));
//    }

    private void DBListAllDepartments() {
        setMainTextPaneText(departmentDao.getAll().toString());
    }

//    private void searchProductOrDepartmentByID() {
//        if (getBarcodeOrIDInput().isEmpty()) {
//            showWarningMessage("ID or barcode wasn't entered.");
//        } else {
//            if (isSelectedSearchDepartmentByID()) {
//                Optional<Department> searchedDepartment = warehouse.getDepartmentById(Integer.parseInt(getBarcodeOrIDInput()));
//                if (isSelectedShowProductsByIdSearch()) {
//                    setMainTextPaneText(searchedDepartment.isPresent() ? searchedDepartment.toString() : "Department not found.");
//                } else {
//                    setMainTextPaneText(searchedDepartment.isPresent() ? searchedDepartment.get().printDepartment(searchedDepartment.get()) : "Department not found.");
//                }
//            } else if (isSelectedSearchProductByID()) {
//                Optional<Product> searchedProduct = warehouse.getProductsByBarcode(Integer.parseInt(getBarcodeOrIDInput()));
//                setMainTextPaneText(searchedProduct.isPresent() ? searchedProduct.toString() : "Product not found.");
//            } else showWarningMessage("Please choose whether you want to search department or product.");
//        }
//    }

    private void DBSearchProductOrDepartmentByID() {
        if (getBarcodeOrIDInput().isEmpty()) {
            showWarningMessage("ID or barcode wasn't entered.");
        }
        else {
            if (isSelectedSearchDepartmentByID()) {
                Department searchedDepartment = departmentDao.getById(Integer.parseInt(getBarcodeOrIDInput()), true);
                if (isSelectedShowProductsByIdSearch()) {
                    setMainTextPaneText(searchedDepartment != null ? departmentDao.printDepartmentAndProductData(searchedDepartment) : "Department with id " + getBarcodeOrIDInput() + " not found.");
                } else {
                    setMainTextPaneText(searchedDepartment != null ? searchedDepartment.printDepartment(searchedDepartment) : "Department with id " + getBarcodeOrIDInput() + " not found.");
                }
            }
            else if (isSelectedSearchProductByID()) {
                Product searchedProduct = productDao.getById(Integer.parseInt(getBarcodeOrIDInput()), true);
                setMainTextPaneText(searchedProduct != null ? searchedProduct.toString() : "Product with id " + getBarcodeOrIDInput() + " not found.");
            } else showWarningMessage("Please choose whether you want to find department or product.");
        }
        clearSearchDepartmentOrProduct();
    }

//    private void departmentCRUD() {
//        if (getDepartmentIDInput().isEmpty() || getDepartmentNameInput().isEmpty()) {
//            showWarningMessage("Please enter parameters of department");
//        } else {
//            if (isSelectedRbAddDepartment()) {
//                Department department = new Department(Integer.parseInt(getDepartmentIDInput()), getDepartmentNameInput());
//                warehouse.addDepartment(department);
//                setMainTextPaneText(warehouse.toString());
//            } else if (isSelectedRbUpdateDepartment()) {
//                Optional<Department> department = warehouse.getDepartmentById(Integer.parseInt(getDepartmentIDInput()));
//                department.ifPresent(value -> value.setName(getDepartmentNameInput()));
//                setMainTextPaneText(warehouse.toString());
//            } else if (isSelectedRbDeleteDepartment()) {
//                Department department = new Department(Integer.parseInt(getDepartmentIDInput()), getDepartmentNameInput());
//                warehouse.deleteDepartment(department);
//                setMainTextPaneText(warehouse.toString());
//            }
//        }
//    }

    private void DBDepartmentCRUD() {
        if (getDepartmentIDInput().isEmpty() || getDepartmentNameInput().isEmpty()) {
            showWarningMessage("Please enter parameters of department");
        }
        else {
            if (isSelectedRbAddDepartment()) {
                    Department department = new Department(Integer.parseInt(getDepartmentIDInput()), getDepartmentNameInput());
                boolean completed = departmentDao.create(department);
                if (completed) {
                    showInformationDialog(department.getName() + " department added successfully.");
                }
                setMainTextPaneText(departmentDao.getAll().toString());
            } else if (isSelectedRbUpdateDepartment()) {
                if (departmentDao.getById(Integer.parseInt(getDepartmentIDInput()), true) == null) {
            showErrorMessage("No department with such ID exist. Please try again.");
        }
                else {
                    Department department = departmentDao.getById(Integer.parseInt(getDepartmentIDInput()), true);
                    department.setName(getDepartmentNameInput());
                    boolean completed = departmentDao.update(department);
                    if (completed) {
                        showInformationDialog(department.getName() + " department updated successfully.");
                    }
                }
                setMainTextPaneText(WarehouseDao.printAll());
            } else if (isSelectedRbDeleteDepartment()) {
                if (departmentDao.getById(Integer.parseInt(getDepartmentIDInput()), true) == null) {
                    showErrorMessage("No department with such ID exist. Please try again.");
                }
                else {
                    Department department = departmentDao.getById(Integer.parseInt(getDepartmentIDInput()), true);
                    departmentDao.remove(department);
                    showInformationDialog(department.getName() + " department deleted successfully.");
                }
                setMainTextPaneText(WarehouseDao.printAll());
            }
        }
        clearDepartmentCRUD();
    }

//    private void productCRUD() {
//        if (getProductSearchDepartmentId().isEmpty()) {
//            showWarningMessage("Please enter department id.");
//        } else {
//            if (getProductBarcodeInput().isEmpty() || getProductPriceInput().isEmpty() || getProductWeightInput().isEmpty() || getProductNameInput().isEmpty()) {
//                showWarningMessage("Please enter all details about product.");
//            } else {
//                if (isSelectedRbAddProduct()) {
//                    Product newProduct = new Product(Integer.parseInt(getProductBarcodeInput()), getProductNameInput(), Integer.parseInt(getProductWeightInput()), Integer.parseInt(getProductPriceInput()), Integer.parseInt(getProductSearchDepartmentId()));
//                    warehouse.addProduct(newProduct, Integer.parseInt(getProductSearchDepartmentId()));
//                    setMainTextPaneText(warehouse.toString());
//                } else if (isSelectedRbDeleteProduct()) {
//                    Product newProduct = new Product(Integer.parseInt(getProductBarcodeInput()), getProductNameInput(), Integer.parseInt(getProductWeightInput()), Integer.parseInt(getProductPriceInput()), Integer.parseInt(getProductSearchDepartmentId()));
//                    warehouse.deleteProduct(newProduct, Integer.parseInt(getProductSearchDepartmentId()));
//                    setMainTextPaneText(warehouse.toString());
//                } else if (isSelectedRbUpdateProduct()) {
//                    Optional<Product> product = warehouse.getProductsByBarcode(Integer.parseInt(getProductBarcodeInput()));
//                    product.ifPresent(value -> {
//                        value.setName(getProductNameInput());
//                        value.setPrice(Integer.parseInt(getProductPriceInput()));
//                        value.setWeight(Integer.parseInt(getProductWeightInput()));
//                        setMainTextPaneText(warehouse.toString());
//                    });
//                    setMainTextPaneText(warehouse.toString());
//                }
//            }
//        }
//    }

    private void DBProductCRUD() {
        if (getProductSearchDepartmentId().isEmpty()) {
            showWarningMessage("Please enter department id.");
        } else if (getProductBarcodeInput().isEmpty() || getProductPriceInput().isEmpty() || getProductWeightInput().isEmpty() || getProductNameInput().isEmpty()) {
            showWarningMessage("Please enter all details about product.");
        }
        else {
            if (isSelectedRbAddProduct()) {
                Product product = new Product(Integer.parseInt(getProductBarcodeInput()), getProductNameInput(), Integer.parseInt(getProductWeightInput()), Integer.parseInt(getProductPriceInput()), Integer.parseInt(getProductSearchDepartmentId()));
                boolean completed = productDao.create(product);
               if (completed) {
                   showInformationDialog(product.getName() + " product added successfully.");
               }
                setMainTextPaneText(WarehouseDao.printAll());
            } else if (isSelectedRbDeleteProduct()) {
                if (productDao.getById(Integer.parseInt(getProductBarcodeInput()), true) == null) {
                    showErrorMessage("No product with such ID found. Please try again.");
                }
                else {
                    Product product = new Product(Integer.parseInt(getProductBarcodeInput()), getProductNameInput(), Integer.parseInt(getProductWeightInput()), Integer.parseInt(getProductPriceInput()), Integer.parseInt(getProductSearchDepartmentId()));
                    productDao.remove(product);
                    showInformationDialog(product.getName() + " product deleted successfully.");
                }
                setMainTextPaneText(WarehouseDao.printAll());
            } else if (isSelectedRbUpdateProduct()) {
                if (productDao.getById(Integer.parseInt(getProductBarcodeInput()), true) == null) {
                    showErrorMessage("No product with such ID found. Please try again.");
                }
                else {
                    Product product = productDao.getById(Integer.parseInt(getProductBarcodeInput()), true);
                    product.setName(getProductNameInput());
                    product.setPrice(Integer.parseInt(getProductPriceInput()));
                    product.setWeight(Integer.parseInt(getProductWeightInput()));
                    boolean completed = productDao.update(product);
                    if (completed) {
                        showInformationDialog(product.getName() + " product added successfully.");
                    }
                }
                setMainTextPaneText(WarehouseDao.printAll());
            }
        }
        clearProductCRUD();
    }

//    private void viewAllXML() {
//        setMainTextPaneText(warehouse.toString());
//    }

    private void DBViewAllData() {
    setMainTextPaneText(WarehouseDao.printAll());
    }

//    private void countAllProducts() {
//        setMainTextPaneText("Total number of products: " + warehouse.countProducts());
//    }

    private void DBCountAllProducts() {
        setMainTextPaneText("Total number of products: " + productDao.getAll().size());
    }

//    private void countAllDepartments() {
//        setMainTextPaneText("Total number of departments: " + warehouse.countDepartments());
//    }

    private void DBCountAllDepartments() {
        setMainTextPaneText("Total number of departments: " + departmentDao.getAll().size());
    }

}
