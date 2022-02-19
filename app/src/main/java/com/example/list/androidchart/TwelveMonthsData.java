package com.example.list.androidchart;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.list.androidchart.uitily.UserDao;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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

public class TwelveMonthsData extends AppCompatActivity {

    BarChart barchart;
    private static String retjson = "";
    ArrayList barEntriesArrayList = new ArrayList<>();
    final ArrayList<String> xAxisLabel = new ArrayList<>();
    BarDataSet barDataSet;
    BarData barData;
    TextView btn_title;
    String type="";
    LineChart lineChart;
    PrivateKey var1=null;
    String var5="",var3="";
    SecretKeySpec var2=null;
    TextView txt_heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twelve_months_data);
        barchart=(BarChart)findViewById(R.id.barchart);
        lineChart=(LineChart) findViewById(R.id.lineChart);
        btn_title=(TextView) findViewById(R.id.btn_title);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("TYPE");
        }

        if(type.equalsIgnoreCase("DEP")){
            btn_title.setText(getString(R.string.lbl_yearDepSummary));
        }
        else{
            btn_title.setText(getString(R.string.lbl_yearAdvSummary));
        }
        txt_heading=(TextView)findViewById(R.id.txt_heading);
        String userName=UserDao.getUserName();
        txt_heading.setText("Welcome "+userName);
        var1 = (PrivateKey)getIntent().getSerializableExtra("VAR1");
        var3 = (String)getIntent().getSerializableExtra("VAR3");

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                int x=(int)e.getX();
                int y=(int)e.getY();
                if(y!=0) {
                    Bundle b = new Bundle();
                    Log.e("sud--x--", x + "---" + xAxisLabel.get(x));
                    Log.e("sud--y--", y + "");
                    Intent in = new Intent(TwelveMonthsData.this, DepositeDetailsChart.class);
                    in.putExtra("VAR1", var1);
                    in.putExtra("VAR3", var3);
                    in.putExtra("TYPE", type);
                    b.putString("ASONDATE", xAxisLabel.get(x));
                    in.putExtras(b);
                    startActivity(in);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"No Data Found For Date "+xAxisLabel.get(x),Toast.LENGTH_LONG).show();
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
        Intent in=new Intent(TwelveMonthsData.this, Dashboard.class);
        in.putExtra("VAR1", var1);
        in.putExtra("VAR3", var3);
        startActivity(in);
        finish();
    }

    class CallWebService extends AsyncTask<Void, Void, Void>
    {
        LoadProgressBar loadProBarObj = new LoadProgressBar(TwelveMonthsData.this);
        String[] valuesToEncrypt = new String[1];

        protected void onPreExecute()
        {
            loadProBarObj.show();
            JSONObject jsonObj=new JSONObject();
            try {
                jsonObj.put("TYPE",type);
                jsonObj.put("METHODCODE","6");
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
                    //drawBarChart();
                    drawLineChart();
                }
            }catch(Exception e){
                e.printStackTrace();
            }



        }// end onPostExecute

    }

    public void drawBarChart(){
        try{



            JSONObject job = new JSONObject(retjson);
            JSONArray jarr=job.getJSONArray("RETVAL");
            for(int i=0;i<jarr.length();i++){
                JSONObject jobj=jarr.getJSONObject(i);
                float value=Float.parseFloat(jobj.getString("AMOUNT"));
                Log.e("value","value-- "+value);
                if(jobj.getString("AMOUNT").indexOf("-")==-1){
                    barEntriesArrayList.add(new BarEntry(i, value,jobj.getString("DATE")));
                    xAxisLabel.add(jobj.getString("DATE"));
                }
                else{
                    barEntriesArrayList.add(new BarEntry(i, value*-1,jobj.getString("DATE")));
                    xAxisLabel.add(jobj.getString("DATE"));
                }

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
            Description description=new Description();
            description.setText(getString(R.string.lbl_amountIn));
            description.setTextSize(15f);
            barchart.setDescription(description);
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

    public void drawLineChart(){
        try {
            ArrayList<Entry> entries = new ArrayList<>();
            JSONObject job = new JSONObject(retjson);
            JSONArray jarr=job.getJSONArray("RETVAL");
            final String[] months = new String[jarr.length()];
            for(int i=0;i<jarr.length();i++) {
                JSONObject jobj=jarr.getJSONObject(i);
                float value=Float.parseFloat(jobj.getString("AMOUNT"));
                if(value<0){
                    value=value*-1;
                }
                xAxisLabel.add(jobj.getString("DATE"));
                months[i]=jobj.getString("DATE");
                entries.add(new Entry(i, value));
            }
            String lbl="";
            if(type.equalsIgnoreCase("DEP")){
                lbl="Deposits";
            }
            else{
                lbl="Advances";
            }

            LineDataSet dataSet = new LineDataSet(entries, lbl);
            dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

            //****
        // Controlling X axis
        XAxis xAxis = lineChart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Customizing x axis value
        //final String[] months = new String[]{"Yesterday", "Today"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value];
            }
        };
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        //***
        // Controlling right side of y axis
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        // Setting Data
        LineData data = new LineData(dataSet);
            Description description=new Description();
            description.setText(getString(R.string.lbl_amountIn));
            description.setTextSize(12f);
            lineChart.setDescription(description);
            lineChart.setData(data);
            lineChart.setScrollBarSize(13);
            lineChart.animateX(2000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
