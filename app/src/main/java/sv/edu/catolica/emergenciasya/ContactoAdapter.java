package sv.edu.catolica.emergenciasya;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ViewHolder> {

    private final List<Contacto> listaContactos;
    private final Context context;

    public ContactoAdapter(Context context, List<Contacto> listaContactos) {
        this.context = context;
        this.listaContactos = listaContactos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto contacto = listaContactos.get(position);
        holder.nombre.setText(contacto.getNombre());
        holder.numero.setText(contacto.getNumero());

        holder.botonLlamar.setOnClickListener(v -> {
            Intent intentLlamada = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacto.getNumero()));
            // Verificar permiso CALL_PHONE antes de llamar
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intentLlamada);
            } else {
                Toast.makeText(context, "Permiso de llamada denegado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, numero;
        MaterialButton botonLlamar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_contact_name);
            numero = itemView.findViewById(R.id.tv_contact_number);
            botonLlamar = itemView.findViewById(R.id.btn_call_contact);
        }
    }
}
