package ir_project;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nitish on 10/14/15.
 */
public class IndexLineData {

    Keyword termData;
    Integer frequency;
    LinkedList<Document> postingList;

    public IndexLineData(Keyword termData, Integer frequency, LinkedList<Document> postingList ) {
        this.termData = termData;
        this.frequency = frequency;
        this.postingList = postingList;
    }
}
