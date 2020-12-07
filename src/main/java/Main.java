import dao.DepartmentDao;
import dao.ProductDao;
import dao.WarehouseDao;
import db.ConnectionService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Product;
import models.Warehouse;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ui.fxml"));
        stage.setTitle("User Registration");
        stage.setScene(new Scene(root, 800, 650));
        stage.show();
    }

//    public static void main(String[] args) {
//        WarehouseDao warehouseDao = new WarehouseDao();
//        Warehouse warehouse = warehouseDao.createWarehouse();
//        ProductDao productDao = new ProductDao();
//
//        Product product = productDao.getById(100, true);
//        System.out.println(product.toString());
//    }



}
