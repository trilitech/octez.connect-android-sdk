package io.tezos.octezconnect.transport.p2p.matrix.storage

import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.StoragePlugin
import io.tezos.octezconnect.transport.p2p.matrix.data.MatrixRoom
import io.tezos.octezconnect.transport.p2p.matrix.internal.storage.decorator.DecoratedP2pMatrixStoragePlugin

public interface P2pMatrixStoragePlugin : StoragePlugin {
    public suspend fun getMatrixRelayServer(): String?
    public suspend fun setMatrixRelayServer(relayServer: String?)

    public suspend fun getMatrixChannels(): Map<String, String>
    public suspend fun setMatrixChannels(channels: Map<String, String>)

    public suspend fun getMatrixSyncToken(): String?
    public suspend fun setMatrixSyncToken(syncToken: String?)

    public suspend fun getMatrixRooms(): List<MatrixRoom>
    public suspend fun setMatrixRooms(rooms: List<MatrixRoom>)

    override fun scoped(beaconScope: BeaconScope): P2pMatrixStoragePlugin
    public fun extend(): ExtendedP2pMatrixStoragePlugin = DecoratedP2pMatrixStoragePlugin(this)
}