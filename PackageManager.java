import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Samson Cain's PackageManager class
 *
 * @author Samson Cain
 * @email srcain@wisc.edu
 * @class CS400 - Programming 3
 * @lecture 001
 * 
 * @project p4 Package Manager
 * 
 * @date November 9, 2019
 * 
 * @filename PackageManager.java
 */

/**
 *           PackageManager2 is used to process json package dependency files and provide function
 *           that make that information available to other users.
 * 
 *           Each package that depends upon other packages has its own entry in the json file.
 * 
 *           Package dependencies are important when building software, as you must install packages
 *           in an order such that each package is installed after all of the packages that it
 *           depends on have been installed.
 * 
 *           For example: package A depends upon package B, then package B must be installed before
 *           package A.
 * 
 *           This program will read package information and provide information about the packages
 *           that must be installed before any given package can be installed. all of the packages
 *           in
 * 
 *           You may add a main method, but we will test all methods with our own Test classes.
 */
public class PackageManager {

  private Graph graph;

  /*
   * Package Manager default no-argument constructor.
   * 
   * All we do is instantiate graph object
   */
  public PackageManager() {
    this.graph = new Graph();
  }

  private JSONObject json;

  /**
   * Takes in a file path for a json file and builds the package dependency graph from it.
   * 
   * @param jsonFilepath the name of json data file with package dependency information
   * 
   * @throws FileNotFoundException if file path is incorrect
   * @throws IOException if the give file cannot be read
   * @throws ParseException if the given json cannot be parsed
   */
  public void constructGraph(String jsonFilepath)
      throws FileNotFoundException, IOException, ParseException {
    Object obj = new JSONParser().parse(new FileReader(jsonFilepath));
    JSONObject jo = (JSONObject) obj;
    this.json = jo;

    // get packages array
    JSONArray packagesArray = (JSONArray) jo.get("packages");

    // get every package and add it as a vertex
    // WE ARE NOT ADDING EDGES YET
    for (Object o : packagesArray) {
      JSONObject pkg = (JSONObject) o;

      // extract name from object and add vertex
      String name = (String) pkg.get("name");
      this.graph.addVertex(name);

      // get dependency array object and add them as vertices
      JSONArray dependencies = (JSONArray) pkg.get("dependencies");
      for (Object dependency : dependencies) {
        this.graph.addVertex((String) dependency);
      }
    }

    // iterate over every package again and add dependencies to adjacency lists
    // for the correct package
    for (Object o : packagesArray) {
      JSONObject pkg = (JSONObject) o;

      // extract name from object and add vertex
      String name = (String) pkg.get("name");

      // extract dependencies from object and add edges
      JSONArray dependencies = (JSONArray) pkg.get("dependencies");
      for (Object dependency : dependencies) {
        this.graph.addEdge((String) dependency, name);
      }
    }
  }

  /**
   * Helper method to get all packages in the graph.
   * 
   * @return Set<String> of all the packages
   */
  public Set<String> getAllPackages() {
    return this.graph.getAllVertices();
  }

  /**
   * Given a package name, returns a list of packages in a valid installation order.
   * 
   * Valid installation order means that each package is listed before any packages that depend upon
   * that package.
   * 
   * @return List<String>, order in which the packages have to be installed
   * 
   * @throws CycleException if you encounter a cycle in the graph while finding the installation
   *         order for a particular package. Tip: Cycles in some other part of the graph that do not
   *         affect the installation order for the specified package, should not throw this
   *         exception.
   * 
   * @throws PackageNotFoundException if the package passed does not exist in the dependency graph.
   */
  public List<String> getInstallationOrder(String pkg)
      throws CycleException, PackageNotFoundException {

    // get all packages and
    List<String> allPackages = new ArrayList<String>(this.graph.getAllVertices());
    if (!allPackages.contains(pkg)) {
      throw new PackageNotFoundException();
    }

    ArrayList<String> order = new ArrayList<String>();
    this.getInstallationOrderHelper(pkg, order, new ArrayList<String>());

    return order;
  }

  /**
   * Helper method for getInstallationOrder(String pkg)
   * 
   * @param pkg to start at
   * @param order ArrayList to keep track of order
   * @param visited ArrayList to keep track nodes visited
   * 
   * @throws CycleException if you encounter a cycle in the graph while finding the installation
   *         order for a particular package. Tip: Cycles in some other part of the graph that do not
   *         affect the installation order for the specified package, should not throw this
   *         exception.
   */
  private void getInstallationOrderHelper(String pkg, ArrayList<String> order,
      ArrayList<String> visited) throws CycleException {
    visited.add(pkg);
    for (String edge : this.getPredecessorsFor(pkg)) {
      if (!order.contains(edge)) {
        if (visited.contains(edge)) {
          throw new CycleException();
        }
        this.getInstallationOrderHelper(edge, order, visited);
      }
    }
    order.add(pkg);
  }

