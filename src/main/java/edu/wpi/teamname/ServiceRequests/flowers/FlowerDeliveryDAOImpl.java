package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class FlowerDeliveryDAOImpl implements IDAO<FlowerDelivery, Integer> {

  @Getter HashMap<Integer, FlowerDelivery> requests = new HashMap<>();
  private dbConnection connection = dbConnection.getInstance();
  @Getter String name;

  public FlowerDeliveryDAOImpl() {
    connection = dbConnection.getInstance();
  }

  public void initTable(String name) {
    this.name = name;
    try {
      Statement st = connection.getConnection().createStatement();

      String flowerRequestsTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + name
              + " "
              + "(deliveryID int UNIQUE PRIMARY KEY,"
              + "cart Varchar(60000),"
              + "orderDate Date,"
              + "orderTime Time,"
              + "room Varchar(60000),"
              + "orderedBy Varchar(60000),"
              + "assignedTo Varchar(60000),"
              + "orderStatus Varchar(60000),"
              + "cost DOUBLE PRECISION)";

      st.execute(flowerRequestsTableConstruct);

      // Move to hashmap requests

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Could not create flowerRequest");
      e.printStackTrace();
    }
  }

  @Override
  public void dropTable() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String drop = "DROP TABLE IF EXISTS " + name + " CASCADE";
      stmt.executeUpdate(drop);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void constructFromRemote() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfFlowerDeliveries = "SELECT * FROM " + name;
      ResultSet data = stmt.executeQuery(listOfFlowerDeliveries);
      while (data.next()) {
        int ID = data.getInt("deliveryID");
        String cart = data.getString("cart");
        Date date = data.getDate("orderDate");
        Time time = data.getTime("orderTime");
        String room = data.getString("room");
        String orderedBy = data.getString("orderedBy");
        String assignedTo = data.getString("assignedTo");
        String orderStatus = data.getString("orderStatus");
        double cost = data.getDouble("cost");

        FlowerDelivery fd =
            new FlowerDelivery(
                ID, cart, date, time, room, orderedBy, assignedTo, orderStatus, cost);
        requests.put(ID, fd);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println(
          "Error accessing the remote and constructing the list of FlowerDeliveries");
    }
  }

  @Override
  public void loadRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the flowerDeliveries from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the flowerDeliveries to the server");
        // constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      try {
        PreparedStatement stmt =
            connection
                .getConnection()
                .prepareStatement(
                    "INSERT INTO "
                        + name
                        + " (deliveryID, cart, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          stmt.setInt(1, Integer.parseInt(fields[0]));
          stmt.setString(2, fields[1]);
          stmt.setDate(3, Date.valueOf(fields[2]));
          stmt.setTime(4, Time.valueOf(fields[3]));
          stmt.setString(5, fields[4]);
          stmt.setString(6, fields[5]);
          stmt.setString(7, fields[6]);
          stmt.setString(8, fields[7]);
          stmt.setDouble(9, Double.parseDouble(fields[8]));
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getSQLState());
        System.out.println(
            "Error accessing the remote and constructing list of requests in remote");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) throws IOException {
    BufferedWriter fileWriter;
    fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write(
        "deliveryID,cart,orderDate,orderTime,room,orderedBye,assignedTo,orderStatus,cost)");
    for (FlowerDelivery flowerDelivery : requests.values()) {
      fileWriter.newLine();
      fileWriter.write(flowerDelivery.toCSVString());
    }
  }

  /**
   * Returns list of all FlowerDeliveries within the deliveries HashMap
   *
   * @return List of all FlowerDeliveries on success
   */
  @Override
  public List<FlowerDelivery> getAll() {
    return requests.values().stream().toList();
  }

  @Override
  public FlowerDelivery getRow(Integer target) {
    return null;
  }

  /**
   * Gets FlowerDelivery
   *
   * @param ID: ID of FlowerDelivery wanted
   * @return request (FlowerDelivery) on success, otherwise returns null or exception
   */
  // TODO: Change to hashmap
  public FlowerDelivery get(int ID) {
    return requests.get(ID);
  }

  @Override
  public void delete(Integer ID) {
    try {
      PreparedStatement deleteFlower =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + name + " WHERE deliveryID = ?");

      deleteFlower.setInt(1, ID);
      deleteFlower.execute();

      requests.remove(ID);

      System.out.println("FlowerRequest deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public void add(FlowerDelivery request) {
    System.out.println("Here in add");
    requests.put(request.getID(), request);
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (deliveryID, cart, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost)"
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, request.getID());
      preparedStatement.setString(2, request.getCart());
      preparedStatement.setDate(3, request.getDate());
      preparedStatement.setTime(4, request.getTime());
      preparedStatement.setString(5, request.getRoom());
      preparedStatement.setString(6, request.getOrderedBy());
      preparedStatement.setString(7, request.getAssignedTo());
      preparedStatement.setString(8, request.getOrderStatus());
      preparedStatement.setDouble(9, request.getCost());

      preparedStatement.executeUpdate();

      requests.put(request.getID(), request);

    } catch (SQLException e) {
      System.out.println("Excepetion:");
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }
}
