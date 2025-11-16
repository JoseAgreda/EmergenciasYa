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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NumerosEmergenciaAdapter extends RecyclerView.Adapter<NumerosEmergenciaAdapter.ViewHolder> {

    private List<NumeroEmergencia> numeros;
    private Context context;

    public NumerosEmergenciaAdapter(Context context, List<NumeroEmergencia> numeros) {
        this.context = context;
        this.numeros = numeros;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_numero_emergencia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumeroEmergencia numero = numeros.get(position);
        holder.nombreTextView.setText(numero.getNombre());
        holder.numeroTextView.setText(numero.getNumero());
        holder.llamarButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + numero.getNumero()));
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return numeros.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView numeroTextView;
        ImageButton llamarButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            numeroTextView = itemView.findViewById(R.id.numeroTextView);
            llamarButton = itemView.findViewById(R.id.llamarButton);
        }
    }
}
