package ba.unsa.etf.rma.asim.spirala1;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_ID;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_NAZIV;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_PREGLEDANA;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.siriL;

/**
 * Created by User on 11.04.2018..
 */

public class KnjigeFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View iv = inflater.inflate(R.layout.knjige_fragment, container, false);

        if (getArguments() != null && getArguments().containsKey("Kategorija")) {
            final Button povratak = (Button) iv.findViewById(R.id.dPovratak);

            final Button preporuci = (Button) iv.findViewById(R.id.dPreporuci);
            final ListView listaKnjiga = (ListView) iv.findViewById(R.id.listaKnjiga);
            final ArrayList<Knjiga> ispis = new ArrayList<Knjiga>();
            final KnjigaAdapter adapter;
            adapter = new KnjigaAdapter(getActivity(), R.layout.element_liste_knjige, ispis);
            boolean autori = getArguments().getBoolean("Autor");
            final String uslovKategorija = getArguments().getString("Kategorija");
            int redbroj;


            // Toast.makeText(getActivity(), String.valueOf(redbroj)+" a uradjen "+String.valueOf(l), Toast.LENGTH_LONG).show();
            final BazaOpenHelper bazaOH = new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);


            if (!autori) {
                kategorije = bazaOH.dajListuKategorija();
                redbroj = kategorije.indexOf(uslovKategorija);
            /*ispis su kategoirije koje se priazuju*/
                ArrayList<Knjiga> aaa = bazaOH.knjigeKategorije((long) redbroj);
                for (Knjiga a : aaa)
                    ispis.add(a);
            } else {
                /*autori koji se ne ponavljaju*/
                ArrayList<String> aaa = bazaOH.dajListuAutora();
                int redbrojautora = aaa.indexOf(uslovKategorija);

                ArrayList<Knjiga> kbla = bazaOH.knjigeAutora((long) redbrojautora);


                for (Knjiga a : kbla)
                    ispis.add(a);

            }


            listaKnjiga.setAdapter(adapter);
            listaKnjiga.setClickable(true);

            listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //BazaOpenHelper bazaOH=new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
                    BazaOpenHelper bazaa = new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
                    Knjiga k = adapter.getItem(position);
                    String imenica = k.getNaziv();
                    if (adapter.getItem(position).isSelektovana()) {
                        Toast.makeText(getActivity(), "Knjiga odselektovana", Toast.LENGTH_LONG).show();
                        ContentValues updatedValues = new ContentValues();
                        /*update u bazi*/
                        updatedValues.put(KNJIGA_PREGLEDANA, 0);


                        String where = KNJIGA_NAZIV + "='" + imenica + "'";
                        String whereArgs[] = null;
                        SQLiteDatabase db = bazaa.getWritableDatabase();
                        db.update(BazaOpenHelper.DATABASE_TABLE2, updatedValues,
                                where, whereArgs);

                    } else {
                        Toast.makeText(getActivity(), "Knjiga selektovana", Toast.LENGTH_LONG).show();
                        ContentValues updatedValues = new ContentValues();
                        /*update u bazi*/
                        updatedValues.put(KNJIGA_PREGLEDANA, 1);


                        String where = KNJIGA_NAZIV + "='" + imenica + "'";
                        String whereArgs[] = null;
                        SQLiteDatabase db = bazaa.getWritableDatabase();
                        db.update(BazaOpenHelper.DATABASE_TABLE2, updatedValues,
                                where, whereArgs);


                    }
                    adapter.getItem(position).setSelektovana(!adapter.getItem(position).isSelektovana());
                    adapter.notifyDataSetChanged();
                }
            });

            povratak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack();
                }
            });


        }
        return iv;
    }
}





