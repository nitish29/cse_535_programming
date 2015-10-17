package ir_project;


import java.io.*;
import java.util.*;


public class CSE535Assignment {

    // Constructor initialization
    CSE535Assignment() {

    }

    public static void main(String args[]) {

        try {

            //String file_name = "/Users/nitish/Downloads/term (2).idx";
            String file_name = args[0];
            String log_file_name = args[1];
            int topKInputValue = Integer.parseInt( args[2] );
            String queryFileName = args[3];
            String line = "null";


            FileReader fileReader = new FileReader(file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            IndexBuilder builder = new IndexBuilder();

            while ((line = bufferedReader.readLine()) != null) {

                IndexLineData initialData = ParseData(line);
                builder.buildIndex(initialData.term, initialData.postingList);
                //break;

            }

            bufferedReader.close();


            HashMap termMap = builder.fetchTAATMap();
            HashMap documentMap = builder.fetchDAATMap();
            ArrayList<TopKTerm> termList = builder.fetchTopKTermList();
            ArrayList<TopKTerm> sortedTopKTermList = builder.sortTopKTerms( termList );
            QueryBuilder.executeQuery( queryFileName, termMap, documentMap, sortedTopKTermList, topKInputValue );

            //TODO : method displaying concatenated strings
//            String topTerm = TopKTerm.getTopKTerms( sortedTopKTermList, topKInputValue );
//            //System.out.println(topTerm);
//            for (TopKTerm term : sortedTopKTermList) {
//                System.out.println(term.postingListSize + " " + term.term );
//            }





        } catch (Exception e) {

            e.printStackTrace();

        }

    }

	/*
	 * ParseData Algorithm
	 * 	- split the line based on "\"
	 *  - store all the split parts in different variables
	 *  - split the second variable to extract the posting size
	 *  - split the third variable to retrieve the document id and frequency
	 *  - make a new document object with the retrieved document id and term freq
	 *  - store the document object in a linked list
	 *  -
	 *
	 *  */

    public static IndexLineData ParseData(String line) {

        String key = "null";
        int size = 0;


        //System.out.println(line);
        String[] part = line.split("\\\\");
        key = part[0];
        size = Integer.parseInt(part[1].split("c")[1]);
        String[] each_posting_entry = (part[2].split("m")[1]).replaceAll("\\[|\\]", "").split(",");

        //Keyword keyword = new Keyword( key, size );


        int totalFrequency = 0;

        LinkedList<Document> postingList = new LinkedList<>();


        for (String single_posting_entry : each_posting_entry) {

            String[] doc_id_term_freq_array = single_posting_entry.split("/");
            int documentId = Integer.parseInt(doc_id_term_freq_array[0].trim());
            int termFrequency = Integer.parseInt(doc_id_term_freq_array[1]);
            totalFrequency = totalFrequency + termFrequency;

            Document new_document = new Document(documentId, termFrequency);
            postingList.add(new_document);

        }

        IndexLineData data = new IndexLineData( key, totalFrequency, postingList );

        return data;


    }


}
