package ru.vlgu.homeautomation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

class textConnection extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progress;
    private Activity context;
    private Boolean test;

    public textConnection(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(context.getResources().getString(R.string.dialog_loading));
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiGateway gw = new ApiGateway();
        SharedPreferences settings = context.getSharedPreferences("userdetails", 0);
        test = gw.testConnection(settings.getString("server_address", ""));
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (test) Toast.makeText(context, context.getResources().getString(R.string.settings_test_true), Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, context.getResources().getString(R.string.settings_test_false), Toast.LENGTH_SHORT).show();
        progress.dismiss();
    }
}

class delSensor extends AsyncTask<Void, Void, Void> {
    private int id;
    private ProgressDialog progress;
    private Activity context;

    public delSensor(int id, Activity context) {
        this.id = id;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(context.getResources().getString(R.string.dialog_loading));
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiGateway gw = new ApiGateway();
        gw.delSensor(id);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        progress.dismiss();
    }
}

class setValue extends AsyncTask<Void, Void, Void> {
    private int id;
    private int value;
    private ProgressDialog progress;
    private Activity context;

    public setValue(Activity context, int id, int value) {
        this.id = id;
        this.value = value;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(context.getResources().getString(R.string.dialog_loading));
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiGateway gw = new ApiGateway();
        gw.setValue(id, value);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        progress.dismiss();
    }
}

public class ApiGateway {
    public String SERVERNAME = "10.0.1.4";

    public ApiGateway(String address) {
        SERVERNAME = address;
    }

    public static class StateData {
        public String curTemp = "";
        public String curOutsideTemp = "";
        public String curHumidity = "";
        public String curWaterTemp = "";
        public String curInputVoltage = "";
        public String serverAddress = "";
        public int serverStatus = 0;
        public String systemName = "";
        public String systemVer = "";
        public String androidVer = "";
    }

    public class Sensor {
        int id = 0;
        String name = "";
        int reading = 0;
        int room_id = 0;
        String type = "";
    }

    public class Camera {
        int id = 0;
        String name = "";
        String address = "";
        int room_id = 0;
    }

