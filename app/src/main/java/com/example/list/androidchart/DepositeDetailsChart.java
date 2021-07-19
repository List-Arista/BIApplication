package com.example.list.androidchart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

public class DepositeDetailsChart extends AppCompatActivity {

    PieChart yespichart,befyespichart;
    private static String NAMESPACE = "";
    private static String URL = "";
    private static String SOAP_ACTION = "";
    private static String retjson = "";
    private static final String METHOD_NAME = "getBranchwiseDep";
    TextView daybefyesturday,yesturday;
    ArrayList<Integer> colors;
    String retVal="";

    PrivateKey var1=null;
    String var5="",var3="";
    SecretKeySpec var2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_details_chart);

        daybefyesturday= (TextView) findViewById(R.id.daybefyesturday);
        yesturday= (TextView) findViewById(R.id.yesturday);

        var1 = (PrivateKey)getIntent().getSerializableExtra("VAR1");
        var3 = (String)getIntent().getSerializableExtra("VAR3");

        daybefyesturday.setText(getString(R.string.depBefYes));
        yesturday.setText(getString(R.string.depYes));

        yespichart= (PieChart) findViewById(R.id.yespichart);
        befyespichart= (PieChart) findViewById(R.id.befyespichart);
        colors = new ArrayList<>();
        Random rnd = new Random();
        for(int i=0;i<=50;i++){
        colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        }
        CallWebService web=new CallWebService();
        web.execute();
        //drawYesturdayChart();
        //drawDayBeforeChart();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(DepositeDetailsChart.this, Dashboard.class);
        in.putExtra("VAR1", var1);
        in.putExtra("VAR3", var3);
        startActivity(in);
        finish();
    }

    class CallWebService extends AsyncTask<Void, Void, Void>
    {
        LoadProgressBar loadProBarObj = new LoadProgressBar(DepositeDetailsChart.this);

        String[] xmlTags = { "PARAMS" };
        String[] valuesToEncrypt = new String[1];
        String generatedXML ="";

        boolean isWSCalled = false;
        protected void onPreExecute()
        {
            loadProBarObj.show();
            JSONObject jsonObj=new JSONObject();
            try {
                jsonObj.put("NAME","111");
                jsonObj.put("METHODCODE","3");
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }

            valuesToEncrypt[0] = jsonObj.toString();

        }

        protected Void doInBackground(Void... arg0) {
            String value4 = getString(R.string.namespace);
            String value5 = getString(R.string.soap_action);
            String value6 = getString(R.string.url);
            final String value7 = "callWebservice";
            try {
                String keyStr=CryptoClass.Function2();
                var2=CryptoClass.getKey(keyStr);
                SoapObject request = new SoapObject(value4, value7);

                request.addProperty("value1", CryptoClass.Function5(valuesToEncrypt[0], var2));
                request.addProperty("value2", CryptoClass.Function3(keyStr, var1));
                request.addProperty("value3", var3);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(value6,45000);
                if(androidHttpTransport!=null)
                    System.out.println("=============== androidHttpTransport is not null ");
                else
                    System.out.println("=============== androidHttpTransport is  null ");

                androidHttpTransport.call(value5, envelope);
                var5 = envelope.bodyIn.toString().trim();
                int i = envelope.bodyIn.toString().trim().indexOf("=");
                var5 = var5.substring(i + 1, var5.length() - 3);

            }// end try
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }// end doInBackground

        protected void onPostExecute(Void paramVoid)
        {
            loadProBarObj.dismiss();
            String str=CryptoClass.Function6(var5,var2);

            retjson=str;
            try{
                JSONObject jobj=new JSONObject(str);
                if(jobj.getString("RESPCODE").equalsIgnoreCase("0")){
                    drawYesturdayChart();
                    drawDayBeforeChart();
                }
            }catch(Exception e){
                e.printStackTrace();
            }



        }// end onPostExecute

    }// end CallLoginWebService


    public void drawYesturdayChart() {
        try{


            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            Log.e("retjson-- ",retjson);
            JSONObject job = new JSONObject(retjson);
            JSONArray jarr=job.getJSONArray("YESDATA");
            for(int i=0;i<jarr.length();i++){

                JSONObject jobj=jarr.getJSONObject(i);
                String branch=jobj.getString("BRNCD");
                String brnName=jobj.getString("BRNNAME");
               float f=Float.parseFloat(jobj.getString("AMOUNT"));
                yvalues.add(new PieEntry(f,"Branch "+branch , i));
            }


            yespichart.setUsePercentValues(true);


            /*yvalues.add(new PieEntry(10f, "branch 1", 0));
            yvalues.add(new PieEntry(20f, "branch 2", 1));
            yvalues.add(new PieEntry(30f, "branch 3", 2));
            yvalues.add(new PieEntry(40f, "branch 4", 3));
            yvalues.add(new PieEntry(50f, "branch 5", 4));
            yvalues.add(new PieEntry(60f, "branch 6", 5));*/

            PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.lblBranch));
            PieData data = new PieData(dataSet);

            data.setValueFormatter(new PercentFormatter());
            Legend l = yespichart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setWordWrapEnabled(true);
            l.setDrawInside(false);
            l.setYOffset(5f);
            Description description = new Description();
            description.setText(getString(R.string.pie_chart));
            yespichart.setDescription(description);
            yespichart.setDrawHoleEnabled(true);
            yespichart.setTransparentCircleRadius(20f);
            yespichart.setHoleRadius(20f);



            /*colors.add(Color.rgb(46, 204, 113));
            colors.add(Color.rgb(241, 196, 15));
            colors.add(Color.rgb(231, 76, 60));
            colors.add(Color.rgb(52, 152, 219));
            colors.add(Color.rgb(179, 100, 53));
            colors.add(Color.rgb(255, 140, 157));
            colors.add(Color.rgb(192, 255, 140));*/

            dataSet.setColors(colors);
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);
            int colorBlack = Color.parseColor("#000000");
            yespichart.setEntryLabelColor(colorBlack);
            yespichart.setEntryLabelTextSize(12f);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            yespichart.setData(data);
            yespichart.invalidate();


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void drawDayBeforeChart() {
        try{



            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();


            befyespichart.setUsePercentValues(true);
            Log.e("retjson-- ",retjson);
            JSONObject job = new JSONObject(retjson);
            JSONArray jarr=job.getJSONArray("BEFYESDATA");

            for(int i=0;i<jarr.length();i++){
                JSONObject jobj=jarr.getJSONObject(i);
                String branch=jobj.getString("BRNCD");
                String brnName=jobj.getString("BRNNAME");
                float f=Float.parseFloat(jobj.getString("AMOUNT"));
                yvalues.add(new PieEntry(f,"Branch "+branch , i));
            }

            PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.lblBranch));
            PieData data = new PieData(dataSet);

            data.setValueFormatter(new PercentFormatter());
            Legend l = befyespichart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setWordWrapEnabled(true);
            l.setDrawInside(false);
            l.setYOffset(5f);
            Description description = new Description();
            description.setText(getString(R.string.pie_chart));
            befyespichart.setDescription(description);
            befyespichart.setDrawHoleEnabled(true);
            befyespichart.setTransparentCircleRadius(20f);
            befyespichart.setHoleRadius(20f);



            /*colors.add(Color.rgb(46, 204, 113));
            colors.add(Color.rgb(241, 196, 15));
            colors.add(Color.rgb(231, 76, 60));
            colors.add(Color.rgb(52, 152, 219));
            colors.add(Color.rgb(179, 100, 53));
            colors.add(Color.rgb(255, 140, 157));
            colors.add(Color.rgb(192, 255, 140));*/

            dataSet.setColors(colors);
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);
            int colorBlack = Color.parseColor("#000000");
            befyespichart.setEntryLabelColor(colorBlack);
            befyespichart.setEntryLabelTextSize(12f);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            befyespichart.setData(data);
            befyespichart.invalidate();


        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
