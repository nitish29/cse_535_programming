package ir_project;

import java.util.LinkedList;

/**
 * Created by nitish on 10/14/15.
 */
public class IndexLineData {

    Keyword term;
    Integer frequency;
    LinkedList<Document> postingList;

    public IndexLineData(Keyword term, Integer frequency, LinkedList<Document> postingList ) {
        this.term = term;
        this.frequency = frequency;
        this.postingList = postingList;
    }
}
