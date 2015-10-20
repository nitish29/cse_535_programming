package ir_project;


import java.io.*;
import java.util.*;


public class CSE535Assignment {

    // Constructor initialization
    CSE535Assignment() {

    }

    public static void main(String args[]) {

        try {

            String file_name = args[0];
            String log_file_name = args[1];
            int topKInputValue = Integer.parseInt( args[2] );
            String queryFileName = args[3];
            String line = "null";
            Log logger = new Log(log_file_name);

            //log1.log("This is written in the first file");
            //System.exit(0);


            logger.log( "##########################################################################################" );

            FileReader fileReader = new FileReader(file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            IndexBuilder builder = new IndexBuilder();


            while ((line = bufferedReader.readLine()) != null) {

                IndexLineData initialData = ParseData(line);
                builder.buildIndex(initialData.term, initialData.postingList);
                //break;

            }

            bufferedReader.close();


            HashMap< String, LinkedList<Document>> termMap = builder.fetchTAATMap();
            HashMap< String, LinkedList<Document>> documentMap = builder.fetchDAATMap();
            ArrayList<TopKTerm> termList = builder.fetchTopKTermList();
            ArrayList<TopKTerm> sortedTopKTermList = builder.sortTopKTerms( termList );

            QueryBuilder.executeQuery( queryFileName, termMap, documentMap, sortedTopKTermList, topKInputValue, logger );

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
	 *  - pass this data to IndexLineData class, by instantiating it
	 *  - pass the IndexLineData object to IndexBuilder class
	 *  - which creates HashMaps for DAAT and TAAT
	 *
	 *  */

    public static IndexLineData ParseData(String line) {

        String key = "null";
        int size = 0;

        String[] part = line.split("\\\\");
        key = part[0];
        size = Integer.parseInt(part[1].split("c")[1]);
        String[] each_posting_entry = (part[2].split("m")[1]).replaceAll("\\[|\\]", "").split(",");

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
