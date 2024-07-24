package alejando.murcia.jesus.arce.medival

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityDetallesPacientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Receptor de valores
        val bundle = intent.extras
        val Nombre = bundle?.getString("Nombres")
        val Apellidos = bundle?.getString("Apellidos")
        val Edad = bundle?.getInt("Edad")
        val NumCama = bundle?.getInt("NumCama")
        val NumHabitacion = bundle?.getInt("NumHabitacion")
        val Enfermedades = bundle?.getString("Enfermedades")
        val Medicamentos = bundle?.getString("Medicamentos")
        val ID_Paciente = bundle?.getInt("ID_Paciente")

        //Elementos de la vista
        

    }
}