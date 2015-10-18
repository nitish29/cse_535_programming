package ir_project;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by nitish on 10/16/15.
 */

/*
    In Loop:
    * read single keyword line
    * get all the query terms in a string array for the fetched line
    * loop over the terms in the string array
    * store each posting in an array list
    * TAATAnd logic
        *

    -


 */

public class QueryBuilder {



    public static void executeQuery( String queryFileName, HashMap< String, LinkedList<Document>> termMap, HashMap< String, LinkedList<Document>> documentMap, ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue ) {


        try {

            String line;
            String[] termList;
            FileReader fileReader = new FileReader(queryFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //execute topK over here

            while ((line = bufferedReader.readLine()) != null) {

                termList = FetchLineData(line);

                QueryBuilder.termAtATimeQueryAnd( termList, termMap );

                //do more steps posting and-ing or-ing topK-ing for each term list


            }

            bufferedReader.close();


        } catch ( Exception e ) {

            e.printStackTrace();

        }

    }

    public static String[] FetchLineData(String line) {

        String[] terms = line.split(" ");
        return terms;


    }

    public static void termAtATimeQueryAnd( String[] termList, HashMap< String, LinkedList<Document>> termMap ) {

        try {

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            for ( String s: termList ) {

                LinkedList<Document> postingListTerm = getPostingList( termMap, s );
                //log the posting list terms in the log file either in this function or in getPostingList function
                list.add( postingListTerm );
            }


            LinkedList<Document> termFilterPostingList = list.get( 0 );
            ListIterator<Document> filterPostingListIterator = termFilterPostingList.listIterator();

//            // Debug code lines
            System.out.println( termFilterPostingList.size() );
            //System.out.println( list.get(1).size() );

//            for ( int i =0 ; i < termFilterPostingList.size(); i++ ) {
//                System.out.println(termFilterPostingList.get( i ).documentId + "," +termFilterPostingList.get( i ).term_frequency);
//            }
//
            //System.exit ( 0 );

            for ( int i = 1; i < list.size(); i++ ) {

                ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                while ( filterPostingListIterator.hasNext() ) {

                    while ( arrayListLinkedListIterator.hasNext() ) {

                        int v = filterPostingListIterator.next().documentId;

                        if ( v  == arrayListLinkedListIterator.next().documentId ) {

                            //System.out.println( v );

                            arrayListLinkedListIterator = list.get(i).listIterator();
                            continue;

                        } else   {

                            filterPostingListIterator.previous();

                        }

                        if ( !arrayListLinkedListIterator.hasNext() ) {

                            filterPostingListIterator.remove();
                            arrayListLinkedListIterator = list.get(i).listIterator();
                            break;

                        }
                        //System.out.println( "hello" );

                    }

                }

            }

            System.out.println( termFilterPostingList.size() );



        } catch ( Exception e ) {

            throw e;

        }


    }

    public static LinkedList<Document> getPostingList( HashMap< String, LinkedList<Document>> map, String term ) {

        LinkedList<Document> postingList = map.get( term );
//        for ( int i =0 ; i < postingList.size(); i++ ) {
//            System.out.println(postingList.get( i ).documentId + "," +postingList.get( i ).term_frequency);
//        }
//        System.exit( 0 );
        return postingList;

    }


    public static void getTopKTerms( ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue ) {

        String topK = "";

        for ( int i = 0; i < topKInputValue; i++ ) {

            //System.out.println(sortedList.get(i).term.term );

            topK = String.join(" , ", Arrays.asList(sortedTopKTermList.get(i).term ));

        }

    }
}
