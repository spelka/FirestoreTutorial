package ca.sebon.firestoretutorial;

import com.google.firebase.firestore.Exclude;

import java.util.List;

public class Note
{
    private String documentId;
    private String title;
    private String description;
    private int priority;
    List<String> tags;

    public Note()
    {
        //Firestore requires a public, zero-argument constructor
    }

    public Note (String title, String description, int priority, List<String> tags)
    {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.tags = tags;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
