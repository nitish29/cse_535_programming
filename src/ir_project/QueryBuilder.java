package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

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



    public static void executeQuery( String queryFileName, HashMap< String, LinkedList<Document>> termMap, HashMap< String, LinkedList<Document>> documentMap, ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue, Log logger ) {

        try {

            String line;
            String[] termList;
            FileReader fileReader = new FileReader(queryFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //execute topK over here
            //TODO : method displaying concatenated strings
            QueryBuilder.printTopKTerms(sortedTopKTermList, topKInputValue, logger);


            while ((line = bufferedReader.readLine()) != null) {

                termList = FetchLineData(line);

                QueryBuilder.termAtATimeQueryAnd( termList, termMap,documentMap, logger );
                QueryBuilder.termAtATimeQueryOr(termList, termMap, logger);
                //QueryBuilder.documentAtATimeQueryAnd(termList, documentMap);



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

    /*
    * Algorithm for TAAT And
    *
    * Iterate over the list of terms passed - [ termList contains list of terms passed from executeQuery function call]
    * fetch the posting list for each term and print it
    *  - if the term doesn't exist, or is the posting list is empty - null value, call the printPostingList function ( which prints term not found )
    *  - else, log the fetched posting list in the log file by calling the same function printPostingList
    *
    *
    *
    *
    *
    * */


    public static void termAtATimeQueryAnd( String[] termList, HashMap< String, LinkedList<Document>> termMap, HashMap< String, LinkedList<Document>> documentMap, Log logger ) {

        try {

            double startTime = System.currentTimeMillis();

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            int noOfDocs = 0;
            int noOfComparisons = 0;
            double noOfSeconds;

            LinkedList<Document> postingListTerm = new LinkedList<>();

            for ( String s: termList ) {

                postingListTerm = getPostingList( termMap, s );
                LinkedList<Document> postingListDoc = getPostingList( documentMap, s );

                if ( postingListTerm == null ) {

                    QueryBuilder.printPostingList( s, postingListTerm, postingListDoc, logger );

                } else {

                    QueryBuilder.printPostingList( s, postingListTerm, postingListDoc, logger );
                    list.add( postingListTerm );


                }

            }



            if ( postingListTerm != null ) {

                LinkedList<Document> termFilterPostingList = list.get(0);

                LinkedList<Document> resultSetList = new LinkedList<>();

                for ( int n = 0; n < termFilterPostingList.size(); n++ ) {

                    resultSetList.add(termFilterPostingList.get(n));

                }

                ListIterator<Document> resultSetListIterator = resultSetList.listIterator();

                //System.out.println( resultSetList.size() );

                for ( int i = 1; i < list.size(); i++ ) {

                    ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                    while ( resultSetListIterator.hasNext() ) {

                        while ( arrayListLinkedListIterator.hasNext() ) {

                            noOfComparisons++;

                            int v = resultSetListIterator.next().documentId;

                            if ( v  == arrayListLinkedListIterator.next().documentId ) {

                                arrayListLinkedListIterator = list.get(i).listIterator();
                                continue;

                            } else   {

                                resultSetListIterator.previous();

                            }

                            if ( !arrayListLinkedListIterator.hasNext() ) {

                                resultSetListIterator.remove();
                                arrayListLinkedListIterator = list.get(i).listIterator();
                                break;

                            }

                        }

                    }

                }

                double endTime = System.currentTimeMillis();

                noOfSeconds = ( endTime - startTime )/1000;

                noOfDocs = resultSetList.size();

                String sortedList = QueryBuilder.sortedFinalTermList( resultSetList );

                String displayTermList = QueryBuilder.getStringTerm( termList );
                logger.log("FUNCTION: termAtATimeQueryAnd " + displayTermList);
                logger.log( noOfDocs + " documents are found" );
                logger.log( noOfComparisons + " comparisons are made");
                logger.log( noOfSeconds + " seconds are used");
                logger.log( "Result: " + sortedList);


            } else {

                String displayTermList = QueryBuilder.getStringTerm(termList);
                logger.log("FUNCTION: termAtATimeQueryAnd " + displayTermList);
                logger.log("terms not found");

            }


        } catch ( Exception e ) {

            throw e;

        }


    }

    /*
    * Algorithm for TAAT OR
    *
    *
    *
    *
    * */

    public static void termAtATimeQueryOr( String[] termList, HashMap< String, LinkedList<Document>> termMap, Log logger ) {


        try {

            int noOfDocs = 0;
            int noOfComparisons = 0;
            double noOfSeconds;

            double startTime = System.currentTimeMillis();

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            for ( String s: termList ) {

                LinkedList<Document> postingListTerm = getPostingList( termMap, s );

                if ( postingListTerm != null ) {

                    list.add( postingListTerm );

                }
            }

            int flag = 0;


            if ( list.size() != 0 ) {

                LinkedList<Document> termFilterPostingList = list.get( 0 );
                LinkedList<Document> resultSetList = new LinkedList<>();

                for ( int n = 0; n < termFilterPostingList.size(); n++ ) {

                    resultSetList.add(termFilterPostingList.get(n));

                }

                //System.out.println( resultSetList.size() );


                for ( int i = 1; i < list.size(); i++ ) {

                    ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                    while ( arrayListLinkedListIterator.hasNext() ) {

                        Document c = arrayListLinkedListIterator.next();

                        //if not already exists then add
                        for ( int j = 0 ; j < resultSetList.size(); j++ ) {

                            if (c.documentId.equals(resultSetList.get(j).documentId)) {

                                flag = 1;
                                noOfComparisons++;
                                break;

                            }
                            flag = 0;
                        }


                        if ( flag == 0 ) {

                            resultSetList.add( c );

                        }


                    }

                }

                double endTime = System.currentTimeMillis();

                noOfSeconds = ( endTime - startTime )/1000;



                noOfDocs = resultSetList.size();
                String sortedList = QueryBuilder.sortedFinalTermList( resultSetList );
                String displayTermList = QueryBuilder.getStringTerm( termList );
                logger.log("FUNCTION: termAtATimeQueryOr " + displayTermList);
                logger.log( noOfDocs + " documents are found" );
                logger.log( noOfComparisons + " comparisons are made");
                logger.log( noOfSeconds + " seconds are used");
                logger.log( "Result: " + sortedList);



            } else {

                String displayTermList = QueryBuilder.getStringTerm(termList);
                logger.log("FUNCTION: termAtATimeQueryOr " + displayTermList);
                logger.log("terms not found");

            }



        } catch ( Exception e ) {

            throw e;

        }


    }

    /*public static void termAtATimeQueryOr( String[] termList, HashMap< String, LinkedList<Document>> termMap ) {


        try {

            int noOfDocs = 0;
            int noOfComparisons = 0;

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            for ( String s: termList ) {

                LinkedList<Document> postingListTerm = getPostingList( termMap, s );

                list.add( postingListTerm );
            }

            int flag = 0;
            LinkedList<Document> termFilterPostingList = list.get( 0 );

            System.out.println( termFilterPostingList.size() );

            for ( int i = 1; i < list.size(); i++ ) {

                ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                while ( arrayListLinkedListIterator.hasNext() ) {

                    Document c = arrayListLinkedListIterator.next();

                    //if not already exists then add
                    for ( int j = 0 ; j < termFilterPostingList.size(); j++ ) {

                        if (c.documentId.equals(termFilterPostingList.get(j).documentId)) {

                            flag = 1;
                            break;

                        }
                        flag = 0;
                    }


                    if ( flag == 0 ) {

                        termFilterPostingList.add( c );

                    }


                }

            }

            System.out.println( termFilterPostingList.size() );

//            for ( int i =0 ; i < termFilterPostingList.size(); i++ ) {
//                System.out.println(termFilterPostingList.get( i ).documentId + "," +termFilterPostingList.get( i ).term_frequency);
//            }


        } catch ( Exception e ) {

            throw e;

        }


    }*/

    /*public static void documentAtATimeQueryAnd( String[] termList, HashMap< String, LinkedList<Document>> documentMap ) {

        try {

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            for ( String s: termList ) {

                LinkedList<Document> postingListDoc = documentMap.get( s );

                for ( int i = 0 ; i < postingListDoc.size(); i++ ) {

                    System.out.println(postingListDoc.get( i ).documentId + "," + postingListDoc.get( i ).term_frequency);

                }
                System.out.println(s);
                System.exit(0);

                //LinkedList<Document> postingListTerm = getPostingList( documentMap, s );
                //list.add( postingListTerm );
            }


            ArrayList compareList = new ArrayList( list.size() );
            ArrayList finalDocList = new ArrayList();

            //set up initial compare array

            for ( int i = 0; i < list.size(); i++ ) {

                System.out.println( list.get(i).element().documentId );
                //compareList.add( 0, list.get(0).element());

            }

            for ( int j = 0; j < compareList.size(); j++ ) {

                System.out.println(compareList.get(j) );

            }



        } catch ( Exception e ) {

            e.printStackTrace();

        }


    }*/



    public static LinkedList<Document> getPostingList( HashMap< String, LinkedList<Document>> map, String term ) {

        LinkedList<Document> postingList = map.get( term );

        if ( postingList == null ) {

            return null;

        } else {


            return postingList;

        }



    }


    public static void printTopKTerms( ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue, Log logger ) {

        String topKList = "";

        for ( int i = 0; i < topKInputValue; i++ ) {

            topKList += sortedTopKTermList.get(i).term + ",";


        }

        String str = removeLastChar( topKList );
        logger.log("FUNCTION: getTopK " + topKInputValue);
        logger.log("Result: " + str);

    }

    public static String getStringTerm ( String[] termList ) {

        String stringTerm="";

        for ( String s: termList ) {

            stringTerm += s + ",";
        }

        String str = removeLastChar( stringTerm );

        return str;


    }


    public static void printPostingList ( String s, LinkedList<Document> postingListTerm, LinkedList<Document> postingListDoc, Log logger) {



        try {


            if ( postingListDoc == null ) {

                logger.log("FUNCTION: getPostings " + s);
                logger.log("term not found");


            } else if ( (postingListDoc.size() == 0) && (postingListTerm.size() == 0) ) {

                logger.log("FUNCTION: getPostings " + s);
                logger.log("term not found");


            } else {

                String orderedByDocID = "";
                String orderedByTermFreq = "";


                for ( int i =0 ; i < postingListDoc.size(); i++ ) {

                    orderedByDocID += postingListDoc.get(i).documentId + ",";

                }

                for ( int i =0 ; i < postingListTerm.size(); i++ ) {

                    orderedByTermFreq += postingListTerm.get(i).documentId + ",";

                }


                String strDoc = removeLastChar( orderedByDocID );
                String strTerm = removeLastChar( orderedByTermFreq );
                logger.log("FUNCTION: getPostings " + s);
                logger.log("Ordered by doc IDs: " + strDoc);
                logger.log("Ordered by TF: " + strTerm);


            }




        } catch ( Exception e ) {

            e.printStackTrace();

        }


    }

    private static String removeLastChar(String str) {

        if (str.length() > 0 && str.charAt(str.length()-1)== ',') {
            str = str.substring(0, str.length()-1);
        }
        return str;

    }

    public static String sortedFinalTermList ( LinkedList<Document> postingListTerm ) {

        String sortedDocIdList = "";

        ArrayList<Integer> resultSetID = new ArrayList<>();

        for ( int n = 0; n < postingListTerm.size(); n++ ) {

            resultSetID.add(postingListTerm.get(n).documentId);

        }

        Collections.sort( resultSetID );

        for ( Integer docID : resultSetID ) {

            sortedDocIdList += Integer.toString( docID) + ",";

        }

        String str = removeLastChar( sortedDocIdList );

        return str;

    }




}
