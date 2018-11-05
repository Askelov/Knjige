package ba.unsa.etf.rma.asim.spirala1;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.getIntent;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KATEGORIJA_NAZIV;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_KAT;
import static ba.unsa.etf.rma.asim.spirala1.BazaOpenHelper.KNJIGA_NAZIV;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.knjige;

/**
 * Created by User on 11.04.2018..
 */

public class DodavanjeKnjigeFragment extends Fragment {
    public ImageView targetImage;
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState){

        return inflater.inflate(R.layout.dodavanje_knjige_fragment,container,false);
}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         targetImage = (ImageView) getView().findViewById(R.id.naslovnaStr);
        final EditText autor = (EditText) getView().findViewById(R.id.imeAutora);
        final EditText naziv = (EditText)  getView().findViewById(R.id.nazivKnjige);
        final Button nadjiSliku = (Button)  getView().findViewById(R.id.dNadjiSliku);
        final Button upisiKnjigu = (Button)  getView().findViewById(R.id.dUpisiKnjigu);
        final Spinner kateSpiner = (Spinner)  getView().findViewById(R.id.sKategorijaKnjige);
        final Button ponisti = (Button)  getView().findViewById(R.id.dPonisti);

      Intent intent = getActivity().getIntent();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, kategorije);
        kateSpiner.setAdapter(adapter);

        upisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        /*provjera jesul popunjena sva mjesta*/
        if (autor.getText().toString().equals("") || naziv.getText().toString().equals("") )
            Toast.makeText(getActivity(), "Popunite prazna mjesta ili promijenite ime knjige", Toast.LENGTH_SHORT).show();
        else {
            //od imageviwa pravimo bitmapu za konstruktor
            targetImage.setDrawingCacheEnabled(true);
            targetImage.buildDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(targetImage.getDrawingCache());

            Knjiga k=new Knjiga(autor.getText().toString(), naziv.getText().toString(), kateSpiner.getSelectedItem().toString(), bitmap);


            k.setId("nema ID");
            k.setOpis("no value");
            k.setBrojStranica(0);
            k.setSelektovana(false);
            boolean postoji=false;

            /*provjera jel ima vec knjiga da je ne unosimo 2 puta isut knjigu*/
            for(Knjiga o: knjige){
                if (o.getNaziv().equals(naziv.getText().toString()))
                    postoji=true;
            }

            if(!postoji){

                knjige.add(k);

                BazaOpenHelper bazaOH = new BazaOpenHelper(getActivity(), DATABASE_NAME, null, DATABASE_VERSION);
                ArrayList<Autor> at = new ArrayList<>();
                at.add(new Autor(autor.getText().toString(), k.getNaziv()));
                k.setAutori(at);
                /*unosimo knjigu, autora, i popunjavamo autorstvo*/
                long rez = bazaOH.dodajKnjigu(k);
                long rez2 = bazaOH.dodajAutora(autor.getText().toString());
                bazaOH.dodajAutorstvo(rez, rez2);
                if (rez != -1)
                    Toast.makeText(getActivity(), "upisana u bazu. id: " + String.valueOf(rez), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "greska pri unosu knjige", Toast.LENGTH_SHORT).show();
            }
            /*ako vec ima knjiga*/
            else {
                Toast.makeText(getActivity(), "knjiga s datim naslovom vec postoji u bazi", Toast.LENGTH_SHORT).show();

            }
        }
            }
        });

        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getFragmentManager().popBackStack();
            }
        });

        nadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textTargetUri = new TextView(getActivity());
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

