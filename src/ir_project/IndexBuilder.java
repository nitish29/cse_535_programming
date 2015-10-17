package ir_project;

import java.text.Collator;
import java.util.*;

/**
 * Created by nitish on 10/15/15.
 */
public class IndexBuilder {

    HashMap< String, LinkedList<Document>> termMap;
    HashMap< String, LinkedList<Document>> documentMap;
    ArrayList<TopKTerm> topTerm;

    public IndexBuilder() {

        this.termMap = new HashMap<>();
        this.documentMap = new HashMap<>();
        this.topTerm = new ArrayList<>();

    }

    public HashMap fetchDAATMap () {

        return this.documentMap;

    }

    public HashMap fetchTAATMap () {

        return this.termMap;

    }

    public ArrayList fetchTopKTermList () {

        return this.topTerm;

    }

    public void buildIndex ( String term, LinkedList<Document> postingList) {

        LinkedList<Document> sortedDocumentPostingList = IndexBuilder.sortDocumentLinkedList ( postingList );
        LinkedList<Document> sortedTermPostingList = IndexBuilder.sortTermLinkedList( postingList );
        TopKTerm termObject = new TopKTerm ( term, postingList.size() );
        topTerm.add( termObject );
        documentMap.put(term, sortedDocumentPostingList);
        termMap.put(term, sortedTermPostingList);



    }


    // This function sorts the posting according to increasing Document Ids

    public static LinkedList<Document> sortDocumentLinkedList ( LinkedList<Document> postingList ) {

        postingList.sort(new Comparator<Document>() {
            @Override
            public int compare(Document d1, Document d2) {

                return d1.documentId.compareTo( d2.documentId );

//                if ( o1.documentId < o2.documentId ){
//
//                    return -1;
//
//                } else if ( o1.documentId > o2.documentId ) {
//
//                    return 1;
//
//                }
//                return 0;
            }
        });


        LinkedList<Document> sortedList = postingList;
        return sortedList;

    }

    // This function sorts the posting according to decreasing term frequencies

    public static LinkedList<Document> sortTermLinkedList ( LinkedList<Document> postingList ) {

        postingList.sort(new Comparator<Document>() {
            @Override
            public int compare(Document d1, Document d2) {

                return d2.term_frequency.compareTo( d1.term_frequency );

            }
        });

        LinkedList<Document> sortedList = postingList;
        return sortedList;

    }

    public static ArrayList<TopKTerm> sortTopKTerms ( ArrayList<TopKTerm> initialList ) {

        initialList.sort(new Comparator<TopKTerm>() {
            @Override
            public int compare(TopKTerm t1, TopKTerm t2) {

                return t2.postingListSize.compareTo(t1.postingListSize);

            }
        });

        ArrayList<TopKTerm> sortedList = initialList;
        return sortedList;
    }


    // Reference function section
    //        while ( listIterator.hasNext() ) {
    //
    //            System.out.println(listIterator.next().documentId);
    //
    //        }


}
