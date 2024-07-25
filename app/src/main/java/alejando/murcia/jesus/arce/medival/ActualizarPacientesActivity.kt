package alejando.murcia.jesus.arce.medival

import Model.Connection
import Model.DataClassEnfermedades
import Model.DataClassMedicamentos
import android.database.AbstractCursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActualizarPacientesActivity : AppCompatActivity() {

        fun ActualizarPaciente(txtNombres: EditText, txtApellidos: EditText, txtEdad: EditText, txtNumHaiación: EditText, txtNumCama: EditText, ID_Medicamento: Int, ID_Enfermedad: Int, ID_Paciente: Int) {
            val connection = Connection().Connect()

            val ActualizarPaciente = connection?.prepareStatement("UPDATE TB_Pacientes SET Nombres = ?, Apellidos = ?, Edad = ?, Num_Habitación = ?, Num_Cama = ? WHERE ID_Paciente = ?")!!
            ActualizarPaciente?.setString(1, txtNombres.text.toString())
            ActualizarPaciente?.setString(2, txtApellidos.text.toString())
            ActualizarPaciente?.setInt(3, txtEdad.text.toString().toInt())
            ActualizarPaciente?.setInt(4, txtNumHaiación.text.toString().toInt())
            ActualizarPaciente?.setInt(5, txtNumCama.text.toString().toInt() )
            ActualizarPaciente?.setInt(6, ID_Paciente)
            ActualizarPaciente?.executeUpdate()

            val ActualizarReceta = connection?.prepareStatement("UPDATE TB_Recetas SET ID_Medicamento = ? WHERE ID_Paciente = ?")!!
            ActualizarReceta.setInt(1,ID_Medicamento)
            ActualizarReceta.setInt(2,ID_Paciente)
            ActualizarPaciente.executeUpdate()

            val ActualizarExpediente = connection?.prepareStatement("UPDATE TB_Expedientes SET ID_Enfermendad = ? WHERE ID_Paciente = ?")!!
            ActualizarExpediente.setInt(1, ID_Enfermedad)
            ActualizarExpediente.setInt(2, ID_Paciente)
            ActualizarPaciente.executeUpdate()

            val commit = connection?.prepareStatement("commit")!!
            commit.executeUpdate()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //elemenos de la vista
            val txtNombres = findViewById<EditText>(R.id.txtNombresActualizar)
            val txtApellidos = findViewById<EditText>(R.id.txtApellidosActualizar)
            val txtEdad = findViewById<EditText>(R.id.txtEdadActualizar)
            val txtNumHaiación = findViewById<EditText>(R.id.txtNumHaiaciónActualizar)
            val txtNumCama = findViewById<EditText>(R.id.txtNumCamaActualizar)

            val btnActualizarPaciente = findViewById<Button>(R.id.btnActualizarPaciente)
            val btnAgregarMedicamento = findViewById<Button>(R.id.btnAgregarMedicamento)
            val btnAgregarEnfermedad = findViewById<Button>(R.id.btnAgregarEnfermedadActualizarActualizar)

            val rcvMedicamentos = findViewById<RecyclerView>(R.id.rcvMedicamentosActualizar)
            val rcvEnfermedades = findViewById<RecyclerView>(R.id.rcvEnfermedadesActualizar)

            val spnMedicamentos = findViewById<Spinner>(R.id.spnMedicamentosActualizar)
            val spnEnfermedad = findViewById<Spinner>(R.id.spnEnfermedadActualizar)

            val btnRegresar = findViewById<ImageView>(R.id.btnRegresar)

            /////// -Poner datos de los intent- ////////////////////////////////////////////////////////

            val ID_Paciente = intent.getIntExtra("ID_Paciente", 1)
            val Nombre = intent.getStringExtra("Nombres")
            val Apellidos = intent.getStringExtra("Apellidos")
            val Edad = intent.getIntExtra("Edad", 1)
            val NumCama = intent.getIntExtra("NumCama", 1)
            val Num_Habitación = intent.getIntExtra("Num_Habitación", 1)

            txtNombres.setText(Nombre)
            txtApellidos.setText(Apellidos)
            txtEdad.setText(Edad.toString())
            txtNumCama.setText(NumCama.toString())
            txtNumHaiación.setText(Num_Habitación.toString())





            /////// -Programar el FidgetSpinner- ///////////////////////////////////////////////////////

            fun GetMedicamentos(): List<DataClassMedicamentos> {
                val objConnection = Connection().Connect()
                val statement = objConnection?.createStatement()
                val resultset = statement?.executeQuery("SELECT * FROM TB_Medicamentos")
                val listadoMedicamentos = mutableListOf<DataClassMedicamentos>()
                while (resultset?.next() == true) {
                    val ID_Medicamento = resultset.getInt("ID_Medicamento")
                    val medicamento = resultset.getString("Medicamento")
                    val FullDataMedicamento = DataClassMedicamentos(ID_Medicamento, medicamento)
                    listadoMedicamentos.add(FullDataMedicamento)
                }
                resultset?.close()
                statement?.close()
                objConnection?.close()
                return listadoMedicamentos
            }

            fun GetEnfermedades(): List<DataClassEnfermedades> {
                val objConnection = Connection().Connect()
                val statement = objConnection?.createStatement()
                val resultset = statement?.executeQuery("SELECT * FROM TB_Enfermedades")
                val listadoEnfermedades = mutableListOf<DataClassEnfermedades>()
                while (resultset?.next() == true) {
                    val ID_Enfermedad = resultset.getInt("ID_Enfermedad")
                    val Enfermedad = resultset.getString("Enfermedad")
                    val FullDataEnfermedad = DataClassEnfermedades(ID_Enfermedad, Enfermedad)
                    listadoEnfermedades.add(FullDataEnfermedad)
                }
                resultset?.close()
                statement?.close()
                objConnection?.close()
                return listadoEnfermedades
            }

            //Programar el spnMedicamentos
            CoroutineScope(Dispatchers.IO).launch {
                //Obtengo los datos
                val listadoMedicamento = GetMedicamentos()
                val Medicamento =listadoMedicamento.map { it.Medicamento }

                withContext(Dispatchers.Main) {
                    //Crear y modificar adaptador
                    val myAdapter = ArrayAdapter(this@ActualizarPacientesActivity, android.R.layout.simple_spinner_dropdown_item, Medicamento)
                    spnMedicamentos.adapter = myAdapter
                }
            }

            //Programar el spnEnfermedades
            CoroutineScope(Dispatchers.IO).launch {
                //Obtengo los datos
                val listadoEnfermedades = GetEnfermedades()
                val Enfermedad =listadoEnfermedades.map { it.Enfermedad }

                withContext(Dispatchers.Main) {
                    //Crear y modificar adaptador
                    val myAdapter = ArrayAdapter(this@ActualizarPacientesActivity, android.R.layout.simple_spinner_dropdown_item, Enfermedad)
                    spnEnfermedad.adapter = myAdapter
                }
            }

            //eventos de los botones
            btnActualizarPaciente.setOnClickListener {
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        val enfermedad = GetEnfermedades()
                        val medicamento = GetMedicamentos()

                        ActualizarPaciente(txtNombres, txtApellidos, txtEdad, txtNumHaiación, txtNumCama, medicamento[spnMedicamentos.selectedItemPosition].ID_Medicamento, enfermedad[spnEnfermedad.selectedItemPosition].ID_Enfermedad, ID_Paciente)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@ActualizarPacientesActivity, "Paciente Actualizado correctamente", Toast.LENGTH_SHORT).show()
                            finish()

                            
                        }

                    }

                }catch (ex: Exception){
                    println(ex.message)
                }


            }
            btnAgregarMedicamento.setOnClickListener {

            }
            btnAgregarEnfermedad.setOnClickListener {

            }

            btnRegresar.setOnClickListener{
                finish()
            }


        }
    }

