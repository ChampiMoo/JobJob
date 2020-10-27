package com.res.jobjob.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.res.jobjob.modelos.Cliente;
import com.res.jobjob.modelos.Socio;

public class SocioProvider {

    DatabaseReference mDatabase;

    public SocioProvider(){


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Socios");
    }

    public Task<Void> create(Socio socio){
        return mDatabase.child(socio.getId()).setValue(socio);

    }

}
