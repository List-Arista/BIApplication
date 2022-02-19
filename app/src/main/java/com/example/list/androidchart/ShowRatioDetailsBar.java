package com.example.list.androidchart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

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

import javax.crypto.spec.SecretKeySpec;

public class ShowRatioDetailsBar extends AppCompatActivity {

    BarChart barchart;
    private static String retjson = "";
    ArrayList barEntriesArrayList = new ArrayList<>();
    final ArrayList<String> xAxisLabel = new ArrayList<>();
    BarDataSet barDataSet;
    BarData barData;

    String type="";

    PrivateKey var1=null;
    String var5="",var3="";
    SecretKeySpec var2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_ratio_details);
        barchart=(BarChart)findViewById(R.id.barchart);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("TYPE");
        }

        if(type.equalsIgnoreCase("CD")){

        }
        else if(type.equalsIgnoreCase("CC_AGT_CASA")){

        }
        else if(type.equalsIgnoreCase("TL_AGT_TD")){

        }

        var1 = (PrivateKey)getIntent().getSerializableExtra("VAR1");
        var3 = (String)getIntent().getSerializableExtra("VAR3");

        barchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                if(type.equalsIgnoreCase("CD")) {
                    int x = (int) e.getX();
                    int y = (int) e.getY();
                    Bundle b = new Bundle();
                    Log.e("sud--x--", x + "---" + xAxisLabel.get(x));
                    Log.e("sud--y--", y + "");
                    Intent in = new Intent(ShowRatioDetailsBar.this, TypeWiseDepAdvDtl.class);
                    in.putExtra("VAR1", var1);
                    in.putExtra("VAR3", var3);
                    b.putString("TYPE", xAxisLabel.get(x));
                    b.putString("IS_CD", type);
                    in.putExtras(b);
                    startActivity(in);
                    finish();
                }
            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        CallWebService cws=new CallWebService();
        cws.execute();

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(ShowRatioDetailsBar.this, Dashboard.class);
        in.putExtra("VAR1", var1);
        in.putExtra("VAR3", var3);
        startActivity(in);
        finish();
    }

    class CallWebService extends AsyncTask<Void, Void, Void>
    {
        LoadProgressBar loadProBarObj = new LoadProgressBar(ShowRatioDetailsBar.this);
        String[] valuesToEncrypt = new String[1];

        protected void onPreExecute()
        {
            loadProBarObj.show();
            JSONObject jsonObj=new JSONObject();
            try {
                jsonObj.put("TYPE",type);
                jsonObj.put("METHODCODE","7");
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
            Log.e("sudarshan--xml_data[0] ",str);
            retjson=str;
            try{
                JSONObject jobj=new JSONObject(str);
                if(jobj.getString("RESPCODE").equalsIgnoreCase("0")){
                    drawBarChart();
                }
            }catch(Exception e){
                e.printStackTrace();
            }



        }// end onPostExecute

    }



    public void drawBarChart(){
        try{



            JSONObject job = new JSONObject(retjson);
                String lable1="",lable2="";
                if(type.equalsIgnoreCase("CD")){
                    lable1="DEPOSIT";
                    lable2="ADVANCE";
                }
                else if(type.equalsIgnoreCase("CC_AGT_CASA")){
                    lable1="CC";
                    lable2="CASA";
                }
                else if(type.equalsIgnoreCase("TL_AGT_TD")){
                    lable1="Term Loan";
                    lable2="Term Deposit";
                }


                float value1=Float.parseFloat(job.getString("VALUE1"));
                if(job.getString("VALUE1").indexOf("-")==-1){
                    barEntriesArrayList.add(new BarEntry(0, value1,lable1));
                    xAxisLabel.add(lable1);
                }
                else{
                    barEntriesArrayList.add(new BarEntry(0, value1*-1,lable1));
                    xAxisLabel.add(lable1);
                }

            float value2=Float.parseFloat(job.getString("VALUE2"));
            if(job.getString("VALUE2").indexOf("-")==-1){
                barEntriesArrayList.add(new BarEntry(1, value2,lable2));
                xAxisLabel.add(lable2);
            }
            else{
                barEntriesArrayList.add(new BarEntry(1, value2*-1,lable2));
                xAxisLabel.add(lable2);
            }

            for(int i=2;i<12;i++){

                    barEntriesArrayList.add(new BarEntry(i, 0,""));
                    xAxisLabel.add("");
            }



            barDataSet = new BarDataSet(barEntriesArrayList, "Loan Status");
            barData = new BarData(barDataSet);

            barchart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));


            barchart.setData(barData);
            barchart.getAxisRight().setEnabled(false);
            barchart.setScaleMinima(4f, 0f);
            barchart.setPinchZoom(false);
            barchart.setDoubleTapToZoomEnabled(false);
            //barchart.getXAxis().setAxisMinimum(0f);
            //barchart.getAxisLeft().setStartAtZero(false);
            //barchart.getAxisRight().setStartAtZero(false);
            //barchart.getAxisLeft().setDrawGridLines(false);
            barchart.getXAxis().setDrawGridLines(false);
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(Color.BLACK);
            barchart.animateXY(2000, 2000);
            barDataSet.setValueTextSize(10f);
            //barchart.getDescription().setEnabled(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
