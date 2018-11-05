package ba.unsa.etf.rma.asim.spirala1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.knjige;

public class DodavanjeKnjigeAkt extends AppCompatActivity {

    ImageView targetImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);

        targetImage = (ImageView) findViewById(R.id.naslovnaStr);
        final EditText autor = (EditText) findViewById(R.id.imeAutora);
        final EditText naziv = (EditText) findViewById(R.id.nazivKnjige);
        final Button nadjiSliku = (Button) findViewById(R.id.dNadjiSliku);
        final Button upisiKnjigu = (Button) findViewById(R.id.dUpisiKnjigu);
        final Spinner kateSpiner = (Spinner) findViewById(R.id.sKategorijaKnjige);
        final Button ponisti = (Button) findViewById(R.id.dPonisti);

        Intent intent = getIntent();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, kategorije);
        kateSpiner.setAdapter(adapter);

        upisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autor.getText().toString().equals("") || naziv.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Popunite prazna mjesta", Toast.LENGTH_SHORT).show();
                else {
                    String tekst = "Knjiga " + naziv.getText().toString() + " dodana u kategoriju "+kateSpiner.getSelectedItem().toString();
                    //od imageviwa pravimo bitmapu za konstruktor
                    targetImage.setDrawingCacheEnabled(true);
                    targetImage.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(targetImage.getDrawingCache());

                    knjige.add(new Knjiga(autor.getText().toString(), naziv.getText().toString(), kateSpiner.getSelectedItem().toString(), bitmap));

                    Toast.makeText(getApplicationContext(), tekst, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DodavanjeKnjigeAkt.this, DodavanjeKnjigeAkt.class);

                    startActivity(intent);
                }
            }
        });

        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DodavanjeKnjigeAkt.this, KategorijeAkt.class);
                startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView textTargetUri = new TextView(this);
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}