package com.oneconnect.leadership.admin.ebook;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneconnect.leadership.admin.AdminMainActivity;
import com.oneconnect.leadership.admin.AdminSignInActivity;
import com.oneconnect.leadership.admin.R;

import java.io.File;
import java.util.List;

/**
 * Created by Nkululeko on 2017/04/12.
 */

public class EbookAdapter extends RecyclerView.Adapter<EbookAdapter.EbookViewHolder> {

    private List<String> paths;
    private Context context;
    private EbookAdapterListener listener;

    public interface EbookAdapterListener {
        void onUploadEbook(String path);
        void onReadEbook(String path);
    }

    public EbookAdapter(List<String> paths, Context context, EbookAdapterListener listener) {
        this.paths = paths;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public EbookAdapter.EbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ebook_item, parent, false);
        return new EbookAdapter.EbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EbookViewHolder holder, int position) {
        final String path = paths.get(position);
        File file = new File(path);
        Log.w("EbookAdapter", "onBindViewHolder: ".concat(path).concat(" size: ").concat(String.valueOf(file.length())) );
        holder.image.setImageDrawable(context.getDrawable(R.drawable.ebook));
        int i = path.lastIndexOf("/");
        holder.txtFileName.setText(path.substring(i + 1));
        holder.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUploadEbook(path);
            }
        });
        holder.bookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReadEbook(path);
                onReadEbook(path);
               /* File pdfFile = new File(Environment.getExternalStorageDirectory(), path);
                try {

                    if (pdfFile.exists()) {

                        Uri path = Uri.fromFile(pdfFile);

                        Intent objIntent = new Intent(Intent.ACTION_VIEW);

                        objIntent.setDataAndType(path, "application/pdf");

                        objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        context.startActivity(objIntent);

                    } else {



                    }

                } catch (ActivityNotFoundException e) {


                } catch (Exception e) {

                    e.printStackTrace();

                }*/
            }
        });
       }


    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class EbookViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtFileName;
        protected ImageView image, bookIcon;
        protected Button btnUpload, btnPlay;

        public EbookViewHolder(View itemView) {
            super(itemView);
            txtFileName = (TextView) itemView.findViewById(R.id.fileName);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnUpload = (Button) itemView.findViewById(R.id.btnUpload);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
            bookIcon = (ImageView) itemView.findViewById(R.id.bookIcon);
        }

    }

    public void onReadEbook(String path){
        File f = new File(path);
        if (f.exists()) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ path);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(target);
            Intent intent = Intent.createChooser(target, "Open File");

        }
    }

}
