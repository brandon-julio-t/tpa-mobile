package edu.bluejack20_2.braven.domains.post

import androidx.fragment.app.Fragment
import edu.bluejack20_2.braven.R
import javax.inject.Inject

class PostValidator @Inject constructor() {
    fun validate(title: String?, description: String?, category: String?, fragment: Fragment): Pair<String, Boolean> {
        if (title.isNullOrBlank()) return Pair(fragment.getString(R.string.validator_title), false)
        if (description.isNullOrBlank()) return Pair(fragment.getString(R.string.validator_description), false)
        if (category.isNullOrBlank()) return Pair(fragment.getString(R.string.validator_category), false)
        return Pair(fragment.getString(R.string.ok), true)
    }
}