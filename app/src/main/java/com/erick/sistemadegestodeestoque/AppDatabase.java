package com.erick.sistemadegestodeestoque;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {Produto.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProdutoDao produtoDao();
}
