package com.example.mobileseenit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileseenit.helpers.PhotoWrapper;

public class ImageDialogSeenIt extends DialogFragment {
        private Dialog mDialog;
        private String imgUrl;
        private ImageView imgView;
        private PhotoWrapper pWrapper;

        public static ImageDialogSeenIt newInstance( PhotoWrapper p) {
                ImageDialogSeenIt f = new ImageDialogSeenIt();
                f.pWrapper = p;
                return f;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        Bundle savedInstanceState) {
                View v = inflater.inflate(R.layout.fragment_dialog, container, false);
                
                //Get fields to fill
                imgView = (ImageView) v.findViewById(R.id.imageDialog);
                TextView title = (TextView) v.findViewById(R.id.photo_title);
                TextView link = (TextView) v.findViewById(R.id.photo_link);
                
                //Set image
                imgView.setImageBitmap(pWrapper.getBitmap());
                
                //Set details
                title.setText(pWrapper.getDetailMap().get(PhotoWrapper.TITLE_FIELD));
                
                //Create clickable link for original image
                link.setText(
                    Html.fromHtml(
                        "<a href=" +pWrapper.getDetailMap().get(PhotoWrapper.LINK_FIELD) +">View Online</a> "));
            link.setMovementMethod(LinkMovementMethod.getInstance());
                return v;
        }

        public Dialog getmDialog() {
                return mDialog;
        }

        public void setmDialog(Dialog mDialog) {
                this.mDialog = mDialog;
        }

}