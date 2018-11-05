package ba.unsa.etf.rma.asim.spirala1;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.siriL;

/**
 * Created by User on 20.05.2018..
 */

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone,DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {

    public ArrayList<Knjiga> vraceneKnjige= new ArrayList<>();
    @Override
    public void onDohvatiDone(ArrayList<Knjiga> knjige) {
        ArrayList<String>imena=new ArrayList<>();

        for(Knjiga k: knjige) {         //izdvojimo imena knjiga koja ce se preikazat u spineru
            imena.add(k.getNaziv());
        }

        vraceneKnjige=knjige;
        final Spinner rez= (Spinner) getView().findViewById(R.id.sRezultat);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, imena);
        rez.setAdapter(adapter);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

switch (resultCode){
    case 0:
         break;
    case 1:
        ArrayList<Knjiga> knj=resultData.getParcelableArrayList("listaKnjiga");
        vraceneKnjige=knj;
        ArrayList<String>imena=new ArrayList<>();//za spinner imena
        for(Knjiga k: knj) {
            imena.add(k.getNaziv());
        }
        final Spinner rez= (Spinner) getView().findViewById(R.id.sRezultat);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, imena);
        rez.setAdapter(adapter);
        break;
    case 2:
        Toast.makeText(getActivity(),"greska",Toast.LENGTH_LONG).show();
        break;
}
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> knjige) {

        ArrayList<String>imena=new ArrayList<>();
        for(Knjiga k: knjige) {

            imena.add(k.getNaziv());
        }
        vraceneKnjige=knjige;
        final Spinner rez= (Spinner) getView().findViewById(R.id.sRezultat);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, imena);
        rez.setAdapter(adapter);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View iv = inflater.inflate(R.layout.fragment_online, container, false);
        final Spinner kat= (Spinner) iv.findViewById(R.id.sKategorije);
        final EditText tekstUpit= (EditText) iv.findViewById(R.id.tekstUpit);
        final Spinner rez= (Spinner) iv.findViewById(R.id.sRezultat);
        final Button pretraga = (Button) iv.findViewById(R.id.dRun);
        final Button dodajknjigu = (Button) iv.findViewById(R.id.dAdd);
        final Button povratak = (Button) iv.findViewById(R.id.dPovratak);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, kategorije);
        kat.setAdapter(adapter);



        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ime = tekstUpit.getText().toString();

                /*poziva se dohvati najnovije od tog autora*/
                    if (ime.contains("autor:"))
                        new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(ime.substring(6, ime.length()));
                    else
                        if(ime.contains("korisnik:")) {
                            Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                           MojResultReceiver mReceiver = new MojResultReceiver(new Handler());
                            mReceiver.setReceiver(FragmentOnline.this);
                            intent.putExtra("idKorisnika",ime.substring(9,ime.length()));
                            intent.putExtra("receiver",mReceiver);
                            getActivity().startService(intent);

                        }
                    else
                        /*normalni input*/
                        new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(ime);

            }
        });

        dodajknjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*ako nema nista u spineru knjigaa*/
                if (rez.getSelectedItem()==null) {
                    Toast.makeText(getActivity(), "nedovoljno podataka", Toast.LENGTH_SHORT).show();

                } else {


                    String odabranaKategorija = kat.getSelectedItem().toString();
                    String imeKnjige = rez.getSelectedItem().toString();
                    BazaOpenHelper bazaOH=new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);

                    for (Knjiga k : vraceneKnjige) {
                        if (k.naziv.equals(imeKnjige)) {
                            k.setImeKategorije(odabranaKategorija);
                            if(!k.getAutori().isEmpty()){
                            k.setImePrezime(k.getAutori().get(0).getImeiPrezime());
                               //u ime knjige stavimo samo prvog autora a ako ni njeg nema onda upisemo "nema autora"*/
                            }
                            else {k.setImePrezime("nema autora");
                            }
                            //provjera da li postoji vec unesensa knjiga;
                            boolean postoji=false;

                            for(Knjiga o: knjige){
                                if (o.getNaziv().equals(k.getNaziv()))
                                    postoji=true;
                            }
                            /*ako je nema unesemo */
                            if(!postoji){
                            knjige.add(k);


                            long rez=bazaOH.dodajKnjigu(k);
                            long rez2= bazaOH.dodajAutora(k.getImePrezime());
                            bazaOH.dodajAutorstvo(rez, rez2);

                            if(rez!=-1)

                            Toast.makeText(getActivity(), "upisana u bazu. id: "+String.valueOf(rez), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "greska pri unosu knjige" , Toast.LENGTH_SHORT).show();
                            break;
                            }
                            else {
                                Toast.makeText(getActivity(), "knjiga vec postoji u bazi" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }


            }
        });

        return iv;
    }

}
