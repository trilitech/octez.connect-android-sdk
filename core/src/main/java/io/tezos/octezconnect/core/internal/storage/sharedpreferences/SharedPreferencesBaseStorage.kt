package io.tezos.octezconnect.core.internal.storage.sharedpreferences

import android.content.SharedPreferences
import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.Maybe
import io.tezos.octezconnect.core.data.Peer
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.exception.BlockchainNotFoundException
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.utils.json
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.Storage
import kotlinx.serialization.Contextual
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public abstract class SharedPreferencesBaseStorage(public val beaconScope: BeaconScope, protected val sharedPreferences: SharedPreferences) {

    protected fun Key.scoped(): String =
        when (beaconScope) {
            is BeaconScope.Global -> value
            is BeaconScope.Instance -> "${beaconScope.id}:$value"
        }

    protected inline fun <reified T> SharedPreferences.getSerializable(key: String, default: @Contextual T, beaconScope: BeaconScope): T {
        val encoded = getString(key, null)

        return encoded?.let { json(beaconScope).decodeFromString(encoded) } ?: default
    }

    protected fun SharedPreferences.putString(key: String, value: String?) {
        edit().putString(key, value).apply()
    }

    protected fun SharedPreferences.putStringSet(key: String, value: Set<String>) {
        edit().putStringSet(key, value).apply()
    }

    protected inline fun <reified T> SharedPreferences.putSerializable(key: String, value: @Contextual T, beaconScope: BeaconScope) {
        val encoded = json(beaconScope).encodeToString(value)
        putString(key, encoded)
    }

    protected interface Key {
        public val value: String

        public fun unscoped(): String = value
    }
}