package ba.unsa.etf.rma.asim.spirala1;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;

/**
 * Created by User on 27.05.2018..
 */

public class FragmentPreporuci extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_preporuka, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button posalji= (Button) getView().findViewById(R.id.dPosalji);


        final String imee = getArguments().getString("Ime");
        final String opiss = getArguments().getString("Opis");
        final String datumm = getArguments().getString("Datum");
        final String brojj = getArguments().getString("Broj");
        final String autorii = getArguments().getString("Autori");
        final String slikaa=getArguments().getString("Slika");

       final TextView ime= (TextView) getView().findViewById(R.id.ime);
        final TextView opis= (TextView) getView().findViewById(R.id.opis);
        final TextView datum= (TextView) getView().findViewById(R.id.datum);
        final TextView broj= (TextView) getView().findViewById(R.id.stranice);
        final TextView autori= (TextView) getView().findViewById(R.id.autori);
        final ImageView slika=(ImageView) getView().findViewById(R.id.slika);
        final Spinner kontakti = (Spinner) getView().findViewById(R.id.sKontakti);
        final ArrayList<String> emails=new ArrayList<>();
        final ArrayList<String> names=new ArrayList<>();

        Cursor phones = getActivity().getContentResolver().query
                (ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String email=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

            emails.add(email);
            names.add(name);

        }
        phones.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, emails);
        kontakti.setAdapter(adapter);

        /*ovo prije plusa je da se i label prikaze "opis:"...blabla*/
        ime.setText(ime.getText()+imee);
        opis.setText(opis.getText()+opiss);
        datum.setText(datumm);
        broj.setText(brojj);
        autori.setText(autori.getText()+autorii);

        if (slikaa!=""){
            Picasso.get().load(slikaa).into(slika);}

        ime.setTypeface(null, Typeface.BOLD);
        autori.setTypeface(null, Typeface.ITALIC);

        posalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("mailto:"));
                i.setType("text/plain");
                int poz=kontakti.getSelectedItemPosition();
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{kontakti.getSelectedItem().toString()});
                String sadrzaj="";
                sadrzaj+="Cao "+ names.get(poz)+",\n"+ "Procitaj knjigu "+ imee + " od "+autorii +"!";
                i.putExtra(Intent.EXTRA_TEXT   , sadrzaj);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
