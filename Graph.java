import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Samson Cain's implementation of a directed and unweighted graph implementation
 *
 * @author Samson Cain
 * @email srcain@wisc.edu
 * @class CS400 - Programming 3
 * @lecture 001
 * 
 * @project p4 Package Manager
 * 
 * @date November 3, 2019
 * 
 * @filename Graph.java
 */
public class Graph implements GraphADT {

  /**
   * Define vertices Map<String, List<String>>
   * 
   * The key will be the vertex and the value will be an ArrayList of Strings which will be the
   * key's (vertex's) dependencies
   */
  private Map<String, List<String>> vertices;

  /*
   * Default no-argument constructor
   * 
   * All this does is initialize the HashMap
   */
  public Graph() {
    this.vertices = new HashMap<String, List<String>>();
  }

  /**
   * Add new vertex to the graph.
   *
   * If vertex is null or already exists, method ends without adding a vertex or throwing an
   * exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   * 
   * @param String vertex to add to graph
   */
  public void addVertex(String vertex) {
    // check if vertex is null or if vertex already exists
    if (vertex == null || this.vertices.containsKey(vertex)) {
      return;
    }
    
    // add new vertex to HashMap
    this.vertices.put(vertex, new ArrayList<String>());
  }

  /**
   * Remove a vertex and all associated edges from the graph.
   * 
   * If vertex is null or does not exist, method ends without removing a vertex, edges, or throwing
   * an exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   * 
   * @param String vertex vertex to remove from graph
   */
  public void removeVertex(String vertex) {
    // check if vertex is null or if vertex does not exist
    /**
     * TODO: Possibly wont work might need to do vertices.get(vertex) == null
     */
    if (vertex == null || !this.vertices.containsKey(vertex)) {
      return;
    }

    // iterate through vertices and remove edges to vertex
    for (String v : this.vertices.keySet()) { // uses keySet() to get all keys in a Set so we can
                                              // use enhanced for loop
      this.vertices.get(v).remove(v);
    }

    // remove vertex from graph
    this.vertices.remove(vertex);
  }

  /**
   * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and unweighted)
   * 
   * If either vertex does not exist, add vertex, and add edge, no exception is thrown. If the edge
   * exists in the graph, no edge is added and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge is not in the graph
   * 
   * @param String vertex1 vertex to add edge from
   * @param String vertex2 vertex to add edge to
   */
  public void addEdge(String vertex1, String vertex2) {
    // make sure both vertexes are not null
    if (vertex1 == null || vertex2 == null) {
      return;
    }

    // check if vertex1 is not in graph and add if necessary
    if (!this.vertices.containsKey(vertex1)) {
      this.addVertex(vertex1);
    }

    // check if vertex2 is not in graph and add if necessary
    if (!this.vertices.containsKey(vertex2)) {
      this.addVertex(vertex2);
    }

    // need to add edge vertex1 -> vertex2
    // make sure edge is not already in graph and add if it is not
    if (!this.vertices.get(vertex1).contains(vertex2)) {
      this.vertices.get(vertex1).add(vertex2);
    }
  }

  /**
   * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed and unweighted) 
   * 
   * If either vertex does not exist, or if an edge from vertex1 to vertex2 does not exist, no edge is
   * removed and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge from vertex1 to vertex2 is in the graph
   * 
   * @param String vertex1 vertex to remove edge from
   * @param String vertex2 vertex to remove edge to
   */
  public void removeEdge(String vertex1, String vertex2) {
    // make sure both vertexes are not null
    if (vertex1 == null || vertex2 == null) {
      return;
    }
    
    // check if vertex1 and vertex2 are in graph, return if they are not
    if (!this.vertices.containsKey(vertex1) || !this.vertices.containsKey(vertex2)) {
      return;
    }
    
    // make sure edge is in graph and then remove it
    if (this.vertices.get(vertex1).contains(vertex2)) {
      this.vertices.get(vertex1).remove(vertex2);
    }
  }

  /**
   * Returns a Set that contains all the vertices
   * 
   * @return Set<String> set of vertices
   */
  public Set<String> getAllVertices() {
    return this.vertices.keySet();
  }

  /**
   * Get all the neighbor (adjacent) vertices of a vertex
   * 
   * @param String vertex the vertex you want to get neighbors of
   * 
   * @return List<String> list of adjacent vertices
   */
  public List<String> getAdjacentVerticesOf(String vertex) {
    return this.vertices.get(vertex);
  }

  /**
   * Returns the number of edges in this graph.
   * 
   * @return int the number of edges
   */
  public int size() {
    int edges = 0;
    
    // iterate through all vertices and sum the amount of edges for each
    for (String v: this.vertices.keySet()) {
      edges += this.vertices.get(v).size();
    }
    
    return edges;
  }

  /**
   * Returns the number of vertices in this graph.
   * 
   * @return int the number of vertices
   */
  public int order() {
    return this.vertices.size();
  }

}
