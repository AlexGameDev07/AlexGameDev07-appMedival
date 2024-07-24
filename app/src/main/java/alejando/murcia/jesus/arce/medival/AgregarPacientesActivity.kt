package alejando.murcia.jesus.arce.medival

import Model.Connection
import android.os.Bundle
import android.provider.ContactsContract.AggregationExceptions
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

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