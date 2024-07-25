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

        // Elementos de la vista
        val btnAgregarPaciente = findViewById<Button>(R.id.btnAgregarPaciente)
        val rcvPacientes = findViewById<RecyclerView>(R.id.rcvPacientes)

        // Asignar un layout al RecyclerView
        rcvPacientes.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador con una lista vacía
        var pacientsAdapter = PacientesAdapter(emptyList())
        rcvPacientes.adapter = pacientsAdapter

        // Funcion para obtener datos
        fun getPacientes(): List<DataClassPacientes> {
            val pacientes = mutableListOf<DataClassPacientes>()
            try {
                val objConnection = Connection().Connect()

                // Verifica que la conexión no sea nula
                val statement = objConnection?.createStatement() ?: throw NullPointerException("La conexión a la base de datos es nula.")
                val resultSet = statement.executeQuery("SELECT * FROM TB_Pacientes")

                while (resultSet.next()) {
                    val idPaciente = resultSet.getInt("ID_Paciente")
                    val nombres = resultSet.getString("Nombres")
                    val apellidos = resultSet.getString("Apellidos")
                    val edad = resultSet.getInt("Edad")
                    val numCama = resultSet.getInt("Num_Cama")
                    val numHabitacion = resultSet.getInt("Num_Habitación")

                    val fullDataPaciente = DataClassPacientes(idPaciente, nombres, apellidos, edad, numHabitacion, numCama)
                    pacientes.add(fullDataPaciente)
                }

                resultSet.close()
                statement.close()
                objConnection.close()

            } catch (e: Exception) {
                e.printStackTrace()
                // Manejar la excepción adecuadamente
            }

            return pacientes
        }

        // Obtener datos y actualizar el adaptador
        CoroutineScope(Dispatchers.IO).launch {
            val pacientes = getPacientes()
            withContext(Dispatchers.Main) {
                pacientsAdapter = PacientesAdapter(pacientes)
            }
        }
    }
}
