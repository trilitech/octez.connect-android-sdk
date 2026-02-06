package io.tezos.octezconnect.core.internal.storage

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.Peer
import io.tezos.octezconnect.core.data.Permission
import io.tezos.octezconnect.core.data.selfRemoved
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.storage.sharedpreferences.getAppMetadata
import io.tezos.octezconnect.core.internal.storage.sharedpreferences.getPeers
import io.tezos.octezconnect.core.internal.storage.sharedpreferences.getPermissions
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.internal.utils.asHexString
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.ExtendedStorage
import io.tezos.octezconnect.core.storage.SecureStorage
import io.tezos.octezconnect.core.storage.Storage
import io.tezos.octezconnect.core.storage.StoragePlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class StorageManager(
    private val beaconScope: BeaconScope,
    private val storage: ExtendedStorage,
    private val secureStorage: SecureStorage,
    private val identifierCreator: IdentifierCreator,
    public val beaconConfiguration: BeaconConfiguration,
) : ExtendedStorage by storage, SecureStorage by secureStorage {

    private val _plugins: MutableList<StoragePlugin> = mutableListOf()
    @PublishedApi
    internal val plugins: List<StoragePlugin> get() = _plugins

    public constructor(
        beaconScope: BeaconScope,
        storage: Storage,
        secureStorage: SecureStorage,
        identifierCreator: IdentifierCreator,
        configuration: BeaconConfiguration,
    ) : this(beaconScope, storage.extend(configuration), secureStorage, identifierCreator, configuration)

    public fun addPlugins(plugins: List<StoragePlugin>) {
        _plugins.addAll(plugins.map { it.scoped(beaconScope) })
    }

    public fun addPlugins(vararg plugins: StoragePlugin) {
        addPlugins(plugins.toList())
    }

    public inline fun <reified T : StoragePlugin> getPlugin(): T? = plugins.filterIsInstance<T>().firstOrNull()

    public inline fun <reified T : StoragePlugin> hasPlugin(): Boolean = plugins.any { it is T }

    private val removedPeers: MutableSharedFlow<Peer> by lazy { MutableSharedFlow(extraBufferCapacity = 64) }
    public val updatedPeers: Flow<Peer>
        get() = merge(storage.peers, removedPeers)

    public suspend fun getPeers(): List<Peer> =
        storage.getPeers(beaconConfiguration)

    public suspend inline fun <reified T: Peer> updatePeers(peers: List<T>, noinline selector: T.() -> List<Any>) {
        addPeers(peers, overwrite = true) {
            when (this) {
                is T -> selector(this)
                else -> null
            }
        }
    }

    public suspend fun removePeers(peers: List<Peer>) {
        removePeers { peer -> peers.any { it.publicKey == peer.publicKey } }
    }

    override suspend fun removePeers(predicate: ((Peer) -> Boolean)?) {
        runCatching {
            val peers = storage.getPeers(beaconConfiguration)
            val toRemove = predicate?.let { peers.filter(it) } ?: peers

            storage.removePeers(predicate)

            val removedSenderIDs = toRemove.map { identifierCreator.senderId(it.publicKey.asHexString().toByteArray()).getOrThrow() }.toSet()

            removePermissions { removedSenderIDs.contains(it.senderId) }
            removeAppMetadata { removedSenderIDs.contains(it.senderId) }

            CoroutineScope(Dispatchers.Default).launch {
                toRemove.map { it.selfRemoved() }.forEach {
                    removedPeers.tryEmit(it)
                }
            }
        }
    }

    public suspend fun getAppMetadata(): List<AppMetadata> =
        storage.getAppMetadata(beaconConfiguration)

    public suspend fun removeAppMetadata(appsMetadata: List<AppMetadata>) {
        removeAppMetadata { metadata -> appsMetadata.any { it.senderId == metadata.senderId } }
    }

    public suspend fun getPermissions(): List<Permission> =
        storage.getPermissions(beaconConfiguration)

    public suspend fun removePermissions(permissions: List<Permission>) {
        removePermissions { permission -> permissions.any { it == permission } }
    }

    override fun scoped(beaconScope: BeaconScope): StorageManager =
        if (beaconScope == this.beaconScope) this
        else StorageManager(beaconScope, storage.scoped(beaconScope), secureStorage.scoped(beaconScope), identifierCreator, beaconConfiguration)
}