package ba.unsa.etf.rma.asim.spirala1;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KATEGORIJA_NAZIV;

public class KategorijeAkt extends AppCompatActivity {

    public static ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    public static ArrayList<String> kategorije = new ArrayList<String>();
    public static boolean siriL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        BazaOpenHelper baza = new BazaOpenHelper(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);

        setContentView(R.layout.activity_kategorije_akt);

        //ucitamo iz baze
        kategorije = baza.dajListuKategorija();

// ako je prazna hardkodiramo ih nekoliko u bazu i onda citamo iz baze
        if (kategorije.isEmpty()) {
            long hardkodiranje = baza.dodajKategoriju("Roman");
            long hardkodiranje1 = baza.dodajKategoriju("Bajka");
            kategorije = baza.dajListuKategorija();

        }

        knjige = baza.dajListuKnjiga();
        /* dodamo i 2 knjige i 2 autora */
        if (knjige.isEmpty()) {

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.defaultphoto);
            ArrayList<Autor> a = new ArrayList<>();
            a.add(new Autor("Ivo Andric", "0"));
            URL b= null;
            try {
                b = new URL("http://books.google.com/books/content?id=Q-rJAQAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
            } catch (MalformedURLException e) {}

            Knjiga k = new Knjiga("Q-rJAQAACAAJ","Prokleta Avlija",a,"Prokleta Avlija je djelo Ive Andrica koje predstavlja izuzetnu kritiku vlasti uopce, pa tako i Osmanske.",
                    "2017-03-23",b,66);
            k.setImePrezime("Ivo Andric");
            k.setImeKategorije("Roman");


            long hardkodiranje = baza.dodajKnjigu(k);
            long hardkodiranjee = baza.dodajAutora("Ivo Andric");

            baza.dodajAutorstvo(hardkodiranje, hardkodiranjee);
            knjige = baza.dajListuKnjiga();
        }

        siriL = false;
        FragmentManager fm = getFragmentManager();
        FrameLayout ldetalji = (FrameLayout) findViewById(R.id.mjestoF2);
        if (ldetalji != null) {
            siriL = true;
            KnjigeFragment fd;
            fd = (KnjigeFragment) fm.findFragmentById(R.id.mjestoF2);
            if (fd == null) {
                fd = new KnjigeFragment();
                fm.beginTransaction().replace(R.id.mjestoF2, fd).commit();
            }
        }
        //ez
        ListeFragment f1 = (ListeFragment) fm.findFragmentByTag("Lista");
        if (f1 == null) {
            f1 = new ListeFragment();
            fm.beginTransaction().replace(R.id.mjestoF1, f1, "Lista").commit();
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }
}
