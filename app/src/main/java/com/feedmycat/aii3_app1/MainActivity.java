package com.feedmycat.aii3_app1;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  public static final String KEY_TITLE = "title";
  public static final String KEY_DESCRIPTION = "description";

  private Button btnTest;

  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnTest = findViewById(R.id.test_btn);

    btnTest.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        saveDocument();
      }
    });
  }

  private void saveDocument() {
    Map<String, Object> doc = new HashMap<>();
    doc.put(KEY_TITLE, "title1");
    doc.put(KEY_DESCRIPTION, "description1");

    db.collection("TestCollection").document("First document").set(doc)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Toast.makeText(MainActivity.this, "Document Saved", Toast.LENGTH_SHORT).show();
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
}
