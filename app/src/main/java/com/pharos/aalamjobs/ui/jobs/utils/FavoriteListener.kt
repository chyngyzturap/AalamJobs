package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.FavJobsResponse


interface FavoriteListener {
    fun postFavJobSuccess()
    fun addToFavFailed(code: Int?)
    fun deleteFromFav()
    fun setFavoriteJob(jobs: FavJobsResponse)
    fun getFavJobError(code: Int?)
}