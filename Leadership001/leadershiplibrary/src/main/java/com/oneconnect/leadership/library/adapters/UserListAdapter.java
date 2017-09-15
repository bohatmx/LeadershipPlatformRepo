package com.oneconnect.leadership.library.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.UserDTO;

import java.util.List;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.DataObjectHolder> {

    private static String LOG_TAG = "UsersListAdapter";
    private List<UserDTO> users;
    private Context context;
    IconListener listener;


    public static abstract class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView email;
        TextView no;
        TextView name;
        //CircleImageView img;
        ImageView iconTakePicture, iconLocation,iconGallery, iconPhone, iconPerson, iconLogs;
        View messageLayout,topLayout;

        public DataObjectHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.dr_name);
           //  no = itemView.findViewById(R.id.dr_img);
            email = itemView.findViewById(R.id.dr_email);
            //img = itemView.findViewById(R.id.dr_img);
            iconGallery = itemView.findViewById(R.id.iconGallery);
            iconLocation = itemView.findViewById(R.id.iconLocation);
            iconPhone = itemView.findViewById(R.id.iconPhone);
            iconPerson = itemView.findViewById(R.id.dr_icon);
            iconTakePicture = itemView.findViewById(R.id.iconTakePhoto);
            iconLogs = itemView.findViewById(R.id.iconLogs);
            topLayout = itemView.findViewById(R.id.topLayout);
            messageLayout = itemView.findViewById(R.id.messageLayout);

        }

        public abstract void onPlayClicked(int position);
    }


    public UserListAdapter(List<UserDTO> list, Context ctx, IconListener listener) {

        this.users = list;
        this.listener = listener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view) {
            @Override
            public void onPlayClicked(int position) {

            }
        };
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int pos) {

        final UserDTO m = users.get(pos);

        holder.name.setText(m.getLastName() + "" + m.getLastName());
        holder.email.setText(m.getEmail());

       /* if (!m.getPhotos().isEmpty() ){
            holder.img.setVisibility(View.VISIBLE);
            holder.iconPerson.setVisibility(View.GONE);
            Glide
                    .with(context)
                    .load(m.getPhotos().get( m.getPhotos().size() - 1 ).getUrl())
                    .into(holder.img);

        } else {
            holder.img.setVisibility(View.GONE);
            holder.iconPerson.setVisibility(View.VISIBLE);
        }*/

        holder.topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.messageLayout.getVisibility() == View.GONE) {
                    holder.messageLayout.setVisibility(View.VISIBLE);
                    animateIn(holder.iconGallery);
                    animateIn(holder.iconLocation);
                    animateIn(holder.iconTakePicture);
                } else {
                    holder.messageLayout.setVisibility(View.GONE);
                }

            }
        });


        holder.iconTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onTakePicture(m);

            }
        });

        holder.iconGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onGallery(m);

            }
        });

        holder.iconLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLocation(m);
            }
        });

        holder.iconPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhone(m);
            }
        });
        holder.iconLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDriverLogs(m);
            }
        });
    }

    public interface IconListener {
        void onTakePicture(UserDTO user);
        void onGallery(UserDTO user);
        void onLocation(UserDTO user);
        void onPhone(UserDTO user);
        void onDriverLogs(UserDTO driver);

    }


    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);
        an.setDuration(500);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


}