package ir_project;

import java.text.Collator;
import java.util.*;

/**
 * Created by nitish on 10/15/15.
 */
public class IndexBuilder {

    HashMap< Keyword, LinkedList<Document>> termMap;
    HashMap< Keyword, LinkedList<Document>> documentMap;

    public IndexBuilder() {

        this.termMap = new HashMap<>();
        this.documentMap = new HashMap<>();

    }

    public HashMap fetchDAATMap () {

        return this.documentMap;

    }

    public HashMap fetchTAATMap () {

        return this.termMap;

    }

    public void buildIndex ( Keyword termData, LinkedList<Document> postingList) {

        LinkedList<Document> sortedDocumentPostingList = IndexBuilder.sortDocumentLinkedList ( postingList );
        LinkedList<Document> sortedTermPostingList = IndexBuilder.sortTermLinkedList(postingList);


//        ListIterator<Document> listIterator = sortedTermPostingList.listIterator();
//
//        while ( listIterator.hasNext() ) {
//
//            System.out.println(listIterator.next().documentId);
//
//        }


        documentMap.put(termData, sortedDocumentPostingList);
        termMap.put(termData, sortedTermPostingList);



    }

    // This function sorts the posting according to increasing Document Ids

    public static LinkedList<Document> sortDocumentLinkedList ( LinkedList<Document> postingList ) {

        postingList.sort(new Comparator<Document>() {
            @Override
            public int compare(Document o1, Document o2) {

                return o1.documentId.compareTo( o2.documentId );

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
            public int compare(Document o1, Document o2) {

                return o2.term_frequency.compareTo( o1.term_frequency );

            }
        });

        LinkedList<Document> sortedList = postingList;
        return sortedList;

    }


}
