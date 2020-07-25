package com.sunbeltfactory.basededatossqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_codigo, et_descripcion, et_precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_codigo = (EditText)findViewById(R.id.txt_codigo);
        et_descripcion = (EditText)findViewById(R.id.txt_descripcion);
        et_precio = (EditText)findViewById(R.id.txt_precio);
    }

    //Metodo para dar de alta a los productos
    public void Registrar(View view){
        AdminSQliteOpenHelper admin = new AdminSQliteOpenHelper(
                this,
                "administracion",  //name: Es la base de datos
                null,
                1 );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();     // getWritableDatabase sirve para dar permisos en Modo lectura y escritura

        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion",descripcion);
            registro.put("precio", precio);

            BaseDeDatos.insert("articulos", null, registro);

            BaseDeDatos.close();
            et_codigo.setText("");
            et_descripcion.setText("");
            et_precio.setText("");
            Toast.makeText(this, "Registro Completo", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();

        }

    }

    //Metodo para consultar
    public void Buscar(View view){
        AdminSQliteOpenHelper admin = new AdminSQliteOpenHelper(this, "administracion", null, 1);
    SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

    String codigo = et_codigo.getText().toString();
    if(!codigo.isEmpty()){
        Cursor fila = BaseDeDatos.rawQuery
                ("select descripcion, precio from articulos where codigo =" + codigo, null);     //Este metodo nos ayuda a aplicar un selec

        if (fila.moveToFirst()) { //Esto lo que hace es realizar la consulta si contiene valores
            et_descripcion.setText(fila.getString(0));
            et_precio.setText(fila.getString(1));
            BaseDeDatos.close();
        }else{
            Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            BaseDeDatos.close();
        }
    }else{
        Toast.makeText(this, "El codigo no puede ir vacio",Toast.LENGTH_SHORT).show();
    } }

    //Metodo para eliminar un producto
    public void Eliminar(View view){
        AdminSQliteOpenHelper admin = new AdminSQliteOpenHelper(this, "administracion", null,1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_codigo.getText().toString();
        if(!codigo.isEmpty()){

        int cantidad = BaseDeDatos.delete("articulos", "codigo= "+ codigo, null);
        BaseDeDatos.close();
        et_codigo.setText("");
        et_descripcion.setText("");
        et_precio.setText("");
        if(cantidad == 1){
            Toast.makeText(this, "Articulo eliminado exitosamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "El articulo no se pudo eliminar", Toast.LENGTH_SHORT).show();
        }
        }else{
            Toast.makeText(this, "Debes introducir el codigo del articulo", Toast.LENGTH_SHORT).show();
            }
    }

    //Metodo para modificar un articulo
    public void Modificar(View view){
        AdminSQliteOpenHelper admin = new AdminSQliteOpenHelper(this, "administracion", null, 1);
     SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        String codigo = et_codigo.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_precio.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cantidad = BaseDeDatos.update("articulos", registro, "codigo=" + codigo, null );
            BaseDeDatos.close();
            if(cantidad == 1){
                Toast.makeText(this, "Articulo actualizado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "El articulo no existe", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
