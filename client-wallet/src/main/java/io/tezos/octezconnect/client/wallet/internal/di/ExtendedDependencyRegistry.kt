package io.tezos.octezconnect.client.wallet.internal.di

import io.tezos.octezconnect.client.wallet.BeaconWalletClient
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.di.findExtended

internal interface ExtendedDependencyRegistry : DependencyRegistry {

    // -- client --

    fun walletClient(connections: List<Connection>, configuration: BeaconConfiguration): BeaconWalletClient
}

internal fun DependencyRegistry.extend(): ExtendedDependencyRegistry =
    if (this is ExtendedDependencyRegistry) this
    else findExtended<WalletClientDependencyRegistry>() ?: WalletClientDependencyRegistry(this).also { addExtended(it) }