package com.pharos.aalamjobs.utils

import android.app.Application
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import com.pharos.aalamjobs.data.local.prefs.UserPreferences2
import com.pharos.aalamjobs.data.local.prefs.UserPreferencesImplementation
import com.pharos.aalamjobs.data.local.user.UserDataLocal
import com.pharos.aalamjobs.data.local.user.UserDataLocalImplement
import com.pharos.aalamjobs.data.repository.AuthRepository
import com.pharos.aalamjobs.data.repository.ItemRepository
import com.pharos.aalamjobs.data.repository.JobsRepository
import com.pharos.aalamjobs.ui.auth.AuthViewModel
import com.pharos.aalamjobs.ui.base.ViewModelFactory
import com.pharos.aalamjobs.ui.splash.UserPrefsViewModelFactory

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import com.pharos.aalamjobs.data.network.JobsApi

class AalamJobsApp : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@AalamJobsApp))
//
        bind() from singleton { JobsApi()}
//        bind() from singleton { ApiService2() }
//        bind() from singleton { ApiService3() }

        bind<UserPreferences2>() with singleton { UserPreferencesImplementation(instance()) }
        bind<UserDataLocal>() with singleton { UserDataLocalImplement(instance()) }

        bind() from provider { UserPrefsViewModelFactory(instance(), instance(), instance()) }
//
//        bind<AuthRepository>() with singleton { AuthRepository(instance()) }
//        bind() from provider { ViewModelFactory(instance()) }

        bind<JobsRepository>() with singleton { JobsRepository(instance()) }
        bind() from provider { ViewModelFactory(instance()) }
//
//        bind<UserRepository>() with singleton { UserRepository(instance()) }
//        bind() from provider { UserViewModelFactory(instance(), instance()) }

//        bind<ItemRepository>() with singleton { ItemRepository(instance()) }
//        bind() from provider {
//            ItemViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind<CartRepository>() with singleton { CartRepository(instance()) }
//        bind() from provider { CartViewModelFactory(instance(), instance()) }
//
//        bind<MembershipRepo>() with singleton { MembershipRepo(instance()) }
//        bind() from provider { MembershipViewModelFactory(instance(), instance()) }
//
//        bind<OrdersRepository>() with singleton { OrdersRepository(instance()) }
//        bind() from provider { OrderViewModelFactory(instance(), instance()) }
//
//        bind<ReviewsRepository>() with singleton { ReviewsRepository(instance()) }
//        bind() from provider { ReviewViewModelFactory(instance(), instance()) }
//
//        bind<OPayRepository>() with singleton { OPayRepository(instance()) }
//        bind() from provider { OPayViewModelFactory(instance()) }

    }
}