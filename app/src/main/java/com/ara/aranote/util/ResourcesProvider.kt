package com.ara.aranote.util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

/**
 * provide string and plural resources without coupling to Android APIs
 */
interface ResourcesProvider {

    fun getString(@StringRes stringResId: Int, formatArgs: Any? = null): String

    fun getQuantityString(
        @PluralsRes stringResId: Int,
        quantity: Int,
        formatArgs: Any? = null
    ): String
}

class ResourcesProviderImpl constructor(
    private val context: Context
) : ResourcesProvider {

    override fun getString(@StringRes stringResId: Int, formatArgs: Any?): String {
        return if (formatArgs != null)
            context.resources.getString(stringResId, formatArgs)
        else
            context.resources.getString(stringResId)
    }

    override fun getQuantityString(
        @PluralsRes stringResId: Int,
        quantity: Int,
        formatArgs: Any?
    ): String {
        return if (formatArgs != null)
            context.resources.getQuantityString(stringResId, quantity, formatArgs)
        else
            context.resources.getQuantityString(stringResId, quantity)
    }
}
