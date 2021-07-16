package org.katolika.fihirana.lib;

import android.app.VoiceInteractor;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.katolika.fihirana.lib.database.FihiranaViewModel;
import org.katolika.fihirana.lib.entities.Hira;

public class DialogUpdate extends BaseActivity {
    FihiranaViewModel fihiranaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        findViewById(R.id.btn_update).setOnClickListener(view -> {

            fihiranaViewModel.getLastChange().observe(this, fanovana -> {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        "https://katolika.org/fihirana/getupdate/" + fanovana.get_id(),
                        response -> {
                            if (response.equals("false")) {

                            } else {

                            }
                        },
                        error -> {

                        }
                );
                requestQueue.add(stringRequest);

                requestQueue.addRequestEventListener((request, event) -> {

                });
            });

        });
        findViewById(R.id.btn_cancel).setOnClickListener(view -> {
            finish();
        });
    }

    static class ApplyUpdateThread extends Thread {
        FihiranaViewModel fihiranaViewModel;
        String response;
        int id;
        String c_table;
        int c_id;
        String c_type;

        ApplyUpdateThread(FihiranaViewModel fv, String res) {
            this.fihiranaViewModel = fv;
            this.response = res;
        }

        @Override
        public void run() {
            try {
                JSONArray updatedJson = new JSONArray(response);
                int total = updatedJson.length();

                for (int i = 0; i < total; i++) {
                    id = updatedJson.getJSONObject(i).getInt("id");
                    c_table = updatedJson.getJSONObject(i).getString("c_table");
                    c_type = updatedJson.getJSONObject(i).getString("c_type");
                    c_id = updatedJson.getJSONObject(i).getInt("c_id");
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
                                                if(jsonObject.getString("c_type").equals("f_hira")) {
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

                                                    }
                                                    //fihirana
                                                    if(jsonObject.getJSONArray("fihirana").length() > 0 ) {

                                                    }
                                                    //salamo
                                                    if(jsonObject.getJSONArray("salamo").length() > 0 ) {

                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> {

                                        }
                                );
                                if (c_type.equals("c")) {

                                } else if (c_type.equals("u")) {

                                }
                            }
                            break;
                        case "f_fihirana":

                            break;
                    }
                    

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
