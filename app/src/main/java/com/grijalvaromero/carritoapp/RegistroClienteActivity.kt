package com.grijalvaromero.carritoapp

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.grijalvaromero.carritoapp.configs.Config
import com.grijalvaromero.carritoapp.databinding.ActivityRegistroClienteBinding
import org.json.JSONObject


class RegistroClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_registro_cliente)

        val binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

    binding.buttonCliienteRegistrar.setOnClickListener {
        var STT_cedula = binding.editTextClienteCedula.text.toString()
        var STT_clave= binding.editTextClienteClave.text.toString()
        var STT_bandera:Boolean= false

//metodo nuevo

        if(STT_Campos(binding)) {
            if(STT_Cedula(STT_cedula)){
                if(STT_Clave(STT_clave)){

                    STT_bandera= true
                }else{
                    Toast.makeText(this,"Error minimo 6 Caracteres, " +
                            "Mayuscula, Minuscula,Numero,y  Caracter Especial",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this,"ERROR: Cedula incorrecta",Toast.LENGTH_LONG).show()
            }

        }else{

        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR: Campos Incompletos")
        builder.setMessage("ERROR: LLenar todos los campos")
        builder.setPositiveButton("Aceptar") { dialog, which ->
        }
        builder.show()
    }




        if(STT_bandera){
            var config = Config()
            var url = config.ipServidor+ "Cliente"

            val params = HashMap<String,String>()
            params["cedulaCli"] =  binding.editTextClienteCedula.text.toString()
            params["nombreCli"] = binding.editTextClienteNombre.text.toString()
            params["apellidoCli"] = binding.editTextClienteApellido.text.toString()
            params["direccionCli"] = binding.editTextClienteDireccion.text.toString()
            params["contrasenia"] = binding.editTextClienteClave.text.toString()
            val jsonObject = JSONObject(params as Map<*, *>?)

            // Volley post request with parameters
            val request = JsonObjectRequest(
                Request.Method.POST,url, jsonObject,
                Response.Listener {
                    // Process the json
                    Toast.makeText(applicationContext, "Cliente Insertado con exito", Toast.LENGTH_LONG).show()
                    var inte = Intent(this,LoginActivity::class.java)
                    startActivity(inte)

                }, Response.ErrorListener{
                    // Error in request
                    Toast.makeText(applicationContext, "No se Inserto con exito", Toast.LENGTH_LONG).show()

                })

            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }


    }

    }
    //Valida campos
    private fun STT_Campos(binding: ActivityRegistroClienteBinding): Boolean {

        if (binding.editTextClienteCedula.text.toString().equals("")) return  false
        if (binding.editTextClienteApellido.text.toString().equals("")) return  false
        if (binding.editTextClienteDireccion.text.toString().equals("")) return  false
        if (binding.editTextClienteNombre.text.toString().equals("")) return  false
        if (binding.editTextClienteClave.text.toString().equals("")) return  false

        return true
    }

    //validacion de la Cedula
    private fun STT_Cedula(STT_cedula: String): Boolean {

        var STT_Correcto = false

        try {
            if (STT_cedula.length === 10)
            {
                val STT_tercer = STT_cedula.substring(2, 3).toInt()
                if (STT_tercer < 6) {
                    val STT_ValCedula = intArrayOf(2, 1, 2, 1, 2, 1, 2, 1, 2)
                    val STT_verificar = STT_cedula.substring(9, 10).toInt()
                    var STT_suma = 0
                    var STT_digito = 0
                    for (i in 0 until STT_cedula.length - 1) {
                        STT_digito = STT_cedula.substring(i, i + 1).toInt() * STT_ValCedula[i]
                        STT_suma += STT_digito % 10 + STT_digito / 10
                    }
                    if (STT_suma % 10 == 0 && STT_suma % 10 == STT_verificar) {
                        STT_Correcto = true
                    } else if (10 - STT_suma % 10 == STT_verificar) {
                        STT_Correcto = true
                    } else {
                        STT_Correcto = false
                    }
                } else {
                    STT_Correcto = false
                }
            } else {
                STT_Correcto = false
            }
        } catch (nfe: NumberFormatException) {
            STT_Correcto = false
        } catch (err: Exception) {
            // println("Ocurrio un error en el proceso de validacion")
            STT_Correcto = false
        }
        if (!STT_Correcto) {
            println("La Cédula ingresada es Incorrecta")
        }
        return STT_Correcto
    }
    // Validar la Clave usuario
    private fun STT_Clave( STT_clave: String): Boolean {

        var STT_mayus = false
        var STT_num= false;
        var STT_minus = false;
        var STT_caracter = false;
        var STT_conMayus = 0;
        var STT_conMinus = 0;
        var STT_bandera=false


        if (STT_clave.length>=6 && STT_clave.length <=10){
            for (item in STT_clave)
            {
                Log.i("STT_clave",item.toString())

                if (Character.isDigit(item))   STT_num = true
                if (Character.isUpperCase(item)){
                   STT_conMayus ++;
                    if (STT_conMayus >=2)
                    STT_mayus = true
                }
                if (Character.isLowerCase(item)){
                    STT_conMinus ++;
                    if (STT_conMinus >=2)
                    STT_minus = true
                }
                if(!Character.isLetterOrDigit(item)) STT_caracter = true
            }

        }else{
            STT_bandera=false
        }

        if (STT_num && STT_mayus && STT_minus && STT_caracter)  STT_bandera = true

        return STT_bandera
    }
}

