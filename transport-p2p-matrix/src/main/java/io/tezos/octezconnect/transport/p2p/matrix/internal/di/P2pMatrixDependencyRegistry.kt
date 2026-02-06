package io.tezos.octezconnect.transport.p2p.matrix.internal.di

import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.migration.Migration
import io.tezos.octezconnect.core.internal.network.HttpClient
import io.tezos.octezconnect.core.internal.utils.app
import io.tezos.octezconnect.core.internal.utils.delegate.lazyWeak
import io.tezos.octezconnect.core.network.provider.HttpClientProvider
import io.tezos.octezconnect.transport.p2p.matrix.P2pMatrix
import io.tezos.octezconnect.transport.p2p.matrix.internal.P2pMatrixCommunicator
import io.tezos.octezconnect.transport.p2p.matrix.internal.P2pMatrixSecurity
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.MatrixClient
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.event.MatrixEventService
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.node.MatrixNodeService
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.room.MatrixRoomService
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.user.MatrixUserService
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.store.MatrixStore
import io.tezos.octezconnect.transport.p2p.matrix.internal.migration.v1_0_4.P2pMatrixMigrationFromV1_0_4
import io.tezos.octezconnect.transport.p2p.matrix.internal.store.P2pMatrixStore
import io.tezos.octezconnect.transport.p2p.matrix.storage.P2pMatrixStoragePlugin

internal class P2pMatrixDependencyRegistry(dependencyRegistry: DependencyRegistry) : ExtendedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- client --

    override fun p2pMatrix(storagePlugin: P2pMatrixStoragePlugin, matrixNodes: List<String>, httpClientProvider: HttpClientProvider?): P2pMatrix {
        with(storageManager) {
            if (!hasPlugin<P2pMatrixStoragePlugin>()) addPlugins(storagePlugin.extend())
        }

        val httpClient = httpClient(httpClientProvider)

        return P2pMatrix(matrixClient(httpClient), p2pMatrixStore(httpClient, matrixNodes), p2pMatrixSecurity, p2pMatrixCommunicator, logger(P2pMatrix.TAG))
    }

    // -- P2P --

    override val p2pMatrixCommunicator: P2pMatrixCommunicator by lazyWeak { P2pMatrixCommunicator(app(beaconScope), crypto, json) }
    override val p2pMatrixSecurity: P2pMatrixSecurity by lazyWeak { P2pMatrixSecurity(app(beaconScope), crypto) }

    override fun p2pMatrixStore(httpClient: HttpClient, matrixNodes: List<String>): P2pMatrixStore =
        P2pMatrixStore(app(beaconScope), p2pMatrixCommunicator, matrixClient(httpClient), matrixNodes, storageManager, migration, logger(P2pMatrixStore.TAG))

    // -- Matrix --

    private val matrixClients: MutableMap<Int, MatrixClient> = mutableMapOf()
    override fun matrixClient(httpClient: HttpClient): MatrixClient =
        matrixClients.getOrPut(httpClient.hashCode()) {
            MatrixClient(
                MatrixStore(storageManager),
                MatrixNodeService(httpClient),
                MatrixUserService(httpClient),
                MatrixRoomService(httpClient),
                MatrixEventService(httpClient),
                poller,
                logger(MatrixClient.TAG),
            )
        }

    // -- migration --

    override val migration: Migration by lazyWeak {
        dependencyRegistry.migration.apply {
            register(
                P2pMatrixMigrationFromV1_0_4(storageManager)
            )
        }
    }
}