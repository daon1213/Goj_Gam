package com.daon.goj_gam.data.repository.review

import android.net.Uri
import android.util.Log
import com.daon.goj_gam.data.entity.impl.ReviewEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class DefaultRestaurantReviewRepository(
    private val firebaseStorage: FirebaseStorage, // 리뷰 이미지 저장
    private val fireStore: FirebaseFirestore, // 리뷰저장
    private val ioDispatcher: CoroutineDispatcher
) : RestaurantReviewRepository {

    // 리뷰 요청
    override suspend fun getReviews(restaurantTitle: String): Result=
        withContext(ioDispatcher) {
            return@withContext try {
                val result = fireStore
                    .collection("review")
                    .whereEqualTo("restaurantTitle", restaurantTitle)
                    .get()
                    .await()
                Log.d("Repository", result.documents.toString())
                Result.Success(result.documents.map {
                    ReviewEntity(
                        id = it.get("id") as Long,
                        userId = it.get("userId") as String,
                        title = it.get("title") as String,
                        createdAt = it.get("createdAt") as Long,
                        content = it.get("content") as String,
                        rating = (it.get("rating") as Double).toFloat(),
                        imagesUrlList = (it.get("imagesUrlList") as? ArrayList<String>),
                        orderId = it.get("orderId") as String,
                        restaurantTitle = it.get("restaurantTitle") as String
                    )
                })
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception)
            }
        }

    // 리뷰 삽입
    override suspend fun insertReview(reviewEntity: ReviewEntity): Result =
        withContext(ioDispatcher) {
            val result: Result

            result = try {
                fireStore.collection("review")
                    .add(reviewEntity)
                Result.Success<Any>()
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception)
            }
            return@withContext result
        }

    override suspend fun insertReviewImages(imageUrlList: List<Uri>): Result =
        withContext(ioDispatcher) {
            val uploadDeferred: List<Deferred<Any>> =
                imageUrlList.mapIndexed { index, uri ->
                    async {
                        try {
                            val fileName = "image_${System.currentTimeMillis()}_$index.png"
                            return@async firebaseStorage
                                .reference
                                .child("review/photo")
                                .child(fileName)
                                .putFile(uri)
                                .await()
                                .storage
                                .downloadUrl
                                .await()
                                .toString()
                        } catch (exception: Exception) {
                            exception.printStackTrace()
                            return@async Pair(uri, exception)
                        }
                    }
                }
            return@withContext Result.Success(uploadDeferred.awaitAll())
        }

    sealed interface Result {
        data class Success<T>(
            val data: T? = null
        ) : Result

        data class Error(
            val e: Throwable
        ) : Result
    }
}