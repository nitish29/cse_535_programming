package ir_project;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by nitish on 10/14/15.
 */
public class TaatMap {

    HashMap<Keyword, LinkedList<Document>> termMap;

    public TaatMap( Keyword termData, LinkedList<Document> postingList ) {

        this.termMap = new HashMap<>();
        //termMap.put(termData, postingList);

    }

    public void putData ( Keyword termData, LinkedList<Document> postingList ) {

        termMap.put(termData, postingList);

    }
}
