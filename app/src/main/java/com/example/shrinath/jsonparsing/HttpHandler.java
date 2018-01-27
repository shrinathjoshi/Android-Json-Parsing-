package com.example.shrinath.jsonparsing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Shrinath on 27-12-2017.
 */

public class HttpHandler {

    public String makeServiceCall(String reqUrl)
    {
        String response=null;
        try{
            URL url=new URL(reqUrl);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            InputStream in=new BufferedInputStream(httpURLConnection.getInputStream());
            response=convertStringToString(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  response;
    }

    private String convertStringToString(InputStream in) {

        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb=new StringBuilder();

        String line;

        try {
            while((line=reader.readLine())!=null)
            {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            return sb.toString();
    }


}
