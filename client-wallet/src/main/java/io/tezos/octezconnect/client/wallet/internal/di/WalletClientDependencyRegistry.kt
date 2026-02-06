package io.tezos.octezconnect.client.wallet.internal.di

import io.tezos.octezconnect.client.wallet.BeaconWalletClient
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.utils.app
import io.tezos.octezconnect.core.internal.utils.beaconSdk

internal class WalletClientDependencyRegistry(dependencyRegistry: DependencyRegistry) : ExtendedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- client --

    private var walletClient: BeaconWalletClient? = null
    override fun walletClient(connections: List<Connection>, configuration: BeaconConfiguration): BeaconWalletClient =
        walletClient ?: BeaconWalletClient(
            app(beaconScope),
            beaconSdk.beaconId(beaconScope),
            beaconScope,
            connectionController(connections),
            messageController,
            storageManager,
            crypto,
            serializer,
            configuration,
            identifierCreator
        ).also { walletClient = it }
}