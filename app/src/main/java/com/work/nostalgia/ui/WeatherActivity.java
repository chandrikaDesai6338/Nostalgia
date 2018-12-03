package com.work.nostalgia.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.work.nostalgia.R;
import com.work.nostalgia.model.WeatherData;
import com.work.nostalgia.network.RetrofitAPIInterface;
import com.work.nostalgia.network.RetrofitHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

public class WeatherActivity extends AppCompatActivity {

    public static String API_KEY = "d63e6792f6c4080a66cd092f5e114c1e";
    ProgressDialog ringProgressDialog;
    @NonNull
    private RetrofitAPIInterface retrofitAPIInterface;
    WeatherData response;

    EditText city;
    TextView cityText, temp, unitTemp, condDescr, skydescfull;
    ImageView imgView;
    Button weaherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city = findViewById(R.id.city);
        cityText = (TextView) findViewById(R.id.cityText);
        temp = (TextView) findViewById(R.id.temp);
        unitTemp = (TextView) findViewById(R.id.unittemp);
        condDescr = (TextView) findViewById(R.id.skydesc);
        imgView = (ImageView) findViewById(R.id.condIcon);
        weaherBtn = findViewById(R.id.weaherBtn);
        skydescfull = findViewById(R.id.skydescfull);


        //initRetrofit
        retrofitAPIInterface = new RetrofitHelper().getRetrofitAPIInterface();
        response = new WeatherData();

        //check Internet Availability
        if (!CheckConnectivity()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.internet))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            weaherBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!(city.getText().toString().trim().isEmpty())){
                        requestWeatherReport(city.getText().toString());
                    }else{
                        city.setError("City name required");
                        city.requestFocus();
                    }

                }
            });

        }
    }

    private void requestWeatherReport(String city) {
        //loading dialog init
        ringProgressDialog = new ProgressDialog(this);
        ringProgressDialog.setMessage(getString(R.string.loading));
        ringProgressDialog.show();
        Observable<WeatherData> observable = retrofitAPIInterface.getWeatherInfo(city, API_KEY);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<WeatherData>() {
                    @Override
                    public void onNext(WeatherData response) {
                        if(response.getCod() == 200 ){
                            display(response);
                        }
                        else{
                            ringProgressDialog.dismiss();
                            Toast.makeText(WeatherActivity.this,"City not Found!!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        ringProgressDialog.dismiss();

                    }
                });
    }

    private void display(WeatherData response) {
        city.setVisibility(View.GONE);
        weaherBtn.setVisibility(View.GONE);
        cityText.setText(response.getName());
        unitTemp.setText("°F");
        temp.setText(String.valueOf(response.getMain().getTemp()));
        condDescr.setText(response.getWeather().get(0).getDescription());
        skydescfull.setText(response.getWeather().get(0).getMain());
        String ImageUrl = "http://openweathermap.org/img/w/"+response.getWeather().get(0).getIcon()+".png";
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide
                .with(this)
                .load(ImageUrl)
                .apply(options)
                .into(imgView);
    }

    //Check Internet Availability
    public boolean CheckConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }

    }
}
