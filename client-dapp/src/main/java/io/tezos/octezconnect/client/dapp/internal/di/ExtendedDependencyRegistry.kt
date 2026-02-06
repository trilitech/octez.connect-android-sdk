package io.tezos.octezconnect.client.dapp.internal.di

import io.tezos.octezconnect.client.dapp.BeaconDAppClient
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.di.findExtended
import io.tezos.octezconnect.client.dapp.internal.controller.account.AccountController
import io.tezos.octezconnect.client.dapp.internal.controller.account.store.AccountControllerStore
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.DAppClientStoragePlugin

internal interface ExtendedDependencyRegistry : DependencyRegistry {

    // -- client --

    fun dAppClient(storagePlugin: DAppClientStoragePlugin, connections: List<Connection>, configuration: BeaconConfiguration): BeaconDAppClient

    // -- controller --

    val accountController: AccountController
    val accountControllerStore: AccountControllerStore
}

internal fun DependencyRegistry.extend(): ExtendedDependencyRegistry =
    if (this is ExtendedDependencyRegistry) this
    else findExtended<DAppClientDependencyRegistry>() ?: DAppClientDependencyRegistry(this).also { addExtended(it) }