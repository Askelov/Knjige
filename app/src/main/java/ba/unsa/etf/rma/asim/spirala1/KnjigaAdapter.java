package ba.unsa.etf.rma.asim.spirala1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.siriL;

/**
 * Created by User on 26.03.2018..
 */

public class KnjigaAdapter extends ArrayAdapter<Knjiga> {
    int resource = R.layout.element_liste_knjige;

    public KnjigaAdapter(Context con, int _resurs, List<Knjiga> items) {
        super(con, _resurs, items);
        resource = _resurs;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


        LinearLayout newView;

        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        Knjiga neki = getItem(position);
        if (neki != null) {
            Button preporuci= (Button) newView.findViewById(R.id.dPreporuci);


            /*opis*/
            final TextView opis= (TextView) newView.findViewById(R.id.eOpis);
            opis.setTypeface(null, Typeface.ITALIC);
            if(neki.getOpis()==null)
                opis.setText("nema opisa");
            else
                opis.setText(neki.getOpis());

            /*datum*/
            final TextView datum= (TextView) newView.findViewById(R.id.eDatumObjavljivanja);
            if(neki.getDatumObjavljivanja()==null)
                datum.setText("Datum objavljivanja: nepoznato");
            else
                datum.setText("Datum objavljivanja: "+neki.getDatumObjavljivanja());

            /*broj stranica*/
            final TextView broj= (TextView) newView.findViewById(R.id.eBrojStranica);
                if (neki.getBrojStranica()==0)
                broj.setText("Broj stranica: 0");
                else
                    broj.setText("Broj stranica: "+
                            String.valueOf( neki.getBrojStranica()));

            /*slika*/
             String slika= "";
            ImageView slikaKnjige = (ImageView) newView.findViewById(R.id.eNaslovna);
            if (neki.getSlikaa()==null){
                Picasso.get().load(neki.getSlika().toString()).into(slikaKnjige);
                slika=neki.getSlika().toString();
                                }
            else
            slikaKnjige.setImageBitmap(neki.getSlikaa());

            final String slikica=slika;

            /*naslov*/
            final TextView naziv = (TextView) newView.findViewById(R.id.eNaziv);
            naziv.setTypeface(null, Typeface.BOLD);
            if(neki.getImeKnjige()==null)
                naziv.setText(neki.getNaziv());
            else
            naziv.setText(neki.getImeKnjige());

            /*autor*/
            final TextView autor = (TextView) newView.findViewById(R.id.eAutor);
            autor.setTypeface(null, Typeface.ITALIC);
            autor.setText(neki.getImePrezime());


            String lista="";
            if(neki.getAutori()==null)
                lista=neki.getImePrezime();
            else {
                    for (Autor a : neki.getAutori()) {
                        lista += a.getImeiPrezime() + ", ";
                    }
                    if (lista.length() > 2)
                        lista=lista.substring(0, lista.length() - 2);

            }
            final String a=lista;

            preporuci.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Fragment fragment = new FragmentPreporuci();
                    Bundle args = new Bundle();
                    KategorijeAkt myActivity = (KategorijeAkt) getContext();
                    FragmentManager manager = myActivity.getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();


                        args.putString("Ime", naziv.getText().toString());
                        args.putString("Opis",opis.getText().toString());
                        args.putString("Datum", datum.getText().toString());
                        args.putString("Broj", broj.getText().toString());
                        args.putString("Autori", a);
                        args.putString("Slika",slikica);

                    fragment.setArguments(args);
                    if (siriL) {
                        transaction.replace(R.id.mjestoF2, fragment).addToBackStack(null).commit();
                    } else {
                        transaction.replace(R.id.mjestoF1, fragment).addToBackStack(null).commit();
                    }
                }

            });

            if (neki.selektovan) {
                newView.setBackgroundColor(0xffaabbed);
            }else{
                newView.setBackgroundResource(android.R.color.transparent);
            }
        }
        return newView;
    }




}
