package io.tezos.octezconnect.client.dapp.internal.storage.plugin

import io.tezos.octezconnect.client.dapp.data.PairedAccount
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.storage.StoragePlugin
import io.tezos.octezconnect.client.dapp.internal.storage.decorator.DecoratedDAppClientStoragePlugin

public interface DAppClientStoragePlugin : StoragePlugin {
    public suspend fun getActiveAccount(): PairedAccount?
    public suspend fun setActiveAccount(account: PairedAccount?)

    public suspend fun getActivePeer(): String?
    public suspend fun setActivePeer(peerId: String?)

    override fun scoped(beaconScope: BeaconScope): DAppClientStoragePlugin
    public fun extend(beaconConfiguration: BeaconConfiguration): ExtendedDAppClientStoragePlugin = DecoratedDAppClientStoragePlugin(this, beaconConfiguration)
}