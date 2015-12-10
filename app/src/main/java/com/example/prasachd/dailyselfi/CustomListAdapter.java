package com.example.prasachd.dailyselfi;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by prasachd on 11/18/15.
 */
public class CustomListAdapter extends ArrayAdapter<MySelfieBean> {
    private final Context mContext;


    public CustomListAdapter(Context context, int resource, int textViewResource, List<MySelfieBean> objects) {
        super(context, resource, textViewResource, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MySelfieBean mySelfie = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_txt);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_img);
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.item_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mySelfie != null) {
            viewHolder.textView.setText(getReadableSelfieName(mySelfie.getMName()));
            viewHolder.imageView.setImageBitmap(mySelfie.getMThumb());
            viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageButton imageButton = (ImageButton)v;
                    Boolean commentAdded = mySelfie.getMCommentAdded();
                    if(commentAdded){
                        showAddedComment(imageButton, mySelfie);
                    }else {
                        showInputDialog(imageButton, mySelfie);
                    }
                }
            });
        }

        return convertView;
    }

    protected void showAddedComment(ImageButton imageButton, MySelfieBean mySelfie) {
        final ImageButton imageButtonFinal = imageButton;
        final MySelfieBean mySelfieBeanFinal = mySelfie;

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.added_comment, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptView);


        final TextView commentTextView = (TextView) promptView.findViewById(R.id.added_comment);
        commentTextView.setText(mySelfie.getMComment());
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        final AlertDialog alert = alertDialogBuilder.create();

        final ImageButton deleteButton = (ImageButton) promptView.findViewById(R.id.deleteComment);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //delete comment from db
                //replace button in main activity view to add
                //dismiss dialog
                mySelfieBeanFinal.setmCommentAdded(false);
                imageButtonFinal.setImageResource(R.drawable.plus);
                ((MainActivity)mContext).deleteComment(mySelfieBeanFinal);
                alert.dismiss();
            }
        });
        final ImageButton editButton = (ImageButton) promptView.findViewById(R.id.editComment);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //dismiss this dialog
                //delete comment from db
                // bring in add comment dialog
                mySelfieBeanFinal.setmCommentAdded(false);
                imageButtonFinal.setImageResource(R.drawable.plus);
                ((MainActivity)mContext).deleteComment(mySelfieBeanFinal);
                alert.dismiss();
                showInputDialog(imageButtonFinal,mySelfieBeanFinal);
            }
        });
        alert.show();
    }

    protected void showInputDialog(ImageButton imageButton,MySelfieBean mySelfie) {
        final ImageButton imageButtonFinal = imageButton;
        final MySelfieBean mySelfieBeanFinal = mySelfie;

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.comment_diaglog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mySelfieBeanFinal.setMComment(editText.getText().toString());
                        mySelfieBeanFinal.setmCommentAdded(true);
                        imageButtonFinal.setImageResource(R.drawable.document);
                        ((MainActivity)mContext).insertComment(mySelfieBeanFinal);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


private class ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageButton imageButton;
    }

    private static String getReadableSelfieName(String name) {
        String[] split = name.split("_");
        String date = split[1];
        String time = split[2];
        String dateTime = date + time;
        String format = "yyyy_MM_dd_HH_mm_ss";

        try {
            return DateFormat.getDateTimeInstance().format(new SimpleDateFormat(format).parse(dateTime));
        } catch (ParseException pe) {
            return name;
        }
    }
}
