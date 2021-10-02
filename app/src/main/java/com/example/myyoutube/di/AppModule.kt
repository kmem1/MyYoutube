package com.example.myyoutube.di

import com.example.myyoutube.data.repository.RepositoryImpl
import com.example.myyoutube.domain.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun provideRepository(
        firebaseDatabase: FirebaseDatabase,
        firebaseAuth: FirebaseAuth
    ): Repository {
        return RepositoryImpl(firebaseDatabase, firebaseAuth)
    }
}