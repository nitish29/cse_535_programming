package ir_project;


import java.io.*;
import java.util.*;


public class CSE535Assignment {

    // Constructor initialization
    CSE535Assignment() {

        //HashMap<String, LinkedList<?> > docMap = new HashMap<String, LinkedList<?>>();
        HashMap docMap = new HashMap();
        HashMap termMap = new HashMap();
        LinkedList termList = new LinkedList();
        LinkedList docList = new LinkedList();

    }

    public static void main(String args[]) {

        try {

            //String file_name = "/Users/nitish/Downloads/term (2).idx";
            String file_name = args[0];
            String log_file_name = args[1];
            String line = "null";


            FileReader fileReader = new FileReader(file_name);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                IndexLineData initial_data = sanitization(line);

                System.out.println(initial_data.size);

                break;

            }

            bufferedReader.close();


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

	/*
	 * Sanitization Algorithm
	 * 	- split the line based on "\"
	 *  - store all the split parts in different variables
	 *  - split the second variable to extract the posting size
	 *  - split the third variable to retrieve the document id and frequency
	 *  - make a new document object with the retrieved document id and term freq
	 *  - store the document object in a linked list
	 *  -
	 *
	 *  */

    public static IndexLineData sanitization(String line) {

        String key = "null";
        Integer size = 0;


        System.out.println(line);
        String[] part = line.split("\\\\");
        key = part[0];
        size = Integer.parseInt(part[1].split("c")[1]);
        String[] each_posting_entry = (part[2].split("m")[1]).replaceAll("\\[|\\]", "").split(",");

        Keyword term = new Keyword(key);

        Integer total_frequency = 0;

        List<Document> new_list = new ArrayList<>();


        for (String single_posting_entry : each_posting_entry) {

            String[] doc_id_term_freq_array = single_posting_entry.split("/");
            Integer document_id = Integer.parseInt(doc_id_term_freq_array[0].trim());
            Integer term_frequency = Integer.parseInt(doc_id_term_freq_array[1]);
            total_frequency = total_frequency + term_frequency;

            Document new_document = new Document(document_id, term_frequency);
            new_list.add(new_document);

        }

        IndexLineData data = new IndexLineData(term, total_frequency, new_list, size);

        return data;


    }


}
