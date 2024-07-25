package RecyclerViewHelper

import Model.Connection
import Model.DataClassPacientes
import alejando.murcia.jesus.arce.medival.ActualizarPacientesActivity
import alejando.murcia.jesus.arce.medival.R.layout.activity_item_card
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PacientesAdapter(private var PacientData: List<DataClassPacientes>) : RecyclerView.Adapter<PacientesViewHolder>() {

    fun RecargarVista(newDataList: List<DataClassPacientes>){
        PacientData = newDataList
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesDeActualizarDatos(idPacientes: Int, nuevoNombre: String){
        val index = PacientData.indexOfFirst { it.idPacientes == idPacientes }
        PacientData[index].nombres= nuevoNombre
        notifyItemChanged(index)
    }

    fun eliminarRegistro(ID_Pacientes:Int,position: Int){

        try{
            //quitar el elementpo de la lista
            val listaDatos = PacientData.toMutableList()
            listaDatos.removeAt(position)

            //quitar de la base de datos
            GlobalScope.launch(Dispatchers.IO) {

                //crear un objeto e la clase conexion
                val objConexion=Connection().Connect()
                val deletePaciente = objConexion?.prepareStatement("DELETE TB_Pacientes WHERE ID_Paciente = ?")!!
                deletePaciente.setInt( 1,ID_Pacientes)
                deletePaciente.executeUpdate()

                val commit = objConexion.prepareStatement( "commit")!!
                commit.executeUpdate()
            }
            PacientData=listaDatos.toList()
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
        catch (ex:Exception){
            println("este es el error que te explota la app loco $ex.message")
        }



    }

    fun AcualizarPaciente(ID_Paciente: Int, Nombres: String, Apellidos: String, Edad: Int, NumHabitacion: Int, NumCama: Int){

        //1- CREO UNA CORRUTINA
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de tipo conexion
            val objConexion = Connection().Connect()

            //2- Creo una variable un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update TB_Pacientes set Nombres = ? Apellidos = ? Edad = ? Num_Habitación = ? Num_Cama = ? where ID_Paciente =?")!!
            updateProducto.setString(1, Nombres)
            updateProducto.setString(2, Apellidos)
            updateProducto.setInt(3, Edad)
            updateProducto.setInt(4, NumHabitacion)
            updateProducto.setInt(5, NumCama)
            updateProducto.setInt(6, ID_Paciente)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarListaDespuesDeActualizarDatos(ID_Paciente, Nombres)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacientesViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(activity_item_card, parent, false)
        return PacientesViewHolder(vista)
    }

    override fun getItemCount() = PacientData.size

    override fun onBindViewHolder(holder: PacientesViewHolder, position: Int) {
        val pacientes = PacientData[position]
        holder.textView.text = pacientes.nombres

        val item =PacientData[position]


        holder.imgBorrar.setOnClickListener {
            //craeamos una alaerta

            //invocamos  el contexto
            try{
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

                Toast.makeText(context, "Paciente eliminado satisfactoriamente", Toast.LENGTH_SHORT).show()
            }catch (ex:Exception){
                println("este es el error que te explota la app loco $ex.message")
            }


        }

        holder.imgEditar.setOnClickListener {

            val context = holder.itemView.context

            val pacientesActivity = Intent(context, ActualizarPacientesActivity::class.java)
            pacientesActivity.putExtra("ID_Paciente", item.idPacientes)
            pacientesActivity.putExtra("Nombres", item.nombres)
            pacientesActivity.putExtra("Apellidos", item.apellidos)
            pacientesActivity.putExtra("Edad", item.edad)
            pacientesActivity.putExtra("NumCama", item.numCama)
            pacientesActivity.putExtra("NumHabitacion", item.numHabitacion)


            context.startActivity(pacientesActivity)
        }

        //darle click a la card
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context

            //Cambiamos de pantalla
            //Abro la pantalla de productos
            val ActualizarPacientesActivity = Intent(context, ActualizarPacientesActivity::class.java)

            //Abriremos la pantalla
            //Pero antes mandamos los parametros

            ActualizarPacientesActivity.putExtra("ID_Paciente", item.idPacientes )
            ActualizarPacientesActivity.putExtra("Nombres", item.nombres)
            ActualizarPacientesActivity.putExtra("Apellidos", item.apellidos)
            ActualizarPacientesActivity.putExtra("Edad", item.edad)
            ActualizarPacientesActivity.putExtra("NumCama", item.numCama)
            ActualizarPacientesActivity.putExtra("Num_Habitación", item.numHabitacion)

            //Inicializamos la actividad

            context.startActivity(ActualizarPacientesActivity)
        }

    }

}