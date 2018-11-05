package ba.unsa.etf.rma.asim.spirala1;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by User on 25.03.2018..
 */

public class Knjiga implements Parcelable{
    String imePrezime;
    String imeKnjige;
    String imeKategorije;
    boolean selektovan=false;
    Bitmap slikaa;
    /*IZMJENE*/

    String id="";
    String naziv="";
    ArrayList<Autor> autori;
    String opis="";
    String datumObjavljivanja="" ;
    URL slika;
    int brojStranica=0;

    public URL getSlika() {
        return slika;
    }



    protected Knjiga(Parcel in) throws MalformedURLException{
        id=in.readString();

        naziv = in.readString();
        autori = in.readArrayList(Autor.class.getClassLoader());
        opis = in.readString();
        datumObjavljivanja = in.readString();
        slika = new URL(in.readString());
        brojStranica = in.readInt();
    }
    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            try{
                return new Knjiga(in);
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(id);
        dest.writeString(naziv);
        dest.writeList(autori);
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeString(slika.toString());
        dest.writeInt(brojStranica);

    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }



    public Knjiga(String imePrezime, String imeKnjige, String imeKategorije, Bitmap slikaa) {
        this.naziv = imeKnjige;
        this.imePrezime = imePrezime;
        this.imeKnjige = imeKnjige;
        this.imeKategorije = imeKategorije;
        this.slikaa = slikaa;
      //  this.slika=new URL("http://books.google.com/books/content?id=Su8hAQAAIAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        this.opis="no value";
    }

    public Bitmap getSlikaa() {
        return slikaa;
    }

    public void setSlikaa(Bitmap slika) {
        this.slikaa = slikaa;
    }

    public boolean isSelektovan() {
        return selektovan;
    }

    public void setSelektovan(boolean selektovan) {
        this.selektovan = selektovan;
    }

    public boolean isSelektovana() {
        return selektovan;
    }

    public void setSelektovana(boolean selektovana) {
        this.selektovan = selektovana;
    }

    public String getImeKategorije() {
        return imeKategorije;
    }

    public void setImeKategorije(String imeKategorije) {
        this.imeKategorije = imeKategorije;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public String getImeKnjige() {
        return imeKnjige;
    }

    public void setImeKnjige(String imeKnjige) {
        this.imeKnjige = imeKnjige;
    }

}
