package com.example.list.androidchart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.list.androidchart.uitily.CallSoapWebService;
import com.example.list.androidchart.uitily.EncryptionModel;
import com.example.list.androidchart.uitily.UitilyInstance;
import com.example.list.androidchart.uitily.UserDao;
import com.github.anastr.speedviewlib.AwesomeSpeedometer;
import com.github.anastr.speedviewlib.DeluxeSpeedView;
import com.github.anastr.speedviewlib.ImageSpeedometer;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.TubeSpeedometer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{
    private static String retjson = "";
    Button btn_yesDep,btn_yesAdv,btn_cdRatio,btn_npa,btn_casaCc,btn_tl_td;
    String retVal="";
    int cnt=0;
    TextView txt_heading,tv_depYes,tv_yesAdv;
    DeluxeSpeedView speedViewNPA;
    DeluxeSpeedView speedViewCasaCc;
    DeluxeSpeedView speedView;
    DeluxeSpeedView speedViewTlTd;
    String usrcode="";
    PrivateKey var1=null;
    String var5="",var3="";
    SecretKeySpec var2=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        tv_depYes=(TextView) findViewById(R.id.tv_depYes);
        tv_yesAdv=(TextView) findViewById(R.id.tv_yesAdv);

        speedView = (DeluxeSpeedView) findViewById(R.id.speedView);
        speedViewNPA = (DeluxeSpeedView) findViewById(R.id.speedViewNPA);
        speedViewCasaCc= (DeluxeSpeedView) findViewById(R.id.speedViewCasaCc);
        speedViewTlTd= (DeluxeSpeedView) findViewById(R.id.speedViewTlTd);

        btn_yesDep = (Button) findViewById(R.id.btn_yesDep);
        btn_yesAdv = (Button) findViewById(R.id.btn_yesAdv);
        btn_cdRatio= (Button) findViewById(R.id.btn_cdRatio);
        btn_npa= (Button) findViewById(R.id.btn_npa);
        btn_casaCc= (Button) findViewById(R.id.btn_casaCc);
        btn_tl_td= (Button) findViewById(R.id.btn_tl_td);
        btn_tl_td.setOnClickListener(this);
        btn_casaCc.setOnClickListener(this);
        btn_npa.setOnClickListener(this);
        btn_cdRatio.setOnClickListener(this);
        btn_yesDep.setOnClickListener(this);
        btn_yesAdv.setOnClickListener(this);
        txt_heading= (TextView) findViewById(R.id.txt_heading);
        Bundle bObj = getIntent().getExtras();
        var1 = (PrivateKey)getIntent().getSerializableExtra("VAR1");
        var3 = (String)getIntent().getSerializableExtra("VAR3");
        try {
            if (bObj != null) {
                Log.e("bObj---", "bObj-if");
                if(bObj.containsKey("USERID")){
                    Log.e("bObj---", "bObj-if1");
                    usrcode=bObj.getString("USERID");
                    UserDao.setUserId(usrcode);
                }
                else{

                    usrcode=UserDao.getUserId();
                    Log.e("bObj---", "bObj-else0"+usrcode);
                }
            }
            else{
                Log.e("bObj---", "bObj-else1");
                usrcode=UserDao.getUserId();
                Log.e("bObj---", "bObj-else2"+usrcode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bObj---", "bObj-exception"+usrcode);
            //Log.e("DASHBOARD", "" + e);
        }

        CallWebService webservice=new CallWebService();
        webservice.execute();
    }

    @Override
    public void onClick(View view) {
        Intent in=null;
        Bundle b=null;
        switch(view.getId())
        {

            case R.id.btn_yesDep:
                b=new Bundle();
                in = new Intent(Dashboard.this, TwelveMonthsData.class);
                in.putExtra("VAR1", var1);
                in.putExtra("VAR3", var3);
                b.putString("TYPE","DEP");
                in.putExtras(b);
                startActivity(in);
                finish();
                break;

            case R.id.btn_yesAdv:
                b=new Bundle();
                in = new Intent(Dashboard.this, TwelveMonthsData.class);
                in.putExtra("VAR1", var1);
                in.putExtra("VAR3", var3);
                b.putString("TYPE","ADV");
                in.putExtras(b);
                startActivity(in);
                finish();
                break;

            case R.id.btn_cdRatio:
                b=new Bundle();
                in = new Intent(Dashboard.this, ShowRatioDetails.class);
                in.putExtra("VAR1", var1);
                in.putExtra("VAR3", var3);
                b.putString("TYPE","CD");
                in.putExtras(b);
                startActivity(in);
                finish();
                break;

            case R.id.btn_npa:

                in = new Intent(Dashboard.this, TypeWiseNpaStatus.class);
                in.putExtra("VAR1", var1);
                in.putExtra("VAR3", var3);
                startActivity(in);
                finish();
                break;
            case R.id.btn_casaCc:
                b=new Bundle();
                in = new Intent(Dashboard.this, ShowRatioDetails.class);
                in.putExtra("VAR1", var1);
                in.putExtra("VAR3", var3);
                b.putString("TYPE","CC_AGT_CASA");
                in.putExtras(b);
                startActivity(in);
                finish();
                break;
            case R.id.btn_tl_td:
                b=new Bundle();
                in = new Intent(Dashboard.this, ShowRatioDetails.class);
                in.putExtra("VAR1", var1);
                in.putExtra("VAR3", var3);
                b.putString("TYPE","TL_AGT_TD");
                in.putExtras(b);
                startActivity(in);
                finish();
                break;
        }
    }

    public void onBackPressed() {
        showshareAlert(getString(R.string.alrt_exit));
    }

    public void showshareAlert(final String str)
    {
        CustomDialogClass alert=new CustomDialogClass(Dashboard.this, str) {
            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.custom_dialog_box);
                Button btn = (Button) findViewById(R.id.btn_cancel);
                TextView txt_message=(TextView)findViewById(R.id.txt_dia);
                txt_message.setText(str);
                btn.setOnClickListener(this);
                btn.setText("Cancel");
                Button btnok = (Button) findViewById(R.id.btn_ok);
                btnok.setOnClickListener(this);
                btnok.setText("Ok");
            }
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_ok:
                        this.dismiss();
                        finish();
                        break;

                    case R.id.btn_cancel:
                        this.dismiss();
                        break;
                    default:
                        break;
                }
                dismiss();
            }
        };
        alert.show();
    }


    class CallWebService extends AsyncTask<Void, Void, Void>
    {
        LoadProgressBar loadProBarObj = new LoadProgressBar(Dashboard.this);

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
                jsonObj.put("USERCODE",usrcode);
                jsonObj.put("METHODCODE","2");
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
           // String[] xml_data = CryptoUtil.readXML(retVal,new String[] { "PARAMS" });
            String str=CryptoClass.Function6(var5,var2);
            Log.e("sudarshan--xml_data[0] ",str);
            retjson=str;
            try{
                JSONObject jobj=new JSONObject(str);
                if(jobj.getString("RESPCODE").equalsIgnoreCase("0")){
                    String userName=jobj.getString("USERNAME");
                    Log.e("userName--",userName);
                    UserDao.setUserName(userName);
                    txt_heading.setText("Welcome "+userName);
                    tv_depYes.setText(jobj.getString("YESDEPOSIT")+" Lakhs.");

                    tv_yesAdv.setText(jobj.getString("YESADVANCE").replace("-","")+" Lakhs.");
                    setGraphs();
                    //drawChart();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }// end onPostExecute

    }// end CallLoginWebService
    public void setGraphs(){

        drawSpeedViewnpa();
        drawSpeedView();
        drawCasa_CcSpeedView();
        drawTD_TLSpeedView();
    }

    public void drawSpeedViewnpa(){
        try {
            JSONObject job = new JSONObject(retjson);
            int data=Integer.parseInt(job.getString("NPARATIO"));
            if(data>100){
                speedViewNPA.setMaxSpeed(200);
            }
            else{
                speedViewNPA.setMaxSpeed(100);
            }

            speedViewNPA.setTickNumber(11);
            speedViewNPA.speedTo(data);
            speedViewNPA.setWithTremble(false);
            speedViewNPA.setUnit(" %");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void drawSpeedView(){
        try {
            JSONObject job = new JSONObject(retjson);
            int data=Integer.parseInt(job.getString("CDRATIO"));
            if(data>100){
                speedView.setMaxSpeed(200);
            }
            else{
                speedView.setMaxSpeed(100);
            }
            speedView.setTickNumber(11);
            speedView.speedTo(data);
            speedView.setWithTremble(false);
            speedView.setUnit(" %");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void drawCasa_CcSpeedView(){
        try {
            JSONObject job = new JSONObject(retjson);
            int data=Integer.parseInt(job.getString("CASAAGTCC"));
            if(data<0){
                data=data*-1;
            }
            if(data>100){
                speedViewCasaCc.setMaxSpeed(200);
            }
            else{
                speedViewCasaCc.setMaxSpeed(100);
            }
            speedViewCasaCc.setTickNumber(11);
            speedViewCasaCc.speedTo(data);
            speedViewCasaCc.setWithTremble(false);
            speedViewCasaCc.setUnit(" %");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void drawTD_TLSpeedView(){
        try {
            JSONObject job = new JSONObject(retjson);
            int data=Integer.parseInt(job.getString("TERMDAGTTERML"));
            if(data>100){
                speedViewTlTd.setMaxSpeed(200);
            }
            else{
                speedViewTlTd.setMaxSpeed(100);
            }
            speedViewTlTd.setTickNumber(11);
            speedViewTlTd.speedTo(data);
            speedViewTlTd.setWithTremble(false);
            speedViewTlTd.setUnit(" %");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
