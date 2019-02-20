package ca.sebon.firestoretutorial;

import com.google.firebase.firestore.Exclude;

public class Note
{
    private String documentId;
    private String title;
    private String description;
    private int priority;

    public Note()
    {
        //Firestore requires a public, zero-argument constructor
    }

    public Note (String title, String description, int priority)
    {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    //This is excluded because the ID is the name of the document, storing it twice would be redundant
    @Exclude
    public String getDocumentId() {
        return this.documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }


}
