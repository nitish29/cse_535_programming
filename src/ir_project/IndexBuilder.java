package ir_project;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

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

        return this.termMap;

    }

    public void buildIndex ( Keyword termData, LinkedList<Document> postingList) {

        System.out.println( termData );
        System.out.println(postingList);

        LinkedList<Document> sortedDocumentPostingList = IndexBuilder.sortDocumentLinkedList ( postingList );
        documentMap.put(termData, sortedDocumentPostingList);



    }

    // This function sorts the posting according to increasing Document Ids

    public static LinkedList<Document> sortDocumentLinkedList ( LinkedList<Document> postingList ) {

        ListIterator<Document> listIterator = postingList.listIterator();

        while ( listIterator.hasNext() ) {

            System.out.println(listIterator.next().documentId);

        }

        LinkedList<Document> sortedList = postingList;
        return sortedList;

    }


}
