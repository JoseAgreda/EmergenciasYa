package sv.edu.catolica.emergenciasya;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        // Fecha formateada
        String fechaTexto = formatearFecha(incident.getFechaRegistro());
        holder.tvFecha.setText(fechaTexto);

        // Mostramos algo con la ubicación o descripción
        String contactoTexto;
        if (incident.getUbicacion() != null && !incident.getUbicacion().isEmpty()) {
            contactoTexto = "Ubicación: " + incident.getUbicacion();
        } else if (incident.getDescripcion() != null && !incident.getDescripcion().isEmpty()) {
            contactoTexto = "Descripción: " + incident.getDescripcion();
        } else {
            contactoTexto = "Registrado manualmente";
        }
        holder.tvContacto.setText(contactoTexto);
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTipoEmergencia;
        TextView tvFecha;
        TextView tvContacto;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipoEmergencia = itemView.findViewById(R.id.tv_tipo_emergencia);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvContacto = itemView.findViewById(R.id.tv_contacto);
        }
    }
}
