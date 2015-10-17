package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * Created by nitish on 10/16/15.
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
                for (String s: termList) {

                    LinkedList<Document> postingList = getPostingList( termMap, s );
                    //System.out.println(postingList.size());
                    System.out.println(termMap.get( s ).getFirst().documentId);
                    System.exit(0);
                }




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



    }

    public static LinkedList<Document> getPostingList ( HashMap< String, LinkedList<Document>> map, String term ) {

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