    public void addPlace(String name, String descr) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/add_place?room_name="+name+"&room_description="+descr);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public void addVideo(String name, String address, int room_id) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/add_video?camera_name"+name+"&camera_address="+address+"&room_id="+room_id);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public void addSensor(String name, String type, int room_id) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/add_sensor?name="+name+"&type="+type+"&room_id="+room_id);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public void delPlace(int id) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/del_place?id="+id);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public void delSensor(int id) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/del_sensor?id="+id);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public void delVideo(int id) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/del_video?id="+id);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public void setValue(int sensor_id, int value) {
        try {
            loadJsonRawObj("http://"+SERVERNAME+"/set_value?sensor_id="+sensor_id+"&value="+value);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
    }

    public ArrayList<Sensor> getLights() {
        ArrayList<Sensor> lights = new ArrayList<Sensor>();
        try {
            JSONArray message = loadJsonMsgArr("http://"+SERVERNAME+"/get_light");
            for (int i = 0; i<message.length(); i++) {
                Sensor light = new Sensor();
                JSONObject post = message.getJSONObject(i);
                light.id = post.getInt("sensor_id");
                light.name = post.getString("sensor_name");
                light.type = post.getString("sensor_type");
                light.reading = post.getInt("sensor_reading");
                light.room_id = post.getInt("sensor_room_id");
                lights.add(light);
            }
        } catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }

        return lights;
    }

    public ArrayList<Sensor> getOther() {
        ArrayList<Sensor> other = new ArrayList<Sensor>();
        try {
            JSONArray message = loadJsonMsgArr("http://"+SERVERNAME+"/get_other");
            for (int i = 0; i<message.length(); i++) {
                Sensor thing = new Sensor();
                JSONObject post = message.getJSONObject(i);
                thing.id = post.getInt("sensor_id");
                thing.name = post.getString("sensor_name");
                thing.type = post.getString("sensor_type");
                thing.reading = post.getInt("sensor_reading");
                thing.room_id = post.getInt("sensor_room_id");
                other.add(thing);
            }
        } catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
        return other;
    }

    public ArrayList<Sensor> getClimate() {
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        try {
            JSONArray message = loadJsonMsgArr("http://"+SERVERNAME+"/get_climate");
            for (int i = 0; i<message.length(); i++) {
                Sensor climate = new Sensor();
                JSONObject post = message.getJSONObject(i);
                climate.id = post.getInt("sensor_id");
                climate.name = post.getString("sensor_name");
                climate.type = post.getString("sensor_type");
                climate.reading = post.getInt("sensor_reading");
                climate.room_id = post.getInt("sensor_room_id");
                sensors.add(climate);
            }
        } catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }

        return sensors;
    }

    public ArrayList<Camera> getCameras() {
        ArrayList<Camera> cameras = new ArrayList<Camera>();
        try {
            JSONArray message = loadJsonMsgArr("http://"+SERVERNAME+"/get_video");
            for (int i = 0; i<message.length(); i++) {
                Camera camera = new Camera();
                JSONObject post = message.getJSONObject(i);
                camera.id = post.getInt("camera_id");
                camera.name = post.getString("camera_name");
                camera.address = post.getString("camera_address");
                camera.room_id = post.getInt("camera_room_id");
                cameras.add(camera);
            }
        } catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }

        return cameras;
    }

    public StateData getSystem() {
        StateData data = new StateData();
        try {
            JSONObject message = loadJsonMsgObj("http://"+SERVERNAME+"/get_system");
            data.curTemp = message.getString("current_temperature");
            data.curOutsideTemp = message.getString("current_outside_temperature");
            data.curHumidity = message.getString("current_humidity");
            data.curWaterTemp = message.getString("hot_running_water_temperature");
            data.curInputVoltage = message.getString("input_voltage");
            data.serverAddress = message.getString("server_address");
            data.serverStatus = message.getInt("server_status");
            data.systemName = message.getString("system_name");
            data.systemVer = message.getString("api_ver");
            data.androidVer = message.getString("andr_ver");
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
        }
        return data;
    }

    public ApiGateway() {
    }

    private JSONArray loadJsonMsgArr(String link) {
        Log.d("link", link);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            InputStream ips  = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String s;
            while(true)
            {
                s = buf.readLine();
                if(s==null || s.length()==0)
                    break;
                sb.append(s);
            }
            buf.close();
            ips.close();
            String jsontext = sb.toString();
            JSONObject entries = new JSONObject(jsontext);
            JSONArray message = entries.getJSONArray("data");
            return message;
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
            return null;
        }
    }
    private JSONObject loadJsonMsgObj(String link) {
        Log.d("link", link);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            InputStream ips  = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String s;
            while(true )
            {
                s = buf.readLine();
                if(s==null || s.length()==0)
                    break;
                sb.append(s);
            }
            buf.close();
            ips.close();
            String jsontext = sb.toString();
            JSONObject entries = new JSONObject(jsontext);
            return entries.getJSONArray("data").getJSONObject(0);
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
            return null;
        }
    }

    private JSONObject loadJsonRawObj(String link) {
        Log.d("link", link);
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            InputStream ips  = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String s;
            while(true )
            {
                s = buf.readLine();
                if(s==null || s.length()==0)
                    break;
                sb.append(s);

            }
            buf.close();
            ips.close();
            String jsontext = sb.toString();
            JSONObject entries = new JSONObject(jsontext);
            return entries;
        }
        catch (Exception je) {
            Log.e("JSON","Error w/file: " + je.getMessage());
            return null;
        }
    }
    public Boolean testConnection(String link) {
        JSONObject test = loadJsonRawObj("http://"+link+"/get_about");
        return (test != null);
    }
}
