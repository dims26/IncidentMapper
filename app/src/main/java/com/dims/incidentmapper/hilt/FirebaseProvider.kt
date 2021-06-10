package com.dims.incidentmapper.hilt

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseProvider {
    @Provides
    @Singleton
    fun provideFirestoreDb() : FirebaseFirestore = FirebaseFirestore.getInstance()

//    @Provides
//    @Singleton
//    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()
}