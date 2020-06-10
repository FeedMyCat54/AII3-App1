package com.feedmycat.aii3_app1;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment implements OnItemSelectedListener {
  private EditText description;
  private String selectedColor = "Red";
  private BottomSheetListener mListener;

  private boolean initialCheck = true;

  // Event that fires when the bottom sheet is dismissed
  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    mListener.saveDescription(description.getText().toString());
    mListener.saveColor(selectedColor);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

    description = v.findViewById(R.id.et_description);
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

  // Interface for handling bottom sheet actions
  public interface BottomSheetListener {
    void onSpinnerItemSelected(String color);
    void saveDescription(String description);
    void saveColor(String color);
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
