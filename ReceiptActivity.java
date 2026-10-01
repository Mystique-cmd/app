import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        ArrayList<String> items = getIntent().getStringArrayListExtra("RECEIPT_ITEMS");
        double grandTotal = getIntent().getDoubleExtra("GRAND_TOTAL", 0.0);

        StringBuilder sb = new StringBuilder();
        sb.append("      MIAMI SUPERMARKET\n");
        sb.append("        *** RECEIPT ***\n");
        sb.append(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()));
        sb.append("\n--------------------------------\n");

        if (items != null) {
            for (String line : items) {
                sb.append(line).append("\n");
            }
        }

        sb.append("--------------------------------\n");
        sb.append(String.format("GRAND TOTAL: KES %.2f\n", grandTotal));
        sb.append("\n   Thank you for shopping!\n");

        ((TextView) findViewById(R.id.tvReceipt)).setText(sb.toString());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}