package RecyclerViewHelper

import alejando.murcia.jesus.arce.medival.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PacientesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textView: TextView = view.findViewById(R.id.txt_NombrePacienteCard)
    val imgEditar: ImageView = view.findViewById(R.id.img_editar)
    val imgBorrar: ImageView = view.findViewById(R.id.img_borrar)

}