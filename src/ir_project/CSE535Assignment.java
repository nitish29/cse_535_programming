package ir_project;


import java.io.*;
import java.util.*;


public class CSE535Assignment {

    // Constructor initialization
    CSE535Assignment() {

    }

    public static void main(String args[]) {

        try {

            //String file_name = "/Users/nitish/Downloads/termData (2).idx";
            String file_name = args[0];
            String log_file_name = args[1];
            String line = "null";


            FileReader fileReader = new FileReader(file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            IndexBuilder builder = new IndexBuilder();


            while ((line = bufferedReader.readLine()) != null) {

                IndexLineData initialData = ParseData(line);
                builder.buildIndex(initialData.termData, initialData.postingList);
                //break;

            }

            bufferedReader.close();

            HashMap termMap = builder.fetchTAATMap();
            HashMap documentMap = builder.fetchDAATMap();



            System.out.println(termMap.size());
            System.out.println(documentMap.size());




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
	 *  - make a new document object with the retrieved document id and termData freq
	 *  - store the document object in a linked list
	 *  -
	 *
	 *  */

    public static IndexLineData ParseData(String line) {

        String key = "null";
        Integer size = 0;


        //System.out.println(line);
        String[] part = line.split("\\\\");
        key = part[0];
        size = Integer.parseInt(part[1].split("c")[1]);
        String[] each_posting_entry = (part[2].split("m")[1]).replaceAll("\\[|\\]", "").split(",");

        Keyword keywordData = new Keyword( key, size );

        Integer totalFrequency = 0;

        LinkedList<Document> postingList = new LinkedList<>();


        for (String single_posting_entry : each_posting_entry) {

            String[] doc_id_term_freq_array = single_posting_entry.split("/");
            Integer documentId = Integer.parseInt(doc_id_term_freq_array[0].trim());
            Integer termFrequency = Integer.parseInt(doc_id_term_freq_array[1]);
            totalFrequency = totalFrequency + termFrequency;

            Document new_document = new Document(documentId, termFrequency);
            postingList.add(new_document);

        }

        IndexLineData data = new IndexLineData( keywordData, totalFrequency, postingList );

        return data;


    }


}
