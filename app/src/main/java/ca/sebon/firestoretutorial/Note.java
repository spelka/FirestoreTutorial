package ca.sebon.firestoretutorial;

public class Note
{
    private String title;
    private String description;

    public Note()
    {
        //Firestore requires a public, zero-argument constructor
    }

    public Note (String title, String description)
    {
        this.title = title;
        this.description = description;
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
