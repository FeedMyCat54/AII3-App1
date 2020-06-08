package com.feedmycat.aii3_app1;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment implements OnItemSelectedListener,
    SensorEventListener {
  private EditText description;
  private TextView pressureTextView;
  private String selectedColor;
  private BottomSheetListener mListener;
  private SensorManager sensorManager;
  private Sensor pressure;

  private boolean initialCheck = true;

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    Toast.makeText(getActivity(), description.getText().toString(), Toast.LENGTH_SHORT).show();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

    description = v.findViewById(R.id.et_description);
    pressureTextView = v.findViewById(R.id.tv_pressure);
    // Gets the spinner
    Spinner spinner = v.findViewById(R.id.spinner_color);
    // Creates array adapter with the colors array
    ArrayAdapter<CharSequence> adapter = ArrayAdapter
        .createFromResource(getActivity(), R.array.marker_colors_array,
            android.R.layout.simple_spinner_dropdown_item);
    // Sets the layout that is used when the choices list appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setOnItemSelectedListener(this);
    spinner.setAdapter(adapter);

    // Get an instance of the sensor service, and use that to get an instance of
    // a particular sensor.
    sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
    pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

    return v;
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    // Prevents the event from happening on initialization
    if (initialCheck) {
      initialCheck = false;
    } else {
      // Return the selected color to the main activity
      selectedColor = parent.getItemAtPosition(position).toString();
      mListener.onSpinnerItemSelected(selectedColor);
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    // Display the pressure in the TextView
    float millibarsOfPressure = event.values[0];
    pressureTextView.setText(String.format("%.3f nbar", millibarsOfPressure));
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  @Override
  public void onResume() {
    // Register a listener for the sensor.
    super.onResume();
    sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  public void onPause() {
    // Unregister the sensor when the activity pauses.
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  // Interface for handling bottom sheet actions
  public interface BottomSheetListener {
    void onSpinnerItemSelected(String color);
  }

  // Attach the fragment to the main Activity
  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    try {
      mListener = (BottomSheetListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement BottomSheetListener.");
    }
  }
}
