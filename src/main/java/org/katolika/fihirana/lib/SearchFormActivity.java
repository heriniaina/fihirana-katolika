package org.katolika.fihirana.lib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.entities.Hira;
import org.katolika.fihirana.lib.entities.Sokajy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchFormActivity extends BaseActivity {

    // Spinner element
    private static final String TAG = "SearchFormActivity";
    Spinner spinner;
    FihiranaViewModel fihiranaViewModel;
    SharedPreferences prefs;
    EditText etTitle;
    EditText etContent;
    Button btnSearch;
    ImageView imgClearTitle;
    ImageView imgClearContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_form);

        imgClearTitle = findViewById(R.id.img_clear_title);
        imgClearContent = findViewById(R.id.img_clear_content);

        prefs = getSharedPreferences("org.katolika.fihirana.lib", 0);
        spinner = findViewById(R.id.spinner);

        ArrayList<SpinnerData> items = new ArrayList<>();
        items.add(new SpinnerData(getResources().getString(R.string.str_sokajy_rehetra), 0));
        ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);
        fihiranaViewModel.getSokajyList().observe(this, sokajyList -> {
            for (Sokajy sokajy : sokajyList) {
                items.add(new SpinnerData(sokajy.getS_title(), sokajy.getId()));
            }
            adapter.notifyDataSetChanged();
        });
        btnSearch = findViewById(R.id.btn_do_search);
        btnSearch.setOnClickListener(view -> {
            doSearch();
        });

        etTitle = findViewById(R.id.txt_from_title);
        etContent = findViewById(R.id.txt_from_content);
        etContent.setEnabled(true);

        etTitle.append(prefs.getString("tadiavo", ""));

        if (etTitle.getText().length() > 0) {
            imgClearTitle.setVisibility(View.VISIBLE);
        } else {
            imgClearTitle.setVisibility(View.GONE);
        }

        etContent.append(prefs.getString("tadiavo_content", ""));

        etTitle.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });
        if (etContent.getText().length() > 0) {
            imgClearContent.setVisibility(View.VISIBLE);
        } else {
            imgClearContent.setVisibility(View.GONE);
        }

        imgClearTitle.setOnClickListener(img -> {
            etTitle.setText("");
        });

        imgClearContent.setOnClickListener(img -> {
            etContent.setText("");
        });
        etContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch();
                return true;
            }
            return false;
        });

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    imgClearTitle.setVisibility(View.VISIBLE);
                } else {
                    imgClearTitle.setVisibility(View.GONE);
                }
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    imgClearContent.setVisibility(View.VISIBLE);
                } else {
                    imgClearContent.setVisibility(View.GONE);
                }
            }
        });

        //missing h_text
        etContent.setOnFocusChangeListener((view, b) -> {
            if (b) {
                fihiranaViewModel.getHiraEmptyTextCount().observe(this, integer -> {
                    Log.d(TAG, "onCreate: empty count " + integer);
                    if (integer > 0) {
                    	etContent.setEnabled(false);
                    	etContent.setHint(R.string.search_form_please_wait);
                        fihiranaViewModel.getHiraEmptyText().observe(SearchFormActivity.this, hiraList -> {
                            List<Hira> newHiraList = new ArrayList<>();
                            for (Hira hira : hiraList) {
                                Log.d(TAG, "onCreate: to update " + hira.getH_title());
                                try {
                                    InputStream is = getAssets().open("files/" + hira.getId() + ".html");
                                    int size = is.available();

                                    // Read the entire asset into a local byte buffer.
                                    byte[] buffer = new byte[size];
                                    is.read(buffer);
                                    is.close();
                                    hira.setH_text(new String(buffer));
                                    newHiraList.add(hira);
                                } catch (Exception e) {
                                    Log.e("SearchFormActivity108", "text file not accessible " + e.getMessage());
                                }
                            }
                            if (!newHiraList.isEmpty()) {
                                Log.d(TAG, "onCreate: " + newHiraList.size());
                                fihiranaViewModel.updateHiraList(newHiraList);
                            }
                        });
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setMessage(R.string.search_form_content_search)
//                                .setPositiveButton(R.string.str_eny, (dialogInterface, i) -> {
//                                    fihiranaViewModel.getHiraEmptyText().observe(SearchFormActivity.this, hiraList -> {
//                                        List<Hira> newHiraList = new ArrayList<>();
//                                        for (Hira hira : hiraList) {
//                                            Log.d(TAG, "onCreate: to update " + hira.getH_title());
//                                            try {
//                                                InputStream is = getAssets().open("files/" + hira.getId() + ".html");
//                                                int size = is.available();
//
//                                                // Read the entire asset into a local byte buffer.
//                                                byte[] buffer = new byte[size];
//                                                is.read(buffer);
//                                                is.close();
//                                                hira.setH_text(new String(buffer));
//                                                newHiraList.add(hira);
//                                            } catch (Exception e) {
//                                                Log.e("SearchFormActivity108", "text file not accessible " + e.getMessage());
//                                            }
//                                        }
//                                        if (!newHiraList.isEmpty()) {
//                                            Log.d(TAG, "onCreate: " + newHiraList.size());
//                                            fihiranaViewModel.updateHiraList(newHiraList);
//                                        }
//                                    });
//                                })
//                                .setNegativeButton(R.string.str_tsia, (dialogInterface, i) -> {
//                                	etContent.setEnabled(true);
//									etContent.setHint(R.string.search_form_from_content_hint);
//                                    dialogInterface.cancel();
//                                }).show();
                    } else {
                    	etContent.setEnabled(true);
                    	etContent.setHint(R.string.search_form_from_content_hint);
					}
                });

            } else {
                etContent.setEnabled(true);
            }
        });

    }

    public void doSearch() {
        Intent intent = new Intent(this, SearchResultActivity.class);

        String search_from_title = etTitle.getText().toString();

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("tadiavo", search_from_title);

        int search_from_category = ((SpinnerData) spinner.getAdapter().getItem(spinner.getSelectedItemPosition())).getValue();


        intent.putExtra("org.katolika.fihirana.lib.SEARCH_FROM_TITLE",
                search_from_title);

		String search_from_content = etContent.getText().toString();
        editor.putString("tadiavo_content", search_from_content );

        editor.apply();


        intent.putExtra("org.katolika.fihirana.lib.SEARCH_FROM_CONTENT",
				search_from_content);

        intent.putExtra(
                "org.katolika.fihirana.lib.SEARCH_FROM_CATEGORY",
                search_from_category);

        startActivity(intent);
        this.finish();
    }


    static class SpinnerData {
        String spinnerText;
        int value;

        public SpinnerData(String spinnerText, int value) {
            this.spinnerText = spinnerText;
            this.value = value;
        }

        public String getSpinnerText() {
            return spinnerText;
        }

        public int getValue() {
            return value;
        }

        public String toString() {
            return spinnerText;
        }
    }

//    private class SpinnerAdapter extends ArrayAdapter<SpinnerData> {
//
//        public SpinnerAdapter(Context context, int resource, List<SpinnerData> items) {
//            super(context, resource, items);
//        }
//
//        @Override
//        public View getDropDownView(int position, @Nullable @android.support.annotation.Nullable View convertView, @NonNull @android.support.annotation.NonNull ViewGroup parent) {
//            return super.getDropDownView(position, convertView, parent);
//        }
//    }

}
