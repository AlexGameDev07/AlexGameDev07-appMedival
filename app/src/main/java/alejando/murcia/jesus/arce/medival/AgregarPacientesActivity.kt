package alejando.murcia.jesus.arce.medival

import Model.Connection
import Model.DataClassEnfermedades
import Model.DataClassMedicamentos
import android.os.Bundle
import android.provider.ContactsContract.AggregationExceptions
import android.provider.Settings.Global
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarPacientesActivity : AppCompatActivity() {

    fun AgregarPaciente(txtNombres: EditText, txtApellidos: EditText, txtEdad: EditText, txtNumHaiación: EditText, txtNumCama: EditText) {
        val connection = Connection().Connect()
        val sqlTrig = "{call PROC_INST_Pacientes(?,?,?,?,?)}"
        val executeTrigger = connection?.prepareStatement(sqlTrig)
        executeTrigger?.setString(1, txtNombres.text.toString())
        executeTrigger?.setString(2, txtApellidos.text.toString())
        executeTrigger?.setInt(3, txtEdad.text.toString().toInt())
        executeTrigger?.setInt(4, txtNumHaiación.text.toString().toInt())
        executeTrigger?.setInt(5, txtNumCama.text.toString().toInt())
        executeTrigger?.execute()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //elemenos de la vista
        val txtNombres = findViewById<EditText>(R.id.txtNombres)
        val txtApellidos = findViewById<EditText>(R.id.txtApellidos)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val txtNumHaiación = findViewById<EditText>(R.id.txtNumHaiación)
        val txtNumCama = findViewById<EditText>(R.id.txtNumCama)

        val btnInsertarPaciente = findViewById<Button>(R.id.btnInsertarPaciente)
        val btnAgregarMedicamento = findViewById<Button>(R.id.btnAgregarMedicamento)
        val btnAgregarEnfermedad = findViewById<Button>(R.id.btnAgregarEnfermedad)

        val rcvMedicamentos = findViewById<RecyclerView>(R.id.rcvMedicamentos)
        val rcvEnfermedades = findViewById<RecyclerView>(R.id.rcvEnfermedades)

        val spnMedicamentos = findViewById<Spinner>(R.id.spnMedicamentos)
        val spnEnfermedad = findViewById<Spinner>(R.id.spnEnfermedad)

        val btnRegresar = findViewById<ImageView>(R.id.btnRegresar)

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
                val myAdapter = ArrayAdapter(this@AgregarPacientesActivity, android.R.layout.simple_spinner_dropdown_item, Medicamento)
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
                val myAdapter = ArrayAdapter(this@AgregarPacientesActivity, android.R.layout.simple_spinner_dropdown_item, Enfermedad)
                spnMedicamentos.adapter = myAdapter
            }
        }

        //eventos de los botones
        btnInsertarPaciente.setOnClickListener {

            AgregarPaciente(txtNombres, txtApellidos, txtEdad, txtNumHaiación, txtNumCama)
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