/**
 * Created by eddyarellanes on 29/03/17.
 * This is the only Code you'll need to do in a Gradle
 */
 compile 'com.android.volley:volley:1.0.0'


/**
 * Created by eddyarellanes on 29/03/17.
 * This is the only Code you'll need to do a Connection in your Activity/Fragment
 */
package com.github.arellanes.eddy;

 VolleyResponse notify= new VolleyResponse() {
            @Override
            public void notifySuccess(String response) {
                information= response;
                ArrayList benefits = new ArrayList();
                try{
                    Log.e("data",information);
                    JSONObject dataJson= new JSONObject(information);
                    JSONArray id_beneficio= dataJson.getJSONArray("id_beneficio");
                    JSONArray titulo= dataJson.getJSONArray("titulo");
                    JSONArray subtitulo= dataJson.getJSONArray("subtitulo");
                    JSONArray descripcion= dataJson.getJSONArray("descripcion");


                    for(int i=0;i<id_beneficio.length();i++){
                        HashMap<String, String> temporal = new HashMap<>();
                        temporal.put("title", (String) titulo.get(i));
                        temporal.put("subtitle", (String) subtitulo.get(i));
                        temporal.put("description", (String) descripcion.get(i));

                        benefits.add(i,temporal);


                    }
                    Singleton.getInstance().setBenefits(benefits);
                    fillLayout();
                }catch (Exception e){
                    Log.e("Error JSON",e.toString());
                }

            }


        };
        ConnectionHttpVolley connection= new ConnectionHttpVolley(notify);
        connection.get(Tarjetas.this,request);