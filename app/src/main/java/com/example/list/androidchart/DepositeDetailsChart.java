package com.example.list.androidchart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.list.androidchart.uitily.PieColors;
import com.example.list.androidchart.uitily.UserDao;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

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

    PieChart yespichart;
    private static String NAMESPACE = "";
    private static String URL = "";
    private static String SOAP_ACTION = "";
    private static String retjson = "";
    private static final String METHOD_NAME = "getBranchwiseDep";
    TextView tv_title,txt_heading;
    ArrayList<Integer> colors;
    String asondate="",type="";

    PrivateKey var1=null;
    String var5="",var3="";
    SecretKeySpec var2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_details_chart);

        tv_title= (TextView) findViewById(R.id.tv_title);

        var1 = (PrivateKey)getIntent().getSerializableExtra("VAR1");
        var3 = (String)getIntent().getSerializableExtra("VAR3");
        yespichart= (PieChart) findViewById(R.id.yespichart);

        colors = PieColors.getColors();

        txt_heading=(TextView)findViewById(R.id.txt_heading);
        String userName= UserDao.getUserName();
        txt_heading.setText("Welcome "+userName);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            asondate = extras.getString("ASONDATE");
            type= extras.getString("TYPE");
        }

        if(type.equalsIgnoreCase("DEP")){
            tv_title.setText(getString(R.string.lbl_branchWiseDep)+" "+asondate);
        }
        else{
            tv_title.setText(getString(R.string.lbl_branchWiseAdv)+" "+asondate);
        }
        CallWebService web=new CallWebService();
        web.execute();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Bundle b=new Bundle();
        Intent in=new Intent(DepositeDetailsChart.this, TwelveMonthsData.class);
        in.putExtra("VAR1", var1);
        in.putExtra("VAR3", var3);
        b.putString("TYPE",type);
        in.putExtras(b);
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
                jsonObj.put("TYPE",type);
                jsonObj.put("ASONDATE",asondate);
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
            JSONArray jarr=job.getJSONArray("RETVAL");
            for(int i=0;i<jarr.length();i++){

                JSONObject jobj=jarr.getJSONObject(i);
                String branch=jobj.getString("BRNCD");
                String brnName=jobj.getString("BRNNAME");
                float f=Float.parseFloat(jobj.getString("AMOUNT"));

                if(f<0) {
                    yvalues.add(new PieEntry(f*-1, brnName+"("+f*-1+")"));
                }
                else{
                    yvalues.add(new PieEntry(f, brnName+"("+f+")"));
                }
            }

            PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.lblBranch));
            dataSet.setColors(colors);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);
            Legend l = yespichart.getLegend();
            l.setEnabled(false);
            /*l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setWordWrapEnabled(true);
            l.setDrawInside(false);
            l.setYOffset(5f);*/
            int colorBlack = Color.parseColor("#000000");
            Description description = new Description();
            description.setText(getString(R.string.lbl_amountIn));
            description.setTextSize(20f);
            yespichart.setUsePercentValues(true);
            yespichart.setDescription(description);
            yespichart.setDrawHoleEnabled(true);
            yespichart.setTransparentCircleRadius(20f);
            yespichart.setHoleRadius(20f);
            yespichart.setEntryLabelColor(colorBlack);
            yespichart.setEntryLabelTextSize(12f);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            yespichart.setData(data);
            yespichart.invalidate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
