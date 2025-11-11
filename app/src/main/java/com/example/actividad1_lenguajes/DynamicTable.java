package com.example.actividad1_lenguajes;

import android.view.ViewGroup;
import android.widget.TableRow;
import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class DynamicTable {
    private TableLayout tabla;

    private Context contexto;

    private String[] cabecera;

    private ArrayList<String[]> datos;

    private TableRow fila;

    private TextView celda;

    public DynamicTable(TableLayout tabla, Context contexto) {
        this.tabla = tabla;
        this.contexto = contexto;
    }

    public void setCabecera(String[] cabecera) {
        this.cabecera = cabecera;
    }

    public void setDatos(ArrayList<String[]> datos) {
        this.datos = datos;
    }

    public void nuevaFila()
    {
        fila=new TableRow(contexto);
    }

    private void nuevaCelda()
    {
        celda=new TextView(contexto);
        celda.setGravity(Gravity.CENTER);
        celda.setTextSize(14);
    }

    public void crearCabecera()
    {
        nuevaFila();
        for (String titulo:cabecera){
            nuevaCelda();
            celda.setText(titulo);
            celda.setTextSize(24);
            celda.setBackgroundColor(3);
            fila.addView(celda,parametrosCelda());
        }
        tabla.addView(fila);
    }

    public void crearFilas() {
        if (datos == null) return;

        for (String[] filaDatos : datos) {
            nuevaFila();
            for (String textoCelda : filaDatos) {
                nuevaCelda();
                celda.setText(textoCelda);
                celda.setBackgroundColor(0xFFFFFFFF); // blanco
                fila.addView(celda, parametrosCelda());
            }
            tabla.addView(fila);
        }
    }

    private TableRow.LayoutParams parametrosCelda()
    {
        TableRow.LayoutParams parametros= new TableRow.LayoutParams();
        parametros.setMargins(1, 1, 1, 1);
        parametros.weight=1;
        return parametros;
    }

}

