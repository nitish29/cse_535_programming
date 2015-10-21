import ir_project.*;

import java.io.*;
import java.util.*;


public class CSE535Assignment {

    // Constructor initialization
    CSE535Assignment() {

    }

    public static void main(String args[]) {

        try {

            String file_name = args[0]; //stores the index file path
            String log_file_name = args[1]; //stores the log file path
            int topKInputValue = Integer.parseInt( args[2] ); //stores the top K input value
            String queryFileName = args[3]; // stores the query file path
            String line = "null";
            Log logger = new Log(log_file_name); //instantiate a logger object - will be used to log data


            logger.log( "##########################################################################################" );

            FileReader fileReader = new FileReader(file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            IndexBuilder builder = new IndexBuilder();


            while ((line = bufferedReader.readLine()) != null) {

                IndexLineData initialData = ParseData(line);
                builder.buildIndex(initialData.term, initialData.postingList); // pass the data parsed and sanitized IndexBuilder Class, which builds indexes

            }

            bufferedReader.close();


            HashMap< String, LinkedList<Document>> termMap = builder.fetchTAATMap();
            HashMap< String, LinkedList<Document>> documentMap = builder.fetchDAATMap();
            ArrayList<TopKTerm> termList = builder.fetchTopKTermList();
            ArrayList<TopKTerm> sortedTopKTermList = IndexBuilder.sortTopKTerms(termList);

            //The function executeQuery of the QueryBuilder class executes all the operations requested - TAATAnd, TAATOr, get postings for each term, get Top K terms
            //pass the query file name, term hash map, document hash map, sorted top term list, top K term input value and the logger as input arguments to execute query function
            QueryBuilder.executeQuery(queryFileName, termMap, documentMap, sortedTopKTermList, topKInputValue, logger);

            logger.log("########################################################################################################");



        } catch (Exception e) {

            e.printStackTrace();

        }

    }

	/*
	 * ParseData Algorithm - For each line
	 * 	- split the line based on "\"
	 *  - store all the split parts in different variables
	 *  - split the second variable to extract the posting size
	 *  - split the third variable to retrieve the document id and frequency
	 *  - make a new document object with the retrieved document id and term freq
	 *  - store the document object in a linked list
	 *  - pass the IndexLineData object to IndexBuilder class
	 *  - which creates HashMaps for DAAT and TAAT
	 *
	 *  */

    public static IndexLineData ParseData(String line) {

        String key = "null";
        int size = 0;
        String[] part = line.split("\\\\"); //split the line based on "\"
        key = part[0]; // get the keyword after the split
        size = Integer.parseInt(part[1].split("c")[1]); //get the size of the posting list
        String[] each_posting_entry = (part[2].split("m")[1]).replaceAll("\\[|\\]", "").split(","); //removes the opening and closing brackets for the posting list and creates a string array by delimeter - ","

        int totalFrequency = 0;

        LinkedList<Document> postingList = new LinkedList<>();


        for (String single_posting_entry : each_posting_entry) {
            //fetch the posting doc ID and term frequency
            String[] doc_id_term_freq_array = single_posting_entry.split("/");
            int documentId = Integer.parseInt(doc_id_term_freq_array[0].trim());
            int termFrequency = Integer.parseInt(doc_id_term_freq_array[1]);
            totalFrequency = totalFrequency + termFrequency;
            //create a new document object with the fetched document id and term frequency
            Document new_document = new Document(documentId, termFrequency);
            //add the document object is the posting linked list
            postingList.add(new_document);

        }

        // pass the keyword, linked list to IndexLineData function to create references
        IndexLineData data = new IndexLineData( key, totalFrequency, postingList );

        return data;


    }


}
