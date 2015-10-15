package ir_project;

import java.util.List;

/**
 * Created by nitish on 10/14/15.
 */
public class IndexLineData {

    Keyword term;
    Integer frequency;
    List<Document> documentList;
    Integer size;

    public IndexLineData(Keyword term, Integer frequency, List<Document> documentList, Integer size) {
        this.term = term;
        this.frequency = frequency;
        this.documentList = documentList;
        this.size = size;
    }
}
