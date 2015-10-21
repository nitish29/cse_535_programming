package ir_project;

/*
* Document is the base entity in a posting list
* Document class over here has two member variables - a Document ID and a Term Frequency
*
* */

public class Document {

    Integer documentId;
    Integer term_frequency;

    public Document( Integer documentId, Integer term_frequency ) {

        this.documentId = documentId;
        this.term_frequency = term_frequency;

    }

}
