package com.erick.sistemadegestodeestoque;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder> {

    private List<Produto> produtos;
    private List<Produto> produtosFiltrados;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public interface OnItemClickListener {
        void onItemClick(Produto produto);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Produto produto);
    }

    public ProdutoAdapter(List<Produto> produtos, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.produtos = produtos;
        this.produtosFiltrados = new ArrayList<>(produtos);
        this.listener = listener;
        this.longListener = longListener;
    }

    public void filtrar(String texto) {
        produtosFiltrados.clear();
        if (texto.isEmpty()) {
            produtosFiltrados.addAll(produtos);
        } else {
            String query = texto.toLowerCase().trim();
            for (Produto p : produtos) {
                if (p.getNome().toLowerCase().contains(query)) {
                    produtosFiltrados.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);
        return new ProdutoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Produto produto = produtosFiltrados.get(position);
        holder.textViewNome.setText(produto.getNome());
        holder.textViewPreco.setText(String.format("R$ %.2f", produto.getPreco()));
        
        String unidade = produto.getUnidadeMedida() != null ? produto.getUnidadeMedida() : "UN";
        holder.textViewQuantidade.setText("Estoque: " + produto.getQuantidade() + " " + unidade);

        if (produto.getQuantidade() < 5) {
            holder.textViewQuantidade.setTextColor(Color.RED);
        } else {
            holder.textViewQuantidade.setTextColor(Color.parseColor("#757575"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(produto);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longListener != null) longListener.onItemLongClick(produto);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return produtosFiltrados.size();
    }

    public static class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNome, textViewPreco, textViewQuantidade;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewNomeProduto);
            textViewPreco = itemView.findViewById(R.id.textViewPrecoProduto);
            textViewQuantidade = itemView.findViewById(R.id.textViewQuantidadeProduto);
        }
    }
}
