package ir_project;

import javax.print.Doc;
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



    public static void executeQuery( String queryFileName, HashMap< String, LinkedList<Document>> termMap, HashMap< String, LinkedList<Document>> documentMap, ArrayList<TopKTerm> sortedTopKTermList, int topKInputValue ) {


        try {

            String line;
            String[] termList;
            FileReader fileReader = new FileReader(queryFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //execute topK over here
            //TODO : method displaying concatenated strings
//            String topTerm = TopKTerm.getTopKTerms( sortedTopKTermList, topKInputValue );
//            for (TopKTerm term : sortedTopKTermList) {
//                System.out.println(term.postingListSize + " " + term.term );
//            }


            while ((line = bufferedReader.readLine()) != null) {

                termList = FetchLineData(line);

                //QueryBuilder.termAtATimeQueryAnd( termList, termMap );
                QueryBuilder.termAtATimeQueryOR(termList, termMap);

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
                //print both the types of postings here - sorted by doc ID and Term freq - can make a call to another function which does that
                list.add( postingListTerm );
            }


            LinkedList<Document> termFilterPostingList = list.get( 0 );
            ListIterator<Document> filterPostingListIterator = termFilterPostingList.listIterator();

//            Debug code lines
            System.out.println( termFilterPostingList.size() );
//            System.out.println( list.get(1).size() );

//            for ( int i =0 ; i < termFilterPostingList.size(); i++ ) {
//                System.out.println(termFilterPostingList.get( i ).documentId + "," +termFilterPostingList.get( i ).term_frequency);
//            }
//
//            System.exit ( 0 );

            for ( int i = 1; i < list.size(); i++ ) {

                ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                while ( filterPostingListIterator.hasNext() ) {

                    while ( arrayListLinkedListIterator.hasNext() ) {

                        int v = filterPostingListIterator.next().documentId;

                        if ( v  == arrayListLinkedListIterator.next().documentId ) {

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

                    }

                }

            }

            System.out.println( termFilterPostingList.size() );
            for ( int i =0 ; i < termFilterPostingList.size(); i++ ) {
                System.out.println(termFilterPostingList.get( i ).documentId + "," +termFilterPostingList.get( i ).term_frequency);
            }




        } catch ( Exception e ) {

            throw e;

        }


    }

    public static void termAtATimeQueryOR( String[] termList, HashMap< String, LinkedList<Document>> termMap ) {


        try {

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


        /*public static void termAtATimeQueryOr( String[] termList, HashMap< String, LinkedList<Document>> termMap ) {


        try {

            ArrayList<LinkedList<Document>> list = new ArrayList<>();

            for ( String s: termList ) {

                LinkedList<Document> postingListTerm = getPostingList( termMap, s );
                list.add( postingListTerm );
            }

            int flag = 0;
            LinkedList<Document> termFilterPostingList = list.get( 0 );
            ListIterator<Document> filterPostingListIterator = termFilterPostingList.listIterator();

            // Debug code lines
            System.out.println( termFilterPostingList.size() );

            for ( int i = 1; i < list.size(); i++ ) {

                ListIterator<Document> arrayListLinkedListIterator = list.get(i).listIterator();

                //if only one element in the filter list,


                while ( filterPostingListIterator.hasNext() ) {

                    while ( arrayListLinkedListIterator.hasNext() ) {

                        int v = filterPostingListIterator.next().documentId;
                        //System.out.println(v);
                        Document c = arrayListLinkedListIterator.next();

                        if ( v  == c.documentId ) {

                            continue;

                        } else   {

                            //if not already exists then add
                            for ( int j = 0 ; j < termFilterPostingList.size(); j++ ) {


                                if (c.documentId.equals(termFilterPostingList.get(j).documentId)) {

                                    flag = 1;
                                    break;

                                }
                                flag = 0;

                            }


                            if ( flag == 0 ) {

                                filterPostingListIterator.add( c );

                            }



                        }

                    }

                    break;

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
}
