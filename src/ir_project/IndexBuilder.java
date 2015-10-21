package ir_project;

import java.util.*;

/*
* This class as the name suggests builds the index for each term fetched from the index file
* Build index indexes the index file line by line
*
*
* */

public class IndexBuilder {

    HashMap< String, LinkedList<Document>> termMap;
    HashMap< String, LinkedList<Document>> documentMap;
    ArrayList<TopKTerm> topTerm;

    public IndexBuilder() {

        this.termMap = new HashMap<>();
        this.documentMap = new HashMap<>();
        this.topTerm = new ArrayList<>();

    }

    public HashMap<String, LinkedList<Document>> fetchDAATMap () {

        return this.documentMap;

    }

    public HashMap<String, LinkedList<Document>> fetchTAATMap () {

        return this.termMap;

    }

    public ArrayList<TopKTerm> fetchTopKTermList () {

        return this.topTerm;

    }

    //this function adds a new entry (a keyword and a posting linked list):
        // in the document hashmap ( linked list sorted by increasing document id);
        // in term hash map (  linked list sorted by decreasing term frequencies )
        // a keyword entered in the array list maintained for storing all the keywords
    // Note : buildIndex operation is done for all the terms in a single line of the index file, to build final indexes

    public void buildIndex ( String term, LinkedList<Document> postingList) {

        LinkedList<Document> sortedDocumentPostingList = IndexBuilder.sortDocumentLinkedList(postingList);
        LinkedList<Document> sortedTermPostingList = IndexBuilder.sortTermLinkedList(postingList);
        TopKTerm termObject = new TopKTerm ( term, postingList.size() );
        topTerm.add( termObject );
        documentMap.put(term, sortedDocumentPostingList);
        termMap.put(term, sortedTermPostingList);


    }


    // This function sorts the posting according to increasing Document Ids, and returns the sorted list for it to be added in the documentMap Hash Map

    public static LinkedList<Document> sortDocumentLinkedList ( LinkedList<Document> postingList ) {

        //create a new linked list to store all sorted linked list elements)
        LinkedList<Document> sorted_by_documents = new LinkedList<>();

        //loop over unsorted posting linked list
        for (Document document : postingList) {

            // initial index - which changes to the index value if a document id is found less than the other document id
            int index = -1;

            //if the sorted linked list is empty, add the linked list element
            if(sorted_by_documents.size() == 0){

                sorted_by_documents.add(document);

            } else {

                //if the sorted link list is not empty, compare all the elements in the sorted list to find an index where the new unsorted posting list element can be inserted
                for ( Document sorted_document : sorted_by_documents ) {

                    //match found for a suitable index to insert the element
                    if( sorted_document.documentId > document.documentId ) {

//                      get sorted document id index
                        index = postingList.indexOf( sorted_document );
                        break;

                    }
                }
                //add document in place of sorted doc
                if ( index != -1 ) {

                    sorted_by_documents.add( index, document );

                } else {

                    //as the element to be inserted has a document id higher than the existing documents in the sorted list, add it in the end of the list

                    sorted_by_documents.add( document );

                }

            }
        }


        LinkedList<Document> sortedList = sorted_by_documents;
        return sortedList;

    }

    // This function sorts the posting according to decreasing term frequencies, and returns the sorted list

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

    // This function sorts the top K terms array list in order of decreasing posting list size, and returns the sorted list
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



}
