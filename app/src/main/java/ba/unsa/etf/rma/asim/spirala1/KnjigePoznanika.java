package ba.unsa.etf.rma.asim.spirala1;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.app.DownloadManager.STATUS_RUNNING;
import static android.drm.DrmInfoStatus.STATUS_ERROR;

/**
 * Created by User on 22.05.2018..
 */

public class KnjigePoznanika extends IntentService {

    public static int STATUS_START=0;
    public static int STATUS_FINISH=1;
    public static int STATUS_ERROR=2;

    public KnjigePoznanika() {
        super(null);
    }

    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver=intent.getParcelableExtra("receiver");
        Bundle b=new Bundle();
        receiver.send(STATUS_START,Bundle.EMPTY);
        ArrayList<Knjiga> knjige=new ArrayList<>();

        String idKorisnika=intent.getStringExtra("idKorisnika");
        try {
            idKorisnika = URLEncoder.encode(idKorisnika, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }
        String url1="https://www.googleapis.com/books/v1/users/"+idKorisnika+"/bookshelves";
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String rezultat = convertStreamToString(in);
            /*redom idemo*/
            JSONObject rez = new JSONObject(rezultat);
            JSONArray shelves = rez.getJSONArray("items");
            for(int i=0;i<shelves.length();i++) {
                JSONObject bookshelf=shelves.getJSONObject(i);
                String idShelf=bookshelf.getString("id");
                try {
                    idShelf = URLEncoder.encode(idShelf, "utf-8");
                } catch (UnsupportedEncodingException e) {

                }

                String link="https://www.googleapis.com/books/v1/users/"+idKorisnika+"/bookshelves/"+idShelf+"/volumes";
                /*kroz nivoe*/
                URL novi=new URL(link);
                HttpURLConnection urlConnection1=(HttpURLConnection)novi.openConnection();

                InputStream in1=new BufferedInputStream((urlConnection1.getInputStream()));
                String rezultat1=convertStreamToString(in1);

                JSONObject jo = new JSONObject(rezultat1);
                JSONArray items = jo.getJSONArray("items");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject book = items.getJSONObject(i);

                    String bookID = book.getString("id");
                    JSONObject volumeInfo=book.getJSONObject("volumeInfo");


                    String bookTitle= "No title";
                    try {
                        bookTitle= volumeInfo.getString("title");
                    }
                    catch(Exception e) {
                        bookTitle="No title";

                    }
                    String bookdesc="no value";
                    try {
                        bookdesc = volumeInfo.getString("description");
                    }
                    catch(Exception e) {
                        bookdesc="no value";

                    }
                    String bookDate="no value";
                    try {
                        bookDate = volumeInfo.getString("publishedDate");
                    } catch (Exception e){
                        bookDate="no value";

                    }
                    int bookStr=0;
                    try{
                        bookStr= volumeInfo.getInt("pageCount");}
                    catch (Exception e){
                        bookStr=0;
                    }
                    URL slika = new URL ("http://books.google.com/books/content?id=Su8hAQAAIAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
                    try {
                        JSONObject joslika = volumeInfo.getJSONObject("imageLinks");
                        slika = new URL(joslika.getString("thumbnail"));
                    } catch (Exception e){
                        slika=new URL("http://books.google.com/books/content?id=Su8hAQAAIAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
                    }
                    ArrayList<Autor> bookAuthors=new ArrayList<>();
                    try {
                        JSONArray joBookAuthors = volumeInfo.getJSONArray("authors");
                        for (int k = 0; k < joBookAuthors.length(); k++) {
                            Autor nekiAutor = new Autor(joBookAuthors.getString(k), bookID);
                            bookAuthors.add(nekiAutor);
                        }
                    } catch (Exception e){

                    }

                    Knjiga nekaKnjiga=new Knjiga(bookID, bookTitle, bookAuthors, bookdesc, bookDate, slika,bookStr);
                    knjige.add(nekaKnjiga);
                }
            }

        } catch (IOException | JSONException e) {
            receiver.send(STATUS_ERROR,Bundle.EMPTY);
        }


        b.putParcelableArrayList("listaKnjiga",knjige);
        receiver.send(STATUS_FINISH,b);
    }

    public String convertStreamToString(InputStream is) {
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e)
            {
            }
        }
        return sb.toString();
    }
}