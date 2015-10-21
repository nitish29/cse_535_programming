package ir_project;

import java.util.LinkedList;

/**
 * Created by nitish on 10/14/15.
 */
public class IndexLineData {

    public String term;
    public Integer frequency;
    public LinkedList<Document> postingList;

    public IndexLineData(String term, Integer frequency, LinkedList<Document> postingList ) {
        this.term = term;
        this.frequency = frequency;
        this.postingList = postingList;
    }
}
