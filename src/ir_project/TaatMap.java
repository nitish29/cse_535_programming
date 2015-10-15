package ir_project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nitish on 10/14/15.
 */
public class TaatMap {

    Map<Keyword, Document> TermMap = new HashMap<>();

    public TaatMap(Map<Keyword, Document> termMap) {

        TermMap = termMap;

    }
}
