package com.example.studentdetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,  final int position, @NonNull MainModel model) {

        holder.name.setText(model.getName());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());

        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(R.drawable.studentt)
                .circleCrop()
                .error(R.drawable.studentt)
                .into(holder.img);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200) // Corrected syntax for setExpanded method
                        .create();


                //dialogPlus.show();
                View view =dialogPlus.getHolderView();
                EditText name=view.findViewById(R.id.txtName);
                EditText course=view.findViewById(R.id.txtCourse);
                EditText email=view.findViewById(R.id.txtEmail);
                EditText turl=view.findViewById(R.id.txtImageUrl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                name.setText(model.getName());
                course.setText(model.getCourse());
                email.setText(model.getEmail());
                turl.setText(model.getTurl());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String ,Object>map =new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("course",course.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("turl",turl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(getRef(holder.getAdapterPosition()).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "Updated Succesfuuly", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss(); //to dismiss dialobg
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.name.getContext(), "Error while uploading", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });



            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("are you sure");
                builder.setMessage("deleted data cannot be recovered");

               builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("students")
                                .child(getRef(holder.getLayoutPosition()).getKey()).removeValue();


                   }
               });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);

    }

    class myViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView name,course,email;

        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView)
        {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.img1);
            name = (TextView)itemView.findViewById((R.id.nametext));
            course =(TextView)itemView.findViewById(R.id.coursetext);
            email =(TextView)itemView.findViewById(R.id.emailtext);

            btnEdit =(Button)itemView.findViewById(R.id.btnEdit);
            btnDelete=(Button)itemView.findViewById(R.id.btnDelete);

        }

    }

}
