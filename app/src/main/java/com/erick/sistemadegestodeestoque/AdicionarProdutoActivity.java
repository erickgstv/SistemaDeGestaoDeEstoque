package com.erick.sistemadegestodeestoque;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AdicionarProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_produto);
        EditText editNome = findViewById(R.id.editTextNome);
        EditText editPreco = findViewById(R.id.editTextPreco);
        EditText editQuantidade = findViewById(R.id.editTextQuantidade);
        android.widget.RadioGroup rgUnidade = findViewById(R.id.radioGroupUnidade);
        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(view -> {
            String nomeStr = editNome.getText().toString();
            String precoStr = editPreco.getText().toString();
            String quantidadeStr = editQuantidade.getText().toString();
            
            String unidadeMedida = "UN";
            if (rgUnidade.getCheckedRadioButtonId() == R.id.radioKG) {
                unidadeMedida = "KG";
            }

            if (!nomeStr.isEmpty() && !precoStr.isEmpty() && !quantidadeStr.isEmpty()) {
                try {
                    double precoConvertido = Double.parseDouble(precoStr);
                    int quantidadeConvertida = Integer.parseInt(quantidadeStr);
                    Produto novoProduto = new Produto(nomeStr, precoConvertido, quantidadeConvertida, unidadeMedida);
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "produto-db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    db.produtoDao().inserir(novoProduto);

                    FirebaseHelper.getCollection()
                            .document(nomeStr)
                            .set(novoProduto)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Produto salvo e sincronizado!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Salvo apenas localmente (sem internet)", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                } catch (Exception e) {
                    Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
