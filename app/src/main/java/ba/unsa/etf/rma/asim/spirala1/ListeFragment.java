package ba.unsa.etf.rma.asim.spirala1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KATEGORIJA_NAZIV;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_KAT;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_NAZIV;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.siriL;

/**
 * Created by User on 11.04.2018..
 */

public class ListeFragment extends Fragment {

    boolean aut = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.liste_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aut = false;
        final Button online= (Button) getView().findViewById(R.id.dDodajOnline);
        final EditText tekst = (EditText) getView().findViewById(R.id.tekstPretraga);
        final Button pretraga = (Button) getView().findViewById(R.id.dPretraga);
        final Button dodajKategoriju = (Button) getView().findViewById(R.id.dDodajKategoriju);
        final Button dodajKnigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
        final ListView listaKategorija = (ListView) getView().findViewById(R.id.listaKategorija);
        final Button dugmeAutori = (Button) getView().findViewById(R.id.dAutori);
        final Button dugmeKategorija = (Button) getView().findViewById(R.id.dKategorije);
        final ArrayList<Integer> brojKnjiga = new ArrayList<Integer>();//nesto kao histogram da znam broj knjgia za svakog autora

       /*BAZA*/
        BazaOpenHelper baza = new BazaOpenHelper(getActivity(), DATABASE_NAME,null, DATABASE_VERSION);
        final ArrayList<String> autori = baza.dajListuAutora(); //SVI AUTORI
        final ArrayList<String> autoriispis = new ArrayList<>();    //AUTORI KOJI SE NE PONALBJAJU
        final ArrayAdapter<String> adapter; //ispis kategorija
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
        final ArrayAdapter<String> adaptera;    //ispis autora koji se ne ponavljaju
        adaptera = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, autoriispis);

        //lista autora koji se ne ponavljaju
        for(String a: autori){
            if(!autoriispis.contains(a))
                autoriispis.add(a);
        }

        dodajKnigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DodavanjeKnjigeFragment();
                if (siriL) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF3, fragment).addToBackStack(null).commit();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment).addToBackStack(null).commit();
                }
            }
        });


        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new KnjigeFragment();
                Bundle args = new Bundle();
                //bool aut = true ako treba izlistat autore, a false ako treba kategorije

                args.putString("Kategorija", listaKategorija.getItemAtPosition(position).toString());
                //ako je aut=true onda je Kategorija ime autora, a ako je false onda je ime kategorije
                args.putBoolean("Autor", aut);
                fragment.setArguments(args);
                if (siriL) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF2, fragment).commit();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment).addToBackStack(null).commit();
                }
            }
        });


        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(tekst.getText(), new Filter.FilterListener() {


                    @Override
                    public void onFilterComplete(int i) {
                        if (adapter.isEmpty()) {
                            dodajKategoriju.setEnabled(true);
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                dodajKategoriju.setEnabled(false);
            }
        });

        dugmeAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pretraga.setVisibility(View.GONE);
                dodajKategoriju.setVisibility(View.GONE);
                tekst.setVisibility(View.GONE);
                listaKategorija.setAdapter(adaptera);
                Fragment fragment = new KnjigeFragment();
                if (siriL) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF2, fragment).commit();
                }
                aut = true;
            }
        });
        dugmeKategorija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aut = false;
                pretraga.setVisibility(View.VISIBLE);
                dodajKategoriju.setVisibility(View.VISIBLE);
                tekst.setVisibility(View.VISIBLE);
                Fragment fragment = new KnjigeFragment();
                if (siriL) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF2, fragment).commit();
                }
                listaKategorija.setAdapter(adapter);
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentOnline();
                //siriL da znam jel mobitel postavljen horizontalno il vodoravno
                if (siriL) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF3, fragment).addToBackStack(null).commit();
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment).addToBackStack(null).commit();
                }

            }
        });


        dodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategorije.add(0, tekst.getText().toString());
                //upis kategorije u bazu
                BazaOpenHelper bazaOH=new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);

                long rez=bazaOH.dodajKategoriju(tekst.getText().toString());
                if (rez!=-1)Toast.makeText(getActivity(), "kategorija unesena. ID: "+String.valueOf(rez), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "greska prilikom unosa kategorije" , Toast.LENGTH_LONG).show();

                adapter.add(tekst.getText().toString());
                adapter.notifyDataSetChanged();
                adapter.getFilter().filter("");
                tekst.setText("");
                dodajKategoriju.setEnabled(false);
            }
        });


    }


}

