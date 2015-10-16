package ir_project;

/**
 * Created by nitish on 10/15/15.
 */
public class TopKTerm {

    String term;
    Integer postingListSize;

    public TopKTerm(String term, Integer postingListSize) {
        this.term = term;
        this.postingListSize = postingListSize;
    }
}
