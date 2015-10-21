package ir_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/*
    This class logs the top K terms based on user input
    It also logs the postings list for each term
    and finally it also logs the boolean model operations (TAAT AND, TAAT OR ) requested

 */

public class QueryBuilder {



    // this function is used to delegate the
    public static void executeQuery( String queryFileName, HashMap< String, LinkedList<Document>> termMap, HashMap< String, LinkedList<Document>> documentMap, ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue, Log logger ) {

        try {

            String line;
            String[] termList;
            FileReader fileReader = new FileReader(queryFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //execute topK over here
            QueryBuilder.printTopKTerms(sortedTopKTermList, topKInputValue, logger); // call to this function executes the top K term query


            while ((line = bufferedReader.readLine()) != null) {

                termList = FetchLineData(line);

                QueryBuilder.termAtATimeQueryAnd( termList, termMap,documentMap, logger ); // call to this function executes TAAT And query
                QueryBuilder.termAtATimeQueryOr(termList, termMap, logger); // call to this function executes TAAT Or query


            }

            bufferedReader.close();


        } catch ( Exception e ) {

            e.printStackTrace();

        }

    }

    //split the line fetched from the query file, to get the terms to be queried against
    public static String[] FetchLineData(String line) {

        String[] terms = line.split(" ");
        return terms;


    }


    //this function executes the TAAT And query, and prints the result in a log file
    // it generates an array list of all the posting list
    // it make a result set array list that would contain the final answer for all the terms entered
    // result set array list contains an initial list - ( the posting list at index 0 of array list)
    // every element in the result set array list is compared with all the other array lists elements starting from index 1
    // if the element is not found in the other array list, it is removed
    // final answer is the truncated result set array list
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

                    // make an initial result set final list that would contain the final answer
                    resultSetList.add(termFilterPostingList.get(n));

                }

                ListIterator<Document> resultSetListIterator = resultSetList.listIterator();


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


    //this function executes the TAAT - Or query and prints the result in a log file
    public static void termAtATimeQueryOr( String[] termList, HashMap< String, LinkedList<Document>> termMap, Log logger ) {


        try {

            int noOfDocs = 0;
            int noOfComparisons = 0;
            double noOfSeconds;

            double startTime = System.currentTimeMillis();

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            //fetch posting list for each term and store all these posting list in an array list
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


                //make an initial result set list that will contain the final answer
                for ( int n = 0; n < termFilterPostingList.size(); n++ ) {

                    resultSetList.add(termFilterPostingList.get(n));

                }

                for ( int i = 1; i < list.size(); i++ ) {

                    //use an iterator to traverse over other array list element starting from index 1
                    ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                    while ( arrayListLinkedListIterator.hasNext() ) {

                        Document c = arrayListLinkedListIterator.next();

                        //this for loop checks whether the document from the array list already exists in the result set or not

                        for ( int j = 0 ; j < resultSetList.size(); j++ ) {

                            if (c.documentId.equals(resultSetList.get(j).documentId)) {

                                //if the document already exists, do not add it
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


    // This function fetches the posting list for a particular term
    public static LinkedList<Document> getPostingList( HashMap< String, LinkedList<Document>> map, String term ) {

        LinkedList<Document> postingList = map.get( term );

        if ( postingList == null ) {

            return null;

        } else {


            return postingList;

        }



    }


    //This function prints the TOP K terms requested in the log file
    public static void printTopKTerms( ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue, Log logger ) {

        String topKList = "";

        for ( int i = 0; i < topKInputValue; i++ ) {

            topKList += sortedTopKTermList.get(i).term + ",";


        }

        String str = removeLastChar( topKList );
        logger.log("FUNCTION: getTopK " + topKInputValue);
        logger.log("Result: " + str);

    }

    //This function converts all the terms to a string - all separated by commas (used for displaying data in log file)
    public static String getStringTerm ( String[] termList ) {

        String stringTerm="";

        for ( String s: termList ) {

            stringTerm += s + ",";
        }

        String str = removeLastChar( stringTerm );

        return str;


    }


    // This function prints the posting in the log file pertaining to a term
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

    // this function removes the last trailing comma from each string generated, which is going to be printed in the log file
    private static String removeLastChar(String str) {

        if (str.length() > 0 && str.charAt(str.length()-1)== ',') {
            str = str.substring(0, str.length()-1);
        }
        return str;

    }


    // this function reorders the final term list which is displayed for TAAT - OR and TAAT AND and concatenates all the terms to a string type
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

    /******************** Incomplete functions below - Please Ingore *******************************************************/

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



}
