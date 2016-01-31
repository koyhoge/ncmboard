package org.koyhoge.ncmboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PostActivity extends AppCompatActivity implements OnClickListener{
    Button submitButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(submitButton)) {
            EditText te = (EditText) findViewById(R.id.post_message);
            SpannableStringBuilder sb = (SpannableStringBuilder)te.getText();
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
