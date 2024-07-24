package RecyclerViewHelper

import Model.Connection
import Model.DataClassPacientes
import alejando.murcia.jesus.arce.medival.PacientesActivity
import alejando.murcia.jesus.arce.medival.R
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PacientesAdapter(private var Data: List<DataClassPacientes>) : RecyclerView.Adapter<RecyclerViewHelper.PacientesViewHolder>() {

    fun RecargarVista(newDataList: List<DataClassPacientes>){
        Data = newDataList
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesDeActualizarDatos(idPacientes: Int, nuevoNombre: String){
        val index = Data.indexOfFirst { it.idPacientes == idPacientes }
        Data[index].nombres= nuevoNombre
        notifyItemChanged(index)
    }

    fun eliminarRegistro(ID_Pacientes:Int,position: Int){

        //quitar el elementpo de la lista
        val listaDatos = Data.toMutableList()
        listaDatos.removeAt(position)

        //quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO) {

            //crear un objeto e la clase conexion
            val objConexion=Connection().Connect()

            val deletePaciente = objConexion?.prepareStatement("DELETE TB_Pacientes WHERE idPaciente = ?")!!
            deletePaciente.setInt( 1,ID_Pacientes)
            deletePaciente.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Data=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()


    }

    fun actualizarProducto(Nombres: String, Apellidos: String, Edad: Int, NumCama: Int, ID_Paciente: Int){

        //1- CREO UNA CORRUTINA
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de tipo conexion
            val objConexion = Connection().Connect()

            //2- Creo una variable un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update TB_Pacientes set Nombres = ? Apellidos = ? Edad = ? Num_Cama = ? where uuid =?")!!
            updateProducto.setString(1, Nombres)
            updateProducto.setString(2, Apellidos)
            updateProducto.setInt(3, Edad)
            updateProducto.setInt(4, NumCama)
            updateProducto.setInt(5, ID_Paciente)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarListaDespuesDeActualizarDatos(ID_Paciente, Nombres)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHelper.PacientesViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return PacientesViewHolder(vista)
    }

    override fun getItemCount() = Data.size

    override fun onBindViewHolder(holder: PacientesViewHolder, position: Int) {
        val pacientes = Data[position]
        holder.textView.text = pacientes.nombres

        val item =Data[position]


        holder.imgBorrar.setOnClickListener {
            //craeamos una alaerta

            //invocamos  el contexto
            val context = holder.itemView.context

            //CREO LA ALERTA

            val builder = AlertDialog.Builder(context)

            //le ponemos titulo a la alerta

            builder.setTitle("¿estas seguro?")

            //ponerle mendsaje a la alerta

            builder.setMessage("Deseas en verdad eliminar el registro")

            //agrgamos los botones

            builder.setPositiveButton("si"){dialog,wich ->
                eliminarRegistro(item.idPacientes,position)
            }

            builder.setNegativeButton("no"){dialog,wich ->

            }

            //cramos la alerta
            val alertDialog=builder.create()

            //mostramos la alerta

            alertDialog.show()

        }

        holder.imgEditar.setOnClickListener {

            val context = holder.itemView.context

            val pacientesActivity = Intent(context, PacientesActivity::class.java)
            pacientesActivity.putExtra("ID_Paciente", item.idPacientes)
            pacientesActivity.putExtra("Nombres", item.nombres)
            pacientesActivity.putExtra("Apellidos", item.apellidos)
            pacientesActivity.putExtra("Edad", item.edad)
            pacientesActivity.putExtra("NumCama", item.numCama)
            pacientesActivity.putExtra("NumHabitacion", item.numHabitacion)
            pacientesActivity.putExtra("Enfermedades", item.enfermedades)
            pacientesActivity.putExtra("Medicamentos", item.medicamentos)

            context.startActivity(pacientesActivity)
        }

        //darle click a la card
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context

            //Cambiamos de pantalla
            //Abro la pantalla de productos
            val pantallaDetalles = Intent(context, PacientesActivity::class.java)

            //Abriremos la pantalla
            //Pero antes mandamos los parametros

            pantallaDetalles.putExtra("ID_Paciente", item.idPacientes )
            pantallaDetalles.putExtra("Nombres", item.nombres)
            pantallaDetalles.putExtra("Apellidos", item.apellidos)
            pantallaDetalles.putExtra("Edad", item.edad)
            pantallaDetalles.putExtra("NumCama", item.numCama)

            //Inicializamos la actividad

            context.startActivity(pantallaDetalles)
        }

    }

}