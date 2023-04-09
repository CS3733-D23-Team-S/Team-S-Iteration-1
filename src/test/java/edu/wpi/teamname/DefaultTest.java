/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.LocationDAOImpl;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.NodeType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  public List<String> getListOfEligibleRooms() {
    LocationDAOImpl locationdao = new LocationDAOImpl();
    List<String> listOfEligibleRooms = new ArrayList<>();
    List<Location> locationList = new ArrayList<>(locationdao.getAll());
    locationList.forEach(System.out::println);

    NodeType[] nodeTypes = new NodeType[6];
    nodeTypes[0] = NodeType.ELEV;
    nodeTypes[1] = NodeType.EXIT;
    nodeTypes[2] = NodeType.HALL;
    nodeTypes[3] = NodeType.REST;
    nodeTypes[4] = NodeType.STAI;
    nodeTypes[5] = NodeType.BATH;

    boolean isFound;
    for (Location loc : locationList) { // hashmap
      isFound = false;
      for (NodeType nt : nodeTypes) {
        if (loc.getNodeType() == nt) {
          isFound = true;
          break;
        }
      }
      if (!isFound) listOfEligibleRooms.add(loc.getLongName());
    }
    Collections.sort(listOfEligibleRooms);

    return listOfEligibleRooms;
  }

  @BeforeAll
  static void setup() throws SQLException {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
  }

  /*@Test
  public void testAlgorithm() {
    AStar a = new AStar();
    List<Integer> res = new ArrayList<>();
    res.add(1805);
    res.add(1810);

    assertEquals(a.findPath("75 Lobby", "75 Lobby Information Desk"), res);

    List<Integer> res2 = new ArrayList<>();
    res2.add(640);
    res2.add(645);
    res2.add(650);
    res2.add(655);
    res2.add(660);

    assertEquals(a.findPath("Staircase K1 Floor 1", "Ambulance Parking Exit Floor 1"), res2);
  }*/

  @Test
  public void testLogin() throws Exception {
    getListOfEligibleRooms().forEach(System.out::println);
    /*LoginDAOImpl LDaoI = LoginDAOImpl.getInstance();
    Exception exception = assertThrows(Exception.class, () -> LDaoI.login("aaaa", "bbbb"));
    assertEquals("User does not exist", exception.getMessage());
    assertTrue(LDaoI.login("admin", "admin"));*/
  }
}
