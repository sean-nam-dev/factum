package com.devflow.factum.data.repository

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import com.devflow.factum.domain.core.Result
import com.devflow.factum.domain.core.DataError
import com.devflow.factum.domain.model.Fact
import com.devflow.factum.domain.model.FactBase
import com.devflow.factum.domain.repository.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): FirestoreRepository {

    override suspend fun getFactFromServer(
        collectionName: String,
        documentId: String
    ): Result<Fact, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = firestore
                    .collection(collectionName)
                    .document(documentId)
                    .get(Source.SERVER)
                    .await()

                val fact = documentSnapshot
                    .toObject(Fact::class.java)?.copy(
                        factBase = FactBase(
                            category = collectionName,
                            documentId = documentId
                        ),
                        readTime = calculateReadTime(documentSnapshot.getString("text"))
                    ) ?: throw IllegalStateException()

                Result.Success(fact)
            } catch (e: FirebaseFirestoreException) {
                when(e.code) {
                    FirebaseFirestoreException.Code.UNAVAILABLE -> Result.Error(DataError.Network.UNAVAILABLE)
                    FirebaseFirestoreException.Code.NOT_FOUND -> Result.Error(DataError.Network.NOT_FOUND)
                    else -> Result.Error(DataError.Network.EXTERNAL_UNKNOWN)
                }
            } catch (e: IllegalStateException) {
                Result.Error(DataError.Network.DOCUMENT_NULL)
            } catch (e: Exception) {
                Result.Error(DataError.Network.INTERNAL_UNKNOWN)
            }
        }
    }

    override suspend fun getFactListFromCache(
        collectionName: String
    ): Result<List<Fact>, DataError.Local> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = firestore
                    .collection(collectionName)
                    .limit(10L)
                    .get(Source.CACHE)
                    .await()

                val facts = querySnapshot.documents.mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(Fact::class.java)?.copy(
                        factBase = FactBase(
                            category = collectionName,
                            documentId = documentSnapshot.id
                        )
                    )
                }

                if (facts.isNotEmpty()) {
                    Result.Success(facts)
                } else {
                    Result.Error(DataError.Local.CACHE_EMPTY)
                }
            } catch (e: Exception) {
                Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun getCollectionSize(collectionName: String): Result<Long, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {
                val size = firestore.collection(collectionName)
                    .count()
                    .get(AggregateSource.SERVER)
                    .await()
                    .count

                Result.Success(size)
            } catch (e: FirebaseFirestoreException) {
                when(e.code) {
                    FirebaseFirestoreException.Code.UNAVAILABLE -> Result.Error(DataError.Network.UNAVAILABLE)
                    FirebaseFirestoreException.Code.NOT_FOUND -> Result.Error(DataError.Network.NOT_FOUND)
                    else -> Result.Error(DataError.Network.EXTERNAL_UNKNOWN)
                }
            } catch (e: Exception) {
                Result.Error(DataError.Network.INTERNAL_UNKNOWN)
            }
        }
    }

    private fun calculateReadTime(text: String?): Int {
        val WORDS_PER_MINUTE  = 150

        val wordCount = text?.split("\\s+".toRegex())?.size ?: 150
        return ceil(wordCount / WORDS_PER_MINUTE.toDouble()).toInt()
    }
}