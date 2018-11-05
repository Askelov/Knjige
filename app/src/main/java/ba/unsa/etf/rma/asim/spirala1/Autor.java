package ba.unsa.etf.rma.asim.spirala1;

import java.util.ArrayList;

/**
 * Created by User on 13.05.2018..
 */

public class Autor {
    String imeiPrezime;
    ArrayList<String> knjige;

    public Autor(String imeiPrezime, String knjigica) {
        this.imeiPrezime = imeiPrezime;
        ArrayList<String> knj= new ArrayList<String>();
        knj.add(knjigica);
    }

    public Autor(String imeiPrezime, ArrayList<String> knjigice) {
        this.imeiPrezime=imeiPrezime;
        for(String s: knjigice)
           if(!knjige.contains(s))
              knjige.add(s);
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String id){
        if(!knjige.contains(id))
            knjige.add(id);
    }
}
