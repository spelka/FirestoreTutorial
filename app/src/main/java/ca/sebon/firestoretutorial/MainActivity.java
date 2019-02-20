package ca.sebon.firestoretutorial;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.collection("Notebook").document("My First Note Document");
    private CollectionReference notebookRef = db.collection("Notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void addNote(View v)
    {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        //handle the case where there is no value in the priority field
        if(editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }
        int priority = Integer.parseInt(editTextPriority.getText().toString());

        //add strings into a container to pass into the DB. Can use a map or an object.
        Note note = new Note(title, description, priority);

        // add the note to the DB
        notebookRef.add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    //return results of a simple query
    public void loadNotes(View v)
    {
        notebookRef.whereGreaterThanOrEqualTo("priority", 1)      //range operator
                .whereLessThanOrEqualTo("priority", 3)            //range operator
                .orderBy("priority", Query.Direction.DESCENDING)        //sorting operator
                .orderBy("title", Query.Direction.ASCENDING)            //sorting operator
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //iterate through the query snapshot to get our document snapshots
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());
                            data += "ID: " + note.getDocumentId() + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription() + "\nPriority: " + note.getPriority() + "\n\n";
                        }

                        textViewData.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void notPriorityTwo(View v)
    {
        Task task1 = notebookRef.whereLessThan("priority", 2)
                .orderBy("priority", Query.Direction.ASCENDING)
                .get();

        Task task2 = notebookRef.whereGreaterThan("priority", 2)
                .orderBy("priority", Query.Direction.ASCENDING)
                .get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(task1, task2);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                //iterate through the List of query snapshot to get our document query document snapshots
                String data = "";
                for (QuerySnapshot queryDocumentSnapshots : querySnapshots)
                {
                    //iterate through the List of query document snapshots to get our document snapshots
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());
                        data += "ID: " + note.getDocumentId() + "\nTitle: " + note.getTitle() + "\nDescription: " + note.getDescription() + "\nPriority: " + note.getPriority() + "\n\n";
                    }

                    textViewData.setText(data);
                }
            }
        });

    }


    public void saveNote(View v)
    {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        //handle the case where there is no value in the priority field
        if(editTextPriority.length() == 0)
        {
            editTextPriority.setText("0");
        }
        int priority = Integer.parseInt(editTextPriority.getText().toString());

        //add strings into a container to pass into the DB. Can use a map or an object.
        Note note = new Note(title, description, priority);

        // add the note to the DB
        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }



    public void mergeDescription(View v)
    {
        String description = editTextDescription.getText().toString();

        Map<String,Object> note = new HashMap<>();
        note.put(KEY_DESCRIPTION, description);

        //SetOptions.merge() will use the merge strategy and will not destroy the old document and replace it with a new one when updating.
        //If the document does not exist, it will create one, only with the provided data.
        //Fields not provided will not exist in the document (not even as a null)
        noteRef.set(note, SetOptions.merge());

    }

    public void updateDescription(View v)
    {
        String description = editTextDescription.getText().toString();
        noteRef.update(KEY_DESCRIPTION, description);
    }

    public void deleteDescription(View v)
    {
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Note Description Deleted", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error Deleting Note Description!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    public void deleteNote(View v)
    {
        noteRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error Deleting Note!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNote(View v)
    {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        //confirm the document snapshot exists before doing work
                        if (documentSnapshot.exists())
                        {
                            Note note = documentSnapshot.toObject(Note.class);
                            textViewData.setText("Title: " + note.getTitle() + "\n" + "Description: " + note.getDescription());
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Error: Document not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }


}
