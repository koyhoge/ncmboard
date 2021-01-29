package org.koyhoge.ncmboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity implements OnClickListener {
    Button submitButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(submitButton)) {
            EditText te = findViewById(R.id.post_message);
            SpannableStringBuilder sb = (SpannableStringBuilder) te.getText();
            String message = sb.toString();

            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("key.StringData", message);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            finish();

        } else if (v.equals(cancelButton)) {
            Intent data = new Intent();
            data.putExtra("key.canceledData", "canceled");
            setResult(RESULT_CANCELED, data);
            finish();
        }
    }
}
