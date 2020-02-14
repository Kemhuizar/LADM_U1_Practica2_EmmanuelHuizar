package mx.edu.ittepic.ladm_u1_practica2_emmanuelhuizar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioButton.setOnClickListener {
            editText3.setText("")
        }

        radioButton2.setOnClickListener {
            editText3.setText("")
        }
        //oi
        button.setOnClickListener {
            if(editText3.text.isEmpty()!=true) {
                if (radioButton.isChecked) {

                    guardarArchivoInterno()
                } else {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
                    } else {
                        guardarArchivoSD()

                    }
                }
            }else{
                AlertDialog.Builder(this).setTitle("Atencion").setMessage("Falta poner el nombre del archivo").setPositiveButton("Ok"){ d, i->}.show()
            }

        }
        button2.setOnClickListener {
            if(editText3.text.isEmpty()!=true) {
                if (radioButton.isChecked) {
                    leerArchivoInterno()
                } else {
                    leerArchivoSD()
                }
            }else{
                AlertDialog.Builder(this).setTitle("Atencion").setMessage("Falta poner el nombre del archivo").setPositiveButton("Ok"){ d, i->
                    ponerTexto("","")
                    editText3.requestFocus()
                }.show()
            }
        }
        button3.setOnClickListener {
            finish()
        }
    }

    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("No hay memoria")
            return
        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivos = File(rutaSD.absolutePath,editText3.text.toString()+".txt")

            var flujoSalida= OutputStreamWriter(FileOutputStream(datosArchivos))
            var data=editText2.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("El archivo se guardo correctamente")
            ponerTexto("","")
            editText2.requestFocus()

        }catch(error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoSD(){
        if(noSD()){
            mensaje("No hay memoria")
            return
        }

        try{
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editText3.text.toString()+".txt")

            var flujoEntrada= BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data=flujoEntrada.readLine()

            ponerTexto(data,editText3.text.toString())
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }


    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()

        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false

    }

    fun guardarArchivoInterno(){
        try{
            var flujoSalida= OutputStreamWriter(openFileOutput(editText3.text.toString()+".txt", Context.MODE_PRIVATE))
            var data=editText2.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("Exito! el archivo se guardo correctamente")

            ponerTexto("","")
            editText2.requestFocus()

        }catch(error: IOException){
            mensaje(error.message.toString())
        }
    }

    private fun leerArchivoInterno(){
        try{
            var flujoEntrada= BufferedReader(InputStreamReader(openFileInput(editText3.text.toString()+".txt")))


            var data=flujoEntrada.readLine()

            ponerTexto(data,editText3.text.toString())
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(m:String){
        AlertDialog.Builder(this).setTitle("Atencion").setMessage(m).setPositiveButton("Ok"){ d, i->}.show()
    }

    fun ponerTexto(t1:String,t2:String){
        editText2.setText(t1)
        editText3.setText(t2)
    }

}
