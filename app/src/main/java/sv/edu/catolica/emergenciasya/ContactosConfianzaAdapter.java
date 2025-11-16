package sv.edu.catolica.emergenciasya;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosConfianzaAdapter extends RecyclerView.Adapter<ContactosConfianzaAdapter.ViewHolder> {

    private List<ContactoConfianza> contactos;

    public ContactosConfianzaAdapter(List<ContactoConfianza> contactos) {
        this.contactos = contactos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contacto_confianza, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactoConfianza contacto = contactos.get(position);

        holder.nombreTextView.setText(contacto.getNombre());
        holder.numeroTextView.setText(contacto.getNumero());

        // ===== Botón LLAMAR =====
        holder.llamarButton.setOnClickListener(v -> {
            Context ctx = v.getContext();
            String numero = contacto.getNumero();

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + numero));
            ctx.startActivity(intent);
        });

        // ===== Botón SMS =====
        holder.smsButton.setOnClickListener(v -> {
            Context ctx = v.getContext();
            String numero = contacto.getNumero();

            // Abrir app de SMS con número y mensaje prellenado
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:" + numero));
            smsIntent.putExtra("sms_body",
                    "Esta es una alerta desde EmergenciasYa!, necesito ayuda.");

            ctx.startActivity(smsIntent);
        });

        // ===== Botón ELIMINAR =====
        holder.eliminarButton.setOnClickListener(v -> {
            contactos.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contactos.size());
        });
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView numeroTextView;
        ImageButton llamarButton;
        ImageButton smsButton;
        ImageButton eliminarButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            numeroTextView = itemView.findViewById(R.id.numeroTextView);
            llamarButton = itemView.findViewById(R.id.llamarButton);
            smsButton = itemView.findViewById(R.id.smsButton);
            eliminarButton = itemView.findViewById(R.id.eliminarButton);
        }
    }
}
