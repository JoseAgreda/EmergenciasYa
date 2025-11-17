package sv.edu.catolica.emergenciasya;

import android.app.AlertDialog; // ¡Importante! Añade esta línea
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GuiaAdapter extends RecyclerView.Adapter<GuiaAdapter.ViewHolder> {

    private final List<Guia> listaDeGuias;
    private final Context context;

    public GuiaAdapter(Context context, List<Guia> listaDeGuias) {
        this.context = context;
        this.listaDeGuias = listaDeGuias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_guia_auxilios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Guia guiaActual = listaDeGuias.get(position);

        // Asigna los datos a los componentes de la tarjeta
        holder.icono.setImageResource(guiaActual.getIconoResId());
        holder.titulo.setText(guiaActual.getTitulo());
        holder.descripcion.setText(guiaActual.getDescCorta()); // Descripción corta en la tarjeta

        // === CAMBIO PRINCIPAL: ACCIÓN AL HACER CLIC ===
        holder.itemView.setOnClickListener(v -> {
            // Crea y muestra un cuadro de diálogo (AlertDialog)
            new AlertDialog.Builder(context)
                    .setTitle(guiaActual.getTitulo()) // Título de la alerta
                    .setMessage(guiaActual.getDescLarga()) // Mensaje con los pasos detallados
                    .setPositiveButton("Entendido", (dialog, which) -> {
                        // Acción al presionar el botón (opcional).
                        // Simplemente cierra el diálogo.
                        dialog.dismiss();
                    })
                    .show(); // Muestra la alerta
        });
    }

    @Override
    public int getItemCount() {
        return listaDeGuias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icono;
        TextView titulo;
        TextView descripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icono = itemView.findViewById(R.id.icon_situacion);
            titulo = itemView.findViewById(R.id.tv_titulo_situacion);
            descripcion = itemView.findViewById(R.id.tv_desc_situacion);
        }
    }
}
