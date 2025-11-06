package com.example.actividad1_lenguajes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;

    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new FeedReaderDbHelper(this);

        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);
    }

    public void listar(View vista) {
        Intent listar = new Intent(this, Listado.class);
        startActivity(listar);
    }

    public void guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());

        long newRowId = db.insert(FeedReaderContract.FeedEntry.nametable, null, values);

        Toast.makeText(getApplicationContext(),
                "se guardó el registro con clave: " + newRowId,
                Toast.LENGTH_LONG).show();

        db.close();
    }

    public void buscar(View vista) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        String sortOrder = FeedReaderContract.FeedEntry.column2 + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nametable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));

            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
        } else {
            Toast.makeText(this, "no se encontró el registro", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    public void eliminar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        int deletedRows = db.delete(FeedReaderContract.FeedEntry.nametable, selection, selectionArgs);
        db.close();

        Toast.makeText(getApplicationContext(),
                "se eliminó " + deletedRows + " registro(s)",
                Toast.LENGTH_LONG).show();
    }

    public void actualizar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, nombre);
        values.put(FeedReaderContract.FeedEntry.column2, apellido);

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        int count = db.update(
                FeedReaderContract.FeedEntry.nametable,
                values,
                selection,
                selectionArgs
        );

        Toast.makeText(getApplicationContext(),
                "se actualizó " + count + " registro(s)",
                Toast.LENGTH_LONG).show();

        db.close();
    }
}

