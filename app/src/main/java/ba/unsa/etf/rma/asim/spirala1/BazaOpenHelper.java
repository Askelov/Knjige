package ba.unsa.etf.rma.asim.spirala1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;

/**
 * Created by User on 30.05.2018..
 */

public class BazaOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "mojaBaza.db";
    public static final int DATABASE_VERSION = 1;
    //TABELE
    public static final String DATABASE_TABLE = "Kategorija";
    public static final String DATABASE_TABLE2 = "Knjiga";
    public static final String DATABASE_TABLE3 = "Autor";
    public static final String DATABASE_TABLE4 = "Autorstvo";

    //KATEGORIJE
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";
    //KNJIGE
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUM = "datumObjavljivanja";
    public static final String KNJIGA_STRANICE = "brojStranica";
    public static final String KNJIGA_WEB = "idWebServis";
    public static final String KNJIGA_KAT = "idkategorije";
    public static final String KNJIGA_SLIKA = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";
    //AUTORI
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";
    //AUTORSTVO
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_AUTOR = "idautora";
    public static final String AUTORSTVO_KNJIGA = "idknjige";

    //kreiramo
    private static final String DATABASE_CREATE = "create table " +
            DATABASE_TABLE + " (" + KATEGORIJA_ID + " integer primary key autoincrement, " + KATEGORIJA_NAZIV + " text not null); ";

    private static final String DATABASE_CREATE2 = "create table " +
            DATABASE_TABLE2 + " (" + KNJIGA_ID + " integer primary key autoincrement, " + KNJIGA_NAZIV + " text not null, " +
            KNJIGA_OPIS + " text, " + KNJIGA_DATUM + " text, " + KNJIGA_STRANICE + " integer, " + KNJIGA_WEB + " text, " + KNJIGA_KAT + " integer not null, " +
            KNJIGA_SLIKA + " text, " + KNJIGA_PREGLEDANA + " integer ); ";

    private static final String DATABASE_CREATE3 = "create table " +
            DATABASE_TABLE3 + " (" + AUTOR_ID + " integer primary key autoincrement, " + AUTOR_IME + " text not null); ";

    private static final String DATABASE_CREATE4 = "create table " +
            DATABASE_TABLE4 + " (" + AUTORSTVO_ID + " integer primary key autoincrement, " + AUTORSTVO_AUTOR + " integer not null, " +
            AUTORSTVO_KNJIGA + " integer not null); ";


    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE2);
        db.execSQL(DATABASE_CREATE3);
        db.execSQL(DATABASE_CREATE4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE2);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE3);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE4);
        onCreate(db);
    }

    public long dodajAutora(String naziv) {
        ContentValues nova = new ContentValues();
        nova.put(AUTOR_IME, naziv);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(this.DATABASE_TABLE3, null, nova);
        db.close();
        return id;
    }

    public void dodajAutorstvo(long a, long b) {
        ContentValues nova = new ContentValues();
        nova.put(AUTORSTVO_AUTOR, a);
        nova.put(AUTORSTVO_KNJIGA, b);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(this.DATABASE_TABLE4, null, nova);
        db.close();
    }

    public long dodajKategoriju(String naziv) {
        ContentValues nova = new ContentValues();
        nova.put(KATEGORIJA_NAZIV, naziv);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(this.DATABASE_TABLE, null, nova);
        db.close();
        return id;
    }

    public long dodajKnjigu(Knjiga knjiga) {
        SQLiteDatabase db = this.getWritableDatabase();
      /* ArrayList<Autor>autori=new ArrayList<>();
                autori =knjiga.getAutori();
        ArrayList<String>uneseniAutori=dajListuAutora();
        for(Autor a: autori){
            if(!uneseniAutori.contains(a.getImeiPrezime()));

                dodajAutora(a.getImeiPrezime());
        }*/
        ArrayList<String> kate = dajListuKategorija();
        ContentValues nova = new ContentValues();
        nova.put(KNJIGA_NAZIV, knjiga.getNaziv());
        nova.put(KNJIGA_OPIS, knjiga.getOpis());
        nova.put(KNJIGA_DATUM, knjiga.getDatumObjavljivanja());
        nova.put(KNJIGA_STRANICE, knjiga.getBrojStranica());
        nova.put(KNJIGA_WEB, knjiga.getId());
        nova.put(KNJIGA_KAT, kate.indexOf(knjiga.getImeKategorije()));
        //dodamo hardkodiranu slikicu
        if (knjiga.getSlika() == null)
            nova.put(KNJIGA_SLIKA, "http://books.google.com/books/content?id=Su8hAQAAIAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        else nova.put(KNJIGA_SLIKA, knjiga.getSlika().toString());
        boolean selekted = knjiga.isSelektovana();
        if (selekted)
            nova.put(KNJIGA_PREGLEDANA, 1);
        else
            nova.put(KNJIGA_PREGLEDANA, 0);
        long id = db.insert(this.DATABASE_TABLE2, null, nova);
        db.close();
        return id;
    }

    public ArrayList<Knjiga> knjigeKategorije(long idKategorije) {
        ArrayList<Knjiga> knjige = dajListuKnjiga();
        ArrayList<Knjiga> zaVratit = new ArrayList<>();
        String kategorija = dajListuKategorija().get((int) idKategorije);
        for (Knjiga k : knjige)
            if (k.imeKategorije.equals(kategorija))
                zaVratit.add(k);
        return zaVratit;
    }

    public Integer dajIdKnjigeZaAutora(int IDautora) {
        String[] kolone_rezultat = new String[]{AUTORSTVO_AUTOR, AUTORSTVO_KNJIGA};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Integer> ideviknjiga = new ArrayList<>();
        Cursor cursor = db.query(this.DATABASE_TABLE4,
                kolone_rezultat, null,
                null, null, null, null);
        int i = 0;
        while (cursor.moveToNext()) {
            //PO PRVOJ KOLONI TRAZIMO ID AUTORA  KAD NADJEMO VRATIMO IZ ISTOG REDA SAMO IZ DRUGE KOLONE KNJIGU
            if (cursor.getInt(cursor.getColumnIndexOrThrow(AUTORSTVO_AUTOR)) == IDautora) {
                i = cursor.getInt(cursor.getColumnIndexOrThrow(AUTORSTVO_KNJIGA));
                break;
            }
        }
        cursor.close();
        return i;
    }

    public ArrayList<String> dajListuKategorija() {
        String[] kolone_rezultat = new String[]{KATEGORIJA_ID, KATEGORIJA_NAZIV};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> kategorije = new ArrayList<>();
        Cursor cursor = db.query(this.DATABASE_TABLE,
                kolone_rezultat, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            kategorije.add(cursor.getString(cursor.getColumnIndexOrThrow(KATEGORIJA_NAZIV)));
        }
        cursor.close();
        return kategorije;
    }

    public ArrayList<String> dajListuAutora() {
        //svi autori iz baze i oni koji se ponavljaju
        String[] kolone_rezultat = new String[]{AUTOR_ID, AUTOR_IME};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> aut = new ArrayList<>();
        Cursor cursor = db.query(this.DATABASE_TABLE3, kolone_rezultat, null, null, null, null, null);
        while (cursor.moveToNext()) {
            aut.add(cursor.getString(cursor.getColumnIndexOrThrow(AUTOR_IME)));
        }
        cursor.close();
        return aut;
    }

    public Knjiga knjigaOdId(int id) {
        Knjiga k = dajListuKnjiga().get(id);
        return k;
    }


    public ArrayList<Knjiga> knjigeAutora(long idAutora) {
        int id = (int) idAutora;
        String ime = dajListuAutora().get(id);
        ArrayList<Integer> isti = new ArrayList<>();
        int i = 0;
        for (String a : dajListuAutora()) {
            if (a.equals(ime))
                isti.add(i);
            i++;
        }
        ArrayList<Integer> idKnjiga = new ArrayList<>();
        for (Integer idd : isti) {
            idKnjiga.add(dajIdKnjigeZaAutora(idd));
        }
        ArrayList<Knjiga> kautora = new ArrayList<>();
        for (Integer back : idKnjiga) {
            kautora.add(knjigaOdId(back));
        }
        return kautora;
    }

    public ArrayList<Knjiga> dajListuKnjiga() {
        String[] kolone_rezultat = new String[]{KNJIGA_ID, KNJIGA_NAZIV, KNJIGA_OPIS, KNJIGA_DATUM, KNJIGA_STRANICE, KNJIGA_WEB, KNJIGA_KAT, KNJIGA_SLIKA, KNJIGA_PREGLEDANA};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Knjiga> knjige = new ArrayList<>();
        Cursor cursor = db.query(this.DATABASE_TABLE2, kolone_rezultat, null, null, null, null, null);
        int ID = cursor.getColumnIndexOrThrow(KNJIGA_ID);
        int NAZIV = cursor.getColumnIndexOrThrow(KNJIGA_NAZIV);
        int OPIS = cursor.getColumnIndexOrThrow(KNJIGA_OPIS);
        int DATUM = cursor.getColumnIndexOrThrow(KNJIGA_DATUM);
        int STRANICE = cursor.getColumnIndexOrThrow(KNJIGA_STRANICE);
        int KATEGORIJA = cursor.getColumnIndexOrThrow(KNJIGA_KAT);
        int SLIKA = cursor.getColumnIndexOrThrow(KNJIGA_SLIKA);
        int PREGLEDANA = cursor.getColumnIndexOrThrow(KNJIGA_PREGLEDANA);
        int redni = 0;
        while (cursor.moveToNext()) {
            //PRVO AUTORA NAPRAVIMO
            ArrayList<Autor> a = new ArrayList<>();
            Autor autic = new Autor(dajListuAutora().get(redni), "a");
            a.add(autic);
            URL b = null;
            try {
                b = new URL(cursor.getString(SLIKA));
            } catch (MalformedURLException e) {
            }
            int pregledana = cursor.getInt(PREGLEDANA);
            Knjiga k = new Knjiga(cursor.getString(ID), cursor.getString(NAZIV), a, cursor.getString(OPIS), cursor.getString(DATUM), b, cursor.getInt(STRANICE));
            k.setImePrezime(dajListuAutora().get(redni));
            k.setImeKategorije(dajListuKategorija().get(cursor.getInt(KATEGORIJA)));
            boolean pt = false;
            if (pregledana == 0)
                pt = false;
            else pt = true;
            k.setSelektovana(pt);
            knjige.add(k);
            redni++;
        }
        cursor.close();
        return knjige;

    }


}

