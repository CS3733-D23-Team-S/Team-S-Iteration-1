package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.Edge;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.databaseredo.orms.NodeType;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import edu.wpi.teamname.pathfinding.MapEditorEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MapEditorController {

  DataBaseRepository dataBase;
  @FXML MFXButton mapEditorToHomeButton;
  @FXML TableView<Object> nodeTable;
  @FXML TableView<Object> locationTable;
  @FXML TableView<Edge> edgeTable;
  @FXML TableView<Object> moveTable;
  @FXML TableColumn<Object, Integer> ntNodeIDCol; // = new TableColumn<>("Node ID");
  @FXML TableColumn<Object, Integer> xCoordCol; // = new TableColumn<>("XCoord");
  @FXML TableColumn<Object, String> yCoordCol; // = new TableColumn<>("YCoord");
  @FXML TableColumn<Object, String> floorCol; // = new TableColumn<>("Floor");
  @FXML TableColumn<Object, String> buildingCol; // = new TableColumn<>("Building");
  @FXML TableColumn<Object, NodeType> nodeTypeCol; // = new TableColumn<>("Node Type");
  @FXML TableColumn<Object, String> longNameCol; // = new TableColumn<>("Long Name");
  @FXML TableColumn<Object, String> shortNameCol; // = new TableColumn<>("Short Name");
  @FXML TableColumn<Object, Date> recentMoveCol; // = new TableColumn<>("Most Recent Move");
  @FXML TableColumn<Edge, Node> startNodeCol; // = new TableColumn<>("Start Node");
  @FXML TableColumn<Edge, Node> endNodeCol; // = new TableColumn<>("End Node");
  @FXML TableColumn<Object, Integer> mtNodeIDCol; // = new TableColumn<>("Node ID");
  @FXML TableColumn<Object, String> locationCol; // = new TableColumn<>("Location");
  @FXML TableColumn<Object, ArrayList<LocalDate>> datesCol; // = new TableColumn<>("Dates");

  public void createLists() {
    System.out.println("test");
    MapEditorEntity mee = new MapEditorEntity();
    final ObservableList nodeList = FXCollections.observableList(dataBase.getNodeDAO().getAll());
    final ObservableList edgeList = FXCollections.observableList(dataBase.getEdgeDAO().getAll());
    final ObservableList locationList =
        FXCollections.observableList(dataBase.getLocationDAO().getAll());
    final ObservableList moveList = FXCollections.observableList(dataBase.getMoveDAO().getAll());

    nodeTable.getItems().addAll(nodeList);

    edgeTable.getItems().addAll(edgeList);

    for (int i = 0; i < locationList.size(); i++) {
      locationTable.getItems().add(locationList.get(i));
    }
    for (int i = 0; i < moveList.size(); i++) {
      moveTable.getItems().add(moveList.get(i));
    }
  }

  public void initialize() {
    dataBase = DataBaseRepository.getInstance();
    mapEditorToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    ntNodeIDCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xCoordCol.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
    yCoordCol.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
    floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
    buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));

    nodeTypeCol.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    shortNameCol.setCellValueFactory(new PropertyValueFactory<>("shortName"));
    longNameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    recentMoveCol.setCellValueFactory(new PropertyValueFactory<>("mostRecentMove"));

    startNodeCol.setCellValueFactory(
        (edge) -> {
          System.out.println("test");
          return new SimpleObjectProperty(edge.getValue().getStartNodeID());
        });
    endNodeCol.setCellValueFactory(new PropertyValueFactory<>("endNode"));

    mtNodeIDCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
    datesCol.setCellValueFactory(new PropertyValueFactory<>("dates"));

    createLists();
  }
}
