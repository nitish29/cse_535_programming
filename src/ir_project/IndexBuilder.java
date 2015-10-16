package ir_project;

import java.util.HashMap;
import java.util.LinkedList;

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
        System.out.println( postingList );

        //LinkedList<Document> sortedDocumentPostingList = IndexBuilder.sortDocumentLinkedList ( LinkedList<Document> );

    }

//    public static LinkedList<Document> sortDocumentLinkedList (  ) {
    public static void sortDocumentLinkedList (  ) {

        LinkedList<Document> sortedList ;
        //return sortedList;
    }


}
