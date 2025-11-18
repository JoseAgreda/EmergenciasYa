package sv.edu.catolica.emergenciasya;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncidenteAdapter extends RecyclerView.Adapter<IncidenteAdapter.ViewHolder> {

    private final List<Incident> incidentes;

    public IncidenteAdapter(List<Incident> incidentes) {
        this.incidentes = incidentes;
    }

    @NonNull
    @Override
    public IncidenteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidente, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidenteAdapter.ViewHolder holder, int position) {
        Incident incident = incidentes.get(position);

        holder.tvTipoEmergencia.setText(incident.getTitulo());

        // Descripción corta
        String desc = incident.getDescripcion();
        if (desc != null && !desc.trim().isEmpty()) {
            if (desc.length() > 60)
                desc = desc.substring(0, 60) + "...";
            holder.tvDescripcionCorta.setText(desc);
            holder.tvDescripcionCorta.setVisibility(View.VISIBLE);
        } else {
            holder.tvDescripcionCorta.setVisibility(View.GONE);
        }

        // Fecha formateada
        String fechaTexto = formatearFecha(incident.getFechaRegistro());
        holder.tvFecha.setText(fechaTexto);

        // Contacto o ubicación
        String contactoTexto;
        if (incident.getUbicacion() != null && !incident.getUbicacion().isEmpty()) {
            contactoTexto = "Ubicación: " + incident.getUbicacion();
        } else if (incident.getDescripcion() != null && !incident.getDescripcion().isEmpty()) {
            contactoTexto = "Descripción: " + incident.getDescripcion();
        } else {
            contactoTexto = "Registrado manualmente";
        }
        holder.tvContacto.setText(contactoTexto);

        // --- Botón eliminar ---
        holder.btnDelete.setOnClickListener(v -> {
            Activity activity = (Activity) v.getContext();

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar incidente")
                    .setMessage("¿Deseas eliminar este incidente?")
                    .setPositiveButton("Sí", (dialog, which) -> {

                        new Thread(() -> {
                            AppDatabase db = AppDatabase.getInstance(v.getContext());
                            db.incidentDao().deleteById(incident.getId());

                            activity.runOnUiThread(() -> {
                                incidentes.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(v.getContext(), "Incidente eliminado", Toast.LENGTH_SHORT).show();
                            });

                        }).start();

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return incidentes != null ? incidentes.size() : 0;
    }

    private String formatearFecha(long millis) {
        if (millis <= 0) return "";
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    // Listener para borrar
    public interface OnDeleteListener {
        void onDelete(Incident incident, int position);
    }

    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.onDeleteListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTipoEmergencia;
        TextView tvDescripcionCorta;
        TextView tvFecha;
        TextView tvContacto;
        ImageView btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoEmergencia = itemView.findViewById(R.id.tv_tipo_emergencia);
            tvDescripcionCorta = itemView.findViewById(R.id.tv_descripcion_corta);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvContacto = itemView.findViewById(R.id.tv_contacto);
            btnDelete = itemView.findViewById(R.id.btn_delete_incidente);
        }
    }
}