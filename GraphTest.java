import static org.junit.Assert.fail;
import java.util.Random;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Samson Cain's JUnit class for Graph testing
 *
 * @author Samson Cain
 * @email srcain@wisc.edu
 * @class CS400 - Programming 3
 * @lecture 001
 * 
 * @project p4 Package Manager
 * 
 * @date November 4, 2019
 * 
 * @filename GraphTest.java
 */

class GraphTest {
  
  Graph graph;
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
    graph = createGraphInstance();
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
    graph = null;
  }
  
  /**
   * returns new Graph instance
   * 
   * @return Graph
   */
  protected Graph createGraphInstance() {
    return new Graph();
  }
  
  /**
   * generates a random String for testing purposes
   * 
   * taken from https://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
   * 
   * @return String a random string
   */
  protected String randomString() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder str = new StringBuilder();
    
    Random rnd = new Random();
    while (str.length() < 10) {
        int index = (int) (rnd.nextFloat() * chars.length());
        str.append(chars.charAt(index));
    }
    return str.toString();
  }
  
  /**
   * Adds 1 vertex and checks order
   */
  @Test
  void testGraph_001_insert_1_vertex_check_size() {
    graph.addVertex(randomString());
    
    if(graph.order() != 1) {
      fail("Vertices should equal 1 but equals " + graph.order());
    } 
  }
  
  /**
   * Adds 10 vertex and checks order
   */
  @Test
  void testGraph_002_insert_10_vertex_check_size() {
    for(int i = 0; i < 10; i++) {
      graph.addVertex(randomString());
    }
    
    if(graph.order() != 10) {
      fail("Vertices should equal 10 but equals " + graph.order());
    } 
  }
  
  /**
   * Adds 100 vertex and checks order
   */
  @Test
  void testGraph_003_insert_100_vertex_check_size() {
    for(int i = 0; i < 100; i++) {
      graph.addVertex(randomString());
    }
    
    if(graph.order() != 100) {
      fail("Vertices should equal 100 but equals " + graph.order());
    } 
  }
  
  /**
   * Adds 2 vertex and then an edge between the two and checks size/order
   */
  @Test
  void testGraph_004_insert_2_vertex_add_edge() {
    graph.addVertex("vertex1");
    graph.addVertex("vertex2");
    
    if(graph.order() != 2) {
      fail("Vertices should equal 2 but equals " + graph.order());
    } 
    
    graph.addEdge("vertex1", "vertex2");
    if(graph.size() != 1) {
      fail("There should be 1 edge(s) but there is " + graph.size());
    }
  }
  
  /**
   * Adds 2 vertex and then an edge between the two. 
   * Then removes edge and checks size/order.
   */
  @Test
  void testGraph_005_insert_2_vertex_add_edge_remove_edge() {
    graph.addVertex("vertex1");
    graph.addVertex("vertex2");
    
    graph.addEdge("vertex1", "vertex2");
    
    graph.removeEdge("vertex1", "vertex2");
    if(graph.size() != 0) {
      fail("There should be 0 edge(s) but there is " + graph.size());
    }  }
  
  /**
   * Tests to see if adding an edge with an empty graph will correctly create
   * the two vertices required, as well as the edge.
   */
  @Test
  void testGraph_006_add_edge_with_empty_graph() {
    graph.addEdge("vertex1", "vertex2");
    if(graph.size() != 1) {
      fail("There should be 1 edge(s) but there is " + graph.size());
    }
    if(graph.order() != 2) {
      fail("Vertices should equal 2 but equals " + graph.order());
    } 
  }
  
  /**
   * Adds 100 vertex and removes them. Then checks order.
   */
  @Test
  void testGraph_007_insert_100_vertex_remove_check_size() {
    // storing random strings in array first so we can remove them as well
    String[] strings = new String[100];
    for(int i = 0; i < 100; i++) {
      strings[i] = randomString();
    }
    
    // add all strings in array to graph
    for(int i = 0; i < 100; i++) {
      graph.addVertex(strings[i]);
    }
    
    // check size
    if(graph.order() != 100) {
      fail("Vertices should equal 100 but equals " + graph.order());
    }
    
    // remove all strings from graph
    for(int i = 0; i < 100; i++) {
      graph.removeVertex(strings[i]);
    }
    
    // check size
    if(graph.order() != 0) {
      fail("Vertices should equal 0 but equals " + graph.order());
    }
  }
  
  /**
   * Add 2 identical vertex and make sure only one is added
   */
  @Test
  void testGraph_008_insert_2_identical_vertex() {
    graph.addVertex("v1");
    graph.addVertex("v1");
    
    // check size
    if(graph.order() != 1) {
      fail("Vertices should equal 1 but equals " + graph.order());
    }
  }
 

}
