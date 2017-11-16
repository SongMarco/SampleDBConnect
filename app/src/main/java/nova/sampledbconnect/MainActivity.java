package nova.sampledbconnect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


// 서버로부터 db 회원정보를 가져온다.







public class MainActivity extends AppCompatActivity {


    ImageView imView;
    TextView txtView;
    String imgUrl = "http://서버주소/appimg/";
    Bitmap bmImg;
    //back task;
    phpDown task;

    ArrayList<ListItem> listItem= new ArrayList<ListItem>();
    ListItem Item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task = new phpDown();
        txtView = (TextView)findViewById(R.id.textView);

        //imView = (ImageView) findViewById(R.id.imageView1);

        task.execute("http://115.68.231.13/project/android/appdata2.php");


    }


    private class back extends AsyncTask<String, Integer,Bitmap>{



        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                //String json = DownloadHtml("http://서버주소/appdata.php");
                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);


            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){
            imView.setImageBitmap(bmImg);
        }

    }
    private class phpDown extends AsyncTask<String, Integer,String> {



        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return jsonHtml.toString();

        }

        protected void onPostExecute(String str){

//            txtView.setText(str);
            String email, password, name, birthday;

            try{

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                for(int i=0; i<ja.length(); i++){
                    JSONObject jo = ja.getJSONObject(i);
                    email = jo.getString("email");
                    password = jo.getString("password");
                    name = jo.getString("name");
                    birthday = jo.getString("birthday");
                    Log.v("listTag", email+password+name+birthday);

                    listItem.add(new ListItem(email,password,name,birthday));


                }

            }catch(JSONException e){
                e.printStackTrace();
            }

            // 리스트아이템에 받아온 회원 정보가 들어있다.
            for(int i = 0; i<listItem.size(); i++){
                txtView.setText("email :"+listItem.get(i).getData(0)+
                        "\npassword:"+listItem.get(i).getData(1)+ "\nname:"+listItem.get(i).getData(2)
                        +"\nname:"+listItem.get(i).getData(3)     );
            }




        }
    }



}


