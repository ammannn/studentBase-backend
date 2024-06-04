package com.university.mcmaster.repositories.utils;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.FirebaseCommonProps;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class FirebaseUtils<T extends FirebaseCommonProps>{

    public boolean save(T entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        try {
            WriteResult future = firestore.collection(entity.getCollection())
                    .document(entity.getId()).set(entity).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(String id,String collection, Map<String,Object> updateMap) {
        Firestore firestore = FirestoreClient.getFirestore();
        try {
            WriteResult result = firestore.collection(collection).document(id)
                    .update(updateMap).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Query addLastSeen(Query query,String collection,String lastSeen){
        if(null != collection && null != lastSeen && false == lastSeen.trim().isEmpty()){
            try {
                DocumentSnapshot snapshot = FirestoreClient.getFirestore().collection(collection).document(lastSeen).get().get();
                if(null != snapshot){
                    System.out.println("snapshot is available");
                    query = query.startAfter(snapshot);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return query;
    }

    public T processQueryForEntity(Query query,Class<T> tClass){
        try {
            return query.get().get().getDocuments().stream().findFirst().map(snp->snp.toObject(tClass)).orElse(null);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> processQueryForEntityList(Query query,Class<T> tClass){
        try {
            return query.get().get().getDocuments().stream().map(snp->snp.toObject(tClass)).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
