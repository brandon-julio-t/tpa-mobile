package edu.bluejack20_2.braven.factories

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.firebase.ui.firestore.SnapshotParser
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import edu.bluejack20_2.braven.interfaces.SimpleFactory

class FirestorePagingAdapterOptionsFactory constructor(
    private val lifecycleOwner: LifecycleOwner,
    private val query: Query
) : SimpleFactory<FirestorePagingOptions<DocumentSnapshot>> {
    override fun create(): FirestorePagingOptions<DocumentSnapshot> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(2)
            .setPageSize(4)
            .build()

        return FirestorePagingOptions.Builder<DocumentSnapshot>()
            .setLifecycleOwner(lifecycleOwner)
            .setQuery(query, config, SnapshotParser { return@SnapshotParser it })
            .build()
    }
}