  /**
   * Method to get all the predecessors for a given package
   * 
   * Used in the getInstallationOrderHelper() method
   * 
   * @param pkg to get predecessors for
   * 
   * @return List<String> containing all of the given packages predecessors
   */
  private List<String> getPredecessorsFor(String pkg) {
    List<String> temp = new ArrayList<String>();

    // get packages array
    JSONArray packagesArray = (JSONArray) this.json.get("packages");

    // iterate over packages
    for (Object o : packagesArray) {
      JSONObject p = (JSONObject) o;

      // if package name equals the one we are looking for
      String name = (String) p.get("name");
      if (name.equals(pkg)) {
        // get the predecessors and add them to the list
        JSONArray dependencies = (JSONArray) p.get("dependencies");
        for (Object dependency : dependencies) {
          temp.add((String) dependency);
        }
      }
    }

    return temp;
  }

  /**
   * Given two packages - one to be installed and the other installed, return a List of the packages
   * that need to be newly installed.
   * 
   * For example, refer to shared_dependecies.json - toInstall("A","B") If package A needs to be
   * installed and packageB is already installed, return the list ["A", "C"] since D will have been
   * installed when B was previously installed.
   * 
   * @return List<String>, packages that need to be newly installed.
   * 
   * @throws CycleException if you encounter a cycle in the graph while finding the dependencies of
   *         the given packages. If there is a cycle in some other part of the graph that doesn't
   *         affect the parsing of these dependencies, cycle exception should not be thrown.
   * 
   * @throws PackageNotFoundException if any of the packages passed do not exist in the dependency
   *         graph.
   */
  public List<String> toInstall(String newPkg, String installedPkg)
      throws CycleException, PackageNotFoundException {
    List<String> installedPkgDeps = this.getInstallationOrder(installedPkg);
    List<String> newPkgDeps = this.getInstallationOrder(newPkg);

    for (String vertex : installedPkgDeps) {
      newPkgDeps.remove(vertex);
    }

    return newPkgDeps;
  }

  /**
   * Return a valid global installation order of all the packages in the dependency graph.
   * 
   * assumes: no package has been installed and you are required to install all the packages
   * 
   * returns a valid installation order that will not violate any dependencies
   * 
   * @return List<String>, order in which all the packages have to be installed
   * @throws CycleException if you encounter a cycle in the graph
   */
  public List<String> getInstallationOrderForAllPackages() throws CycleException {
    List<String> visited = new ArrayList<String>();
    Stack<String> stack = new Stack<String>();

    for (String vertex : this.graph.getAllVertices()) {
      if (!visited.contains(vertex)) {
        getInstallationOrderForAllPackagesHelper(vertex, visited, stack);
      }
    }

    List<String> results = new ArrayList<String>();
    while (!stack.isEmpty()) {
      results.add(stack.pop());
    }

    return results;
  }

  /**
   * Recursive helper method for getInstallationOrderForAllPackages()
   * 
   * Just uses a BFS
   * 
   * @param pkg to start at
   * @param visited List<String> of visited nodes/vertices/packages
   * @param stack stack<String> stack to keep track of correct order
   * @throws CycleException 
   */
  private void getInstallationOrderForAllPackagesHelper(String pkg, List<String> visited,
      Stack<String> stack) {
    visited.add(pkg);

    for (String vertex : this.graph.getAdjacentVerticesOf(pkg)) {
      if (!visited.contains(vertex)) {
        getInstallationOrderForAllPackagesHelper(vertex, visited, stack);
      }
    }

    stack.push(pkg);
  }

  /**
   * Find and return the name of the package with the maximum number of dependencies.
   * 
   * Tip: it's not just the number of dependencies given in the json file. The number of
   * dependencies includes the dependencies of its dependencies. But, if a package is listed in
   * multiple places, it is only counted once.
   * 
   * Example: if A depends on B and C, and B depends on C, and C depends on D. Then, A has 3
   * dependencies - B,C and D.
   * 
   * @return String, name of the package with most dependencies.
   * @throws CycleException if you encounter a cycle in the graph
   */
  public String getPackageWithMaxDependencies() throws CycleException {
    String maxDependencies;

    // get all packages in graph
    Set<String> allPackages = this.getAllPackages();

    // create two new array lists for later
    List<String> longest = new ArrayList<String>();
    List<String> tempList;

    // instantiate maxDependencies
    maxDependencies = "";

    // loop through and look for the package with the most dependencies
    for (String pkg : allPackages) {

      try {
        tempList = this.getInstallationOrder(pkg);

        // check current largest amount of dependencies with the tempList
        if (tempList.size() > longest.size()) {
          longest = tempList;
          maxDependencies = pkg;
        }

      } catch (PackageNotFoundException e) {}

    }

    return maxDependencies;
  }

  public static void main(String[] args) {
    PackageManager pkgManager = new PackageManager();
    try {
       pkgManager.constructGraph("cyclic.json");
      // pkgManager.constructGraph("shared_dependencies.json");
       // pkgManager.constructGraph("valid.json");

      // System.out.println("getAllPackages(): " + pkgManager.getAllPackages());
      // System.out.println(pkgManager.getInstallationOrder("A"));
      // System.out.println(pkgManager.getInstallationOrderForAllPackages());
      System.out.println(pkgManager.getInstallationOrderForAllPackages());


    } catch (IOException | ParseException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
