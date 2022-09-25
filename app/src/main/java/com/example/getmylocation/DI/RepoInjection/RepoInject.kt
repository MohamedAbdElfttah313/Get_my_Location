package com.example.getmylocation.DI.RepoInjection

import com.example.getmylocation.Data.Repositories.AuthRepositoryImp
import com.example.getmylocation.Data.Repositories.FriendsRepositoryImp
import com.example.getmylocation.Domain.Repositories.AuthRepository
import com.example.getmylocation.Domain.Repositories.FriendsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepoInject {

    @Singleton
    @Binds
    abstract fun bindAuthRepo(authRepository: AuthRepositoryImp): AuthRepository

    @Singleton
    @Binds
    abstract fun bindFriendsRepo(friendsRepository: FriendsRepositoryImp): FriendsRepository
}