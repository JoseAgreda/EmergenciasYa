package sv.edu.catolica.emergenciasya;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GuiaPrimerosAuxiliosAdapter extends RecyclerView.Adapter<GuiaPrimerosAuxiliosAdapter.ViewHolder> {

    private final List<GuiaItem> listaOriginal;
    private final List<GuiaItem> listaFiltrada;

    public GuiaPrimerosAuxiliosAdapter(List<GuiaItem> pasos) {
        this.listaOriginal = new ArrayList<>(pasos);
        this.listaFiltrada = new ArrayList<>(pasos);
    }

    @NonNull
    @Override
    public GuiaPrimerosAuxiliosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guia_auxilios, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull GuiaPrimerosAuxiliosAdapter.ViewHolder holder, int position) {
        GuiaItem item = listaFiltrada.get(position);
        holder.tvTitulo.setText(item.getTitulo());
        holder.tvDescripcion.setText(item.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return listaFiltrada != null ? listaFiltrada.size() : 0;
    }

    public void filtrar(String texto) {
        listaFiltrada.clear();

        if (texto == null || texto.trim().isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            String query = texto.toLowerCase();
            for (GuiaItem item : listaOriginal) {
                if (item.getTitulo().toLowerCase().contains(query)
                        || item.getDescripcion().toLowerCase().contains(query)) {
                    listaFiltrada.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        TextView tvDescripcion;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tv_titulo_situacion);
            tvDescripcion = itemView.findViewById(R.id.tv_desc_situacion);
        }
    }
}
