package com.example.list.androidchart;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.anastr.speedviewlib.SpeedView;
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
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {
    Button btnBarChart, btnPieChart;
    private  static String TAG="MainActivity";
    private static String NAMESPACE = "";
    private static String URL = "";
    private static String SOAP_ACTION = "";
    private static String retjson = "";
    private static final String METHOD_NAME = "getDashboardData";
    int cnt=0;
    Button btnpichart,btnbarchart,btnlinechart,btnspeedview,btnlinechart1;
    View panelpichart,panelbarchart,panellinechart,panelspeedview,panellinechart1;
    private  float[] yData={70f,20f,10f,8f,3f};
    String retVal="";
    SpeedView speedView;
    BarChart barchart;
    private String[] xData={"Jayesh","Shrikant","Toufik","Nitin"};
  PieChart pieChart;
  TextView txt_heading;
    LineChart linechart,linechart1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate: startingto create chart");
        btnpichart= (Button) findViewById(R.id.btnpichart);
        btnbarchart= (Button) findViewById(R.id.btnbarchart);
        btnlinechart= (Button) findViewById(R.id.btnlinechart);
        btnlinechart1=(Button)findViewById(R.id.btnlinechart1);
        btnspeedview=(Button)findViewById(R.id.btnspeedview);
        /*txt_heading = (TextView) findViewById(R.id.txt_heading);
        txt_heading.setText(getString(R.string.bank_name));*/
        panelpichart = findViewById(R.id.panelpichart);
        panelbarchart = findViewById(R.id.panelbarchart);
        panellinechart = findViewById(R.id.panellinechart);
        panellinechart1 =findViewById(R.id.panellinechart1);
        panelspeedview=findViewById(R.id.panelspeedview);
        btnpichart.setOnClickListener(this);
        btnbarchart.setOnClickListener(this);
        btnlinechart.setOnClickListener(this);
        btnspeedview.setOnClickListener(this);
        btnlinechart1.setOnClickListener(this);

        panelpichart.setOnClickListener(this);
        panellinechart.setOnClickListener(this);
        panellinechart1.setOnClickListener(this);
        panelspeedview.setOnClickListener(this);


        panelpichart.setVisibility(View.VISIBLE);
        panelbarchart.setVisibility(View.GONE);
        panellinechart.setVisibility(View.VISIBLE);




         speedView = (SpeedView) findViewById(R.id.speedView);
         barchart = (BarChart) findViewById(R.id.barchart);
         linechart = findViewById(R.id.linechart);
         CallWebService webservice=new CallWebService();


        webservice.execute();
        //setGraphs();
// change MAX speed to 320



    }
    public void setGraphs(){


        drawSpeedView();
        //pieChart=(PieChart)findViewById(R.id.pichart);
        //pieChart.setDescription("Work In Percentage");
        drawChart();

       /* pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Loans");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(true);
        addDataset();*/



        BarData dataq = new BarData(getDataSet());
        barchart.setData(dataq);
        //chart.animateXY(2000, 2000);
        barchart.invalidate();
        barchart.getXAxis().setDrawGridLines(false);
        // chart.getLeftAxis().setDrawGridLines(false);


        drawDepositeLineChart();

        drawLoanLineChart();

        //refresh


////line two



    }
    public void drawSpeedView(){
        try {
            JSONObject job = new JSONObject(retjson);
            int data=Integer.parseInt(job.getString("CDRATIO"));
            speedView.setMaxSpeed(20);
            speedView.setTickNumber(11);
            speedView.speedTo(data);
            speedView.setWithTremble(false);
            speedView.setUnit("% NPA");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private BarDataSet getDataSet() {

        ArrayList<BarEntry> entries = new ArrayList();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries,"hi");
        return dataset;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> labels = new ArrayList();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        return labels;
    }
    private  void addDataset(){
        ArrayList<PieEntry>yEntrys=new ArrayList<>();
        ArrayList<String>xEntrys=new ArrayList<>();

        for(int i=0;i<yData.length;i++)
        {
            yEntrys.add(new PieEntry(yData[i],i));
        }
        for(int i=0;i<xData.length;i++)
        {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet=new PieDataSet(yEntrys,"NPA Catagory");
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueTextSize(4);

        ArrayList<Integer> colors=new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);
      //  pieDataSet.set
        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setXEntrySpace(7);
        legend.setYEntrySpace(5);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        PieData pieData=new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

        /*BarChart chart = findViewById(R.id.barchart);

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(945f, 0));
        NoOfEmp.add(new BarEntry(1040f, 1));
        NoOfEmp.add(new BarEntry(1133f, 2));
        NoOfEmp.add(new BarEntry(1240f, 3));
        NoOfEmp.add(new BarEntry(1369f, 4));
        NoOfEmp.add(new BarEntry(1487f, 5));
        NoOfEmp.add(new BarEntry(1501f, 6));
        NoOfEmp.add(new BarEntry(1645f, 7));
        NoOfEmp.add(new BarEntry(1578f, 8));
        NoOfEmp.add(new BarEntry(1695f, 9));

        ArrayList year = new ArrayList();

        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");

        BarDataSet bardataset = new BarDataSet(NoOfEmp, "No Of Employee");
        chart.animateY(5000);
        BarData data = new BarData(year, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);*/
    }

    @Override
    public void onClick(View view) {
        Intent in=null;
        switch(view.getId())
        {

            case R.id.btnpichart:
               /* panelpichart.setVisibility(View.VISIBLE);
                panelbarchart.setVisibility(View.GONE);
                panellinechart.setVisibility(View.GONE);*/
                in = new Intent(MainActivity.this, PichartDemo.class);
                startActivity(in);
                finish();

                break;
            case R.id.btnbarchart:
               /* panelpichart.setVisibility(View.GONE);
                panelbarchart.setVisibility(View.VISIBLE);
                panellinechart.setVisibility(View.GONE);*/
                /*in = new Intent(MainActivity.this, PichartDemo.class);
                startActivity(in);
                finish();*/
                break;
            case R.id.btnlinechart:
               /* panelpichart.setVisibility(View.GONE);
                panelbarchart.setVisibility(View.GONE);
                panellinechart.setVisibility(View.VISIBLE);
*/              in = new Intent(MainActivity.this, DepositeDetailsChart.class);
                startActivity(in);
                finish();
                break;
            case R.id.btnlinechart1:
               /* panelpichart.setVisibility(View.GONE);
                panelbarchart.setVisibility(View.GONE);
                panellinechart.setVisibility(View.VISIBLE);
*/              in = new Intent(MainActivity.this, TypeWiseDepAdvDtl.class);
                startActivity(in);
                finish();
                break;
            case R.id.btnspeedview:
                in = new Intent(MainActivity.this, SpeedMeter.class);
                startActivity(in);
                finish();
                break;
        }
    }

    class CallWebService extends AsyncTask<Void, Void, Void>
    {
        LoadProgressBar loadProBarObj = new LoadProgressBar(MainActivity.this);

        String[] xmlTags = { "PARAMS" };
        String[] valuesToEncrypt = new String[1];
        String generatedXML ="";

        boolean isWSCalled = false;
        protected void onPreExecute()
        {
            cnt=0;
            loadProBarObj.show();
            JSONObject jsonObj=new JSONObject();
            try {
                jsonObj.put("NAME","111");
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }

            valuesToEncrypt[0] = jsonObj.toString();

            generatedXML = CryptoUtil.generateXML(xmlTags, valuesToEncrypt);
            System.out.println("&&&&&&&&&& generatedXML " + generatedXML);
        }

        protected Void doInBackground(Void... arg0) {
            NAMESPACE =getString(R.string.namespace);
            URL =getString(R.string.url);
            SOAP_ACTION =getString(R.string.soap_action);

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("Params", generatedXML);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,
                        180000);
                Log.e("suddd","androidHttpTransport"+androidHttpTransport);
                if (androidHttpTransport != null)
                    System.out
                            .println("=============== androidHttpTransport is not null ");
                else
                    System.out
                            .println("=============== androidHttpTransport is  null ");
                androidHttpTransport.call(SOAP_ACTION, envelope);
                retVal = envelope.bodyIn.toString().trim();
                retVal = retVal.substring(retVal.indexOf("=") + 1,
                        retVal.length() - 3);
                Log.e("suddd","suddddd11111");
                cnt=1;
                isWSCalled = true;
            }// end try
            catch (SocketTimeoutException e)
            {
                e.printStackTrace();
                cnt = 22;
            }
            catch(ConnectException e){
                e.printStackTrace();
                cnt=99;
            }
            catch (Exception e) {
                cnt=0;
                e.printStackTrace();
                System.out.println("Exception 2");
                System.out.println("Exception" + e);
            }
            return null;
        }// end doInBackground

        protected void onPostExecute(Void paramVoid)
        {
            loadProBarObj.dismiss();
            String[] xml_data = CryptoUtil.readXML(retVal,new String[] { "PARAMS" });
            Log.e("sudarshan--xml_data[0] ",xml_data[0]);
            retjson=xml_data[0];
            try{
                JSONObject jobj=new JSONObject(xml_data[0]);
                if(jobj.getString("RESPCODE").equalsIgnoreCase("0")){
                    setGraphs();
                    //drawChart();
                }
            }catch(Exception e){
                e.printStackTrace();
            }



        }// end onPostExecute

    }// end CallLoginWebService


    public void drawDepositeLineChart() {

        try{

            float yesvalue=0.0f,daybefore=0.0f;
            Log.e("sudarshan--- ",retjson);
            JSONObject job = new JSONObject(retjson);
            JSONArray jarr = job.getJSONArray("DEPOSITE");
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);

                Log.e("5289--- ",jobj.getString("YESDEPOSIT"));
                Log.e("7896389--- ",jobj.getString("BEFYESDEPOSIT"));

                yesvalue =Float.parseFloat(jobj.getString("YESDEPOSIT"));
                yesvalue=yesvalue/1000;

                daybefore =Float.parseFloat(jobj.getString("BEFYESDEPOSIT"));
                daybefore=daybefore/1000;


            }
            int bal1= (int) yesvalue;
            int bal2= (int) daybefore;

            Log.e("yesvalue--- ",""+bal1);
            Log.e("daybefore--- ",""+bal2);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, bal1));
        entries.add(new Entry(1, bal2));
        //entries.add(new Entry(2, 2));
        //entries.add(new Entry(3, 4));

        LineDataSet dataSet = new LineDataSet(entries, "Deposit");
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //****
        // Controlling X axis
        XAxis xAxis = linechart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Customizing x axis value
        final String[] months = new String[]{"Yesterday", "Day Bef.Yesterday"};//, "Mar", "Apr"

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
        YAxis yAxisRight = linechart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = linechart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        // Setting Data
        LineData data = new LineData(dataSet);
        linechart.setData(data);
        linechart.animateX(2500);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void drawLoanLineChart(){

        try{

            float yesvalue=0.0f,daybefore=0.0f;
            Log.e("sudarshan--- ",retjson);
            JSONObject job = new JSONObject(retjson);
            JSONArray jarr = job.getJSONArray("ADVANCE");
            for (int i = 0; i < jarr.length(); i++) {
                JSONObject jobj = jarr.getJSONObject(i);

                Log.e("5289--- ",jobj.getString("YESADVANCE"));
                Log.e("7896389--- ",jobj.getString("BEFYESADVANCE"));

                yesvalue =Float.parseFloat(jobj.getString("YESADVANCE"));
                yesvalue=yesvalue/1000;

                daybefore =Float.parseFloat(jobj.getString("BEFYESADVANCE"));
                daybefore=daybefore/1000;
            }
            int bal1= (int) yesvalue;
            int bal2= (int) daybefore;

        linechart1 = findViewById(R.id.linechart1);

        ArrayList<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0, bal1));
        entries1.add(new Entry(1, bal2));
        //entries.add(new Entry(2, 2));
        //entries.add(new Entry(3, 4));

        LineDataSet dataSet1 = new LineDataSet(entries1, "Advances");
        dataSet1.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet1.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //****
        // Controlling X axis
        XAxis xAxis1 = linechart1.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Customizing x axis value
        final String[] months1 = new String[]{"Yesterday", "Today"};//, "Mar", "Apr"

        IAxisValueFormatter formatter1 = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months1[(int) value];
            }
        };
        xAxis1.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis1.setValueFormatter(formatter1);

        //***
        // Controlling right side of y axis
        YAxis yAxisRight1 = linechart1.getAxisRight();
        yAxisRight1.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft1= linechart1.getAxisLeft();
        yAxisLeft1.setGranularity(1f);

        // Setting Data
        LineData data4 = new LineData(dataSet1);
        linechart1.setData(data4);
        linechart1.animateX(2500);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void drawChart() {
        try{


            float totamount=0;
            ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
            ArrayList<Integer> colors = new ArrayList<>();
            JSONObject job = new JSONObject(retjson);
            JSONArray jarr=job.getJSONArray("YESTERDAYLONDATA");
            Random rnd = new Random();
            for(int i=0;i<jarr.length();i++){

                JSONObject jobj=jarr.getJSONObject(i);
                float value=Float.parseFloat(jobj.getString("BALANCE"));
                Log.e("value","value-- "+value);
                if(jobj.getString("BALANCE").indexOf("-")==-1){
                    yvalues.add(new PieEntry(value, jobj.getString("NPASTAUS")));
                colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

                }
                else{
                colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
                    yvalues.add(new PieEntry(value*-1, jobj.getString("NPASTAUS"), i));

                }

            }
        PieChart pieChart = findViewById(R.id.pichart);
        pieChart.setUsePercentValues(true);



        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.loanlbl));
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setWordWrapEnabled(true);
        l.setDrawInside(false);
        l.setYOffset(6f);
        Description description = new Description();
        description.setText(getString(R.string.pie_chart));
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(60f);
        pieChart.setHoleRadius(60f);



        /*colors.add(Color.rgb(46, 204, 113));
        colors.add(Color.rgb(241, 196, 15));
        colors.add(Color.rgb(231, 76, 60));
        colors.add(Color.rgb(52, 152, 219));
        colors.add(Color.rgb(179, 100, 53));
        colors.add(Color.rgb(255, 140, 157));
        colors.add(Color.rgb(192, 255, 140));*/

        dataSet.setColors(colors);
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.BLACK);
        int colorBlack = Color.parseColor("#000000");
        pieChart.setEntryLabelColor(colorBlack);
        pieChart.setEntryLabelTextSize(8f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChart.setData(data);
        pieChart.invalidate();


    }catch(Exception e){
            e.printStackTrace();
        }

    }


}
