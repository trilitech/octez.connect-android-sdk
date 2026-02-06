package io.tezos.octezconnect.transport.p2p.matrix.internal.di

import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.di.findExtended
import io.tezos.octezconnect.core.internal.network.HttpClient
import io.tezos.octezconnect.core.network.provider.HttpClientProvider
import io.tezos.octezconnect.transport.p2p.matrix.P2pMatrix
import io.tezos.octezconnect.transport.p2p.matrix.internal.P2pMatrixCommunicator
import io.tezos.octezconnect.transport.p2p.matrix.internal.P2pMatrixSecurity
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.MatrixClient
import io.tezos.octezconnect.transport.p2p.matrix.internal.store.P2pMatrixStore
import io.tezos.octezconnect.transport.p2p.matrix.storage.P2pMatrixStoragePlugin

internal interface ExtendedDependencyRegistry : DependencyRegistry {

    // -- client --

    fun p2pMatrix(storagePlugin: P2pMatrixStoragePlugin, matrixNodes: List<String>, httpClientProvider: HttpClientProvider?): P2pMatrix

    // -- P2P --

    val p2pMatrixCommunicator: P2pMatrixCommunicator
    val p2pMatrixSecurity: P2pMatrixSecurity
    fun p2pMatrixStore(httpClient: HttpClient, matrixNodes: List<String>): P2pMatrixStore

    // -- Matrix --

    fun matrixClient(httpClient: HttpClient): MatrixClient
}

internal fun DependencyRegistry.extend(): ExtendedDependencyRegistry =
    if (this is ExtendedDependencyRegistry) this
    else findExtended<P2pMatrixDependencyRegistry>() ?: P2pMatrixDependencyRegistry(this).also { addExtended(it) }