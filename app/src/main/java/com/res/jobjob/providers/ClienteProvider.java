package com.res.jobjob.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.res.jobjob.modelos.Cliente;

import java.util.HashMap;
import java.util.Map;

public class ClienteProvider {

    DatabaseReference mDatabase;

    public ClienteProvider(){


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Clientes");
    }

    public Task<Void> create(Cliente cliente){
        Map<String,Object> map = new HashMap<>();
        map.put("nombre",cliente.getNombre());
        map.put("correo",cliente.getCorreo());
        return mDatabase.child(cliente.getId()).setValue(map);

    }


}
