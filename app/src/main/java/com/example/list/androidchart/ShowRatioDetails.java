package com.example.list.androidchart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

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

public class ShowRatioDetails extends AppCompatActivity {

    PieChart yespichart;
    private static String retjson = "";
    TextView tv_title,txt_heading;
    ArrayList<Integer> colors;
    String type="";
    ArrayList<String> xdata=new ArrayList<String>();
    ArrayList<String> ydata=new ArrayList<>();
    PrivateKey var1=null;
    String var5="",var3="",is_cd="";
    SecretKeySpec var2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposite_details_chart);

        tv_title= (TextView) findViewById(R.id.tv_title);

        var1 = (PrivateKey)getIntent().getSerializableExtra("VAR1");
        var3 = (String)getIntent().getSerializableExtra("VAR3");
        txt_heading=(TextView)findViewById(R.id.txt_heading);
        String userName= UserDao.getUserName();
        txt_heading.setText("Welcome "+userName);

        yespichart= (PieChart) findViewById(R.id.yespichart);

        colors = new ArrayList<>();
        colors = PieColors.getColors();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("TYPE");
        }

        if(type.equalsIgnoreCase("CD")){
            tv_title.setText(getString(R.string.lbl_advAgtdep));
        }
        else if(type.equalsIgnoreCase("CC_AGT_CASA")){
            tv_title.setText(getString(R.string.lbl_depAgtLon));
        }
        else if(type.equalsIgnoreCase("TL_AGT_TD")){
            tv_title.setText(getString(R.string.lbl_TdepAgtTlon));
        }

        yespichart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if(type.equalsIgnoreCase("CD")) {
                    try {

                        int pos1 = e.toString().indexOf("y: ");
                        String balances = e.toString().substring(pos1 + 3);
                        Log.e("sud--", "" + xdata.size());
                        for (int i = 0; i < xdata.size(); i++) {
                            if (xdata.get(i).equalsIgnoreCase(balances)) {
                                pos1 = i;
                                break;
                            }
                        }
                        String typ = ydata.get(pos1);
                        Bundle b = new Bundle();
                        Intent in = new Intent(ShowRatioDetails.this, TypeWiseDepAdvDtl.class);
                        in.putExtra("VAR1", var1);
                        in.putExtra("VAR3", var3);
                        b.putString("TYPE", typ);
                        b.putString("IS_CD", type);
                        in.putExtras(b);
                        startActivity(in);
                        finish();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        CallWebService web=new CallWebService();
        web.execute();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent in=new Intent(ShowRatioDetails.this, Dashboard.class);
        in.putExtra("VAR1", var1);
        in.putExtra("VAR3", var3);
        startActivity(in);
        finish();
    }

    class CallWebService extends AsyncTask<Void, Void, Void>
    {
        LoadProgressBar loadProBarObj = new LoadProgressBar(ShowRatioDetails.this);
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

            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            Log.e("retjson-- ",retjson);
            JSONObject job = new JSONObject(retjson);


            float value1=Float.parseFloat(job.getString("VALUE1"));
            float value2=Float.parseFloat(job.getString("VALUE2"));
                if(value1!=0) {
                    ydata.add(0,lable1);
                    if (value1 < 0) {
                        xdata.add(0,""+(value1*-1));
                        yvalues.add(new PieEntry(value1 * -1,  lable1+"("+value1 * -1+")"));
                    } else {
                        xdata.add(0,""+(value1));
                        yvalues.add(new PieEntry(value1,  lable1+"("+value1+")"));
                    }
                }

            if(value2!=0) {
                ydata.add(1,lable2);
                if (value2 < 0) {
                    xdata.add(1,""+(value2*-1));
                    yvalues.add(new PieEntry(value2 * -1,  lable2+"("+value2 * -1+")"));
                } else {
                    yvalues.add(new PieEntry(value2,  lable2+"("+value2+")"));
                    xdata.add(1,""+(value2));

                }
            }


            PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.lblBranch));

            dataSet.setColors(colors);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            yespichart.setUsePercentValues(true);
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.BLACK);
            Legend l = yespichart.getLegend();
            l.setEnabled(false);
            int colorBlack = Color.parseColor("#000000");
            Description description = new Description();
            description.setText(getString(R.string.lbl_amountIn));
            description.setTextSize(15f);
            yespichart.setDescription(description);
            yespichart.setDrawHoleEnabled(true);
            yespichart.setTransparentCircleRadius(20f);
            yespichart.setHoleRadius(20f);
            yespichart.setEntryLabelColor(colorBlack);
            yespichart.setEntryLabelTextSize(12f);
            yespichart.setData(data);
            yespichart.invalidate();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
