package ir_project;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nitish on 10/14/15.
 */
public class DaatMap {

    HashMap<Keyword, LinkedList<Document>> documentMap;

    public DaatMap( ) {

        this.documentMap = new HashMap<>();
        //documentMap.put(termData, postingList);

    }

    public void putData ( Keyword termData, LinkedList<Document> postingList ) {

        documentMap.put(termData, postingList);

    }

}
