package io.tezos.octezconnect.client.dapp.internal.di

import io.tezos.octezconnect.client.dapp.BeaconDAppClient
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.di.DependencyRegistry
import io.tezos.octezconnect.core.internal.utils.app
import io.tezos.octezconnect.core.internal.utils.beaconSdk
import io.tezos.octezconnect.core.internal.utils.delegate.lazyWeak
import io.tezos.octezconnect.client.dapp.internal.controller.account.AccountController
import io.tezos.octezconnect.client.dapp.internal.controller.account.store.AccountControllerStore
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.DAppClientStoragePlugin

internal class DAppClientDependencyRegistry(dependencyRegistry: DependencyRegistry) : ExtendedDependencyRegistry, DependencyRegistry by dependencyRegistry {

    // -- client --

    private var dAppClient: BeaconDAppClient? = null
    override fun dAppClient(storagePlugin: DAppClientStoragePlugin, connections: List<Connection>, configuration: BeaconConfiguration): BeaconDAppClient {
        with(storageManager) {
            if (!hasPlugin<DAppClientStoragePlugin>()) addPlugins(storagePlugin.scoped(beaconScope).extend(configuration))
        }

        return dAppClient ?: BeaconDAppClient(
            app(beaconScope),
            beaconSdk.beaconId(beaconScope),
            beaconScope,
            connectionController(connections),
            messageController,
            accountController,
            storageManager,
            crypto,
            serializer,
            identifierCreator,
            configuration,
        ).also { dAppClient = it }
    }

    // -- account --

    override val accountController: AccountController by lazyWeak { AccountController(accountControllerStore, blockchainRegistry) }
    override val accountControllerStore: AccountControllerStore by lazyWeak { AccountControllerStore(storageManager) }
}