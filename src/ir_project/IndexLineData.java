package ir_project;

import java.util.LinkedList;



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
