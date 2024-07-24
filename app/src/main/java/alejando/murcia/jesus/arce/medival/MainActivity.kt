package alejando.murcia.jesus.arce.medival

import Model.Connection
import Model.DataClassPacientes
import RecyclerViewHelper.PacientesAdapter
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Elementos de la vista
        val btnAgregarPaciente = findViewById<Button>(R.id.btnAgregarPaciente)
        val rcvPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)

        /***** -Mostrar datos- ********************************************************************/

        //Asignar un layuotu al rcv
        rcvPacientes.layoutManager = LinearLayoutManager(this)

        //Funcion para obtener datos
        fun GetPacientes(): List<DataClassPacientes> {
            val objConnection = Connection().Connect()
            val statement = objConnection?.createStatement()
            val resultset = statement?.executeQuery("SELECT * FROM TB_Pacientes")!!

            val pacientes = mutableListOf<DataClassPacientes>()
            while (resultset.next()) {
                val ID_Paciente = resultset.getInt("ID_Pacientes")
                val Nombres = resultset.getString("Nombres")
                val Apellidos = resultset.getString("Apellidos")
                val Edad = resultset.getInt("Edad")
                val NumCama = resultset.getInt("Num_Cama")
                val NumHabitacion = resultset.getInt("Num_Habitacion")
                val FullDataPacient = DataClassPacientes(ID_Paciente, Nombres, Apellidos, Edad, NumCama, NumHabitacion)
                pacientes.add(FullDataPacient)

            }
            return pacientes
        }

        //Asignar un adaptador
        CoroutineScope(Dispatchers.IO).launch{
            val Pacientes = GetPacientes()
            withContext(Dispatchers.Main){
                val PacientsAdapter = PacientesAdapter(Pacientes)
                rcvPacientes.adapter = PacientsAdapter
            }
        }


    }
}