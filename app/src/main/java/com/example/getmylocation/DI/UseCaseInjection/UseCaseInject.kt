package com.example.getmylocation.DI.UseCaseInjection

import com.example.getmylocation.Domain.UseCases.AuthUseCases.*
import com.example.getmylocation.Domain.UseCases.FriendsUseCases.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseInject {

    @Binds
    @Singleton
    abstract fun bindLoginUseCase(loginUseCase: loginUseCaseImp) : loginUseCase

    @Binds
    @Singleton
    abstract fun bindAddUserDataUseCase(addUserDataUseCase: addUserDataUseCaseImp) : addUserDataUseCase

    @Binds
    @Singleton
    abstract fun bindGetCurrentUserUseCase(getCurrentUserUseCase: getCurrentUserUseCaseImp) : getCurrentUserUseCase


    @Binds
    @Singleton
    abstract fun bindGetSearchResByUseCase(getSearchResultByUseCase: getSearchResultByUseCaseImp) : getSearchResultByUseCase

    @Binds
    @Singleton
    abstract fun bindGetFriendsUseCase(getFriendsUseCase: getFriendsUseCaseImp) : getFriendsUseCase

    @Binds
    @Singleton
    abstract fun bindGetFriendRequestsUseCase(getFriendReqUseCase: getFriendRequestsUseCaseImp) : getFriendRequestsUseCase

    @Binds
    @Singleton
    abstract fun bindSignUpUseCase(signUpUseCase: signUpUseCaseImp) : signUpUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateFriendAcc(updateFriendAcc: updateFriendAcceptedUseCaseImp) : updateFriendAcceptedUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateSelfFriendAcc(updateSelfFriendAccUseCase: updateSelfFriendAccUseCaseImp) : updateSelfFriendAccUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateSelfFriendDec(updateSelfFriendDec: updateSelfFriendDecUseCaseImp) : updateSelfFriendDecUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateFriendDec(updateFriendDec: updateFriendDecUseCaseImp) : updateFriendDecUseCase

    @Binds
    @Singleton
    abstract fun bindSignOut(signOut: signOutUseCaseImp) : signOutUseCase

    @Binds
    @Singleton
    abstract fun bindSendOrUnsend(sendUnsend: sendOrUnsendUseCaseImp) : sendOrUnsendUseCase


}