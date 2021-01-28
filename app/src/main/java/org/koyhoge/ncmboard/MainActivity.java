package org.koyhoge.ncmboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBObject;
import com.nifcloud.mbaas.core.NCMBQuery;
import com.nifcloud.mbaas.core.NCMBUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * Request code for floating action button
     */
    public static final int REQ_CODE_FAB = 1;

    /**
     * Object class name for messages
     */
    private static final String NCMB_CLASSNAME_MESSAGES = "messages";

    /**
     * Current user object
     */
    public NCMBUser currentUser = null;

    protected ListView messageView;
    protected List<MessageItem> messages;
    protected MessageItemAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize NCMB
        String app_key = "APP_KEY";
        String client_key = "CLIENT_KEY";
        NCMB.initialize(this.getApplicationContext(), app_key, client_key);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showLoginDialog();

        // setup message container
        messages = new ArrayList<>();
        messageAdapter = new MessageItemAdapter(this, 0, messages);
        messageView = findViewById(R.id.listView);
        messageView.setAdapter(messageAdapter);

        // load exists messages
        loadMessages();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (currentUser == null) {
                Toast.makeText(MainActivity.this,
                        "Please login first",
                        Toast.LENGTH_LONG)
                        .show();
                showLoginDialog();
                return;
            }
            startActivityForResult(new Intent(
                    MainActivity.this,
                    PostActivity.class
            ), REQ_CODE_FAB);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (requestCode == REQ_CODE_FAB) {
                if (resultCode == RESULT_OK) {
                    String message = bundle.getString("key.StringData");

                    MessageItem item = new MessageItem();
                    item.setMessage(message);
                    item.setUserName(currentUserName());
                    item.setTimestamp(new Date());
                    try {
                        appendMessage(item);
                    } catch (NCMBException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    /**
     * Append message item to list
     *
     * @param item appended message item
     */
    protected void appendMessage(MessageItem item) throws NCMBException {

        NCMBObject messageObj = new NCMBObject(NCMB_CLASSNAME_MESSAGES);
        messageObj.put("userId", currentUser.getObjectId());
        messageObj.put("userName", currentUser.getUserName());
        messageObj.put("message", item.getMessage());

        messageObj.saveInBackground(e -> {
            if (e != null) {
                Toast.makeText(MainActivity.this,
                        "Message post failed",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }
            // update all messages
            loadMessages();
        });
    }

    /**
     * load all messages already exists
     */
    protected void loadMessages() {
        NCMBQuery<NCMBObject> query = new NCMBQuery<>(NCMB_CLASSNAME_MESSAGES);
        query.findInBackground((list, e) -> {
            if (e != null) {
                Toast.makeText(MainActivity.this,
                        "Failed loading messages",
                        Toast.LENGTH_LONG)
                        .show();
            } else {
                List<MessageItem> tmpMessages = new ArrayList<>();
                for (NCMBObject obj : list) {
                    MessageItem item = new MessageItem();
                    item.setUserName(obj.getString("userName"));
                    item.setUserId(obj.getString("userId"));
                    item.setMessage(obj.getString("message"));
                    try {
                        item.setTimestamp(obj.getUpdateDate());
                    } catch (NCMBException ncmbException) {
                        ncmbException.printStackTrace();
                    }

                    tmpMessages.add(item);
                }

                // update messages
                messages.clear();
                messages.addAll(tmpMessages);
                messageAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Get current user name as string
     *
     * @return user name
     */
    public String currentUserName() {
        if (currentUser == null) {
            return "";
        }
        return currentUser.getUserName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Show login dialog
     */
    public void showLoginDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = prompt.findViewById(R.id.login_name);
        final EditText pass = prompt.findViewById(R.id.login_password);

        alertDialogBuilder.setTitle("NcmBoard LOGIN");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    String password = pass.getText().toString();
                    String username = user.getText().toString();
                    try {
                        if (username.length() < 2 || password.length() < 2) {
                            Toast.makeText(MainActivity.this,
                                    "Invalid username or password",
                                    Toast.LENGTH_LONG)
                                    .show();
                            showLoginDialog();
                        } else {
                            NCMBUser.loginInBackground(username, password, (ncmbUser, e) -> {
                                if (e != null) {
                                    // Login failed
                                    Toast.makeText(MainActivity.this,
                                            "Invalid username or passwrd",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    showLoginDialog();
                                } else {
                                    // Login successed
                                    currentUser = ncmbUser;
                                }
                            });
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        alertDialogBuilder.show();
    }
}
