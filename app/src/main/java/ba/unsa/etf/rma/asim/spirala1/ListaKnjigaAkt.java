package ba.unsa.etf.rma.asim.spirala1;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.asim.spirala1.KategorijeAkt.knjige;

public class ListaKnjigaAkt extends AppCompatActivity {


    private ListView listaKnjiga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);

        final Button povratak = (Button) findViewById(R.id.dPovratak);
        listaKnjiga = (ListView) findViewById(R.id.listaKnjiga);

        final ArrayList<Knjiga> ispis = new ArrayList<Knjiga>();//pomocna lista u kojoj su elementi koje treba ispisat
        final KnjigaAdapter adapter;
        adapter = new KnjigaAdapter(this, R.layout.element_liste_knjige, ispis);

        Intent intent = getIntent();
        final String uslovKategorija = intent.getStringExtra("SelektovanaKategorija");

        for (Knjiga k : knjige) {
            if (k.getImeKategorije().equals(uslovKategorija))
                ispis.add(k);
        }

        listaKnjiga.setAdapter(adapter);
        listaKnjiga.setClickable(true);

        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItem(position).isSelektovana())
                    Toast.makeText(ListaKnjigaAkt.this, "Knjiga odselektovana", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ListaKnjigaAkt.this, "Knjiga selektovana", Toast.LENGTH_LONG).show();
                adapter.getItem(position).setSelektovana(!adapter.getItem(position).isSelektovana());
                adapter.notifyDataSetChanged();
            }
        });

        povratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaKnjigaAkt.this, KategorijeAkt.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
