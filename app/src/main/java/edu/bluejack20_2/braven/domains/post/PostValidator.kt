package edu.bluejack20_2.braven.domains.post

import javax.inject.Inject

class PostValidator @Inject constructor() {
    fun validate(title: String?, description: String?, category: String?): Pair<String, Boolean> {
        if (title.isNullOrBlank()) return Pair("Title must not be empty", false)
        if (description.isNullOrBlank()) return Pair("Description must not be empty", false)
        if (category.isNullOrBlank()) return Pair("Category must not be empty", false)
        return Pair("ok", true)
    }
}