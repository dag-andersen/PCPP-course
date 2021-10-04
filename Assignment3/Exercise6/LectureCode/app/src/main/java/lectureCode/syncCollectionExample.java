package lectureCode;
import java.util.*;  
public class syncCollectionExample { 
  public static void main(String[] args) {  new syncCollectionExample(); }

 public String getLast(ArrayList<String> l) {
    int last= l.size()-1;
    return l.get(last);
  }

  public static void delete(ArrayList<String> l) {
    int last= l.size()-1;
    l.remove(last);
  }

  public syncCollectionExample() {
    //Create vector object   
    ArrayList<String> a= new ArrayList<String>(); 
 
    //Add values in the vector  
    a.add("A");  
    a.add("B");  
    a.add("C");  
 
    //Create a synchronized view  
    Collection<String> synColl = Collections.synchronizedCollection(a);  
    System.out.println("Synchronized view is :"+synColl); 
    
    System.out.println("Last:"+getLast(new ArrayList<String>(synColl))); 
  }  
} 