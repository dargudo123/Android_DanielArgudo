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

    public void Listar(View vista) {
        Intent listar = new Intent(this, Listado.class);
        startActivity(listar);
    }

    public void Guardar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.COLUMN2, txtapellido.getText().toString());

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

        Toast.makeText(getApplicationContext(),
                "Se guardó el registro con clave: " + newRowId,
                Toast.LENGTH_LONG).show();

        db.close();
    }

    public void Buscar(View vista) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN1,
                FeedReaderContract.FeedEntry.COLUMN2
        };

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        String sortOrder = FeedReaderContract.FeedEntry.COLUMN2 + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN1));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN2));

            txtnombre.setText(nombre);
            txtapellido.setText(apellido);
        } else {
            Toast.makeText(this, "No se encontró el registro", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    public void Eliminar(View vista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        Toast.makeText(getApplicationContext(),
                "Se eliminó " + deletedRows + " registro(s)",
                Toast.LENGTH_LONG).show();
    }
}

public void Actualizar(View vista)
{
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    String nombre = txtnombre.getText().toString();
    String apellido = txtapellido.getText().toString();
    ContentValues values = new ContentValues();
    values.put(FeedReaderContract.FeedEntry.column1, nombre);
    values.put(FeedReaderContract.FeedEntry.column2, apellido);

    String selection = FeedReaderContract.FeedEntry._ID + "= ?";
    String[] selectionArgs = { txtid.getText().toString() };

    int count = db.update(FeedReaderContract.FeedEntry.nameTable,
            values,
            selection,
            selectionArgs);
    Toast.makeText(getApplicationContext(), "se actualizó"+
            count+"registro(s)",Toast.LENGTH_LONG).show();
    db.close();

}
