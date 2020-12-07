package lab3;

import db.ConnectionService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import static lab3.Objects.*;
import static lab3.ViewType.*;
import static lab3.Operation.*;

public class Server {

    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) throws Exception {
        try {
            try {
                server = new ServerSocket(8080);

                System.out.println("Server is running!");

                while (true) {
                    try (Socket clientSocket = server.accept()) {
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                        String word = in.readLine();
                        System.out.println("Server received message: " + word);

                        if (word.equals("exit")) {
                            break;
                        }

                        out.write(processClientMessage(word) + "\n");
                        out.flush();
                    } finally {
                        in.close();
                        out.close();
                    }
                }
            } finally {
                System.out.println("Shutting down the server");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static String processClientMessage(String mes) throws Exception {
        Operation type;
        int index = 0;
        System.out.println("Operation: ");
        if (mes.indexOf("Add") == 0) {
            type = Operation.Add;
            index += "Add".length();
            System.out.println("Add");
        } else if (mes.indexOf("Delete") == 0) {
            type = Operation.Delete;
            index += "Delete".length();
            System.out.println("Delete");
        } else if (mes.indexOf("Update") == 0) {
            type = Operation.Update;
            index += "Update".length();
            System.out.println("Update");
        } else if (mes.indexOf("Calculate") == 0) {
            type = Operation.Calculate;
            index += "Calculate".length();
            System.out.println("Calculate");
        } else if (mes.indexOf("Show") == 0) {
            type = Operation.Show;
            index += "Show".length();
            System.out.println("Show");
        } else {
            type = Operation.Unknown;
            System.out.println("Unknown");
        }
        System.out.println("index = " + index);

        System.out.println("Object is ");
        Object obj = Objects.Unknown;
        ViewType show_type = ViewType.Unknown;
        switch (type) {
            case Add:
            case Update:
            case Delete:
                if (mes.indexOf("Product", index) == index) {
                    obj = Product;
                    index += "Model".length();
                    System.out.println("Model");
                } else if (mes.indexOf("Manufacturer", index) == index) {
                    obj = Department;
                    index += "Manufacturer".length();
                    System.out.println("Manufacturer");
                }
                ++ index; // space after object type in client msg
                break;
            case Show:
                System.out.println("Show type is ");
                if (mes.indexOf("ManufacturerList", index) == index) {
                    show_type = ViewType.DepartmentList;
                    index += "ManufacturerList".length();
                    System.out.println("ManufacturerList");
                } else if (mes.indexOf("ModelsWithManufacturer", index) == index) {
                    show_type = ViewType.ProductsWithDepartments;
                    index += "ModelsWithManufacturer".length();
                    System.out.println("ModelsWithManufacturer");
                } else if (mes.indexOf("ModelsByManufacturer", index) == index) {
                    show_type = ViewType.ProductsByDepartment;
                    index += "ModelsByManufacturer".length();
                    ++index;
                    System.out.println("ModelsByManufacturer");
                }
                break;
            case Unknown:
                return "Wrong operation type provided. Try again.";
        }

        if (obj == Objects.Unknown && show_type == ViewType.Unknown && type != Operation.Calculate) {
            return "Wrong client message. Try again";
        }

        if (type == Operation.Add || type == Operation.Update || type == Operation.Delete
                || show_type == ViewType.ProductsByDepartment) {
            return makeQuery(type, (Objects) obj, show_type, mes.substring(index));
        } else {
            return makeQuery(type, (Objects) obj, show_type, "");
        }
    }

    public static String makeQuery(Operation type, Objects obj, ViewType show_type, String query) throws Exception {
        ConnectionService connectionService = new ConnectionService();
//        Model model = new Model("CarShowroom", "localhost", 3306);
//        Manufacturer manufacturer = new Manufacturer("CarShowroom", "localhost", 3306);
        switch (obj) {
            case Product:
                switch (type) {
                    case Add:
                    {
                        // String name, int man_id, int col_id, int year, int eng_cap, int count
                        int index = 0;
                        String name = "";
                        // man_id, col_id, year, eng_cap, count;
                        Vector<Integer> vals = new Vector<>();

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                name = query.substring(0, ind);
                                index += ind;
                                ++ index;
                            }
                            System.out.println("index = " + index);
                            try {
                                for (int i = 0; i < 4; ++i) {
                                    ind = query.indexOf(" ", index);
                                    if (ind != -1) {
                                        vals.add(Integer.parseInt(query.substring(index, ind)));
                                        index = ind + 1;
                                    }

                                    if (i == 3) {
                                        // add last number (no space after last number)
                                        vals.add(Integer.parseInt(query.substring(index)));
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                return "Error while processing query: " + e.getMessage();
                            }
                        }

                        boolean is_ok = connectionService.addProducts(name, vals.elementAt(0), vals.elementAt(1),
                                vals.elementAt(2), vals.elementAt(3));

                        if (is_ok) {
                            return "Product added successfully";
                        } else {
                            return "Error while adding product";
                        }
                    }
                    case Update:
                    {
                        // String name, int man_id, int col_id, int year, int eng_cap, int count
                        int index = 0;
                        String name = "";
                        // man_id, col_id, year, eng_cap, count;
                        Vector<Integer> vals = new Vector<>();

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                vals.add(Integer.parseInt(query.substring(0, ind)));
                                index = (ind + 1);
                            }

                            ind = query.indexOf(" ", index);
                            if (ind != -1) {
                                name = query.substring(index, ind);
                                index = (ind + 1);
                            }

                            try {
                                for (int i = 0; i < 4; ++i) {
                                    ind = query.indexOf(" ", index);
                                    if (ind != -1) {
                                        vals.add(Integer.parseInt(query.substring(index, ind)));
                                        index = (ind + 1);
                                    }

                                    if (i == 3) {
                                        // add last number (no space after last number)
                                        vals.add(Integer.parseInt(query.substring(index)));
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                return "Error while processing query";
                            }
                        }

                        boolean is_ok = connectionService.updateProduct(name, vals.elementAt(0), vals.elementAt(1), vals.elementAt(2));

                        if (is_ok) {
                            return "Product updated successfully";
                        } else {
                            return "Error while updating product";
                        }
                    }
                    case Delete:
                    {
                        int index = 0;
                        int id = 0;

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                id = Integer.parseInt(query.substring(0, ind));
                            } else {
                                id = Integer.parseInt(query);
                            }
                        }

                        boolean is_ok = connectionService.deleteProduct(id);

                        if (is_ok) {
                            return "Product deleted succsessfully";
                        } else {
                            return "Error while deleting product";
                        }
                    }
                    default:
                        return "Wrong operation for Product object.";
                }
            case Department:
                switch (type) {
                    case Add:
                    {
                        // String name, Calendar foundation_date (year, month, day)
                        int index = 0;
                        String name = "";
                        Vector<Integer> vals = new Vector<>();

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                name = query.substring(0, ind);
                                index += ind;
                                ++ index;
                            }
                            System.out.println("index = " + index);

                            try {
                                for (int i = 0; i < 3; ++i) {
                                    ind = query.indexOf(" ", index);
                                    if (ind != -1) {
                                        vals.add(Integer.parseInt(query.substring(index, ind)));
                                        index = ind + 1;
                                    }

                                    if (i == 2) {
                                        // add last number (no space after last number)
                                        vals.add(Integer.parseInt(query.substring(index)));
                                        break;
                                    }
                                }

                                System.out.println("Foundation date:");
                                for (var el : vals) {
                                    System.out.println(el);
                                }

                            } catch (Exception e) {
                                return "Error while processing query: " + e.getMessage();
                            }
                        }

//                        System.out.println("");
                        boolean is_ok = connectionService.addDepartment(name, vals.elementAt(0));

                        if (is_ok) {
                            return "Department added successfully";
                        } else {
                            return "Error while adding department";
                        }

                    }
                    case Delete:
                    {
                        int id = 0;

                        // read query
                        {
                            int ind = query.indexOf(" ");
                            if (ind != -1) {
                                id = Integer.parseInt(query.substring(0, ind));
                            } else {
                                id = Integer.parseInt(query);
                            }
                        }

                        boolean is_ok = connectionService.deleteDepartment(id);

                        if (is_ok) {
                            return "Department deleted successfully";
                        } else {
                            return "Error while deleting department";
                        }
                    }
                    default:
                        return "Wrong operation for Department object.";
                }
            case Unknown:
                switch (type) {
                    case Show:
                        switch (show_type) {
                            case DepartmentList:
                                return connectionService.showDepartments();
                            case ProductsByDepartment:
                            {
                                int man_id = 0;

                                // read query
                                {
                                    int ind = query.indexOf(" ");
                                    if (ind != -1) {
                                        man_id = Integer.parseInt(query.substring(0, ind));
                                    } else {
                                        man_id = Integer.parseInt(query);
                                    }
                                }

                                return connectionService.findProductsByDepartmentID(man_id);
                            }
                            case ProductsWithDepartments:
                                return connectionService.showProductsByDepartment();
                            default:
                                return "Wrong show type provided";
                        }
                    default:
                        return "Wrong operation for non-selected object.";
                }
            default:
                return "Something went wrong";
        }
    }

}