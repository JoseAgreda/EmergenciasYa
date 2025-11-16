package sv.edu.catolica.emergenciasya;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrimerosAuxiliosAdapter extends RecyclerView.Adapter<PrimerosAuxiliosAdapter.ViewHolder> {
    public static final String EXTRA_TEMA = "extra_tema";


    private final List<PrimerosAuxilios> temas;
    private final Context context;

    public PrimerosAuxiliosAdapter(Context context, List<PrimerosAuxilios> temas) {
        this.context = context;
        this.temas = temas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_primeros_auxilios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrimerosAuxilios tema = temas.get(position);

        holder.tituloTextView.setText(tema.getTitulo());
        holder.descripcionTextView.setText(tema.getDescripcion());

        // Al tocar la tarjeta, mostramos la info en un cuadro de diálogo
        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(tema.getTitulo())
                    .setMessage(tema.getDescripcion())
                    .setPositiveButton("Cerrar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return temas != null ? temas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tituloTextView;
        TextView descripcionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
        }
    }
}
