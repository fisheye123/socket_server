package database;

import me.paraweb.orderbuilder.Orders.Order;
import me.paraweb.orderbuilder.Orders.OrderComponents;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {

    DBWorker worker = new DBWorker();
    ArrayList<OrderComponents> storArr;
    String storStr;
    ResultSet resultSet;
    int id;
    String fileName = "id.txt";

    public Main(){
    }

    public void ProjectList (Order project) {
        try {
            CreateDB(project.getProjectName(), project.getClient());
            PushDB(project, "Дизайн", project.design.size());
            PushDB(project, "Вёрстка", project.pageProofs.size());
            PushDB(project, "Программирование", project.programming.size());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateDB(String project, String client) throws ClassNotFoundException, SQLException
    {
        String path = "C:\\Users\\Виктория\\IdeaProjects\\untitled\\src\\test\\java";
        File file = new File(path, fileName);
        try {
            if(!file.exists()){
                System.out.println("Файл создан.");
                FileWriter writer = new FileWriter(path);
                writer.write(1);
            }
            String str1;
            String str2 = null;
            try {
                BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
                
                while ((str1 = in.readLine()) != null) {
                    str2 = str1;
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            id = Integer.parseInt(str2);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        Statement statement = null;
        try {
            statement = worker.getConnection().createStatement();
            String query1 = "CREATE TABLE design" + id + " (id INT NOT NULL AUTO_INCREMENT, name CHAR(45), time INT (3), rate INT(6), PRIMARY KEY(id));";
            String query2 = "CREATE TABLE pageproofs" + id + " (id INT NOT NULL AUTO_INCREMENT, name CHAR(45), time INT (3), rate INT(6), PRIMARY KEY(id));";
            String query3 = "CREATE TABLE programming" + id + " (id INT NOT NULL AUTO_INCREMENT, name CHAR(45), time INT (3), rate INT(6), PRIMARY KEY(id));";
            statement.executeUpdate(query1);
            statement.executeUpdate(query2);
            statement.executeUpdate(query3);
            String dbproject = String.format("INSERT INTO listofprojects (id, projectname, design, pageproofs, programming, clientname) VALUES (%d, '%s', '%s', '%s', '%s', '%s');", id, project, "design"+id, "pageproofs"+id, "programming"+id, client);
            statement.executeUpdate(dbproject);

            id++;

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.print(id);
            } finally {
                out.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Таблицы созданы.");
                statement.close();
            } catch(SQLException se) {  }
        }
    }

    public void PushDB (Order clientSess, String storage, int size) {
        int rate = 0;
        String temp = Integer.toString(id-1);
        switch (storage) {
            case "Дизайн":
                storArr = clientSess.design;
                storStr = "design"+temp;
                rate = clientSess.getDesignRate();
                break;
            case "Программирование":
                storArr = clientSess.programming;
                storStr = "programming"+temp;
                rate = clientSess.getProgrammingRate();
                break;
            case "Вёрстка":
                storArr = clientSess.pageProofs;
                storStr = "pageproofs"+temp;
                rate = clientSess.getPageProofsRate();
                break;
        }

        for (int i = 0; i < size; i++){
            Statement statement = null;
            try {
                statement = worker.getConnection().createStatement();
                statement.executeUpdate(String.format("INSERT INTO mydbtest.%s (name, time, rate) VALUES ('%s', %d, %d)",
                        storStr, storArr.get(i).getComponentName(), storArr.get(i).getHourse(), rate));
                System.out.println("Записи в таблицы (design, pageproofs, programming) добавлены.");
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try { statement.close(); } catch(SQLException se) {  }
            }
        }
    }

    public void PullDB (Order order, String table, String storage) {
        storStr = table;
        String query = "select * from " + storStr + ";";
        Statement statement;
        try {
            statement = worker.getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            switch (storage) {
                case "Дизайн":
                    while (resultSet.next()) {
                        order.setDesignRate(resultSet.getInt(4));
                    }
                    break;
                case "Программирование":
                    while (resultSet.next()) {
                        order.setProgrammingRate(resultSet.getInt(4));
                    }
                    break;
                case "Вёрстка":
                    while (resultSet.next()) {
                        order.setPageProofsRate(resultSet.getInt(4));
                    }
                    break;
            }

            resultSet = statement.executeQuery(query);
            switch (storage) {
                case "Дизайн":
                    while (resultSet.next()) {
                        OrderComponents orderComponents = new OrderComponents(resultSet.getString(2), resultSet.getInt(3));
                        order.AddComponent("Дизайн", orderComponents);
                    }
                    break;
                case "Программирование":
                    while (resultSet.next()) {
                        OrderComponents orderComponents = new OrderComponents(resultSet.getString(2), resultSet.getInt(3));
                        order.AddComponent("Программирование", orderComponents);
                    }
                    break;
                case "Вёрстка":
                    while (resultSet.next()) {
                        OrderComponents orderComponents = new OrderComponents(resultSet.getString(2), resultSet.getInt(3));
                        order.AddComponent("Вёрстка", orderComponents);
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order GetTemplate (int id) {
        Order order = new Order("", "");
        Statement statement;
        String query;

        if (id == 0) {
            System.out.println("Get the standard template.");
            query = "select * from hospital;";
            try {
                statement = worker.getConnection().createStatement();
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    OrderComponents orderComponents = new OrderComponents(resultSet.getString(2), 0);
                    order.AddComponent("Дизайн", orderComponents);
                    order.AddComponent("Программирование", orderComponents);
                    order.AddComponent("Вёрстка", orderComponents);
                }
                order.setProjectName("Hospital");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return order;
        }
        else {
            System.out.println("Get a custom template.");
            query = String.format("SELECT * FROM listofprojects WHERE id = %d;", id);
            String design;
            String pageproofs;
            String programming;
            try {
                statement = worker.getConnection().createStatement();
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    order.setProjectName(resultSet.getString(2));
                    order.setClient(resultSet.getString(6));

                    design = resultSet.getString(3);
                    PullDB(order, design, "Дизайн");
                }
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    pageproofs = resultSet.getString(4);
                    PullDB(order, pageproofs, "Вёрстка");
                }
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    programming = resultSet.getString(5);
                    PullDB(order, programming, "Программирование");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return order;
        }
    }

    public String[] GetDBList () throws SQLException {
        String query;
        Statement statement;
        String[] result;
        statement = worker.getConnection().createStatement();
        query = "select count(*) from listofprojects;";
        resultSet = statement.executeQuery(query);

        int count = 0;
        if(resultSet.next()) {
            count = resultSet.getInt(1);
        }
        System.out.println("Number of templates: " + count);
        result = new String[count];

        query = "select projectname from listofprojects;";
        resultSet = statement.executeQuery(query);
        int i = 0;
        while (resultSet.next()) {
            result[i] = resultSet.getString(1);
            System.out.println(i + ": " + result[i]);
            i++;
        }
        return result;
    }

}
