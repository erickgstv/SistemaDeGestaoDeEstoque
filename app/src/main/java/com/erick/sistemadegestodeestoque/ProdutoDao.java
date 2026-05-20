package com.erick.sistemadegestodeestoque;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;
import java.util.List;
@Dao
public interface ProdutoDao {
    @Insert
    void inserir(Produto produto);

    @Query("SELECT * FROM produto ORDER BY nome ASC")
    List<Produto> buscarTodos();

    @Update
    void atualizar(Produto produto);

    @Delete
    void deletar(Produto produto);

    @Query("DELETE FROM produto")
    void limparTudo();
}
