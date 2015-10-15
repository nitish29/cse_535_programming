package ir_project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nitish on 10/14/15.
 */
public class DaatMap {

    Map<Keyword, Document> DocumentMap = new HashMap<>();

    public DaatMap( Map<Keyword, Document> documentMap ) {

        DocumentMap = documentMap;

    }
}
