package com.github.arellanes.eddy;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
//This is for Customize Dialog Messages and Logs for Debug Mode
import android.util.Log;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by eddyarellanes on 28/02/17.
 * Parámetros que reciben las funciones post/get :
 * El Contexto, Dirección Url, Serie de Key-Value que harán de name-value en el WebService
 * Se recibe un JSON y se devuelve como String
 */

public class ConnectionHttpVolley {
    private RequestQueue requestQueue;
    public String result;
    VolleyResponse callback = null;
    //This is the intents to do a Connection if it fails, then return error message
    int tryConnectionPost=0;
    int tryConnectionGet=0;


    ConnectionHttpVolley(VolleyResponse callbackFrom){
        callback = callbackFrom;
    }

    public String post(final Context context, final String url, final HashMap data){
        requestQueue = Volley.newRequestQueue(context);

        try {//Connection to WebService
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.e("response",response);
                            callback.notifySuccess(response);




                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("Error Response", error.toString());
                            //result="Error";
                            if(tryConnectionPost<=3){
                                tryConnectionPost+=1;
                                post(context,url,data);
                            }
                            else{
                                errorMessage(context);
                            }

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    //Log.e("PARAMETROS",data.toString());
                    params.putAll(data);
                    return params;
                }
            };

            requestQueue.add(postRequest);

        } catch (Exception e) {
            Log.e("Connection Error", e.toString());

        }

        return result;
    }

    public String get(final Context context, final String url){

        requestQueue = Volley.newRequestQueue(context);
        try {//Connection to WebService
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.notifySuccess(response);
                            //Log.e("Result",result);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.e("Error Response", error.toString());
                            //result="Error";
                            if(tryConnectionGet<=3){
                                tryConnectionGet+=1;
                                get(context,url);
                            }
                            else{
                                errorMessage(context);
                            }

                        }
                    }
            ) {

            };

            requestQueue.add(postRequest);

        } catch (Exception e) {
            Log.e("Connection Error", e.toString());
            errorMessage(context );

        }
        return result;
    }
    public void errorMessage(Context  context){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        //You can set a Class property and put in a custom message on the activity :3
        builder.setMessage("No se encontraron resultados, verifique su conexión a internet o intente más tarde por favor")
                .setCancelable(false)
                .setPositiveButton("Entendido",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
        ;

        AlertDialog alert = builder.create();
        alert.show();
    }
}
