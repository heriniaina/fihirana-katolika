package org.katolika.fihirana.lib;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.entities.Fanovana;
import org.katolika.fihirana.lib.entities.Fihirana;
import org.katolika.fihirana.lib.entities.Hira;

import java.util.ArrayList;
import java.util.List;

public class DialogUpdate extends BaseActivity {
    FihiranaViewModel fihiranaViewModel;
    private static final String TAG = "DialogUpdate";
    TextView txtMessage;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    boolean updating = false;
    List<Integer> completed = new ArrayList<>();
    Button btnUpdate;
    Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);


        txtMessage = findViewById(R.id.txtMessage);

        fihiranaViewModel = new ViewModelProvider(this).get(FihiranaViewModel.class);
        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);

        btnUpdate.setOnClickListener(view -> {

            fihiranaViewModel.getLastChange().observe(this, fanovana -> {
                if(!completed.contains(fanovana.getId())) {

                    Log.d(TAG, "onCreate: updating true");

                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    Log.d(TAG, "onCreate: fanovana get id = " + fanovana.getId());
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.GET,
                            "https://katolika.org/fihirana/getupdate/" + fanovana.getId(),
                            response -> {
                                if (response.equals("false")) {
                                    txtMessage.setText(R.string.upgrade_not_required);
                                    readyToClose();
                                } else {
                                    new ApplyUpdateThread(this, fihiranaViewModel, response).start();
                                }
                            },
                            error -> {
                                Log.d(TAG, "onCreate: 47");
                            }
                    );
                    requestQueue.add(stringRequest);

                    requestQueue.addRequestEventListener((request, eventListener) -> {
                        //affichage fotsiny ny eto fa ny update tokony efa atao isaky ny mihetsika
                        //dia filazana hoe vita = true
                        if(eventListener == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                            Log.d(TAG, "parent update finished");
                            txtMessage.setText(R.string.upgrade_not_required);
                            updating = false;
                            readyToClose();
                        }
                    });
                }
            });


        });
        btnCancel.setOnClickListener(view -> {
            finish();
        });
    }

    public void readyToClose() {
        btnUpdate.setVisibility(View.GONE);
        btnCancel.setText(R.string.update_close);
    }

    class ApplyUpdateThread extends Thread {
        private static final String TAG = "DialogUpdate thread";
        FihiranaViewModel fihiranaViewModel;
        Context context;
        String response;
        List<Fanovana> fanovanaList = new ArrayList<>();


        ApplyUpdateThread(Context context, FihiranaViewModel fv, String res) {
            this.context = context;
            this.fihiranaViewModel = fv;
            this.response = res;
        }

        @Override
        public void run() {
            try {
                JSONArray updatedJson = new JSONArray(response);
                int total = updatedJson.length();

                RequestQueue updateQueue = Volley.newRequestQueue(context);
                for (int i = 0; i < total; i++) {
                    int id = updatedJson.getJSONObject(i).getInt("id");
                    String c_table = updatedJson.getJSONObject(i).getString("c_table");
                    String c_type = updatedJson.getJSONObject(i).getString("c_type");
                    int c_id = updatedJson.getJSONObject(i).getInt("c_id");
                    int c_date = updatedJson.getJSONObject(i).getInt("c_date");
                    fanovanaList.add( new Fanovana(id, c_date, c_id, c_table, c_type));
                    completed.add(id);
                    Log.d(TAG, "run: insert ============ " + id);


                    int finalI = i;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> {
                        txtMessage.setText(getString(R.string.upgrade_number_of_total, finalI + 1, total));
                    });
                    //which table
                    switch (c_table) {
                        case "f_hira":
                            if (c_type.equals("d")) {
                                fihiranaViewModel.deleteHiraById(c_id);
                            } else {
                                //get hira
                                StringRequest stringRequest = new StringRequest(
                                        Request.Method.GET,
                                        "https://katolika.org/fihirana/getupdatedetails/" + id,
                                        content -> {
                                            try {
                                                JSONObject jsonObject = new JSONObject(content);

                                                if(jsonObject.getString("type").equals("f_hira")) {
                                                    Hira hira = new Hira();
                                                    hira.setId(jsonObject.getInt("_id"));
                                                    hira.setH_title(jsonObject.getString("h_title"));
                                                    hira.setH_text(jsonObject.getString("h_text"));
                                                    if(c_type.equals("c")) {
                                                        fihiranaViewModel.insertHira(hira);
                                                    }
                                                    else if(c_type.equals("u")) {
                                                        fihiranaViewModel.updateHira(hira);
                                                    }

                                                    //sokajy
                                                    if(jsonObject.getJSONArray("sokajy").length() > 0 ) {
                                                        fihiranaViewModel.saveSokajyJson(hira.getId(), jsonObject.getJSONArray("sokajy"));
                                                    }
                                                    //fihirana
                                                    if(jsonObject.getJSONArray("fihirana").length() > 0 ) {
                                                        fihiranaViewModel.saveFihiranaJson(hira.getId(), jsonObject.getJSONArray("fihirana"));
                                                    }
                                                    //salamo
                                                    if(jsonObject.getInt("salamo") > 0) {
                                                        fihiranaViewModel.saveSalamoJson(hira.getId(), jsonObject.getInt("salamo"));
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> {
                                            Log.d(TAG, "run: 136");
                                        }
                                );

                                updateQueue.add(stringRequest);
                            }
                            break;
                        case "f_fihirana":
                            //on ne supprimera jamais un fihirana
//                            if (c_type.equals("d")) {
//                                fihiranaViewModel.deleteHiraById(c_id);
//                            } else {
//                            }
//get hira
                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.GET,
                                    "https://katolika.org/fihirana/getupdatedetails/" + id,
                                    content -> {
                                        try {
                                            JSONObject jsonObject = new JSONObject(content);
                                            if(jsonObject.getString("type").equals("f_fihirana")) {
                                                Fihirana fihirana = new Fihirana();
                                                fihirana.setId(jsonObject.getInt("_id"));
                                                fihirana.setF_description(jsonObject.getString("f_description"));
                                                fihirana.setF_title(jsonObject.getString("f_title"));
                                                if(c_type.equals("c")) {
                                                    fihiranaViewModel.insertFihirana(fihirana);
                                                }
                                                else if(c_type.equals("u")) {
                                                    fihiranaViewModel.updateFihirana(fihirana);

                                                }

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    error -> {
                                        Log.d(TAG, "run: 175");
                                    }
                            );

                            updateQueue.add(stringRequest);
                            break;
                    }

                    if(i == total -1) {
                        runOnUiThread(DialogUpdate.this::readyToClose);
                    }
                }
                updateQueue.addRequestEventListener((request, eventListener) -> {
                    if(eventListener == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                        Log.d(TAG, "run: inserting ========== total ");
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

            /***
             * mila ovaina isaky ny mihetsika ihany ny fanovana
             * amin'izay raha bloké dia tsy miverina hatramin'ny voalohany
              */

            if(fanovanaList.size() > 0) {
                fihiranaViewModel.insertChangeList(fanovanaList);
            }
        }
    }
}
