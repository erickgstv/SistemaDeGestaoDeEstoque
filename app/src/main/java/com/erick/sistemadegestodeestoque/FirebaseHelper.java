package com.erick.sistemadegestodeestoque;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static String storeId = "loja_padrao";

    public static void init(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Config", Context.MODE_PRIVATE);
        storeId = pref.getString("storeId", "loja_padrao");
    }

    public static void setStoreId(Context context, String newId) {
        storeId = newId;
        SharedPreferences pref = context.getSharedPreferences("Config", Context.MODE_PRIVATE);
        pref.edit().putString("storeId", newId).apply();
    }

    public static String getStoreId() {
        return storeId;
    }

    public static CollectionReference getCollection() {
        return FirebaseFirestore.getInstance()
                .collection("lojas")
                .document(storeId)
                .collection("produtos");
    }
}
