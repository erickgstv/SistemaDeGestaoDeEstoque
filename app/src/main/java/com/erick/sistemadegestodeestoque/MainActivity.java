package com.erick.sistemadegestodeestoque;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppDatabase db;
    private ProdutoAdapter adapter;
    private SearchView searchView;
    private TextView textViewValorTotal;
    private TextView textViewStoreName;
    private ImageButton btnConfigLoja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseHelper.init(this);

        recyclerView = findViewById(R.id.recyclerViewProdutos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
        textViewValorTotal = findViewById(R.id.textViewValorTotal);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "produto-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        textViewStoreName = findViewById(R.id.textViewStoreName);
        btnConfigLoja = findViewById(R.id.btnConfigLoja);

        textViewStoreName.setText("Loja: " + FirebaseHelper.getStoreId());
        btnConfigLoja.setOnClickListener(v -> mostrarDialogoConfigLoja());

        FloatingActionButton fab = findViewById(R.id.fabAdicionar);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AdicionarProdutoActivity.class);
            startActivity(intent);
        });

        escutarAlteracoesNuvem();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.filtrar(newText);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();
    }

    private void escutarAlteracoesNuvem() {
        FirebaseHelper.getCollection().addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                db.produtoDao().limparTudo();
                for (com.google.firebase.firestore.DocumentSnapshot doc : value.getDocuments()) {
                    Produto p = doc.toObject(Produto.class);
                    if (p != null) {
                        db.produtoDao().inserir(p);
                    }
                }
                atualizarLista();
            }
        });
    }

    private void atualizarLista() {
        List<Produto> produtos = db.produtoDao().buscarTodos();
        adapter = new ProdutoAdapter(produtos, this::mostrarDialogoEdicao, this::confirmarExclusao);
        recyclerView.setAdapter(adapter);
        
        double total = 0;
        for (Produto p : produtos) {
            total += p.getPreco() * p.getQuantidade();
        }
        textViewValorTotal.setText(String.format("R$ %.2f", total));

        if (!searchView.getQuery().toString().isEmpty()) {
            adapter.filtrar(searchView.getQuery().toString());
        }
    }

    private void confirmarExclusao(Produto produto) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Produto")
                .setMessage("Tem certeza que deseja excluir " + produto.getNome() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    db.produtoDao().deletar(produto);
                    FirebaseHelper.getCollection().document(produto.getNome()).delete();
                    atualizarLista();
                    Toast.makeText(this, "Produto excluído", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void mostrarDialogoEdicao(Produto produto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_editar_produto, null);

        EditText etNome = view.findViewById(R.id.editTextNomeEdicao);
        EditText etPreco = view.findViewById(R.id.editTextPrecoEdicao);
        EditText etEstoque = view.findViewById(R.id.editTextEstoqueEdicao);
        android.widget.RadioGroup rgUnidade = view.findViewById(R.id.radioGroupUnidadeEdicao);
        Button btnAumentarPreco = view.findViewById(R.id.btnAumentarPreco);
        Button btnDiminuirPreco = view.findViewById(R.id.btnDiminuirPreco);
        Button btnAumentarEstoque = view.findViewById(R.id.btnAumentarEstoque);
        Button btnDiminuirEstoque = view.findViewById(R.id.btnDiminuirEstoque);
        Button btnSalvar = view.findViewById(R.id.btnSalvarEdicao);

        String nomeOriginal = produto.getNome();
        etNome.setText(produto.getNome());
        etPreco.setText(String.valueOf(produto.getPreco()));
        etEstoque.setText(String.valueOf(produto.getQuantidade()));
        
        if ("KG".equals(produto.getUnidadeMedida())) {
            rgUnidade.check(R.id.radioKGEdicao);
        } else {
            rgUnidade.check(R.id.radioUnidadeEdicao);
        }

        btnAumentarPreco.setOnClickListener(v -> ajustarPreco(etPreco, 1.0));
        btnDiminuirPreco.setOnClickListener(v -> ajustarPreco(etPreco, -1.0));
        btnAumentarEstoque.setOnClickListener(v -> ajustarEstoque(etEstoque, 1));
        btnDiminuirEstoque.setOnClickListener(v -> ajustarEstoque(etEstoque, -1));

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnSalvar.setOnClickListener(v -> {
            try {
                String novoNome = etNome.getText().toString();
                double novoPreco = Double.parseDouble(etPreco.getText().toString());
                int novaQuantidade = Integer.parseInt(etEstoque.getText().toString());
                
                String novaUnidade = "UN";
                if (rgUnidade.getCheckedRadioButtonId() == R.id.radioKGEdicao) {
                    novaUnidade = "KG";
                }

                if (novoNome.isEmpty()) {
                    Toast.makeText(this, "O nome não pode ser vazio", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!novoNome.equals(nomeOriginal)) {
                    FirebaseHelper.getCollection().document(nomeOriginal).delete();
                }

                produto.setNome(novoNome);
                produto.setPreco(novoPreco);
                produto.setQuantidade(novaQuantidade);
                produto.setUnidadeMedida(novaUnidade);
                db.produtoDao().atualizar(produto);
                FirebaseHelper.getCollection().document(produto.getNome()).set(produto);

                atualizarLista();
                dialog.dismiss();
                Toast.makeText(this, "Produto atualizado!", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valores inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void mostrarDialogoConfigLoja() {
        EditText etStoreId = new EditText(this);
        etStoreId.setText(FirebaseHelper.getStoreId());
        new AlertDialog.Builder(this)
                .setTitle("Código da Loja")
                .setMessage("Digite o código para compartilhar este estoque:")
                .setView(etStoreId)
                .setPositiveButton("Salvar", (d, w) -> {
                    String novoId = etStoreId.getText().toString().trim();
                    if (!novoId.isEmpty()) {
                        FirebaseHelper.setStoreId(this, novoId);
                        recreate();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void ajustarPreco(EditText editText, double valor) {
        try {
            double atual = Double.parseDouble(editText.getText().toString());
            double novo = atual + valor;
            if (novo < 0) novo = 0;
            editText.setText(String.valueOf(novo));
        } catch (NumberFormatException e) {
            editText.setText(String.valueOf(Math.max(0, valor)));
        }
    }

    private void ajustarEstoque(EditText editText, int valor) {
        try {
            int atual = Integer.parseInt(editText.getText().toString());
            int novo = atual + valor;
            if (novo < 0) novo = 0;
            editText.setText(String.valueOf(novo));
        } catch (NumberFormatException e) {
            editText.setText(String.valueOf(Math.max(0, valor)));
        }
    }
}
