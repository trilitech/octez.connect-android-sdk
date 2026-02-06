package io.tezos.octezconnect.client.dapp.internal.storage.decorator

import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.DAppClientStoragePlugin
import io.tezos.octezconnect.client.dapp.internal.storage.plugin.ExtendedDAppClientStoragePlugin

internal class DecoratedDAppClientStoragePlugin(
    private val plugin: DAppClientStoragePlugin,
    private val beaconConfiguration: BeaconConfiguration,
) : ExtendedDAppClientStoragePlugin, DAppClientStoragePlugin by plugin {

    override suspend fun removeActiveAccount() {
        setActiveAccount(null)
    }

    override suspend fun removeActivePeer() {
        setActivePeer(null)
    }

    override fun scoped(beaconScope: BeaconScope): ExtendedDAppClientStoragePlugin = DecoratedDAppClientStoragePlugin(plugin.scoped(beaconScope), beaconConfiguration)
    override fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStoragePlugin = this
}