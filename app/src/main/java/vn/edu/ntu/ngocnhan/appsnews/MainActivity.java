package vn.edu.ntu.ngocnhan.appsnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView; //khai báo ánh xạ cho listView
    Customadapter customadapter;
    ArrayList<Docbao> mangdocbao;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.listView);
        mangdocbao= new ArrayList<Docbao>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Readdata().execute("https://vnexpress.net/rss/the-gioi.rss");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("link", mangdocbao.get(position).link);
                startActivity(intent);
            }
        });


    }
    //
    class Readdata extends AsyncTask<String,Integer,String>{


        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        private String getCharacterDataFromElement(Element line) {
            // TODO Auto-generated method stub
            Node child = line.getFirstChild();
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
            return "";
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListdescription = document.getElementsByTagName("description");
            String hinhanh = "";
            String title = "";
            String link = "";
            for (int i =0 ;i<nodeList.getLength(); i++){
                String cdata = nodeListdescription.item(i + 1).getTextContent();
                String[] arg = cdata.split(">");
                String[] img = arg[1].split("src=");
                String[] src = img[1].split("data-original=");
                for (int k = 0; k < src.length; k++) {
                    if (k == 1) {
                        hinhanh = src[k];
//                        Log.d("a", "aa: " + hinhanh);
                    }
                }

//                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"])+['\"][^>]*>");
//                Matcher matcher = p.matcher(cdata);
//                if (matcher.find()){
//                    hinhanh = matcher.group(1); //đọc
//                    Log.d("anh", "onPostExecute: " + hinhanh);
//                }
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                link = parser.getValue(element, "link");
                mangdocbao.add(new Docbao(title,link,hinhanh));
            }
            customadapter= new Customadapter(MainActivity.this,android.R.layout.simple_expandable_list_item_1, mangdocbao);
            listView.setAdapter(customadapter);
            super.onPostExecute(s);
        }
    }

    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
}
