package com.example.moltox.taxiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by moltox on 30.05.2016.
 */
public class HttpParser {

    private String httpMethod;
    private static final String TAG = "vOut: HttpParser Class";
    Context context;
    ProgressDialog pdialog;
    private boolean connectionReady;
    private HttpURLConnection connection;
    private String ApiKey;
    private OnRequestExecutedListener mOnRequestExecutedListener;

    public HttpParser(Context context, String httpMethod, String StringUrl, String ApiKey, OnRequestExecutedListener mListener)  throws IOException {
        httpMethod = "POST";
        this.context = context;
        String myUrl = StringUrl;
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mOnRequestExecutedListener = (OnRequestExecutedListener) mListener;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(mListener.toString()
                    + " must implement OnDialogConfirmedListener");
        }
        if (ApiKey != null)  {
            myUrl = myUrl + "?apikey=" + ApiKey;
        }

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            connectionReady = true;
            Log.v(HttpParser.TAG, "Network connection available");
            Log.v(HttpParser.TAG, "Tryin to create connection");
            URL url = null;
            url = new URL(myUrl);
        /*
        Setup Connection
         */
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setChunkedStreamingMode(50);
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Connection","Keep-Alive");
            // connection.setRequestProperty("Accept-Encoding", "identity");
            Log.v(TAG,"HttpUrlConnection setuped");
            Log.v(TAG, "Http Url: " + connection.getURL());
            Log.v(TAG, "Current HttpMethod is: " + connection.getRequestMethod());
            connectionReady=true;

        } else {
            connectionReady = false;
            Log.v(TAG, "No network connection available");
        }
        Log.v(TAG, "HttpParser(constructor) Class initialized");

    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod)
    {
        this.httpMethod = httpMethod;
    }

    public boolean IsConnectionReady() {
        return connectionReady;
    }

    public String exchangeData(JSONObject contentJSON) throws IOException {
        /*
        Do Connect
         */
        // connection.connect();
        Log.d(TAG, "HttpURLConnection -> Connected");
        connection.connect();
        JSONObject mJSONObject = contentJSON;
        String mJSONString = mJSONObject.toString();
        String urlencodedmJSONString = URLEncoder.encode(mJSONString,"UTF-8");
        int len = 5000;
        /*
        Do OutputStream
         */
        OutputStream dos = new DataOutputStream(connection.getOutputStream());
        dos.write(urlencodedmJSONString.getBytes());
        dos.flush();
        dos.close();


        /*
        Do InputStream Stuff
         */
        int responseCode = connection.getResponseCode();
        Log.d(TAG,"The Response Code is:" + responseCode);
        Log.d(TAG, "The Content Type is: " + connection.getContentType());
        Log.d(TAG, "The Content Encoding is: " + connection.getContentEncoding());
        InputStream dis = new DataInputStream(connection.getInputStream());
        String contentAsString = readIt(dis,len);
        Log.d(TAG, "ContentAsString is read");
        connection.disconnect();
        return contentAsString;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void execute(JSONObject requestContent)  {
        //  TODO; What happens if no InternetConnection available
        if (IsConnectionReady()) {
           makeHttpRequest makeHttpRequest =  new makeHttpRequest(requestContent,this.context);
           makeHttpRequest.execute();
        }
    }

    public class makeHttpRequest extends AsyncTask<String, Void, String> {

        JSONObject contentJSON;
        protected Context context;


        public makeHttpRequest(JSONObject jsonObject,Context context) {
            contentJSON = jsonObject;
            Log.v(TAG, "jsonObject transmitted");
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(this.context);
            pdialog.setMessage("Wait... ");
            pdialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(false);
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String returnString = null;
            Log.v(TAG, "makeHttpRequest Async Task doInBackground");
            // params comes from the execute() call: params[0] is the url.
            try {
                return exchangeData(contentJSON);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }

        }

        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Log.v(TAG, "OnPostExecute Result: " + result);
            Log.v(TAG, "Callin OnRequestExecutedListener");
            mOnRequestExecutedListener.OnRequestExecuted(result);
        }
    }

    public interface OnRequestExecutedListener  {
        public void OnRequestExecuted(String result);
    }

}