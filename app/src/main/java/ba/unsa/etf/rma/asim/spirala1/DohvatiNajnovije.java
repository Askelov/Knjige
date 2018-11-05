package ba.unsa.etf.rma.asim.spirala1;

import android.os.AsyncTask;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by User on 22.05.2018..
 */

public class DohvatiNajnovije extends AsyncTask<String,Integer,Void> {


    public interface IDohvatiNajnovijeDone
    {
        public void onNajnovijeDone(ArrayList<Knjiga> knjige);
    }

    private ArrayList<Knjiga> knjige=new ArrayList<Knjiga>();
    private IDohvatiNajnovijeDone pok;

    public DohvatiNajnovije(IDohvatiNajnovijeDone p)
    {
        pok = p;
    }

    @Override
    protected Void doInBackground(String... params) {
        String query = null;
        try {

            query= URLEncoder.encode(params[0],"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "https://www.googleapis.com/books/v1/volumes?q=inauthor:"+query+"&maxResults=5&orderBy=newest";
        try {

            URL url = new URL(url1);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);


            JSONArray items = jo.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {

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
                        String knjigaidd=bookID;
                        Autor nekiAutor = new Autor(joBookAuthors.getString(k), knjigaidd);
                        bookAuthors.add(nekiAutor);
                    }
                } catch (Exception e){
                    /*ostane lista prazna*/

                }

                Knjiga nekaKnjiga=new Knjiga(bookID, bookTitle, bookAuthors, bookdesc, bookDate, slika,bookStr);
                knjige.add(nekaKnjiga);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        pok.onNajnovijeDone(knjige);
    }
    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally
        {
            try
            {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }
}
