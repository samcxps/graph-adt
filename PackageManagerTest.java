import static org.junit.Assert.fail;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Samson Cain's JUnit class for PackageManager testing
 *
 * @author Samson Cain
 * @email srcain@wisc.edu
 * @class CS400 - Programming 3
 * @lecture 001
 * 
 * @project p4 Package Manager
 * 
 * @date November 11, 2019
 * 
 * @filename PackageManagerTest.java
 */
class PackageManagerTest {
  
  PackageManager pkgManager;
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {
    pkgManager = createPackageManagerInstance();
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {
    pkgManager = null;
  }
  
  /**
   * returns new PackageManager instance
   * 
   * @return PackageManager
   */
  protected PackageManager createPackageManagerInstance() {
    return new PackageManager();
  }

  /**
   * Tests building graph from valid json 
   * and loading json with no issues
   */
  @Test
  void testGraph_001_build_valid_graph() {
    try {
      pkgManager.constructGraph("valid.json");
    } catch (Exception e) {
      fail("Could not build Graph from valid JSON file");
    }
  }
  
  /**
   * Tests building graph from cyclic json and 
   * loading json with no issues
   */
  @Test
  void testGraph_002_build_cyclic_graph() {
    try {
      pkgManager.constructGraph("cyclic.json");
    } catch (Exception e) {
      fail("Could not build Graph from valid JSON file");
    }
  }
  
  /**
   * Tests building graph from json with shared dependencies 
   * and loading json with no issues
   */
  @Test
  void testGraph_003_build_shared_dependency_graph() {
    try {
      pkgManager.constructGraph("shared_dependencies.json");
    } catch (Exception e) {
      fail("Could not build Graph from valid JSON file");
    }
  }
  
  /**
   * Test that CycleException is thrown when loading the
   * cyclic JSON and attempting to get install order
   */
  @Test
  void testGraph_004_cycle_exception() {
    try {
      pkgManager.constructGraph("cyclic.json");
      pkgManager.getInstallationOrder("A");
      
    } catch (CycleException e) {
      
    } catch (Exception e) {
      fail("Unkown exception ocurred: " + e);
    }
  }
  
  /**
   * Test that CycleException is not thrown when loading the
   * shared_dependency JSON and attempting to get install order
   */
  @Test
  void testGraph_005_no_cycle_exception() {
    try {
      pkgManager.constructGraph("shared_dependencies.json");
      pkgManager.getInstallationOrder("A");
      
    } catch (CycleException e) {
      fail("cycle exception ocurred when it should not have: " + e);
    } catch (Exception e) {
      fail("Unkown exception ocurred: " + e);
    }
  }
  
  /**
   * Test get max dependencies works when loading
   * valid json
   */
  @Test
  void testGraph_006_get_max_dependencies() {
    try {
      pkgManager.constructGraph("valid.json");
      if (!pkgManager.getPackageWithMaxDependencies().equals("A")) {
        fail("getPackageWithMaxDependencies() should return 'A' but it did not");
      }
      
    } catch (Exception e) {
      fail("Unkown exception ocurred: " + e);
    }
  }
  
  /**
   * Test exception is called if JSON file is not present
   */
  @Test
  void testGraph_007_construct_graph_no_json() {
    try {
      pkgManager.constructGraph("non-existent.json");
      
      
    } catch (FileNotFoundException e) {
      
    } catch (Exception e) {
      fail("Unkown exception ocurred: " + e);
    }
  }

}
