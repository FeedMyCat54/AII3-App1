package com.feedmycat.aii3_app1;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

  private static final String TAG = "DatabaseManager";
  private static final String KEY_TITLE = "title", KEY_LATITUDE = "latitude", KEY_LONGITUDE = "longitude", KEY_BAROMETER = "barometer";

  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  // Adds a marker document
  public void addMarker(final String title, double latitude, double longitude, double barometer) {
    Map<String, Object> marker = new HashMap<>();
    marker.put(KEY_TITLE, title);
    marker.put(KEY_LATITUDE, latitude);
    marker.put(KEY_LONGITUDE, longitude);
    marker.put(KEY_BAROMETER, barometer);

    db.collection("Markers").document(title)
        .set(marker)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d(TAG, "Document added: " + title);
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d(TAG, "Error adding document " + e.toString());
          }
        });
  }

  // Deletes a specific marker document
  public void deleteMarker(String title) {
    db.document("Markers/" + title).delete();
  }

  // Updates the color of a marker
  public void updateMarkerColor(String title, String color) {
    db.collection("Markers").document(title).update("color", color);
  }

  // Updates the description of a marker
  public void updateMarkerDescription(String title, String description) {
    db.collection("Markers").document(title).update("description", description);
  }
}
