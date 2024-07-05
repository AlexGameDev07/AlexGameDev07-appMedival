package RecyclerViewHelper

import Model.Connection
import Model.DataClassPacientes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adapter (private var Data: List<DataClassPacientes>) : RecyclerView.Adapter<ViewHolder>(){

    fun RecargarVista(newDataList: List<DataClassPacientes>){
        Data = newDataList
        notifyDataSetChanged()
    }

    fun actualizarListaDespuesDeActualizarDatos(id: Int, nuevoNombre: String){
        val index = Data.indexOfFirst { it.idPacientes == id }
        Data[index].nombres = nuevoNombre
        notifyItemChanged(index)
    }

    fun eliminarPaciente(idPaciente: Int,position: Int){

        //quitar el elemento de la lista
        val listaDatos = Data.toMutableList()
        listaDatos.removeAt(position)

        //quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO) {

            //crear un objeto e la clase conexion
            val objConexion=Connection().Connect()

            val statementDelPaciente = objConexion?.prepareStatement("DELETE FROM TB_Pacientes WHERE ID_Paciente = ?")!!
            statementDelPaciente.setInt( 1,idPaciente)
            statementDelPaciente.executeUpdate()

            val commit = objConexion.prepareStatement( "commit")!!
            commit.executeUpdate()
        }
        Data=listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun ActualizarInfoPacientes(nombreProducto: String, uuid: String){

        //1- CREO UNA CORRUTINA
        GlobalScope.launch(Dispatchers.IO){

            //1- Creo un objeto de tipo conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Creo una variable un prepareStatement
            val updateProducto = objConexion?.prepareStatement("update tbProductos set nombreProducto = ? where uuid =?")!!
            updateProducto.setString(1, nombreProducto)
            updateProducto.setString(2, uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarListaDespuesDeActualizarDatos(uuid, nombreProducto)
            }
        }
    }
}