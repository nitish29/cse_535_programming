package ir_project;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by nitish on 10/15/15.
 */
public class TopKTerm {

    String term;
    Integer postingListSize;

    public TopKTerm(String term, int postingListSize) {
        this.term = term;
        this.postingListSize = postingListSize;
    }

    public static String getTopKTerms( ArrayList<TopKTerm> sortedList ,int n ) {

        String topK = "";

        for ( int i = 0; i < n; i++ ) {

            //System.out.println(sortedList.get(i).term.term );

            topK = String.join(" , ", Arrays.asList(sortedList.get(i).term));

        }

        return topK;

    }
}
