package tdtu.mobile_dev_assignment.lc_assignment.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import tdtu.mobile_dev_assignment.lc_assignment.Models.User;
import tdtu.mobile_dev_assignment.lc_assignment.R;
import tdtu.mobile_dev_assignment.lc_assignment.Screen.LoginScreen;

public class AccountFragment extends Fragment {

    TextInputEditText username, email, phone, birthdate;
    CircleImageView avatar;
    SharedPreferences sp;
    Gson gson = new Gson();
    Button editBtn, logoutBtn;
    FirebaseDatabase mDatabase;
    DatabaseReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = view.findViewById(R.id.user_et);
        email = view.findViewById(R.id.email_et);
        phone = view.findViewById(R.id.phone_et);
        birthdate = view.findViewById(R.id.birthdate_et);
        editBtn = view.findViewById(R.id.editAccountBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        mDatabase = FirebaseDatabase.getInstance();

        avatar = view.findViewById(R.id.circularAvatar);

        String userString = sp.getString("User", "");
        User user = gson.fromJson(userString, User.class);

        userRef = mDatabase.getReference().child("Users").child(user.getUser_id());

        Picasso.get().load(user.getAvatar()).fit().centerCrop().into(avatar);

        username.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        birthdate.setText(user.getBirthdate());

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setName(username.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setBirthdate(birthdate.getText().toString());

                userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Edit succesfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to edit. Try again later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(getContext(), LoginScreen.class);
                startActivity(login);
                Animatoo.animateCard(getContext());
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sp = context.getSharedPreferences("HotelPrefs", Context.MODE_PRIVATE);
    }
}