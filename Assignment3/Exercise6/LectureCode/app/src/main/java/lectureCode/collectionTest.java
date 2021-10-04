package lectureCode;
import java.util.*;  
public class collectionTest{ 

  public static void main(String[] args) { new collectionTest(); }

  public collectionTest() {
    Collection<String> obj = new ArrayList<>();  
    obj.add("PCPP-2021"); // not thread safe  

    Collection<String> synObj = Collections.synchronizedCollection(obj);  
    synObj.add("SSSIT"); // thread safe  
          
    //Below iteration not correct if other threads use synObj 
    Iterator<String> ite1 = synObj.iterator();  
    while (ite1.hasNext()) {  
      String s = ite1.next();  
      System.out.println(s);  
    }  
          
    //Below code is the right way to iterate synchronized collections  
    synchronized(synObj) {   
      Iterator<String> ite2 = synObj.iterator();  
      while (ite2.hasNext()) {  
        String s = ite2.next();  
        System.out.println(s);  
      }  
    }  
  }  
}  