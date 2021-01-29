package org.koyhoge.ncmboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class MessageItemAdapter extends ArrayAdapter<MessageItem> {
    private LayoutInflater layoutInflater_;

    private SimpleDateFormat dateFormat_;

    /**
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    MessageItemAdapter(Context context, int textViewResourceId, List<MessageItem> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @param position    postion of item
     * @param convertView
     * @param parent
     * @return convertedView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageItem item = getItem(position);

        // create convertView when null only
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.message_item, null);
        }

        // Set data to each Widgets
        TextView userNameView = convertView.findViewById(R.id.username);
        userNameView.setText(item.getUserName());

        TextView timestampView = convertView.findViewById(R.id.timestamp);
        DateFormat df = getDateFormat();
        timestampView.setText(df.format(item.getTimestamp()));

        TextView messageView = convertView.findViewById(R.id.message);
        messageView.setText(item.getMessage());

        return convertView;
    }

    /**
     * Get DateFormat object to convert timestamp
     *
     * @return DateFormat object in internal use
     */
    protected DateFormat getDateFormat() {
        if (dateFormat_ == null) {
            dateFormat_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return dateFormat_;
    }
}
